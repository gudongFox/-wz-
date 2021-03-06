package com.cmcu.mcc.five.service;


import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.dto.CommonCodeDto;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.service.CommonFileService;
import com.cmcu.common.util.*;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.dto.ActHistoryDto;
import com.cmcu.mcc.five.dao.FiveAssetComputerMapper;
import com.cmcu.mcc.five.dao.FiveOaInformationEquipmentExamineListDetailMapper;
import com.cmcu.mcc.five.dao.FiveOaInformationEquipmentExamineListMapper;
import com.cmcu.mcc.five.dto.FiveOaInformationEquipmentApplyDto;
import com.cmcu.mcc.five.dto.FiveOaInformationEquipmentExamineDto;
import com.cmcu.mcc.five.dto.FiveOaInformationEquipmentExamineListDto;
import com.cmcu.mcc.five.entity.*;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FiveOaInformationEquipmentExamineListService extends BaseService {

    @Resource
    FiveOaInformationEquipmentExamineListMapper fiveOaInformationEquipmentExamineListMapper;
    @Resource
    FiveOaInformationEquipmentExamineListDetailMapper fiveOaInformationEquipmentExamineListDetailMapper;
    @Resource
    HrEmployeeMapper hrEmployeeMapper;

    @Autowired
    HrEmployeeService hrEmployeeService;
    @Autowired
    HrEmployeeSysService hrEmployeeSysService;

    @Autowired
    MyActService myActService;
    @Autowired
    ProcessQueryService processQueryService;

    @Autowired
    SelectEmployeeService selectEmployeeService;
    @Autowired
    TaskHandleService taskHandleService;
    @Autowired
    ActService actService;
    @Resource
    HandleFormService handleFormService;
    @Resource
    FiveOaInformationEquipmentApplyService fiveOaInformationEquipmentApplyService;
    @Resource
    CommonFileService commonFileService;
   //????????????
    public void remove(int id,String userLogin){
        FiveOaInformationEquipmentExamineList item = fiveOaInformationEquipmentExamineListMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"??????????????????"+item.getCreatorName()+"??????");
        //????????????
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    public void update(FiveOaInformationEquipmentExamineListDto item){

        FiveOaInformationEquipmentExamineList model = fiveOaInformationEquipmentExamineListMapper.selectByPrimaryKey(item.getId());

        model.setFormNo(item.getFormNo());
        model.setDeptId(item.getDeptId());
        model.setDiskNo(item.getDiskNo());
        model.setCheckPrice(item.getCheckPrice());
        model.setRemark(item.getRemark());
        model.setGmtModified(new Date());

        Map variables = Maps.newHashMap();
        variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDeptId()));//?????????????????????
        variables.put("otherDeptChargeMan", selectEmployeeService.getOtherDeptChargeMan(item.getDeptId()));//??????????????????
        variables.put("processDescription", "?????????????????????????????????????????????:"+model.getDeptName());
        List<FiveOaInformationEquipmentExamineListDetail> listDetails = listDetail(model.getId());
        variables.put("flag",false);
        variables.put("sign",false);
        for (FiveOaInformationEquipmentExamineListDetail detail:listDetails){
            if (StringUtils.isEmpty(detail.getCheckPrice()))continue;
            if (Double.valueOf(detail.getCheckPrice())>=5000){
                variables.put("sign",true);
                //??????:????????????????????????,???????????????
                variables.put("copyMen", MyStringUtil.listToString(selectEmployeeService.getFinanceChargeMen(item.getDeptId()))+",2589");
            }else {
                variables.put("flag",true);
                variables.put("financeMan",selectEmployeeService.getFinanceChargeMen(item.getDeptId()));
            }
        }



        myActService.setVariables(model.getProcessInstanceId(),variables);
        //????????????
        BeanValidator.check(model);
        fiveOaInformationEquipmentExamineListMapper.updateByPrimaryKey(model);


    }

    public FiveOaInformationEquipmentExamineListDto getModelById(int id){
        return getDto(fiveOaInformationEquipmentExamineListMapper.selectByPrimaryKey(id));
    }

    public FiveOaInformationEquipmentExamineListDto getDto(FiveOaInformationEquipmentExamineList item) {
        FiveOaInformationEquipmentExamineListDto dto=FiveOaInformationEquipmentExamineListDto.adapt(item);
        dto.setProcessName("?????????");
        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if(customProcessInstance ==null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaInformationEquipmentExamineListMapper.updateByPrimaryKey(dto);
            }

            if(customProcessInstance!=null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }


        if (dto.getDiskNo()!=null){
            dto.setEquipmentApply(fiveOaInformationEquipmentApplyService.getModelByNo(dto.getDiskNo()));
        }


        //???????????????
        String totalFinalPrice= MyStringUtil.moneyToString("0");
        for (FiveOaInformationEquipmentExamineListDetail detail:listDetail(item.getId())){
            totalFinalPrice = MyStringUtil.getNewAddMoney(totalFinalPrice,detail.getCheckPrice(),2);
        }
        dto.setCheckPrice(totalFinalPrice);
        return dto;
    }

    public int getNewModel(String userLogin){
        FiveOaInformationEquipmentExamineList item=new FiveOaInformationEquipmentExamineList();

        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());

        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());

        item.setDeleted(false);
        item.setProcessEnd(false);//??????????????????

        item.setGmtCreate(new Date());//
        item.setGmtModified(new Date());

        ModelUtil.setNotNullFields(item);
        fiveOaInformationEquipmentExamineListMapper.insert(item);

        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDeptId()));
        variables.put("processDescription","?????????????????????(??????)??????:"+item.getDeptName());
        variables.put("administrativeDeptEquipment", selectEmployeeService.getUserListByRoleName("?????????????????????(????????????)"));//???????????????????????????????????????
        variables.put("financeDeptChargeMan", selectEmployeeService.getParentDeptChargeMen(18,1,false));//????????????????????????
        variables.put("administrativeDeptChargeMan", selectEmployeeService.getParentDeptChargeMen(67,1,false));//????????????????????????
        variables.put("qualityDeptChargeMen",selectEmployeeService.getParentDeptChargeMen(11,1,false));//????????????????????????????????????


        String businessKey= EdConst.FIVE_OA_INFORMATIONEQUIPMENTEXAMINE_LIST+ "_"+item.getId();

        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_INFORMATIONEQUIPMENTEXAMINE_LIST, businessKey , variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);

        fiveOaInformationEquipmentExamineListMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref, int pageNum, int pageSize) {
        params.put("deleted",false);
        params.put("isLikeSelect",true);

        //???????????????????????????????????????  myDeptData true?????????????????? false???????????????
        Map map =new HashMap();
        map.put("myDeptData",false);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaInformationEquipmentExamineListMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaInformationEquipmentExamineList item=(FiveOaInformationEquipmentExamineList)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

     //??????
    public void removeDetail(int id){
        FiveOaInformationEquipmentExamineListDetail model = fiveOaInformationEquipmentExamineListDetailMapper.selectByPrimaryKey(id);

        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveOaInformationEquipmentExamineListDetailMapper.updateByPrimaryKey(model);
    }

    public void updateDetail(FiveOaInformationEquipmentExamineListDetail item){
        FiveOaInformationEquipmentExamineListDetail model;
        if (item.getId()==0){
            model =new  FiveOaInformationEquipmentExamineListDetail();
        }else {
             model = fiveOaInformationEquipmentExamineListDetailMapper.selectByPrimaryKey(item.getId());
        }
        model.setListId(item.getListId());
        model.setDeptId(item.getDeptId());
        model.setDeptName(item.getDeptName());
        model.setFormNo(item.getFormNo());
        model.setAcceptTime(item.getAcceptTime());
        model.setEquipmentNo(item.getEquipmentNo());
        model.setChargeMan(item.getChargeMan());
        model.setChargeManName(item.getChargeManName());
        model.setChargeManNo(item.getChargeManNo());
        model.setUser(item.getUser());
        model.setUserName(item.getUserName());
        model.setEquipmentName(item.getEquipmentName());
        model.setBrand(item.getBrand());
        model.setType(item.getType());
        model.setSecretRank(item.getSecretRank());
        model.setUseType(item.getUseType());
        model.setAddress(item.getAddress());
        model.setOsVersion(item.getOsVersion());
        model.setOsInstallTime(item.getOsInstallTime());
        model.setMacAddress(item.getMacAddress());
        model.setNetType(item.getNetType());
        model.setIpAddress(item.getIpAddress());
        model.setDisplayNo(item.getDisplayNo());
        model.setDriveType(item.getDriveType());
        model.setUsbType(item.getUsbType());
        model.setStartTime(item.getStartTime());
        model.setUseCondition(item.getUseCondition());
        model.setCheckPrice(MyStringUtil.moneyToString(item.getCheckPrice(),2));
        model.setFixedAssetNo(item.getFixedAssetNo());
        model.setExamineMan(item.getExamineMan());
        model.setExamineManName(item.getExamineManName());
        model.setExamineComment(item.getExamineComment());
        model.setAffairComment(item.getAffairComment());
        model.setTechnologyComment(item.getTechnologyComment());
        model.setUser(item.getUser());
        model.setUserName(item.getUserName());
        model.setGmtModified(new Date());
        if (item.getId()==0){
            model.setGmtCreate(new Date());

            ModelUtil.setNotNullFields(model);
            fiveOaInformationEquipmentExamineListDetailMapper.insert(model);
            model.setBusinessKey("informationEquipmentExamineListDetail_"+model.getId());
        }
        //?????????????????? ???????????????
        if( commonFileService.listData(model.getBusinessKey(),0,"").size()>0){
            model.setUploadfile(true);
        }else {
            model.setUploadfile(false);
        }
        fiveOaInformationEquipmentExamineListDetailMapper.updateByPrimaryKey(model);
    }

    public FiveOaInformationEquipmentExamineListDetail  getNewModelDetail(int  id){
        FiveOaInformationEquipmentExamineListDetail item=new FiveOaInformationEquipmentExamineListDetail();
        item.setListId(id+"");
        item.setId(0);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);
        item.setCheckPrice("0");
        ModelUtil.setNotNullFields(item);
       // fiveOaInformationEquipmentExamineListDetailMapper.insert(item);
        return item;

    }

    public FiveOaInformationEquipmentExamineListDetail getModelDetailById(int id){
        return fiveOaInformationEquipmentExamineListDetailMapper.selectByPrimaryKey(id);
    }

    public List<FiveOaInformationEquipmentExamineListDetail> listDetail(int id){
        Map <String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("listId",id);
        List<FiveOaInformationEquipmentExamineListDetail> list = fiveOaInformationEquipmentExamineListDetailMapper.selectAll(params);
        return list;
    }


    /**
     * ?????????????????? ???????????????????????????
     * @param detailId
     */
    public void getNewDetailByApplyDetail(int detailId,int listId,String userLogin){
        FiveOaInformationEquipmentApplyDetail applyDetail = fiveOaInformationEquipmentApplyService.getModelDetailById(detailId);

        FiveOaInformationEquipmentExamineListDetail detail=new FiveOaInformationEquipmentExamineListDetail();
        detail.setListId(listId+"");
        detail.setEquipmentName(applyDetail.getEquipmentName());
        detail.setDeptId(applyDetail.getDeptId());
        detail.setDeptName(applyDetail.getDeptName());
        detail.setBrand(applyDetail.getBrand());
        detail.setCheckPrice(applyDetail.getPrice());
         detail.setRemark(applyDetail.getOtherRequirement());//????????????????????????
        detail.setCreator(userLogin);
        detail.setCreatorName(selectEmployeeService.getNameByUserLogin(userLogin));
        detail.setGmtCreate(new Date());
        detail.setGmtModified(new Date());
        detail.setDeleted(false);
        ModelUtil.setNotNullFields(detail);
        fiveOaInformationEquipmentExamineListDetailMapper.insert(detail);
        detail.setBusinessKey("informationEquipmentExamineListDetail_"+detail.getId());
        fiveOaInformationEquipmentExamineListDetailMapper.updateByPrimaryKey(detail);

    }

    @Resource
    FiveAssetComputerService fiveAssetComputerService;
    @Resource
    FiveAssetComputerMapper fiveAssetComputerMapper;

    /*????????????????????? ??????????????????????????????*/
    public void getEquipmentExamineNo(int id){
        FiveOaInformationEquipmentExamineListDetail modelById = getModelDetailById(id);
        CommonCodeDto commonCodeDto = commonCodeService.getDataByCatalogAndName(MccConst.APP_CODE, "?????????????????????", modelById.getOsInstallTime());
        String eqNo=commonCodeDto.getCode();
        int year=MyDateUtil.getYear();
        Map map=Maps.newHashMap();
        map.put("deleted",false);
        map.put("equipmentType",modelById.getOsInstallTime());//????????????
        map.put("formNoNull",eqNo+year);//?????? 012020001
        List<FiveAssetComputer> list= fiveAssetComputerMapper.selectAll(map);
        int seq=0;
        if(list.size()>0){
            list.sort(Comparator.comparing(FiveAssetComputer::getComputerNo));
            String formNo = list.get(list.size()-1).getComputerNo();
            String substring = formNo.substring(formNo.length() - 3);
            seq = Integer.parseInt(substring);
        }
        DecimalFormat mFormat = new DecimalFormat("000");
        String format = mFormat.format(seq+1);
        String computerNo=eqNo+year+format;
        //????????????????????????????????? ?????? ??????????????????????????????
        if (!StringUtils.isNotBlank(modelById.getFormNo())){//null  "" " "????????????????????????
            modelById.setFormNo(computerNo);//????????????
            modelById.setGmtModified(new Date());
            fiveOaInformationEquipmentExamineListDetailMapper.updateByPrimaryKey(modelById);
        }
        //???????????? ??????  ????????????????????????
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("computerNo",modelById.getFormNo());
        if (fiveAssetComputerMapper.selectAll(params).size()==0){
            if (!modelById.getSecretRank().equals("??????")){
                int computer = fiveAssetComputerService.getModelByEquipmentExamine(id,false);
            }
        }
    }

    //??????
    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
    /*    FiveOaInformationEquipmentApply item = fiveOaInformationEquipmentApplyMapper.selectByPrimaryKey(id);
        data.put("deptName",item.getDeptName());
        data.put("applyTime",item.getApplyTime());
        data.put("linkManName",item.getLinkManName());
        data.put("linkManPhone",item.getLinkManPhone());
        if (item.getPlan()){
            data.put("plan","???");
        }else {
            data.put("plan","???");
        }

        data.put("equipmentUse",item.getEquipmentUse());
        data.put("totalMoney",item.getTotalMoney());

        data.put("creatorName",item.getCreatorName());
        data.put("gmtCreate",item.getGmtCreate());

        Map map =new HashMap();
        map.put("informationEquipmentApplyId",item.getId());
        map.put("deleted",false);
        List<FiveOaInformationEquipmentApplyDetail> informationEquipmentApplyDetails = fiveOaInformationEquipmentApplyDetailMapper.selectAll (map);
        data.put("informationEquipmentApplyDetails",informationEquipmentApplyDetails);



        List<ActHistoryDto> actHistoryDtos = actService.listPassedHistoryDto(item.getProcessInstanceId());
        for (ActHistoryDto dto:actHistoryDtos){
            if(dto.getActivityName()==null){
                break;
            }
            if ("????????????-??????".equals(dto.getActivityName())){
                data.put("deptCharge",dto);
            }
            if ("????????????????????????".equals(dto.getActivityName())){
                data.put("financeDept",dto);
            }
            if ("???????????????????????????-??????".equals(dto.getActivityName())){
                data.put("technologyDept",dto);
            }
            if ("????????????????????????".equals(dto.getActivityName())){
                data.put("affairs",dto);
            }
            if (dto.getActivityName().contains("????????????")){
                data.put("equipmentDept",dto);
            }
        }
        data.put("nodes",actHistoryDtos);*/

        return data;
    }

    /**
     * ??????excl
     * @param startTime1 ????????????
     * @param endTime1 ????????????
     * @return
     */
    public List<Map> listMapData(Map<String,Object> params,  String uiSref,String startTime1 ,String endTime1,String userLogin){
        List<Map> list = new ArrayList<>();
        //??????????????????
        params.put("isLikeSelect",true);
        //??????????????????
        Map head=Maps.newHashMap();
        head.put("myDeptData",false);
        head.put("uiSref",uiSref);
        head.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(head));
        params.remove("userLogin");//???????????????userLogin ??????????????????
        //????????? ???????????????
        params.put("deleted",false);
        params.put("processEnd",true);
        //???????????????
        //params.put("startTime1",startTime1);
        //params.put("endTime1",endTime1);

        //int i = 0;
        List<FiveOaInformationEquipmentExamineList> fiveOaInformationEquipmentExamineLists=fiveOaInformationEquipmentExamineListMapper.selectAll(params);
        for (FiveOaInformationEquipmentExamineList dto:fiveOaInformationEquipmentExamineLists){
            Map map=new LinkedHashMap();
            map.put("????????????",dto.getDeptName());
            map.put("??????????????????",dto.getDiskNo());
            map.put("?????????????????????",dto.getCheckPrice());
            map.put("??????",dto.getRemark());
            map.put("?????????",dto.getCreatorName());
            map.put("????????????",MyDateUtil.dateToStr(dto.getGmtCreate()));
            list.add(map);
        }
        return list;
    }
}
