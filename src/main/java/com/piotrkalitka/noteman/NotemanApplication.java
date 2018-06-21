package com.piotrkalitka.noteman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EntityScan(basePackageClasses = {NotemanApplication.class, Jsr310JpaConverters.class})
public class NotemanApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotemanApplication.class, args);
    }

    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

}
