package app.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import app.models.entity.User;

@Schema(description = "Registration request body")
public class UserRegisterRequest {

    private static final int MIN_CHARS = 3;

    private static final int MAX_CHARS = 100;

    @Schema(description = "Unique username", example = "johndoe", required = true)
    @NotBlank(message = "Username is required")
    @Size(min = MIN_CHARS, max = MAX_CHARS, message = "Username must be between 3 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "Username must be alphanumeric and can contain '.', '_', or '-'")
    private String userName;

    @Schema(description = "Password", example = "securePassword123", required = true)
    @NotBlank(message = "Password is required")
    @Size(min = MIN_CHARS, max = MAX_CHARS, message = "Password must be at least 3 characters")
    private String password;

    @Schema(description = "Confirm password", example = "securePassword123", required = true)
    @NotBlank(message = "Password confirmation is required")
    private String confirmPassword;

    @Schema(description = "Email address", example = "john.doe@example.com", required = true)
    @NotBlank(message = "Email is required")
    @Email(message = "Email address is not valid")
    private String email;

    @Schema(description = "First name", example = "John")
    private String firstName;

    @Schema(description = "Last name", example = "Doe")
    private String lastName;

    @Schema(description = "Address", example = "123 Main St, New York, NY")
    private String address;

    @Schema(description = "Company name", example = "ACME Corp")
    private String companyName;

    public UserRegisterRequest() {
    }

    public User toEntity() {
        final User user = new User();
        user.setUserName(this.userName);
        user.setPassword(this.password);
        user.setConfirmPassword(this.confirmPassword);
        user.setEmail(this.email);
        user.setFirstName(this.firstName);
        user.setLastName(this.lastName);
        user.setAddress(this.address);
        user.setCompanyName(this.companyName);
        return user;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(final String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(final String companyName) {
        this.companyName = companyName;
    }
}
