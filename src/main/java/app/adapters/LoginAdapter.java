package app.adapters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import app.services.UserService;

@Configuration
public class LoginAdapter implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginAdapter.class);

    private final UserService userService;

    public LoginAdapter(final UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onApplicationEvent(final InteractiveAuthenticationSuccessEvent event) {
        final String username = event.getAuthentication().getName();
        LOGGER.info("Successful login event for user: {}", username);
        userService.updateLastLogin(username);
    }
}
