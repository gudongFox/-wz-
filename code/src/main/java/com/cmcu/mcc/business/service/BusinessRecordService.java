package com.cmcu.mcc.business.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.exception.ParamException;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyPoiUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.business.dao.*;
import com.cmcu.mcc.business.dto.BusinessPreContractDto;
import com.cmcu.mcc.business.dto.BusinessRecordDto;
import com.cmcu.mcc.business.dto.FiveBusinessContractReviewDto;
import com.cmcu.mcc.business.entity.*;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.hr.dto.HrEmployeeSysDto;
import com.cmcu.mcc.hr.entity.HrDept;
import com.cmcu.mcc.hr.service.HrDeptService;
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
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BusinessRecordService extends BaseService {

    @Resource
    BusinessRecordMapper businessRecordMapper;

    @Resource
    CommonCodeService commonCodeService;

    @Autowired
    HrEmployeeSysService hrEmployeeSysService;

    @Autowired
    SelectEmployeeService selectEmployeeService;

    @Autowired
    HrDeptService hrDeptService;

    @Autowired
    MyActService myActService;

    @Autowired
    MyHistoryService myHistoryService;

    @Autowired
    BusinessCustomerMapper businessCustomerMapper;
    @Autowired
    TaskHandleService taskHandleService;
    @Autowired
    FiveBusinessContractLibraryMapper fiveBusinessContractLibraryMapper;
    @Autowired
    HrEmployeeService hrEmployeeService;
    @Autowired
    FiveBusinessContractLibrarySubpackageMapper fiveBusinessContractLibrarySubpackageMapper;
    @Autowired
    HandleFormService handleFormService;

    @Resource
    FiveBusinessContractReviewService fiveBusinessContractReviewService;
    @Resource
    BusinessPreContractService businessPreContractService;


    public void remove(int id, String userLogin) {
        BusinessRecord item = businessRecordMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "????????????????????????!");
        //??????????????? ????????????????????? ????????????
        Assert.state(MyStringUtil.getStringListExcept0(item.getContractReviewIds()).size()==0, "????????? ??????????????????");
        Assert.state(MyStringUtil.getStringListExcept0(item.getPreContractIds()).size()==0, "????????? ?????????????????????");
        //??????????????? ??????
        if(item.getSubpackageId()!=0){
            FiveBusinessContractLibrarySubpackage subpackage = fiveBusinessContractLibrarySubpackageMapper.selectByPrimaryKey(item.getSubpackageId());
            subpackage.setRecordId(0);
            fiveBusinessContractLibrarySubpackageMapper.updateByPrimaryKey(subpackage);
        }
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    public void update(BusinessRecordDto businessRecordDto) {
        BusinessRecord item = businessRecordMapper.selectByPrimaryKey(businessRecordDto.getId());
        //???????????? ???????????????
        Map map =new HashMap();
        map.put("projectName",businessRecordDto.getProjectName());
        map.put("deleted",false);
        List<BusinessRecord> businessRecords = businessRecordMapper.selectAll(map);
        if(businessRecords.size()!=0){
            //???????????????????????????
            Assert.state( businessRecords.get(0).getId().equals(businessRecordDto.getId()),"????????????"+businessRecordDto.getProjectName()+" ?????????");
        }

        //????????????????????????
        if(businessRecordDto.getSubpackageId()!=0){
            //????????????????????? ??????
            if(item.getSubpackageId()!=0){
                FiveBusinessContractLibrarySubpackage subpackage = fiveBusinessContractLibrarySubpackageMapper.selectByPrimaryKey(item.getSubpackageId());
                subpackage.setRecordId(0);
                fiveBusinessContractLibrarySubpackageMapper.updateByPrimaryKey(subpackage);
            }
            FiveBusinessContractLibrarySubpackage subpackage = fiveBusinessContractLibrarySubpackageMapper.selectByPrimaryKey(businessRecordDto.getSubpackageId());
            subpackage.setRecordId(item.getId());
            fiveBusinessContractLibrarySubpackageMapper.updateByPrimaryKey(subpackage);
            item.setSubpackageId(businessRecordDto.getSubpackageId());
        }
        if(!Strings.isNullOrEmpty(businessRecordDto.getCustomerCode())){
            item.setCustomerCode(businessRecordDto.getCustomerCode());
        }else {
            item.setCustomerCode("");
        }
        item.setCustomerId(businessRecordDto.getCustomerId());
        item.setCustomerName(businessRecordDto.getCustomerName());
        item.setCustomerAddress(businessRecordDto.getCustomerAddress());
        item.setLinkMan(businessRecordDto.getLinkMan());
        item.setLinkTel(businessRecordDto.getLinkTel());
        item.setLinkTitle(businessRecordDto.getLinkTitle());
        item.setProjectName(businessRecordDto.getProjectName());
        item.setProjectAddress(businessRecordDto.getProjectAddress());
        item.setProjectType(businessRecordDto.getProjectType());
        item.setProjectScale(businessRecordDto.getProjectScale());
        item.setInvestSource(businessRecordDto.getInvestSource());
        item.setProjectInvest(businessRecordDto.getProjectInvest());
        item.setTenderBond(businessRecordDto.getTenderBond());
        item.setPlanBeginTime(businessRecordDto.getPlanBeginTime());
        item.setPlanEndTime(businessRecordDto.getPlanEndTime());
        item.setProjectNo(businessRecordDto.getProjectNo());
        if (businessRecordDto.getTenderBond()){
            item.setTenderBondType(businessRecordDto.getTenderBondType());
            item.setTenderBondMoney(MyStringUtil.moneyToString(businessRecordDto.getTenderBondMoney()));
        }else {
            item.setTenderBondType(" ");
            item.setTenderBondMoney(" ");
        }

        item.setBusinessUser(businessRecordDto.getBusinessUser());
        item.setBusinessUserName(businessRecordDto.getBusinessUserName());
        item.setBusinessContent(businessRecordDto.getBusinessContent());
        item.setBidPlanOpen(businessRecordDto.getBidPlanOpen());
        item.setBidRealOpen(businessRecordDto.getBidRealOpen());
        item.setSecret(businessRecordDto.getSecret());
        item.setWin(businessRecordDto.getWin());
        item.setAttend(businessRecordDto.getAttend());
        item.setRemark(businessRecordDto.getRemark());
        item.setIndustryType(businessRecordDto.getIndustryType());

        item.setDeptName(businessRecordDto.getDeptName());
        item.setDeptId(businessRecordDto.getDeptId());
        item.setMessageDeptId(businessRecordDto.getMessageDeptId());
        item.setMessageDeptName(businessRecordDto.getMessageDeptName());
        item.setMessageUser(businessRecordDto.getMessageUser());
        item.setMessageUserName(businessRecordDto.getMessageUserName());

        item.setGmtModified(new Date());
        BeanValidator.check(item);
        Assert.state(!Strings.isNullOrEmpty(item.getDeptName()),"????????????????????????");
        List<String> deptChargeMen = selectEmployeeService.getDeptAllChargeMen(item.getDeptId());//??????+??????
        if(deptChargeMen.size()==0) throw  new IllegalArgumentException(hrDeptService.getModelById(item.getDeptId()).getName()+"???????????????????????????!");
        Map variables=Maps.newHashMap();
        variables.put("deptChargeMen", deptChargeMen);
        variables.put("processDescription", "??????????????????:"+item.getProjectName()+"("+item.getProjectNo()+")");
        myActService.setVariables(item.getProcessInstanceId(),variables);
        businessRecordMapper.updateByPrimaryKey(item);
    }

    public BusinessRecordDto getModelById(int id) {
        return getDto(businessRecordMapper.selectByPrimaryKey(id));
    }

    public BusinessRecord getNewModel(String userLogin,String uiSref) {
        List<String> businessContract = hrEmployeeService.selectUserByRoleNames("?????????????????????(??????)");
        Assert.state(businessContract.size()>0,"?????????????????? ???????????? ??????");
        List<String> businessLeader = selectEmployeeService.getDeptChargeMen(48);
        Assert.state(businessLeader.size()>0,"?????????????????????????????????");

        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        BusinessRecord item = new BusinessRecord();
        item.setCustomerId(0);
        HrEmployeeSysDto hrEmployeeDto = hrEmployeeSysService.getModelByUserLogin(userLogin);
        item.setProjectType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,uiSref.contains("five")?"??????????????????":"??????????????????").toString());
       /* item.setBusinessContent(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????").toString());*/
        item.setInvestSource(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????").toString());
        item.setTenderBondType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????").toString());

        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getHrEmployee().getUserName());
        item.setTenderBond(false);
        item.setWin(false);
        item.setAttend(false);
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);
        item.setProcessEnd(false);
        //??????????????????????????????????????? ??? 0
        item.setStatus("0");
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setIndustryType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????").toString());
        ModelUtil.setNotNullFields(item);
        businessRecordMapper.insert(item);

        String processKey=EdConst.FIVE_BUSINESS_RECORD;
        String businessKey=processKey+"_"+item.getId();
        Map variables = Maps.newHashMap();
        List<String> businessMen = selectEmployeeService.getBusinessMenByDeptId(item.getDeptId());
        if(businessMen.size()!=0){
            variables.put("businessMen",selectEmployeeService.getBusinessMenByDeptId(item.getDeptId()));//??????????????????
            variables.put("businessMenFlag",true);
        }else{
            variables.put("businessMenFlag",false);
        }
        variables.put("userLogin", userLogin);
        variables.put("businessContract", businessContract);
        variables.put("businessLeader", businessLeader);
        variables.put("deptChargeMen", selectEmployeeService.getDeptChargeMen(hrEmployeeDto.getDeptId()));
        variables.put("processDescription", "??????????????????");
        String processInstanceId = taskHandleService.startProcess(processKey, businessKey, variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);
        businessRecordMapper.updateByPrimaryKey(item);
        return item;
    }

    public BusinessRecordDto getDto(Object item) {
        BusinessRecord model= (BusinessRecord) item;
        BusinessRecordDto dto=BusinessRecordDto.adapt(model);
        if(dto.getCustomerId()!=null&&dto.getCustomerId()!=0){
            dto.setTaxNo(businessCustomerMapper.selectByPrimaryKey(dto.getCustomerId()).getTaxNo());
        }
        dto.setProcessName("?????????");
        if(!dto.getProcessEnd()&&StringUtils.isNotEmpty(dto.getProcessInstanceId())){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), "", "");

            if(customProcessInstance==null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                businessRecordMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance!=null&& StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }
        if(MyStringUtil.getStringListExcept0(dto.getContractReviewIds()).size()>0){
            dto.setReviewUse(true);
        }
        if(MyStringUtil.getStringListExcept0(dto.getPreContractIds()).size()>0){
            dto.setPreUse(true);
        }
        return dto;
    }


    public List<BusinessRecord> selectAll(String userLogin, String uiSref) {
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        //???????????????????????????????????????  myDeptData true?????????????????? false???????????????
        Map map =new HashMap();
        map.put("myDeptData",true);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));
        return  businessRecordMapper.selectAll(params);
    }


    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        HrEmployeeSysDto hrEmployeeSysDto = hrEmployeeSysService.getModelByUserLogin(userLogin);
        params.put("deleted",false);
        //??????????????????
        if(params.get("timeType").equals("??????????????????")){
            params.put("planSStartTime",params.get("startTime"));
            params.put("planSEndTime",params.get("endTime"));
        }else if(params.get("timeType").equals("??????????????????")) {
            params.put("planEStartTime",params.get("startTime"));
            params.put("planEEndTime",params.get("endTime"));
        }else if(params.get("timeType").equals("??????????????????")) {
            params.put("bidStartTime",params.get("startTime"));
            params.put("bidEndTime",params.get("endTime"));
        }else if(params.get("timeType").equals("????????????")) {
            params.put("createStartTime",params.get("startTime"));
            params.put("createEndTime",params.get("endTime"));
        }
        //???????????????????????????????????????  myDeptData true?????????????????? false???????????????
        Map map =new HashMap();
        map.put("myDeptData",true);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));
        //??????????????????deptIds
        int headDeptId = selectEmployeeService.getHeadDeptId(hrEmployeeSysDto.getDeptId());
        List<Integer> deptIds = hrDeptService.listChild(headDeptId).stream().map(HrDept::getId).collect(Collectors.toList());
        deptIds.add(headDeptId);

        if(!params.containsKey("deptIdList")){
            params.remove("deptId");
            params.put("deptIdList",deptIds);
        }else {
            deptIds.addAll((List<Integer>)params.get("deptIdList"));
            params.put("deptIdList",deptIds);
        }

        params.put("isLikeSelect",true);
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> businessRecordMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            BusinessRecord item=(BusinessRecord)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public PageInfo<Object> listPagedDataCommon(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        HrEmployeeSysDto hrEmployeeSysDto = hrEmployeeSysService.getModelByUserLogin(userLogin);
        params.put("deleted",false);

        //???????????????????????????????????????  myDeptData true?????????????????? false???????????????
        Map map =new HashMap();
        map.put("myDeptData",true);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));
        //??????????????????deptIds
        int headDeptId = selectEmployeeService.getHeadDeptId(hrEmployeeSysDto.getDeptId());
        List<Integer> deptIds = hrDeptService.listChild(headDeptId).stream().map(HrDept::getId).collect(Collectors.toList());
        deptIds.add(headDeptId);

        if(!params.containsKey("deptIdList")){
            params.remove("deptId");
            params.put("deptIdList",deptIds);
        }else {
            deptIds.addAll((List<Integer>)params.get("deptIdList"));
            params.put("deptIdList",deptIds);
        }

        params.put("isLikeSelect",true);
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> businessRecordMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            BusinessRecord item=(BusinessRecord)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }




    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        BusinessRecord item = businessRecordMapper.selectByPrimaryKey(id);
        data.put("customerName", item.getCustomerName());
        data.put("customerAddress", item.getCustomerAddress());
        data.put("linkMan", item.getLinkMan());
        data.put("linkTel", item.getLinkTel());
        data.put("linkTitle", item.getLinkTitle());
        data.put("projectName", item.getProjectName());
        data.put("projectAddress", item.getProjectAddress());
        data.put("projectScale", item.getProjectScale());
        data.put("businessContent", item.getBusinessContent());
        data.put("projectType", item.getProjectType());
        data.put("projectInvest", item.getProjectInvest());
        data.put("investSource", item.getInvestSource());
        data.put("tenderBond", item.getTenderBond());
        data.put("tenderBondType", item.getTenderBondType());
        data.put("tenderBondMoney", item.getTenderBondMoney());
        data.put("deptName", item.getDeptName());
        data.put("businessUserName", item.getBusinessUserName());
        data.put("bidPlanOpen", item.getBidPlanOpen());
        data.put("bidRealOpen", item.getBidRealOpen());
        data.put("win", item.getWin());
        data.put("attend", item.getAttend());
        data.put("gmtCreate", item.getGmtCreate());
        data.put("creatorName", item.getCreatorName());
        data.put("nodes", myHistoryService.listPassedHistoryDto(item.getProcessInstanceId()));
        return data;
    }

    public boolean isExist(String projectName){
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("projectName", projectName);
        if (PageHelper.count(()->businessRecordMapper.selectAll(params))==0){
            return false;
        }else {
            return true;
        }
    }

    public String insertByFile(InputStream inputStream, String userLogin) throws IOException {
        List<Map> list = MyPoiUtil.listTableData(inputStream, 1, 0, false);
        int updateNum=0;
        for(int i=1;i<=list.size();i++) {
            Map map = list.get(i - 1);
            BusinessRecord item = new BusinessRecord();
            Map params = Maps.newHashMap();
            params.put("customerName", map.get(0).toString());
            if (StringUtils.isNotEmpty(map.get(5).toString())){
                params.put("projectName", map.get(5).toString());
            }
            if (PageHelper.count(() -> businessRecordMapper.selectAll(params)) > 0) {
                item = businessRecordMapper.selectAll(params).get(0);
                updateNum++;
            }
            if (StringUtils.isNotEmpty(map.get(0).toString())) {
                item.setCustomerName(map.get(0).toString());
            }
            if (StringUtils.isNotEmpty(map.get(1).toString())) {
                item.setCustomerAddress(map.get(1).toString());
            }
            if (StringUtils.isNotEmpty(map.get(2).toString())) {
                item.setLinkMan(map.get(2).toString());
            }
            if (StringUtils.isNotEmpty(map.get(3).toString())) {
                item.setLinkTel(map.get(3).toString());
            }
            if (StringUtils.isNotEmpty(map.get(4).toString())) {

                item.setLinkTitle(map.get(4).toString());
            }
            if (StringUtils.isNotEmpty(map.get(5).toString())) {
                item.setProjectName(map.get(5).toString());
            }
            if (StringUtils.isNotEmpty(map.get(6).toString())) {
                item.setProjectAddress(map.get(6).toString());
            }
            if (StringUtils.isNotEmpty(map.get(7).toString())) {
                item.setProjectScale(map.get(7).toString());
            }
            if (StringUtils.isNotEmpty(map.get(8).toString())) {
                item.setBusinessContent(map.get(8).toString());
            }
            if (StringUtils.isNotEmpty(map.get(9).toString())) {
                item.setProjectType(map.get(9).toString());
            }
            if (StringUtils.isNotEmpty(map.get(10).toString())) {
                item.setInvestSource(map.get(10).toString());
            }
            if (StringUtils.isNotEmpty(map.get(11).toString())) {
                item.setProjectInvest(map.get(11).toString());
            }
            if (StringUtils.isNotEmpty(map.get(12).toString())) {
                String username = map.get(12).toString().replaceAll("???", ",");
                item.setBusinessUserName(username);
                item.setBusinessUser(hrEmployeeSysService.selectByUserName(username));
            }
            if (StringUtils.isNotEmpty(map.get(13).toString())) {
                item.setDeptName(map.get(13).toString());
                if (hrDeptService.selectByName(map.get(13).toString()) != null) {
                    item.setDeptId(hrDeptService.selectByName(map.get(13).toString()).getId());
                }
            }
            if (StringUtils.isNotEmpty(map.get(14).toString())) {
                if (map.get(14).equals("???")) {
                    item.setAttend(true);
                } else {
                    item.setAttend(false);
                }
            }
            if (StringUtils.isNotEmpty(map.get(15).toString())) {
                if (map.get(15).equals("???")) {
                    item.setWin(true);
                } else {
                    item.setWin(false);
                }
            }
            if (StringUtils.isNotEmpty(map.get(16).toString())) {
                item.setBidPlanOpen(map.get(16).toString());
            }
            if (StringUtils.isNotEmpty(map.get(17).toString())) {
                item.setBidRealOpen(map.get(17).toString());
            }
            if (StringUtils.isNotEmpty(map.get(18).toString())) {
                if (map.get(18).equals("???")) {
                    item.setTenderBond(true);
                } else {
                    item.setTenderBond(false);
                }
            }
            if (StringUtils.isNotEmpty(map.get(19).toString())) {
                item.setTenderBondMoney(map.get(19).toString());
            }
            if (StringUtils.isNotEmpty(map.get(20).toString())) {
                item.setTenderBondType(map.get(20).toString());
            }
            if (StringUtils.isNotEmpty(map.get(21).toString())) {
                item.setRemark(map.get(21).toString());
            }
            item.setDeleted(false);
            item.setGmtModified(new Date());
            if (item.getId() == null) {
                item.setCreator(userLogin);
                item.setCreatorName(hrEmployeeSysService.getModelByUserLogin(userLogin).getHrEmployee().getUserName());
                item.setGmtCreate(new Date());
                item.setProcessEnd(true);
                item.setProcessInstanceId("");
                item.setId(0);
                ModelUtil.setNotNullFields(item);
                try {
                    BeanValidator.check(item);
                }catch (ParamException e){
                    throw new ParamException("???"+(i+1)+"??????????????????"+e.getMessage());
                }
                businessRecordMapper.insert(item);
            } else {
                businessRecordMapper.updateByPrimaryKey(item);
            }

        }
        return  updateNum+","+(list.size()-updateNum);
    }

    /**
     * ?????? ??????????????????????????????  ?????????
     * @param userLogin
     * @return
     */
    public List<BusinessRecordDto> listRecordByUserLogin(String userLogin,int recordId){
        HrEmployeeSysDto hrEmployeeSysDto = hrEmployeeSysService.getModelByUserLogin(userLogin);
        List<BusinessRecordDto> dtos = new ArrayList<>();
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        //????????????????????????
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,"five.businessRecord");
        if (deptIdList.size()==0){
           //??????????????????deptIds
            int headDeptId = selectEmployeeService.getHeadDeptId(hrEmployeeSysDto.getDeptId());
            List<Integer> deptIds = hrDeptService.listChild(headDeptId).stream().map(HrDept::getId).collect(Collectors.toList());
            deptIds.add(headDeptId);
            params.put("deptIdList",deptIds);
        }else {
            params.put("deptIdList",deptIdList);
        }

      /*  //???????????????????????????????????????  myDeptData true?????????????????? false???????????????
        Map map =new HashMap();
        map.put("myDeptData",true);
        map.put("uiSref","five.businessRecord");
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));*/

        List<BusinessRecord> businessRecords = businessRecordMapper.selectAll(params);
        for(BusinessRecord businessRecord :businessRecords){
            BusinessRecordDto dto =getDto(businessRecord);
           /* if(dto.getProcessEnd()&&dto.getContractReviewId()==0&&dto.getPreContractId()==0){
                dtos.add(dto);
            }*/
            if(dto.getProcessEnd()){
                dtos.add(dto);
            }
        }
       /* if(recordId!=0){
            BusinessRecord businessRecord = businessRecordMapper.selectByPrimaryKey(recordId);
            dtos.add(getDto(businessRecord));
        }*/

        return dtos;
    }

    public int addCustomer(String recordId, String userLogin) {
        BusinessRecordDto item = getModelById(Integer.parseInt(recordId));
        Map map =new HashMap();
        map.put("name",item.getCustomerName());
        BusinessCustomer bc;
        List<BusinessCustomer> businessCustomers = businessCustomerMapper.selectAll(map);
        if(businessCustomers.size()>0){
            bc = businessCustomers.get(0);
            bc.setCode(item.getCustomerCode());
            bc.setName(item.getCustomerName());
            bc.setAddress(item.getCustomerAddress());
            bc.setLinkMan(item.getLinkMan());
            bc.setLinkTitle(item.getLinkTitle());
            bc.setLinkTel(item.getLinkTel());
            bc.setGmtModified(new Date());
            businessCustomerMapper.updateByPrimaryKey(bc);
        }else{
            bc = new BusinessCustomer();
            bc.setCode(item.getCustomerCode());
            bc.setName(item.getCustomerName());
            bc.setAddress(item.getCustomerAddress());
            bc.setLinkMan(item.getLinkMan());
            bc.setLinkTitle(item.getLinkTitle());
            bc.setLinkTel(item.getLinkTel());

            HrEmployeeSysDto hrEmployeeDto = hrEmployeeSysService.getModelByUserLogin(userLogin);
            bc.setDeptId(hrEmployeeDto.getDeptId());
            bc.setDeptName(hrEmployeeDto.getDeptName());
            bc.setCreator(userLogin);
            bc.setCreatorName(hrEmployeeDto.getHrEmployee().getUserName());
            bc.setGmtCreate(new Date());
            bc.setGmtModified(new Date());
            ModelUtil.setNotNullFields(bc);
            businessCustomerMapper.insert(bc);

            bc.setBusinessKey(EdConst.FIVE_BUSINESS_CUSTOMER+"_" + item.getId());
            Map variables = Maps.newHashMap();
            variables.put("userLogin", userLogin);
            variables.put("processDescription", "??????????????????");
            variables.put("businessDevelopmentUser",selectEmployeeService.getUserListByRoleName("?????????????????????(??????)"));//?????????????????????????????????
            String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_BUSINESS_CUSTOMER,bc.getBusinessKey(), variables, MccConst.APP_CODE);
            bc.setProcessInstanceId(processInstanceId);
            businessCustomerMapper.updateByPrimaryKey(bc);
        }

        return bc.getId();
    }
    /**
     * XX ??????????????????2017?????????17???2018?????????18??????????????????
     * XX ????????????????????????
     * X ?????????????????? ???????????? S ???????????? Z ???????????? C ?????? K ?????? J ????????? W
     * XXX  ?????????????????????????????????????????????001??????
     * @param id
     */
    public String getProjectNo(int id){
        try {
            BusinessRecordDto recordDto = getModelById(id);
            // BusinessContractDto businessContractDto = businessContractService.getModelById(Integer.valueOf(businessPreContractDto.getContractId()));
            String newProjectNo = "";
            //???????????? 2019???  19
            String signYear = recordDto.getPlanBeginTime().split("-")[0];
            String year = signYear.substring(2);
            //?????????????????? 2??? 01
            String deptCode= hrDeptService.getModelById(recordDto.getDeptId()).getDeptCode();
            //??????????????????
            String typeCode="";

            if(recordDto.getProjectType().equals("????????????")){
                typeCode="S";
            }else if(recordDto.getProjectType().equals("????????????")){
                typeCode="Z";
            }else if(recordDto.getProjectType().equals("????????????")){
                typeCode="C";
            } else if(recordDto.getProjectType().equals("??????")){
                typeCode="K";
            } else if(recordDto.getProjectType().equals("??????")){
                typeCode="J";
            } else if(recordDto.getProjectType().equals("?????????")){
                typeCode="W";
            }
            //??????  ?????? ?????? ???????????? ???????????? ??????????????????
            /*if (recordDto.getProjectNo().contains(year+deptCode+typeCode)){
                return recordDto.getProjectNo();
            }*/

            //?????? 001
            Map<String, Object> params=Maps.newHashMap();
            params.put("deleted",false);
            params.put("projectNoHead",year+deptCode+typeCode);
            List<BusinessRecord> businessRecords = businessRecordMapper.selectAll(params);
            int size=0;
            //???????????????????????????
            if (!businessRecords.isEmpty()){
                for (BusinessRecord businessRecord:businessRecords){
                    if(businessRecord.getId()!=id&&StringUtils.isNotEmpty(businessRecord.getProjectNo())){
                        String maxSize=  businessRecord.getProjectNo().substring(5);
                        if (size<Integer.parseInt(maxSize)){
                            size = Integer.parseInt(maxSize);
                        }
                    }
                }
            }
            size=size+1;
            String format = String.format("%03d", size);
            newProjectNo=newProjectNo+year+deptCode+typeCode+format;

            recordDto.setProjectNo(newProjectNo);
            update(recordDto);

            return newProjectNo;

        }catch (Exception e){
            Assert.state(false, "?????????????????????????????????????????????????????????????????????");
            return "";
        }


    }

    public List<FiveBusinessContractReview> selectReview(int recordId, String userLogin) {
        BusinessRecordDto recordDto = getModelById(recordId);
        List<FiveBusinessContractReview> list = new ArrayList<>();
        List<String> reviewIds = MyStringUtil.getStringListExcept0(recordDto.getContractReviewIds());
        for(String reviewId:reviewIds){
            FiveBusinessContractReviewDto reviewDto = fiveBusinessContractReviewService.getModelById(Integer.valueOf(reviewId));
            list.add(reviewDto);
        }
        return  list;
    }
    public List<BusinessPreContract> selectPre(int recordId, String userLogin) {
        BusinessRecordDto recordDto = getModelById(recordId);
        List<BusinessPreContract> list = new ArrayList<>();
        List<String> preIds = MyStringUtil.getStringListExcept0(recordDto.getPreContractIds());
        for(String preId:preIds){
            BusinessPreContractDto preContractDto = businessPreContractService.getModelById(Integer.valueOf(preId));
            list.add(preContractDto);
        }
        return  list;
    }
}
