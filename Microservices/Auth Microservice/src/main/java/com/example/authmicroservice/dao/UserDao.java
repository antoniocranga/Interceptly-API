package com.example.authmicroservice.dao;

import com.example.authmicroservice.util.enums.ProviderEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class UserDao{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "first_name", columnDefinition = "VARCHAR(20)")
    @Size(max = 20)
    private String firstName;

    @Column(name = "last_name", columnDefinition = "VARCHAR(20)")
    @Size(max = 20)
    private String lastName;

    @Column(name = "username", columnDefinition = "VARCHAR(100)")
    @Size(max = 100)
    private String username;

    @Column(name = "email", unique = true, columnDefinition = "VARCHAR(100)")
    @Size(max = 100)
    @Email
    private String email;

    @Column(name = "verified", columnDefinition = "TINYINT(1) DEFAULT 0")
    private Integer verified = 0;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "password", columnDefinition = "VARCHAR(100)")
    @Size(max = 100)
    @JsonIgnore
    private String password;

    @Column(name = "provider", columnDefinition = "INT")
    @Enumerated(EnumType.ORDINAL)
    private ProviderEnum provider;
//    @OneToMany(mappedBy = "user")
//    @JsonManagedReference(value = "user-permissions")
//    private List<PermissionDao> permissions;
}
