package com.codesaid_music.database;

import android.content.Context;

import com.codesaid_music.view.login.bean.DaoMaster;
import com.codesaid_music.view.login.bean.DaoSession;
import com.codesaid_music.view.login.bean.UserProfileDao;

import org.greenrobot.greendao.database.Database;

/**
 * Created By codesaid
 * On :2019-12-10
 * Package Name: com.codesaid_music.database
 */
public class DatabaseManager {
    private DaoSession mDaoSession = null;
    private UserProfileDao mDao = null;

    private DatabaseManager() {

    }

    public DatabaseManager init(Context context) {
        initDao(context);
        return this;
    }

    private static final class Holder {
        private static final DatabaseManager INSTANCE = new DatabaseManager();
    }

    public static DatabaseManager getInstance() {
        return Holder.INSTANCE;
    }

    private void initDao(Context context) {
        final ReleaseOpenHelper helper = new ReleaseOpenHelper(context, "fast_ec.db");
        final Database db = helper.getWritableDb();
        mDaoSession = new DaoMaster(db).newSession();
        mDao = mDaoSession.getUserProfileDao();
    }

    public final UserProfileDao getDao() {
        return mDao;
    }
}
