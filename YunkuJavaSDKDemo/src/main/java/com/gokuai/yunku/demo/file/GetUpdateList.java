package com.gokuai.yunku.demo.file;

import com.gokuai.base.DebugConfig;
import com.gokuai.yunku.demo.helper.DeserializeHelper;
import com.gokuai.yunku.demo.helper.EntFileManagerHelper;

/**
 * Created by qp on 2017/3/2.
 *
 * 获取更新列表
 */

public class GetUpdateList {

    public static void main(String[] args) {

        DebugConfig.PRINT_LOG = true;
//        DebugConfig.LOG_PATH="LogPath/";

            String returnString = EntFileManagerHelper.getInstance().getUpdateList(false, 0);

            DeserializeHelper.getInstance().deserializeReturn(returnString);
        }
    }
