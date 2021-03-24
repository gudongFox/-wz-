package com.cmcu.mcc.ed.service;

import com.cmcu.act.dto.CustomProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.common.dao.CommonDirMapper;
import com.cmcu.common.dao.CommonFileMapper;
import com.cmcu.common.dao.CommonFormDataMapper;
import com.cmcu.common.dto.CommonDirDto;
import com.cmcu.common.dto.CommonEdArrangeUserDto;
import com.cmcu.common.dto.UserDto;
import com.cmcu.common.entity.CommonDir;
import com.cmcu.common.entity.CommonEdArrangeUser;
import com.cmcu.common.entity.CommonFile;
import com.cmcu.common.entity.CommonFormData;
import com.cmcu.common.service.CommonEdArrangeUserService;
import com.cmcu.common.service.CommonUserService;
import com.cmcu.common.util.JsonMapper;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.act.model.MyHistoryTask;
import com.cmcu.mcc.act.model.MyProcessInstance;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.business.dao.BusinessContractMapper;
import com.cmcu.mcc.business.dto.BusinessContractDto;
import com.cmcu.mcc.business.entity.BusinessContract;
import com.cmcu.mcc.business.service.BusinessContractService;
import com.cmcu.mcc.ed.dao.*;
import com.cmcu.mcc.ed.dto.EdStepBuildDto;
import com.cmcu.mcc.ed.entity.*;
import com.cmcu.mcc.five.dao.FiveEdHouseValidateMapper;
import com.cmcu.mcc.five.entity.FiveEdArrange;
import com.cmcu.mcc.five.entity.FiveEdArrangeUser;

