package com.authentication.api.domain.model;

import lombok.*;

/**
 * The Notification email.
 * Used to define a model for sending emails..
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class NotificationEmail {
    private String subject;
    private String title;
    private String recipient;
    private String body;
}
