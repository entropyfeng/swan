package com.example.swan.swanserver.model.po;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 抽象验证用户基类
 *
 * @author entryfeng
 */
public abstract class AbstractAuthUser {


    @NotNull
    private String userId;

    @NotNull
    private String password;

    @NotNull
    private Date createDate;

    @NotNull
    private Date updateDate;

    //get and setter begin


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

}
