package com.interceptly.api.dto.post;

import com.interceptly.api.dto.base.Project;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class ProjectPostDto extends Project {

    public ProjectPostDto(String title, String description, String color) {
        super(title, description, color);
    }
}