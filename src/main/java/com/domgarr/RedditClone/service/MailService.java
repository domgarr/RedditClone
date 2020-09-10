package com.domgarr.RedditClone.service;

import com.domgarr.RedditClone.exception.SpringRedditException;
import com.domgarr.RedditClone.model.NotificationEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
@Slf4j
public class MailService {
    private final JavaMailSender mailSender;
    private final MailContentBuilder mailContentBuilder;

    //Alternative to using Async would be RabbitMQ or ActiveMQ (Message Queues)
    @Async
    public void sendMail(NotificationEmail notificationEmail ){
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("springreddit@email.com");
            messageHelper.setTo(notificationEmail.getRecipient());
            messageHelper.setSubject(notificationEmail.getSubject());
            messageHelper.setText(mailContentBuilder.build(notificationEmail.getBody()));
        };

        try{
            mailSender.send(messagePreparator);
            log.info("Activation email sent!");
        } catch(MailException e){
            throw new SpringRedditException("Exception occured when sending mail to: " + notificationEmail.getRecipient());
        }
    }
}
