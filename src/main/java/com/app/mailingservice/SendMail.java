package com.app.mailingservice;

import java.io.File;
import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class SendMail {

	@Autowired
	private JavaMailSender mailSender;
	   
	public void sendMailWithAttachment() {

 System.out.println("Sending mail");
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
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

    System.out.println("Sending Done");
		
	}
}
