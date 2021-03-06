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

        //???????????????????????????????????????
        if (dtoList.stream().anyMatch(p -> !p.getTaskName().equalsIgnoreCase("??????"))) {
            dtoList = dtoList.stream().filter(p -> !p.getTaskName().equalsIgnoreCase("??????")).collect(Collectors.toList());
        }
        return dtoList;
    }

    /**
     * ??????????????????
     *
     * @param taskId
     * @return
     */
    public List<UserTaskDto> listBackUserTask(String taskId) {

        List<UserTaskDto> list = Lists.newArrayList();
        //?????????????????? ??????????????????
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String definitionKey = task.getTaskDefinitionKey();

        //????????????id??????????????????id
        String processInstanceId = task.getProcessInstanceId();
        String processDefinitionId = historyService.createHistoricActivityInstanceQuery().processInstanceId(task.getProcessInstanceId()).list().get(0).getProcessDefinitionId();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);


        Map variables = taskService.getVariables(taskId);
        //????????????????????????
        List<HistoricActivityInstance> historicActivityInstances = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricActivityInstanceStartTime().desc()
                .list().stream()
                .filter(p -> p.getActivityType().equals("userTask"))
                .filter(p -> !p.getTaskId().equalsIgnoreCase(definitionKey))
                .collect(Collectors.toList());

        //???????????????????????????userTaskKey
        UserTask currentUserTask = (UserTask) bpmnModel.getFlowElement(definitionKey);


        List<SequenceFlow> incomingFlows = currentUserTask.getIncomingFlows();
        FlowElement preElement = bpmnModel.getMainProcess().getFlowElement(incomingFlows.get(0).getSourceRef());
        //?????????????????????????????????????????????????????????????????????
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
            model.setTaskName("??????");
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
            //????????????
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
                    model.setTaskName("??????");
                    model.setMultiple(false);
                    model.setSeq(seq);
                    list.add(model);
                }
            }

        } else if (nextFlowElement instanceof ExclusiveGateway) {
            //????????????
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
                System.out.println(nextFlowElement.getName() + "????????????????????????????????????!");
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
                    model.setTaskName("??????");
                    model.setMultiple(false);
                    model.setSeq(seq);
                    list.add(model);
                }
            }

        } else if (nextFlowElement instanceof InclusiveGateway) {
            //????????????
            ExpressionFactory factory = new ExpressionFactoryImpl();
            SimpleContext context = new SimpleContext();
            for (Object key : keyVariables.keySet()) {
                context.setVariable(key.toString(), factory.createValueExpression(keyVariables.get(key), keyVariables.get(key).getClass()));
            }
            for (SequenceFlow outgoingFlow : ((InclusiveGateway) nextFlowElement).getOutgoingFlows()) {
                if (StringUtils.isNotEmpty(outgoingFlow.getConditionExpression())) {
                    //??????????????????
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
                            //????????????????????????
                            user.put("selected", false);
                        }

                    }
                    users.add(user);
                }
            } catch (Exception ex) {
                System.out.println(userTask.getName() + "??????????????????!");
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
                System.out.println(userTask.getName() + "??????????????????!");
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
                //??????????????????
                case EdConst.FIVE_OA_INVENT_PAYMENT://??????????????????
                    result.put("url", "five.oaInventPaymentDetail");
                    params.put("paymentId", projectId);
                    break;
                case EdConst.FIVE_OA_DEPT_JOURNAL://????????????
                    result.put("url", "five.oaDeptJournalDetail");
                    params.put("journalId", projectId);
                    break;
                case EdConst.FIVE_OA_MATERIAL_BORROW://?????????????????????
                    result.put("url", "five.oaMaterialBorrowDetail");
                    params.put("materialBorrowId", projectId);
                    break;
                case EdConst.FIVE_OA_MATERIAL_PURCHASE://?????????????????????
                    result.put("url", "five.oaMaterialPurchaseDetail");
                    params.put("materialPurchaseId", projectId);
                    break;
                case EdConst.FIVE_OA_DISPATCH://???????????? 11???
                    result.put("url", "five.oaDispatchDetail");
                    params.put("dispatchId", projectId);
                    break;
                case EdConst.FIVE_OA_DISPATCH_CPC://??????????????????????????????
                    result.put("url", "five.oaDispatchCPCDetail");
                    params.put("dispatchCPCId", projectId);
                    break;
                case EdConst.FIVE_OA_DISPATCH_ACADEMY://????????????????????????????????????
                    result.put("url", "five.oaDispatchAcademyDetail");
                    params.put("dispatchAcademyId", projectId);
                    break;
                case EdConst.FIVE_OA_FILE_SYNERGY://????????????
                    result.put("url", "five.oaFileSynergyDetail");
                    params.put("synergyId", projectId);
                    break;
                case EdConst.FIVE_OA_DISPATCH_CPCA_ACADEMY://???????????????????????????????????????????????????
                    result.put("url", "five.oaDispatchCPCAcademyDetail");
                    params.put("dispatchCPCAcademyId", projectId);
                    break;
                case EdConst.FIVE_OA_GOABROAD://??????????????? ???????????????????????????
                    result.put("url", "five.oaGoAbroadDetail");
                    params.put("goAbroadId", projectId);
                    break;
                case EdConst.FIVE_OA_DEPATRMENTPOST://??????????????????????????? ???????????????
                    result.put("url", "five.oaDepartmentPostDetail");
                    params.put("departmentPostId", projectId);
                    break;
                case EdConst.FIVE_OA_CONTRACTLAWEXAMINE://????????? ???????????????????????????
                    result.put("url", "five.oaContractLawExamineDetail");
                    params.put("contractLawExamineId", projectId);
                    break;
                case EdConst.FIVE_OA_RULELAWEXAMINE://????????? ?????????????????????????????????
                    result.put("url", "five.oaRuleLawExamineDetail");
                    params.put("ruleLawExamineId", projectId);
                    break;
                case EdConst.FIVE_OA_FILE_INSTRUCTION://???????????????
                    result.put("url", "five.oaFileInstructionDetail");
                    params.put("instructionId", projectId);
                    break;
                case EdConst.FIVE_OA_REPORT://????????????
                    result.put("url", "five.oaReportDetail");
                    params.put("reportId", projectId);
                    break;
                case EdConst.FIVE_OA_SCIENTIFICTASKCOSTAPPLY://????????????????????????
                    result.put("url", "five.oaScientificTaskCostApplyDetail");
                    params.put("scientificTaskCostApplyId", projectId);
                    break;
                case EdConst.FIVE_OA_PLATFORM_RECORD://??????????????????????????????
                    result.put("url", "five.oaPlatformRecordDetail");
                    params.put("recordId", projectId);
                    break;
                case EdConst.FIVE_OA_REVIEW_CONTRACT://??????????????????
                    result.put("url", "five.oaReviewContractDetail");
                    params.put("contractId", projectId);
                    break;
                case EdConst.FIVE_OA_CONTRACT_SIGN://?????????????????????
                    result.put("url", "five.oaContractSignDetail");
                    params.put("signId", projectId);
                    break;
                case EdConst.FIVE_OA_BID_APPLY://???????????????
                    result.put("url", "five.oaBidApplyDetail");
                    params.put("applyId", projectId);
                    break;
                case EdConst.FIVE_OA_ASSOCIATION_APPLY://??????????????????
                    result.put("url", "five.oaAssociationApplyDetail");
                    params.put("applyId", projectId);
                    break;
                case EdConst.FIVE_OA_ASSOCIATION_CHANGE://????????????????????????
                    result.put("url", "five.oaAssociationChangeDetail");
                    params.put("changeId", projectId);
                    break;
                case EdConst.FIVE_OA_ASSOCIATION_PAYMENT://??????????????????
                    result.put("url", "five.oaAssociationPaymentDetail");
                    params.put("paymentId", projectId);
                    break;
                case EdConst.FIVE_OA_INLAND_PROJECT_APPLY://??????????????????
                    result.put("url", "five.oaInlandProjectApplyDetail");
                    params.put("applyId", projectId);
                    break;
                case EdConst.FIVE_OA_INLAND_PROJECT_REVIEW://??????????????????
                    result.put("url", "five.oaInlandProjectReviewDetail");
                    params.put("reviewId", projectId);
                    break;
                case EdConst.FIVE_OA_RESEARCH_PROJECT_REVIEW://???????????????????????????
                    result.put("url", "five.oaResearchProjectReviewDetail");
                    params.put("projectId", projectId);
                    break;
                case EdConst.FIVE_OA_SOFTWARE://??????????????????
                    result.put("url", "five.oaSoftwareDetail");
                    params.put("softwareId", projectId);
                    break;
                case EdConst.FIVE_OA_FILE_TRANSFER://???????????????????????????
                    result.put("url", "five.oaFileTransferDetail");
                    params.put("transferId", projectId);
                    break;
                case EdConst.FIVE_OA_COMPUTER_NETWORK://???????????????????????????
                    result.put("url", "five.oaComputerNetworkDetail");
                    params.put("networkId", projectId);
                    break;
                case EdConst.FIVE_OA_ASSET_COMPUTER://?????????????????????????????????????????????
                    result.put("url", "five.oaTrackAssetComputerDetail");
                    params.put("computerId", projectId);
                    break;
                case EdConst.FIVE_OA_COMPUTER_CHANGE://??????????????????
                    result.put("url", "five.oaComputerChangeDetail");
                    params.put("changeId", projectId);
                    break;
                case EdConst.FIVE_OA_MESSAGE_EXPORT://??????????????????????????????
                    result.put("url", "five.oaMessageExportDetail");
                    params.put("exportId", projectId);
                    break;
                case EdConst.FIVE_OA_NEWSEXAMINE://?????????????????????????????????
                    result.put("url", "five.oaNewsExamineDetail");
                    params.put("newsExamineId", projectId);
                    break;
                case EdConst.FIVE_OA_PROJECTFUNDPLAN://????????????????????????
                    result.put("url", "five.oaProjectFundPlanDetail");
                    params.put("projectFundPlanId", projectId);
                    break;
                case EdConst.FIVE_OA_INFORMATIONEQUIPMENTEXAMINE://??????????????????????????????
                    result.put("url", "five.oaInformationEquipmentExamineDetail");
                    params.put("informationEquipmentExamineId", projectId);
                    break;
                case EdConst.FIVE_OA_INFORMATIONEQUIPMENTEXAMINE_LIST://??????????????????????????????
                    result.put("url", "five.oaInformationEquipmentExamineListDetail");
                    params.put("listId", projectId);
                    break;
                case EdConst.FIVE_OA_OUTSPECIALIST://?????????????????????
                    result.put("url", "five.oaOutSpecialistDetail");
                    params.put("outSpecialistId", projectId);
                    break;
                case EdConst.FIVE_OA_NONINDEPENDENTDEPTSET://????????????????????????????????????
                    result.put("url", "five.oaNonIndependentDeptSetDetail");
                    params.put("nonIndependentDeptSetId", projectId);
                    break;
                case EdConst.FIVE_OA_NONSECRETEQUIPMENTSCRAP://????????????????????????????????????
                    result.put("url", "five.oaNonSecretEquipmentScrapDetail");
                    params.put("nonSecretEquipmentScrapId", projectId);
                    break;
                case EdConst.FIVE_OA_GOABROADTRAINASK://???????????????????????????
                    result.put("url", "five.oaGoAbroadTrainAskDetail");
                    params.put("goAbroadTrainAskId", projectId);
                    break;
                case EdConst.FIVE_OA_INFORMATIONEQUIPMENTPROCUREMENT://????????????????????????????????????
                    result.put("url", "five.oaInformationEquipmentProcurementDetail");
                    params.put("informationEquipmentProcurementId", projectId);
                    break;
                case EdConst.FIVE_OA_INFORMATIONEQUIPMENTAPPLY://??????????????????????????????
                    result.put("url", "five.oaInformationEquipmentApplyDetail");
                    params.put("informationEquipmentApplyId", projectId);
                    break;
                case EdConst.FIVE_OA_PRIVILEGE_MANAGEMENT://????????????
                    result.put("url", "five.oaPrivilegeManagementDetail");
                    params.put("privilegeManagementId", projectId);
                    break;
                case EdConst.FIVE_OA_GOABROADTRAINDECLARE://?????????????????????
                    result.put("url", "five.oaGoAbroadTrainDeclareDetail");
                    params.put("goAbroadTrainDeclareId", projectId);
                    break;
                case EdConst.FIVE_OA_PROFESSIONALSKILLTRAIN://???????????????????????????
                    result.put("url", "five.oaProfessionalSkillTrainDetail");
                    params.put("professionalSkillTrainId", projectId);
                    break;
                case EdConst.FIVE_OA_OUTTECHNICALEXCHANGE://????????????????????????????????????
                    result.put("url", "five.oaOutTechnicalExchangeDetail");
                    params.put("outTechnicalExchangeId", projectId);
                    break;
                case EdConst.FIVE_OA_TECHNOLOGYARTICLEEXAMINE://?????????????????????????????????
                    result.put("url", "five.oaTechnologyArticleExamineDetail");
                    params.put("technologyArticleExamineId", projectId);
                    break;
                case EdConst.FIVE_OA_PRESSUREPIPSEALEXAMINE://?????????????????????????????????????????????
                    result.put("url", "five.oaPressurePipSealExamineDetail");
                    params.put("pressurePipSealExamineId", projectId);
                    break;
                case EdConst.OA_STAMP_APPLY_CONTRACT://?????????????????????
                    result.put("url", "oa.stampApplyDetail");
                    params.put("stampId", projectId);
                    break;
                case EdConst.OA_STAMP_APPLY_CONSTRUCTION://?????????????????????
                    result.put("url", "oa.stampApplyDetail");
                    params.put("stampId", projectId);
                    break;
                case EdConst.OA_STAMP_APPLY_QUALITY://?????????????????????
                    result.put("url", "oa.stampApplyDetail");
                    params.put("stampId", projectId);
                    break;
                case EdConst.OA_STAMP_APPLY_OFFICE://?????????????????????
                    result.put("url", "five.oaStampApplyOfficeDetail");
                    params.put("applyId", projectId);
                    break;

                case EdConst.Five_OA_DECISION_MAKING://????????????
                    result.put("url", "five.oaDecisionMakingDetail");
                    params.put("makingId", projectId);
                    break;
                case EdConst.Five_OA_DECISION_MAKING_PARTY://????????????
                    result.put("url", "five.oaDecisionMakingDetail");
                    params.put("makingId", projectId);
                    break;
                case EdConst.Five_OA_DECISION_MAKING_DIRECTOR://????????????
                    result.put("url", "five.oaDecisionMakingDetail");
                    params.put("makingId", projectId);
                    break;
                case EdConst.Five_OA_DECISION_MAKING_OFFICE://????????????
                    result.put("url", "five.oaDecisionMakingDetail");
                    params.put("makingId", projectId);
                    break;
                case EdConst.FIVE_OA_SIGNQUOTE://??????
                    result.put("url", "five.oaSignQuoteDetail");
                    params.put("signQuoteId", projectId);
                    break;
                case EdConst.FIVE_OA_INSTITUTIONCOUNTSIGN://???????????????
                    result.put("url", "five.oaInstitutionCountSignDetail");
                    params.put("institutionCountSignId", projectId);
                    break;
                case EdConst.FIVE_OA_GENERAL_COUNTERSIGN://????????????
                    result.put("url", "five.oaGeneralCountersignDetail");
                    params.put("generalId", projectId);
                    break;
               /* case EdConst.FIVE_OA_SELF_CAR_APPLY://??????????????????
                    result.put("url", "five.oaSelfCarApplyDetail");
                    params.put("id", projectId);
                    break;
                case EdConst.FIVE_OA_LEADER_CAR_APPLY://??????????????????
                    result.put("url", "five.oaLeaderCarApplyDetail");
                    params.put("id", projectId);
                    break;*/
                case EdConst.FIVE_OA_EMPLOYEEENTRYEXAMINE://?????????????????????
                    result.put("url", "five.oaEmployeeEntryExamineDetail");
                    params.put("employeeEntryExamineId", projectId);
                    break;
                case EdConst.FIVE_OA_FURNITURE_PURCHASE://??????????????????
                    result.put("url", "five.oaFurniturePurchaseDetail");
                    params.put("furnitureId", projectId);
                    break;
                case EdConst.FIVE_OA_EMPLOYEETRANSFEREXAMINE://???????????????????????????
                    result.put("url", "five.oaEmployeeTransferExamineDetail");
                    params.put("employeeTransferExamineId", projectId);
                    break;
                case EdConst.FIVE_OA_PROCESSDEVELOPAPPLY://????????????
                    result.put("url", "five.oaProcessDevelopApplyDetail");
                    params.put("processDevelopApplyId", projectId);
                    break;
                case EdConst.FIVE_OA_EMPLOYEERETIREEXAMINE://??????
                    result.put("url", "five.oaEmployeeRetireExamineDetail");
                    params.put("employeeRetireExamineId", projectId);
                    break;
                case EdConst.FIVE_OA_EMPLOYEEDIMISSIONEXAMINE://??????
                    result.put("url", "five.oaEmployeeDimissionExamineDetail");
                    params.put("employeeDimissionExamineId", projectId);
                    break;
                case EdConst.FIVE_OA_COMPUTER_MAINTAIN://?????????????????????????????????
                    result.put("url", "five.oaComputerMaintainDetail");
                    params.put("computerMaintainId", projectId);
                    break;
                case EdConst.FIVE_OA_COMPUTER_PERSON_REPAIR://??????????????????????????????????????????????????????
                    result.put("url", "five.oaComputerPersonRepairDetail");
                    params.put("computerPersonRepairId", projectId);
                    break;
                case EdConst.FIVE_OA_SECRET_SYSTEM://??????????????????????????????????????????????????????
                    result.put("url", "five.oaSecretSystemDetail");
                    params.put("id", projectId);
                    break;
                case EdConst.FIVE_OA_COMPUTERAPPLICATION://??????????????????U????????????
                    result.put("url", "five.oaComputerApplicationDetail");
                    params.put("computerApplicationId", projectId);
                    break;
                case EdConst.FIVE_OA_CARD_CHANGE://?????????????????????
                    result.put("url", "five.oaCardChangeDetail");
                    params.put("changeId", projectId);
                    break;
                case EdConst.FIVE_OA_EQUIPMENT_ACCEPTANCE://????????????
                    result.put("url", "five.oaEquipmentAcceptanceDetail");
                    params.put("equipmentId", projectId);
                    break;
                case EdConst.FIVE_ASSET_ALLOT://??????????????????
                    result.put("url", "five.assetAllotDetail");
                    params.put("assetAllotId", projectId);
                    break;
                case EdConst.FIVE_COMPUTER_SCRAP://?????????????????????????????????????????????
                    result.put("url", "five.computerScrapDetail");
                    params.put("computerScrapId", projectId);
                    break;
                case EdConst.FIVE_ASSET_SCRAP://????????????????????????
                    result.put("url", "five.assetScrapDetail");
                    params.put("assetScrapId", projectId);
                    break;

                //????????????
//                case EdConst.FIVE_ED_TASK://???????????????
//                    result.put("url", "five.detail.taskDetail");
//                    params.put("taskId", businessId);
//                    params.put("id", projectId);
//                    break;
//                case EdConst.FIVE_ED_ARRANGE://?????????????????????
//                    result.put("url", "five.detail.arrangeDetail");
//                    params.put("arrangeId", businessId);
//                    params.put("id", projectId);
//                    break;
                case EdConst.FIVE_ED_DESIGN_RULE://?????????????????????
                    result.put("url", "five.detail.designRuleDetail");
                    params.put("designRuleId", businessId);
                    params.put("id", projectId);
                    break;
                case EdConst.FIVE_ED_REVIEW_MAJOR://??????????????????
                    result.put("url", "five.detail.reviewMajorDetail");
                    params.put("reviewMajorId", businessId);
                    params.put("id", projectId);
                    break;
                case EdConst.FIVE_ED_REVIEW_PLAN://??????????????????
                    result.put("url", "five.detail.reviewPlanDetail");
                    params.put("reviewPlanId", businessId);
                    params.put("id", projectId);
                    break;
                case EdConst.FIVE_ED_REVIEW_SPECIAL://????????????
                    result.put("url", "five.detail.reviewSpecialDetail");
                    params.put("reviewSpecialId", businessId);
                    params.put("id", projectId);
                    break;
                case EdConst.FIVE_ED_REVIEW_APPLY://??????????????????????????????
                    result.put("url", "five.detail.reviewApplyDetail");
                    params.put("reviewApplyId", businessId);
                    params.put("id", projectId);
                    break;
                case EdConst.FIVE_ED_HOUSE_REFER://?????????????????????
                    result.put("url", "five.detail.houseReferDetail");
                    params.put("houseReferId", businessId);
                    params.put("id", projectId);
                    break;
                case EdConst.FIVE_ED_VALIDATE://?????????????????????
                    result.put("url", "five.detail.houseValidateDetail");
                    params.put("validateId", businessId);
                    params.put("id", projectId);
                    break;
                case EdConst.FIVE_ED_OUT_REVIEW://??????????????????
                    result.put("url", "five.detail.outReviewDetail");
                    params.put("outReviewId", businessId);
                    params.put("id", projectId);
                    break;
                case EdConst.FIVE_ED_SERVICE_CHANGE://?????????????????????
                    result.put("url", "five.detail.serviceChangeDetail");
                    params.put("serviceChangeId", businessId);
                    params.put("id", projectId);
                    break;
                case EdConst.FIVE_ED_SERVICE_DISCUSS://?????????????????????????????????
                    result.put("url", "five.detail.serviceDiscussDetail");
                    params.put("serviceDiscussId", businessId);
                    params.put("id", projectId);
                    break;

                case EdConst.FIVE_ED_PLOT_APPLY://???????????????
                    result.put("url", "five.detail.plotApplyDetail");
                    params.put("plotId", businessId);
                    params.put("id", projectId);
                    break;
                case EdConst.FIVE_ED_STAMP://????????????????????????
                    result.put("url", "five.detail.stampDetail");
                    params.put("stampId", businessId);
                    params.put("id", projectId);
                    break;
                case EdConst.FIVE_ED_PRODUCT://??????????????????????????????
                    result.put("url", "five.detail.productDetail");
                    params.put("productId", businessId);
                    params.put("id", projectId);
                    break;
                case EdConst.FIVE_ED_RETURN_VISIT://????????????????????????
                    result.put("url", "five.detail.returnVisitDetail");
                    params.put("returnVisitId", businessId);
                    params.put("id", projectId);
                    break;
                case EdConst.FIVE_ED_QUALITY_ISSUE://????????????????????????
                    result.put("url", "five.detail.qualityIssueDetail");
                    params.put("qualityIssueId", businessId);
                    params.put("id", projectId);
                    break;
                case EdConst.FIVE_ED_QUALITY_CHECK://???????????????????????????
                    result.put("url", "five.detail.qualityCheckDetail");
                    params.put("qualityCheckId", businessId);
                    params.put("id", projectId);
                    break;
                case EdConst.FIVE_ED_DESIGN_DRAWING_CHECK://????????????????????????
                    result.put("url", "five.detail.designDrawingCheckDetail");
                    params.put("checkId", businessId);
                    params.put("id", projectId);
                    break;
                case EdConst.FIVE_ED_MAJOR_DRAWING_CHECK://????????????????????????
                    result.put("url", "five.detail.majorDrawingCheckDetail");
                    params.put("checkId", businessId);
                    params.put("id", projectId);
                    break;
                //??????????????????
                case EdConst.OA_NOTICE_APPLY://????????????
                    result.put("url", "oa.noticeApplyDetail");
                    params.put("id", projectId);
                    break;
                case EdConst.OA_KNOWLEDGE://?????????
                    result.put("url", "oa.knowledgeDetail");
                    params.put("id", projectId);
                    break;
                case EdConst.OA_TECHNOLOGY://??????????????????
                    result.put("url", "oa.technologyDetail");
                    params.put("id", projectId);
                    break;
                case EdConst.OA_SOFTWARE_APPLY://??????????????????
                    result.put("url", "oa.softwareApplyDetail");
                    params.put("id", projectId);
                    break;
                case EdConst.FIVE_OA_INFORMATION_PLAN://????????????????????????
                    result.put("url", "five.oaInformationPlanDetail");
                    params.put("planId", projectId);
                    break;
                case EdConst.OA_ARCHIVE://????????????????????????
                    result.put("url", "oa.archiveDetail");
                    params.put("id", projectId);
                    break;
                case EdConst.OA_ARCHIVE_APPLY://??????????????????
                    result.put("url", "oa.archiveApplyDetail");
                    params.put("id", projectId);
                    break;
                case EdConst.FIVE_OA_MEETINGROME_APPLY:// ???????????????
                    result.put("url", "five.oaMeetingRoomApplyDetail");
                    params.put("id", projectId);
                    break;
                case EdConst.FIVE_OA_MEETING_ARRANGE:// ????????????
                    result.put("url", "five.oaMeetingArrangeDetail");
                    params.put("id", projectId);
                    break;



                //????????????
                case EdConst.STAMP_OTHER: //??????????????????
                    result.put("url", "ed.detail.stampOtherDetail");
                    params.put("stampOtherId", businessId);
                    params.put("id", projectId);
                    break;
                case EdConst.STAMP_ED://????????????????????????
                    result.put("url", "ed.detail.stampEdDetail");
                    params.put("stampEdId", businessId);
                    params.put("id", projectId);
                    break;
                case EdConst.STAMP_CHANGE_AND_PROCESS://?????????????????????????????????
                    result.put("url", "ed.detail.stampChangeAndProcessDetail");
                    params.put("changeAndProcessId", businessId);
                    params.put("id", projectId);
                    break;
                case EdConst.EXPLORE_STAMP_CHANGE_AND_PROCESS://??????-?????????????????????????????????
                    result.put("url", "explore.detail.stampChangeAndProcessDetail");
                    params.put("changeAndProcessId", businessId);
                    params.put("id", projectId);
                    break;
                case EdConst.EXPLORE_STAMP_OTHER://??????-??????????????????
                    result.put("url", "explore.detail.stampOtherDetail");
                    params.put("stampOtherId", businessId);
                    params.put("id", projectId);
                    break;
                case EdConst.STAMP_EXPLORE://??????-??????????????????
                    result.put("url", "explore.detail.stampExploreDetail");
                    params.put("stampExploreId", businessId);
                    params.put("id", projectId);
                    break;
                case EdConst.STAMP_NO_CONTRACT://????????????????????????
                    result.put("url", "me.stampNoDetail");
                    params.put("stampId", projectId);
                    break;
                //????????????
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
                case EdConst.HR_HONOR://????????????
                    result.put("url", "hr.honorDetail");
                    params.put("honorId", projectId);
                    break;
                case EdConst.HR_EDUCATION://????????????
                    result.put("url", "hr.educationDetail");
                    params.put("educationId", projectId);
                    break;
                case EdConst.HR_INSURE://?????????????????????
                    result.put("url", "hr.insureDetail");
                    params.put("insureId", projectId);
                    break;
                case EdConst.HR_CERTIFICATION://????????????
                    result.put("url", "hr.certificationDetail");
                    params.put("certificationId", projectId);
                    break;
                case EdConst.FIVE_OA_INVENT_APPLY://????????????
                    result.put("url", "five.oaInventApplyDetail");
                    params.put("inventId", projectId);
                    break;
                //????????????
                case EdConst.FIVE_HR_DESIGN_QUALIFY_APPLY://??????????????????????????????
                    result.put("url", "five.hrQualifyApplyDetail");
                    params.put("qualifyApplyId", projectId);
                    break;
                case EdConst.FIVE_HR_APPROVE_APPLY://???????????????????????????
                    result.put("url", "five.hrApproveApplyDetail");
                    params.put("qualifyApplyId", projectId);
                    break;
                case "fiveHrQualifyChief":
                    FiveHrQualifyChief fiveHrQualifyChief=fiveHrQualifyChiefMapper.selectByPrimaryKey(projectId);
                    if(fiveHrQualifyChief.getApplyType().contains("??????")){
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

                //????????????
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
                case EdConst.BUSINESS_CONTRACT://????????????
                    result.put("url", "business.contractDetail");
                    params.put("contractId", projectId);
                    break;
                case EdConst.FIVE_BUSINESS_PRE_CONTRACT://???????????????
                    result.put("url", "five.businessPreContractDetail");
                    params.put("preContractId", projectId);
                    break;
                case EdConst.FIVE_BUSINESS_CUSTOMER://??????????????????
                    result.put("url", "five.businessCustomerDetail");
                    params.put("customerId", projectId);
                    break;
                case EdConst.FIVE_BUSINESS_SUPPLIER://??????????????????
                    result.put("url", "five.businessSupplierDetail");
                    params.put("supplierId", projectId);
                    break;
                case EdConst.FIVE_BUSINESS_INPUT_CONTRACT://????????????
                    result.put("url", "five.businessInputContractDetail");
                    params.put("inputContractId", projectId);
                    break;
                case EdConst.FIVE_BUSINESS_CONTRACT://????????????
                    result.put("url", "business.contractDetail");
                    params.put("contractId", projectId);
                    break;
                case EdConst.FIVE_BUSINESSCONTRACTREVIEW://??????????????????
                    result.put("url", "five.businessContractReviewDetail");
                    params.put("contractReviewId", projectId);
                    break;
                case EdConst.FIVE_BUSINESSCONTRACTREVIEWEASY://??????????????????(???)
                    result.put("url", "five.businessContractReviewDetail");
                    params.put("contractReviewId", projectId);
                    break;
                case EdConst.FIVE_BUSINESS_SUBPACKAGE://????????????
                    result.put("url", "five.businessSubpackageDetail");
                    params.put("subpackageId", projectId);
                    break;
                case EdConst.FIVE_BUSINESS_PURCHASE://????????????
                    result.put("url", "five.businessPurchaseDetail");
                    params.put("purchaseId", projectId);
                    break;
                case EdConst.FIVE_BUSINESS_INCOME://????????????
                    result.put("url", "five.businessIncomeDetail");
                    params.put("incomeId", projectId);
                    break;
                case EdConst.FIVE_BUSINESS_OUTASSIST://??????????????????
                    result.put("url", "five.businessOutAssistDetail");
                    params.put("outAssistId", projectId);
                    break;
                case EdConst.FIVE_BUSINESS_COOPERATION_DELEGATION://????????????????????????
                    result.put("url", "five.businessCooperationDelegationDetail");
                    params.put("cooperationDelegationId", projectId);
                    break;
                case EdConst.FIVE_BUSINESS_COOPERATION_CONTRACT://????????????????????????
                    result.put("url", "five.businessCooperationContractDetail");
                    params.put("cooperationContractId", projectId);
                    break;
                case EdConst.FIVE_BUSINESS_ADVANCE://????????????
                    result.put("url", "five.businessAdvanceDetail");
                    params.put("advanceId", projectId);
                    break;
                case EdConst.FIVE_BUSINESS_ADVANCE_COLLECT://????????????
                    result.put("url", "five.businessAdvanceCollectDetail");
                    params.put("advanceCollectId", projectId);
                    break;
                case EdConst.FIVE_BUSINESS_TENDER_DOCUMENT_REVIEW://????????????????????????????????????
                    result.put("url", "five.businessTenderDocumentReviewDetail");
                    params.put("tenderId", projectId);
                    break;
                case EdConst.FIVE_BUSINESS_STATISTICS://??????????????????
                    result.put("url", "five.businessStatisticsDetail");
                    params.put("statisticsId", projectId);
                    break;
                    //????????????
                case EdConst.FIVE_FINANCE_INCOME://????????????
                    result.put("url", "finance.incomeDetail");
                    params.put("incomeId", projectId);
                    break;
                case EdConst.FIVE_FINANCE_NODE://????????????
                    result.put("url", "finance.nodeDetail");
                    params.put("nodeId", projectId);
                    break;
                case EdConst.FIVE_FINANCE_INCOME_CONFIRM://????????????
                    result.put("url", "finance.incomeConfirmDetail");
                    params.put("incomeConfirmId", projectId);
                    break;
                case EdConst.FIVE_FINANCE_INVOICE://????????????
                    result.put("url", "finance.invoiceDetail");
                    params.put("invoiceId", projectId);
                    break;
                case EdConst.FIVE_FINANCE_INVOICE_CANCEL://????????????
                    result.put("url", "finance.invoiceCancelDetail");
                    params.put("invoiceCancelId", projectId);
                    break;
                case EdConst.FIVE_FINANCE_INVOICE_COLLECTION://?????????????????????
                    result.put("url", "finance.invoiceCollectionDetail");
                    params.put("invoiceCollectionId", projectId);
                    break;
                case EdConst.FIVE_FINANCE_BALANCE://??????????????????
                    result.put("url", "finance.balanceDetail");
                    params.put("balanceId", projectId);
                    break;
                case EdConst.FIVE_FINANCE_STAMPTAX://???????????????
                    result.put("url", "finance.stampTaxDetail");
                    params.put("stampTaxId", projectId);
                    break;
                case EdConst.FIVE_FINANCE_OUTSUPPLY://??????????????????
                    result.put("url", "finance.outSupplyDetail");
                    params.put("outSupplyId", projectId);
                    break;
              /*  case "test"://??????????????????
                    result.put("url", "finance.balanceDetail");
                    params.put("balanceId", projectId);
                    break;*/
                //????????????
                case EdConst.FIVE_OA_SUPERVISE:
                    result.put("url", "five.superviseDetail");
                    params.put("superviseId", projectId);
                    break;
                //????????????????????????
                case EdConst.FIVE_OA_SUPERVISE_YEAR:
                    result.put("url", "five.superviseYearDetail");
                    params.put("superviseId", projectId);
                    break;
                //????????????
                case EdConst.FIVE_OA_SUPERVISE_FILE:
                    result.put("url", "five.superviseFileDetail");
                    params.put("superviseId", projectId);
                    break;
                case EdConst.FIVE_OA_SUPERVISE_DETAIL:
                    result.put("url", "five.superviseDetailChild");
                    params.put("detailId", projectId);
                    break;
                //??????
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
                case EdConst.FIVE_FINANCE_TRANSFER_ACCOUNTS_COMMON://????????????
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
                case EdConst.FIVE_FINANCE_LOAN_COMMON://??????
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
                case EdConst.FIVE_FINANCE_REIMBURSE_COMMON://????????????
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
                case EdConst.FIVE_FINANCE_REFUND://??????
                    result.put("url", "finance.refundDetail");
                    params.put("refundId", projectId);
                    break;

                case EdConst.FIVE_FINANCE_TRAVEL_EXPENSE_COMMON://?????????
                    result.put("url", "finance.travelExpenseDetail");
                    params.put("travelExpenseId", projectId);
                    break;
                case EdConst.FIVE_FINANCE_SUBPACKAGE_PAYMENT://??????????????????
                    result.put("url", "finance.subpackagePaymentDetail");
                    params.put("subpackagePaymentId", projectId);
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
                case EdConst.FIVE_OA_EXTERNAL_RESEARCH_PROJECT_APPLY://????????????????????????
                    result.put("url", "five.oaExternalResearchProjectApplyDetail");
                    params.put("projectId", projectId);
                    break;
                case EdConst.FIVE_OA_EXTERNAL_STANDARD_APPLY://????????????????????????
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

                case EdConst.FIVE_OA_DISPATCH://???????????? 11???
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
        //????????????
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
        //????????????
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
