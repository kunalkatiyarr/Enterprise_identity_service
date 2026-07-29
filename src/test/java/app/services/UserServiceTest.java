package app.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import app.configs.ApplicationConfig;
import app.exceptions.DuplicateResourceException;
import app.models.dto.UserRegisterRequest;
import app.models.dto.UserResponseDto;
import app.models.entity.User;
import app.repositories.UserRepository;
import javax.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UserServiceTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpSession httpSession;

    @Mock
    private ApplicationConfig config;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        userService = new UserService(config, userRepository, httpSession);
    }

    @Test
    public void testCreateUser_Success() {
        final UserRegisterRequest request = new UserRegisterRequest();
        request.setUserName("testuser");
        request.setEmail("test@example.com");
        request.setPassword("password");
        request.setConfirmPassword("password");

        when(userRepository.findOneByUserName("testuser")).thenReturn(null);
        when(userRepository.findOneByEmail("test@example.com")).thenReturn(null);
        
        final User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUserName("testuser");
        savedUser.setEmail("test@example.com");
        
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        final UserResponseDto result = userService.createUser(request);

        assertNotNull(result);
        assertEquals("testuser", result.getUserName());
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    public void testCreateUser_DuplicateUserName() {
        final UserRegisterRequest request = new UserRegisterRequest();
        request.setUserName("testuser");
        request.setEmail("test@example.com");

        when(userRepository.findOneByUserName("testuser")).thenReturn(new User());

        assertThrows(DuplicateResourceException.class, () -> {
            userService.createUser(request);
        });
    }
}
