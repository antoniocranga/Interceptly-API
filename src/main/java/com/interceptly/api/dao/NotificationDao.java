package com.interceptly.api.dao;

import com.interceptly.api.util.enums.NotificationTypeEnum;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@Data
@Table(name = "notifications")
public class NotificationDao extends BaseEntity {
    @Column(name = "type", nullable = false, columnDefinition = "INT DEFAULT 0")
    @Enumerated(EnumType.ORDINAL)
    private NotificationTypeEnum type;

    @Column(name = "seen", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean seen;

    @Column(name = "message", columnDefinition = "VARCHAR(300)")
    private String message;
    @Column(name = "redirect_url", columnDefinition = "VARCHAR(300)")
    private String redirectUrl;

    @Column(name = "sent_to", columnDefinition = "INT UNSIGNED")
    private Integer sentTo;

    @Column(name = "sent_by", columnDefinition = "INT UNSIGNED")
    private Integer sentBy;
}
