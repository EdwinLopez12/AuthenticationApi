package com.authentication.api.domain.model;

import lombok.*;

/**
 * The Notification email.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class NotificationEmail {
    private String subject;
    private String recipient;
    private String body;
}
