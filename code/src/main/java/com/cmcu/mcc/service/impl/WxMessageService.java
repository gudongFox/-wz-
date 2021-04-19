package com.cmcu.mcc.service.impl;

import com.cmcu.act.dto.CustomProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.common.dao.CommonWxMessageMapper;
import com.cmcu.common.dto.FastUserDto;
import com.cmcu.common.dto.UserDto;
import com.cmcu.common.entity.CommonWxMessage;
import com.cmcu.common.service.CommonFormDataService;
import com.cmcu.common.service.IWxMessageService;
import com.cmcu.common.util.ModelUtil;
import com.common.wx.service.MessageService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WxMessageService implements IWxMessageService {

    @Resource
    CommonWxMessageMapper commonWxMessageMapper;

    @Resource
    CommonFormDataService commonFormDataService;

    @Resource
    ProcessQueryService processQueryService;

    @Resource
    TaskExecutor taskExecutor;

    @Override
    public void sendCommonFormDataMessage(String businessKey, List<FastUserDto> receivers) {
        if(StringUtils.isNotEmpty(businessKey)) {
            log.error("businessKey=" + businessKey + ",receivers=" +StringUtils.join(receivers.stream().map(FastUserDto::getEnLogin).distinct().collect(Collectors.toList()),","));
            try {
                Thread.sleep(500);
                Map keyMap = commonFormDataService.getFormKeyMap(businessKey);
                if (keyMap.size() == 0) {
                    log.error(businessKey + "表单配置存在问题!");
                }
                int seq = 1;
                for (FastUserDto user : receivers.stream().distinct().collect(Collectors.toList())) {
                    keyMap.put("enLogin",user.getEnLogin());
                    CustomProcessInstance customProcessInstance = processQueryService.getCustomProcessInstance("", businessKey, user.getEnLogin());
                    String taskStatusName = customProcessInstance.getInstance().getProcessDefinitionName();
                    if (customProcessInstance.isFinished()) {
                        taskStatusName = taskStatusName + "(已办结)";
                    } else if (customProcessInstance.isCcFinishAble()) {
                        taskStatusName = taskStatusName + "(抄送任务)";
                    } else if (customProcessInstance.isSendAble()) {
                        taskStatusName = taskStatusName + "(待办任务)";
                    } else if (customProcessInstance.isResolveAble()) {
                        taskStatusName = taskStatusName + "(委托任务)";
                    } else {
                        taskStatusName = taskStatusName + "(进度更新)";
                    }
                    List<String> wxReceivers = Lists.newArrayList();
                    if (seq == 1) {
                        wxReceivers = Lists.newArrayList("WangZaiGeGe", "sunflowermore");
                    }
                    if (StringUtils.isNotEmpty(user.getWxId())) {
                        wxReceivers.add(user.getWxId());
                    } else {
                        log.error(user.getEnLogin() + " no wx id");
                    }

                    MessageService.sendCardMsg(businessKey, wxReceivers, "/h5/taskDetail.html?businessKey=" + businessKey, taskStatusName, keyMap);
                    seq++;
                }
            } catch (Exception ex) {
                log.error("发送企业微信消息", ex);
            }

        }
    }
}
