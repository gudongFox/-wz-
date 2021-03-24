package com.cmcu.mcc.comm;

import com.cmcu.common.entity.CommonConfig;
import com.cmcu.common.service.CommonAttachService;
import com.cmcu.common.service.CommonConfigService;
import com.common.wx.service.BaseService;
import com.common.wx.service.MeetingRoomService;
import com.common.wx.service.MessageService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

@Component
public class InitDataCache {

    @Resource
    CommonConfigService commonConfigService;


    @PostConstruct
    public void init() {
        CommonConfig commonConfig = commonConfigService.getConfig();
        MccConst.DEFAULT_URL = commonConfig.getDefaultUrl();
        MccConst.APP_CODE = commonConfig.getAppCode();
        BaseService.CORPID = commonConfig.getWxCorpId();
        BaseService.APPSECRET = commonConfig.getWxCorpSecret();
        BaseService.AGENTID = commonConfig.getWxAgentId();
        BaseService.DEFAULTURL = commonConfig.getDefaultUrl();
        MeetingRoomService.SECRET = commonConfig.getMeetingRoomSecret();
        MessageService.ENABLE = commonConfig.getEnableWx();
        MessageService.NoticeAgentId = commonConfig.getNoticeAgentId();
        MessageService.NoticeSecret = commonConfig.getNoticeSecret();
        CommonAttachService.BASE_PATH=commonConfig.getDirPath();
    }

    @PreDestroy
    public void destroy() {

    }
}
