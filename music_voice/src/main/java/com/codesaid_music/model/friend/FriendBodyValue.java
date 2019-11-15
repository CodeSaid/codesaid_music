package com.codesaid_music.model.friend;

import com.codesaid.lib_audio.mediaplayer.model.AudioBean;
import com.codesaid_music.model.BaseModel;

import java.util.ArrayList;

/**
 * @author codesaid
 * desc：朋友实体
 */
public class FriendBodyValue extends BaseModel {

    public int type;
    public String avatr;
    public String name;
    public String fans;
    public String text;
    public ArrayList<String> pics;
    public String videoUrl;
    public String zan;
    public String msg;
    public AudioBean audioBean;
}
