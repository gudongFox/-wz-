package com.cmcu.mcc.five.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.common.dao.CommonFormDataMapper;
import com.cmcu.common.entity.CommonFormData;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.util.*;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.five.dao.FiveOaStampApplyOfficeMapper;
import com.cmcu.mcc.five.dto.FiveOaStampApplyOfficeDto;
import com.cmcu.mcc.five.entity.FiveOaStampApplyOffice;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrDeptDto;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.HrDeptService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.service.impl.HandleFormService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
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
public class FiveOaStampApplyOfficeService extends BaseService {
    @Resource
    FiveOaStampApplyOfficeMapper fiveOaStampApplyOfficeMapper;
    @Resource
    HandleFormService handleFormService;
    @Resource
    MyActService myActService;
    @Resource
    HrEmployeeMapper hrEmployeeMapper;
    @Resource
    SelectEmployeeService selectEmployeeService;
    @Resource
    HrDeptService hrDeptService;
    @Resource
    CommonFormDataMapper commonFormDataMapper;

    public void remove(int id,String userLogin){
        FiveOaStampApplyOffice item = fiveOaStampApplyOfficeMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

        //流程作废
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

    }

    public void update(FiveOaStampApplyOfficeDto fiveOaStampApplyOfficeDto){
        FiveOaStampApplyOffice model = fiveOaStampApplyOfficeMapper.selectByPrimaryKey(fiveOaStampApplyOfficeDto.getId());
        model.setRecord(fiveOaStampApplyOfficeDto.getRecord());
        model.setDeptId(fiveOaStampApplyOfficeDto.getDeptId());
        model.setDeptName(fiveOaStampApplyOfficeDto.getDeptName());
        model.setItem(fiveOaStampApplyOfficeDto.getItem());
        model.setStampName(fiveOaStampApplyOfficeDto.getStampName());
        model.setStampDate(fiveOaStampApplyOfficeDto.getStampDate());
        model.setFileName(fiveOaStampApplyOfficeDto.getFileName());
        model.setFileCount(fiveOaStampApplyOfficeDto.getFileCount());
        model.setLegalReview(fiveOaStampApplyOfficeDto.getLegalReview());
        model.setViceLeader(fiveOaStampApplyOfficeDto.getViceLeader());
        model.setViceLeaderName(fiveOaStampApplyOfficeDto.getViceLeaderName());
        model.setItemDept(fiveOaStampApplyOfficeDto.getItemDept());
        model.setItemDeptName(fiveOaStampApplyOfficeDto.getItemDeptName());
        model.setSecrecy(fiveOaStampApplyOfficeDto.getSecrecy());
        model.setItemDeptChargeMen(fiveOaStampApplyOfficeDto.getItemDeptChargeMen());
        model.setContractNo(fiveOaStampApplyOfficeDto.getContractNo());
        model.setSecondDeptId(fiveOaStampApplyOfficeDto.getSecondDeptId());
        model.setDeptViceChargeMen(fiveOaStampApplyOfficeDto.getDeptViceChargeMen());
        model.setRemark(fiveOaStampApplyOfficeDto.getRemark());
        model.setContracted(fiveOaStampApplyOfficeDto.getContracted());
        model.setGmtModified(new Date());

        HrDeptDto hrDeptDto = hrDeptService.getModelById(model.getDeptId());
        model.setSecondDeptId(hrDeptDto.getParentId());

        fiveOaStampApplyOfficeMapper.updateByPrimaryKey(model);
        Map variables = Maps.newHashMap();
        variables.put("secondDeptId",model.getSecondDeptId());//发起部门上级部门是否是建研院
        variables.put("deptViceChargeMen",selectEmployeeService.getParentDeptChargeMen(model.getDeptId(),2,false));//建研院分院院长
        variables.put("deptChargeMen",selectEmployeeService.getParentDeptChargeMen(model.getDeptId(),1,true));//单位、部门第一负责人
         if (model.getItemDept()!=0){
             variables.put("itemDeptChargeMen",selectEmployeeService.getParentDeptChargeMen(model.getItemDept(),3,true));//事项归口部门负责人
             variables.put("itemDept",model.getItemDept());//事项归口管理部门是否是经营发展部
         }

        variables.put("legalReview",model.getLegalReview());//是否法律审查
        if (model.getSecrecy()||model.getStampName().contains("保密办用印")){
            variables.put("secrecy",true);//是否保密
        }else {
            variables.put("secrecy",false);//是否保密
        }
        variables.put("processDescription", "公司章用印申请："+model.getItem());

        BeanValidator.check(model);
        myActService.setVariables(model.getProcessInstanceId(),variables);

    }

