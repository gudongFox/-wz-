package com.cmcu.act.service;

import com.cmcu.common.util.WebUtil;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.DynamicBpmnService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;

import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ProcessDefinitionService {


    @Resource
    RepositoryService repositoryService;

    @Resource
    HistoryService historyService;



    /**
     * 流程定义
     * @param pageNum
     * @param pageSize
     * @param params
     * @return
     */
    public PageInfo<Object> listPagedData(int pageNum, int pageSize, Map params) {
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        if (params.containsKey("modelKey")) {
            String modelKey = params.get("modelKey").toString();
            processDefinitionQuery = processDefinitionQuery.processDefinitionKeyLike("%" + modelKey + "%");
        }
        if (params.containsKey("modelCategory")) {
            String modelCategory = params.get("modelCategory").toString();
            processDefinitionQuery = processDefinitionQuery.processDefinitionCategory(modelCategory);
        }
        if (params.containsKey("modelName")) {
            String modelName = params.get("modelName").toString();
            processDefinitionQuery = processDefinitionQuery.processDefinitionNameLike("%" + modelName + "%");
        }
        if (params.containsKey("modelTenetId")) {
            String modelTenetId = params.get("modelTenetId").toString();
            processDefinitionQuery = processDefinitionQuery.processDefinitionTenantIdLike("%" + modelTenetId + "%");
        }

        List<ProcessDefinition> oList = processDefinitionQuery.orderByDeploymentId().desc().listPage((pageNum - 1) * pageSize, pageSize);
        List<Map> list = Lists.newArrayList();
        oList.forEach(o -> {
            Map map = Maps.newHashMap();
            map.put("tenantId", o.getTenantId());
            map.put("name", o.getName());
            map.put("key", o.getKey());
            map.put("deploymentId", o.getDeploymentId());
            map.put("id", o.getId());
            map.put("version", o.getVersion());
            map.put("category", o.getCategory());
            map.put("deleted", historyService.createHistoricProcessInstanceQuery().processDefinitionId(o.getId()).deleted().count());
            map.put("finished", historyService.createHistoricProcessInstanceQuery().processDefinitionId(o.getId()).notDeleted().finished().count());
            map.put("unfinished", historyService.createHistoricProcessInstanceQuery().processDefinitionId(o.getId()).notDeleted().unfinished().count());
            list.add(map);
        });
        return WebUtil.buildPageInfo(pageNum, pageSize, (int) processDefinitionQuery.count(), list);
    }

    public void remove(String deploymentId){
        ProcessDefinition processDefinition=repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).singleResult();
        long count=historyService.createHistoricProcessInstanceQuery().processDefinitionId(processDefinition.getId()).count();
        Assert.state(count==0,"已运行实例化流程("+count+"),无法删除!");
        repositoryService.deleteDeployment(deploymentId);
    }

    public InputStream  getPngInputStream(String deploymentId){
        ProcessDefinition processDefinition=repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
        ProcessDiagramGenerator p = new DefaultProcessDiagramGenerator();
        InputStream inputStream=p.generateDiagram(bpmnModel,"png","微软雅黑","微软雅黑",null,null,1.0);
        return  inputStream;

    }



}
