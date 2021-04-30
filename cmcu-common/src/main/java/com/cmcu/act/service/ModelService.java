package com.cmcu.act.service;

import com.cmcu.act.dto.ActBpmnModel;

import com.cmcu.act.extend.CustomBpmnJsonConverter;
import com.cmcu.act.extend.CustomUserTaskJsonConverter;
import com.cmcu.common.dto.CommonCodeDto;
import com.cmcu.common.entity.CommonCode;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.JsonMapper;

import com.cmcu.common.util.WebUtil;
import com.cmcu.common.service.CommonUserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static com.cmcu.common.util.JsonMapper.objectMapper;

@Slf4j
@Service
public class ModelService implements ModelDataJsonConstants {

    String MODEL_KEY = "modelKey";
    String MODEL_CATEGORY="modelCategory";


    @Resource
    RepositoryService repositoryService;

    @Resource
    ActCacheService actCacheService;

    @Resource
    CommonUserService commonUserService;

    @Resource
    CommonCodeService commonCodeService;

    public List<Map> listModelCategory(String enLogin) {

        String tenetId=commonUserService.getTenetId(enLogin);
        List<CommonCodeDto> oList=commonCodeService.listDataByCatalog(tenetId,"流程类别");
        List<Map> list = Lists.newArrayList();
        Map all = Maps.newHashMap();
        all.put("code", "");
        all.put("name", "全部");
        list.add(all);
        for (CommonCode commonCode : oList) {
            Map item = Maps.newHashMap();
            item.put("code", commonCode.getCode());
            item.put("name", commonCode.getName());
            list.add(item);
        }
        return list;
    }



    public PageInfo<Object> listPagedData(int pageNum, int pageSize, Map params) {
        ModelQuery modelQuery = repositoryService.createModelQuery();
        if (params.containsKey("modelCategory")) {
            String modelCategory = params.get("modelCategory").toString();
            modelQuery = modelQuery.modelCategory(modelCategory);

        }
        if (params.containsKey("modelName")) {
            String modelName = params.get("modelName").toString();
            modelQuery = modelQuery.modelNameLike("%" + modelName + "%");
        }
        if (params.containsKey("modelTenetId")) {
            String modelTenetId = params.get("modelTenetId").toString();
            modelQuery = modelQuery.modelTenantIdLike("%" + modelTenetId + "%");
        }
        List<Model> list = modelQuery.orderByLastUpdateTime()
                .desc().listPage((pageNum - 1) * pageSize, pageSize);
        return WebUtil.buildPageInfo(pageNum, pageSize, (int) modelQuery.count(), list);
    }

    public String getNewModel(String enLogin) throws UnsupportedEncodingException {
        Model model = repositoryService.newModel();
        String name = "hello-world-" + DateFormatUtils.format(new Date(), "yyyyMMddHH");
        String key = "hello-world";

        Map editorNode =Maps.newHashMap();
        editorNode.put("id", "canvas");
        editorNode.put("resourceId", "canvas");
        ObjectNode stencilSetNode = objectMapper.createObjectNode();
        stencilSetNode.put("namespace",
                "http://b3mn.org/stencilset/bpmn2.0#");
        editorNode.put("stencilset", stencilSetNode);



        Map metaInfo = Maps.newHashMap();
        metaInfo.put(MODEL_NAME, name);
        metaInfo.put(MODEL_DESCRIPTION, "");
        metaInfo.put(MODEL_REVISION, 1);
        metaInfo.put(MODEL_KEY, key);
        model.setName(name);
        model.setKey(key);
        model.setVersion(1);


        Map properties=Maps.newHashMap();
        properties.put("name",name);
        if(StringUtils.isNotEmpty(enLogin)){
            properties.put("process_author",enLogin);
            model.setTenantId(commonUserService.getTenetId(enLogin));
            ModelQuery modelQuery=repositoryService.createModelQuery();
            if(StringUtils.isNotEmpty(model.getTenantId())){
                modelQuery=modelQuery.modelTenantId(model.getTenantId());
            }

            if(modelQuery.count()>0) {
                Model last =modelQuery.orderByCreateTime().desc().listPage(0, 1).get(0);
                String modelCategory = JsonMapper.string2Map(last.getMetaInfo()).getOrDefault(MODEL_CATEGORY, "").toString();
                metaInfo.put(MODEL_CATEGORY, modelCategory);
                properties.put("process_namespace", modelCategory);
            }
        }
        editorNode.put("properties",properties);

        model.setMetaInfo(JsonMapper.obj2String(metaInfo));
        repositoryService.saveModel(model);
        repositoryService.addModelEditorSource(model.getId(), JsonMapper.obj2String(editorNode).getBytes("utf-8"));
        return model.getId();
    }

