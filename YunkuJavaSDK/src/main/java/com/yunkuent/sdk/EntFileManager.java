package com.yunkuent.sdk;

import com.gokuai.base.HttpEngine;
import com.gokuai.base.LogPrint;
import com.gokuai.base.RequestMethod;
import com.gokuai.base.ReturnResult;
import com.gokuai.base.utils.Util;
import com.google.gson.Gson;
import com.yunkuent.sdk.upload.UploadCallBack;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Brandon on 2014/8/14.
 */
public class EntFileManager extends HttpEngine {

    private static final String TAG = "EntFileManager";

    protected static String DEFAULT_OPNAME = "";
    static String UPLOAD_ROOT_PATH = "";
    static String DEFAULT_UPLOAD_TAGS = "";
    static boolean RANDOM_GUID_TAG = false;

    private static final int UPLOAD_LIMIT_SIZE = 52428800;
    private final String URL_API_FILELIST = HostConfig.API_ENT_HOST + "/1/file/ls";
    private final String URL_API_UPDATE_LIST = HostConfig.API_ENT_HOST + "/1/file/updates";
    private final String URL_API_FILE_INFO = HostConfig.API_ENT_HOST + "/1/file/info";
    private final String URL_API_CREATE_FOLDER = HostConfig.API_ENT_HOST + "/1/file/create_folder";
    private final String URL_API_CREATE_FILE = HostConfig.API_ENT_HOST + "/1/file/create_file";
    private final String URL_API_COPY_FILE = HostConfig.API_ENT_HOST + "/1/file/copy";
    private final String URL_API_MCOPY_FILE = HostConfig.API_ENT_HOST + "/1/file/mcopy";
    private final String URL_API_DEL_FILE = HostConfig.API_ENT_HOST + "/1/file/del";
    private final String URL_API_RECYCLE_FILE = HostConfig.API_ENT_HOST + "/1/file/recycle";
    private final String URL_API_RECOVER_FILE = HostConfig.API_ENT_HOST + "/1/file/recover";
    private final String URL_API_DEL_COMPLETELY_FILE = HostConfig.API_ENT_HOST + "/1/file/del_completely";
    private final String URL_API_MOVE_FILE = HostConfig.API_ENT_HOST + "/1/file/move";
    private final String URL_API_HISTORY_FILE = HostConfig.API_ENT_HOST + "/1/file/history";
    private final String URL_API_LINK_FILE = HostConfig.API_ENT_HOST + "/1/file/link";
    private final String URL_API_SENDMSG = HostConfig.API_ENT_HOST + "/1/file/sendmsg";
    private final String URL_API_GET_LINK = HostConfig.API_ENT_HOST + "/1/file/links";
    private final String URL_API_UPDATE_COUNT = HostConfig.API_ENT_HOST + "/1/file/updates_count";
    private final String URL_API_GET_SERVER_SITE = HostConfig.API_ENT_HOST + "/1/file/servers";
    private final String URL_API_CREATE_FILE_BY_URL = HostConfig.API_ENT_HOST + "/1/file/create_file_by_url";
    private final String URL_API_UPLOAD_SERVERS = HostConfig.API_ENT_HOST + "/1/file/upload_servers";
    private final String URL_API_GET_UPLOAD_URL = HostConfig.API_ENT_HOST + "/1/file/download_url";
    private final String URL_API_FILE_SEARCH = HostConfig.API_ENT_HOST + "/1/file/search";
    private final String URL_API_PREVIEW_URL = HostConfig.API_ENT_HOST + "/1/file/preview_url";
    private final String URL_API_GET_PERMISSION = HostConfig.API_ENT_HOST + "/1/file/get_permission";
    private final String URL_API_SET_PERMISSION = HostConfig.API_ENT_HOST + "/1/file/file_permission";
    private final String URL_API_ADD_TAG = HostConfig.API_ENT_HOST + "/1/file/add_tag";
    private final String URL_API_DEL_TAG = HostConfig.API_ENT_HOST + "/1/file/del_tag";


    public EntFileManager(String orgClientId, String orgClientSecret) {
        super(orgClientId, orgClientSecret);
    }

    /**
     * 获取根目录文件列表
     *
     * @return
     */
    public String getFileList() {
        return this.getFileList("", null, null, 0, 100, false);
    }

