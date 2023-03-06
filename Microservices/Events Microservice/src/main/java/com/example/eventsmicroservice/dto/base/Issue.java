package com.example.eventsmicroservice.dto.base;

import com.example.eventsmicroservice.dao.IssueDao;
import com.example.eventsmicroservice.utils.enums.IssueStatusEnum;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;

@MappedSuperclass
@EqualsAndHashCode
@Data
public abstract class Issue {
    private Boolean isBookmarked;
    private IssueStatusEnum status;

    public IssueDao toDao(IssueDao issue) {
        if (isBookmarked != null) {
            issue.setIsBookmarked(isBookmarked);
        }
        if (status != null) {
            issue.setStatus(status);
        }
        return issue;
    }
}
