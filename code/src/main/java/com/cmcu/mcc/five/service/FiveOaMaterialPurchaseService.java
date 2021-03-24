package com.cmcu.mcc.five.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.five.dao.FiveOaMaterialPurchaseDetailMapper;
import com.cmcu.mcc.five.dao.FiveOaMaterialPurchaseMapper;
import com.cmcu.mcc.five.dto.FiveOaMaterialPurchaseDto;
import com.cmcu.mcc.five.entity.*;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrDeptDto;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.HrDeptService;
import com.cmcu.mcc.hr.service.HrEmployeeService;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.service.ActService;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FiveOaMaterialPurchaseService extends BaseService {
    @Resource
    FiveOaMaterialPurchaseMapper fiveOaMaterialPurchaseMapper;
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
    FiveOaMaterialPurchaseDetailMapper fiveOaMaterialPurchaseDetailMapper;
    @Autowired
    ActService actService;
    @Resource
    HandleFormService handleFormService;
    @Resource
    HrDeptService hrDeptService;

    public void remove(int id,String userLogin){
        FiveOaMaterialPurchase item = fiveOaMaterialPurchaseMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

        //流程作废
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

    }

    public void update(FiveOaMaterialPurchaseDto fiveOaMaterialPurchaseDto){
        FiveOaMaterialPurchase model = fiveOaMaterialPurchaseMapper.selectByPrimaryKey(fiveOaMaterialPurchaseDto.getId());
        model.setApplicantManName(fiveOaMaterialPurchaseDto.getApplicantManName());
        model.setApplicantMan(fiveOaMaterialPurchaseDto.getApplicantMan());
        model.setApplicantNo(fiveOaMaterialPurchaseDto.getApplicantNo());
        model.setApplicantTel(fiveOaMaterialPurchaseDto.getApplicantTel());
        model.setCompanyCharge(fiveOaMaterialPurchaseDto.getCompanyCharge());
        model.setCompanyChargeName(fiveOaMaterialPurchaseDto.getCompanyChargeName());
        model.setLibrary(fiveOaMaterialPurchaseDto.getLibrary());
        model.setDeptId(fiveOaMaterialPurchaseDto.getDeptId());
        model.setDeptName(fiveOaMaterialPurchaseDto.getDeptName());
        model.setRemark(fiveOaMaterialPurchaseDto.getRemark());
        model.setGmtModified(new Date());

        ModelUtil.setNotNullFields(model);
        fiveOaMaterialPurchaseMapper.updateByPrimaryKey(model);

        Map variables = Maps.newHashMap();
        if(!"".equals(fiveOaMaterialPurchaseDto.getCompanyCharge())) {
            variables.put("deptChargeMan", MyStringUtil.getStringList(fiveOaMaterialPurchaseDto.getCompanyCharge()));//发起者部门领导
        }
        myActService.setVariables(model.getProcessInstanceId(),variables);
        BeanValidator.check(model);
    }


    public FiveOaMaterialPurchaseDto getModelById(int id){

        return getDto(fiveOaMaterialPurchaseMapper.selectByPrimaryKey(id));
    }

    public FiveOaMaterialPurchaseDto getDto(FiveOaMaterialPurchase item) {
        FiveOaMaterialPurchaseDto dto=FiveOaMaterialPurchaseDto.adapt(item);
        dto.setProcessName("已完成");
        if (!dto.getProcessEnd()) {

            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if(customProcessInstance ==null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaMaterialPurchaseMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance!=null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }

        List<FiveOaMaterialPurchaseDetail> fiveOaMaterialPurchaseDetails = listDetail(item.getId());
        double contNums=0.0;
        for (FiveOaMaterialPurchaseDetail detail:fiveOaMaterialPurchaseDetails){
            if(!Strings.isNullOrEmpty(detail.getBookNumber())) {
                contNums = contNums + Double.valueOf(detail.getBookNumber());
            }
        }
        dto.setContNums(contNums);
        return dto;
    }

    public int getNewModel(String userLogin){


        FiveOaMaterialPurchase item=new FiveOaMaterialPurchase();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        HrDeptDto hrDeptDto = hrDeptService.getModelById(hrEmployeeDto.getDeptId());

        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setApplicantManName(hrEmployeeDto.getUserName());
        item.setApplicantNo(hrEmployeeDto.getUserLogin());
        item.setApplicantTel(hrEmployeeDto.getMobile());
        item.setCompanyCharge(hrDeptDto.getDeptFirstLeader());
        item.setCompanyChargeName(hrDeptDto.getDeptFirstLeaderName());
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        ModelUtil.setNotNullFields(item);

        fiveOaMaterialPurchaseMapper.insert(item);
        String businessKey= EdConst.FIVE_OA_MATERIAL_PURCHASE+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("deptChargeMan", selectEmployeeService.getDeptChargeMen(hrEmployeeDto.getDeptId()));
        variables.put("processDescription", "资料采购申请流程："+item.getCreatorName());

        item.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_MATERIAL_PURCHASE,item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        fiveOaMaterialPurchaseMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
        params.put("isLikeSelect",true);

        //如果有数据权限判断数据权限  myDeptData true查看当前部门 false查看创建人
        Map map =new HashMap();
        map.put("myDeptData",false);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));
        /*  List<Integer> deptIdList=hrEmployeeSysService.getMyDeptListOa(userLogin,uiSref);
         if (deptIdList.size()==0){
            params.put("creator",userLogin);
        }else {
            params.put("deptIdList",deptIdList);
        }*/
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaMaterialPurchaseMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaMaterialPurchase item=(FiveOaMaterialPurchase)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }


    public void updateDetail(FiveOaMaterialPurchaseDetail item){
        FiveOaMaterialPurchaseDetail model = fiveOaMaterialPurchaseDetailMapper.selectByPrimaryKey(item.getId());
        model.setStandardNo(item.getStandardNo());
        model.setDataName(item.getDataName());
        model.setBookNumber(item.getBookNumber());
        model.setRemark(item.getRemark());
        fiveOaMaterialPurchaseDetailMapper.updateByPrimaryKey(model);
    }

    public FiveOaMaterialPurchaseDetail getNewModelDetail(int  id){
        FiveOaMaterialPurchaseDetail item=new FiveOaMaterialPurchaseDetail();
        item.setMaterialPurchaseId(id);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);
        ModelUtil.setNotNullFields(item);
        fiveOaMaterialPurchaseDetailMapper.insert(item);
        return item;

    }

    public FiveOaMaterialPurchaseDetail getModelDetailById(int id){
        return fiveOaMaterialPurchaseDetailMapper.selectByPrimaryKey(id);
    }

    public List<FiveOaMaterialPurchaseDetail> listDetail(int id){
        Map <String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("materialPurchaseId",id);//小写
        List<FiveOaMaterialPurchaseDetail> list = fiveOaMaterialPurchaseDetailMapper.selectAll(params);
        return list;
    }
    public void removeDetail(int id){
        FiveOaMaterialPurchaseDetail model = fiveOaMaterialPurchaseDetailMapper.selectByPrimaryKey(id);

        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveOaMaterialPurchaseDetailMapper.updateByPrimaryKey(model);
    }
    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaMaterialPurchase item = fiveOaMaterialPurchaseMapper.selectByPrimaryKey(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String number = sdf.format(new Date())+item.getCreator().substring((item.getCreator()).length() - 4);
        data.put("tableNo",number);
        data.put("deptName",item.getDeptName());
        data.put("applicantManName",item.getApplicantManName());
        data.put("applicantNo",item.getApplicantNo());
        data.put("applicantTel",item.getApplicantTel());
        data.put("companyChargeName",item.getCompanyChargeName());
        data.put("library",item.getLibrary());

        data.put("creatorName",item.getCreatorName());
        data.put("gmtCreate",item.getGmtCreate());

        Map map =new HashMap();
        map.put("materialPurchaseId",item.getId());
        map.put("deleted",false);
        List<FiveOaMaterialPurchaseDetail> materialPurchaseDetails = fiveOaMaterialPurchaseDetailMapper.selectAll (map);
        data.put("materialPurchaseDetails",materialPurchaseDetails);
        Double sum=0.0d;
        for (FiveOaMaterialPurchaseDetail detail:materialPurchaseDetails) {
            sum+=Double.valueOf(detail.getBookNumber());
        }
        data.put("sum",sum);

        data.put("nodes",actService.listPassedHistoryDto(item.getProcessInstanceId()));

        return data;
    }
}