    /**
     * 获取文件列表
     *
     * @param fullPath 路径, 空字符串表示根目录
     * @return
     */
    public String getFileList(String fullPath) {
        return this.getFileList(fullPath, null, null, 0, 100, false);
    }

    /**
     * 获取文件列表
     *
     * @param fullPath 路径, 空字符串表示根目录
     * @param order    排序
     * @return
     */
    public String getFileList(String fullPath, String order) {
        return this.getFileList(fullPath, order, null, 0, 100, false);
    }


    /**
     * 获取文件列表
     *
     * @param fullPath 路径, 空字符串表示根目录
     * @param order    排序
     * @param tag      返回指定标签的文件
     * @param start    起始下标, 分页显示
     * @param size     返回文件/文件夹数量限制
     * @param dirOnly  只返回文件夹    @return
     */
    public String getFileList(String fullPath, String order, String tag, int start, int size, boolean dirOnly) {
        String url = URL_API_FILELIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("org_client_id", mClientId);
        params.put("dateline", Util.getUnixDateline() + "");
        params.put("fullpath", fullPath);
        params.put("tag", tag);
        params.put("start", start + "");
        params.put("size", size + "");
        params.put("order", order);
        if (dirOnly) {
            params.put("dir", "1");
        }
        params.put("sign", generateSign(params));
        return new RequestHelper().setParams(params).setUrl(url).setMethod(RequestMethod.GET).executeSync();
    }

    /**
     * 获取更新列表
     *
     * @param isCompare
     * @param fetchDateline
     * @return
     */
    public String getUpdateList(boolean isCompare, long fetchDateline) {
        String url = URL_API_UPDATE_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("org_client_id", mClientId);
        params.put("dateline", Util.getUnixDateline() + "");
        if (isCompare) {
            params.put("mode", "compare");
        }
        params.put("fetch_dateline", fetchDateline + "");
        params.put("sign", generateSign(params));
        return new RequestHelper().setParams(params).setUrl(url).setMethod(RequestMethod.GET).executeSync();
    }

    /**
     * 获取文件信息
     *
     * @param fullPath
     * @param net
     * @return
     */
    public String getFileInfo(String fullPath, NetType net, boolean getAttribute) {
        String url = URL_API_FILE_INFO;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("org_client_id", mClientId);
        params.put("dateline", Util.getUnixDateline() + "");
        params.put("fullpath", fullPath);
        params.put("attribute", (getAttribute ? 1 : 0) + "");
        switch (net) {
            case DEFAULT:
                break;
            case IN:
                params.put("net", net.name().toLowerCase());
                break;
        }
        params.put("sign", generateSign(params));
        return new RequestHelper().setParams(params).setUrl(url).setMethod(RequestMethod.GET).executeSync();
    }

    /**
     * 创建文件夹
     *
     * @param fullPath
     * @param opName
     * @return
     */
    public String createFolder(String fullPath, String opName) {
        String url = URL_API_CREATE_FOLDER;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("org_client_id", mClientId);
        params.put("dateline", Util.getUnixDateline() + "");
        params.put("fullpath", fullPath);
        params.put("op_name", opName);
        params.put("sign", generateSign(params));
        return new RequestHelper().setParams(params).setUrl(url).setMethod(RequestMethod.POST).executeSync();
    }

    /**
     * 获取实际的上传地址
     *
     * @return
     */
    private String getRealPath(String fullPath) {
        if (!Util.isEmpty(UPLOAD_ROOT_PATH)) {
            return UPLOAD_ROOT_PATH + fullPath;
        }
        return fullPath;
    }

    /**
     * 添加上传默认标签
     *
     * @param fullPath
     * @return
     */
    String addUploadTags(String fullPath) {
        if (RANDOM_GUID_TAG) {
            DEFAULT_UPLOAD_TAGS += "|" + UUID.randomUUID();
        }

        String tags[] = DEFAULT_UPLOAD_TAGS.split("\\|");
        return addTag(fullPath, tags);

    }

    /**
     * 通过文件流上传 (覆盖同名文件)
     *
     * @param fullPath
     * @param opName
     * @param stream
     * @return
     */
    public String createFile(String fullPath, String opName, FileInputStream stream) {
        return createFile(fullPath, opName, stream, true);
    }

