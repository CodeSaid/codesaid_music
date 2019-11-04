package com.codesaid_music.model.user;


import com.codesaid_music.model.BaseModel;

/**
 * 用户数据协议
 */
public class User extends BaseModel {

    public String userId; //用户唯一标识符
    public String photoUrl;
    public String name;
    public String tick;
    public String mobile;
    public String platform;
}
