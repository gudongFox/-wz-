package com.cmcu.mcc.five.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.five.dao.FiveOaComputerPurchaseDetailMapper;
import com.cmcu.mcc.five.dao.FiveOaComputerPurchaseMapper;
import com.cmcu.mcc.five.dto.FiveOaComputerPurchaseDto;
import com.cmcu.mcc.five.entity.FiveOaComputerPurchase;
import com.cmcu.mcc.five.entity.FiveOaComputerPurchaseDetail;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.HrEmployeeService;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.service.impl.HandleFormService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;
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
public class FiveOaComputerPurchaseService {
    @Resource
    FiveOaComputerPurchaseMapper fiveOaComputerPurchaseMapper;
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
    FiveOaComputerPurchaseDetailMapper fiveOaComputerPurchaseDetailMapper;
    @Resource
    HandleFormService handleFormService;

    public void remove(int id,String userLogin){
        FiveOaComputerPurchase item = fiveOaComputerPurchaseMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

        //流程作废
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

    }

    public void update(FiveOaComputerPurchaseDto fiveOaComputerPurchaseDto){
        FiveOaComputerPurchase model = fiveOaComputerPurchaseMapper.selectByPrimaryKey(fiveOaComputerPurchaseDto.getId());
        model.setDeptId(fiveOaComputerPurchaseDto.getDeptId());
        model.setDeptName(fiveOaComputerPurchaseDto.getDeptName());
        model.setApplicationMan(fiveOaComputerPurchaseDto.getApplicationMan());
        model.setApplicationManName(fiveOaComputerPurchaseDto.getApplicationManName());
        model.setApplicationTime(fiveOaComputerPurchaseDto.getApplicationTime());
        model.setRemark(fiveOaComputerPurchaseDto.getRemark());
        model.setGmtModified(new Date());
        fiveOaComputerPurchaseMapper.updateByPrimaryKey(model);

        Map variables = Maps.newHashMap();
        if(!Strings.isNullOrEmpty(fiveOaComputerPurchaseDto.getDeptId())) {
            variables.put("deptChargeMan", selectEmployeeService.getDeptChargeMen(Integer.parseInt(fiveOaComputerPurchaseDto.getDeptId())));//发起者部门领导
        }
        BeanValidator.check(model);
        myActService.setVariables(model.getProcessInstanceId(),variables);
    }

    public FiveOaComputerPurchaseDto getModelById(int id){

        return getDto(fiveOaComputerPurchaseMapper.selectByPrimaryKey(id));
    }

    public FiveOaComputerPurchaseDto getDto(FiveOaComputerPurchase item) {
        FiveOaComputerPurchaseDto dto=FiveOaComputerPurchaseDto.adapt(item);
        dto.setProcessName("已完成");

        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");
            if(customProcessInstance == null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaComputerPurchaseMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance != null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());

            }
        }

        return dto;
    }

    public int getNewModel(String userLogin){


        FiveOaComputerPurchase item=new FiveOaComputerPurchase();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeptId(hrEmployeeDto.getDeptId()+"");
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setApplicationMan(hrEmployeeDto.getUserLogin());
        item.setApplicationManName(hrEmployeeDto.getUserName());
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        ModelUtil.setNotNullFields(item);

        fiveOaComputerPurchaseMapper.insert(item);
        String businessKey= EdConst.FIVE_OA_COMPUTER_PURCHASE+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);

        variables.put("processDescription", "资料采购申请流程："+item.getCreatorName());

        item.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_COMPUTER_PURCHASE,item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        fiveOaComputerPurchaseMapper.updateByPrimaryKey(item);
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
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaComputerPurchaseMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaComputerPurchase item=(FiveOaComputerPurchase) p;
            FiveOaComputerPurchaseDto dto = getDto(item);
            if (dto.getAttendUser().contains(userLogin)){
                list.add(dto);
            }
        });
        pageInfo.setList(list);
        return pageInfo;
    }





    public void updateDetail(FiveOaComputerPurchaseDetail item){
        FiveOaComputerPurchaseDetail model = fiveOaComputerPurchaseDetailMapper.selectByPrimaryKey(item.getId());
        model.setDeptId(item.getDeptId());
        model.setDeptName(item.getDeptName());
        model.setPerson(item.getPerson());
        model.setPersonName(item.getPersonName());
        model.setPersonNo(item.getPersonNo());
        model.setSecretLevel(item.getSecretLevel());
        model.setPhone(item.getPhone());
        model.setRoomNo(item.getRoomNo());
        model.setDeviceType(item.getDeviceType());
        model.setNetType(item.getNetType());
        model.setCommand(item.getCommand());
        model.setKeyNo(item.getKeyNo());
        model.setPhysicsAddress(item.getPhysicsAddress());
        model.setIpAddress(item.getIpAddress());
        model.setDiskNo(item.getDiskNo());
        model.setRemark(item.getRemark());
        fiveOaComputerPurchaseDetailMapper.updateByPrimaryKey(model);
    }

    public FiveOaComputerPurchaseDetail getNewModelDetail(int  computerPurchaseId){
        FiveOaComputerPurchaseDetail item=new FiveOaComputerPurchaseDetail();
        item.setComputerPurchaseId(computerPurchaseId);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);
        ModelUtil.setNotNullFields(item);
        fiveOaComputerPurchaseDetailMapper.insert(item);
        return item;

    }

    public FiveOaComputerPurchaseDetail getModelDetailById(int id){
        return fiveOaComputerPurchaseDetailMapper.selectByPrimaryKey(id);
    }

    public List<FiveOaComputerPurchaseDetail> listDetail(int computerPurchaseId){
        Map <String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("computerPurchaseId",computerPurchaseId);//小写
        List<FiveOaComputerPurchaseDetail> list = fiveOaComputerPurchaseDetailMapper.selectAll(params);
        return list;
    }

    public void removeDetail(int id){
        FiveOaComputerPurchaseDetail model = fiveOaComputerPurchaseDetailMapper.selectByPrimaryKey(id);
        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveOaComputerPurchaseDetailMapper.updateByPrimaryKey(model);
    }
}