    public FiveOaStampApplyOfficeDto getModelById(int id){

        return getDto(fiveOaStampApplyOfficeMapper.selectByPrimaryKey(id));
    }

    public FiveOaStampApplyOfficeDto getDto(FiveOaStampApplyOffice item) {
        FiveOaStampApplyOfficeDto dto=FiveOaStampApplyOfficeDto.adapt(item);
        dto.setProcessName("已完成");
        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");
            if(customProcessInstance==null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaStampApplyOfficeMapper.updateByPrimaryKey(dto);
            }else {
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }
        //去除首位逗号
        dto.setStampName(item.getStampName().replaceAll("^,*|,*$",""));

        return dto;
    }

    public int getNewModel(String userLogin){
        FiveOaStampApplyOffice item=new FiveOaStampApplyOffice();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        ModelUtil.setNotNullFields(item);
        fiveOaStampApplyOfficeMapper.insert(item);
        String businessKey= EdConst.OA_STAMP_APPLY_OFFICE+ "_" + item.getId();
        Map variables = Maps.newHashMap();

        variables.put("userLogin", userLogin);
        variables.put("processDescription", "公司章用印申请："+item.getDeptName());

        item.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(EdConst.OA_STAMP_APPLY_OFFICE,item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        fiveOaStampApplyOfficeMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref, int pageNum, int pageSize) {
        params.put("deleted",false);
        params.put("isLikeSelect",true);

        Map map =new HashMap();
        map.put("myDeptData",false);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaStampApplyOfficeMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaStampApplyOffice item=(FiveOaStampApplyOffice)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }


    //todo hnz 用印流程数据迁移
    public void listDateByFormKey(){
        Map params = Maps.newHashMap();
        params.put("deleted",false);
        params.put("formKey","oaStampApplyOffice");
        List<CommonFormData> commonFormData = commonFormDataMapper.selectAll(params);
        for (CommonFormData data:commonFormData){
           if(data.getCreator()=="luodong"){
               continue;
           }
            FiveOaStampApplyOffice model=new FiveOaStampApplyOffice();
            Map<String, Object> map = JsonMapper.string2Map(data.getFormData());
            model.setItem(map.getOrDefault("item","").toString());
            model.setStampDate(map.getOrDefault("stampDate","").toString());
            model.setStampName(map.getOrDefault("stampName","").toString());
            model.setFileCount(map.getOrDefault("fileCount","").toString());
            model.setFileName(map.getOrDefault("fileName","").toString());
            String itemDept=map.getOrDefault("itemDept","").toString();
            if(StringUtils.isEmpty(itemDept)) itemDept="0";
            model.setItemDept(Integer.parseInt(itemDept));
            model.setItemDeptName(map.get("itemDeptName").toString());
            model.setDeptId(data.getDeptId());
            model.setDeptName(data.getDeptName());
            model.setItemDeptChargeMen(map.getOrDefault("itemDeptChargeMen","").toString());
            model.setContractNo(map.getOrDefault("contractNo","").toString());

            String secondDeptId=map.getOrDefault("secondDeptId","").toString();
            if(StringUtils.isEmpty(secondDeptId)) secondDeptId="0";
            model.setSecondDeptId(Integer.parseInt(secondDeptId));

            model.setDeptViceChargeMen(map.getOrDefault("deptViceChargeMen","").toString());
            model.setRemark(map.getOrDefault("remark","").toString());

            model.setCreatorName(map.getOrDefault("creatorName","").toString());
            model.setCreator(map.getOrDefault("creator","").toString());

            model.setLegalReview(Boolean.parseBoolean(map.getOrDefault("legalReview","false").toString()));
            model.setContracted(Boolean.parseBoolean(map.getOrDefault("contracted","false").toString()));
            model.setRecord(Boolean.parseBoolean(map.getOrDefault("record","false").toString()));
            model.setSecrecy(Boolean.parseBoolean(map.getOrDefault("secrecy","false").toString()));
            //MyDateUtil.strToDate(map.getOrDefault("gmtCreate","").toString())
            //MyDateUtil.strToDate(map.getOrDefault("gmtModified","").toString())
            model.setGmtCreate(data.getGmtCreate());
            model.setGmtModified(data.getGmtModified());

            model.setProcessInstanceId(data.getProcessInstanceId());
            model.setBusinessKey(data.getBusinessKey());
            model.setProcessEnd(data.getProcessEnd());
            model.setDeleted(false);
            model.setId(data.getId());
            ModelUtil.setNotNullFields(model);
            fiveOaStampApplyOfficeMapper.insert(model);
        }
    }

}
