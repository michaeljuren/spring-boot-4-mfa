package com.michaeljuren.mfa.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean mfaEnabled = false;

    // The secret key for TOTP generation
    @Column(name = "mfa_secret")
    private String mfaSecret;

    @Column(nullable = false)
    private boolean enabled = true;

    @Column(nullable = false)
    private String role = "USER";

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