    public void remove(String id){
        repositoryService.deleteModel(id);
    }

    public String  deployment(String id) throws IOException {
        Model model = repositoryService.getModel(id);
        Map metaInfo = JsonMapper.string2Map(model.getMetaInfo());
        byte[] bytes = repositoryService.getModelEditorSource(model.getId());
        Assert.state(bytes != null, "模型数据为空，请先设计流程并成功保存，再进行发布。");

        JsonNode modelNode = new ObjectMapper().readTree(bytes);
        CustomBpmnJsonConverter.getConvertersToBpmnMap().put("UserTask", CustomUserTaskJsonConverter.class);
        BpmnModel bpmnModel = new CustomBpmnJsonConverter().convertToBpmnModel(modelNode);
        Assert.state(bpmnModel.getProcesses().size() > 0, "数据模型不符要求，请至少设计一条主线流程。");

        List<String> gateways=bpmnModel.getMainProcess().findFlowElementsOfType(Gateway.class).stream().map(BaseElement::getId).collect(Collectors.toList());
        Assert.state(gateways.size()%2==0,"所有网关必须成对出现");
        for(String gateWayId:gateways) {
            Assert.state(gateWayId.indexOf("begin_") == 0 || gateWayId.indexOf("end_") == 0, "网关名称（" + gateWayId + ")必须以begin_/end_开始!");
            if (gateWayId.indexOf("begin_") == 0) {
                String endId= "end_" +StringUtils.replace(gateWayId, "begin_", "");
                long endCount = gateways.stream().filter(p -> p.equalsIgnoreCase(endId)).count();
                Assert.state(endCount == 1, gateWayId + "对应的结尾"+endId+"应为1个("+endCount+").");
            } else {
                String beginId= "begin_" +StringUtils.replace(gateWayId, "end_", "");
                long beginCount = gateways.stream().filter(p -> p.equalsIgnoreCase(beginId)).count();
                Assert.state(beginCount == 1, gateWayId + "对应的开始"+beginId+"应为1个("+beginCount+").");
            }
        }

        byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(bpmnModel);

        String modelKey= metaInfo.get(MODEL_KEY).toString();
        String modelCategory=metaInfo.get(MODEL_CATEGORY).toString();

        //发布流程
        String processName = model.getTenantId() + "_" + modelKey+ ".bpmn20.xml";
        Deployment deployment = repositoryService.createDeployment()
                .name(model.getName())
                .tenantId(model.getTenantId())
                .category(modelCategory)
                .key(modelKey)
                .deploymentProperty("description","hello_world_description")
                .addString(processName, new String(bpmnBytes, "UTF-8"))
                .deploy();
        model.setDeploymentId(deployment.getId());
        repositoryService.saveModel(model);

        ProcessDefinition processDefinition=repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        actCacheService.cleanProcessDefinitionCache(processDefinition.getKey());
        return deployment.getId();
    }

