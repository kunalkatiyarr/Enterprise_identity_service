package app.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import app.models.dto.ApiResponse;
import app.models.dto.UserRegisterRequest;
import app.models.dto.UserResponseDto;
import app.models.dto.UserUpdateRequest;
import app.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User Management", description = "REST APIs for user profile lookup, registration, modification, deletion, and searches")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final int DEFAULT_PAGE_SIZE = 10;

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    // 1. Get All Users (Returns JSON list directly)
    @Operation(summary = "Get paginated list of all users")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserResponseDto>>> getAllUsers(
            @PageableDefault(size = DEFAULT_PAGE_SIZE, sort = "userName") final Pageable pageable) {
        final Page<UserResponseDto> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
    }

    // 2. Get Single User by ID
    @Operation(summary = "Get single user by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserById(@PathVariable final Long id) {
        final UserResponseDto user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", user));
    }

    // 3. Create / Register New User
    @Operation(summary = "Register a new user")
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDto>> createUser(@Valid @RequestBody final UserRegisterRequest userDetails) {
        final UserResponseDto savedUser = userService.createUser(userDetails);
        return ResponseEntity.ok(ApiResponse.success("User created successfully", savedUser));
    }

    // 4. Update Existing User
    @Operation(summary = "Update an existing user's details")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateUser(@PathVariable final Long id, @Valid @RequestBody final UserUpdateRequest userDetails) {
        final UserResponseDto updatedUser = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(ApiResponse.success("User updated successfully", updatedUser));
    }

    // 5. Delete User
    @Operation(summary = "Delete user by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable final Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully with ID: " + id));
    }

    // 6. Search Users Dynamically
    @Operation(summary = "Search users dynamically with dynamic query filters")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<UserResponseDto>>> searchUsers(
            @RequestParam(required = false) final String email,
            @RequestParam(required = false) final String name,
            @PageableDefault(size = DEFAULT_PAGE_SIZE, sort = "userName") final Pageable pageable) {
        final Page<UserResponseDto> users = userService.searchUsers(email, name, pageable);
        return ResponseEntity.ok(ApiResponse.success("Users matching search criteria retrieved successfully", users));
    }
}