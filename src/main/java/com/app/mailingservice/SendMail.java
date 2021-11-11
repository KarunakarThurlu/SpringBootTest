package com.app.mailingservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Component
public class SendMail {

    private static Logger logger= LoggerFactory.getLogger(SendMail.class);

    @Autowired
	private JavaMailSender mailSender;
	   
	public void sendMailWithAttachment() {
        logger.info("entering into send mail");
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            helper.setTo("karunakar001122@gmail.com");
            helper.setFrom("info@signator.se");
            helper.setSubject("Mail with Custom file Name");
            helper.setText("Custom File name");
            FileSystemResource file = new FileSystemResource("/home/karunakar/tony/Resume Karunakar.docx.pdf");
            helper.addAttachment("Hållläggs på.pdf", file);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
	}
}
