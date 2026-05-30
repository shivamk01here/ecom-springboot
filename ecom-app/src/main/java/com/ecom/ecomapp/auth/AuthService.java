package com.ecom.ecomapp.auth;

import com.ecom.ecomapp.config.JwtUtil;
import com.ecom.ecomapp.config.UserPrincipal;
import com.ecom.ecomapp.user.Role;
import com.ecom.ecomapp.user.UserEntity;
import com.ecom.ecomapp.user.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadCredentialsException("Email already registered");
        }

        UserEntity user = new UserEntity(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getName(),
                Role.USER
        );

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole().name());
        return new LoginResponse(token, user.getEmail(), user.getName(), user.getRole().name());
    }

    public LoginResponse login(LoginRequest request) {
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole().name());
        return new LoginResponse(token, user.getEmail(), user.getName(), user.getRole().name());
    }

    public UserProfileResponse getProfile(UserPrincipal principal) {
        UserEntity user = userRepository.findById(principal.id())
                .orElseThrow(() -> new BadCredentialsException("User not found"));
        return UserProfileResponse.fromPrincipal(principal, user.getName());
    }

    public UserProfileResponse updateProfile(Long userId, UpdateProfileRequest request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new BadCredentialsException("User not found"));
        user.setName(request.getName());
        UserEntity updated = userRepository.save(user);
        return new UserProfileResponse(updated.getId(), updated.getEmail(), updated.getName(), updated.getRole().name());
    }

    public void changePassword(Long userId, ChangePasswordRequest request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new BadCredentialsException("User not found"));
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadCredentialsException("Incorrect current password");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
