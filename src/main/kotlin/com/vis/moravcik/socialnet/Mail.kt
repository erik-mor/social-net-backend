package com.vis.moravcik.socialnet

import org.springframework.context.annotation.Bean
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSenderImpl

import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component
import java.util.*

@Bean
fun getJavaMailSender(): JavaMailSender? {
    val mailSender = JavaMailSenderImpl()
    mailSender.host = "smtp.gmail.com"
    mailSender.port = 587
    mailSender.username = "my.gmail@gmail.com"
    mailSender.password = "password"
    val props: Properties = mailSender.javaMailProperties
    props.put("mail.transport.protocol", "smtp")
    props.put("mail.smtp.auth", "true")
    props.put("mail.smtp.starttls.enable", "true")
    props.put("mail.debug", "true")
    return mailSender
}

@Component
class EmailServiceImpl(
    val emailSender: JavaMailSender
) {

    fun sendSimpleMessage(to: String, subject:String, text:String) {
        val message: SimpleMailMessage = SimpleMailMessage()
        message.setFrom("noreply@baeldung.com");
        message.setTo(to)
        message.setSubject(subject)
        message.setText(text)
        emailSender.send(message)
    }
}

