package com.manajemen.asset.auth;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manajemen.asset.dto.ChangePasswordRequest;
import com.manajemen.asset.dto.UserDto;
import com.manajemen.asset.entity.CustomUserDetails;
import com.manajemen.asset.entity.User;
import com.manajemen.asset.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/users")
public class AuthController {
    @Autowired
    AuthenticationManager authManager;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UserService userService;

    @Operation(summary = "User login to get access token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email and access token", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class)) }),
            @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequest request) {
        try {
            Authentication authentication = authManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long userId = userDetails.getUserId(); // Dapatkan userId dari CustomUserDetails
            String accessToken = jwtUtil.generateAccessToken(authentication, userId);
            AuthResponse response = new AuthResponse(request.getEmail(), accessToken);
            return ResponseEntity.ok().body(response);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @Operation(summary = "Register a new user to create an account for login.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully, returns user details", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDto request) {
        UserDto user = userService.createUser(request);
        return ResponseEntity.ok().body(user);
    }

    @Operation(summary = "Get user profile by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns user profile data", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserProfile(@PathVariable Long userId,
            @RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7); // Hapus "Bearer " dari awal token
        Long userIdFromToken = jwtUtil.extractUserId(jwtToken);
        if (!userId.equals(userIdFromToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        UserDto userProfile = userService.getUserProfile(userId);
        return ResponseEntity.ok(userProfile);
    }

    @Operation(summary = "Update user profile by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUserProfile(@PathVariable Long userId, @RequestBody UserDto userDto,
            @RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7); // Hapus "Bearer " dari awal token
        Long userIdFromToken = jwtUtil.extractUserId(jwtToken);
        if (!userId.equals(userIdFromToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        User updatedUser = userService.updateUserProfile(userId, userDto);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Change user password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid old password", content = @Content)
    })
    @PostMapping("/{userId}/change-password")
    public ResponseEntity<String> changePassword(@PathVariable Long userId,
            @RequestBody ChangePasswordRequest passwordRequest, @RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7); // Hapus "Bearer " dari awal token
        Long userIdFromToken = jwtUtil.extractUserId(jwtToken);
        if (!userId.equals(userIdFromToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        boolean isChanged = userService.changePassword(userId, passwordRequest);
        if (isChanged) {
            return ResponseEntity.ok("Password changed successfully");
        } else {
            return ResponseEntity.badRequest().body("Invalid old password");
        }
    }

    @Operation(summary = "Delete user by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long userId,
            @RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7); // Hapus "Bearer " dari awal token
        Long userIdFromToken = jwtUtil.extractUserId(jwtToken);
        if (!userId.equals(userIdFromToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        userService.deleteUserById(userId);
        return ResponseEntity.ok("User deleted successfully");
    }
}
