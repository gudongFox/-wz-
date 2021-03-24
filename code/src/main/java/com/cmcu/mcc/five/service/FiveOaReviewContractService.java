package com.cmcu.mcc.five.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.dto.ActHistoryDto;
import com.cmcu.mcc.five.dao.FiveOaReviewContractMapper;
import com.cmcu.mcc.five.dto.FiveOaReviewContractDto;
import com.cmcu.mcc.five.entity.FiveOaReviewContract;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.HrEmployeeService;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.service.ActService;
import com.cmcu.mcc.service.impl.HandleFormService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class FiveOaReviewContractService {
    @Resource
    FiveOaReviewContractMapper fiveOaReviewContractMapper;
    @Resource
    HrEmployeeMapper hrEmployeeMapper;
    @Resource
    CommonCodeService commonCodeService;
    @Autowired
    MyActService myActService;
    @Autowired
    ProcessQueryService processQueryService;
    @Autowired
    HrEmployeeSysService hrEmployeeSysService;
    @Autowired
    SelectEmployeeService selectEmployeeService;
    @Autowired
    TaskHandleService taskHandleService;
    @Autowired
    HrEmployeeService hrEmployeeService;
    @Autowired
    ActService actService;

    @Resource
    HandleFormService handleFormService;

    public void remove(int id,String userLogin){
        FiveOaReviewContract item = fiveOaReviewContractMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

        //流程作废
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);


    }

    public void update(FiveOaReviewContractDto fiveOaReviewContractDto){
        FiveOaReviewContract model = fiveOaReviewContractMapper.selectByPrimaryKey(fiveOaReviewContractDto.getId());
        model.setDeptId(fiveOaReviewContractDto.getDeptId());
        model.setDeptName(fiveOaReviewContractDto.getDeptName());
        model.setProjectType(fiveOaReviewContractDto.getProjectType());
        model.setContractTime(fiveOaReviewContractDto.getContractTime());
        model.setProjectName(fiveOaReviewContractDto.getProjectName());
        model.setContractNo(fiveOaReviewContractDto.getContractNo());
        model.setContractMoney(fiveOaReviewContractDto.getContractMoney());
        model.setReviewLevel(fiveOaReviewContractDto.getReviewLevel());
        model.setFirstParty(fiveOaReviewContractDto.getFirstParty());
        model.setSecondParty(fiveOaReviewContractDto.getSecondParty());
        model.setTotalDesigner(fiveOaReviewContractDto.getTotalDesigner());
        model.setTotalDesignerName(fiveOaReviewContractDto.getTotalDesignerName());
        model.setReviewTime(fiveOaReviewContractDto.getReviewTime());
        model.setReviewAddress(fiveOaReviewContractDto.getReviewAddress());
        model.setReviewUser(fiveOaReviewContractDto.getReviewUser());
        model.setReviewUserName(fiveOaReviewContractDto.getReviewUserName());
        model.setContractContent(fiveOaReviewContractDto.getContractContent());
        model.setReviewContent(fiveOaReviewContractDto.getReviewContent());
        model.setReviewLawId(fiveOaReviewContractDto.getReviewLawId());
        model.setReviewLawName(fiveOaReviewContractDto.getReviewLawName());
        model.setRemark(fiveOaReviewContractDto.getRemark());
        model.setReviewConclusion(fiveOaReviewContractDto.getReviewConclusion());
        Map variables = Maps.newHashMap();
        if (model.getReviewLevel().contains("公司级")){
            variables.put("flag", 1);
        }else {
            variables.put("flag", 0);
        }
        variables.put("totalDesigner", model.getTotalDesigner());
        variables.put("reviewUsers", MyStringUtil.getStringList(model.getReviewUser()));
        myActService.setVariables(model.getProcessInstanceId(),variables);
        BeanValidator.check(model);
        fiveOaReviewContractMapper.updateByPrimaryKey(model);


    }

    public FiveOaReviewContractDto getModelById(int id){

        return getDto(fiveOaReviewContractMapper.selectByPrimaryKey(id));
    }

    public FiveOaReviewContractDto getDto(FiveOaReviewContract item) {
        FiveOaReviewContractDto dto=FiveOaReviewContractDto.adapt(item);
        dto.setProcessName("已完成");
        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");
            if(customProcessInstance==null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaReviewContractMapper.updateByPrimaryKey(dto);
            }else {
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }

        return dto;
    }

    public int getNewModel(String userLogin){

        FiveOaReviewContract item=new FiveOaReviewContract();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());

        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setReviewLevel(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"评审级别").toString());
        item.setProjectType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"五洲项目类型").toString());

        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        ModelUtil.setNotNullFields(item);

        fiveOaReviewContractMapper.insert(item);
        String businessKey= EdConst.FIVE_OA_REVIEW_CONTRACT+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "合同评审单");
        variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDeptId()));//发起者部门领导
        variables.put("businessLeader", selectEmployeeService.getDeptChargeMen(48));//经营发展部领导
        variables.put("businessChargeLeader", selectEmployeeService.getOtherDeptChargeMan(48));//经营发展部分管领导
        variables.put("opratorLeader", selectEmployeeService.getDeptChargeMen(29));//运营部领导
        variables.put("lawReview", selectEmployeeService.getUserListByRoleName("法律审查"));//法律审查
        item.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_REVIEW_CONTRACT,item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        fiveOaReviewContractMapper.updateByPrimaryKey(item);
        return item.getId();
    }


    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
         List<Integer> deptIdList=hrEmployeeSysService.getMyDeptListOa(userLogin,uiSref);
         if (deptIdList.size()==0){
            params.put("creator",userLogin);
        }else {
            params.put("deptIdList",deptIdList);
        }
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaReviewContractMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaReviewContract item=(FiveOaReviewContract)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }
    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaReviewContract item = fiveOaReviewContractMapper.selectByPrimaryKey(id);
        data.put("projectName",item.getProjectName());
        data.put("projectType",item.getProjectType());
        data.put("contractTime",item.getContractTime());
        data.put("contractNo",item.getContractNo());
        data.put("contractMoney",item.getContractMoney());
        data.put("reviewLevel",item.getReviewLevel());
        data.put("totalDesignerName",item.getTotalDesignerName());
        data.put("firstParty",item.getFirstParty());
        data.put("secondParty",item.getSecondParty());
        data.put("reviewTime",item.getReviewTime());
        data.put("reviewAddress",item.getReviewAddress());
        data.put("reviewUserName",item.getReviewUserName());
        data.put("contractContent",item.getContractContent());
        data.put("reviewContent",item.getReviewContent());
        data.put("reviewConclusion",item.getReviewConclusion());

        data.put("creatorName",item.getCreatorName());
        data.put("gmtCreate",item.getGmtCreate());

        List<ActHistoryDto> actHistoryDtos = actService.listPassedHistoryDto(item.getProcessInstanceId());
        for (ActHistoryDto dto:actHistoryDtos){
            if (dto.getActivityName()==null){
                break;
            }
            if ("项目经理".equals(dto.getActivityName())){
                data.put("projectManager",dto);
            }
            if ("发起人单位负责人".equals(dto.getActivityName())){
                data.put("deptChargeMen",dto);
            }
            if ("经验发展部负责人".equals(dto.getActivityName())){
                data.put("manageLeader",dto);
            }
            if ("经营发展部主管领导".equals(dto.getActivityName())){
                data.put("manageSupervisorLeader",dto);
            }
        }
        data.put("nodes",actHistoryDtos);

        return data;
    }

}
