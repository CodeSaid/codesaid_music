package com.codesaid_music.utils;

import com.codesaid_music.model.user.User;

/**
 * Created By codesaid
 * On :2019-11-03
 * Package Name: com.codesaid_music.utils
 * desc: 单例模式 ----> 管理用户登录信息类
 */
public class UserManager {

    private static UserManager mInstance;
    private User mUser;

    private UserManager() {
    }

    /**
     * 双检查机制单例模式
     *
     * @return
     */
    public static UserManager getInstance() {
        if (mInstance == null) {
            synchronized (UserManager.class) { // 保证单例的唯一性
                if (mInstance == null) {
                    mInstance = new UserManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 保存用户信息到内存中
     *
     * @param user User
     */
    public void setUser(User user) {
        this.mUser = user;
        saveLocal(user);
    }

    /**
     * 保存用户信息到数据库
     *
     * @param user user
     */
    private void saveLocal(User user) {

    }

    /**
     * 获取用户信息
     *
     * @return user
     */
    public User getUser() {
        return this.mUser;
    }

    /**
     * 从数据库中获取用户信息
     *
     * @return user
     */
    private User getUserFromLocal() {
        return null;
    }

    public boolean hasLogined() {
        return mUser == null ? false : true;
    }

    /**
     * 删除 User 信息
     */
    public void removeUser() {
        this.mUser = null;
    }
}