import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.service.impl.HandleFormService;
import com.cmcu.mcc.sys.entity.SysConfig;
import com.cmcu.mcc.sys.service.SysConfigService;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.impl.util.json.JSONException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.struts2.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EdSignPaperService {

    @Autowired
    MyHistoryService myHistoryService;

    @Autowired
    SelectEmployeeService selectEmployeeService;





    @Resource
    EdProjectStepMapper edProjectStepMapper;

    @Autowired
    SysConfigService sysConfigService;

    @Resource
    BusinessContractMapper businessContractMapper;

    @Autowired
    BusinessContractService businessContractService;

    @Autowired
    EdStepBuildService edStepBuildService;


    @Autowired
    FiveEdHouseValidateMapper fiveEdHouseValidateMapper;

    @Autowired
    CommonFileMapper commonFileMapper;
    @Autowired
    CommonDirMapper commonDirMapper;
    @Autowired
    EdStepBuildMapper edStepBuildMapper;
    @Autowired
    HandleFormService handleFormService;
    @Autowired
    CommonFormDataMapper commonFormDataMapper;
    @Autowired
    ProcessQueryService processQueryService;


    @Resource
    HistoryService historyService;

    @Resource
    CommonEdArrangeUserService commonEdArrangeUserService;

    @Resource
    CommonUserService commonUserService;




    private List<Map> getUsers(String userType,String users){
        List<Map> list=Lists.newArrayList();
        for(String user:MyStringUtil.getStringList(users)){
            HrEmployeeDto hrEmployeeDto=selectEmployeeService.selectByUserLogin(user);
            if(hrEmployeeDto!=null) {
                Map map = Maps.newHashMap();
                map.put("userName", selectEmployeeService.getNameByUserLogin(user));
                map.put("userLogin", user);
                map.put("userType", userType);
                map.put("signId", hrEmployeeDto.getSignAttachId());
                list.add(map);
            }
        }
        return list;
    }








    public Map getDwgInformation(int fileId,String enLogin){
        Map result=Maps.newHashMap();
        List<Map> users=Lists.newArrayList();
        result.put("users",users);
        result.put("stampAble",false);
        result.put("codeAble",false);

        CommonFile commonFile=commonFileMapper.selectByPrimaryKey(fileId);
        if(commonFile!=null){

            int stepId=0;
            List<String> majorNames=Lists.newArrayList();
            List<String> buildNameList=Lists.newArrayList();
            if(commonFile.getBusinessKey().startsWith("co_")){
                stepId=Integer.parseInt(StringUtils.split(commonFile.getBusinessKey(),"_")[1]);
                CommonDir dir=commonDirMapper.selectByPrimaryKey(commonFile.getDirId());
                majorNames.add(dir.getMajorName());
                buildNameList.add(dir.getBuildName());
                result.put("majorName",dir.getMajorName());
                result.put("buildName",dir.getBuildName());
            }else {
                CommonFormData commonFormData = commonFormDataMapper.selectByBusinessKey(commonFile.getBusinessKey());
                stepId = commonFormData.getReferId();
                Map formData = JsonMapper.string2Map(commonFormData.getFormData());
                String majorName = formData.getOrDefault("majorName", "").toString();
                if (StringUtils.isNotEmpty(majorName)) majorNames.add(majorName);
                buildNameList.addAll(MyStringUtil.getStringList(formData.getOrDefault("buildNameList", "").toString()));
                result.put("buildName",StringUtils.join(buildNameList,","));
            }

            if(stepId>0) {
                EdProjectStep step = edProjectStepMapper.selectByPrimaryKey(stepId);
                BusinessContractDto contract = businessContractService.getModelById(step.getProjectId());
                result.put("stepId", stepId);
                result.put("projectId", contract.getId());
                result.put("constructionName", contract.getCustomerName());
                result.put("projectName", step.getProjectName());
                result.put("projectNo", step.getProjectNo());
                result.put("stageName", step.getStageName().replace("阶段", ""));
                result.put("dwgVersion", step.getDwgVersion());
                result.put("dwgTime", step.getDwgTime());
                if(majorNames.size()==1) result.put("dwgType",getDwgType(step.getStageName(),majorNames.get(0)));

                String businessKey = commonEdArrangeUserService.getDefaultArrangeBusinessKey(stepId);
                if (StringUtils.isNotEmpty(businessKey)) {
                    List<CommonEdArrangeUserDto> arrangeUsers = commonEdArrangeUserService.listUser(businessKey);
                    if (majorNames.size() > 0)
                        arrangeUsers = arrangeUsers.stream().filter(p -> majorNames.contains(p.getMajorName())).collect(Collectors.toList());
                    if (buildNameList.size() > 0)
                        arrangeUsers = arrangeUsers.stream().filter(p -> buildNameList.contains(p.getBuildName())).collect(Collectors.toList());
                    for (String roleCode : arrangeUsers.stream().map(CommonEdArrangeUser::getRoleCode).distinct().collect(Collectors.toList())) {
                        List<String> enLoginList = MyStringUtil.getStringList(StringUtils.join(arrangeUsers.stream().filter(p -> p.getRoleCode().equalsIgnoreCase(roleCode)).map(CommonEdArrangeUser::getAllUser).collect(Collectors.toList()), ","));
                        users.addAll(getUsers(roleCode+"Name", enLoginList));
                    }
                }
                users.addAll(getUsers("projectChargeName1",MyStringUtil.getStringList(step.getExeChargeMan())));
                users.addAll(getUsers("totalDesignerName",MyStringUtil.getStringList(step.getExeChargeMan())));

                if (!commonFile.getBusinessKey().startsWith("co_")) {
                    //用户是罗冬或者参与角色有打码节点
                    if(enLogin.equalsIgnoreCase("luodong")|| historyService.createHistoricTaskInstanceQuery()
                            .processInstanceBusinessKey(commonFile.getBusinessKey())
                            .taskAssignee(enLogin)
                            .taskNameLike("%打码%").count()>0) {
                        result.put("codeAble", true);
                    }
                }

                return result;
            }


        }

        return result;
    }

    private List<Map> getUsers(String roleName,List<String> enLoginList) {
        List<Map> users = Lists.newArrayList();
        for (String enLogin : enLoginList) {
            UserDto userDto = commonUserService.selectByEnLogin(enLogin);
            if (userDto != null) {
                Map user = Maps.newHashMap();
                user.put("cnName", userDto.getCnName());
                user.put("enLogin", enLogin);
                user.put("roleName", roleName);
                user.put("signUrl", userDto.getSignUrl());
                users.add(user);
            }
        }
        return users;
    }


    private String getDwgType(String stageName,String majorName){
        if(stageName.contains("施工")){
            if(majorName.equalsIgnoreCase("建筑")){
                return "建施";
            }else if(majorName.equalsIgnoreCase("给排水")){
                return "水施";
            }else if(majorName.equalsIgnoreCase("电气")){
                return "电施";
            }
        }
        return majorName;
    }


}
