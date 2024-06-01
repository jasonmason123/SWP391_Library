package com.springdemo.library.services;

import com.springdemo.library.model.dto.EmailDetailsDto;
import com.springdemo.library.model.dto.OtpDto;
import com.springdemo.library.services.interfaces.IMessageService;
import com.springdemo.library.utils.Common;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@Slf4j
public class EmailService implements IMessageService<EmailDetailsDto> {
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public boolean sendToUser(EmailDetailsDto emailDetailsDto) {
        try {
            // Creating a simple mail message
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            // Setting up necessary details
            mailMessage.setFrom(sender);
            mailMessage.setTo(emailDetailsDto.getRecipient());
            mailMessage.setText(emailDetailsDto.getMessageBody());
            mailMessage.setSubject(emailDetailsDto.getSubject());

            // Sending the mail
            javaMailSender.send(mailMessage);

            return true;
        } catch (Exception e) {
            log.error("failed to send email: " + e);
            return false;
        }
    }

    public boolean sendHtmlEmail(EmailDetailsDto emailDetailsDto) {
        try {
            MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
            mimeMessage.setContent(emailDetailsDto.getMessageBody(), "text/html");
            mimeMessageHelper.setTo(emailDetailsDto.getRecipient());
            mimeMessageHelper.setFrom(this.sender);
            mimeMessageHelper.setSubject(emailDetailsDto.getSubject());
            this.javaMailSender.send(mimeMessage);
            return true;
        } catch (Exception e) {
            log.error("failed to send email: " + e);
            return false;
        }
    }

    public boolean sendEmailWithAttachment(EmailDetailsDto emailDetailsDto) {
        if(emailDetailsDto.getAttachmentPath() != null || !emailDetailsDto.getAttachmentPath().isEmpty()) {
            // Creating a mime message
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper;

            try {
                // Setting multipart as true for attachments to be send
                mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
                mimeMessageHelper.setFrom(sender);
                mimeMessageHelper.setTo(emailDetailsDto.getRecipient());
                mimeMessageHelper.setText(emailDetailsDto.getMessageBody());
                mimeMessageHelper.setSubject(emailDetailsDto.getSubject());

                // Adding the attachment
                FileSystemResource file = new FileSystemResource(new File(emailDetailsDto.getAttachmentPath()));

                mimeMessageHelper.addAttachment(file.getFilename(), file);

                // Sending the mail
                javaMailSender.send(mimeMessage);
                return true;
            } catch (MessagingException e) {
                log.error("failed to send email");
                return false;
            }
        }
        return false;
    }

    public OtpDto sendOtpViaEmail(String recipient) {
        String otp = Common.generateRandomNumberString(6);
        EmailDetailsDto content = EmailDetailsDto.builder().recipient(recipient)
                            .subject("Mã xác minh của bạn: " + otp)
                            .messageBody("Vui lòng nhập mã xác minh: " + otp + " để xác thực").build();
        if(sendToUser(content))
            return new OtpDto(otp); //sent successfully
        return null; //sent failed
    }

}
