package com.example.tv360;

import com.example.tv360.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class Tv360Application {
    public static void main(String[] args) {
        SpringApplication.run(Tv360Application.class, args);
    }
}
