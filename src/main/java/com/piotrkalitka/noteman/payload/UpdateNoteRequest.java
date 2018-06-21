package com.piotrkalitka.noteman.payload;

import javax.validation.constraints.Size;

public class UpdateNoteRequest {

    @Size(min = 1, max = 100)
    private String title;
    @Size(max = 500)
    private String content;

    public UpdateNoteRequest() {
    }

    public UpdateNoteRequest(String title, String content) {
        this.title = title;
        this.content = content;
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
}