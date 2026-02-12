package com.bytewizard.msgmate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MsgMateApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsgMateApplication.class, args);
    }

}
