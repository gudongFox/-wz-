package com.cmcu.mcc.service;

import com.cmcu.common.act.dto.UserTaskDto;
import com.cmcu.common.entity.CommonFormData;
import com.cmcu.common.service.CommonFormDataService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.dto.ProcessInstanceDto;
import com.cmcu.mcc.ed.dao.*;
import com.cmcu.mcc.ed.entity.*;

import com.cmcu.mcc.ed.service.EdProjectTreeService;
import com.cmcu.mcc.five.dao.FiveHrQualifyChiefMapper;
import com.cmcu.mcc.five.entity.FiveHrQualifyChief;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.common.util.JsonMapper;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;
import org.activiti.bpmn.model.*;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.el.ExpressionFactory;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ActBusinessService {

    Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    TaskService taskService;

    @Autowired
    HistoryService historyService;

    @Autowired
    RepositoryService repositoryService;

    @Resource
    HrEmployeeMapper hrEmployeeMapper;


    @Resource
    EdProjectStepMapper edProjectStepMapper;
    @Resource
    EdProjectTreeMapper edProjectTreeMapper;

    @Resource
    ActService actService;


    @Resource
    FiveHrQualifyChiefMapper fiveHrQualifyChiefMapper;
    @Autowired
    ActDiagramService actDiagramService;
     @Autowired
     CommonFormDataService commonFormDataService;


    public List<UserTaskDto> listNextUserTask(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Map variables = taskService.getVariables(taskId);
        String taskDefinitionKey = task.getTaskDefinitionKey();
        List<UserTaskDto> dtoList = Lists.newArrayList();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(task.getProcessInstanceId())
                .orderByHistoricActivityInstanceStartTime().desc()
                .list().stream()
                .filter(p -> p.getActivityType().equals("userTask")).collect(Collectors.toList());
        handleNextFlowElement(task.getProcessInstanceId(), taskDefinitionKey, variables, bpmnModel, historicActivityInstanceList, dtoList);

        //只要有其他节点就不显示结束
        if (dtoList.stream().anyMatch(p -> !p.getTaskName().equalsIgnoreCase("结束"))) {
            dtoList = dtoList.stream().filter(p -> !p.getTaskName().equalsIgnoreCase("结束")).collect(Collectors.toList());
        }
        return dtoList;
    }

    /**
     * 得到打回数据
     *
     * @param taskId
     * @return
     */
    public List<UserTaskDto> listBackUserTask(String taskId) {

        List<UserTaskDto> list = Lists.newArrayList();
        //获得当前任务 判断任务状态
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String definitionKey = task.getTaskDefinitionKey();

        //根据任务id得到流程实例id
        String processInstanceId = task.getProcessInstanceId();
        String processDefinitionId = historyService.createHistoricActivityInstanceQuery().processInstanceId(task.getProcessInstanceId()).list().get(0).getProcessDefinitionId();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);


        Map variables = taskService.getVariables(taskId);
        //得到历史任务信息
        List<HistoricActivityInstance> historicActivityInstances = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricActivityInstanceStartTime().desc()
                .list().stream()
                .filter(p -> p.getActivityType().equals("userTask"))
                .filter(p -> !p.getTaskId().equalsIgnoreCase(definitionKey))
                .collect(Collectors.toList());

        //流程定义查找前面的userTaskKey
        UserTask currentUserTask = (UserTask) bpmnModel.getFlowElement(definitionKey);


        List<SequenceFlow> incomingFlows = currentUserTask.getIncomingFlows();
        FlowElement preElement = bpmnModel.getMainProcess().getFlowElement(incomingFlows.get(0).getSourceRef());
        //如果是并行网关、或者包容网关只允许打回到上一级
        if ((preElement instanceof ParallelGateway || preElement instanceof InclusiveGateway) && ((Gateway) preElement).getOutgoingFlows().size() > 1) {
            List<SequenceFlow> flows = ((Gateway) preElement).getIncomingFlows();
            FlowElement flowElement = bpmnModel.getMainProcess().getFlowElement(flows.get(0).getSourceRef());
            if (flowElement instanceof UserTask) {
                UserTask userTask = (UserTask) flowElement;
                UserTaskDto model = new UserTaskDto();
                model.setPreKey(definitionKey);
                model.setTaskKey(userTask.getId());
                model.setTaskName(userTask.getName());
                model.setMultiple(userTask.getLoopCharacteristics() != null);
                if (list.stream().noneMatch(p -> p.getTaskKey().equalsIgnoreCase(userTask.getId()))) {
                    model.setUsers(getUserList(processInstanceId, userTask, historicActivityInstances, variables));
                    list.add(model);
                }
            }
        } else {
            recursionFindPreTask(processInstanceId, historicActivityInstances, variables, (FlowNode) preElement, bpmnModel, list);
        }
        list.sort(Comparator.comparing(UserTaskDto::getPreKey));
        return list;
    }

    private void recursionFindPreTask(String processInstanceId, List<HistoricActivityInstance> historicActivityInstances, Map<String, Object> variables, FlowNode flowNode, BpmnModel bpmnModel, List<UserTaskDto> list) {

        List<SequenceFlow> sequenceFlowList = flowNode.getIncomingFlows();
        if (flowNode instanceof UserTask) {
            UserTask userTask = (UserTask) flowNode;
            UserTaskDto model = new UserTaskDto();
            model.setPreKey(sequenceFlowList.get(0).getSourceRef());
            model.setTaskKey(userTask.getId());
            model.setTaskName(userTask.getName());
            model.setMultiple(userTask.getLoopCharacteristics() != null);
            if (list.stream().anyMatch(p -> p.getTaskKey().equalsIgnoreCase(userTask.getId()))) {
                return;
            }
            model.setUsers(getUserList(processInstanceId, userTask, historicActivityInstances, variables));
            list.add(model);
        }
        if (sequenceFlowList.size() > 0) {
            for (SequenceFlow sequenceFlow : sequenceFlowList) {
                String sourceRef = sequenceFlow.getSourceRef();
                if (StringUtils.isNotEmpty(sourceRef)) {
                    FlowNode preFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(sourceRef);
                    if (preFlowNode instanceof StartEvent) {
                        return;
                    }
                    recursionFindPreTask(processInstanceId, historicActivityInstances, variables, preFlowNode, bpmnModel, list);
                }
            }
        }
    }


    private void recursionFindPreTask(BpmnModel bpmnModel, String taskDefinitionKey, List<UserTask> preTaskKeyList) {
        if (!preTaskKeyList.contains(taskDefinitionKey)) {
            FlowNode currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(taskDefinitionKey);
            List<SequenceFlow> sequenceFlowList = currentFlowNode.getIncomingFlows();
            if (sequenceFlowList.size() > 0) {
                for (SequenceFlow sequenceFlow : sequenceFlowList) {
                    String sourceRef = sequenceFlow.getSourceRef();
                    if (StringUtils.isNotEmpty(sourceRef)) {
                        FlowElement preElement = bpmnModel.getMainProcess().getFlowElement(sourceRef);
                        if (preElement instanceof UserTask) {
                            UserTask preUserTask = (UserTask) preElement;
                            preTaskKeyList.add(preUserTask);
                            recursionFindPreTask(bpmnModel, preUserTask.getId(), preTaskKeyList);
                        } else {
                            recursionFindPreTask(bpmnModel, preElement.getId(), preTaskKeyList);
                        }
                    }
                }
            }
        }

    }


    private void handleNextFlowElement(String processInstanceId, String definitionKey, Map keyVariables, BpmnModel bpmnModel, List<HistoricActivityInstance> historicActivityInstances, List<UserTaskDto> list) {
        FlowNode currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(definitionKey);
        List<SequenceFlow> sequenceFlowList = currentFlowNode.getOutgoingFlows();
        FlowElement nextFlowElement = bpmnModel.getMainProcess().getFlowElement(sequenceFlowList.get(0).getTargetRef());

        int seq = 1;
        if (list.stream().anyMatch(p -> p.getTaskKey().equalsIgnoreCase(definitionKey))) {
            seq = list.stream().filter(p -> p.getTaskKey().equalsIgnoreCase(definitionKey)).map(p -> p.getSeq()).findFirst().get() + 1;
        }
        if (nextFlowElement instanceof EndEvent) {
            EndEvent endEvent = (EndEvent) nextFlowElement;
            UserTaskDto model = new UserTaskDto();
            model.setTaskKey(endEvent.getId());
            model.setTaskName("结束");
            model.setMultiple(false);
            model.setSeq(seq);
            list.add(model);
        } else if (nextFlowElement instanceof UserTask) {
            UserTask nextUserTask = (UserTask) nextFlowElement;
            UserTaskDto model = new UserTaskDto();
            model.setTaskKey(nextUserTask.getId());
            model.setTaskName(nextUserTask.getName());
            model.setMultiple(nextUserTask.getLoopCharacteristics() != null);
            model.setSeq(seq);
            if (list.stream().noneMatch(p -> p.getTaskKey().equalsIgnoreCase(nextUserTask.getId()))) {
                model.setUsers(getUserList(processInstanceId, nextUserTask, historicActivityInstances, keyVariables));
                list.add(model);
                handleNextFlowElement(processInstanceId, nextUserTask.getId(), keyVariables, bpmnModel, historicActivityInstances, list);
            }
        } else if (nextFlowElement instanceof ParallelGateway) {
            //并行网关
            List<SequenceFlow> parallelFlows = ((ParallelGateway) nextFlowElement).getOutgoingFlows();
            for (SequenceFlow parallelFlow : parallelFlows) {
                FlowElement parallelTask = bpmnModel.getMainProcess().getFlowElement(parallelFlow.getTargetRef());
                if (parallelTask instanceof UserTask) {
                    UserTask nextParallelUserTask = (UserTask) parallelTask;
                    UserTaskDto model = new UserTaskDto();
                    model.setTaskKey(nextParallelUserTask.getId());
                    model.setTaskName(nextParallelUserTask.getName());
                    model.setMultiple(nextParallelUserTask.getLoopCharacteristics() != null);
                    model.setSeq(seq);
                    if (list.stream().noneMatch(p -> p.getTaskKey().equalsIgnoreCase(nextParallelUserTask.getId()))) {
                        model.setUsers(getUserList(processInstanceId, nextParallelUserTask, historicActivityInstances, keyVariables));
                        list.add(model);
                        handleNextFlowElement(processInstanceId, nextParallelUserTask.getId(), keyVariables, bpmnModel, historicActivityInstances, list);
                    }
                } else if (parallelTask instanceof EndEvent) {
                    EndEvent endEvent = (EndEvent) parallelTask;
                    UserTaskDto model = new UserTaskDto();
                    model.setTaskKey(endEvent.getId());
                    model.setTaskName("结束");
                    model.setMultiple(false);
                    model.setSeq(seq);
                    list.add(model);
                }
            }

        } else if (nextFlowElement instanceof ExclusiveGateway) {
            //排他网关
            ExpressionFactory factory = new ExpressionFactoryImpl();
            SimpleContext context = new SimpleContext();
            for (Object key : keyVariables.keySet()) {
                context.setVariable(key.toString(), factory.createValueExpression(keyVariables.get(key), keyVariables.get(key).getClass()));
            }
            SequenceFlow defaultSequenceFlow = null;
            for (SequenceFlow outgoingFlow : ((ExclusiveGateway) nextFlowElement).getOutgoingFlows()) {
                if (StringUtils.isNotEmpty(outgoingFlow.getConditionExpression())) {
                    if ((boolean) factory.createValueExpression(context, outgoingFlow.getConditionExpression(), boolean.class).getValue(context)) {
                        defaultSequenceFlow = outgoingFlow;
                        break;
                    }
                } else {
                    defaultSequenceFlow = outgoingFlow;
                }

            }
            if (defaultSequenceFlow != null) {
                System.out.println(nextFlowElement.getName() + "条件表达式缺少必要的参数!");
                nextFlowElement = bpmnModel.getMainProcess().getFlowElement(defaultSequenceFlow.getTargetRef());
                if (nextFlowElement instanceof UserTask) {
                    UserTask nextExclusiveUserTask = (UserTask) nextFlowElement;
                    UserTaskDto model = new UserTaskDto();
                    model.setTaskKey(nextExclusiveUserTask.getId());
                    model.setTaskName(nextExclusiveUserTask.getName());
                    model.setMultiple(nextExclusiveUserTask.getLoopCharacteristics() != null);
                    model.setSeq(seq);
                    if (list.stream().noneMatch(p -> p.getTaskKey().equalsIgnoreCase(nextExclusiveUserTask.getId()))) {
                        model.setUsers(getUserList(processInstanceId, nextExclusiveUserTask, historicActivityInstances, keyVariables));
                        list.add(model);
                        handleNextFlowElement(processInstanceId, nextExclusiveUserTask.getId(), keyVariables, bpmnModel, historicActivityInstances, list);
                    }
                } else if (nextFlowElement instanceof EndEvent) {
                    EndEvent endEvent = (EndEvent) nextFlowElement;
                    UserTaskDto model = new UserTaskDto();
                    model.setTaskKey(endEvent.getId());
                    model.setTaskName("结束");
                    model.setMultiple(false);
                    model.setSeq(seq);
                    list.add(model);
                }
            }

        } else if (nextFlowElement instanceof InclusiveGateway) {
            //包容网关
            ExpressionFactory factory = new ExpressionFactoryImpl();
            SimpleContext context = new SimpleContext();
            for (Object key : keyVariables.keySet()) {
                context.setVariable(key.toString(), factory.createValueExpression(keyVariables.get(key), keyVariables.get(key).getClass()));
            }
            for (SequenceFlow outgoingFlow : ((InclusiveGateway) nextFlowElement).getOutgoingFlows()) {
                if (StringUtils.isNotEmpty(outgoingFlow.getConditionExpression())) {
                    //满足这个条件
                    if ((boolean) factory.createValueExpression(context, outgoingFlow.getConditionExpression(), boolean.class).getValue(context)) {
                        UserTask userTask = (UserTask) bpmnModel.getMainProcess().getFlowElement(outgoingFlow.getTargetRef());
                        UserTaskDto model = new UserTaskDto();
                        model.setTaskKey(userTask.getId());
                        model.setTaskName(userTask.getName());
                        model.setMultiple(userTask.getLoopCharacteristics() != null);
                        model.setSeq(seq);
                        if (list.stream().noneMatch(p -> p.getTaskKey().equalsIgnoreCase(userTask.getId()))) {
                            model.setUsers(getUserList(processInstanceId, userTask, historicActivityInstances, keyVariables));
                            list.add(model);
                            handleNextFlowElement(processInstanceId, userTask.getId(), keyVariables, bpmnModel, historicActivityInstances, list);
                        }
                    }
                } else {
                    UserTask userTask = (UserTask) bpmnModel.getMainProcess().getFlowElement(outgoingFlow.getTargetRef());
                    UserTaskDto model = new UserTaskDto();
                    model.setTaskKey(userTask.getId());
                    model.setTaskName(userTask.getName());
                    model.setMultiple(userTask.getLoopCharacteristics() != null);
                    model.setSeq(seq);
                    if (list.stream().noneMatch(p -> p.getTaskKey().equalsIgnoreCase(userTask.getId()))) {
                        model.setUsers(getUserList(processInstanceId, userTask, historicActivityInstances, keyVariables));
                        list.add(model);
                        handleNextFlowElement(processInstanceId, userTask.getId(), keyVariables, bpmnModel, historicActivityInstances, list);
                    }
                }
            }
        }
    }

    private List<Map> getUserList(String processInstanceId, UserTask userTask, List<HistoricActivityInstance> historicActivityInstances, Map variables) {

        List<String> candidates = Lists.newArrayList();
        if (variables.containsKey("candidates")) {
            candidates = Arrays.stream(StringUtils.split(variables.get("candidates").toString(), ","))
                    .filter(p -> StringUtils.split(p, "_")[0].equalsIgnoreCase(userTask.getId())).map(p -> StringUtils.split(p, "_")[1]).collect(Collectors.toList());
        }

        List<Map> users = Lists.newArrayList();
        if (userTask.getLoopCharacteristics() != null) {
            try {
                ExpressionFactory factory = new ExpressionFactoryImpl();
                SimpleContext context = new SimpleContext();
                for (Object key : variables.keySet()) {
                    context.setVariable(key.toString(), factory.createValueExpression(variables.get(key), variables.get(key).getClass()));
                }
                List<String> userLoginList = (List<String>) factory.createValueExpression(context, userTask.getLoopCharacteristics().getInputDataItem(), List.class).getValue(context);
                for (String userLogin : userLoginList) {
                    Map user = Maps.newHashMap();
                    user.put("userLogin", userLogin);
                    user.put("userName", hrEmployeeMapper.getNameByUserLogin(userLogin));
                    user.put("selected", true);
                    if (historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId)
                            .taskAssignee(userLogin).taskDefinitionKey(userTask.getId()).orderByHistoricTaskInstanceEndTime().desc().count() > 0) {
                        String preTaskId = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId)
                                .taskAssignee(userLogin).taskDefinitionKey(userTask.getId()).orderByHistoricTaskInstanceEndTime().desc().list().get(0).getId();
                        if (historyService.createHistoricVariableInstanceQuery().taskId(preTaskId).variableName("back_candidate").singleResult() != null) {
                            //代表已经处理过了
                            user.put("selected", false);
                        }

                    }
                    users.add(user);
                }
            } catch (Exception ex) {
                System.out.println(userTask.getName() + "获取用户失败!");
            }
        } else if (StringUtils.isNotEmpty(userTask.getAssignee())) {

            try {
                String assigne = userTask.getAssignee();
                if (assigne.contains("${")) {
                    ExpressionFactory factory = new ExpressionFactoryImpl();
                    SimpleContext context = new SimpleContext();
                    for (Object key : variables.keySet()) {
                        context.setVariable(key.toString(), factory.createValueExpression(variables.get(key), variables.get(key).getClass()));
                    }
                    assigne = factory.createValueExpression(context, assigne, String.class).getValue(context).toString();
                }
                Map user = Maps.newHashMap();
                user.put("userLogin", assigne);
                user.put("userName", hrEmployeeMapper.getNameByUserLogin(assigne));
                user.put("selected", true);
                users.add(user);
            } catch (Exception ex) {
                System.out.println(userTask.getName() + "获取用户失败!");
            }
        } else if (userTask.getCandidateUsers().size() > 0) {

            List<String> candidateUsers = Lists.newArrayList();
            for (String sysCandidateUser : userTask.getCandidateUsers()) {
                if (sysCandidateUser.contains("${")) {
                    ExpressionFactory factory = new ExpressionFactoryImpl();
                    SimpleContext context = new SimpleContext();
                    for (Object key : variables.keySet()) {
                        context.setVariable(key.toString(), factory.createValueExpression(variables.get(key), variables.get(key).getClass()));
                    }
                    Object result = factory.createValueExpression(context, sysCandidateUser, List.class).getValue(context);
                    candidateUsers.addAll((ArrayList<String>) result);
                } else {
                    candidateUsers.add(sysCandidateUser);
                }
            }

            String pre = "";
            if (historicActivityInstances.stream().anyMatch(p -> p.getTaskId().equalsIgnoreCase(userTask.getId()))) {
                pre = historicActivityInstances.stream().filter(p -> p.getTaskId().equalsIgnoreCase(userTask.getId())).map(p -> p.getAssignee()).findFirst().get();
            }

            for (String userLogin : candidateUsers) {
                Map user = Maps.newHashMap();
                user.put("userLogin", userLogin);
                user.put("userName", hrEmployeeMapper.getNameByUserLogin(userLogin));
                user.put("selected", pre.equalsIgnoreCase(userLogin));
                users.add(user);
            }

            if (StringUtils.isEmpty(pre) && users.size() >= 1) {
                users.get(0).put("selected", true);
            }
        }

        if (candidates.size() > 0 && users.size() > 1) {
            for (Map user : users) {
                String userLogin = user.get("userLogin").toString();
                user.put("selected", candidates.contains(userLogin));
            }
        }
        return users;
    }


    public Map  getNgDirectUrl(String businessKey,String enLogin) {
        Map result = Maps.newHashMap();
        Map params = Maps.newHashMap();
        result.put("params", params);
        List<String> formKeyList=Arrays.asList("fiveEd");
        if(formKeyList.stream().anyMatch(p->businessKey.startsWith(p))) {
            CommonFormData commonFormData = commonFormDataService.getModelByBusinessKey(businessKey);
            Map data = JsonMapper.string2Map(commonFormData.getFormData());
            result.put("url", "five.detail.formDetail");
            params.put("formDataId", commonFormData.getId());
            params.put("id", Integer.parseInt(data.getOrDefault("projectId", "").toString()));
            if (businessKey.startsWith("fiveEdTask")) {
                result.put("url", "five.detail.taskDetail");
            }
            return result;
        }

        result.put("url", "five.dashboard");
        try {
            String key = StringUtils.split(businessKey, "_")[0].trim();
            int projectId = 0;
            int businessId = 0;

            if (businessKey.contains("_five_")) {
                businessId = Integer.parseInt(StringUtils.split(businessKey, "_")[2].trim());
            } else {
                projectId = Integer.parseInt(StringUtils.split(businessKey, "_")[1].trim());
                if (StringUtils.split(businessKey, "_").length > 2) {
                    businessId = Integer.parseInt(StringUtils.split(businessKey, "_")[2].trim());
                }
            }


            switch (key) {
                //五洲综合管理
                case EdConst.FIVE_OA_INVENT_PAYMENT://专利缴费申请
                    result.put("url", "five.oaInventPaymentDetail");
                    params.put("paymentId", projectId);
                    break;
                case EdConst.FIVE_OA_DEPT_JOURNAL://院刊审查
                    result.put("url", "five.oaDeptJournalDetail");
                    params.put("journalId", projectId);
                    break;
                case EdConst.FIVE_OA_MATERIAL_BORROW://资料采购申请表
                    result.put("url", "five.oaMaterialBorrowDetail");
                    params.put("materialBorrowId", projectId);
                    break;
                case EdConst.FIVE_OA_MATERIAL_PURCHASE://资料采购申请表
                    result.put("url", "five.oaMaterialPurchaseDetail");
                    params.put("materialPurchaseId", projectId);
                    break;
                case EdConst.FIVE_OA_DISPATCH://公司发文 11个
                    result.put("url", "five.oaDispatchDetail");
                    params.put("dispatchId", projectId);
                    break;
                case EdConst.FIVE_OA_DISPATCH_CPC://五洲公司委员会发文稿
                    result.put("url", "five.oaDispatchCPCDetail");
                    params.put("dispatchCPCId", projectId);
                    break;
                case EdConst.FIVE_OA_DISPATCH_ACADEMY://五洲工程设计研究院发文单
                    result.put("url", "five.oaDispatchAcademyDetail");
                    params.put("dispatchAcademyId", projectId);
                    break;
                case EdConst.FIVE_OA_FILE_SYNERGY://协同文件
                    result.put("url", "five.oaFileSynergyDetail");
                    params.put("synergyId", projectId);
                    break;
                case EdConst.FIVE_OA_DISPATCH_CPCA_ACADEMY://中共五洲工程设计研究委员会院发文单
                    result.put("url", "five.oaDispatchCPCAcademyDetail");
                    params.put("dispatchCPCAcademyId", projectId);
                    break;
                case EdConst.FIVE_OA_GOABROAD://人力资源部 因公出国内部审批单
                    result.put("url", "five.oaGoAbroadDetail");
                    params.put("goAbroadId", projectId);
                    break;
                case EdConst.FIVE_OA_DEPATRMENTPOST://信息化建设与管理部 部门发文单
                    result.put("url", "five.oaDepartmentPostDetail");
                    params.put("departmentPostId", projectId);
                    break;
                case EdConst.FIVE_OA_CONTRACTLAWEXAMINE://公司办 合同法律审查工作单
                    result.put("url", "five.oaContractLawExamineDetail");
                    params.put("contractLawExamineId", projectId);
                    break;
                case EdConst.FIVE_OA_RULELAWEXAMINE://公司办 规章制度法律审查工作单
                    result.put("url", "five.oaRuleLawExamineDetail");
                    params.put("ruleLawExamineId", projectId);
                    break;
                case EdConst.FIVE_OA_FILE_INSTRUCTION://文件批示单
                    result.put("url", "five.oaFileInstructionDetail");
                    params.put("instructionId", projectId);
                    break;
                case EdConst.FIVE_OA_REPORT://报告文单
                    result.put("url", "five.oaReportDetail");
                    params.put("reportId", projectId);
                    break;
                case EdConst.FIVE_OA_SCIENTIFICTASKCOSTAPPLY://科研课题费用申请
                    result.put("url", "five.oaScientificTaskCostApplyDetail");
                    params.put("scientificTaskCostApplyId", projectId);
                    break;
                case EdConst.FIVE_OA_PLATFORM_RECORD://各地公共资源平台备案
                    result.put("url", "five.oaPlatformRecordDetail");
                    params.put("recordId", projectId);
                    break;
                case EdConst.FIVE_OA_REVIEW_CONTRACT://合同评审记录
                    result.put("url", "five.oaReviewContractDetail");
                    params.put("contractId", projectId);
                    break;
                case EdConst.FIVE_OA_CONTRACT_SIGN://业务合同签发单
                    result.put("url", "five.oaContractSignDetail");
                    params.put("signId", projectId);
                    break;
                case EdConst.FIVE_OA_BID_APPLY://投标申请单
                    result.put("url", "five.oaBidApplyDetail");
                    params.put("applyId", projectId);
                    break;
                case EdConst.FIVE_OA_ASSOCIATION_APPLY://协会入会申请
                    result.put("url", "five.oaAssociationApplyDetail");
                    params.put("applyId", projectId);
                    break;
                case EdConst.FIVE_OA_ASSOCIATION_CHANGE://协会信息变更流程
                    result.put("url", "five.oaAssociationChangeDetail");
                    params.put("changeId", projectId);
                    break;
                case EdConst.FIVE_OA_ASSOCIATION_PAYMENT://协会缴费流程
                    result.put("url", "five.oaAssociationPaymentDetail");
                    params.put("paymentId", projectId);
                    break;
                case EdConst.FIVE_OA_INLAND_PROJECT_APPLY://内部项目申请
                    result.put("url", "five.oaInlandProjectApplyDetail");
                    params.put("applyId", projectId);
                    break;
                case EdConst.FIVE_OA_RESEARCH_PROJECT_REVIEW://科技开发费项目评审
                    result.put("url", "five.oaResearchProjectReviewDetail");
                    params.put("projectId", projectId);
                    break;
                case EdConst.FIVE_OA_SOFTWARE://软件购置申请
                    result.put("url", "five.oaSoftwareDetail");
                    params.put("softwareId", projectId);
                    break;
                case EdConst.FIVE_OA_FILE_TRANSFER://非密网文件传送流程
                    result.put("url", "five.oaFileTransferDetail");
                    params.put("transferId", projectId);
                    break;
                case EdConst.FIVE_OA_COMPUTER_NETWORK://非密计算机入网审批
                    result.put("url", "five.oaComputerNetworkDetail");
                    params.put("networkId", projectId);
                    break;
                case EdConst.FIVE_OA_ASSET_COMPUTER://公司非密计算机及信息化设备台帐
                    result.put("url", "five.oaTrackAssetComputerDetail");
                    params.put("computerId", projectId);
                    break;
                case EdConst.FIVE_OA_COMPUTER_CHANGE://策略变更申请
                    result.put("url", "five.oaComputerChangeDetail");
                    params.put("changeId", projectId);
                    break;
                case EdConst.FIVE_OA_MESSAGE_EXPORT://个人非密信息导出审批
                    result.put("url", "five.oaMessageExportDetail");
                    params.put("exportId", projectId);
                    break;
                case EdConst.FIVE_OA_NEWSEXAMINE://新闻宣传及信息报送审查
                    result.put("url", "five.oaNewsExamineDetail");
                    params.put("newsExamineId", projectId);
                    break;
                case EdConst.FIVE_OA_PROJECTFUNDPLAN://项目资金使用计划
                    result.put("url", "five.oaProjectFundPlanDetail");
                    params.put("projectFundPlanId", projectId);
                    break;
                case EdConst.FIVE_OA_INFORMATIONEQUIPMENTEXAMINE://信息化设备验收审批表
                    result.put("url", "five.oaInformationEquipmentExamineDetail");
                    params.put("informationEquipmentExamineId", projectId);
                    break;
                case EdConst.FIVE_OA_INFORMATIONEQUIPMENTEXAMINE_LIST://信息化设备验收审批表
                    result.put("url", "five.oaInformationEquipmentExamineListDetail");
                    params.put("listId", projectId);
                    break;
                case EdConst.FIVE_OA_OUTSPECIALIST://外部专家申请表
                    result.put("url", "five.oaOutSpecialistDetail");
                    params.put("outSpecialistId", projectId);
                    break;
                case EdConst.FIVE_OA_NONINDEPENDENTDEPTSET://非独立运行中心设立申请表
                    result.put("url", "five.oaNonIndependentDeptSetDetail");
                    params.put("nonIndependentDeptSetId", projectId);
                    break;
                case EdConst.FIVE_OA_NONSECRETEQUIPMENTSCRAP://非密计算机及外设报废申请
                    result.put("url", "five.oaNonSecretEquipmentScrapDetail");
                    params.put("nonSecretEquipmentScrapId", projectId);
                    break;
                case EdConst.FIVE_OA_GOABROADTRAINASK://参加出国培训的请示
                    result.put("url", "five.oaGoAbroadTrainAskDetail");
                    params.put("goAbroadTrainAskId", projectId);
                    break;
                case EdConst.FIVE_OA_INFORMATIONEQUIPMENTPROCUREMENT://年度信息化设备采购预算表
                    result.put("url", "five.oaInformationEquipmentProcurementDetail");
                    params.put("informationEquipmentProcurementId", projectId);
                    break;
                case EdConst.FIVE_OA_INFORMATIONEQUIPMENTAPPLY://信息化设备购置申请单
                    result.put("url", "five.oaInformationEquipmentApplyDetail");
                    params.put("informationEquipmentApplyId", projectId);
                    break;
                case EdConst.FIVE_OA_PRIVILEGE_MANAGEMENT://权限调整
                    result.put("url", "five.oaPrivilegeManagementDetail");
                    params.put("privilegeManagementId", projectId);
                    break;
                case EdConst.FIVE_OA_GOABROADTRAINDECLARE://出国培训申报表
                    result.put("url", "five.oaGoAbroadTrainDeclareDetail");
                    params.put("goAbroadTrainDeclareId", projectId);
                    break;
                case EdConst.FIVE_OA_PROFESSIONALSKILLTRAIN://专业技术培训申请表
                    result.put("url", "five.oaProfessionalSkillTrainDetail");
                    params.put("professionalSkillTrainId", projectId);
                    break;
                case EdConst.FIVE_OA_OUTTECHNICALEXCHANGE://参加外部技术交流会议审批
                    result.put("url", "five.oaOutTechnicalExchangeDetail");
                    params.put("outTechnicalExchangeId", projectId);
                    break;
                case EdConst.FIVE_OA_TECHNOLOGYARTICLEEXAMINE://对外发表科技论文审查单
                    result.put("url", "five.oaTechnologyArticleExamineDetail");
                    params.put("technologyArticleExamineId", projectId);
                    break;
                case EdConst.FIVE_OA_PRESSUREPIPSEALEXAMINE://压力管道设计资格印章使用审批表
                    result.put("url", "five.oaPressurePipSealExamineDetail");
                    params.put("pressurePipSealExamineId", projectId);
                    break;
                case EdConst.OA_STAMP_APPLY_CONTRACT://用印审批合同章
                    result.put("url", "oa.stampApplyDetail");
                    params.put("stampId", projectId);
                    break;
                case EdConst.OA_STAMP_APPLY_CONSTRUCTION://用印审批施工章
                    result.put("url", "oa.stampApplyDetail");
                    params.put("stampId", projectId);
                    break;
                case EdConst.OA_STAMP_APPLY_QUALITY://用印审批压力章
                    result.put("url", "oa.stampApplyDetail");
                    params.put("stampId", projectId);
                    break;
                case EdConst.OA_STAMP_APPLY_OFFICE://用印审批公司章
                    result.put("url", "five.oaStampApplyOfficeDetail");
                    params.put("applyId", projectId);
                    break;

                case EdConst.Five_OA_DECISION_MAKING://决策会议
                    result.put("url", "five.oaDecisionMakingDetail");
                    params.put("makingId", projectId);
                    break;
                case EdConst.Five_OA_DECISION_MAKING_PARTY://决策会议
                    result.put("url", "five.oaDecisionMakingDetail");
                    params.put("makingId", projectId);
                    break;
                case EdConst.Five_OA_DECISION_MAKING_DIRECTOR://决策会议
                    result.put("url", "five.oaDecisionMakingDetail");
                    params.put("makingId", projectId);
                    break;
                case EdConst.Five_OA_DECISION_MAKING_OFFICE://决策会议
                    result.put("url", "five.oaDecisionMakingDetail");
                    params.put("makingId", projectId);
                    break;
                case EdConst.FIVE_OA_SIGNQUOTE://签报
                    result.put("url", "five.oaSignQuoteDetail");
                    params.put("signQuoteId", projectId);
                    break;
                case EdConst.FIVE_OA_INSTITUTIONCOUNTSIGN://制度会签单
                    result.put("url", "five.oaInstitutionCountSignDetail");
                    params.put("institutionCountSignId", projectId);
                    break;
                case EdConst.FIVE_OA_GENERAL_COUNTERSIGN://通用会签
                    result.put("url", "five.oaGeneralCountersignDetail");
                    params.put("generalId", projectId);
                    break;
               /* case EdConst.FIVE_OA_SELF_CAR_APPLY://个人用车申请
                    result.put("url", "five.oaSelfCarApplyDetail");
                    params.put("id", projectId);
                    break;
                case EdConst.FIVE_OA_LEADER_CAR_APPLY://领导用车申请
                    result.put("url", "five.oaLeaderCarApplyDetail");
                    params.put("id", projectId);
                    break;*/
                case EdConst.FIVE_OA_EMPLOYEEENTRYEXAMINE://职工入职审批单
                    result.put("url", "five.oaEmployeeEntryExamineDetail");
                    params.put("employeeEntryExamineId", projectId);
                    break;
                case EdConst.FIVE_OA_FURNITURE_PURCHASE://办公家具采购
                    result.put("url", "five.oaFurniturePurchaseDetail");
                    params.put("furnitureId", projectId);
                    break;
                case EdConst.FIVE_OA_EMPLOYEETRANSFEREXAMINE://职工内部调整审批单
                    result.put("url", "five.oaEmployeeTransferExamineDetail");
                    params.put("employeeTransferExamineId", projectId);
                    break;
                case EdConst.FIVE_OA_PROCESSDEVELOPAPPLY://流程申请
                    result.put("url", "five.oaProcessDevelopApplyDetail");
                    params.put("processDevelopApplyId", projectId);
                    break;
                case EdConst.FIVE_OA_EMPLOYEERETIREEXAMINE://退休
                    result.put("url", "five.oaEmployeeRetireExamineDetail");
                    params.put("employeeRetireExamineId", projectId);
                    break;
                case EdConst.FIVE_OA_EMPLOYEEDIMISSIONEXAMINE://离职
                    result.put("url", "five.oaEmployeeDimissionExamineDetail");
                    params.put("employeeDimissionExamineId", projectId);
                    break;
                case EdConst.FIVE_OA_COMPUTER_MAINTAIN://计算机及外设维修服务单
                    result.put("url", "five.oaComputerMaintainDetail");
                    params.put("computerMaintainId", projectId);
                    break;
                case EdConst.FIVE_OA_COMPUTER_PERSON_REPAIR://信息化设备外部人员现场维修情况记录表
                    result.put("url", "five.oaComputerPersonRepairDetail");
                    params.put("computerPersonRepairId", projectId);
                    break;
                case EdConst.FIVE_OA_SECRET_SYSTEM://涉密信息系统账户及权限开通、变更申请
                    result.put("url", "five.oaSecretSystemDetail");
                    params.put("id", projectId);
                    break;
                case EdConst.FIVE_OA_COMPUTERAPPLICATION://公用计算机及U盘申请表
                    result.put("url", "five.oaComputerApplicationDetail");
                    params.put("computerApplicationId", projectId);
                    break;
                case EdConst.FIVE_OA_CARD_CHANGE://职工充值卡变动
                    result.put("url", "five.oaCardChangeDetail");
                    params.put("changeId", projectId);
                    break;
                case EdConst.FIVE_OA_EQUIPMENT_ACCEPTANCE://设备验收
                    result.put("url", "five.oaEquipmentAcceptanceDetail");
                    params.put("equipmentId", projectId);
                    break;
                case EdConst.FIVE_ASSET_ALLOT://固定资产调拨
                    result.put("url", "five.assetAllotDetail");
                    params.put("assetAllotId", projectId);
                    break;
                case EdConst.FIVE_COMPUTER_SCRAP://非密计算机及外设设备报废审批表
                    result.put("url", "five.computerScrapDetail");
                    params.put("computerScrapId", projectId);
                    break;
                case EdConst.FIVE_ASSET_SCRAP://固定资产设备报废
                    result.put("url", "five.assetScrapDetail");
                    params.put("assetScrapId", projectId);
                    break;

                //五洲项目
//                case EdConst.FIVE_ED_TASK://设计任务书
//                    result.put("url", "five.detail.taskDetail");
//                    params.put("taskId", businessId);
//                    params.put("id", projectId);
//                    break;
//                case EdConst.FIVE_ED_ARRANGE://人员与计划安排
//                    result.put("url", "five.detail.arrangeDetail");
//                    params.put("arrangeId", businessId);
//                    params.put("id", projectId);
//                    break;
                case EdConst.FIVE_ED_DESIGN_RULE://设计指导性文件
                    result.put("url", "five.detail.designRuleDetail");
                    params.put("designRuleId", businessId);
                    params.put("id", projectId);
                    break;
                case EdConst.FIVE_ED_REVIEW_MAJOR://专业方案讨论
                    result.put("url", "five.detail.reviewMajorDetail");
                    params.put("reviewMajorId", businessId);
                    params.put("id", projectId);
                    break;
                case EdConst.FIVE_ED_REVIEW_PLAN://总体方案评审
                    result.put("url", "five.detail.reviewPlanDetail");
                    params.put("reviewPlanId", businessId);
                    params.put("id", projectId);
                    break;
                case EdConst.FIVE_ED_REVIEW_SPECIAL://专项评审
                    result.put("url", "five.detail.reviewSpecialDetail");
                    params.put("reviewSpecialId", businessId);
                    params.put("id", projectId);
                    break;
                case EdConst.FIVE_ED_REVIEW_APPLY://公司级设计评审申报表
                    result.put("url", "five.detail.reviewApplyDetail");
                    params.put("reviewApplyId", businessId);
                    params.put("id", projectId);
                    break;
                case EdConst.FIVE_ED_HOUSE_REFER://设计信息联系单
                    result.put("url", "five.detail.houseReferDetail");
                    params.put("houseReferId", businessId);
                    params.put("id", projectId);
                    break;
                case EdConst.FIVE_ED_VALIDATE://设计文件校审单
                    result.put("url", "five.detail.houseValidateDetail");
                    params.put("validateId", businessId);
                    params.put("id", projectId);
                    break;
                case EdConst.FIVE_ED_OUT_REVIEW://外部设计评审
                    result.put("url", "five.detail.outReviewDetail");
                    params.put("outReviewId", businessId);
                    params.put("id", projectId);
                    break;
                case EdConst.FIVE_ED_SERVICE_CHANGE://设计变更通知单
                    result.put("url", "five.detail.serviceChangeDetail");
                    params.put("serviceChangeId", businessId);
                    params.put("id", projectId);
                    break;
                case EdConst.FIVE_ED_SERVICE_DISCUSS://施工单位变更设计洽商单
                    result.put("url", "five.detail.serviceDiscussDetail");
                    params.put("serviceDiscussId", businessId);
                    params.put("id", projectId);
                    break;

                case EdConst.FIVE_ED_PLOT_APPLY://出图申请单
                    result.put("url", "five.detail.plotApplyDetail");
                    params.put("plotId", businessId);
                    params.put("id", projectId);
                    break;
                case EdConst.FIVE_ED_STAMP://设计成果用印申请
                    result.put("url", "five.detail.stampDetail");
                    params.put("stampId", businessId);
                    params.put("id", projectId);
                    break;
                case EdConst.FIVE_ED_PRODUCT://设计文件图纸验收清单
                    result.put("url", "five.detail.productDetail");
                    params.put("productId", businessId);
                    params.put("id", projectId);
                    break;
                case EdConst.FIVE_ED_RETURN_VISIT://工程设计回访记录
                    result.put("url", "five.detail.returnVisitDetail");
                    params.put("returnVisitId", businessId);
                    params.put("id", projectId);
                    break;
                case EdConst.FIVE_ED_QUALITY_ISSUE://设计质量问题登记
                    result.put("url", "five.detail.qualityIssueDetail");
                    params.put("qualityIssueId", businessId);
                    params.put("id", projectId);
                    break;
                case EdConst.FIVE_ED_QUALITY_CHECK://质量抽查审校记录单
                    result.put("url", "five.detail.qualityCheckDetail");
                    params.put("qualityCheckId", businessId);
                    params.put("id", projectId);
                    break;
                case EdConst.FIVE_ED_DESIGN_DRAWING_CHECK://设计图纸资料交验
                    result.put("url", "five.detail.designDrawingCheckDetail");
                    params.put("checkId", businessId);
                    params.put("id", projectId);
                    break;
                case EdConst.FIVE_ED_MAJOR_DRAWING_CHECK://专业图纸验收清单
                    result.put("url", "five.detail.majorDrawingCheckDetail");
                    params.put("checkId", businessId);
                    params.put("id", projectId);
                    break;
                //技术质量管理
                case EdConst.OA_NOTICE_APPLY://电子公告
                    result.put("url", "oa.noticeApplyDetail");
                    params.put("id", projectId);
                    break;
                case EdConst.OA_KNOWLEDGE://知识库
                    result.put("url", "oa.knowledgeDetail");
                    params.put("id", projectId);
                    break;
                case EdConst.OA_TECHNOLOGY://科技质量申报
                    result.put("url", "oa.technologyDetail");
                    params.put("id", projectId);
                    break;
                case EdConst.OA_SOFTWARE_APPLY://购置软件申报
                    result.put("url", "oa.softwareApplyDetail");
                    params.put("id", projectId);
                    break;
                case EdConst.FIVE_OA_INFORMATION_PLAN://年度软件采购预算
                    result.put("url", "five.oaInformationPlanDetail");
                    params.put("planId", projectId);
                    break;
                case EdConst.OA_ARCHIVE://工程项目归档申请
                    result.put("url", "oa.archiveDetail");
                    params.put("id", projectId);
                    break;
                case EdConst.OA_ARCHIVE_APPLY://工程资料借阅
                    result.put("url", "oa.archiveApplyDetail");
                    params.put("id", projectId);
                    break;
                case EdConst.FIVE_OA_MEETINGROME_APPLY:// 会议室申请
                    result.put("url", "five.oaMeetingRoomApplyDetail");
                    params.put("id", projectId);
                    break;
                case EdConst.FIVE_OA_MEETING_ARRANGE:// 会议安排
                    result.put("url", "five.oaMeetingArrangeDetail");
                    params.put("id", projectId);
                    break;



                //用印管理
                case EdConst.STAMP_OTHER: //其他用印申请
                    result.put("url", "ed.detail.stampOtherDetail");
                    params.put("stampOtherId", businessId);
                    params.put("id", projectId);
                    break;
                case EdConst.STAMP_ED://设计成果用印申请
                    result.put("url", "ed.detail.stampEdDetail");
                    params.put("stampEdId", businessId);
                    params.put("id", projectId);
                    break;
                case EdConst.STAMP_CHANGE_AND_PROCESS://变更及过程资料用印申请
                    result.put("url", "ed.detail.stampChangeAndProcessDetail");
                    params.put("changeAndProcessId", businessId);
                    params.put("id", projectId);
                    break;
                case EdConst.EXPLORE_STAMP_CHANGE_AND_PROCESS://勘察-变更及过程资料用印申请
                    result.put("url", "explore.detail.stampChangeAndProcessDetail");
                    params.put("changeAndProcessId", businessId);
                    params.put("id", projectId);
                    break;
                case EdConst.EXPLORE_STAMP_OTHER://勘察-其他用印申请
                    result.put("url", "explore.detail.stampOtherDetail");
                    params.put("stampOtherId", businessId);
                    params.put("id", projectId);
                    break;
                case EdConst.STAMP_EXPLORE://勘察-其他用印申请
                    result.put("url", "explore.detail.stampExploreDetail");
                    params.put("stampExploreId", businessId);
                    params.put("id", projectId);
                    break;
                case EdConst.STAMP_NO_CONTRACT://未签合同用印申请
                    result.put("url", "me.stampNoDetail");
                    params.put("stampId", projectId);
                    break;
                //人资管理
                case EdConst.HR_INCOME_PROOF:
                    result.put("url", "hr.incomeProofDetail");
                    params.put("incomeProofId", projectId);
                    break;
                case EdConst.HR_VACATION:
                    result.put("url", "hr.vacationDetail");
                    params.put("vacationId", projectId);
                    break;
                case EdConst.HR_LEAVE:
                    result.put("url", "hr.leaveDetail");
                    params.put("leaveId", projectId);
                    break;
                case EdConst.HR_HONOR://个人荣誉
                    result.put("url", "hr.honorDetail");
                    params.put("honorId", projectId);
                    break;
                case EdConst.HR_EDUCATION://学历信息
                    result.put("url", "hr.educationDetail");
                    params.put("educationId", projectId);
                    break;
                case EdConst.HR_INSURE://参保证明申请表
                    result.put("url", "hr.insureDetail");
                    params.put("insureId", projectId);
                    break;
                case EdConst.HR_CERTIFICATION://职业资格
                    result.put("url", "hr.certificationDetail");
                    params.put("certificationId", projectId);
                    break;
                case EdConst.FIVE_OA_INVENT_APPLY://专利申请
                    result.put("url", "five.oaInventApplyDetail");
                    params.put("inventId", projectId);
                    break;
                //经营管理
                case EdConst.FIVE_HR_DESIGN_QUALIFY_APPLY://设计校审人员资格认定
                    result.put("url", "five.hrQualifyApplyDetail");
                    params.put("qualifyApplyId", projectId);
                    break;
                case EdConst.FIVE_HR_APPROVE_APPLY://审定人资格认定申请
                    result.put("url", "five.hrApproveApplyDetail");
                    params.put("qualifyApplyId", projectId);
                    break;
                case "fiveHrQualifyChief":
                    FiveHrQualifyChief fiveHrQualifyChief=fiveHrQualifyChiefMapper.selectByPrimaryKey(projectId);
                    if(fiveHrQualifyChief.getApplyType().contains("兼职")){
                        result.put("url", "five.hrProChiefApplyDetail");
                    }else{
                        result.put("url", "five.hrChiefApplyDetail");
                    }
                    params.put("id", projectId);
                    break;
                case "fiveHrExternalApply":
                    result.put("url", "five.hrExternalApplyDetail");
                    params.put("id", projectId);
                    break;

                //经营管理
                case EdConst.BUSINESS_RECORD:
                    result.put("url", "business.recordDetail");
                    params.put("recordId", projectId);
                    break;
                case EdConst.FIVE_BUSINESS_RECORD:
                    result.put("url", "five.businessRecordDetail");
                    params.put("recordId", projectId);
                    break;
                case EdConst.BUSINESS_BID_APPLY:
                    result.put("url", "business.bidApplyDetail");
                    params.put("bidApplyId", projectId);
                    break;
                case EdConst.BUSINESS_BID_ATTEND:
                    result.put("url", "business.bidAttendDetail");
                    params.put("attendId", projectId);
                    break;
                case EdConst.BUSINESS_BID_PROJECT_CHARGE:
                    result.put("url", "business.bidProjectChargeDetail");
                    params.put("projectChargeId", projectId);
                    break;
                case EdConst.BUSINESS_BID_CONTRACT:
                    result.put("url", "business.bidContractDetail");
                    params.put("contractId", projectId);
                    break;
                case EdConst.BUSINESS_CONTRACT://项目启动
                    result.put("url", "business.contractDetail");
                    params.put("contractId", projectId);
                    break;
                case EdConst.FIVE_BUSINESS_PRE_CONTRACT://超前任务单
                    result.put("url", "five.businessPreContractDetail");
                    params.put("preContractId", projectId);
                    break;
                case EdConst.FIVE_BUSINESS_CUSTOMER://客户信息录入
                    result.put("url", "five.businessCustomerDetail");
                    params.put("customerId", projectId);
                    break;
                case EdConst.FIVE_BUSINESS_SUPPLIER://供方信息录入
                    result.put("url", "five.businessSupplierDetail");
                    params.put("supplierId", projectId);
                    break;
                case EdConst.FIVE_BUSINESS_INPUT_CONTRACT://合同录入
                    result.put("url", "five.businessInputContractDetail");
                    params.put("inputContractId", projectId);
                    break;
                case EdConst.FIVE_BUSINESS_CONTRACT://项目启动
                    result.put("url", "business.contractDetail");
                    params.put("contractId", projectId);
                    break;
                case EdConst.FIVE_BUSINESSCONTRACTREVIEW://合同评审记录
                    result.put("url", "five.businessContractReviewDetail");
                    params.put("contractReviewId", projectId);
                    break;
                case EdConst.FIVE_BUSINESSCONTRACTREVIEWEASY://合同评审记录(简)
                    result.put("url", "five.businessContractReviewDetail");
                    params.put("contractReviewId", projectId);
                    break;
                case EdConst.FIVE_BUSINESS_SUBPACKAGE://分包评审
                    result.put("url", "five.businessSubpackageDetail");
                    params.put("subpackageId", projectId);
                    break;
                case EdConst.FIVE_BUSINESS_PURCHASE://采购评审
                    result.put("url", "five.businessPurchaseDetail");
                    params.put("purchaseId", projectId);
                    break;
                case EdConst.FIVE_BUSINESS_INCOME://合同收费
                    result.put("url", "five.businessIncomeDetail");
                    params.put("incomeId", projectId);
                    break;
                case EdConst.FIVE_BUSINESS_OUTASSIST://外协支出报表
                    result.put("url", "five.businessOutAssistDetail");
                    params.put("outAssistId", projectId);
                    break;
                case EdConst.FIVE_BUSINESS_COOPERATION_DELEGATION://经营合作项目确认
                    result.put("url", "five.businessCooperationDelegationDetail");
                    params.put("cooperationDelegationId", projectId);
                    break;
                case EdConst.FIVE_BUSINESS_COOPERATION_CONTRACT://内部协作协议合同
                    result.put("url", "five.businessCooperationContractDetail");
                    params.put("cooperationContractId", projectId);
                    break;
                case EdConst.FIVE_BUSINESS_ADVANCE://预支明细
                    result.put("url", "five.businessAdvanceDetail");
                    params.put("advanceId", projectId);
                    break;
                case EdConst.FIVE_BUSINESS_ADVANCE_COLLECT://支出签发
                    result.put("url", "five.businessAdvanceCollectDetail");
                    params.put("advanceCollectId", projectId);
                    break;
                case EdConst.FIVE_BUSINESS_TENDER_DOCUMENT_REVIEW://工程项目承包招标文件评审
                    result.put("url", "five.businessTenderDocumentReviewDetail");
                    params.put("tenderId", projectId);
                    break;
                    //财务管理
                case EdConst.FIVE_FINANCE_INCOME://收入管理
                    result.put("url", "finance.incomeDetail");
                    params.put("incomeId", projectId);
                    break;
                case EdConst.FIVE_FINANCE_NODE://票据管理
                    result.put("url", "finance.nodeDetail");
                    params.put("nodeId", projectId);
                    break;
                case EdConst.FIVE_FINANCE_INCOME_CONFIRM://收入确认
                    result.put("url", "finance.incomeConfirmDetail");
                    params.put("incomeConfirmId", projectId);
                    break;
                case EdConst.FIVE_FINANCE_INVOICE://发票申请
                    result.put("url", "finance.invoiceDetail");
                    params.put("invoiceId", projectId);
                    break;
                case EdConst.FIVE_FINANCE_INVOICE_CANCEL://发票作废
                    result.put("url", "finance.invoiceCancelDetail");
                    params.put("invoiceCancelId", projectId);
                    break;
                case EdConst.FIVE_FINANCE_INVOICE_COLLECTION://发票应收款催款
                    result.put("url", "finance.invoiceCollectionDetail");
                    params.put("invoiceCollectionId", projectId);
                    break;
                case EdConst.FIVE_FINANCE_BALANCE://资金余额上报
                    result.put("url", "finance.balanceDetail");
                    params.put("balanceId", projectId);
                    break;
                case EdConst.FIVE_FINANCE_STAMPTAX://印花税申报
                    result.put("url", "finance.stampTaxDetail");
                    params.put("stampTaxId", projectId);
                    break;
                case EdConst.FIVE_FINANCE_OUTSUPPLY://对外提供资料
                    result.put("url", "finance.outSupplyDetail");
                    params.put("outSupplyId", projectId);
                    break;
              /*  case "test"://资金余额上报
                    result.put("url", "finance.balanceDetail");
                    params.put("balanceId", projectId);
                    break;*/
                //常规督办
                case EdConst.FIVE_OA_SUPERVISE:
                    result.put("url", "five.superviseDetail");
                    params.put("superviseId", projectId);
                    break;
                //年度重点任务督办
                case EdConst.FIVE_OA_SUPERVISE_YEAR:
                    result.put("url", "five.superviseYearDetail");
                    params.put("superviseId", projectId);
                    break;
                //文件督办
                case EdConst.FIVE_OA_SUPERVISE_FILE:
                    result.put("url", "five.superviseFileDetail");
                    params.put("superviseId", projectId);
                    break;
                case EdConst.FIVE_OA_SUPERVISE_DETAIL:
                    result.put("url", "five.superviseDetailChild");
                    params.put("detailId", projectId);
                    break;
                //预算
                case EdConst.FIVE_BUDGET_INDEPENDENT:
                    result.put("url", "budget.independentDetail");
                    params.put("independentId", projectId);
                    break;
                case EdConst.FIVE_BUDGET_PRODUCTION:
                    result.put("url", "budget.productionDetail");
                    params.put("productionId", projectId);
                    break;
                case EdConst.FIVE_BUDGET_FUNCTION:
                    result.put("url", "budget.functionDetail");
                    params.put("functionId", projectId);
                    break;
                case EdConst.FIVE_BUDGET_MAINTAIN:
                    result.put("url", "budget.maintainDetail");
                    params.put("maintainId", projectId);
                    break;
                case EdConst.FIVE_BUDGET_STOCK:
                    result.put("url", "budget.stockDetail");
                    params.put("stockId", projectId);
                    break;
                case EdConst.FIVE_BUDGET_BUSINESS:
                    result.put("url", "budget.businessDetail");
                    params.put("businessId", projectId);
                    break;
                case EdConst.FIVE_BUDGET_POSTEXPENSE:
                    result.put("url", "budget.postExpenseDetail");
                    params.put("postExpenseId", projectId);
                    break;
                case EdConst.FIVE_BUDGET_SCINETIFICFUNDSOUT:
                    result.put("url", "budget.scientificFundsOutDetail");
                    params.put("scientificFundsOutId", projectId);
                    break;
                case EdConst.FIVE_BUDGET_SCINETIFICFUNDSIN:
                    result.put("url", "budget.scientificFundsInDetail");
                    params.put("scientificFundsInId", projectId);
                    break;
                case EdConst.FIVE_BUDGET_PUBLICFUNDS:
                    result.put("url", "budget.publicFundsDetail");
                    params.put("publicFundsId", projectId);
                    break;
                case EdConst.FIVE_BUDGET_CAPITALOUT:
                    result.put("url", "budget.capitalOutDetail");
                    params.put("capitalOutId", projectId);
                    break;
                case EdConst.FIVE_BUDGET_LABORCOST:
                    result.put("url", "budget.laborCostDetail");
                    params.put("laborCostId", projectId);
                    break;
                case EdConst.FIVE_BUDGET_STAFFNUMBER:
                    result.put("url", "budget.staffNumberDetail");
                    params.put("staffNumberId", projectId);
                    break;
                case EdConst.FIVE_BUDGET_TURNINRENT:
                    result.put("url", "budget.turnInRentDetail");
                    params.put("turnInRentId", projectId);
                    break;
                case EdConst.FIVE_BUDGET_TURN_IN:
                    result.put("url", "budget.turnInDetail");
                    params.put("turnInId", projectId);
                    break;
                case EdConst.FIVE_BUDGET_FEE:
                    result.put("url", "budget.feeDetail");
                    params.put("feeId", projectId);
                    break;
                case EdConst.FIVE_BUDGET_FEE_Change:
                    result.put("url", "budget.feeChangeDetail");
                    params.put("feeId", projectId);
                    break;
                case EdConst.FIVE_FINANCE_SELFBANK:
                    result.put("url", "finance.selfBankDetail");
                    params.put("selfBankId", projectId);
                    break;
                case EdConst.FIVE_FINANCE_COMPANYBANK:
                    result.put("url", "finance.companyBankDetail");
                    params.put("companyBankId", projectId);
                    break;
                case EdConst.FIVE_BUSINESS_CHANGECUSTOMER:
                    result.put("url", "five.businessChangeCustomerDetail");
                    params.put("changeCustomerId", projectId);
                    break;
                case EdConst.FIVE_BUSINESS_CHANGESUPPLIER:
                    result.put("url", "five.businessChangeSupplierDetail");
                    params.put("changeSupplierId", projectId);
                    break;
                case EdConst.FIVE_FINANCE_TRANSFER_ACCOUNTS_COMMON://费用申请
                    result.put("url", "finance.transferAccountsDetail");
                    params.put("transferAccountsId", projectId);
                    break;
                case EdConst.FIVE_FINANCE_TRANSFER_ACCOUNTS_RED:
                    result.put("url", "finance.transferAccountsRedDetail");
                    params.put("transferAccountsId", projectId);
                    break;
                case EdConst.FIVE_FINANCE_TRANSFER_ACCOUNTS_BUILD:
                    result.put("url", "finance.transferAccountsBuildDetail");
                    params.put("transferAccountsId", projectId);
                    break;
                case EdConst.FIVE_FINANCE_TRANSFER_FEE:
                    result.put("url", "finance.transferFeeDetail");
                    params.put("transferFeeId", projectId);
                    break;
                case EdConst.FIVE_FINANCE_LOAN_COMMON://借款
                    result.put("url", "finance.loanDetail");
                    params.put("loanId", projectId);
                    break;
                case EdConst.FIVE_FINANCE_LOAN_FUNCTION:
                    result.put("url", "finance.loanFunctionDetail");
                    params.put("loanId", projectId);
                    break;
                case EdConst.FIVE_FINANCE_LOAN_RED:
                    result.put("url", "finance.loanRedDetail");
                    params.put("loanId", projectId);
                    break;
                case EdConst.FIVE_FINANCE_LOAN_BUILD:
                    result.put("url", "finance.loanBuildDetail");
                    params.put("loanId", projectId);
                    break;
                case EdConst.FIVE_FINANCE_REIMBURSE_COMMON://费用报销
                    result.put("url", "finance.reimburseDetail");
                    params.put("reimburseId", projectId);
                    break;
                case EdConst.FIVE_FINANCE_REIMBURSE_FUNCTION:
                    result.put("url", "finance.reimburseFunctionDetail");
                    params.put("reimburseId", projectId);
                    break;
                case EdConst.FIVE_FINANCE_REIMBURSE_RED:
                    result.put("url", "finance.reimburseRedDetail");
                    params.put("reimburseId", projectId);
                    break;
                case EdConst.FIVE_FINANCE_REIMBURSE_BUILD:
                    result.put("url", "finance.reimburseBuildDetail");
                    params.put("reimburseId", projectId);
                    break;
                case EdConst.FIVE_FINANCE_REFUND://还款
                    result.put("url", "finance.refundDetail");
                    params.put("refundId", projectId);
                    break;

                case EdConst.FIVE_FINANCE_TRAVEL_EXPENSE_COMMON://差旅费
                    result.put("url", "finance.travelExpenseDetail");
                    params.put("travelExpenseId", projectId);
                    break;
                case EdConst.FIVE_FINANCE_TRAVEL_EXPENSE_FUNCTION:
                    result.put("url", "finance.travelExpenseFunctionDetail");
                    params.put("travelExpenseId", projectId);
                    break;
                case EdConst.FIVE_FINANCE_TRAVEL_EXPENSE_RED:
                    result.put("url", "finance.travelExpenseRedDetail");
                    params.put("travelExpenseId", projectId);
                    break;
                case EdConst.FIVE_FINANCE_TRAVEL_EXPENSE_BUILD:
                    result.put("url", "finance.travelExpenseBuildDetail");
                    params.put("travelExpenseId", projectId);
                    break;
                case EdConst.FIVE_OA_EXTERNAL_RESEARCH_PROJECT_APPLY://外部科研项目申请
                    result.put("url", "five.oaExternalResearchProjectApplyDetail");
                    params.put("projectId", projectId);
                    break;
                case EdConst.FIVE_OA_EXTERNAL_STANDARD_APPLY://外部科研项目申请
                    result.put("url", "five.oaExternalStandardApplyDetail");
                    params.put("externalStandardApplyId", projectId);
                    break;
                default:
                    CommonFormData commonFormData = commonFormDataService.getModelByBusinessKey(businessKey);
                    int formDataId= commonFormData.getId();
                    projectId=commonFormData.getReferId();
                    result.put("url", "formData.detail");
                    params.put("id", projectId);
                    params.put("formDataId", formDataId);
                    break;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        result.put("params", params);
        return result;
    }


    public Map getNgActRelevanceUrl(String businessKey) {
        Map result = Maps.newHashMap();
        Map params = Maps.newHashMap();
        result.put("url", "dashboard");
        try {
            String key = StringUtils.split(businessKey, "_")[0].trim();
            int projectId = 0;
            int businessId = 0;

            if (businessKey.contains("_five_")) {
                businessId = Integer.parseInt(StringUtils.split(businessKey, "_")[2].trim());
            } else {
                projectId = Integer.parseInt(StringUtils.split(businessKey, "_")[1].trim());
                if (StringUtils.split(businessKey, "_").length > 2) {
                    businessId = Integer.parseInt(StringUtils.split(businessKey, "_")[2].trim());
                }
            }


            switch (key) {

                case EdConst.FIVE_OA_DISPATCH://公司发文 11个
                    result.put("url", "act.oaDispatch");
                    result.put("name", "dispatchId");
                    result.put("id", projectId);
                    params.put("dispatchId",projectId);
                    break;

            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        result.put("params", params);
        return result;
    }


    public Map getEdStepTimeline(int stepId) {
        Map result = Maps.newHashMap();
        result.put("stepId", stepId);
        EdProjectStep step = edProjectStepMapper.selectByPrimaryKey(stepId);
        result.put("stepName", step.getStepName());
        result.put("stageName", step.getStageName());
        List<Map> list = Lists.newArrayList();
        Map params = Maps.newHashMap();
        params.put("projectId", step.getProjectId());
        params.put("deleted", false);
        params.put("foreignKey", step.getId());
        List<EdProjectTree> treeList = edProjectTreeMapper.selectAll(params);
        //查询条件
        Map pa = Maps.newHashMap();
        pa.put("deleted", false);
        pa.put("stepId", stepId);
        Map item;
        ProcessInstanceDto processInstanceDto;
        for (EdProjectTree edProjectTree : treeList) {
            if (!edProjectTree.getParentId().equals(step.getId())) {
                String firstUserTaskName;
                switch (edProjectTree.getNodeUrl()) {

                }
            }
        }
        result.put("list", list);
        return result;
    }

    public Map getExploreStepTimeline(int stepId) {
        Map result = Maps.newHashMap();
        result.put("stepId", stepId);
        EdProjectStep step = edProjectStepMapper.selectByPrimaryKey(stepId);
        result.put("stepName", step.getStepName());
        result.put("stageName", step.getStageName());
        List<Map> list = Lists.newArrayList();
        Map params = Maps.newHashMap();
        params.put("projectId", step.getProjectId());
        params.put("deleted", false);
        params.put("foreignKey", step.getId());
        List<EdProjectTree> treeList = edProjectTreeMapper.selectAll(params);
        //查询条件
        Map pa = Maps.newHashMap();
        pa.put("deleted", false);
        pa.put("stepId", stepId);
        Map item;
        ProcessInstanceDto processInstanceDto;
        for (EdProjectTree edProjectTree : treeList) {
            if (!edProjectTree.getParentId().equals(step.getId())) {
                switch (edProjectTree.getNodeUrl()) {


                }
            }
        }
        result.put("list", list);
        return result;
    }

    public List<Map> getProjectTimeline(int projectId) {
        List<Map> list = Lists.newArrayList();
        Map params = Maps.newHashMap();
        params.put("projectId", projectId);
        params.put("deleted", false);
        List<EdProjectStep> edProjectStep = edProjectStepMapper.selectAll(params);
        for (EdProjectStep step : edProjectStep) {
            list.add(getEdStepTimeline(step.getId()));
        }
        return list;
    }

    public List<Map> getExploreTimeline(int projectId) {
        List<Map> list = Lists.newArrayList();
        Map params = Maps.newHashMap();
        params.put("projectId", projectId);
        params.put("deleted", false);
        List<EdProjectStep> edProjectStep = edProjectStepMapper.selectAll(params);
        for (EdProjectStep step : edProjectStep) {
            list.add(getExploreStepTimeline(step.getId()));
        }
        return list;
    }


}
