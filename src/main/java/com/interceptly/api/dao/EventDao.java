package com.interceptly.api.dao;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
public class EventDao {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @NotNull
    @Column(name = "id",updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type= "uuid-char")
    private UUID id;

    @NotNull
    @Size(max=16777215)
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

    @Column(name = "environment", updatable = false, columnDefinition = "VARCHAR(15)",length = 15)
    @Size(max = 15)
    private String environment;

    @Column(name = "package_name", updatable = false, columnDefinition = "VARCHAR(30)", length = 15)
    @Size(max = 30)
    private String packageName;

    @Column(name = "package_version",updatable = false, columnDefinition = "VARCHAR(30)", length = 30)
    @Size(max = 30)
    private String packageVersion;

    @Column(name = "created_at", updatable = false,columnDefinition = "DATE")
    @CreatedDate
    private Date createdAt;

    @NotNull
    @Column(name = "issue_id", updatable = false, nullable = false, columnDefinition = "INT UNSIGNED")
    private Integer issueId;

    @Column(name = "name", updatable = false, columnDefinition = "TEXT", length = 65535)
    @Size(max = 65535)
    private String name;

    @NotNull
    @Column(name= "type", updatable = false, nullable = false, columnDefinition = "VARCHAR(100)",length = 100)
    @Size(max = 100)
    private String type;

    @Column(name= "version", updatable = false, columnDefinition = "VARCHAR(15)", length = 15)
    @Size(max = 15)
    private String version;

    @Column(name= "user_agent", updatable = false, columnDefinition = "TEXT")
    private String userAgent;

    @Transient
    private Map<String, Object> tags;
//    @JoinColumn(name = "issue_id", nullable = false)
//    @ManyToOne
//    @OnDelete(action= OnDeleteAction.CASCADE)
//    @JsonIgnore
//    private IssueDao issue;
}
