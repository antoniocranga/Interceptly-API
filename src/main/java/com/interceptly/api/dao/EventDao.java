package com.interceptly.api.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;


import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Table(name = "events")
public class EventDao {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(name = "id",updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type= "uuid-char")
    private UUID id;

    @Column(name = "message", updatable = false, columnDefinition = "MEDIUMTEXT")
    private String message;

    @Column(name = "stack_trace", updatable = false, columnDefinition = "LONGTEXT")
    private String stackTrace;

    @Column(name = "body", updatable = false, columnDefinition = "TEXT")
    private String body;

    @Column(name = "headers", updatable = false, columnDefinition = "TEXT")
    private String headers;

    @Column(name = "environment", updatable = false, columnDefinition = "VARCHAR(15)")
    private String environment;

    @Column(name = "package_name", updatable = false, columnDefinition = "VARCHAR(30)")
    private String packageName;

    @Column(name = "package_version",updatable = false, columnDefinition = "VARCHAR(30)")
    private String packageVersion;

    @Column(name = "created_at", updatable = false,columnDefinition = "DATE")
    @CreatedDate
    private Date createdAt;

    @Column(name = "browser", updatable = false, columnDefinition = "TEXT")
    private String browser;

    @Column(name = "client_os", updatable = false, columnDefinition = "TEXT")
    private String clientOs;

    @Column(name = "issue_id", updatable = false, nullable = false, columnDefinition = "INT UNSIGNED")
    private Integer issueId;

//    @JoinColumn(name = "issue_id", nullable = false)
//    @ManyToOne
//    @OnDelete(action= OnDeleteAction.CASCADE)
//    @JsonIgnore
//    private IssueDao issue;
}