    /**
     * 通过文件流上传
     *
     * @param fullPath
     * @param opName
     * @param stream
     * @return
     */
    public String createFile(String fullPath, String opName, FileInputStream stream, boolean overwrite) {

        fullPath = getRealPath(fullPath);
        opName = Util.isEmpty(opName) ? DEFAULT_OPNAME : opName;

        try {
            if (stream.available() > UPLOAD_LIMIT_SIZE) {
                LogPrint.error(TAG, "文件大小超过50MB");
                return "";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String fileName = Util.getNameFromPath(fullPath);

        try {
            long dateline = Util.getUnixDateline();

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("org_client_id", mClientId);
            params.put("dateline", dateline + "");
            params.put("fullpath", fullPath);
            params.put("op_name", opName);
            params.put("overwrite", (overwrite ? 1 : 0) + "");
            params.put("filefield", "file");

            MsMultiPartFormData multipart = new MsMultiPartFormData(URL_API_CREATE_FILE, "UTF-8");
            multipart.addFormField("org_client_id", mClientId);
            multipart.addFormField("dateline", dateline + "");
            multipart.addFormField("fullpath", fullPath);
            multipart.addFormField("op_name", opName);
            multipart.addFormField("overwrite", (overwrite ? 1 : 0) + "");
            multipart.addFormField("filefield", "file");
            multipart.addFormField("sign", generateSign(params));

            multipart.addFilePart("file", stream, fileName);

            String returnString = multipart.finish();

            if (Util.isEmpty(DEFAULT_UPLOAD_TAGS)) {
                return returnString;
            }

            ReturnResult returnResult = ReturnResult.create(returnString);
            if (returnResult.getStatusCode() == HttpURLConnection.HTTP_OK) {
                return addUploadTags(fullPath);
            }


        } catch (IOException ex) {
            System.err.println(ex);
        }
        return "";
    }


    /**
     * 文件分块上传 (覆盖同名文件)
     *
     * @param fullPath
     * @param opName
     * @param opId
     * @param localFilePath
     * @param callBack
     * @return
     */
    public UploadRunnable uploadByBlock(String fullPath, String opName, int opId, String localFilePath,
                                        int blockSize, UploadCallBack callBack) {
        return uploadByBlock(fullPath, opName, opId, localFilePath, true, blockSize, callBack);
    }

    /**
     * 文件分块上传
     *
     * @param fullPath
     * @param opName
     * @param opId
     * @param localFilePath
     * @param overwrite
     * @param callBack
     */
    public UploadRunnable uploadByBlock(String fullPath, String opName, int opId, String localFilePath,
                                        boolean overwrite, int blockSize, UploadCallBack callBack) {

        opName = Util.isEmpty(opName) ? DEFAULT_OPNAME : opName;
        fullPath = getRealPath(fullPath);

        UploadRunnable<EntFileManager> uploadRunnable = new UploadRunnable<EntFileManager>(URL_API_CREATE_FILE, localFilePath, fullPath,
                opName, opId, mClientId, Util.getUnixDateline(), callBack, mClientSecret, overwrite, blockSize, this);

        Thread thread = new Thread(uploadRunnable);
        thread.start();
        return uploadRunnable;
    }


    /**
     * 文件流分块上传 (覆盖同名文件)
     *
     * @param fullPath
     * @param opName
     * @param opId
     * @param inputStream
     * @param callBack
     * @return
     */
    public UploadRunnable uploadByBlock(String fullPath, String opName, int opId, InputStream inputStream,
                                        int blockSize, UploadCallBack callBack) {
        return uploadByBlock(fullPath, opName, opId, inputStream, true, blockSize, callBack);
    }

    /**
     * 通过文件流分块上传
     *
     * @param fullPath
     * @param opName
     * @param opId
     * @param inputStream
     * @param overwrite
     * @param callBack
     * @return
     */
    public UploadRunnable uploadByBlock(String fullPath, String opName, int opId, InputStream inputStream,
                                        boolean overwrite, int blockSize, UploadCallBack callBack) {
        opName = Util.isEmpty(opName) ? DEFAULT_OPNAME : opName;
        fullPath = getRealPath(fullPath);

        UploadRunnable<EntFileManager> uploadRunnable = new UploadRunnable<EntFileManager>(URL_API_CREATE_FILE, inputStream, fullPath, opName,
                opId, mClientId, Util.getUnixDateline(), callBack, mClientSecret, overwrite, blockSize, this);

        Thread thread = new Thread(uploadRunnable);
        thread.start();
        return uploadRunnable;
    }


    /**
     * 通过本地路径上传 （覆盖同名文件）
     *
     * @param fullPath
     * @param opName
     * @param localPath
     * @return
     */
    public String createFile(String fullPath, String opName, String localPath) {
        return createFile(fullPath, opName, localPath, true);
    }

    /**
     * 通过本地路径上传
     *
     * @param fullPath
     * @param opName
     * @param localPath
     * @param overwrite
     * @return
     */
    public String createFile(String fullPath, String opName, String localPath, boolean overwrite) {
        File file = new File(localPath.trim());
        if (file.exists()) {
            try {
                FileInputStream inputStream = new FileInputStream(file);
                return createFile(fullPath, opName, inputStream, overwrite);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            LogPrint.error(TAG, "file not exist");
        }

        return "";

    }

    /**
     * 复制文件
     *
     * @param originFullPath
     * @param targetFullPath
     * @param opName
     * @return
     */
    public String copy(String originFullPath, String targetFullPath, String opName) {
        String url = URL_API_COPY_FILE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("org_client_id", mClientId);
        params.put("dateline", Util.getUnixDateline() + "");
        params.put("from_fullpath", originFullPath);
        params.put("fullpath", targetFullPath);
        params.put("op_name", opName);
        params.put("sign", generateSign(params));
        return new RequestHelper().setParams(params).setUrl(url).setMethod(RequestMethod.POST).executeSync();
    }


    /**
     * 复制文件( 拷贝 tag 以及 opname )
     *
     * @param originFullPaths
     * @param targetFullPaths
     * @param sp
     * @return
     */
    public String copyAll(String originFullPaths, String targetFullPaths, String sp) {
        String url = URL_API_MCOPY_FILE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("org_client_id", mClientId);
        params.put("dateline", Util.getUnixDateline() + "");
        params.put("from_fullpaths", originFullPaths);
        params.put("paths", targetFullPaths);
        params.put("sp", sp);
        params.put("copy_all", 1 + "");
        params.put("sign", generateSign(params));
        return new RequestHelper().setParams(params).setUrl(url).setMethod(RequestMethod.POST).executeSync();
    }


    /**
     * 删除文件
     *
     * @param fullPaths
     * @param opName
     * @return
     */
    public String del(String fullPaths, String opName) {
        return del(fullPaths, opName, false);
    }

    /**
     * 删除文件
     *
     * @param fullPaths
     * @param opName
     * @param destroy
     * @return
     */
    public String del(String fullPaths, String opName, boolean destroy) {
        String url = URL_API_DEL_FILE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("org_client_id", mClientId);
        params.put("dateline", Util.getUnixDateline() + "");
        params.put("fullpaths", fullPaths);
        params.put("destroy", destroy ? "1" : "0");
        params.put("op_name", opName);
        params.put("sign", generateSign(params));
        return new RequestHelper().setParams(params).setUrl(url).setMethod(RequestMethod.POST).executeSync();
    }


    /**
     * 根据 tag 删除文件
     *
     * @param tag
     * @param path
     * @param opName @return
     */
    public String delByTag(String tag, String path, String opName) {
        return delByTag(tag, path, opName, false);
    }

    /**
     * 根据 tag 删除文件
     *
     * @param tag
     * @param path
     * @param destroy
     * @param opName  @return
     */
    public String delByTag(String tag, String path, String opName, boolean destroy) {
        String url = URL_API_DEL_FILE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("org_client_id", mClientId);
        params.put("dateline", Util.getUnixDateline() + "");
        params.put("tag", tag);
        params.put("destroy", destroy ? "1" : "0");
        params.put("path", path);
        params.put("op_name", opName);
        params.put("sign", generateSign(params));
        return new RequestHelper().setParams(params).setUrl(url).setMethod(RequestMethod.POST).executeSync();
    }

    /**
     * 回收站
     *
     * @param start
     * @param size
     * @return
     */
    public String recycle(int start, int size) {
        String url = URL_API_RECYCLE_FILE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("org_client_id", mClientId);
        params.put("dateline", Util.getUnixDateline() + "");
        params.put("start", start + "");
        params.put("size", size + "");
        params.put("sign", generateSign(params));
        return new RequestHelper().setParams(params).setUrl(url).setMethod(RequestMethod.POST).executeSync();
    }

    /**
     * 恢复删除文件
     *
     * @param fullpaths
     * @param opName
     * @return
     */
    public String recover(String fullpaths, String opName) {
        String url = URL_API_RECOVER_FILE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("org_client_id", mClientId);
        params.put("dateline", Util.getUnixDateline() + "");
        params.put("fullpaths", fullpaths);
        params.put("op_name", opName);
        params.put("sign", generateSign(params));
        return new RequestHelper().setParams(params).setUrl(url).setMethod(RequestMethod.POST).executeSync();
    }

    /**
     * 彻底删除文件（夹）
     *
     * @param fullpaths
     * @param opName
     * @return
     */
    public String delCompletely(String[] fullpaths, String opName) {
        String url = URL_API_DEL_COMPLETELY_FILE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("org_client_id", mClientId);
        params.put("dateline", Util.getUnixDateline() + "");
        params.put("fullpaths", Util.strArrayToString(fullpaths, "|") + "");
        params.put("op_name", opName);
        params.put("sign", generateSign(params));
        return new RequestHelper().setParams(params).setUrl(url).setMethod(RequestMethod.POST).executeSync();
    }

    /**
     * 移动文件
     *
     * @param fullPath
     * @param destFullPath
     * @param opName
     * @return
     */
    public String move(String fullPath, String destFullPath, String opName) {
        String url = URL_API_MOVE_FILE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("org_client_id", mClientId);
        params.put("dateline", Util.getUnixDateline() + "");
        params.put("fullpath", fullPath);
        params.put("dest_fullpath", destFullPath);
        params.put("op_name", opName);
        params.put("sign", generateSign(params));
        return new RequestHelper().setParams(params).setUrl(url).setMethod(RequestMethod.POST).executeSync();
    }

    /**
     * 获取文件历史
     *
     * @param fullPath
     * @param start
     * @param size
     * @return
     */
    public String history(String fullPath, int start, int size) {
        String url = URL_API_HISTORY_FILE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("org_client_id", mClientId);
        params.put("dateline", Util.getUnixDateline() + "");
        params.put("fullpath", fullPath);
        params.put("start", start + "");
        params.put("size", size + "");
        params.put("sign", generateSign(params));
        return new RequestHelper().setParams(params).setUrl(url).setMethod(RequestMethod.POST).executeSync();
    }

    /**
     * 获取文件链接
     *
     * @param fullPath
     * @param deadline
     * @param authType
     * @param password
     * @return
     */
    public String link(String fullPath, int deadline, AuthType authType, String password) {
        String url = URL_API_LINK_FILE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("org_client_id", mClientId);
        params.put("dateline", Util.getUnixDateline() + "");
        params.put("fullpath", fullPath);

        if (deadline != 0) {
            params.put("deadline", deadline + "");
        }

        if (!authType.equals(AuthType.DEFAULT)) {
            params.put("auth", authType.toString().toLowerCase());
        }
        params.put("password", password);
        params.put("sign", generateSign(params));
        return new RequestHelper().setParams(params).setUrl(url).setMethod(RequestMethod.POST).executeSync();
    }


    /**
     * 发送消息
     *
     * @param title
     * @param text
     * @param image
     * @param linkUrl
     * @param opName
     * @return
     */
    public String sendmsg(String title, String text, String image, String linkUrl, String opName) {
        String url = URL_API_SENDMSG;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("org_client_id", mClientId);
        params.put("dateline", Util.getUnixDateline() + "");
        params.put("title", title);
        params.put("text", text);
        params.put("image", image);
        params.put("url", linkUrl);
        params.put("op_name", opName);
        params.put("sign", generateSign(params));
        return new RequestHelper().setParams(params).setUrl(url).setMethod(RequestMethod.POST).executeSync();
    }


    /**
     * 获取当前库所有外链
     *
     * @return
     */
    public String links(boolean fileOnly) {
        String url = URL_API_GET_LINK;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("org_client_id", mClientId);
        params.put("dateline", Util.getUnixDateline() + "");
        if (fileOnly) {
            params.put("file", "1");
        }
        params.put("sign", generateSign(params));
        return new RequestHelper().setParams(params).setUrl(url).setMethod(RequestMethod.GET).executeSync();
    }


    /**
     * 文件更新数量
     *
     * @param beginDateline
     * @param endDateline
     * @param showDelete
     * @return
     */
    public String getUpdateCounts(long beginDateline, long endDateline, boolean showDelete) {
        String url = URL_API_UPDATE_COUNT;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("org_client_id", mClientId);
        params.put("dateline", Util.getUnixDateline() + "");
        params.put("begin_dateline", beginDateline + "");
        params.put("end_dateline", endDateline + "");
        params.put("showdel", (showDelete ? 1 : 0) + "");
        params.put("sign", generateSign(params));
        return new RequestHelper().setParams(params).setUrl(url).setMethod(RequestMethod.GET).executeSync();
    }

    /**
     * 通过链接上传文件（覆盖同名文件）
     *
     * @param fullPath
     * @param opId
     * @param opName
     * @param fileUrl
     * @return
     */
    public String createFileByUrl(String fullPath, int opId, String opName, String fileUrl) {
        return createFileByUrl(fullPath, opId, opName, true, fileUrl);
    }

    /**
     * 通过链接上传文件
     *
     * @param fullPath
     * @param opId
     * @param opName
     * @param overwrite
     * @param fileUrl
     * @return
     */
    public String createFileByUrl(String fullPath, int opId, String opName, boolean overwrite, String fileUrl) {
        String url = URL_API_CREATE_FILE_BY_URL;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("org_client_id", mClientId);
        params.put("fullpath", fullPath);
        params.put("dateline", Util.getUnixDateline() + "");
        if (opId > 0) {
            params.put("op_id", opId + "");
        } else {
            params.put("op_name", opName + "");
        }
        params.put("overwrite", (overwrite ? 1 : 0) + "");
        params.put("url", fileUrl);
        params.put("sign", generateSign(params));
        return new RequestHelper().setParams(params).setUrl(url).setMethod(RequestMethod.POST).executeSync();
    }

    /**
     * 获取上传地址
     * <p>
     * (支持50MB以上文件的上传)
     *
     * @return
     */
    public String getUploadServers() {
        String url = URL_API_UPLOAD_SERVERS;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("org_client_id", mClientId);
        params.put("dateline", Util.getUnixDateline() + "");
        params.put("sign", generateSign(params));
        return new RequestHelper().setParams(params).setUrl(url).setMethod(RequestMethod.GET).executeSync();
    }


    /**
     * 获取服务器地址
     *
     * @param type
     * @return
     */
    public String getServerSite(String type) {
        String url = URL_API_GET_SERVER_SITE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("org_client_id", mClientId);
        params.put("type", type);
        params.put("dateline", Util.getUnixDateline() + "");
        params.put("sign", generateSign(params));
        return new RequestHelper().setParams(params).setUrl(url).setMethod(RequestMethod.POST).executeSync();
    }

    /**
     * 文件搜索
     *
     * @param keyWords
     * @param path
     * @param scopes
     * @param start
     * @param size
     * @return
     */
    public String search(String keyWords, String path, int start, int size, ScopeType... scopes) {
        String url = URL_API_FILE_SEARCH;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("org_client_id", mClientId);
        params.put("keywords", keyWords);
        params.put("path", path);
        if (scopes != null) {
            params.put("scope", new Gson().toJson(scopes).toLowerCase());
        }
        params.put("start", start + "");
        params.put("size", size + "");
        params.put("dateline", Util.getUnixDateline() + "");
        params.put("sign", generateSign(params));
        return new RequestHelper().setParams(params).setUrl(url).setMethod(RequestMethod.GET).executeSync();
    }


    /**
     * 复制一个EntFileManager对象
     *
     * @return
     */
    public EntFileManager clone() {
        return new EntFileManager(mClientId, mClientSecret);
    }

    /**
     * 通过文件唯一标识获取下载地址
     *
     * @param hash
     * @param isOpen
     * @param net
     * @return
     */
    public String getDownloadUrlByHash(String hash, final boolean isOpen, NetType net) {
        return getDownloadUrl(hash, null, isOpen, net, "");
    }

    /**
     * 通过文件路径获取下载地址
     *
     * @param fullPath
     * @param isOpen
     * @param net
     * @return
     */
    public String getDownloadUrlByFullPath(String fullPath, final boolean isOpen, NetType net) {
        return getDownloadUrl(null, fullPath, isOpen, net, "");
    }

    /**
     * 获取下载地址
     *
     * @param hash
     * @param fullPath
     * @param isOpen
     * @param net
     * @return
     */
    private String getDownloadUrl(String hash, String fullPath, final boolean isOpen, NetType net, String fileName) {
        String url = URL_API_GET_UPLOAD_URL;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("org_client_id", mClientId);
        params.put("dateline", Util.getUnixDateline() + "");
        params.put("hash", hash);
        params.put("fullpath", fullPath);
        params.put("filename", fileName);
        params.put("open", (isOpen ? 1 : 0) + "");
        switch (net) {
            case DEFAULT:
                break;
            case IN:
                params.put("net", net.name().toLowerCase());
                break;
        }
        params.put("sign", generateSign(params));
        return new RequestHelper().setParams(params).setUrl(url).setMethod(RequestMethod.GET).executeSync();
    }

    /**
     * 文件预览地址
     *
     * @param fullPath
     * @param showWaterMark
     * @param memberName
     * @return
     */
    public String previewUrl(String fullPath, final boolean showWaterMark, String memberName) {
        String url = URL_API_PREVIEW_URL;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("org_client_id", mClientId);
        params.put("dateline", Util.getUnixDateline() + "");
        params.put("fullpath", fullPath);
        params.put("member_name", memberName);
        params.put("watermark", (showWaterMark ? 1 : 0) + "");
        params.put("sign", generateSign(params));
        return new RequestHelper().setParams(params).setUrl(url).setMethod(RequestMethod.GET).executeSync();
    }

    /**
     * 获取文件夹权限
     *
     * @param fullPath
     * @param memberId
     * @return
     */
    public String getPermission(String fullPath, int memberId) {
        String url = URL_API_GET_PERMISSION;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("org_client_id", mClientId);
        params.put("dateline", Util.getUnixDateline() + "");
        params.put("fullpath", fullPath);
        params.put("member_id", memberId + "");
        params.put("sign", generateSign(params));
        return new RequestHelper().setParams(params).setUrl(url).setMethod(RequestMethod.POST).executeSync();
    }

    /**
     * 修改文件夹权限
     *
     * @param fullPath
     * @param permissions
     * @return
     */
    public String setPermission(String fullPath, int memberId, FilePermissions... permissions) {
        String url = URL_API_SET_PERMISSION;
        HashMap<String, String> params = new HashMap<String, String>();
        if (permissions != null) {
            HashMap<Integer, ArrayList<String>> map = new HashMap<Integer, ArrayList<String>>();
            ArrayList<String> list = new ArrayList<String>();
            for (FilePermissions p : permissions) {
                list.add(p.toString().toLowerCase());
            }
            map.put(memberId, list);
            params.put("permissions", new Gson().toJson(map));
        }
        params.put("org_client_id", mClientId);
        params.put("dateline", Util.getUnixDateline() + "");
        params.put("fullpath", fullPath);
        params.put("sign", generateSign(params));
        return new RequestHelper().setParams(params).setUrl(url).setMethod(RequestMethod.POST).executeSync();
    }

    /**
     * 添加标签
     *
     * @param fullPath
     * @param tags
     * @return
     */
    public String addTag(String fullPath, String[] tags) {
        String url = URL_API_ADD_TAG;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("org_client_id", mClientId);
        params.put("dateline", Util.getUnixDateline() + "");
        params.put("fullpath", fullPath);
        params.put("tag", Util.strArrayToString(tags, ";") + "");
        params.put("sign", generateSign(params));
        return new RequestHelper().setParams(params).setUrl(url).setMethod(RequestMethod.POST).executeSync();
    }

    /**
     * 删除标签
     *
     * @param fullPath
     * @param tags
     * @return
     */
    public String delTag(String fullPath, String[] tags) {
        String url = URL_API_DEL_TAG;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("org_client_id", mClientId);
        params.put("dateline", Util.getUnixDateline() + "");
        params.put("fullpath", fullPath);
        params.put("tag", Util.strArrayToString(tags, ";") + "");
        params.put("sign", generateSign(params));
        return new RequestHelper().setParams(params).setUrl(url).setMethod(RequestMethod.POST).executeSync();
    }

    public enum AuthType {
        DEFAULT,
        PREVIEW,
        DOWNLOAD,
        UPLOAD
    }

    public enum NetType {
        DEFAULT,
        IN
    }

}
