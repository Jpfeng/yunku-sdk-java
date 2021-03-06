package com.gokuai.yunku.demo.compat.v2.file;

import com.gokuai.base.DebugConfig;
import com.gokuai.yunku.demo.Config;
import com.gokuai.yunku.demo.compat.v2.helper.EntFileManagerHelper;
import com.yunkuent.sdk.UploadRunnable;
import com.yunkuent.sdk.compat.v2.ConfigHelper;
import com.yunkuent.sdk.upload.UploadCallBack;

/**
 * Created by qp on 2017/3/2.
 * <p>
 * 文件分块上传
 */
public class UploadByBlock {

    public static void main(String[] args) {

        //-------- 如果想改编上传基础配置，可以进行以几种配置------
        new ConfigHelper()
                .uploadOpname("[Default Name]")
                .uploadRootPath("default/custom/upload/path")
                .uploadTags("[tag1]|[tag2]").config();

        //---------------------------------------------------

        DebugConfig.PRINT_LOG = true;

        UploadRunnable u = EntFileManagerHelper.getInstance().uploadByBlock("testBlockSize.jpg", "Brandon", 0, Config.TEST_FILE_PATH, true, 10485760, new UploadCallBack() {

            @Override
            public void onSuccess(long threadId, String fileHash) {
                System.out.println("success:" + threadId);

            }

            public void onFail(long threadId, String errorMsg) {
                System.out.println("fail:" + threadId + " errorMsg:" + errorMsg);

            }

            public void onProgress(long threadId, float percent) {
                System.out.println("onProgress:" + threadId + " onProgress:" + percent * 100);

            }
        });
    }
}
