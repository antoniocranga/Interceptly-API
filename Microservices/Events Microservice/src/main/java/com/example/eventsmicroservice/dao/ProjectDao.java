package com.example.eventsmicroservice.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@Data
@Table(name = "projects")
public class ProjectDao extends BaseEntity {

    @Column(name = "title", columnDefinition = "VARCHAR(50)")
    @Size(max = 50)
    private String title;

    @Column(name = "description", columnDefinition = "VARCHAR(150)")
    @Size(max = 150)
    private String description;

    @Column(name = "color", columnDefinition = "VARCHAR(7) default '#2196f3' ")
    @Size(max = 7)
    private String color;

    @Column(name = "api_key", unique = true, columnDefinition = "VARCHAR(36)", nullable = false)
    @NotNull
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID apiKey;

    @Column(name = "owner", columnDefinition = "INT UNSIGNED", nullable = false)
    @JsonIgnore
    private Integer owner;
}
