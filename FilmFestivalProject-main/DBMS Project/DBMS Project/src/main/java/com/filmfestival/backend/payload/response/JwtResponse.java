package com.filmfestival.backend.payload.response;

import lombok.Data;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Integer id;
    private String name;
    private String email;
    private String role;

    public JwtResponse(String accessToken, Integer id, String name, String email, String role) {
        this.token = accessToken;
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }
}
