package com.project.other;

import com.project.MailSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean
    MailSender getMailSender(){
        return new MailSender();
    }

}
