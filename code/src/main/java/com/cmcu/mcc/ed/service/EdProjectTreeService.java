package com.cmcu.mcc.ed.service;


import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.business.dao.BusinessContractMapper;
import com.cmcu.mcc.business.dto.BusinessContractDto;
import com.cmcu.mcc.business.entity.BusinessContract;
import com.cmcu.mcc.business.service.BusinessContractService;
import com.cmcu.mcc.ed.dao.EdProjectStepMapper;
import com.cmcu.mcc.ed.dao.EdProjectTreeMapper;
import com.cmcu.mcc.ed.dao.EdProjectTreeStateMapper;
import com.cmcu.mcc.ed.dto.EdProjectTreeDto;
import com.cmcu.mcc.ed.entity.EdProjectStep;
import com.cmcu.mcc.ed.entity.EdProjectTree;
import com.cmcu.mcc.ed.entity.EdProjectTreeState;
import com.common.model.JsTreeDto;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EdProjectTreeService {

    Logger logger= LoggerFactory.getLogger(getClass());

    @Resource
    EdProjectTreeMapper edProjectTreeMapper;


    @Resource
    EdProjectStepMapper edProjectStepMapper;

    @Resource
    EdProjectTreeStateMapper edProjectTreeStateMapper;


    @Resource
    BusinessContractMapper businessContractMapper;


    @Autowired
    BusinessContractService businessContractService;

    @Resource
    TaskExecutor taskExecutor;


    @Resource
    EdFormService edFormService;




    public EdProjectTreeDto getModelById(int id){
        EdProjectTree edProjectTree=edProjectTreeMapper.selectByPrimaryKey(id);
        return getDto(edProjectTree);
    }

    public List<EdProjectTree> listProjectTree(int projectId,String userLogin) {
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("projectId", projectId);
        Map stateParams = Maps.newHashMap();
        stateParams.put("userLogin", userLogin);
        stateParams.put("projectId",projectId);
        List<EdProjectTreeState> stateList=edProjectTreeStateMapper.selectAll(stateParams);
        List<EdProjectTree> list = edProjectTreeMapper.selectAll(params).stream().sorted(Comparator.comparing(EdProjectTree::getSeq)).collect(Collectors.toList());
        list.forEach(p -> {
            if (stateList.stream().anyMatch(o->o.getNodeId().equals(p.getId()))) {
                EdProjectTreeState state=stateList.stream().filter(o->o.getNodeId().equals(p.getId())).findFirst().get();
                p.setOpened(state.getOpened());
                p.setSelected(state.getSelected());
            }
        });
        return list;
    }

    public List<JsTreeDto> listProjectJsTree(int projectId, Integer stepId, Integer nodeId, String enLogin) {
        List<JsTreeDto> list = Lists.newArrayList();
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("projectId", projectId);
        List<EdProjectTree> oList = edProjectTreeMapper.selectAll(params);
        if (stepId == null && nodeId == null) {
            Map stateParams = Maps.newHashMap();
            stateParams.put("userLogin", enLogin);
            stateParams.put("projectId", projectId);
            List<EdProjectTreeState> stateList = edProjectTreeStateMapper.selectAll(stateParams);
            oList.forEach(p -> {
                if (stateList.stream().anyMatch(o -> o.getNodeId().equals(p.getId()))) {
                    EdProjectTreeState state = stateList.stream().filter(o -> o.getNodeId().equals(p.getId())).findFirst().get();
                    p.setOpened(state.getOpened());
                    p.setSelected(state.getSelected());
                }
            });
        }else {
            if (nodeId != null) {
                for (EdProjectTree tree : oList) {
                    tree.setSelected(tree.getId().equals(nodeId));
                    if (tree.getSelected()) {
                        recursiveOpenParentTree(tree.getParentId(), oList);
                    }
                }
            } else {
                for (EdProjectTree tree : oList) {
                    tree.setSelected(false);
                    if (tree.getNodeUrl().equalsIgnoreCase("ed.detail.stepShow") && tree.getForeignKey().equals(stepId)) {
                        tree.setSelected(true);
                        if (tree.getSelected()) {
                            recursiveOpenParentTree(tree.getParentId(), oList);
                        }
                    }
                }
            }
        }
        for (EdProjectTree tree : oList) {
            JsTreeDto item = new JsTreeDto(tree.getId(), tree.getNodeName(), tree.getParentId() == 0 ? "#" : tree.getParentId().toString());
            item.getState().setOpened(tree.getOpened());
            item.getState().setSelected(tree.getSelected());
            item.getState().setDisabled(tree.getDisabled());
            item.setIcon(tree.getIcon());
            item.setData(tree);
            list.add(item);

        }
        return list;
    }

    private void recursiveOpenParentTree(int parentId,List<EdProjectTree> list){
        if(list.stream().anyMatch(p->p.getId().equals(parentId))){
            EdProjectTree parent=list.stream().filter(p->p.getId().equals(parentId)).findFirst().get();
            parent.setOpened(true);
            recursiveOpenParentTree(parent.getParentId(),list);
        }
    }

    /**
     * ??????????????????
     * @param stepId
     * @return id,parentId,projectId,referId,nodeText,nodeKey
     */
    public List<Map> listCadEdTree(int stepId,String enLogin) {
        List<Map> list = Lists.newArrayList();
        EdProjectStep edProjectStep = edProjectStepMapper.selectByPrimaryKey(stepId);
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("foreignKey", stepId);
        params.put("projectId", edProjectStep.getProjectId());
        for (EdProjectTree edTree : edProjectTreeMapper.selectAll(params)) {
            Map item=Maps.newHashMap();
            item.put("id",edTree.getId());
            item.put("projectId",edTree.getProjectId());
            item.put("referId",edTree.getForeignKey());
            item.put("parentId",edTree.getParentId());
            item.put("nodeText",edTree.getNodeName());
            item.put("nodeKey",edTree.getReferType());
            item.put("nodeUrl",edTree.getNodeUrl());
            item.put("seq",edTree.getSeq());
            item.put("formDataCount", edFormService.getDataCount(edTree.getId()));
            list.add(item);
        }
        return list;
    }


    public List<Map> listCadEdTreeByProject(int projectId,String enLogin) {
        List<Map> list = Lists.newArrayList();
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("foreignKey", projectId);
        params.put("projectId", projectId);
        for (EdProjectTree edTree : edProjectTreeMapper.selectAll(params)) {
            Map item = Maps.newHashMap();
            item.put("id", edTree.getId());
            item.put("projectId", edTree.getProjectId());
            item.put("referId", edTree.getForeignKey());
            item.put("parentId", edTree.getParentId());
            item.put("nodeText", edTree.getNodeName());
            item.put("nodeKey", edTree.getReferType());
            item.put("nodeUrl", edTree.getNodeUrl());
            item.put("formDataCount", edFormService.getDataCount(edTree.getId()));
            item.put("seq", edTree.getSeq());
            list.add(item);
        }
        return list;
    }


    public void setSelectedNode(int projectId,Integer stepId,Integer nodeId,String enLogin) {
        if(StringUtils.isNotEmpty(enLogin)) {
            if (nodeId == null) {
                if (stepId != null) {
                    EdProjectStep step = edProjectStepMapper.selectByPrimaryKey(stepId);
                    Map params = Maps.newHashMap();
                    params.put("deleted", false);
                    params.put("projectId", step.getProjectId());
                    params.put("foreignKey", step.getId());
                    List<EdProjectTree> nodes = edProjectTreeMapper.selectAll(params).stream().sorted(Comparator.comparing(EdProjectTree::getSeq)).collect(Collectors.toList());
                    for (EdProjectTree node : nodes) {
                        if (node.getNodeUrl().equalsIgnoreCase("ed.detail.stepInfo") || node.getNodeUrl().equalsIgnoreCase("five.detail.stepShow")) {
                            nodeId = node.getId();
                            break;
                        }
                    }
                } else {
                    Map params = Maps.newHashMap();
                    params.put("deleted", false);
                    params.put("projectId", projectId);
                    if (PageHelper.count(() -> edProjectTreeMapper.selectAll(params)) > 0) {
                        PageInfo<EdProjectTree> pageInfo = PageHelper.startPage(1, 1).doSelectPageInfo(() -> edProjectTreeMapper.selectAll(params));
                        nodeId = pageInfo.getList().get(0).getId();
                    }
                }
            }
            if (nodeId != null) {
                EdProjectTree edProjectTree = edProjectTreeMapper.selectByPrimaryKey(nodeId);
                if (edProjectTree != null) {
                    rememberNodeState(edProjectTree.getId(), true, "selected", enLogin);
                }
            }
        }
    }



    public void rememberNodeState(int id,boolean value,String stateType,String userLogin){
        Map params=Maps.newHashMap();
        params.put("nodeId",id);
        params.put("userLogin",userLogin);
        EdProjectTree edProjectTree=edProjectTreeMapper.selectByPrimaryKey(id);
        if(stateType.equalsIgnoreCase("selected")){
            //???????????????????????????
            Map allParams=Maps.newHashMap();
            allParams.put("userLogin",userLogin);
            allParams.put("projectId",edProjectTree.getProjectId());
            allParams.put("selected",true);
            for(EdProjectTreeState edProjectTreeState:edProjectTreeStateMapper.selectAll(allParams)){
                edProjectTreeState.setSelected(false);
                edProjectTreeStateMapper.updateByPrimaryKey(edProjectTreeState);
            }
        }
        if(PageHelper.count(()->edProjectTreeStateMapper.selectAll(params))==0) {
            EdProjectTreeState model=new EdProjectTreeState();
            model.setUserLogin(userLogin);
            model.setProjectId(edProjectTree.getProjectId());
            model.setNodeId(id);
            model.setOpened(edProjectTree.getOpened());
            model.setSelected(edProjectTree.getSelected());
            if(stateType.equalsIgnoreCase("opened")){
                model.setOpened(value);
            }else{
                model.setSelected(value);
            }
            edProjectTreeStateMapper.insert(model);
        }else {
            EdProjectTreeState model = edProjectTreeStateMapper.selectAll(params).get(0);
            if(stateType.equalsIgnoreCase("opened")){
                model.setOpened(value);
            }else{
                model.setSelected(value);
            }
            edProjectTreeStateMapper.updateByPrimaryKey(model);
        }
    }


    /**
     * ??????????????????????????????????????????????????????
     * @param businessContractId
     */
    public void genProjectTree(int businessContractId) {
        taskExecutor.execute(() -> {
            BusinessContractDto businessContract = businessContractService.getModelById(businessContractId);
            genFiveProjectTree(businessContract);
        });
    }


    /**
     * ?????????????????????
     * @param businessContract
     */
    private void genFiveProjectTree(BusinessContract businessContract) {
        int businessContractId = businessContract.getId();
        List<String> stageNames = MyStringUtil.getStringList(businessContract.getStageNames());
        Map params = Maps.newHashMap();
        params.put("projectId", businessContractId);
        params.put("parentId", 0);
        params.put("foreignKey", businessContractId);
        List<EdProjectTree> roots = edProjectTreeMapper.selectAll(params);
        int seq=1;
        for (String stageName : stageNames) {
            int rootId;
            if (roots.stream().noneMatch(p -> p.getNodeName().equalsIgnoreCase(stageName))) {
                rootId = insert(businessContractId, 0, businessContractId, "",stageName, "five.detail.step", "", seq);

            } else {
                EdProjectTree root = roots.stream().filter(p -> p.getNodeName().equalsIgnoreCase(stageName)).findFirst().get();
                root.setNodeUrl("five.detail.step");
                root.setDeleted(false);
                root.setSeq(seq);
                root.setGmtModified(new Date());
                edProjectTreeMapper.updateByPrimaryKey(root);
                rootId = root.getId();
            }
            EdProjectTree rootNode = edProjectTreeMapper.selectByPrimaryKey(rootId);
            rootNode.setOpened(true);
            edProjectTreeMapper.updateByPrimaryKey(rootNode);

            seq++;
            genFiveProjectStepTree(rootId, businessContractId);
        }
        for (EdProjectTree tree : roots.stream().filter(p -> !p.getDeleted()).collect(Collectors.toList())) {
            if (!stageNames.contains(tree.getNodeName())) {
                invalidTreeNode(tree);
            }
        }
    }

    /**
     * ??????????????????????????????????????????(Five)
     * ?????????????????????????????????????????????
     * @param rootId
     * @param businessContractId
     */
    private void genFiveProjectStepTree(int rootId,int businessContractId) {
        BusinessContract businessContract = businessContractMapper.selectByPrimaryKey(businessContractId);
        EdProjectTree stageTreeNode = edProjectTreeMapper.selectByPrimaryKey(rootId);

        Map params = Maps.newHashMap();
        params.put("projectId", businessContractId);
        params.put("deleted", false);
        params.put("stageName", stageTreeNode.getNodeName());
        List<EdProjectStep> edProjectSteps = edProjectStepMapper.selectAll(params);
        for (EdProjectStep edProjectStep : edProjectSteps) {
            int stepId = edProjectStep.getId();
            int stepTreeId = insert(businessContractId, rootId, stepId, "", edProjectStep.getStepName(), "five.detail.stepShow", "", edProjectStep.getSeq()+2);
            if (businessContract.getEd()) {
                insert(businessContractId, stepTreeId, stepId, "", "???????????????", "five.detail.task", "icon-user", 1);
                insert(businessContractId, stepTreeId, stepId, "", "?????????????????????", "five.detail.arrange", "icon-user", 2);
                insert(businessContractId, stepTreeId, stepId, "fiveEdInputReview", "?????????????????????", "five.detail.form", "icon-doc", 3);
                if (edProjectStep.getStageName().contains("??????????????????")) {
                    insert(businessContractId, stepTreeId, stepId, "fiveEdDesignRule", "???????????????????????????????????????", "five.detail.form", "icon-clock", 4);
                } else if (edProjectStep.getStageName().contains("?????????????????????")) {
                    insert(businessContractId, stepTreeId, stepId, "fiveEdDesignRule", "?????????????????????(??????????????????)", "five.detail.form", "icon-clock", 4);
                } else {
                    insert(businessContractId, stepTreeId, stepId, "fiveEdDesignRule", "?????????????????????(????????????)", "five.detail.form", "icon-clock", 4);
                }
                //int insertId = insert(businessContractId, stepTreeId, stepId, "??????????????????", "", "", 4);
                insert(businessContractId, stepTreeId, stepId, "fiveEdReviewMajor", "??????????????????", "five.detail.form", "icon-doc", 5);
                insert(businessContractId, stepTreeId, stepId, "fiveEdReviewPlan", "??????????????????", "five.detail.form", "icon-doc", 6);
                insert(businessContractId, stepTreeId, stepId, "fiveEdReviewCompany", "???????????????", "five.detail.form", "icon-doc", 7);

                insert(businessContractId, stepTreeId, stepId, "fiveEdRefer", "??????????????????", "five.detail.form", "icon-loop", 8);
                insert(businessContractId, stepTreeId, stepId, "fiveEdValidate", "??????????????????", "five.detail.form", "icon-note", 9);
                insert(businessContractId, stepTreeId, stepId, "fiveEdProduct", "??????????????????", "five.detail.form", "icon-note", 10);
                insert(businessContractId, stepTreeId, stepId, "fiveEdStamp", "????????????????????????", "five.detail.form", "icon-shield", 23);

                //insert(businessContractId, stepTreeId, stepId, "????????????????????????", "five.detail.designSign", "icon-clock", 13);
                insert(businessContractId, stepTreeId, stepId, "fiveEdOutReview", "??????????????????", "five.detail.form", "icon-question", 11);
                //insert(businessContractId, stepTreeId, stepId, "???????????????????????????", "five.detail.countersign", "icon-compass", 20);
                int desId = insert(businessContractId, stepTreeId, stepId, "", "??????????????????", "", "", 55);

                //insert(businessContractId, stepTreeId, stepId, "???????????????", "five.detail.plotApply", "icon-star", 22);

                int teachId = insert(businessContractId, stepTreeId, stepId, "", "??????????????????", "", "", 35);
                insert(businessContractId, teachId, stepId, "fiveEdReturnVisit", "????????????", "five.detail.form", "icon-compass", 1);
                insert(businessContractId, teachId, stepId, "fiveEdQualityCheck", "?????????????????? ", "five.detail.form", "icon-eye", 2);
                insert(businessContractId, teachId, stepId, "fiveEdQualityAnalysis", "??????????????????", "five.detail.form", "icon-calculator", 3);
                insert(businessContractId, teachId, stepId, "fiveEdQualityIssue", "??????????????????", "five.detail.form", "icon-bell", 4);


                int serviceId = insert(businessContractId, stepTreeId, stepId, "", "??????????????????", "", "", 36);
                insert(businessContractId, serviceId, stepId, "fiveEdChange", "?????????????????????", "five.detail.form", "icon-compass", 1);
                //insert(businessContractId, serviceId, stepId, "", "??????????????????????????????????????????", "five.detail.serviceDiscuss", "icon-compass", 2);
                insert(businessContractId, serviceId, stepId, "fiveEdServiceHandle", "???????????????????????????", "five.detail.form", "icon-compass", 3);
                insert(businessContractId, serviceId, stepId, "fiveEdTechHand", "??????????????????", "five.detail.form", "icon-star", 10);
                insert(businessContractId, serviceId, stepId, "fiveEdInvalidate", "?????????????????????", "five.detail.form", "icon-star", 11);
                insert(businessContractId, serviceId, stepId, "fiveEdTechCommunicate", "???????????????", "five.detail.form", "icon-star", 12);
                insert(businessContractId, serviceId, stepId, "fiveEdOutService", "????????????", "five.detail.form", "icon-star", 13);
                insert(businessContractId, serviceId, stepId, "fiveEdSatisfaction", "?????????????????????", "five.detail.form", "icon-star", 14);
                insert(businessContractId, serviceId, stepId, "fiveEdChargePaper", "????????????????????????", "five.detail.form", "icon-star", 15);
                insert(businessContractId, serviceId, stepId, "fiveEdMeetingDelegate", "???????????????", "five.detail.form", "icon-star", 16);
                insert(businessContractId, serviceId, stepId, "fiveEdSubAcceptMeeting", "???????????????????????????", "five.detail.form", "icon-star", 17);
                insert(businessContractId, serviceId, stepId, "fiveEdCompletedMeeting", "???????????????????????????", "five.detail.form", "icon-star", 18);
                insert(businessContractId, serviceId, stepId, "fiveEdSummaryMeeting", "???????????????", "five.detail.form", "icon-star", 19);
                insert(businessContractId, serviceId, stepId, "fiveEdCompletedRecord", "?????????????????????", "five.detail.form", "icon-star", 20);

                int drawingCheckId = insert(businessContractId, stepTreeId, stepId, "", "????????????", "", "", 37);
                insert(businessContractId, drawingCheckId, stepId, "fiveEdDesignDrawingCheck", "????????????????????????(??????)", "five.detail.designDrawingCheck", "icon-star", 1);
                insert(businessContractId, drawingCheckId, stepId, "fiveEdMajorDrawingCheck", "????????????????????????", "five.detail.majorDrawingCheck", "icon-star", 2);
                insert(businessContractId, drawingCheckId, stepId, "fiveEdDesignDrawing", "??????????????????????????????(??????)", "five.detail.designDrawing", "icon-doc", 3);

            }
        }

        //???????????????????????????
        Map treeParams = Maps.newHashMap();
        treeParams.put("projectId", businessContractId);
        treeParams.put("deleted", false);
        treeParams.put("parentId", rootId);
        for (EdProjectTree edProjectTree : edProjectTreeMapper.selectAll(treeParams)) {
            if (edProjectSteps.stream().noneMatch(p -> p.getId().equals(edProjectTree.getForeignKey()))) {
                if (!edProjectTree.getNodeName().equals("??????????????????")) {
                    invalidTreeNode(edProjectTree);
                }
            }
        }

        insert(businessContractId, rootId, 0, "fiveEdStep", "??????????????????", "five.detail.step", "icon-grid", 1);
        insert(businessContractId, rootId, 0, "fiveEdDoc", "??????????????????", "five.detail.doc", "icon-notebook", 2);

    }

    private int insert(int projectId,int parentId,int foreignKey,String referType,String name,String url,String icon,int seq) {
        Map params = Maps.newHashMap();
        params.put("projectId", projectId);
        params.put("parentId", parentId);
        params.put("foreignKey", foreignKey);

       if(url.contains("five.detail.stepShow")) {
           params.put("url",url);
       }else {
           params.put("nodeName", name);
       }

        if (PageHelper.count(() -> edProjectTreeMapper.selectAll(params)) == 0) {
            Date now = new Date();
            EdProjectTree edProjectTree = new EdProjectTree();
            edProjectTree.setProjectId(projectId);
            edProjectTree.setForeignKey(foreignKey);
            edProjectTree.setParentId(parentId);
            edProjectTree.setNodeName(name);
            edProjectTree.setNodeUrl(url);
            //referType
            if(StringUtils.isEmpty(referType)) {
                edProjectTree.setReferType(getReferType(url));
            }else{
                edProjectTree.setReferType(referType);
            }
            edProjectTree.setSeq(seq);
            edProjectTree.setIcon(icon);
            edProjectTree.setOpened(false);
            if(url.equalsIgnoreCase("ed.detail.stepShow")){
                edProjectTree.setOpened(true);
            }
            edProjectTree.setSelected(false);
            edProjectTree.setDisabled(false);
            edProjectTree.setDeleted(false);
            edProjectTree.setGmtCreate(now);
            edProjectTree.setGmtModified(now);
            ModelUtil.setNotNullFields(edProjectTree);
            edProjectTreeMapper.insert(edProjectTree);
            return edProjectTree.getId();
        } else {
            List<EdProjectTree> trees = edProjectTreeMapper.selectAll(params);
            if (trees.stream().anyMatch(p -> !p.getDeleted())) {
                EdProjectTree tree = trees.stream().filter(p -> !p.getDeleted()).findFirst().get();
                tree.setNodeName(name);
                tree.setNodeUrl(url);
                tree.setSeq(seq);
                tree.setIcon(icon);
                if(StringUtils.isEmpty(referType)) {
                    tree.setReferType(getReferType(url));
                }else{
                    tree.setReferType(referType);
                }
                tree.setGmtModified(new Date());
                edProjectTreeMapper.updateByPrimaryKey(tree);
                return tree.getId();
            } else {

                EdProjectTree tree = trees.stream().findFirst().get();
                tree.setNodeName(name);
                tree.setNodeUrl(url);
                tree.setSeq(seq);
                tree.setIcon(icon);
                tree.setDeleted(false);
                tree.setGmtModified(new Date());
                if(StringUtils.isEmpty(referType)) {
                    tree.setReferType(getReferType(url));
                }else{
                    tree.setReferType(referType);
                }
                edProjectTreeMapper.updateByPrimaryKey(tree);
                return tree.getId();
            }
        }
    }

    private void invalidTreeNode(EdProjectTree edProjectTree){
        edProjectTree.setDeleted(true);
        edProjectTree.setGmtModified(new Date());
        edProjectTreeMapper.updateByPrimaryKey(edProjectTree);
        Map treeParams = Maps.newHashMap();
        treeParams.put("projectId", edProjectTree.getProjectId());
        treeParams.put("parentId", edProjectTree.getId());
        treeParams.put("deleted",false);
        for(EdProjectTree child :edProjectTreeMapper.selectAll(treeParams)){
            invalidTreeNode(child);
        }
    }

    private EdProjectTreeDto getDto(EdProjectTree edProjectTree){
        EdProjectTreeDto edProjectTreeDto= EdProjectTreeDto.adapt(edProjectTree);
        if(edProjectTreeDto.getParentId()!=0){
            EdProjectTree parent=edProjectTreeMapper.selectByPrimaryKey(edProjectTree.getParentId());
            edProjectTreeDto.setParentForeignKey(parent.getForeignKey());
            edProjectTreeDto.setParentNodeName(parent.getNodeName());
        }
        return edProjectTreeDto;
    }


    /**
     * ???????????????????????????url
     * @param stepId
     * @return
     */
    public  String getStepDefaultUrl(int stepId){
        try {
            EdProjectStep edProjectStep = edProjectStepMapper.selectByPrimaryKey(stepId);
            BusinessContract businessContract = businessContractMapper.selectByPrimaryKey(edProjectStep.getProjectId());
            if(businessContract.getProjectType().contains("??????")){
                Map params=Maps.newHashMap();
                params.put("deleted",false);
                params.put("nodeUrl","explore.detail.stepShow");
                params.put("foreignKey",stepId);
                if(PageHelper.count(()->edProjectTreeMapper.selectAll(params))>0){
                    EdProjectTree edProjectTree=edProjectTreeMapper.selectAll(params).get(0);
                    return "/index#/explore/detail/stepShow?id="+businessContract.getId()+"&nodeId="+edProjectTree.getId();
                }
            }else if(businessContract.getProjectType().contains("??????")||businessContract.getProjectType().contains("??????")){
                Map params=Maps.newHashMap();
                params.put("deleted",false);
                params.put("nodeUrl","ed.detail.stepShow");
                params.put("foreignKey",stepId);
                if(PageHelper.count(()->edProjectTreeMapper.selectAll(params))>0){
                    EdProjectTree edProjectTree=edProjectTreeMapper.selectAll(params).get(0);
                    return "/index#/ed/detail/stepShow?id="+businessContract.getId()+"&nodeId="+edProjectTree.getId();
                }
            }else{
                Map params=Maps.newHashMap();
                params.put("deleted",false);
                params.put("nodeUrl","five.detail.stepShow");
                params.put("foreignKey",stepId);
                if(PageHelper.count(()->edProjectTreeMapper.selectAll(params))>0){
                    EdProjectTree edProjectTree=edProjectTreeMapper.selectAll(params).get(0);
                    return "/index#/five/detail/stepShow?id="+businessContract.getId()+"&nodeId="+edProjectTree.getId();
                }
            }
            return "/index";
        }catch (Exception ex){

            return "/index";
        }
    }


    public String getReferType(String nodeUrl) {
        String referType = "";
        switch (nodeUrl) {
            case "five.detail.task"://???????????????
                referType = "fiveEdTask";
                break;
            case "five.detail.arrange"://?????????????????????
                referType = "fiveEdArrange";
                break;
            case "five.detail.designRule"://?????????????????????
                referType = "fiveEdDesignRule";
                break;
            case "five.detail.reviewMajor"://??????????????????
                referType = "fiveEdReviewMajor";
                break;
            case "five.detail.reviewPlan"://??????????????????
                referType = "fiveEdReviewPlan";
                break;
            case "five.detail.reviewSpecial"://????????????
                referType = "fiveEdReviewSpecial";
                break;
            case "five.detail.refer"://?????????????????????
                referType = "fiveEdRefer";
                break;
            case "five.detail.validate"://?????????????????????
                referType = "fiveEdValidate";
                break;
            case "five.detail.outReview"://??????????????????
                referType = "fiveEdOutReview";
                break;
            case "five.detail.serviceChange"://?????????????????????
                referType = "fiveEdServiceChange";
                break;
            case "five.detail.serviceDiscuss"://??????????????????????????????????????????
                referType = "fiveEdServiceDiscuss";
                break;
            case "five.detail.serviceHandle"://???????????????????????????
                referType = "fiveEdServiceHandle";
                break;
            case "five.detail.plotApply"://???????????????
                referType = "fiveEdPlotApply";
                break;
            case "five.detail.stamp"://????????????????????????
                referType = "fiveEdStamp";
                break;
            case "five.detail.returnVisit"://????????????????????????
                referType = "fiveEdReturnVisit";
                break;
            case "five.detail.qualityAnalysis"://????????????????????????
                referType = "fiveEdQualityAnalysis";
                break;
            case "five.detail.qualityCheck"://???????????????????????????
                referType = "fiveEdQualityCheck";
                break;
        }
        return referType;
    }



}
