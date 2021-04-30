package com.cmcu.mcc.finance.service;

import com.cmcu.act.dto.CustomProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyDateUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.act.model.MyProcessInstance;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.finance.dao.FiveFinanceBackLetterMapper;
import com.cmcu.mcc.finance.dto.FiveFinanceBackLetterDto;
import com.cmcu.mcc.finance.entity.FiveFinanceBackLetter;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FiveFinanceBackLetterService {
    @Resource
    FiveFinanceBackLetterMapper fiveFinanceBackLetterMapper;
    @Resource
    HrEmployeeMapper hrEmployeeMapper;
    @Resource
    CommonCodeService commonCodeService;
    @Autowired
    MyActService myActService;
    @Autowired
    MyHistoryService myHistoryService;
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
    @Autowired
    ProcessQueryService processQueryService;


    public void remove(int id,String userLogin){
        FiveFinanceBackLetter item = fiveFinanceBackLetterMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");
        //流程作废
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    public void update(FiveFinanceBackLetterDto fiveFinanceBackLetterDto){
        FiveFinanceBackLetter model = fiveFinanceBackLetterMapper.selectByPrimaryKey(fiveFinanceBackLetterDto.getId());

        model.setDeptId(fiveFinanceBackLetterDto.getDeptId());
        model.setDeptName(fiveFinanceBackLetterDto.getDeptName());
        model.setUser(fiveFinanceBackLetterDto.getUser());
        model.setUserName(fiveFinanceBackLetterDto.getUserName());
        model.setRelateType(fiveFinanceBackLetterDto.getRelateType());
        model.setProjectName(fiveFinanceBackLetterDto.getProjectName());
        model.setProjectNo(fiveFinanceBackLetterDto.getProjectNo());
        model.setContractName(fiveFinanceBackLetterDto.getContractName());
        model.setContractNo(fiveFinanceBackLetterDto.getContractNo());
        model.setBidNo(fiveFinanceBackLetterDto.getBidNo());
        model.setCombo(fiveFinanceBackLetterDto.getCombo());
        model.setComboName(fiveFinanceBackLetterDto.getComboName());
        model.setBackLetterType(fiveFinanceBackLetterDto.getBackLetterType());
        model.setAssureDate(fiveFinanceBackLetterDto.getAssureDate());
        model.setAssureDateEnd(fiveFinanceBackLetterDto.getAssureDateEnd());
        model.setAssureMonth(fiveFinanceBackLetterDto.getAssureMonth());
        model.setBackContractNo(fiveFinanceBackLetterDto.getBackContractNo());
        model.setBackLetterNo(fiveFinanceBackLetterDto.getBackLetterNo());
        model.setAssureBank(fiveFinanceBackLetterDto.getAssureBank());
        model.setBackLetterDate(fiveFinanceBackLetterDto.getBackLetterDate());
        model.setCancelDate(fiveFinanceBackLetterDto.getCancelDate());
        model.setContinueDate(fiveFinanceBackLetterDto.getContinueDate());
        model.setContinueDateEnd(fiveFinanceBackLetterDto.getContinueDateEnd());
        model.setContinueMonth(fiveFinanceBackLetterDto.getContinueMonth());
        model.setRemark(fiveFinanceBackLetterDto.getRemark());
        model.setGmtModified(new Date());
        //元转万元
        model.setPoundage(MyStringUtil.getMoneyW(fiveFinanceBackLetterDto.getPoundage()));
        model.setCash(MyStringUtil.getMoneyW(fiveFinanceBackLetterDto.getCash()));

        fiveFinanceBackLetterMapper.updateByPrimaryKey(model);

        Map variables = Maps.newHashMap();
        variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(model.getDeptId()));//部门领导
        variables.put("deptDeputyMen",selectEmployeeService.getOtherDeptChargeMan(model.getDeptId()));//部门副总
        variables.put("cashier",selectEmployeeService.getUserListByRoleName("财务金融部-出纳"));//财务部-出纳

        if(fiveFinanceBackLetterDto.getRelateType().equals("撤销")){
            variables.put("flag", 1);
        }else{
            variables.put("flag", 0);
        }
        myActService.setVariables(model.getProcessInstanceId(),variables);

    }


    public FiveFinanceBackLetterDto getModelById(int id){

        return getDto(fiveFinanceBackLetterMapper.selectByPrimaryKey(id));
    }

    public FiveFinanceBackLetterDto getDto(FiveFinanceBackLetter item) {
        FiveFinanceBackLetterDto dto=FiveFinanceBackLetterDto.adapt(item);
        //万元转为元
        dto.setPoundage(MyStringUtil.getMoneyY(dto.getPoundage()));
        dto.setCash(MyStringUtil.getMoneyY(dto.getCash()));
        dto.setProcessName("已完成");
        //MyProcessInstance processInstanceDto=myHistoryService.getMyProcessInstance(dto.getProcessInstanceId(),"");
        CustomProcessInstance customProcessInstance = processQueryService.getCustomProcessInstance(dto.getProcessInstanceId
                (), "", "");
        dto.setProcessName(customProcessInstance.getCurrentTaskName());
            if(customProcessInstance.isFinished()){
                dto.setProcessName("已完成");
            }

        if(!item.getProcessEnd()&&customProcessInstance.isFinished()){
            dto.setProcessEnd(true);
            fiveFinanceBackLetterMapper.updateByPrimaryKey(dto);
        }
        return dto;
    }

    public int getNewModel(String userLogin){
        FiveFinanceBackLetter item=new FiveFinanceBackLetter();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setUser(hrEmployeeDto.getUserLogin());
        item.setUserName(hrEmployeeDto.getUserName());
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setAssureDate(MyDateUtil.getStringToday());
        item.setAssureDateEnd(MyDateUtil.getStringToday());
        item.setBackLetterDate(MyDateUtil.getStringToday());
        item.setCancelDate(MyDateUtil.getStringToday());
        item.setContinueDate(MyDateUtil.getStringToday());
        item.setContinueDateEnd(MyDateUtil.getStringToday());
        item.setRelateType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"相关按钮").toString());
        item.setBackLetterType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"保函类型").toString());
        item.setCombo("否");
        ModelUtil.setNotNullFields(item);
        fiveFinanceBackLetterMapper.insert(item);
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("deptChargeMan", selectEmployeeService.getDeptChargeMen(hrEmployeeDto.getDeptId()));
        variables.put("processDescription", "保函管理："+item.getCreatorName());

        String  businessKey= EdConst.FIVE_FINANCE_BACK_LETTER+ "_" + item.getId();
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_FINANCE_BACK_LETTER,businessKey, variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);
        fiveFinanceBackLetterMapper.updateByPrimaryKey(item);
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
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveFinanceBackLetterMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveFinanceBackLetter item=(FiveFinanceBackLetter)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }
    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveFinanceBackLetter item = fiveFinanceBackLetterMapper.selectByPrimaryKey(id);
        data.put("deptName",item.getDeptName());
        data.put("creatorName",item.getCreatorName());
        data.put("gmtCreate",item.getGmtCreate());

        Map map =new HashMap();
        map.put("materialPurchaseId",item.getId());
        map.put("deleted",false);

        data.put("nodes",actService.listPassedHistoryDto(item.getProcessInstanceId()));

        return data;
    }
}
