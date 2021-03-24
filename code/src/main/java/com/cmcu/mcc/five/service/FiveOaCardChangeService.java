package com.cmcu.mcc.five.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyDateUtil;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.five.dao.FiveOaCardChangeDetailMapper;
import com.cmcu.mcc.five.dao.FiveOaCardChangeMapper;
import com.cmcu.mcc.five.dto.FiveOaCardChangeDto;
import com.cmcu.mcc.five.entity.*;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.service.impl.HandleFormService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class FiveOaCardChangeService extends BaseService {
    @Resource
    FiveOaCardChangeMapper fiveOaCardChangeMapper;
    @Resource
    FiveOaCardChangeDetailMapper fiveOaCardChangeDetailMapper;
    @Resource
    HandleFormService handleFormService;
    @Resource
    HrEmployeeMapper hrEmployeeMapper;
    @Resource
    SelectEmployeeService selectEmployeeService;
    @Resource
    TaskHandleService taskHandleService;

    @Resource
    CommonCodeService commonCodeService;
    @Resource
    ProcessQueryService processQueryService;

    public void remove(int id,String userLogin) {
        FiveOaCardChange item = fiveOaCardChangeMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin),"只允许创建人删除！");

        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    public void update(FiveOaCardChange item){
        FiveOaCardChange model = fiveOaCardChangeMapper.selectByPrimaryKey(item.getId());
        model.setRemark(item.getRemark());
        model.setGmtModified(new Date());

        fiveOaCardChangeMapper.updateByPrimaryKey(model);

    }

    public FiveOaCardChange getModelById(int id){
        return fiveOaCardChangeMapper.selectByPrimaryKey(id);
    }


    public int getNewModel(String userLogin){


        FiveOaCardChange item= new FiveOaCardChange();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());

        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setChangeTime(MyDateUtil.getUserDate("yyyy-MM"));

        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        ModelUtil.setNotNullFields(item);

        fiveOaCardChangeMapper.insert(item);
        String businessKey= EdConst.FIVE_OA_CARD_CHANGE+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "职工充值卡变动:"+item.getCreatorName());
        variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDeptId()));//发起者部门领导
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_CARD_CHANGE,businessKey, variables, MccConst.APP_CODE);
        item.setBusinessKey(businessKey);
        item.setProcessInstanceId(processInstanceId);
        fiveOaCardChangeMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref, int pageNum, int pageSize) {
        params.put("deleted",false);
        /*List<Integer> deptIdList=hrEmployeeSysService.getMyDeptListOa(userLogin,uiSref);
        if (deptIdList.size()==0){
            params.put("creator",userLogin);
        }else {
            params.put("deptIdList",deptIdList);
        }*/
        params.put("isLikeSelect",true);

        //如果有数据权限判断数据权限  myDeptData true查看当前部门 false查看创建人
        Map map =new HashMap();
        map.put("myDeptData",false);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaCardChangeMapper.selectAll(params));
        return pageInfo;
    }

    public FiveOaCardChangeDto getDto(FiveOaCardChange item) {
        FiveOaCardChangeDto dto=FiveOaCardChangeDto.adapt(item);
        dto.setProcessName("已完成");

        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if(customProcessInstance == null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaCardChangeMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance != null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }

        return dto;
    }


    public void removeDetail(int id){
        FiveOaCardChangeDetail model = fiveOaCardChangeDetailMapper.selectByPrimaryKey(id);

        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveOaCardChangeDetailMapper.updateByPrimaryKey(model);
    }

    public void updateDetail(FiveOaCardChangeDetail item){
        item.setGmtModified(new Date());
        fiveOaCardChangeDetailMapper.updateByPrimaryKey(item);
    }

    public FiveOaCardChangeDetail getNewModelDetail(int  id,String userLogin){

        FiveOaCardChangeDetail item=new FiveOaCardChangeDetail();

        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setChangeId(id);
        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setUserType(hrEmployeeDto.getUserType());

        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);
        item.setCardTypeNow(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"充卡类型").toString());
        item.setCardTypeChange(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"充卡类型").toString());
        ModelUtil.setNotNullFields(item);
        fiveOaCardChangeDetailMapper.insert(item);
        return item;

    }

    public FiveOaCardChangeDetail getModelDetailById(int id){
        return fiveOaCardChangeDetailMapper.selectByPrimaryKey(id);
    }

    public List<FiveOaCardChangeDetail> listDetail(int id){
        Map <String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("changeId",id);
        List<FiveOaCardChangeDetail> list = fiveOaCardChangeDetailMapper.selectAll(params);
        return list;
    }


}
