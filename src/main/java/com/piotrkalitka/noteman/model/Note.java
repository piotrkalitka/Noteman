package com.piotrkalitka.noteman.model;

import com.piotrkalitka.noteman.model.audit.UserDateAudit;

import org.springframework.util.StringUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "notes")
public class Note extends UserDateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 1, max = 100)
    private String title;

    @Size(max = 500)
    private String content;

    public Note() {
    }

    public Note(@NotBlank @Size(min = 1, max = 100) String title, @Size(max = 500) String text) {
        this.title = title;
        this.content = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void update(Note newContent) {
        if (!StringUtils.isEmpty(newContent.getTitle())) {
            this.title = newContent.getTitle();
        }
        String object = newContent.getContent();
        if (newContent.getContent() != null) {
            this.content = newContent.getContent();
        }
    }
}