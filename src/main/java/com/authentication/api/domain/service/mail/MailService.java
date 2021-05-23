package com.authentication.api.domain.service.mail;

import com.authentication.api.domain.exception.AuthenticationApiException;
import com.authentication.api.domain.model.NotificationEmail;
import lombok.AllArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * The type Mail service.
 */
@Service
@AllArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;
    private final MailContentBuilder mailContentBuilder;
    private static final String URL_BASE = "http://localhost:9090/api/auth/";

    /**
     * Set up email data.
     *
     * @param email the email
     * @param token the token
     */
    @Async
    public void setUpEmailData(String subject, String title, String email, String body, String endPoint, String token){
        NotificationEmail notificationEmail = NotificationEmail.builder()
                .subject(subject)
                .title(title)
                .recipient(email)
                .body(body+ URL_BASE + endPoint+"/"+token)
                .build();
        sendEmail(notificationEmail);
    }

    private void sendEmail(NotificationEmail notificationEmail) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("apiauth@email.com");
            messageHelper.setTo(notificationEmail.getRecipient());
            messageHelper.setSubject(notificationEmail.getSubject());
            messageHelper.setText(mailContentBuilder.build(notificationEmail.getTitle() + notificationEmail.getBody()));
        };
        try{
            javaMailSender.send(messagePreparator);
        }catch (MailException mailException){
            throw new AuthenticationApiException("Exception occurred when sending the mail to " + notificationEmail.getRecipient(), mailException);
        }
    }


}
