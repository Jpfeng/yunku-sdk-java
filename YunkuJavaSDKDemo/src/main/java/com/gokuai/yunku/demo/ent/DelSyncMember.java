package com.gokuai.yunku.demo.ent;

import com.gokuai.base.DebugConfig;
import com.gokuai.yunku.demo.helper.DeserializeHelper;
import com.gokuai.yunku.demo.helper.EntManagerHelper;

/**
 * Created by qp on 2017/3/2.
 *
 * 修改同步分组
 */
public class DelSyncMember {

    public static void main(String[] args) {

        DebugConfig.PRINT_LOG = true;
//        DebugConfig.LOG_PATH="LogPath/";

        String returnString = EntManagerHelper.getInstance().delSyncMember(new String[]{"MemberTest", "MemberTest1", "MemberTest2"});

        DeserializeHelper.getInstance().deserializeReturn(returnString);
    }
}
