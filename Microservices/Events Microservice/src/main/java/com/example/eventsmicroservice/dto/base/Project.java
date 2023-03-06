package com.example.eventsmicroservice.dto.base;

import com.example.eventsmicroservice.dao.ProjectDao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public abstract class Project {
    private String title;

    private String description;

    private String color;

    @JsonIgnore
    private Integer owner;

    public Project(String title, String description, String color) {
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
