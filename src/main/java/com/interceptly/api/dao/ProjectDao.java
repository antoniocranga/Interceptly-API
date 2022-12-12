package com.interceptly.api.dao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "projects")
public class ProjectDao extends BaseEntity{

    @Column(name="title")
    private String title;

    @Column(name="description")
    private String description;

    @Column(name="image_url")
    private String imageUrl;

    @Column(name="api_key")
    private String apiKey;

    @Column(name="owner")
    @JsonIgnore
    private Integer owner;
}
