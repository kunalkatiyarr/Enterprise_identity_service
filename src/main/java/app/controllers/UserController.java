package app.controllers;

import javax.validation.Valid;
import app.models.dto.ApiResponse;
import app.models.dto.UserRegisterRequest;
import app.models.dto.UserResponseDto;
import app.models.dto.UserUpdateRequest;
import app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    // 1. Get All Users (Returns JSON list directly)
    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    // 2. Get Single User by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserById(@PathVariable final Long id) {
        final UserResponseDto user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", user));
    }

    // 3. Create / Register New User
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDto>> createUser(@Valid @RequestBody final UserRegisterRequest userDetails) {
        final UserResponseDto savedUser = userService.createUser(userDetails);
        return ResponseEntity.ok(ApiResponse.success("User created successfully", savedUser));
    }

    // 4. Update Existing User
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateUser(@PathVariable final Long id, @Valid @RequestBody final UserUpdateRequest userDetails) {
        final UserResponseDto updatedUser = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(ApiResponse.success("User updated successfully", updatedUser));
    }

    // 5. Delete User
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.ok("User deleted successfully with ID: " + id);
    }
}