package com.example.swan.swanserver.model.po;


/**
 * @author entropyfeng
 * 默认验证用户类
 * 用户可以自定义验证类并继承{@link AbstractAuthUser}
 */
public class DefaultAuthUser extends AbstractAuthUser {

    public DefaultAuthUser(){
        super();
    }


    private String username;

    /**
     * 密码加盐
     */
    private String salt;

    private String email;

    private String phone;

    private String userStatus;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

}
