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

    /**
     * Set up email data.
     *
     * @param email the email
     * @param token the token
     */
    @Async
    public void setUpEmailData(String email, String token){
        sendEmail(
                new NotificationEmail(
                    "Please active your account",
                    email,
                    "Thanks to join us. "+
                            "Please click on the below url to active your account "+
                            "http://localhost:9090/api/auth/account-verification/"+token
                )
        );
    }

    private void sendEmail(NotificationEmail notificationEmail) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("apiauth@email.com");
            messageHelper.setTo(notificationEmail.getRecipient());
            messageHelper.setSubject(notificationEmail.getSubject());
            messageHelper.setText(mailContentBuilder.build(notificationEmail.getBody()));
        };
        try{
            javaMailSender.send(messagePreparator);
        }catch (MailException mailException){
            throw new AuthenticationApiException("Exception occurred when sending the mail to " + notificationEmail.getRecipient(), mailException);
        }
    }


}
