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
     * 得到树形列表
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
            //取消其他的选中状态
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
     * 为某个合同生成或者取消设计管理根节点
     * @param businessContractId
     */
    public void genProjectTree(int businessContractId) {
        taskExecutor.execute(() -> {
            BusinessContractDto businessContract = businessContractService.getModelById(businessContractId);
            genFiveProjectTree(businessContract);
        });
    }


    /**
     * 五洲树节点生成
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
     * 根据设计项目的根节点阶段名称(Five)
     * 依据分期的数据生成下面树的节点
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
                insert(businessContractId, stepTreeId, stepId, "", "设计任务书", "five.detail.task", "icon-user", 1);
                insert(businessContractId, stepTreeId, stepId, "", "人员与计划安排", "five.detail.arrange", "icon-user", 2);
                insert(businessContractId, stepTreeId, stepId, "fiveEdInputReview", "设计与输入评审", "five.detail.form", "icon-doc", 3);
                if (edProjectStep.getStageName().contains("前期咨询阶段")) {
                    insert(businessContractId, stepTreeId, stepId, "fiveEdDesignRule", "设计指导性文件（指导意见）", "five.detail.form", "icon-clock", 4);
                } else if (edProjectStep.getStageName().contains("施工图设计阶段")) {
                    insert(businessContractId, stepTreeId, stepId, "fiveEdDesignRule", "设计指导性文件(统一技术措施)", "five.detail.form", "icon-clock", 4);
                } else {
                    insert(businessContractId, stepTreeId, stepId, "fiveEdDesignRule", "设计指导性文件(设计原则)", "five.detail.form", "icon-clock", 4);
                }
                //int insertId = insert(businessContractId, stepTreeId, stepId, "设计评审管理", "", "", 4);
                insert(businessContractId, stepTreeId, stepId, "fiveEdReviewMajor", "专业方案评审", "five.detail.form", "icon-doc", 5);
                insert(businessContractId, stepTreeId, stepId, "fiveEdReviewPlan", "总体方案评审", "five.detail.form", "icon-doc", 6);
                insert(businessContractId, stepTreeId, stepId, "fiveEdReviewCompany", "公司级评审", "five.detail.form", "icon-doc", 7);

                insert(businessContractId, stepTreeId, stepId, "fiveEdRefer", "设计协作提资", "five.detail.form", "icon-loop", 8);
                insert(businessContractId, stepTreeId, stepId, "fiveEdValidate", "设计文件审校", "five.detail.form", "icon-note", 9);
                insert(businessContractId, stepTreeId, stepId, "fiveEdProduct", "设计文件成品", "five.detail.form", "icon-note", 10);
                insert(businessContractId, stepTreeId, stepId, "fiveEdStamp", "设计成果用印申请", "five.detail.form", "icon-shield", 23);

                //insert(businessContractId, stepTreeId, stepId, "设计图签使用管理", "five.detail.designSign", "icon-clock", 13);
                insert(businessContractId, stepTreeId, stepId, "fiveEdOutReview", "外部设计评审", "five.detail.form", "icon-question", 11);
                //insert(businessContractId, stepTreeId, stepId, "各专业设计图纸会签", "five.detail.countersign", "icon-compass", 20);
                int desId = insert(businessContractId, stepTreeId, stepId, "", "设计服务管理", "", "", 55);

                //insert(businessContractId, stepTreeId, stepId, "出图申请单", "five.detail.plotApply", "icon-star", 22);

                int teachId = insert(businessContractId, stepTreeId, stepId, "", "技术质量管理", "", "", 35);
                insert(businessContractId, teachId, stepId, "fiveEdReturnVisit", "设计回访", "five.detail.form", "icon-compass", 1);
                insert(businessContractId, teachId, stepId, "fiveEdQualityCheck", "设计质量抽查 ", "five.detail.form", "icon-eye", 2);
                insert(businessContractId, teachId, stepId, "fiveEdQualityAnalysis", "设计质量剖析", "five.detail.form", "icon-calculator", 3);
                insert(businessContractId, teachId, stepId, "fiveEdQualityIssue", "重大质量问题", "five.detail.form", "icon-bell", 4);


                int serviceId = insert(businessContractId, stepTreeId, stepId, "", "设计服务管理", "", "", 36);
                insert(businessContractId, serviceId, stepId, "fiveEdChange", "设计变更通知单", "five.detail.form", "icon-compass", 1);
                //insert(businessContractId, serviceId, stepId, "", "建设、施工单位变更设计洽商单", "five.detail.serviceDiscuss", "icon-compass", 2);
                insert(businessContractId, serviceId, stepId, "fiveEdServiceHandle", "技术服务问题处理单", "five.detail.form", "icon-compass", 3);
                insert(businessContractId, serviceId, stepId, "fiveEdTechHand", "设计交底纪要", "five.detail.form", "icon-star", 10);
                insert(businessContractId, serviceId, stepId, "fiveEdInvalidate", "归档底图作废单", "five.detail.form", "icon-star", 11);
                insert(businessContractId, serviceId, stepId, "fiveEdTechCommunicate", "技术洽商单", "five.detail.form", "icon-star", 12);
                insert(businessContractId, serviceId, stepId, "fiveEdOutService", "现场服务", "five.detail.form", "icon-star", 13);
                insert(businessContractId, serviceId, stepId, "fiveEdSatisfaction", "顾客满意度调查", "five.detail.form", "icon-star", 14);
                insert(businessContractId, serviceId, stepId, "fiveEdChargePaper", "设计负责人责任书", "five.detail.form", "icon-star", 15);
                insert(businessContractId, serviceId, stepId, "fiveEdMeetingDelegate", "参会委托书", "five.detail.form", "icon-star", 16);
                insert(businessContractId, serviceId, stepId, "fiveEdSubAcceptMeeting", "分项验收、会议纪要", "five.detail.form", "icon-star", 17);
                insert(businessContractId, serviceId, stepId, "fiveEdCompletedMeeting", "竣工验收、会议纪要", "five.detail.form", "icon-star", 18);
                insert(businessContractId, serviceId, stepId, "fiveEdSummaryMeeting", "项目总结会", "five.detail.form", "icon-star", 19);
                insert(businessContractId, serviceId, stepId, "fiveEdCompletedRecord", "竣工验收备案证", "five.detail.form", "icon-star", 20);

                int drawingCheckId = insert(businessContractId, stepTreeId, stepId, "", "图纸交验", "", "", 37);
                insert(businessContractId, drawingCheckId, stepId, "fiveEdDesignDrawingCheck", "设计图纸资料交验(总师)", "five.detail.designDrawingCheck", "icon-star", 1);
                insert(businessContractId, drawingCheckId, stepId, "fiveEdMajorDrawingCheck", "专业图纸验收清单", "five.detail.majorDrawingCheck", "icon-star", 2);
                insert(businessContractId, drawingCheckId, stepId, "fiveEdDesignDrawing", "设计文件图纸验收清单(总师)", "five.detail.designDrawing", "icon-doc", 3);

            }
        }

        //检查是否有被删除的
        Map treeParams = Maps.newHashMap();
        treeParams.put("projectId", businessContractId);
        treeParams.put("deleted", false);
        treeParams.put("parentId", rootId);
        for (EdProjectTree edProjectTree : edProjectTreeMapper.selectAll(treeParams)) {
            if (edProjectSteps.stream().noneMatch(p -> p.getId().equals(edProjectTree.getForeignKey()))) {
                if (!edProjectTree.getNodeName().equals("项目期次管理")) {
                    invalidTreeNode(edProjectTree);
                }
            }
        }

        insert(businessContractId, rootId, 0, "fiveEdStep", "项目期次管理", "five.detail.step", "icon-grid", 1);
        insert(businessContractId, rootId, 0, "fiveEdDoc", "顾客财产登记", "five.detail.doc", "icon-notebook", 2);

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
     * 得到某个分期跳转的url
     * @param stepId
     * @return
     */
    public  String getStepDefaultUrl(int stepId){
        try {
            EdProjectStep edProjectStep = edProjectStepMapper.selectByPrimaryKey(stepId);
            BusinessContract businessContract = businessContractMapper.selectByPrimaryKey(edProjectStep.getProjectId());
            if(businessContract.getProjectType().contains("勘察")){
                Map params=Maps.newHashMap();
                params.put("deleted",false);
                params.put("nodeUrl","explore.detail.stepShow");
                params.put("foreignKey",stepId);
                if(PageHelper.count(()->edProjectTreeMapper.selectAll(params))>0){
                    EdProjectTree edProjectTree=edProjectTreeMapper.selectAll(params).get(0);
                    return "/index#/explore/detail/stepShow?id="+businessContract.getId()+"&nodeId="+edProjectTree.getId();
                }
            }else if(businessContract.getProjectType().contains("房建")||businessContract.getProjectType().contains("市政")){
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
            case "five.detail.task"://设计任务书
                referType = "fiveEdTask";
                break;
            case "five.detail.arrange"://人员与计划安排
                referType = "fiveEdArrange";
                break;
            case "five.detail.designRule"://设计指导性文件
                referType = "fiveEdDesignRule";
                break;
            case "five.detail.reviewMajor"://专业方案讨论
                referType = "fiveEdReviewMajor";
                break;
            case "five.detail.reviewPlan"://总体方案评审
                referType = "fiveEdReviewPlan";
                break;
            case "five.detail.reviewSpecial"://专项评审
                referType = "fiveEdReviewSpecial";
                break;
            case "five.detail.refer"://设计协作提资单
                referType = "fiveEdRefer";
                break;
            case "five.detail.validate"://设计文件校审单
                referType = "fiveEdValidate";
                break;
            case "five.detail.outReview"://外部设计评审
                referType = "fiveEdOutReview";
                break;
            case "five.detail.serviceChange"://设计变更通知单
                referType = "fiveEdServiceChange";
                break;
            case "five.detail.serviceDiscuss"://建工、施工单位变更设计洽商单
                referType = "fiveEdServiceDiscuss";
                break;
            case "five.detail.serviceHandle"://技术服务问题处理单
                referType = "fiveEdServiceHandle";
                break;
            case "five.detail.plotApply"://出图申请单
                referType = "fiveEdPlotApply";
                break;
            case "five.detail.stamp"://设计成果用印申请
                referType = "fiveEdStamp";
                break;
            case "five.detail.returnVisit"://工程设计回访记录
                referType = "fiveEdReturnVisit";
                break;
            case "five.detail.qualityAnalysis"://设计质量问题剖析
                referType = "fiveEdQualityAnalysis";
                break;
            case "five.detail.qualityCheck"://质量抽查审校记录单
                referType = "fiveEdQualityCheck";
                break;
        }
        return referType;
    }



}
