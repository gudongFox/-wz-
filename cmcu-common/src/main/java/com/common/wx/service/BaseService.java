package com.common.wx.service;

import com.common.util.CryptionUtil;
import com.common.model.JsonData;
import com.common.util.JsonMapper;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.*;




@Slf4j
public class BaseService {




    public static String CORPID = "ww9a29cc2a4f707781";

    public static String APPSECRET = "EdI1zd85TUFatbWfBc0Qtq4EvSuZl1WSPhTyDQ2fVt4";

    public static Integer AGENTID = 1000031;

    public static String DEFAULTURL = "https://co.wuzhou.com.cn";

    public static String DEFAULT_CALLBACK_URL = DEFAULTURL + "/wx/auth";

    private static Date jsApiTicketExpiresTime=new Date();
    private static String jsApiTicket="";


    public static Map cachedTokenMap= Maps.newHashMap();


    /**
     * 得到accessToken
     * @param appSecret 应用密钥
     * @return
     */
    public static synchronized JsonData getAccessToken(String appSecret) {
        if (StringUtils.isEmpty(appSecret)) {
            appSecret = APPSECRET;
        }
        if (cachedTokenMap.containsKey(appSecret)) {
            Map token = (Map) cachedTokenMap.get(appSecret);
            Date expiresTime = (Date) token.get("expiresTime");
            if (expiresTime.getTime() > new Date().getTime()) {
                return JsonData.success(token.get("accessToken").toString());
            }
            cachedTokenMap.remove(appSecret);
        }
        String url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=" + CORPID + "&corpsecret=" + appSecret;
        JsonData response = httpGet(url);
        if (response.getRet()) {
            Map result = (Map) response.getData();
            Map token = Maps.newHashMap();
            Date expiresTime = DateUtils.addSeconds(new Date(), Integer.parseInt(result.get("expires_in").toString()) - 60 * 10);
            String accessToken = result.get("access_token").toString();
            token.put("expiresTime", expiresTime);
            token.put("accessToken", accessToken);
            cachedTokenMap.put(appSecret, token);
            return JsonData.success(accessToken);
        }
        log.error(JsonMapper.obj2String(response));
        return response;
    }

    public static synchronized JsonData getAccessToken() {
       return getAccessToken(APPSECRET);
    }


    public static  JsonData getUserIdByCode(String code){
        JsonData tokenResult = BaseService.getAccessToken(APPSECRET);
        if (!tokenResult.getRet()) return tokenResult;
        String url = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token="+tokenResult.getData().toString()+"&code="+code;
        JsonData response = BaseService.httpGet(url);
        if (response.getRet()) {
            Map result = (Map) response.getData();
            return JsonData.success(result.get("UserId").toString());
        }
        return response;
    }




    private static synchronized JsonData getJsApiTicket() {

        if (StringUtils.isNotEmpty(jsApiTicket)) {
            if (jsApiTicketExpiresTime.getTime() > new Date().getTime()) {
                return JsonData.success(jsApiTicket);
            }
        }
        JsonData tokenResult = getAccessToken();
        if (!tokenResult.getRet()) return tokenResult;

        String url = "https://qyapi.weixin.qq.com/cgi-bin/get_jsapi_ticket?access_token=" + tokenResult.getData().toString();
        JsonData response = httpGet(url);
        if (response.getRet()) {
            Map result = (Map) response.getData();
            jsApiTicketExpiresTime = DateUtils.addSeconds(new Date(), Integer.parseInt(result.get("expires_in").toString()) - 10 * 60);
            jsApiTicket = result.get("ticket").toString();
            return JsonData.success(jsApiTicket);
        }
        return response;
    }



    public static String getNonceStr(){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<10;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static JsonData getSignature(String nonceStr,long timeStamp,String url) {

        JsonData jsApiTicketResult = getJsApiTicket();
        if (!jsApiTicketResult.getRet()) return jsApiTicketResult;
        StringBuilder sb = new StringBuilder();
        sb.append("jsapi_ticket=");
        sb.append(jsApiTicketResult.getData().toString());
        sb.append("&noncestr=");
        sb.append(nonceStr);
        sb.append("&timestamp=");
        sb.append(timeStamp);
        sb.append("&url=");
        sb.append(url);
        String encrypt= CryptionUtil.sha1Encrypt(sb.toString());
        return JsonData.success(encrypt);
    }




    public static JsonData httpPost(String url, String data) {
        String resp=null;
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(data,"utf-8"));
            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                HttpEntity entity = response.getEntity();
                resp = EntityUtils.toString(entity, "utf-8");
                EntityUtils.consume(entity);
            } finally {
                response.close();
            }
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
        return transJsonData(resp);
    }

    public static JsonData httpGet(String url) {
        String resp="";
        try {
            String charset = "utf-8";
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response  = httpclient.execute(httpGet);
            try {
                HttpEntity entity = response.getEntity();
                resp = EntityUtils.toString(entity, charset);
                EntityUtils.consume(entity);
            } finally {
                response.close();
            }
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
        return transJsonData(resp);
    }

    private static JsonData transJsonData(String response) {
        if (StringUtils.isEmpty(response)) {
            return JsonData.fail("返回信息为空!");
        }
        try {
            Map result = JsonMapper.string2Map(response);
            String errcode = result.getOrDefault("errcode", "0").toString();
            if (errcode.equals("0")) {
                return JsonData.success(result);
            } else {
                log.error(response);
                return JsonData.fail(errcode + "_" + result.getOrDefault("errmsg", "").toString());
            }
        } catch (Exception ex) {
            log.error("GetJsonResult:", ex);
            return JsonData.fail(ex.getMessage());
        }
    }

}
