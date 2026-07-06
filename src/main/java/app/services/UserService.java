package app.services;

import java.util.List;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.Charsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.transaction.annotation.Transactional;
import app.Application;
import app.configs.ApplicationConfig;
import app.exceptions.DuplicateResourceException;
import app.exceptions.ResourceNotFoundException;
import app.models.dto.UserDto;
import app.models.dto.UserRegisterRequest;
import app.models.dto.UserResponseDto;
import app.models.dto.UserUpdateRequest;
import app.models.entity.User;
import app.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {

    private static final int INVALID_ACTIVATION_LENGTH = 5;

    private final ApplicationConfig config;

    private final UserRepository repo;

    private final HttpSession httpSession;

    private static final String CURRENT_USER_KEY = "CURRENT_USER";

    public UserService(final ApplicationConfig config, final UserRepository repo, final HttpSession httpSession) {
        this.config = config;
        this.repo = repo;
        this.httpSession = httpSession;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) {

        final User user = repo.findOneByUserNameOrEmail(username, username);

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        if (config.isUserVerification() && !user.getToken().equals("1")) {
            Application.LOGGER.error("User [{}] tried to login but is not activated", username);
            throw new UsernameNotFoundException(username + " has not been activated yet");
        }
        httpSession.setAttribute(CURRENT_USER_KEY, user);
        final List<GrantedAuthority> auth = AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRole());

        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), auth);
    }

    public void autoLogin(final User user) {

        autoLogin(user.getUserName());
    }

    public void autoLogin(final String username) {

        final UserDetails userDetails = this.loadUserByUsername(username);
        final UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);
        if (auth.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
    }

    public User register(final UserDto userDto) {

        final User user = userDto.toEntity();
        user.setPassword(encodeUserPassword(user.getPassword()));

        if (this.repo.findOneByUserName(user.getUserName()) == null && this.repo.findOneByEmail(user.getEmail()) == null) {
            final String activation = createActivationToken(user, false);
            user.setToken(activation);
            this.repo.save(user);
            return user;
        }

        return null;
    }

    public String encodeUserPassword(final String password) {

        final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    public Boolean delete(final Long id) {

        this.repo.deleteById(id);
        return true;
    }

    public User activate(final String activation) {

        if ("1".equals(activation) || activation.length() < INVALID_ACTIVATION_LENGTH) {
            return null;
        }
        final User u = this.repo.findOneByToken(activation);
        if (u != null) {
            u.setToken("1");
            this.repo.save(u);
            return u;
        }
        return null;
    }

    public String createActivationToken(final User user, final boolean save) {

        final String toEncode = user.getUserName() + config.getSecret();
        final String activationToken = DigestUtils.md5DigestAsHex(toEncode.getBytes(Charsets.UTF_8));
        if (save) {
            user.setToken(activationToken);
            this.repo.save(user);
        }
        return activationToken;
    }

    public String createResetPasswordToken(final User user, final boolean save) {

        final String toEncode = user.getEmail() + config.getSecret();
        final String resetToken = DigestUtils.md5DigestAsHex(toEncode.getBytes(Charsets.UTF_8));
        if (save) {
            user.setToken(resetToken);
            this.repo.save(user);
        }
        return resetToken;
    }

    public User resetActivation(final String email) {

        final User u = this.repo.findOneByEmail(email);
        if (u != null) {
            createActivationToken(u, true);
            return u;
        }
        return null;
    }

    public Boolean resetPassword(final UserDto userDto) {

        final User u = this.repo.findOneByUserName(userDto.getUserName());
        if (u != null) {
            u.setPassword(encodeUserPassword(userDto.getPassword()));
            u.setToken("1");
            this.repo.save(u);
            return true;
        }
        return false;
    }

    public void updateUser(final UserDto user) {

        updateUser(user.getUserName(), user);
    }

    public void updateUser(final String userName, final UserDto newData) {

        this.repo.updateUser(
                userName,
                newData.getEmail(),
                newData.getFirstName(),
                newData.getLastName(),
                newData.getAddress(),
                newData.getCompanyName());
    }

    @Transactional
    public UserResponseDto updateUser(final Long id, final UserUpdateRequest userDetails) {
        final User existingUser = this.repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        final User userWithEmail = this.repo.findOneByEmail(userDetails.getEmail());
        if (userWithEmail != null && !userWithEmail.getId().equals(id)) {
            throw new DuplicateResourceException("Email address already in use: " + userDetails.getEmail());
        }

        existingUser.setEmail(userDetails.getEmail());
        existingUser.setFirstName(userDetails.getFirstName());
        existingUser.setLastName(userDetails.getLastName());
        existingUser.setAddress(userDetails.getAddress());
        existingUser.setCompanyName(userDetails.getCompanyName());

        final User updatedUser = this.repo.save(existingUser);
        return new UserResponseDto(updatedUser);
    }

    public User getLoggedInUser() {

        return getLoggedInUser(false);
    }

    public User getLoggedInUser(final boolean forceFresh) {

        final String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = (User) httpSession.getAttribute(CURRENT_USER_KEY);
        if (forceFresh || httpSession.getAttribute(CURRENT_USER_KEY) == null) {
            user = this.repo.findOneByUserName(userName);
            httpSession.setAttribute(CURRENT_USER_KEY, user);
        }
        return user;
    }

    public void updateLastLogin(final String userName) {

        this.repo.updateLastLogin(userName);
    }

    public void updateProfilePicture(final User user, final String profilePicture) {

        this.repo.updateProfilePicture(user.getUserName(), profilePicture);
    }

    public UserResponseDto getUserById(final Long id) {
        final User user = this.repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return new UserResponseDto(user);
    }

    @Transactional
    public UserResponseDto createUser(final UserRegisterRequest userDetails) {
        if (this.repo.findOneByUserName(userDetails.getUserName()) != null) {
            throw new DuplicateResourceException("Username already exists: " + userDetails.getUserName());
        }
        if (this.repo.findOneByEmail(userDetails.getEmail()) != null) {
            throw new DuplicateResourceException("Email already exists: " + userDetails.getEmail());
        }

        final User user = userDetails.toEntity();
        user.setPassword(encodeUserPassword(user.getPassword()));
        
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("ROLE_USER");
        }

        final String activationToken = createActivationToken(user, false);
        user.setToken(activationToken);
        
        final User savedUser = this.repo.save(user);
        return new UserResponseDto(savedUser);
    }

    @Transactional
    public void deleteUserById(final Long id) {
        if (!this.repo.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        this.repo.deleteById(id);
    }

    public Page<UserResponseDto> getAllUsers(final Pageable pageable) {
        return this.repo.findAll(pageable).map(UserResponseDto::new);
    }

    public Page<UserResponseDto> searchUsers(final String email, final String name, final Pageable pageable) {
        final org.springframework.data.jpa.domain.Specification<User> spec = (root, query, cb) -> {
            final java.util.List<javax.persistence.criteria.Predicate> predicates = new java.util.ArrayList<>();
            
            if (email != null && !email.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
            }
            
            if (name != null && !name.trim().isEmpty()) {
                final javax.persistence.criteria.Predicate nameLike = cb.like(cb.lower(root.get("userName")), "%" + name.toLowerCase() + "%");
                final javax.persistence.criteria.Predicate firstNameLike = cb.like(cb.lower(root.get("firstName")), "%" + name.toLowerCase() + "%");
                final javax.persistence.criteria.Predicate lastNameLike = cb.like(cb.lower(root.get("lastName")), "%" + name.toLowerCase() + "%");
                predicates.add(cb.or(nameLike, firstNameLike, lastNameLike));
            }
            
            return cb.and(predicates.toArray(new javax.persistence.criteria.Predicate[0]));
        };
        
        return this.repo.findAll(spec, pageable).map(UserResponseDto::new);
    }
}