    public void save(String modelId, MultiValueMap<String, String> values){
        try {

            Model model = repositoryService.getModel(modelId);
            Map metaInfo=JsonMapper.string2Map(model.getMetaInfo());
            Map json_xml=JsonMapper.string2Map(values.getFirst("json_xml"));
            Map properties=(Map)json_xml.get("properties");

            String processId= properties.get("process_id").toString();
            String processName=properties.get("name").toString();
            String processDescription=properties.get("documentation").toString();
            String processCategory=properties.get("process_namespace").toString();

            metaInfo.put(MODEL_NAME,processName);
            metaInfo.put(MODEL_DESCRIPTION,processDescription);
            metaInfo.put(MODEL_KEY,processId);
            metaInfo.put(MODEL_CATEGORY,processCategory);

            model.setMetaInfo(JsonMapper.obj2String(metaInfo));
            model.setCategory(processCategory);
            model.setKey(processId);
            model.setName(processName);
            repositoryService.saveModel(model);
            repositoryService.addModelEditorSource(model.getId(), JsonMapper.obj2String(json_xml).getBytes("utf-8"));

            InputStream svgStream = new ByteArrayInputStream(values.getFirst("svg_xml").getBytes("utf-8"));
            TranscoderInput input = new TranscoderInput(svgStream);

            PNGTranscoder transcoder = new PNGTranscoder();
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            TranscoderOutput output = new TranscoderOutput(outStream);

            transcoder.transcode(input, output);
            final byte[] result = outStream.toByteArray();
            repositoryService.addModelEditorSourceExtra(model.getId(), result);
            outStream.close();

        } catch (Exception e) {
            log.error("Error saving model", e);
        }

    }


    public ActBpmnModel getActBpmnModel(String processDefinitionId) {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        ProcessDefinition processDefinition= repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();

        ActBpmnModel model = new ActBpmnModel(bpmnModel);
        model.setTenetId(processDefinition.getTenantId());
        model.setName(processDefinition.getName());
        StartEvent startEvent = bpmnModel.getMainProcess().findFlowElementsOfType(StartEvent.class).get(0);
        EndEvent endEvent = bpmnModel.getMainProcess().findFlowElementsOfType(EndEvent.class).get(0);

        List<FlowNode> simplestPath = Lists.newArrayList();
        recursiveGatewayPath(bpmnModel, startEvent, simplestPath, endEvent.getId());
        model.setSimplestPath(simplestPath);
        List<String> gateWayIds = bpmnModel.getMainProcess().findFlowElementsOfType(Gateway.class).stream().map(BaseElement::getId).collect(Collectors.toList());
        for (String beginId : gateWayIds.stream().filter(p -> p.contains("begin_")).collect(Collectors.toList())) {
            Map path = Maps.newHashMap();
            path.put("beginId", beginId);
            String endId = "end_" + StringUtils.replace(beginId, "begin_", "");
            Gateway beginGateWay = (Gateway) bpmnModel.getMainProcess().getFlowElement(beginId);
            Gateway endGateWay = (Gateway) bpmnModel.getMainProcess().getFlowElement(endId);
            for (SequenceFlow sequenceFlow : beginGateWay.getOutgoingFlows()) {
                FlowNode preNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(sequenceFlow.getTargetRef());
                List<FlowNode> nodes = Lists.newArrayList();
                nodes.add(beginGateWay);
                recursiveGatewayPath(bpmnModel, preNode, nodes, endId);
                nodes.add(endGateWay);
                model.getPathList().add(nodes);
            }
        }
        return model;
    }

    public void recursiveGatewayPath(BpmnModel bpmnModel, FlowNode preNode, List<FlowNode> nodes, String endId){
        String preNodeId=preNode.getId();
        if(preNode instanceof EndEvent||preNodeId.equalsIgnoreCase(endId)){
            return;
        }
        nodes.add(preNode);
        if(preNode.getId().indexOf("begin_")==0&&(preNode instanceof Gateway)){
            FlowNode nextNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement("end_" + StringUtils.replace(preNode.getId(), "begin_", ""));
            recursiveGatewayPath(bpmnModel, nextNode, nodes, endId);
        }else {
            //出线肯定只有一条
            SequenceFlow sequenceFlow = preNode.getOutgoingFlows().get(0);
            FlowNode nextNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(sequenceFlow.getTargetRef());
            recursiveGatewayPath(bpmnModel, nextNode, nodes, endId);
        }
    }



