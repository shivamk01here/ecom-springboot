package com.ecom.ecomapp.auth;

import com.ecom.ecomapp.config.UserPrincipal;

public class UserProfileResponse {

    private Long id;
    private String email;
    private String name;
    private String role;

    public UserProfileResponse(Long id, String email, String name, String role) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.role = role;
    }

    public static UserProfileResponse fromPrincipal(UserPrincipal principal, String name) {
        return new UserProfileResponse(principal.id(), principal.email(), name, principal.role());
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getRole() { return role; }
}
