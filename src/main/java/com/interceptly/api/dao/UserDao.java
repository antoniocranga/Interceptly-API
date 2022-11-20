package com.interceptly.api.dao;

import com.interceptly.api.util.ProviderEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.sql.Date;
import java.util.List;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Table(name = "users")
public class UserDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "timestamp")
    private Date timestamp;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "username")
    private String username;

    @Column(name = "email", unique = true)
    @Email
    private String email;

    @Column(name = "verified")
    private Integer verified;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "password")
    @JsonIgnore
    private String password;

    @Column(name = "provider")
    @Enumerated(EnumType.ORDINAL)
    private ProviderEnum provider;

    @ElementCollection
    @CollectionTable(name = "users_projects",joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "project_id")
    private Set<String> projects;
}
