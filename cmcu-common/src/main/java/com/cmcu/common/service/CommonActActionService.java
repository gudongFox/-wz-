package com.cmcu.common.service;

import com.cmcu.common.dao.CommonActActionMapper;
import com.cmcu.common.entity.CommonActAction;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Service
public class CommonActActionService {

    @Resource
    CommonActActionMapper commonActActionMapper;

    @Resource
    HistoryService historyService;


    public boolean getStared(String tenetId,String processInstanceId,String enLogin) {
        CommonActAction item= commonActActionMapper.getModel(tenetId,processInstanceId,enLogin);
        if(item==null) return false;
        if(item.getDeleted()) return false;
        return true;
    }

    public void toggleStar(String tenetId,String processInstanceId,String enLogin) {
        Date now=new Date();
        CommonActAction item = commonActActionMapper.getModel(tenetId, processInstanceId, enLogin);
        if (item == null) {
            item = new CommonActAction();
            item.setActionName("star");
            item.setId(0);
            item.setGmtCreate(now);
            item.setDeleted(false);
        }
        item.setEnLogin(enLogin);
        item.setProcessInstanceId(processInstanceId);
        item.setTenetId(tenetId);
        item.setGmtModified(now);

        if(item.getId()==0){
            commonActActionMapper.insert(item);
        }else{
            item.setDeleted(!item.getDeleted());
            commonActActionMapper.updateByPrimaryKey(item);
        }
    }

    public List<String> listStaredUser(String processInstanceId){
        Map params=Maps.newHashMap();
        params.put("processInstanceId",processInstanceId);
        params.put("deleted",false);
        List<CommonActAction> actActions= commonActActionMapper.selectAll(params);
        return actActions.stream().map(p->p.getEnLogin()).distinct().collect(Collectors.toList());
    }



}
