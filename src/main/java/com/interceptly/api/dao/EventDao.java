package com.interceptly.api.dao;

import lombok.*;
import net.minidev.json.JSONObject;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Entity
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "events")
@EntityListeners(AuditingEntityListener.class)
public class EventDao {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @NotNull
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    private UUID id;

    @NotNull
    @Size(max = 16777215)
    @Column(name = "message", updatable = false, nullable = false, columnDefinition = "MEDIUMTEXT", length = 16777215)
    private String message;

    @Column(name = "stack_trace", updatable = false, columnDefinition = "LONGTEXT")
    @Lob
    private String stackTrace;

    @Column(name = "body", updatable = false, columnDefinition = "TEXT", length = 65535)
    @Size(max = 65535)
    private String body;

    @Column(name = "headers", updatable = false, columnDefinition = "TEXT", length = 65535)
    @Size(max = 65535)
    private String headers;

    @Column(name = "environment", updatable = false, columnDefinition = "VARCHAR(15)", length = 15)
    @Size(max = 15)
    private String environment;

    @Column(name = "package_name", updatable = false, columnDefinition = "VARCHAR(30)", length = 15)
    @Size(max = 30)
    private String packageName;

    @Column(name = "package_version", updatable = false, columnDefinition = "VARCHAR(30)", length = 30)
    @Size(max = 30)
    private String packageVersion;

    @CreatedDate
    @NotNull
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "DATETIME")
    private LocalDateTime createdAt;

    @NotNull
    @Column(name = "issue_id", updatable = false, nullable = false, columnDefinition = "INT UNSIGNED")
    private Integer issueId;

    @Column(name = "name", updatable = false, columnDefinition = "TEXT", length = 65535)
    @Size(max = 65535)
    private String name;

    @NotNull
    @Column(name = "type", updatable = false, nullable = false, columnDefinition = "VARCHAR(100)", length = 100)
    @Size(max = 100)
    private String type;

    @Column(name = "version", updatable = false, columnDefinition = "VARCHAR(15)", length = 15)
    @Size(max = 15)
    private String version;

    @Column(name = "user_agent", updatable = false, columnDefinition = "TEXT")
    private String userAgent;

    @Column(name = "tags", updatable = false, columnDefinition = "TEXT")
    private String tags;

    @Column(name = "device_type", updatable = false, columnDefinition = "TEXT")
    private String deviceType;

    @Column(name = "browser_major_version", updatable = false, columnDefinition = "INT")
    private String browserMajorVersion;

    @Column(name = "browser", updatable = false, columnDefinition = "TEXT")
    private String browser;

    @Column(name = "platform_version", updatable = false, columnDefinition = "TEXT")
    private String platformVersion;

    @Column(name = "browser_type",updatable = false, columnDefinition = "TEXT")
    private String browserType;

    @Column(name = "project_id", updatable = false, columnDefinition = "INT")
    private Integer projectId;
//    @JoinColumn(name = "issue_id", nullable = false)
//    @ManyToOne
//    @OnDelete(action= OnDeleteAction.CASCADE)
//    @JsonIgnore
//    private IssueDao issue;
}
