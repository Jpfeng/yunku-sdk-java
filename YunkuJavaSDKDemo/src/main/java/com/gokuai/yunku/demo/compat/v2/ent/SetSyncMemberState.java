package com.gokuai.yunku.demo.compat.v2.ent;

import com.gokuai.yunku.demo.compat.v2.helper.EntManagerHelper;
import com.gokuai.yunku.demo.helper.DeserializeHelper;
import com.yunkuent.sdk.DebugConfig;

/**
 * Created by qp on 2017/3/22.
 */
public class SetSyncMemberState {

    public static void main(String[] args) {

        DebugConfig.PRINT_LOG = true;
//        DebugConfig.LOG_PATH="LogPath/";

        String returnString = EntManagerHelper.getInstance().setSyncMemberState("ac1d8e1f-6d67-4143-8494-4c864c5f3d31", true);

        DeserializeHelper.getInstance().deserializeReturn(returnString);
    }
}
