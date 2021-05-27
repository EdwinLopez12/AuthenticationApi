package com.authentication.api.domain.service.mail;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * The Mail content builder.
 */
@Service
@AllArgsConstructor
public class MailContentBuilder {
    private final TemplateEngine templateEngine;

    /**
     * Inject the data to mail template
     *
     * @param message the message
     * @return the string
     */
    String build(String title, String message, String url){
        Context context = new Context();
        context.setVariable("title", title);
        context.setVariable("message", message);
        context.setVariable("url", url);
        return templateEngine.process("mailTemplate", context);
    }
}
