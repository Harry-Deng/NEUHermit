package com.sora.gcdr.db;

public class User {
    public static final String KEY_USERNAME = "username";
    public static final String KEY_NICKNAME = "nickname";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_QQ_NUMBER = "qq_number";
    public static final String KEY_USER_ID = "objectId";
    private String id;//用于云端识别

    private String username;
    private String password;
    private String nickname;
    private String qqNumber;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getQqNumber() {
        return qqNumber;
    }

    public void setQqNumber(String qqNumber) {
        this.qqNumber = qqNumber;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
