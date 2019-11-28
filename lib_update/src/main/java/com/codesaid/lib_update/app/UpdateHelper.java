package com.codesaid.lib_update.app;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.codesaid.lib_commin_ui.CommonDialog;
import com.codesaid.lib_network.CommonOkHttpClient;
import com.codesaid.lib_network.okhttp.listener.DisposeDataHandle;
import com.codesaid.lib_network.okhttp.listener.DisposeDataListener;
import com.codesaid.lib_network.okhttp.request.CommonRequest;
import com.codesaid.lib_network.okhttp.utils.ResponseEntityToModule;
import com.codesaid.lib_update.R;
import com.codesaid.lib_update.update.UpdateService;
import com.codesaid.lib_update.update.constant.Constants;
import com.codesaid.lib_update.update.model.UpdateModel;
import com.codesaid.lib_update.update.utils.Utils;

/**
 * Created By codesaid
 * On :2019-11-28
 * Package Name: com.codesaid.lib_update.app
 */
public class UpdateHelper {

    public static final String UPDATE_FILE_KEY = "apk";

    public static String UPDATE_ACTION;

    //SDK全局Context, 供子模块用
    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
        UPDATE_ACTION = mContext.getPackageName() + ".INSTALL";
    }

    public static Context getContext() {
        return mContext;
    }

    /**
     * 检查更新方法
     *
     * @param activity activity
     */
    public static void checkUpdate(final Activity activity) {
        CommonOkHttpClient.get(CommonRequest.
                        createGetRequest(Constants.CHECK_UPDATE),
                new DisposeDataHandle(new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        final UpdateModel updateModel = (UpdateModel) responseObj;
                        if (Utils.getVersionCode(mContext) < updateModel.data.currentVersion) {
                            //说明有新版本,开始下载
                            CommonDialog dialog =
                                    new CommonDialog(activity, mContext.getString(R.string.update_new_version),
                                            mContext.getString(R.string.update_title),
                                            mContext.getString(R.string.update_install),
                                            mContext.getString(R.string.cancel), new CommonDialog.DialogClickListener() {
                                        @Override
                                        public void onDialogClick() {
                                            UpdateService.startService(mContext);
                                        }
                                    });
                            dialog.show();
                        } else {
                            //弹出一个toast提示当前已经是最新版本等处理
                            Toast.makeText(activity, "当前已经是最新版本!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Object reasonObj) {
                        onSuccess(
                                ResponseEntityToModule.parseJsonToModule(MockData.UPDATE_DATA, UpdateModel.class));
                    }
                }, UpdateModel.class));
    }
}
