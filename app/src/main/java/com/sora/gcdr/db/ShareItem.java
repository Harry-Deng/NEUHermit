package com.sora.gcdr.db;

public class ShareItem {
    String username;
    long datetime;
    String content;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ShareItem(String username, long datetime, String content) {
        this.username = username;
        this.datetime = datetime;
        this.content = content;
    }
}
