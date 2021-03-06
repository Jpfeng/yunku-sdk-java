package com.gokuai.base;

import com.gokuai.base.utils.Util;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by qp on 2017/4/21.
 */
public abstract class HttpEngine extends SignAbility {

    private final static String LOG_TAG = "HttpEngine";

    protected String mClientSecret;
    protected String mClientId;


    public HttpEngine(String clientId, String clientSecret) {
        mClientId = clientId;
        mClientSecret = clientSecret;
    }


    public static final int ERRORID_NETDISCONNECT = 1;

    /**
     * 从服务器获得数据后的回调
     *
     * @author Administrator
     */
    public interface DataListener {
        void onReceivedData(int apiId, Object object, int errorId);
    }

    /**
     * API签名,SSO签名
     *
     * @param params
     * @return
     */
    public String generateSign(HashMap<String, String> params) {
        return generateSign(params, mClientSecret);
    }

    /**
     * @param params
     * @param ignoreKeys 忽略签名
     * @return
     */
    protected String generateSign(HashMap<String, String> params, ArrayList<String> ignoreKeys) {
        return generateSign(params, mClientSecret, ignoreKeys);
    }


    /**
     * 请求协助类
     */
    public class RequestHelper {
        RequestMethod method;
        HashMap<String, String> params;
        HashMap<String, String> headParams;
        String url;
        boolean checkAuth;
        String postType = NetConfig.POST_DEFAULT_FORM_TYPE;
        IAsyncTarget target;

        ArrayList<String> ignoreKeys;

        public RequestHelper setMethod(RequestMethod method) {
            this.method = method;
            return this;
        }

        public RequestHelper setParams(HashMap<String, String> params) {
            this.params = params;
            return this;
        }

        public RequestHelper setHeadParams(HashMap<String, String> headParams) {
            this.headParams = headParams;
            return this;
        }

        public RequestHelper setCheckAuth(boolean checkAuth) {
            this.checkAuth = checkAuth;
            return this;
        }

        public RequestHelper setPostType(String postType) {
            this.postType = postType;
            return this;
        }

        public RequestHelper setUrl(String url) {
            this.url = url;
            return this;
        }

        public RequestHelper setIgnoreKeys(ArrayList<String> ignoreKeys) {
            this.ignoreKeys = ignoreKeys;
            return this;
        }

        public RequestHelper setTarget(IAsyncTarget target) {
            this.target = target;
            return this;
        }

        /**
         * 同步执行
         *
         * @return
         */
        public String executeSync() {
            checkNecessaryParams(url, method);

            if (!isNetworkAvailableEx()) {
                ReturnResult returnResult = new ReturnResult("", ERRORID_NETDISCONNECT);
                return new Gson().toJson(returnResult);
            }

            if (checkAuth) {
                if (HttpEngine.this instanceof IAuthRequest) {
                    return ((IAuthRequest) HttpEngine.this).sendRequestWithAuth(url, method, params, headParams, ignoreKeys, postType);
                } else {
                    LogPrint.error(LOG_TAG, "You need implement IAuthRequest before set checkAuth=true");
                }

            }
            return NetConnection.sendRequest(url, method, params, headParams, postType);
        }

        /**
         * 异步执行
         *
         * @return
         */
        public IAsyncTarget executeAsync(DataListener listener, int apiId, RequestHelperCallBack callBack) {

            checkNecessaryParams(url, method);

            if (target != null) {
                return target.execute(listener, this, callBack, apiId);

            }
            return new DefaultAsyncTarget().execute(listener, this, callBack, apiId);
        }

        /**
         * 异步执行
         *
         * @return
         */
        public IAsyncTarget executeAsync(DataListener listener, int apiId) {
            return executeAsync(listener, apiId, null);
        }


        private void checkNecessaryParams(String url, RequestMethod method) {
            if (Util.isEmpty(url)) {
                throw new IllegalArgumentException("url must not be null");
            }

            if (method == null) {
                throw new IllegalArgumentException("method must not be null");
            }
        }


    }


    public interface RequestHelperCallBack {
        Object getReturnData(String returnString);
    }


    /**
     * @return
     */
    protected boolean isNetworkAvailableEx() {
        return true;
    }
}
