package com.interceptly.api.dto.base;

import com.interceptly.api.dao.ProjectDao;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.MappedSuperclass;
import java.util.Optional;
import java.util.UUID;

@MappedSuperclass
@EqualsAndHashCode
@Data
public abstract class Project {
    private String title;

    private String description;

    private String base64;

    public ProjectDao toProjectDao(Integer owner) {
        return ProjectDao.builder().
                title(title).
                description(description).
                owner(owner).
                apiKey(UUID.randomUUID()).
                build();
    }
}
