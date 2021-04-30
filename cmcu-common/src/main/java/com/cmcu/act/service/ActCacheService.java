package com.cmcu.act.service;

import com.cmcu.act.dto.ActBpmnModel;
import com.cmcu.common.util.GuavaCacheService;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Component
public class ActCacheService {

    @Resource
    GuavaCacheService guavaCacheService;

    @Resource
    RepositoryService repositoryService;

    @Resource
    ModelService modelService;



    public ActBpmnModel getActBpmnModel(String processDefinitionId) {
        if(StringUtils.isEmpty(processDefinitionId)) return null;
        String key = "processDefinitionId_bpmn_" + processDefinitionId;
        ActBpmnModel actBpmnModel = guavaCacheService.get(key, () -> Optional.of(modelService.getActBpmnModel(processDefinitionId)));
        return actBpmnModel;
    }

    public ProcessDefinition getProcessDefinition(String processDefinitionKey) {
        if(StringUtils.isEmpty(processDefinitionKey)) return null;
        String key = "processDefinitionKey_" + processDefinitionKey;
        ProcessDefinition processDefinition = guavaCacheService.get(key, () -> {
            List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processDefinitionKey).orderByDeploymentId().desc().list();
            if(processDefinitionList.size()==0){
                System.out.println(processDefinitionKey);
            }

            Assert.state(processDefinitionList.size()>0,"not found "+processDefinitionKey+" process!");
            return Optional.of(processDefinitionList.get(0));
        });
        return processDefinition;
    }

    public ProcessDefinition getProcessDefinitionById(String processDefinitionId) {
        String key = "processDefinitionId_" + processDefinitionId;
        ProcessDefinition processDefinition = guavaCacheService.get(key, () -> {
            ProcessDefinition item = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
            Assert.state(item!=null,"not found "+processDefinitionId+" process!");
            return Optional.of(item);
        });
        return processDefinition;
    }


    public List<ProcessDefinition> listProcessDefinitions() {
        String key = "processDefinition_all";
        List<ProcessDefinition> processDefinitions = guavaCacheService.get(key, () -> {
            List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().orderByDeploymentId().desc().list();
            return Optional.of(processDefinitionList);
        });
        return processDefinitions;
    }

    public void cleanProcessDefinitionCache(String processDefinitionKey) {
        String key = "processDefinitionKey_" + processDefinitionKey;
        guavaCacheService.invalidate(key);
    }

}
