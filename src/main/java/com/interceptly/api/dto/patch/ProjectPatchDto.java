package com.interceptly.api.dto.patch;

import com.interceptly.api.dto.base.Project;
import lombok.*;


@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class ProjectPatchDto extends Project {
    public ProjectPatchDto(String title, String description, String color, Integer owner){
        super(title,description,color);
        this.setOwner(owner);
    }
}
