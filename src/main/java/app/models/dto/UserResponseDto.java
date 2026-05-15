package app.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import app.models.entity.User;

@Schema(description = "User details response")
public class UserResponseDto {

    @Schema(description = "Database generated ID", example = "1")
    private Long id;

    @Schema(description = "Unique username", example = "johndoe")
    private String userName;

    @Schema(description = "Email address", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Role of the user", example = "ROLE_USER")
    private String role;

    @Schema(description = "First name", example = "John")
    private String firstName;

    @Schema(description = "Last name", example = "Doe")
    private String lastName;

    @Schema(description = "Address", example = "123 Main St, New York, NY")
    private String address;

    @Schema(description = "Company name", example = "ACME Corp")
    private String companyName;

    @Schema(description = "Last login timestamp", example = "2026-08-04T19:22:22Z")
    private String lastLogin;

    @Schema(description = "URL to profile picture", example = "users/johndoe/profile.jpg")
    private String profilePicture;

    @Schema(description = "Timestamp when the record was created")
    private Instant createdAt;

    @Schema(description = "Timestamp when the record was last updated")
    private Instant updatedAt;

    public UserResponseDto() {
    }

    public UserResponseDto(final User user) {
        if (user != null) {
            this.id = user.getId();
            this.userName = user.getUserName();
            this.email = user.getEmail();
            this.role = user.getRole();
            this.firstName = user.getFirstName();
            this.lastName = user.getLastName();
            this.address = user.getAddress();
            this.companyName = user.getCompanyName();
            this.lastLogin = user.getLastLogin();
            this.profilePicture = user.getProfilePicture();
            this.createdAt = user.getCreationTime();
            this.updatedAt = user.getLastModified();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(final String role) {
        this.role = role;
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

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(final String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(final String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(final Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
