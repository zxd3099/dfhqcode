package com.dfhqcode.config;

import com.chinamobile.cmss.sdk.face.ECloudDefaultClient;
import com.chinamobile.cmss.sdk.face.http.constant.Region;
import com.chinamobile.cmss.sdk.face.http.signature.Credential;

/**
 * @author zxd3099
 * @create 2022-06-29-11:10
 */
public class CloudConfig {
    private static final String USER_AK;
    private static final String USER_SK;
    public static final ECloudDefaultClient client;

    static {
        USER_AK = "7fb8ade4b3354580b3fda59a5f92edf0";
        USER_SK = "a3b199fefaf24a809afad6c4e3061d55";
        Credential credential = new Credential(CloudConfig.USER_AK, USER_SK);
        client = new ECloudDefaultClient(credential, Region.POOL_SZ);
    }
}
