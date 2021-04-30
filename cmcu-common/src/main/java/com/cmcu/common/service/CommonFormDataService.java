package com.cmcu.common.service;

import com.cmcu.act.dto.ActBpmnModel;
import com.cmcu.act.dto.CustomHistoricTaskInstance;
import com.cmcu.act.dto.CustomProcessInstance;
import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.*;
import com.cmcu.common.dao.CommonEdRoleMapper;
import com.cmcu.common.dao.CommonFormDataMapper;
import com.cmcu.common.dao.CommonFormDetailMapper;
import com.cmcu.common.dao.CommonFormMapper;
import com.cmcu.common.dto.*;
import com.cmcu.common.entity.*;
import com.cmcu.common.util.JsonMapper;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
import com.common.model.JsTreeDto;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommonFormDataService extends BaseService {

    @Resource
    ProcessHandleService processHandleService;

    @Resource
    ProcessQueryService processQueryService;

    @Resource
    ActCacheService actCacheService;

    @Resource
    CommonFormMapper commonFormMapper;

    @Resource
    CommonFormService commonFormService;

    @Resource
    public
    CommonFormDataMapper commonFormDataMapper;

    @Resource
    CommonFormDetailMapper commonFormDetailMapper;

    @Resource
    CommonCodeService commonCodeService;

    @Resource
    CommonUserService commonUserService;


    @Resource
    CommonFormDataBaseService commonFormDataBaseService;

    @Resource
    IHandleFormService handleFormService;

    @Resource
    IEdDataService edDataService;

    @Resource
    TaskService taskService;

    @Resource
    CommonEdRoleMapper commonEdRoleMapper;

    @Resource
    CommonEdArrangeUserService commonEdArrangeUserService;

    @Resource
    TaskHandleService taskHandleService;

    @Resource
    TaskQueryService taskQueryService;

    @Resource
    IUserDataService userDataService;


    public Map listPagedData(int pageNum,int pageSize,String referType,String enLogin,String q,Map params) {
        UserDto userDto = userDataService.selectByEnLogin(enLogin);
        String creator = params.getOrDefault("creator", "").toString();
        String uiSref = params.getOrDefault("uiSref", "").toString();
        int referId = Integer.parseInt(params.getOrDefault("referId", "0").toString());
        Map dataParams = Maps.newHashMap();
        dataParams.put("referType", referType);
        dataParams.put("deleted", false);
        dataParams.put("q", q);

        //查指定创建人
        if (StringUtils.isNotEmpty(creator)) {
            dataParams.put("creator", creator);
        }
        //指定了(关键Id)部门Id
        else if (referId > 0) {
            dataParams.put("referId", referId);
        }
        //未指定功能,则直接查当前用户得
        else if (StringUtils.isEmpty(uiSref)) {
            dataParams.put("creator", enLogin);
        } else {
            //根据权限查询
            List<Integer> deptIdList = userDataService.getMyDeptList(enLogin, uiSref);
            if (deptIdList.size() > 0) {
                dataParams.put("referIdList", deptIdList);
            } else {
                //没有则只查自己的
                dataParams.put("creator", enLogin);
            }
        }
        Map result = Maps.newHashMap();
        CommonForm commonForm = commonFormService.getModel(userDto.getTenetId(), referType, 0);
        boolean haveProcess = StringUtils.isNotEmpty(TplConfigDto.getInstance(commonForm.getOtherTplConfig()).getProcessKey());

        Map detailParams = Maps.newHashMap();
        detailParams.put("deleted", false);
        detailParams.put("formId", commonForm.getId());
        detailParams.put("listHidden", false);

        List<CommonFormDetail> details = commonFormDetailMapper.selectAll(detailParams).stream().sorted(Comparator.comparing(CommonFormDetail::getListSeq)).collect(Collectors.toList());
        result.put("heads", getHeadList(details, haveProcess, userDto.getTenetId()));
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> commonFormDataMapper.selectAll(dataParams));
        List<CommonFormData> commonFormDataList = Lists.newArrayList();
        pageInfo.getList().forEach(p -> commonFormDataList.add((CommonFormData) p));
        List<Object> list = Lists.newArrayList();
        list.addAll(getDataList(commonFormDataList, details, enLogin));
        pageInfo.setList(list);
        result.put("pageInfo", pageInfo);
        result.put("template", commonForm);
        return result;
    }

    public Map listData(String referType,int referId,String q,String enLogin,List<String> forceShowCodeList) {

        Map result = Maps.newHashMap();
        String tenetId=commonUserService.getTenetId(enLogin);
        List<Map> headList = Lists.newArrayList();
        List<Map> dataList = Lists.newArrayList();
        if (StringUtils.isNotEmpty(referType)) {
            Map params = Maps.newHashMap();
            params.put("deleted", false);
            params.put("referType", referType);
            params.put("referId", referId);
            params.put("q",q);
            //todo:  hnz屏蔽使用  临时解决下,如果是用印只取自己的
            /*
            if(referType.equalsIgnoreCase("oaStampApplyOffice")){
                params.put("creator",enLogin);
                params.remove("referId");
            }*/

            CommonForm commonForm = commonFormService.getModel(tenetId, referType,0);
            List<CommonFormData> list = commonFormDataMapper.selectAll(params);

            if (commonForm != null) {
                TplConfigDto tplConfigDto = TplConfigDto.getInstance(commonForm.getOtherTplConfig());
                boolean haveProcess = StringUtils.isNotEmpty(tplConfigDto.getProcessKey());


                Map detailParams = Maps.newHashMap();
                detailParams.put("deleted", false);
                detailParams.put("formId", commonForm.getId());
                List<CommonFormDetail> details = Lists.newArrayList();
                List<CommonFormDetail> oDetailsConfigList = commonFormDetailMapper.selectAll(detailParams).stream()
                        .filter(p -> !p.getListHidden() || forceShowCodeList.contains(p.getInputCode())).collect(Collectors.toList());
                if (referType.startsWith("ed_")) {
                    String projectType = "";
                    if (list.size() > 0) {
                        Map data = JsonMapper.string2Map(list.get(0).getFormData());
                        projectType = data.getOrDefault("projectType", "").toString();
                    }
                    for (CommonFormDetail commonFormDetail : oDetailsConfigList) {
                        Map data = JsonMapper.string2Map(commonFormDetail.getInputConfig());
                        String configProjectType = data.getOrDefault("projectType", "").toString();
                        if (StringUtils.isEmpty(configProjectType) || projectType.equalsIgnoreCase(configProjectType)) {
                            details.add(commonFormDetail);
                        }
                    }
                } else {
                    details.addAll(oDetailsConfigList);
                }
                details = details.stream().sorted(Comparator.comparing(CommonFormDetail::getListSeq)).collect(Collectors.toList());
                headList=getHeadList(details,haveProcess,tenetId);
                dataList=getDataList(list,details,enLogin);
            }
            result.put("template", commonForm);
        }
        result.put("heads", headList);
        result.put("list", dataList);
        return result;
    }

    /**
     * 得到list表头
     * @param details
     * @param haveProcess
     * @param tenetId
     * @return
     */
    private List<Map> getHeadList(List<CommonFormDetail> details,boolean haveProcess,String tenetId) {
        List<Map> headList = Lists.newArrayList();
        boolean allCustomWidth = true;
        for (CommonFormDetail headDetail : details) {
            Map head = Maps.newHashMap();
            head.put("text", headDetail.getInputText());
            InputConfigDto inputConfigDto = InputConfigDto.getInstance(headDetail.getInputConfig());
            head.put("style", inputConfigDto.getHeadStyle());
            if (!inputConfigDto.getHeadStyle().containsKey("width")) {
                allCustomWidth = false;
            }
            headList.add(head);
        }


        Map head = Maps.newHashMap();
        head.put("text", "流程状态");
        Map headStyle = Maps.newHashMap();
        if (!allCustomWidth) {
            if (haveProcess) {
                headStyle.put("width", "185px");
            } else {
                headStyle.put("width", "120px");
            }
        }
        head.put("style", headStyle);
        headList.add(head);
        return headList;
    }

    /**
     * 得到list数据列表
     * @param list
     * @param details
     * @return
     */
    private List<Map> getDataList(List<CommonFormData> list,List<CommonFormDetail> details,String enLogin) {

        List<Map> dataList = Lists.newArrayList();

        for (CommonFormData commonFormData : list) {
            Map data = JsonMapper.string2Map(commonFormData.getFormData());
            Map item = Maps.newHashMap();
            List<Map> propertyList = Lists.newArrayList();
            for (CommonFormDetail detailConfig : details) {
                InputConfigDto inputConfigDto = InputConfigDto.getInstance(detailConfig.getInputConfig());
                Map property = Maps.newHashMap();
                property.put("head",detailConfig.getInputText());
                property.put("style", inputConfigDto.getContentStyle());
                String text=data.getOrDefault(detailConfig.getInputCode(), "").toString();
                if(detailConfig.getInputText().equalsIgnoreCase("创建时间")&&StringUtils.isNotEmpty(text)){
                    try{
                        Date date= DateUtils.parseDate(text,"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd");
                        text=DateFormatUtils.format(date,"yyyy-MM-dd");
                    }catch (Exception ed){

                    }
                }
                if (StringUtils.isNotEmpty(inputConfigDto.getDateFormat())) {
                    try {
                        Date date = DateUtils.parseDate(text, "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm");
                        text=DateFormatUtils.format(date, inputConfigDto.getDateFormat());
                    } catch (Exception dateFormatEx) {

                    }
                }


                property.put("text", text);

                propertyList.add(property);
            }

            CustomSimpleProcessInstance customSimpleProcessInstance =null;
            if(StringUtils.isNotEmpty(commonFormData.getProcessInstanceId())&&!commonFormData.getProcessEnd()) {
                 customSimpleProcessInstance =processQueryService.getCustomSimpleProcessInstance(commonFormData.getProcessInstanceId(), "", enLogin);
                if(customSimpleProcessInstance==null||customSimpleProcessInstance.isFinished()){
                    commonFormData.setProcessEnd(true);
                    commonFormDataMapper.updateByPrimaryKey(commonFormData);
                }
            }

            Map property = Maps.newHashMap();
            Map style = Maps.newHashMap();
            property.put("head","当前状态");
            property.put("style", style);
            style.put("font-weight", "bold");
            if (commonFormData.getProcessEnd()) {
                property.put("text", "已完成");
                style.put("color", "green");
            } else {
                if (customSimpleProcessInstance!=null) {
                    property.put("text", customSimpleProcessInstance.getCurrentTaskName().length()>13?customSimpleProcessInstance.getCurrentTaskName().substring(0,12)+"...":customSimpleProcessInstance.getCurrentTaskName());
                    if (customSimpleProcessInstance.getMyRunningTaskNameList().size() > 0) {
                        style.put("color", "blue");
                    }
                    item.put("processInstance", customSimpleProcessInstance);
                } else {
                    style.put("color", "blue");
                    property.put("text", "填写中");
                }
            }
            propertyList.add(property);
            item.put("color", style.get("color"));
            item.put("id", commonFormData.getId());
            item.put("processInstanceId", commonFormData.getProcessInstanceId());
            item.put("processEnd", commonFormData.getProcessEnd());
            item.put("removeAble", commonFormData.getCreator().equalsIgnoreCase(enLogin)&&(StringUtils.isEmpty(commonFormData.getProcessInstanceId()) || !commonFormData.getProcessEnd()));
            item.put("propertyList", propertyList);
            item.put("businessKey", commonFormData.getBusinessKey());

            StringBuilder cadTaskName = new StringBuilder();
            cadTaskName.append(commonFormData.getCreatorName());
            cadTaskName.append("-");
            cadTaskName.append(DateFormatUtils.format(commonFormData.getGmtCreate(), "MMdd"));
            cadTaskName.append("-");
            for (int i = 0; i < Math.min(propertyList.size(), 2); i++) {
                String headText = propertyList.get(i).get("text").toString();
                if (!headText.equalsIgnoreCase("填表人") && !headText.equalsIgnoreCase("填表时间") && !headText.equalsIgnoreCase("创建人") && !headText.equalsIgnoreCase("创建时间")) {
                    cadTaskName.append(propertyList.get(i).get("text"));
                    cadTaskName.append("-");
                }
            }
            item.put("cadTaskName", cadTaskName.toString().endsWith("-") ? cadTaskName.toString().substring(0, cadTaskName.toString().length() - 1) : cadTaskName.toString());

            StringBuilder cadTaskTooltip = new StringBuilder();
            for (int i = 2; i < propertyList.size(); i++) {
                cadTaskTooltip.append(propertyList.get(i).get("text"));
                cadTaskTooltip.append("-");
            }
            item.put("cadTaskTooltip", cadTaskTooltip.toString().endsWith("-") ? cadTaskTooltip.toString().substring(0, cadTaskTooltip.toString().length() - 1) : cadTaskTooltip.toString());
            item.put("dirPath", commonFormData.getCreatorName() + "-" + DateFormatUtils.format(commonFormData.getGmtCreate(), "yyyyMMdd") + "-" + commonFormData.getId());
            dataList.add(item);
        }
        return dataList;
    }
    /**
     * 得到任务简述
     * @param businessKey
     * @return
     */
    public String getProcessDescription(String businessKey) {
        StringBuilder taskDescription = new StringBuilder();
        try {
            Map params = Maps.newHashMap();
            params.put("businessKey", businessKey);
            params.put("deleted", false);
            if (PageHelper.count(() -> commonFormDataMapper.selectAll(params)) > 0) {
                CommonFormData commonFormData = commonFormDataMapper.selectAll(params).get(0);
                Map data = JsonMapper.string2Map(commonFormData.getFormData());

                List<String> ignoreHeads = Arrays.asList("填表人", "填表时间", "创建人", "创建时间","修改时间");
                CommonForm commonForm = commonFormService.getModel(commonFormData.getTenetId(), commonFormData.getReferType(), commonFormData.getFormVersion());
                if (commonForm != null) {
                    List<CommonFormDetail> details = commonFormService.listDetail(commonForm.getId()).stream()
                            .filter(p -> !p.getListHidden())
                            .filter(p->!ignoreHeads.contains(p.getInputText()))
                            .sorted(Comparator.comparing(CommonFormDetail::getListSeq)).collect(Collectors.toList());

                    //强制加上项目名称作为显示
                    if(details.stream().noneMatch(p->p.getInputCode().equalsIgnoreCase("projectName"))) {
                        String projectName = data.getOrDefault("projectName", "").toString();
                        if (StringUtils.isNotEmpty(projectName)) {
                            taskDescription.append("项目名称");
                            taskDescription.append("(");
                            taskDescription.append(projectName);
                            taskDescription.append(") ");
                        }
                    }

                    for (int i = 0; i <Math.min(details.size(),5); i++) {
                        String dataValue = data.getOrDefault(details.get(i).getInputCode(), "").toString();
                        if (StringUtils.isNotEmpty(dataValue)) {
                            taskDescription.append(details.get(i).getInputText());
                            taskDescription.append("(");
                            taskDescription.append(dataValue);
                            taskDescription.append(") ");
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String result = taskDescription.toString();
        if (result.endsWith("-")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    /**
     * 得到任务简述
     * @param businessKey
     * @return
     */
    public String getFormDescription(String businessKey) {
        Map map = getFormKeyMap(businessKey);
        List<String> list = Lists.newArrayList();
        for (Object key : map.keySet()) {
            StringBuilder sb = new StringBuilder();
            sb.append(key.toString());
            sb.append(":");
            sb.append(map.get(key).toString());
            list.add(sb.toString());
        }
        return StringUtils.join(list, ",");
    }

    public Map getFormKeyMap(String businessKey) {
        Map params = Maps.newHashMap();
        params.put("businessKey", businessKey);
        params.put("deleted", false);
        if (PageHelper.count(() -> commonFormDataMapper.selectAll(params)) > 0) {
            CommonFormData commonFormData = commonFormDataMapper.selectAll(params).get(0);
            return getFormKeyMap(commonFormData);
        }
        return Maps.newHashMap();
    }

    private Map getFormKeyMap(CommonFormData commonFormData) {
        Map map = Maps.newLinkedHashMap();
        try {
            Map data = JsonMapper.string2Map(commonFormData.getFormData());
            List<String> ignoreHeads = Arrays.asList("填表人", "填表时间", "创建人", "创建时间");
            CommonForm commonForm = commonFormService.getModel(commonFormData.getTenetId(), commonFormData.getReferType(), commonFormData.getFormVersion());
            if (commonForm != null) {
                List<CommonFormDetail> details = commonFormService.listDetail(commonForm.getId()).stream()
                        .filter(p -> !p.getListHidden())
                        .filter(p -> !ignoreHeads.contains(p.getInputText()))
                        .sorted(Comparator.comparing(CommonFormDetail::getListSeq)).collect(Collectors.toList());


                for (int i = 0; i < details.size(); i++) {
                    String dataValue = data.getOrDefault(details.get(i).getInputCode(), "").toString();
                    if (StringUtils.isNotEmpty(dataValue)) {
                        map.put(details.get(i).getInputText(), dataValue);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    /**
     * 是否开启意见标注
     * @param businessKey
     * @return
     */
    public boolean checkConfigMarkRole(String businessKey){
        CommonFormData commonFormData=commonFormDataMapper.selectByBusinessKey(businessKey);
        if(commonFormData!=null) {
            CommonForm commonForm = commonFormService.getModel(commonFormData.getTenetId(), commonFormData.getReferType(), commonFormData.getFormVersion());
            if (commonForm != null) {
                TplConfigDto tplConfigDto = TplConfigDto.getInstance(commonForm.getOtherTplConfig());
                return tplConfigDto.getMarkRoleNames().size() > 0;
            }
        }
        return false;
    }

    public Map listData(String referType,int referId,String q,String enLogin) {
        return listData(referType,referId,q,enLogin,Lists.newArrayList());
    }

    public Map getModelById(int id, String enLogin) {
        Map result = Maps.newHashMap();
        CommonFormData commonFormData = commonFormDataMapper.selectByPrimaryKey(id);
        result.put("data", commonFormData);
        result.put("formData", JsonMapper.string2Map(commonFormData.getFormData()));
        List<InputGroupDto> groupList = listFormProperty(commonFormData, enLogin);
        result.put("groupList", groupList);
        result.put("template", commonFormService.getModel(commonFormData.getTenetId(), commonFormData.getFormKey(), commonFormData.getFormVersion()));
        //reduce transfer data
        commonFormData.setFormData("");
        return result;
    }

    public Map getFormDataById(int id) {
        CommonFormData commonFormData = commonFormDataMapper.selectByPrimaryKey(id);
        return JsonMapper.string2Map(commonFormData.getFormData());
    }

    public Map getDataByBusinessKey(String businessKey, String enLogin) {
        CommonFormData commonFormData = getModelByBusinessKey(businessKey);
        if (commonFormData != null) {
            return getModelById(commonFormData.getId(), enLogin);
        }
        return Maps.newHashMap();
    }

    public CommonFormData getModelByBusinessKey(String businessKey){
        Map params= Maps.newHashMap();
        params.put("businessKey",businessKey);
        if(PageHelper.count(()->commonFormDataMapper.selectAll(params))==0) return null;
        CommonFormData commonFormData = commonFormDataMapper.selectAll(params).get(0);
        return commonFormData;
    }

    public List<CommonFormData> listDataByReferType(String tenetId,String referType,int referId) {
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("tenetId", tenetId);
        params.put("referType", referType);
        params.put("referId", referId);
        return commonFormDataMapper.selectAll(params).stream().sorted(Comparator.comparing(CommonFormData::getId).reversed()).collect(Collectors.toList());
    }

    @Transactional
    public CommonFormData getNewModel(String referType,int referId, String enLogin) {
        Assert.state(StringUtils.isNotEmpty(referType), "表单标识不能为空!");
        Assert.state(StringUtils.isNotEmpty(enLogin), "用户标识不能为空!");
        Assert.state(referId > 0, "表单关联标识不能为空!");
        UserDto userDto = commonUserService.selectByEnLogin(enLogin);
        CommonForm commonForm = commonFormService.getModel(userDto.getTenetId(), referType, 0);
        Assert.state(commonForm != null, "未发现表单:" + referType);
        List<CommonFormDetail> details = commonFormService.listDetail(commonForm.getId());
        //可能的变量集合
        Map allVariables = getUserBasicVariable(enLogin);
        if (referType.startsWith("ed_") || referType.toLowerCase().contains("ed")) {
            allVariables.putAll(getEdData(referType, referId, userDto, details.stream().filter(p -> StringUtils.isNotEmpty(p.getDataSource()) && p.getDataSource().startsWith("arrange_")).collect(Collectors.toList())));
        }
        return getNewModel(referId, commonForm, allVariables, userDto, false);
    }

    public CommonFormData getNewModel(int referId,CommonForm commonForm,Map allVariables,UserDto userDto,boolean forceNoProcess) {
        List<CommonFormDetail> details = commonFormService.listDetail(commonForm.getId());
        LinkedHashMap data = Maps.newLinkedHashMap();
        details.forEach(p -> {
            try {
                InputConfigDto inputConfigDto = InputConfigDto.getInstance(p.getInputConfig());
                if(inputConfigDto.isAddDefaultValue()) {
                    Object dataValue = allVariables.getOrDefault(p.getInputCode(), "");
                    if (p.getInputType().equalsIgnoreCase("number") && StringUtils.isNotEmpty(dataValue.toString())) {
                        data.put(p.getInputCode(), Double.parseDouble(dataValue.toString()));
                    } else {
                        data.put(p.getInputCode(), dataValue);
                    }
                    if (p.getInputType().equalsIgnoreCase("select") && !p.getMultiple() && StringUtils.isEmpty(dataValue.toString())) {
                        if (StringUtils.isNotEmpty(p.getDataSource())) {
                            if (p.getDataSource().startsWith("code_")) {
                                String catalog = StringUtils.split(p.getDataSource(), "_")[1];
                                data.put(p.getInputCode(), commonCodeService.selectDefaultCodeValue(userDto.getTenetId(), catalog));
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                System.out.println("转换失败：" + p.getInputCode());
            }
        });
        CommonFormData item = new CommonFormData();
        item.setTenetId(userDto.getTenetId());
        item.setReferType(commonForm.getFormKey());
        item.setFormData(JsonMapper.obj2String(data));
        item.setDeptId(userDto.getDeptId());
        item.setDeptName(userDto.getDeptName());
        item.setFormKey(commonForm.getFormKey());
        item.setFormVersion(commonForm.getFormVersion());
        item.setReferId(referId);
        item.setCreator(userDto.getEnLogin());
        item.setCreatorName(userDto.getCnName());
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setSeq(1);
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        ModelUtil.setNotNullFields(item);
        commonFormDataMapper.insert(item);
        String businessKey = item.getFormKey() + "_" + item.getId();
        TplConfigDto tplConfigDto = TplConfigDto.getInstance(commonForm.getOtherTplConfig());
        if (StringUtils.isNotEmpty(tplConfigDto.getProcessKey()) && !forceNoProcess) {
            Map variables = Maps.newHashMap();
            variables.put("initiator", userDto.getEnLogin());
            String processInstanceId = taskHandleService.startProcess(tplConfigDto.getProcessKey(), businessKey, variables, userDto.getTenetId());
            item.setProcessInstanceId(processInstanceId);
            updateProcessVariables(item.getProcessInstanceId(), data);
        }
        item.setBusinessKey(businessKey);
        commonFormDataMapper.updateByPrimaryKey(item);
        return item;
    }

    private Map getEdData(String referType,int referId,UserDto userDto, List<CommonFormDetail> details) {
        Map result = edDataService.getNewEdData(referType, referId, userDto.getEnLogin());

        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("tenetId", userDto.getTenetId());
        List<String> roleList = commonEdRoleMapper.selectAll(params).stream().map(CommonEdRole::getCnName).distinct().collect(Collectors.toList());
        result.put("roleList", roleList);
        if (details.size() > 0) {
            CommonEdArrangeDto commonEdArrangeDto = commonEdArrangeUserService.getCommonEdArrangeDto(referId, userDto.getEnLogin());
            if (details.stream().anyMatch(p -> p.getDataSource().equalsIgnoreCase("arrange_myBuildName"))) {
                CommonFormDetail detail = details.stream().filter(p -> p.getDataSource().equalsIgnoreCase("arrange_myBuildName")).findFirst().get();
                if (detail.getMultiple()) {
                    result.put(detail.getInputCode(), commonEdArrangeDto.getMyBuildNameList());
                } else if (commonEdArrangeDto.getMyBuildNameList().size() > 0) {
                    result.put(detail.getInputCode(), commonEdArrangeDto.getMyBuildNameList().get(0));
                }
            }

            String myMajorName = "";
            if (details.stream().anyMatch(p -> p.getDataSource().equalsIgnoreCase("arrange_myMajorName"))) {
                CommonFormDetail detail = details.stream().filter(p -> p.getDataSource().equalsIgnoreCase("arrange_myMajorName")).findFirst().get();
                if (detail.getMultiple()) {
                    result.put(detail.getInputCode(), commonEdArrangeDto.getMyMajorNameList());
                } else if (commonEdArrangeDto.getMyBuildNameList().size() > 0) {
                    myMajorName = commonEdArrangeDto.getMyMajorNameList().get(0);
                    result.put(detail.getInputCode(), myMajorName);
                }
            }

            String myBuildName = "";
            if (details.stream().anyMatch(p -> p.getDataSource().equalsIgnoreCase("arrange_myBuildName"))) {
                CommonFormDetail detail = details.stream().filter(p -> p.getDataSource().equalsIgnoreCase("arrange_myBuildName")).findFirst().get();
                if (detail.getMultiple()) {
                    result.put(detail.getInputCode(), commonEdArrangeDto.getMyBuildNameList());
                } else if (commonEdArrangeDto.getMyBuildNameList().size() > 0) {
                    myBuildName = commonEdArrangeDto.getMyBuildNameList().get(0);
                    result.put(detail.getInputCode(), myBuildName);
                }
            }


            List<String> roleCodeList = commonEdArrangeDto.getUserList().stream()
                    .map(CommonEdArrangeUser::getRoleCode).distinct().collect(Collectors.toList());

            for (CommonFormDetail detail : details) {
                //包含这个roleCode
                if (roleCodeList.stream().anyMatch(p -> detail.getDataSource().toLowerCase().contains(p))) {
                    String roleCode = roleCodeList.stream().filter(p -> detail.getDataSource().toLowerCase().contains(p)).findFirst().get();

                    List<CommonEdArrangeUserDto> users = commonEdArrangeDto.getUserList().stream()
                            .filter(p -> p.getRoleCode().equalsIgnoreCase(roleCode)).collect(Collectors.toList());
                    if (!detail.getDataSource().contains("_all")) {
                        if (StringUtils.isNotEmpty(myMajorName)) {
                            String majorName = myMajorName;
                            users = users.stream().filter(p -> p.getMajorName().equalsIgnoreCase(majorName)).collect(Collectors.toList());
                        } else {
                            users = users.stream().filter(p -> commonEdArrangeDto.getMyMajorNameList().contains(p.getMajorName())).collect(Collectors.toList());
                        }
                        if (StringUtils.isNotEmpty(myBuildName)) {
                            String buildName = myBuildName;
                            users = users.stream().filter(p -> p.getBuildName().equalsIgnoreCase(buildName)).collect(Collectors.toList());
                        } else {
                            users = users.stream().filter(p -> commonEdArrangeDto.getMyBuildNameList().contains(p.getBuildName())).collect(Collectors.toList());
                        }
                    }
                    List<String> enLoginList = MyStringUtil.getStringList(StringUtils.join(users.stream().map(CommonEdArrangeUser::getAllUser).collect(Collectors.toList()), ","));
                    List<String> cnNameList = MyStringUtil.getStringList(StringUtils.join(users.stream().map(CommonEdArrangeUser::getAllUser).collect(Collectors.toList()), ","));
                    if (detail.getMultiple()) {
                        result.put(roleCode + "Men", enLoginList);
                        result.put(roleCode + "Name", StringUtils.join(cnNameList, ","));
                    } else if (enLoginList.size() > 0) {
                        result.put(roleCode + "Man", enLoginList.get(0));
                        result.put(roleCode + "ManName", commonUserService.getCnNames(enLoginList.get(0)));
                    }
                }
            }
        }
        return result;
    }

    /**
     * 保存数据
     * @param id
     * @param currentData
     * @param enLogin
     */
    public void save22(int id,Map currentData,String enLogin) {

        boolean autoSubmit = (boolean) currentData.getOrDefault("autoSubmit", false);
        currentData.put("gmtModified", new Date());
        CommonFormData commonFormData = commonFormDataMapper.selectByPrimaryKey(id);

        boolean update = false;
        Map preData = JsonMapper.string2Map(commonFormData.getFormData());
        List<CommonFormDetail> list = commonFormDataBaseService.listEditableDetail(commonFormData, enLogin);
        for (CommonFormDetail detail : list) {
            Object value = currentData.getOrDefault(detail.getInputCode(), "");
            if (detail.getRequired()) {
                Assert.state(!autoSubmit || StringUtils.isNotEmpty(value.toString()), detail.getInputText() + "不能为空!");
            }
            if (!value.toString().equals(preData.getOrDefault(detail.getInputCode(), "").toString())) {
                update = true;
                Assert.state(value.toString().length() <= detail.getMaxLength(), detail.getInputText() + "数据长度应小于" + detail.getMaxLength() + "!");
                preData.put(detail.getInputCode(), value);
            }
        }

        if (!autoSubmit) {
            Assert.state(update, "未发现任何数据变化!");
        }

        if (update) {
            commonFormData.setFormData(JsonMapper.obj2String(preData));
            commonFormData.setGmtModified(new Date());
            commonFormDataMapper.updateByPrimaryKey(commonFormData);
            preData = handleFormService.afterSaveData(commonFormData, preData);
            updateProcessVariables(commonFormData.getProcessInstanceId(), preData);
        }
    }

    /**
     * 保存数据
     * @param id
     * @param currentData
     * @param enLogin
     */
    public void save(int id,Map currentData,String enLogin) {

        boolean autoSubmit = (boolean) currentData.getOrDefault("autoSubmit", false);
        currentData.put("gmtModified", new Date());
        CommonFormData commonFormData = commonFormDataMapper.selectByPrimaryKey(id);

        boolean update = false;
        Map preData = JsonMapper.string2Map(commonFormData.getFormData());
        List<CommonFormDetail> list = commonFormDataBaseService.listEditableDetail(commonFormData, enLogin);
        for (CommonFormDetail detail : list) {
            Object value = currentData.getOrDefault(detail.getInputCode(), "");
            if (!value.toString().equals(preData.getOrDefault(detail.getInputCode(), "").toString())) {
                Assert.state(value.toString().length() <= detail.getMaxLength(), detail.getInputText() + "数据长度应小于" + detail.getMaxLength() + "!");
                update = true;
                preData.put(detail.getInputCode(), value);
            }
        }



        if(update) {
            commonFormData.setFormData(JsonMapper.obj2String(preData));
            commonFormData.setGmtModified(new Date());
            commonFormDataMapper.updateByPrimaryKey(commonFormData);
            preData = handleFormService.afterSaveData(commonFormData, preData);
            updateProcessVariables(commonFormData.getProcessInstanceId(), preData);
        }

        if (autoSubmit) {
            for (CommonFormDetail detail : list) {
                Object value = currentData.getOrDefault(detail.getInputCode(), "");
                if (detail.getRequired()) {
                    Assert.state(StringUtils.isNotEmpty(value.toString()), detail.getInputText() + "不能为空!");
                }
            }
        }else{
            Assert.state(update, "未发现任何数据变化!");
        }
    }

    public void checkFormData(String businessKey,String enLogin) {
        Map params = Maps.newHashMap();
        params.put("businessKey", businessKey);
        params.put("deleted", false);

        if(businessKey.startsWith("fiveEdTask")){
            handleFormService.checkFormData(businessKey, enLogin);
        }
        else if (PageHelper.count(() -> commonFormDataMapper.selectAll(params)) > 0) {
            CommonFormData commonFormData = commonFormDataMapper.selectAll(params).get(0);
            Map currentData = JsonMapper.string2Map(commonFormData.getFormData());
            List<CommonFormDetail> list = commonFormDataBaseService.listEditableDetail(commonFormData, enLogin);
            for (CommonFormDetail detail : list) {
                Object value = currentData.getOrDefault(detail.getInputCode(), "");
                if (detail.getRequired()) {
                    Assert.state(StringUtils.isNotEmpty(value.toString()), detail.getInputText() + "不能为空!");
                }
                Assert.state(value.toString().length() <= detail.getMaxLength(), detail.getInputText() + "数据长度应小于" + detail.getMaxLength() + "!");
            }
        } else {
            handleFormService.checkFormData(businessKey, enLogin);
        }
    }

    @Transactional
    public void remove(int id,String enLogin){
        CommonFormData commonFormData=commonFormDataMapper.selectByPrimaryKey(id);
        Assert.state(commonFormData.getCreator().equalsIgnoreCase(enLogin),"您不是发起人"+commonFormData.getCreatorName()+"");
        Assert.state(!commonFormData.getProcessEnd(),"已完成的流程无法删除!");
        if(StringUtils.isNotEmpty(commonFormData.getProcessInstanceId())) {
            processHandleService.deleteProcessInstanceById(commonFormData.getProcessInstanceId(), enLogin, "delete process");
        }
        commonFormData.setDeleted(true);
        commonFormData.setGmtModified(new Date());
        commonFormDataMapper.updateByPrimaryKey(commonFormData);
        handleFormService.deleteFormData(commonFormData.getBusinessKey(),enLogin);
    }

    public List<InputGroupDto> listFormProperty(CommonFormData commonFormData,String enLogin) {
        List<InputGroupDto> list = Lists.newArrayList();

        String tenetId = commonFormData.getTenetId();
        Map params = Maps.newHashMap();
        params.put("formKey", commonFormData.getFormKey());
        params.put("formVersion", commonFormData.getFormVersion());
        params.put("tenetId", tenetId);
        if (PageHelper.count(() -> commonFormMapper.selectAll(params)) > 0) {
            CommonForm commonForm = commonFormService.getModel(commonFormData.getTenetId(), commonFormData.getFormKey(), commonFormData.getFormVersion());

            List<CommonFormDetail> commonFormDetails=Lists.newArrayList();

            List<CommonFormDetail> allDetails = commonFormService.listDetail(commonForm.getId());
            if(allDetails.stream().anyMatch(p->StringUtils.isNotEmpty(p.getShowTag()))) {
                List<CustomHistoricTaskInstance> taskInstances = taskQueryService.listHistoricTaskInstance(commonFormData.getProcessInstanceId());
                for (CommonFormDetail detail : allDetails) {
                    List<String> showTags=MyStringUtil.getStringList(detail.getShowTag());
                    if(showTags.size()>0) {
                        List<String> attends = taskInstances.stream().filter(p -> showTags.stream().anyMatch(o ->p.getName().contains(o))).map(CustomHistoricTaskInstance::getAssignee).collect(Collectors.toList());
                        if (attends.contains(enLogin)) {
                            commonFormDetails.add(detail);
                        }
                    }else{
                        commonFormDetails.add(detail);
                    }
                }
            }else{
                commonFormDetails.addAll(allDetails);
            }

            List<CommonFormDetail> editableDetails = commonFormDataBaseService.listEditableDetail(commonFormData, enLogin);
            CommonEdArrangeDto commonEdArrangeDto=new CommonEdArrangeDto();
            if(commonFormDetails.stream().filter(p->StringUtils.isNotEmpty(p.getDataSource())).anyMatch(p->p.getDataSource().startsWith("arrange_"))){
                commonEdArrangeDto=commonEdArrangeUserService.getCommonEdArrangeDto(commonFormData.getReferId(),enLogin);
            }

            Map data = JsonMapper.string2Map(commonFormData.getFormData());
            List<String> groupNames = commonFormDetails.stream().map(CommonFormDetail::getGroupName).distinct().collect(Collectors.toList());
            for (String groupName : groupNames) {
                InputGroupDto group = new InputGroupDto();
                group.setFormDataId(commonFormData.getId());
                group.setGroupName(groupName);
                group.setItems(Lists.newArrayList());

                for (CommonFormDetail detail : commonFormDetails.stream()
                        .filter(p -> p.getGroupName().equalsIgnoreCase(groupName))
                        .sorted(Comparator.comparing(CommonFormDetail::getDetailSeq))
                        .collect(Collectors.toList())) {
                    InputItemDto item = new InputItemDto();
                    detail.setEditable(editableDetails.stream().anyMatch(p -> p.getId().equals(detail.getId())));

                    InputConfigDto inputConfigDto = InputConfigDto.getInstance(detail.getInputConfig());
                    item.setCommonFormDetail(detail);
                    item.setInputConfigDto(inputConfigDto);

                    String dataValue=data.getOrDefault(detail.getInputCode(), "").toString();
                    try {
                        if(StringUtils.isNotEmpty(dataValue)) {
                            if (inputConfigDto.getMultipleTimes() > 0&&inputConfigDto.getMultipleTimes()!=1) {
                                BigDecimal bigDecimalValue = new BigDecimal(dataValue).multiply(new BigDecimal(inputConfigDto.getMultipleTimes()));
                                dataValue = bigDecimalValue.toString();
                            }
                        }
                        if (inputConfigDto.getDotSize() >= 0) {
                            if (dataValue.contains(".")) dataValue = dataValue + "00000000";
                            if (!dataValue.contains(".")) dataValue = dataValue + ".00000000";
                            StringBuilder sb = new StringBuilder();
                            sb.append(StringUtils.split(dataValue, ".")[0]);
                            sb.append(".");
                            if (StringUtils.split(dataValue, ".")[1].length() > inputConfigDto.getDotSize()) {
                                sb.append(StringUtils.split(dataValue, ".")[1], 0, inputConfigDto.getDotSize());
                                dataValue = sb.toString();
                            }
                        }
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                    item.setInputValue(dataValue);
                    if(StringUtils.isNotEmpty(dataValue)&&dataValue.startsWith("[")&&dataValue.endsWith("]")){
                        item.setInputValue(MyStringUtil.getStringList(dataValue.replace("[","").replace("]","")));
                    }
                    item.setDataSource(Lists.newArrayList());
                    if (StringUtils.isNotEmpty(detail.getDataSource())) {
                        List<CommonCodeDto> dataSources = listDataSource(commonFormData, commonEdArrangeDto, detail.getDataSource(), enLogin);
                        if (StringUtils.isNotEmpty(item.getInputValue().toString())) {
                            if (dataSources.size() > 0) {
                                CommonCode first = dataSources.get(0);
                                if ("Boolean".equalsIgnoreCase(first.getCodeType())) {
                                    item.setInputValue(Boolean.parseBoolean(item.getInputValue().toString()));
                                } else if ("Integer".equalsIgnoreCase(first.getCodeType())) {
                                    item.setInputValue(Integer.parseInt(item.getInputValue().toString()));
                                } else if ("BigDecimal".equalsIgnoreCase(first.getCodeType())) {
                                    item.setInputValue(new BigDecimal(item.getInputValue().toString()));
                                }
                            }
                        }

                        if (detail.getInputType().equalsIgnoreCase("tree")) {
                            List<JsTreeDto> treeList = Lists.newArrayList();
                            for (CommonCodeDto cc : dataSources) {
                                Optional<CommonCodeDto> parentCode = dataSources.stream().filter(p -> p.getId().equals(cc.getParentId())).findFirst();
                                String parent = "#";
                                if (parentCode.isPresent()) {
                                    parent = parentCode.get().getId().toString();
                                }
                                JsTreeDto tree = new JsTreeDto(cc.getId(), cc.getName(), parent);
                                tree.setData(cc);
                                treeList.add(tree);
                            }
                            item.setDataSource(treeList);
                        } else {
                            item.setDataSource(dataSources);
                        }
                    }

                    if(!detail.getEditable()) detail.setDisabled(true);
                    group.getItems().add(item);
                }

                if(group.getItems().size()>0) list.add(group);
            }
        }
        return list;
    }

    public List<CommonCodeDto> listDataSource(CommonFormData commonFormData,CommonEdArrangeDto commonEdArrangeDto,String dataSource,String enLogin) {
        List<CommonCodeDto> dataSources = Lists.newArrayList();
        String tenetId = commonFormData.getTenetId();
        if (StringUtils.isNotEmpty(dataSource)) {
            if (dataSource.startsWith("code_")) {
                String catalog = StringUtils.split(dataSource, "_")[1];
                dataSources = commonCodeService.listDataByCatalog(tenetId, catalog);
                if (dataSource.endsWith("_名称")) {
                    dataSources.forEach(p -> {
                        p.setCode(p.getName());
                        p.setCodeValue(p.getName());
                    });
                }
            }else if(dataSource.startsWith("arrange_")){
                if(dataSource.equalsIgnoreCase("arrange_majorName")){
                    for(String majorName :  commonEdArrangeDto.getMajorNameList()) {
                        dataSources.add(new CommonCodeDto(majorName, majorName));
                    }
                }
                else if(dataSource.equalsIgnoreCase("arrange_myMajorName")){
                    for(String majorName :  commonEdArrangeDto.getMyMajorNameList()) {
                        dataSources.add(new CommonCodeDto(majorName, majorName));
                    }
                } else if(dataSource.equalsIgnoreCase("arrange_buildName")){
                    for(String buildName :  commonEdArrangeDto.getBuildNameList()) {
                        dataSources.add(new CommonCodeDto(buildName, buildName));
                    }
                } else if(dataSource.equalsIgnoreCase("arrange_myBuildName")){
                    for(String buildName :  commonEdArrangeDto.getMyBuildNameList()) {
                        dataSources.add(new CommonCodeDto(buildName, buildName));
                    }
                }
            }
            else {
                dataSources = handleFormService.listDataSource(commonFormData, dataSource, enLogin);
            }

        }
        return dataSources;
    }


    /**
     * 更新流程变量
     * @param processInstanceId
     * @param formData
     */
    public  void updateProcessVariables(String processInstanceId,Map formData) {
        if (StringUtils.isNotEmpty(processInstanceId)&&taskService.createTaskQuery().processInstanceId(processInstanceId).count() > 0) {
            Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).list().get(0);
            Map variables = Maps.newHashMap();
            CustomProcessInstance customProcessInstance = processQueryService.getCustomProcessInstance(processInstanceId, "", "");
            if (customProcessInstance != null && customProcessInstance.getInstance() != null) {
                String description = getFormDescription(customProcessInstance.getInstance().getBusinessKey());
                variables.put(TaskQueryService.PROCESS_DESCRIPTION, description);
            }
            ActBpmnModel actBpmnModel = actCacheService.getActBpmnModel(task.getProcessDefinitionId());
            List<String> processVariables = actBpmnModel.getVariables();
            for (String variable : processVariables) {
                if (formData.containsKey(variable)) {
                    Object value = formData.getOrDefault(variable, "");
                    if (value.toString().startsWith("[")) {
                        variables.put(variable, MyStringUtil.getStringList(value.toString().substring(1, value.toString().length() - 1)));
                    } else if (variable.contains("Men")) {
                        variables.put(variable, MyStringUtil.getStringList(value.toString()));
                    } else {
                        variables.put(variable, value);
                    }
                }
            }
            taskService.setVariables(task.getId(), variables);
        }
    }

    /**
     * 得到关键信息
     * @param businessKey
     * @return
     */
    public Map getFormDataInfo(String businessKey,String excludeInfo) {
        Map result=Maps.newHashMap();
        try {
            Map params = Maps.newHashMap();
            params.put("businessKey", businessKey);
            params.put("deleted", false);
            if (PageHelper.count(() -> commonFormDataMapper.selectAll(params)) > 0) {
                List<String> infoList = Lists.newArrayList();
                List<String> excludeIds=MyStringUtil.getStringList(excludeInfo);
                CommonFormData commonFormData = commonFormDataMapper.selectAll(params).get(0);
                Map data = com.common.util.JsonMapper.string2Map(commonFormData.getFormData());
                CommonForm commonForm = commonFormService.getModel(commonFormData.getTenetId(), commonFormData.getReferType(), commonFormData.getFormVersion());
                if (commonForm != null) {
                    List<CommonFormDetail> details = commonFormService.listDetail(commonForm.getId()).stream()
                            .filter(p->!excludeIds.contains(p.getInputCode()))
                            .sorted(Comparator.comparing(CommonFormDetail::getDetailSeq)).collect(Collectors.toList());
                    for (CommonFormDetail detail : details) {
                        InputConfigDto inputConfigDto = InputConfigDto.getInstance(detail.getInputConfig());
                        if (inputConfigDto.isKeyInfo()) {
                            infoList.add(data.getOrDefault(detail.getInputCode(), "").toString());
                        }
                    }
                }

                result.put("modelCategory",commonForm.getFormCategory());
                result.put("keyInfo",StringUtils.join(infoList,"-"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * 得到基础信息
     * @param enLogin
     * @return
     */
    public Map getUserBasicVariable(String enLogin) {
        UserDto userDto = commonUserService.selectByEnLogin(enLogin);
        DeptDto deptDto = commonUserService.getDept(userDto.getDeptId());
        String now = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        Map data = Maps.newHashMap();
        data.put("tenetId", userDto.getTenetId());
        data.put("creator", userDto.getEnLogin());
        data.put("creatorName", userDto.getCnName());
        data.put("enLogin", userDto.getEnLogin());
        data.put("cnName", userDto.getCnName());
        data.put("gmtCreate", now);
        data.put("gmtModified", now);
        data.put("deptId", userDto.getDeptId());
        data.put("deptName", userDto.getDeptName());
        data.put("secondDeptId", deptDto.getSecondDeptId());
        data.put("secondLeaderMen", deptDto.getSecondLeaderMen());
        data.put("secondViceLeaderMen", deptDto.getSecondViceLeaderMen());
        List<String> deptChargeMen = deptDto.getDeptChargeMen();
        List<String> deptViceChargeMen = deptDto.getDeptViceChargeMen();
        data.put("deptChargeMen", deptChargeMen);
        data.put("deptViceChargeMen", deptViceChargeMen);
        if (deptChargeMen.size() > 0) {
            data.put("deptChargeMan", deptChargeMen.get(0));
            data.put("deptChargeManName", commonUserService.getCnNames(deptChargeMen.get(0)));
            //有部门负责人则加上testMen
            data.put("testMen", Lists.newArrayList("luodong", deptChargeMen.get(0)));
        }
        return data;
    }

    public List<CommonCodeDto> listFileType(String businessKey) {
        CommonFormData commonFormData = commonFormDataMapper.selectByBusinessKey(businessKey);
        if (commonFormData != null) {
            String tenetId = commonFormData.getTenetId();
            CommonForm commonForm = commonFormService.getModel(commonFormData.getTenetId(), commonFormData.getFormKey(), commonFormData.getFormVersion());
            if (commonForm != null) {
                TplConfigDto tplConfigDto = TplConfigDto.getInstance(commonForm.getOtherTplConfig());
                if (tplConfigDto.isShowFileType()) {
                    String fileTypeCode = tplConfigDto.getFileTypeCode();
                    if (StringUtils.isNotEmpty(fileTypeCode)) {
                        if (fileTypeCode.startsWith("code_")) {
                            String codeCatalog = fileTypeCode.replace("code_", "");
                            return commonCodeService.listDataByCatalog(tenetId, codeCatalog);
                        }
                    }
                }
            }
        }
        return Lists.newArrayList();
    }






    //hnz： 关联流程调用
    public Map listActPagedData(int pageNum,int pageSize,String referType,String enLogin,String q,Map params) {
        UserDto userDto = userDataService.selectByEnLogin(enLogin);
        String creator = params.getOrDefault("creator", "").toString();
        String uiSref = params.getOrDefault("uiSref", "").toString();
        int referId = Integer.parseInt(params.getOrDefault("referId", "0").toString());
        Map dataParams = Maps.newHashMap();
        dataParams.put("referType", referType);
        dataParams.put("deleted", false);
        dataParams.put("q", q);

        //查指定创建人
        if (StringUtils.isNotEmpty(creator)) {
            dataParams.put("creator", creator);
        }
        //指定了(关键Id)部门Id
        else if (referId > 0) {
            dataParams.put("referId", referId);
        }
        //未指定功能,则直接查当前用户得
        else if (StringUtils.isEmpty(uiSref)) {
            dataParams.put("creator", enLogin);
        } else {
            //根据权限查询
            List<Integer> deptIdList = userDataService.getMyDeptList(enLogin, uiSref);
            if (deptIdList.size() > 0) {
                dataParams.put("referIdList", deptIdList);
            } else {
                //没有则只查自己的
                dataParams.put("creator", enLogin);
            }
        }
        Map result = Maps.newHashMap();

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> commonFormDataMapper.selectAll(dataParams));

        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            List<CommonFormData> commonFormDataList = Lists.newArrayList();
            commonFormDataList.add((CommonFormData) p);
            CommonForm commonForm = commonFormService.getModel(userDto.getTenetId(), ((CommonFormData) p).getFormKey(), 0);

            Map detailParams = Maps.newHashMap();
            detailParams.put("deleted", false);
            detailParams.put("formId", commonForm.getId());
            detailParams.put("listHidden", false);
            List<CommonFormDetail> details = commonFormDetailMapper.selectAll(detailParams).stream().sorted(Comparator.comparing(CommonFormDetail::getListSeq)).collect(Collectors.toList());
            list.addAll(getDataList(commonFormDataList, details, enLogin,commonForm.getFormDesc()));
        });


        pageInfo.setList(list);
        result.put("pageInfo", pageInfo);

        return result;
    }
    //hnz 关联流程反向
    private List<Map> getDataList(List<CommonFormData> list,List<CommonFormDetail> details,String enLogin,String formDesc) {

        List<Map> dataList = Lists.newArrayList();

        for (CommonFormData commonFormData : list) {
            Map data = JsonMapper.string2Map(commonFormData.getFormData());
            Map item = Maps.newHashMap();
            List<Map> propertyList = Lists.newArrayList();
            for (CommonFormDetail detailConfig : details) {
                InputConfigDto inputConfigDto = InputConfigDto.getInstance(detailConfig.getInputConfig());
                Map property = Maps.newHashMap();
                property.put("head",detailConfig.getInputText());
                property.put("style", inputConfigDto.getContentStyle());
                String text=data.getOrDefault(detailConfig.getInputCode(), "").toString();
                if(detailConfig.getInputText().equalsIgnoreCase("创建时间")&&StringUtils.isNotEmpty(text)){
                    try{
                        Date date= DateUtils.parseDate(text,"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd");
                        text=DateFormatUtils.format(date,"yyyy-MM-dd");
                    }catch (Exception ed){

                    }
                }
                if (StringUtils.isNotEmpty(inputConfigDto.getDateFormat())) {
                    try {
                        Date date = DateUtils.parseDate(text, "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm");
                        text=DateFormatUtils.format(date, inputConfigDto.getDateFormat());
                    } catch (Exception dateFormatEx) {

                    }
                }


                property.put("text", text);

                propertyList.add(property);
            }

            CustomSimpleProcessInstance customSimpleProcessInstance =null;
            if(StringUtils.isNotEmpty(commonFormData.getProcessInstanceId())&&!commonFormData.getProcessEnd()) {
                customSimpleProcessInstance =processQueryService.getCustomSimpleProcessInstance(commonFormData.getProcessInstanceId(), "", enLogin);
                if(customSimpleProcessInstance==null||customSimpleProcessInstance.isFinished()){
                    commonFormData.setProcessEnd(true);
                    commonFormDataMapper.updateByPrimaryKey(commonFormData);
                }
            }

            Map property = Maps.newHashMap();
            Map style = Maps.newHashMap();
            property.put("head","当前状态");
            property.put("style", style);
            style.put("font-weight", "bold");
            if (commonFormData.getProcessEnd()) {
                property.put("text", "已完成");
                style.put("color", "green");
            } else {
                if (customSimpleProcessInstance!=null) {
                    property.put("text", customSimpleProcessInstance.getCurrentTaskName().length()>13?customSimpleProcessInstance.getCurrentTaskName().substring(0,12)+"...":customSimpleProcessInstance.getCurrentTaskName());
                    if (customSimpleProcessInstance.getMyRunningTaskNameList().size() > 0) {
                        style.put("color", "blue");
                    }
                    item.put("processInstance", customSimpleProcessInstance);
                } else {
                    style.put("color", "blue");
                    property.put("text", "填写中");
                }
            }
            propertyList.add(property);
            item.put("color", style.get("color"));
            item.put("id", commonFormData.getId());
            item.put("creatorName",commonFormData.getCreatorName());
            item.put("gmtCreate",commonFormData.getGmtCreate());
            item.put("deptName",commonFormData.getDeptName());
            item.put("processInstanceId", commonFormData.getProcessInstanceId());
            item.put("processEnd", commonFormData.getProcessEnd());
            item.put("removeAble", commonFormData.getCreator().equalsIgnoreCase(enLogin)&&(StringUtils.isEmpty(commonFormData.getProcessInstanceId()) || !commonFormData.getProcessEnd()));
            item.put("propertyList", propertyList);
            item.put("businessKey", commonFormData.getBusinessKey());
            item.put("formDesc",formDesc);

            StringBuilder cadTaskName = new StringBuilder();
            cadTaskName.append(commonFormData.getCreatorName());
            cadTaskName.append("-");
            cadTaskName.append(DateFormatUtils.format(commonFormData.getGmtCreate(), "MMdd"));
            cadTaskName.append("-");
            for (int i = 0; i < Math.min(propertyList.size(), 2); i++) {
                String headText = propertyList.get(i).get("text").toString();
                if (!headText.equalsIgnoreCase("填表人") && !headText.equalsIgnoreCase("填表时间") && !headText.equalsIgnoreCase("创建人") && !headText.equalsIgnoreCase("创建时间")) {
                    cadTaskName.append(propertyList.get(i).get("text"));
                    cadTaskName.append("-");
                }
            }
            item.put("cadTaskName", cadTaskName.toString().endsWith("-") ? cadTaskName.toString().substring(0, cadTaskName.toString().length() - 1) : cadTaskName.toString());

            StringBuilder cadTaskTooltip = new StringBuilder();
            for (int i = 2; i < propertyList.size(); i++) {
                cadTaskTooltip.append(propertyList.get(i).get("text"));
                cadTaskTooltip.append("-");
            }
            item.put("cadTaskTooltip", cadTaskTooltip.toString().endsWith("-") ? cadTaskTooltip.toString().substring(0, cadTaskTooltip.toString().length() - 1) : cadTaskTooltip.toString());
            item.put("dirPath", commonFormData.getCreatorName() + "-" + DateFormatUtils.format(commonFormData.getGmtCreate(), "yyyyMMdd") + "-" + commonFormData.getId());
            dataList.add(item);
        }
        return dataList;
    }


}
