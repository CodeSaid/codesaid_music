package com.codesaid_music.view.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.codesaid.lib_commin_ui.base.BaseActivity;
import com.codesaid.lib_network.okhttp.listener.DisposeDataListener;
import com.codesaid_music.R;
import com.codesaid_music.api.RequestCenter;
import com.codesaid_music.model.login.LoginEvent;
import com.codesaid_music.model.user.User;
import com.codesaid_music.utils.UserManager;

import org.greenrobot.eventbus.EventBus;

/**
 * Created By codesaid
 * On :2019-11-03
 * Package Name: com.codesaid_music.view.login
 * desc: 登录页面
 */
public class LoginActivity extends BaseActivity {

    String url = "http://132.232.72.120:8080/music/login.json";

    String html;

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_layout);

        findViewById(R.id.login_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                OkHttpClient okHttpClient = new OkHttpClient();
//                Request request = new Request.Builder()
//                        .url(url)
//                        .get()
//                        .build();
//
//                okHttpClient.newCall(request).enqueue(new Callback() {
//                    @Override
//                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                        Log.d("TAG", e.toString());
//                    }
//
//                    @Override
//                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                        String result = response.body().string();
//                        try {
//                            JSONObject jsonObject = new JSONObject(result);
//                            String name = jsonObject.getString("name");
//                            Log.d("TAG", name);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });


                                RequestCenter.login(new DisposeDataListener() {
                                @Override
                                public void onSuccess (Object responseObj){
                                    // 登录成功
                                    User user = (User) responseObj;
                                    UserManager.getInstance().setUser(user);
                                    EventBus.getDefault().post(new LoginEvent());
                                    finish();
                                }

                                @Override
                                public void onFailure (Object reasonObj){

                                }
                            });
            }
        });
    }
}
