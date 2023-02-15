package com.interceptly.api.dto.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.interceptly.api.dao.ProjectDao;
import com.interceptly.api.dto.patch.ProjectPatchDto;
import com.interceptly.api.dto.post.ProjectPostDto;
import lombok.*;


import java.util.UUID;
@Getter
@Setter
public abstract class Project {
    private String title;

    private String description;

    private String color;

    @JsonIgnore
    private Integer owner;

    public Project(String title, String description, String color){
        this.title = title;
        this.description = description;
        this.color = color;
    }

    public ProjectDao toProjectDao() {
        return ProjectDao.builder().
                title(title).
                owner(owner).
                description(description).
                color(color).
                apiKey(UUID.randomUUID()).
                build();
    }
}
