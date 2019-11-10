package com.codesaid.lib_audio.mediaplayer.db;

import android.database.sqlite.SQLiteDatabase;

import com.codesaid.lib_audio.app.AudioHelper;
import com.codesaid.lib_audio.mediaplayer.model.AudioBean;
import com.codesaid.lib_audio.mediaplayer.model.Favourite;

/**
 * Created By codesaid
 * On :2019-11-08
 * Package Name: com.codesaid.lib_audio.mediaplayer.db
 * desc: 操作 GreenDao 数据库帮助类
 */
public class GreenDaoHelper {


    private static final String DB_NAME = "codesaid_music_db";

    // 数据库帮助类 用来创建 升级 数据库
    private static DaoMaster.DevOpenHelper mHelper;

    // 最终创建好的数据库
    private static SQLiteDatabase mDb;

    // 管理数据库
    private static DaoMaster mDaoMaster;

    // 管理各种实体 Dao
    private static DaoSession mDaoSession;


    public static void initDatabase() {
        mHelper = new DaoMaster.DevOpenHelper(AudioHelper.getContext(), DB_NAME, null);
        mDb = mHelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(mDb);
        mDaoSession = mDaoMaster.newSession();
    }

    /**
     * 添加歌曲到收藏列表
     *
     * @param bean AudioBean
     */
    public static void addFavourite(AudioBean bean) {
        FavouriteDao dao = mDaoSession.getFavouriteDao();
        Favourite favourite = new Favourite();
        favourite.setAudioId(bean.id);
        favourite.setAudioBean(bean);
        dao.insertOrReplace(favourite);
    }

    /**
     * 查询收藏列表中是否有该歌曲
     *
     * @param bean AudioBean
     * @return Favourite
     */
    public static Favourite selectFavourite(AudioBean bean) {
        FavouriteDao dao = mDaoSession.getFavouriteDao();
        Favourite favourite = dao.queryBuilder().where(
                FavouriteDao.Properties.AudioId.eq(bean.id)).unique();
        return favourite;
    }

    /**
     * 从收藏列表中移除该歌曲
     *
     * @param bean AudioBean
     */
    public static void removeFavourite(AudioBean bean) {
        FavouriteDao dao = mDaoSession.getFavouriteDao();
        Favourite favourite = selectFavourite(bean);
        dao.delete(favourite);
    }
}
