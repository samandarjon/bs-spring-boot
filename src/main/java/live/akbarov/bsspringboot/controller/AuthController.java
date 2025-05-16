package live.akbarov.bsspringboot.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import live.akbarov.bsspringboot.dto.AuthRequest;
import live.akbarov.bsspringboot.dto.AuthResponse;
import live.akbarov.bsspringboot.dto.RegisterRequest;
import live.akbarov.bsspringboot.entity.UserEntity;
import live.akbarov.bsspringboot.security.JwtTokenUtil;
import live.akbarov.bsspringboot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        // Check if username or email already exists
        if (userService.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body(new AuthResponse(null, "Username already exists"));
        }
        if (userService.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(new AuthResponse(null, "Email already exists"));
        }

        // For regular registration, always use USER role
        UserEntity.Role role = UserEntity.Role.USER;

        // Register the user
        UserEntity user = userService.registerUser(
                request.getUsername(),
                request.getPassword(),
                request.getEmail(),
                role
        );

        // Generate token
        String token = jwtTokenUtil.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token, "User registered successfully"));
    }

    @PostMapping("/admin/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthResponse> registerAdmin(@Valid @RequestBody RegisterRequest request) {
        // Check if username or email already exists
        if (userService.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body(new AuthResponse(null, "Username already exists"));
        }
        if (userService.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(new AuthResponse(null, "Email already exists"));
        }

        // Use the role from the request, or default to ADMIN if not specified
        UserEntity.Role role = request.getRole() != null ? request.getRole() : UserEntity.Role.ADMIN;

        // Register the user with the specified role
        UserEntity user = userService.registerUser(
                request.getUsername(),
                request.getPassword(),
                request.getEmail(),
                role
        );

        return ResponseEntity.ok(new AuthResponse(null, "User registered with role " + role + " successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtTokenUtil.generateToken(userDetails);
            return ResponseEntity.ok(new AuthResponse(token, "Login successful"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new AuthResponse(null, "Invalid username or password"));
        }
    }

    @GetMapping("/oauth2/success")
    public ResponseEntity<AuthResponse> oauth2Success(@AuthenticationPrincipal OAuth2User oauth2User) {
        // Extract user information from OAuth2 authentication
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");

        // Check if user exists, if not, register them
        UserEntity user;
        if (!userService.existsByEmail(email)) {
            // Generate a username based on email or name
            String username = email != null ? email.split("@")[0] : name.toLowerCase().replace(" ", "");
            // Generate a random password (user will authenticate via OAuth2)
            String password = java.util.UUID.randomUUID().toString();

            // Register the user with USER role
            user = userService.registerUser(username, password, email, UserEntity.Role.USER);
        } else {
            // Get existing user
            user = userService.findByUsername(email.split("@")[0]).orElseThrow();
        }

        // Generate JWT token
        String token = jwtTokenUtil.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token, "OAuth2 login successful"));
    }

    @GetMapping("/oauth2/failure")
    public ResponseEntity<AuthResponse> oauth2Failure() {
        return ResponseEntity.badRequest().body(new AuthResponse(null, "OAuth2 authentication failed"));
    }
}
