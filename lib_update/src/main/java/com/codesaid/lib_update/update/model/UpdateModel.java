package com.codesaid.lib_update.update.model;

import java.io.Serializable;

/**
 * Created By codesaid
 * On :2019-11-27
 * Package Name: com.codesaid.lib_update.update.model
 */
public class UpdateModel implements Serializable {

    private static final long serialVersionUID = -5161684897150460361L;

    public int ecode;
    public String emsg;
    public UpdateInfo data;
}
