package com.common.wx.service;

import com.cmcu.common.dao.CommonWxMessageMapper;
import com.cmcu.common.entity.CommonWxMessage;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.web.ApplicationContextHelper;
import com.common.model.JsonData;
import com.common.util.JsonMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.util.Assert;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class MessageService {

    public static Boolean ENABLE=true;

    public static JsonData sendCardMsg(String businessKey,List<String> wxIds,String url,String title,Map keyMap) {


        boolean checkedKeyMapGood=false;
        if(keyMap!=null&&keyMap.size()>0) {
            for (Object key : keyMap.keySet()) {
                if (keyMap.get(key) != null && StringUtils.isNotEmpty(keyMap.get(key).toString())) {
                    checkedKeyMapGood = true;
                    break;
                }
            }
        }

        if(!checkedKeyMapGood){
            return JsonData.fail(businessKey+"通用表单未配置重要显示字段("+title+")");
        }


        if(!url.startsWith("http")) url=BaseService.DEFAULTURL+url;

        StringBuilder description=new StringBuilder();
        for(Object key:keyMap.keySet()){
            if(!key.toString().equalsIgnoreCase("enLogin")) {
                if (keyMap.get(key) != null) {
                    description.append("<div>" + key.toString() + "：" + keyMap.get(key).toString() + "</div>");
                }
            }
        }
        Assert.state(StringUtils.isNotEmpty(description.toString()), "概述不能为空!");


        JsonData tokenResult;
        if(ENABLE) {
            tokenResult=BaseService.getAccessToken();
        }else{
            tokenResult=JsonData.success("virtual token");
        }
        CommonWxMessageMapper commonWxMessageMapper= ApplicationContextHelper.popBean(CommonWxMessageMapper.class);
        CommonWxMessage commonWxMessage = new CommonWxMessage();
        if(commonWxMessageMapper!=null) {
            Date now = new Date();
            commonWxMessage.setRemark(ENABLE ? "" : "模拟发送");
            commonWxMessage.setMsgType("textcard");
            Map msgData = Maps.newHashMap();
            msgData.put("wxIds", wxIds);
            msgData.put("url", url);
            msgData.put("title", title);
            msgData.put("keyMap", keyMap);
            commonWxMessage.setMsgData(JsonMapper.obj2String(msgData));
            commonWxMessage.setToUser(StringUtils.join(wxIds.stream().sorted().collect(Collectors.toList()), "|"));
            commonWxMessage.setMsgUrl(url);
            commonWxMessage.setTaskId(businessKey);
            commonWxMessage.setMsgTitle(title);
            CommonWxMessage pre=commonWxMessageMapper.getLatestTryMessage(commonWxMessage.getMsgType(),commonWxMessage.getMsgTitle(),commonWxMessage.getMsgUrl(),commonWxMessage.getToUser());
            if(pre!=null){
                if(DateUtils.addHours(pre.getTryTime(),2).getTime()>now.getTime()){
                    String errorInfo="请勿重复发送(2h）上一次尝试时间："+ DateFormatUtils.format(pre.getTryTime(),"yyyy-MM-dd HH:mm:ss")+"）";
                    log.error(errorInfo);
                    return JsonData.success(errorInfo);
                }
            }
            commonWxMessage.setSended(!ENABLE);
            commonWxMessage.setTryCount(1);
            commonWxMessage.setTryTime(now);
            commonWxMessage.setGmtCreate(now);
            commonWxMessage.setGmtModified(now);
            ModelUtil.setNotNullFields(commonWxMessage);
            commonWxMessageMapper.insert(commonWxMessage);
        }
        String wxUrl = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token="+tokenResult.getData().toString();
        Map params= Maps.newHashMap();
        params.put("touser",StringUtils.join(wxIds,"|"));
        params.put("msgtype","textcard");
        params.put("agentid", BaseService.AGENTID);
        Map textcard=Maps.newHashMap();
        textcard.put("title",title);


        textcard.put("description",description.toString());
        textcard.put("url",url);
        textcard.put("btntxt","详情");
        textcard.put("enable_duplicate_check",1);
        params.put("textcard",textcard);
        JsonData result;

        if(ENABLE) {
            result=BaseService.httpPost(wxUrl, JsonMapper.obj2String(params));
        }else {
            result = JsonData.success("模拟成功");
        }

        if(commonWxMessageMapper!=null){
            commonWxMessage.setSended(result.getRet());
            if(!result.getRet()) {
                commonWxMessage.setRemark(result.getMsg());
            }
            commonWxMessageMapper.updateByPrimaryKey(commonWxMessage);
        }
        return result;
    }


    public static Integer NoticeAgentId =1000036;

    public static String NoticeSecret ="X9b_JGgapxK0V0ssNojX5BTkbZV0BJ8WpPyi_1tD57I";


    /**
     * 发送图片消息
     * @param wxIds 小于1000
     * @param url "/h5/taskDetail.html?businessKey=" + businessKey
     * @param title
     * @param description
     * @param picUrl
     * @return
     */
    public static JsonData sendPicMessage(List<String> wxIds,String url,String title,String description,String picUrl) {
        wxIds=wxIds.stream().filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
        Assert.state(wxIds.size() > 0 && wxIds.size() < 5000, "接收人数量应大于0且小于5000!");
        Assert.state(StringUtils.isNotEmpty(url), "详情页面不能为空!");
        Assert.state(StringUtils.isNotEmpty(title), "标题不能为空!");
        Assert.state(StringUtils.isNotEmpty(description), "概述不能为空!");
        Assert.state(StringUtils.isNotEmpty(picUrl), "图片不能为空!");

        if (!url.startsWith("http")) url = BaseService.DEFAULTURL + url;
        if (!picUrl.startsWith("http")) picUrl = BaseService.DEFAULTURL + picUrl;

        JsonData tokenResult;
        if(ENABLE) {
            tokenResult=BaseService.getAccessToken(NoticeSecret);
        }else{
            tokenResult=JsonData.success("virtual token");
        }
        CommonWxMessageMapper commonWxMessageMapper= ApplicationContextHelper.popBean(CommonWxMessageMapper.class);
        JsonData result = new JsonData(true);
        for(int i=0;i<=wxIds.size()/1000;i++) {
            int endIndex = (i + 1) * 1000;
            int beginIndex = i * 1000;
            if (endIndex > wxIds.size()) endIndex = wxIds.size();

            CommonWxMessage commonWxMessage = new CommonWxMessage();
            if (commonWxMessageMapper != null) {
                Date now = new Date();
                commonWxMessage.setRemark(ENABLE ? "" : "模拟发送");
                commonWxMessage.setMsgType("news");
                Map data = Maps.newHashMap();
                data.put("wxIds", wxIds.subList(beginIndex, endIndex));
                data.put("url", url);
                data.put("title", title);
                data.put("description", description);
                data.put("picUrl", picUrl);
                commonWxMessage.setToUser(StringUtils.join(wxIds.subList(beginIndex, endIndex).stream().sorted().collect(Collectors.toList()), "|"));
                commonWxMessage.setMsgUrl(url);
                commonWxMessage.setMsgTitle(title);
                CommonWxMessage pre = commonWxMessageMapper.getLatestTryMessage(commonWxMessage.getMsgType(), commonWxMessage.getMsgTitle(), commonWxMessage.getMsgUrl(), commonWxMessage.getToUser());
                if (pre != null) {
                    if (DateUtils.addHours(pre.getTryTime(), 24).getTime() > now.getTime()) {
                        return JsonData.success("请勿重复发送（24h）（上一次尝试时间：" + DateFormatUtils.format(pre.getTryTime(), "yyyy-MM-dd HH:mm:ss") + "）");
                    }
                }

                commonWxMessage.setSended(!ENABLE);
                commonWxMessage.setTryCount(1);
                commonWxMessage.setTryTime(now);
                commonWxMessage.setGmtCreate(now);
                commonWxMessage.setGmtModified(now);
                commonWxMessage.setTaskId("");
                ModelUtil.setNotNullFields(commonWxMessage);
                commonWxMessageMapper.insert(commonWxMessage);
            }

            String wxUrl = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + tokenResult.getData().toString();
            Map params = Maps.newHashMap();
            params.put("touser", StringUtils.join(wxIds.subList(beginIndex, endIndex), "|"));
            params.put("msgtype", "news");
            params.put("agentid", NoticeAgentId > 0 ? NoticeAgentId : BaseService.AGENTID);
            Map news = Maps.newHashMap();
            List<Map> articles = Lists.newArrayList();
            Map article = Maps.newHashMap();
            article.put("title", title);
            article.put("description", description);
            article.put("url", url);
            article.put("picurl", picUrl);
            article.put("enable_duplicate_check", 1);
            articles.add(article);
            news.put("articles", articles);
            params.put("news", news);
            if (ENABLE) {
                result = BaseService.httpPost(wxUrl, JsonMapper.obj2String(params));
            } else {
                result = JsonData.success("模拟发送成功");
            }
            if (commonWxMessageMapper != null) {
                commonWxMessage.setSended(result.getRet());
                if (!result.getRet()) {
                    commonWxMessage.setRemark(result.getMsg());
                }
                commonWxMessageMapper.updateByPrimaryKey(commonWxMessage);
            }
        }
        return result;
    }

    public static  String getAuth2Url(String redirect_url) throws UnsupportedEncodingException {
        String url = " https://open.weixin.qq.com/connect/oauth2/authorize" +
                "?appid=" + BaseService.CORPID +
                "&redirect_uri=" + BaseService.DEFAULT_CALLBACK_URL + "?url=index" +
                "&response_type=code&scope=snsapi_base&state=pyl#wechat_redirect";
        return url;
    }





}
