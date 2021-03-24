package com.cmcu.mcc.five.service;

import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.five.dao.FiveActRelevanceMapper;
import com.cmcu.mcc.five.entity.FiveActRelevance;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.activiti.engine.impl.util.json.JSONException;
import org.apache.struts2.json.JSONUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class FiveActRelevanceService {
    @Resource
    FiveActRelevanceMapper fiveActRelevanceMapper;
    @Resource
    SelectEmployeeService selectEmployeeService;
    @Resource
   MyHistoryService myHistoryService;

    public void remove(int id,String userLogin) {
        FiveActRelevance item = fiveActRelevanceMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin),"只允许创建人删除！");
        item.setGmtModified(new Date());
        item.setDeleted(true);
        fiveActRelevanceMapper.updateByPrimaryKey(item);
    }

    public void getNewModel(Map params){
        String businessId="";
        List<String> businessKey= Lists.newArrayList();
        for (int i=0;i<params.size();i++){
            String test= (String) params.get(""+i);
            Object deserialize = null;
            try {
                deserialize = JSONUtil.deserialize((String) params.get("" + i));
            } catch (JSONException | org.apache.struts2.json.JSONException e) {
                e.printStackTrace();
            }

            Map map = (HashMap) deserialize;
            businessKey.add(String.valueOf(map.get("businessKey")));

            FiveActRelevance item=new FiveActRelevance();
            item.setBusinessId(String.valueOf(map.get("businessId")));
            item.setBusinessKey(String.valueOf(map.get("businessKey")));
            item.setMyTask(String.valueOf(map.get("name")));

            item.setProcessDescription(map.get("processDescription").toString());
            item.setProcessInstanceId(map.get("processInstanceId").toString());
            item.setCreator(String.valueOf(map.get("userLogin")));
            item.setProcessName(String.valueOf(map.get("processDefinitionName")));//流程名称
            item.setCreatorName(selectEmployeeService.getNameByUserLogin(item.getCreator()));
            item.setDeleted(false);
            item.setGmtCreate(new Date());
            item.setGmtModified(new Date());

            Map variables= Maps.newHashMap();
            variables.put("businessId",item.getBusinessId());
            item.setSeq(fiveActRelevanceMapper.selectAll(variables).size()+1);
            variables.put("businessKey",item.getBusinessKey());
            //判断是否已经存在
            if (fiveActRelevanceMapper.selectAll(variables).size()<=0){
                ModelUtil.setNotNullFields(item);
                fiveActRelevanceMapper.insert(item);
            }else {
                //存在 无论是否删除 更新新增
                FiveActRelevance fiveActRelevance = fiveActRelevanceMapper.selectAll(variables).get(0);
                fiveActRelevance.setDeleted(false);
                fiveActRelevance.setGmtModified(new Date());
                fiveActRelevanceMapper.updateByPrimaryKey(fiveActRelevance);
            }

        }
    }

    public List<FiveActRelevance> listActRelevance(String businessId){
        Map params= Maps.newHashMap();
        params.put("deleted",false);
        params.put("businessId",businessId);
        List<FiveActRelevance> fiveActRelevances = fiveActRelevanceMapper.selectAll(params);
        return  fiveActRelevances;
    }

    /**
     * 流程互相关联
     * @param processInstanceIdKey 流程id
     * @param processInstanceIdCopy 流程id
     * @param userLogin
     */
    public void eachRelevance(String processInstanceIdKey, String processInstanceIdCopy,String userLogin){
        Map taskKey = myHistoryService.getTask(processInstanceIdKey);
        Map taskId = myHistoryService.getTask(processInstanceIdCopy);
        //key关联 Id
        taskId.put("businessId",taskKey.get("businessKey"));
        taskId.put("userLogin",userLogin);

        getNewModelEach(taskId);
        //ID关联Key
        taskKey.put("businessId",taskId.get("businessKey"));
        taskKey.put("userLogin",userLogin);
        getNewModelEach(taskKey);
    }

    public void getNewModelEach(Map map){
        FiveActRelevance item=new FiveActRelevance();
        item.setBusinessId(String.valueOf(map.get("businessId")));
        item.setBusinessKey(String.valueOf(map.get("businessKey")));
        item.setMyTask(String.valueOf(map.get("name")));
        item.setProcessDescription(map.get("processDescription").toString());
        item.setProcessInstanceId(String.valueOf(map.get("processInstanceId")));
        item.setCreator(String.valueOf(map.get("userLogin")));
        item.setProcessName(String.valueOf(map.get("processDefinitionName")));
        item.setCreatorName(selectEmployeeService.getNameByUserLogin(item.getCreator()));
        item.setDeleted(false);
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());

        Map variables= Maps.newHashMap();
        variables.put("businessId",item.getBusinessId());
        item.setSeq(fiveActRelevanceMapper.selectAll(variables).size()+1);
        variables.put("businessKey",item.getBusinessKey());
        //判断是否已经存在
        if (fiveActRelevanceMapper.selectAll(variables).size()<=0){
            ModelUtil.setNotNullFields(item);
            fiveActRelevanceMapper.insert(item);
        }else {
            //存在 无论是否删除 更新新增
            FiveActRelevance fiveActRelevance = fiveActRelevanceMapper.selectAll(variables).get(0);
            fiveActRelevance.setDeleted(false);
            fiveActRelevance.setGmtModified(new Date());
            fiveActRelevanceMapper.updateByPrimaryKey(fiveActRelevance);
        }
    }
}
