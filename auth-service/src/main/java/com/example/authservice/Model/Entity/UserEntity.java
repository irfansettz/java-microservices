package com.example.authservice.Model.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Index;

import java.sql.Timestamp;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users", schema = "security")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Index(name = "user_idx", columnNames = {"id", "username", "email", "password", "active", "token", "refreshToken"})
    private Long id;

    @Column(unique = true)
    @NotNull
    private String username;

    @Column(unique = true)
    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    @Column(name = "is_active")
    private boolean active = true;

    @Column(name = "refresh_token", length = 3000)
    private String refreshToken;

    @CreationTimestamp
    @Column(name = "created_on")
    private Timestamp createdOn;

    @UpdateTimestamp
    @Column(name = "last_updated_on")
    private Timestamp lastUpdatedOn;
}