    //xxin  12.16
    public String uploadBpmn(MultipartFile file,String enLogin) throws Exception {
        InputStream inputStream = file.getInputStream();
        BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
        XMLStreamReader xmlStreamReader;
        try {
            xmlStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(inputStream);
        } catch (XMLStreamException e) {
            log.error("xml转换失败", e);
            e.printStackTrace();
            throw new Exception("xml转换失败");
        }
        BpmnModel bpmnModel = xmlConverter.convertToBpmnModel(xmlStreamReader);
        BpmnJsonConverter bpmnJsonConverter = new BpmnJsonConverter();
        ObjectNode modelNode = bpmnJsonConverter.convertToJson(bpmnModel);
        Model model = repositoryService.newModel();
        List<Process> processes = bpmnModel.getProcesses();
        String name = "newProcess";
        String key = "process";
        Integer version = 1;
        if (processes.size() > 0) {
            if (StringUtils.isNotBlank(processes.get(0).getName())) {
                name = processes.get(0).getName();
            }
            if (StringUtils.isNotBlank(processes.get(0).getId())) {
                key = processes.get(0).getId();
            }
        }
        model.setName(name);
        model.setKey(key);

        List<Model> modelList = repositoryService.createModelQuery().modelKey(key).orderByModelVersion()
                .desc().list();
        if (modelList.size() > 0) {
            version = modelList.get(0).getVersion() + 1;
        }
        model.setVersion(version);
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put(ModelDataJsonConstants.MODEL_NAME, name);
        objectNode.put(ModelDataJsonConstants.MODEL_REVISION, String.valueOf(version));

        objectNode.put(MODEL_DESCRIPTION, "");
        objectNode.put(MODEL_KEY, key);

        if(StringUtils.isNotEmpty(enLogin)){
            objectNode.put("process_author",enLogin);
            model.setTenantId(commonUserService.getTenetId(enLogin));
            ModelQuery modelQuery=repositoryService.createModelQuery();
            if(StringUtils.isNotEmpty(model.getTenantId())){
                modelQuery=modelQuery.modelTenantId(model.getTenantId());
            }

            if(modelQuery.count()>0) {
                Model last =modelQuery.orderByCreateTime().desc().listPage(0, 1).get(0);
                String modelCategory = JsonMapper.string2Map(last.getMetaInfo()).getOrDefault(MODEL_CATEGORY, "").toString();
                objectNode.put(MODEL_CATEGORY, modelCategory);
                objectNode.put("process_namespace", modelCategory);
            }
        }


        model.setMetaInfo(objectNode.toString());
        repositoryService.saveModel(model);
        repositoryService
                .addModelEditorSource(model.getId(), modelNode.toString().getBytes(StandardCharsets.UTF_8));


        return name;
    }

    public void downloadModel(String id, HttpServletResponse response) throws Exception {
        //根据数据库id获取model
        Model model = repositoryService.getModel(id);
        JsonNode editorNode;
        try {
            //获取核心数据
            editorNode = new ObjectMapper().readTree(repositoryService.getModelEditorSource(id));
        } catch (IOException e) {
            log.error("流程model转换为JsonNode失败", e);
            throw new Exception("流程model转换为JsonNode失败");
        }
        //JsonNode转换为BpmnNode
        BpmnJsonConverter bpmnJsonConverter = new BpmnJsonConverter();
        BpmnModel bpmnModel = bpmnJsonConverter.convertToBpmnModel(editorNode);

        byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(bpmnModel, "utf-8");
        String fileName = model.getName() + ".bpmn20.xml";

        response.reset();
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder
                .encode(fileName, "utf-8"));
        response.setCharacterEncoding("utf-8");
        response.setContentLength(bpmnBytes.length);
        response.setContentType("application/force-download;charset=utf-8");
        OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        outputStream.write(bpmnBytes);
        outputStream.flush();
        outputStream.close();
    }
}
