package com.cmcu.mcc.five.service;


import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.dto.CommonCodeDto;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.service.CommonDirService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyDateUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.dto.ActHistoryDto;
import com.cmcu.mcc.five.dao.FiveAssetComputerMapper;
import com.cmcu.mcc.five.dao.FiveOaInformationEquipmentExamineMapper;
import com.cmcu.mcc.five.dto.FiveOaInformationEquipmentExamineDto;
import com.cmcu.mcc.five.entity.FiveAssetComputer;
import com.cmcu.mcc.five.entity.FiveOaInformationEquipmentExamine;
import com.cmcu.mcc.five.entity.FiveOaSignQuote;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class FiveOaInformationEquipmentExamineService extends BaseService {

    @Resource
    FiveOaInformationEquipmentExamineMapper fiveOaInformationEquipmentExamineMapper;
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
    ActService actService;
    @Resource
    HandleFormService handleFormService;
    @Resource
    FiveOaInformationEquipmentApplyService fiveOaInformationEquipmentApplyService;
    @Resource
    FiveAssetComputerMapper fiveAssetComputerMapper;
    @Resource
    CommonDirService commonDirService;

    public void remove(int id,String userLogin){
       FiveOaInformationEquipmentExamine item = fiveOaInformationEquipmentExamineMapper.selectByPrimaryKey(id);
       Assert.state(item.getCreator().equals(userLogin),"??????????????????"+item.getCreatorName()+"??????");

       //????????????
       handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

   }

    public void update(FiveOaInformationEquipmentExamineDto item){
       FiveOaInformationEquipmentExamine model = fiveOaInformationEquipmentExamineMapper.selectByPrimaryKey(item.getId());
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
       model.setDiskNo(item.getDiskNo());
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

       model.setRemark(item.getRemark());
       model.setGmtModified(new Date());
       BeanValidator.validateObject(model);


      //???????????????????????????????????????????????? ???????????????????????????
       if (!Strings.isNullOrEmpty(model.getEquipmentNo())&&Strings.isNullOrEmpty(model.getFormNo())&&!model.getEquipmentNo().equals("???")){
           ModelUtil.setNotNullFields(model);
           Map map=Maps.newHashMap();
           map.put("deleted",false);
           map.put("equipmentNo",model.getEquipmentNo());
           Assert.state(fiveOaInformationEquipmentExamineMapper.selectAll(map).size()<=1,"???????????????????????????????????????????????????,??????????????????"+model.getEquipmentNo()+"");
           Assert.state(fiveAssetComputerMapper.selectAll(map).size()<=0,"?????????????????????,?????????????????????"+model.getEquipmentNo()+"?????????????????????????????????");

       }
        Map variables = Maps.newHashMap();
      if (item.getDeptId()!=model.getId()){
          variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(model.getDeptId()));
          model.setDeptName(item.getDeptName());
          model.setDeptId(item.getDeptId());
      }

      if (!Strings.isNullOrEmpty(model.getCheckPrice())){
          Double checkPrice= Double.valueOf(model.getCheckPrice());
          if (checkPrice<5000){
              variables.put("sign",0);
              variables.put("financeMan",selectEmployeeService.getFinanceChargeMen(item.getDeptId()));
          }else {
              variables.put("sign",1);
              //??????:???????????????
              variables.put("copyMen", MyStringUtil.listToString(selectEmployeeService.getFinanceChargeMen(item.getDeptId())));
          }
      }

       fiveOaInformationEquipmentExamineMapper.updateByPrimaryKey(model);
       variables.put("processDescription","??????????????????????????? "+item.getEquipmentName());
       BeanValidator.check(model);
       myActService.setVariables(model.getProcessInstanceId(),variables);


   }

    public FiveOaInformationEquipmentExamineDto getModelById(int id){

           return getDto(fiveOaInformationEquipmentExamineMapper.selectByPrimaryKey(id));
    }

    public FiveOaInformationEquipmentExamineDto getDto(FiveOaInformationEquipmentExamine item) {
        FiveOaInformationEquipmentExamineDto dto=FiveOaInformationEquipmentExamineDto.adapt(item);
        dto.setProcessName("?????????");

        if (!"".equals(item.getDiskNo())){
            dto.setEquipmentApply(fiveOaInformationEquipmentApplyService.getModelByNo(item.getDiskNo()));
        }

        if (!dto.getProcessEnd()) {
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if(customProcessInstance ==null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaInformationEquipmentExamineMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance!=null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }

        return dto;
    }

    public int getNewModel(String userLogin){
       FiveOaInformationEquipmentExamine item=new FiveOaInformationEquipmentExamine();
       HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

       item.setDeptId(hrEmployeeDto.getDeptId());
       item.setDeptName(hrEmployeeDto.getDeptName());
       item.setCreator(hrEmployeeDto.getUserLogin());
       item.setCreatorName(hrEmployeeDto.getUserName());

       item.setDeleted(false);
       item.setProcessEnd(false);
       item.setCreator(userLogin);
       item.setGmtModified(new Date());
       item.setGmtCreate(new Date());
       item.setUseType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????").toString());
       item.setUsbType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"usb??????").toString());
       ModelUtil.setNotNullFields(item);
       fiveOaInformationEquipmentExamineMapper.insert(item);

       String businessKey= EdConst.FIVE_OA_INFORMATIONEQUIPMENTEXAMINE+ "_" + item.getId();
       Map variables = Maps.newHashMap();
       variables.put("userLogin", userLogin);
       variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDeptId()));
       variables.put("processDescription","??????????????????????????? ");
       variables.put("administrativeDeptEquipment", selectEmployeeService.getUserListByRoleName("?????????????????????(????????????)"));//???????????????????????????????????????
       variables.put("financeDeptChargeMan", selectEmployeeService.getParentDeptChargeMen(18,1,false));//????????????????????????
       variables.put("administrativeDeptChargeMan", selectEmployeeService.getParentDeptChargeMen(67,1,false));//????????????????????????
       variables.put("qualityDeptChargeMen",selectEmployeeService.getParentDeptChargeMen(11,1,false));//????????????????????????????????????

       String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_INFORMATIONEQUIPMENTEXAMINE,businessKey, variables, MccConst.APP_CODE);
       item.setProcessInstanceId(processInstanceId);
       item.setBusinessKey(businessKey);
       fiveOaInformationEquipmentExamineMapper.updateByPrimaryKey(item);

        //?????????????????????
        commonDirService.newDir(businessKey,"??????",0,userLogin);
        commonDirService.newDir(businessKey,"????????????",0,userLogin);

        return item.getId();
   }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
        params.put("isLikeSelect",true);

        //???????????????????????????????????????  myDeptData true?????????????????? false???????????????
        Map map =new HashMap();
        map.put("myDeptData",false);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));


        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaInformationEquipmentExamineMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaInformationEquipmentExamine item=(FiveOaInformationEquipmentExamine)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaInformationEquipmentExamine item = fiveOaInformationEquipmentExamineMapper.selectByPrimaryKey(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String number = sdf.format(new Date())+item.getCreator().substring((item.getCreator()).length() - 4);
        data.put("tableNo",number);
        data.put("deptName",item.getDeptName());
        data.put("equipmentName",item.getEquipmentName());
        data.put("equipmentNo",item.getEquipmentNo());
        data.put("chargeManName",item.getChargeManName());
        data.put("chargeMan",item.getChargeMan());
        data.put("chargeManNo",item.getChargeManNo());
        data.put("acceptTime",item.getAcceptTime());
        data.put("userName",item.getUserName());
        data.put("brand",item.getBrand());

        data.put("type",item.getType());
        data.put("secretRank",item.getSecretRank());
        data.put("useType",item.getUseType());
        data.put("address",item.getAddress());
        data.put("osVersion",item.getOsVersion());
        data.put("osInstallTime",item.getOsInstallTime());
        data.put("diskNo",item.getDiskNo());

        data.put("macAddress",item.getMacAddress());
        data.put("netType",item.getNetType());
        data.put("ipAddress",item.getIpAddress());
        data.put("displayNo",item.getDisplayNo());
        data.put("driveType",item.getDriveType());
        data.put("usbType",item.getUseType());
        data.put("useCondition",item.getUseCondition());

        data.put("checkPrice",item.getCheckPrice());
        data.put("startTime",item.getStartTime());
        data.put("fixedAssetNo",item.getFixedAssetNo());
        data.put("examineManName",item.getExamineManName());
        data.put("examineComment",item.getExamineComment());
        data.put("affairComment",item.getAffairComment());
        data.put("technologyComment",item.getTechnologyComment());

        data.put("creatorName",item.getCreatorName());
        data.put("gmtCreate",item.getGmtCreate());

        List<ActHistoryDto> actHistoryDtos = actService.listPassedHistoryDto(item.getProcessInstanceId());
        for (ActHistoryDto dto:actHistoryDtos){

            if ("???????????????(???????????????)".equals(dto.getActivityName())&&dto.getActivityName()!=null){
                data.put("startMan",dto);
            }
            if ("????????????????????????".equals(dto.getActivityName())&&dto.getActivityName()!=null){
                data.put("affairMan",dto);
            }
            if ("??????????????????????????????????????????".equals(dto.getActivityName())&&dto.getActivityName()!=null){
                data.put("technologyMan",dto);
            }
            if (StringUtils.isNotBlank(dto.getActivityName())){
                if (dto.getActivityName().contains("??????")&&dto.getActivityName()!=null){
                    data.put("financeMan",dto);
                }
            }


        }
        data.put("nodes",actHistoryDtos);

        return data;
    }

    /**
     * 01-????????????????????????????????????????????????
     * 02-????????????????????????????????????????????????????????????
     * 03-??????????????????????????????????????????????????????????????????????????????????????????????????????
     * 04-?????????????????????U?????????????????????
     * 05-?????????
     * 06-?????????????????????????????????????????????
     * 07-????????????
     * 08-??????USBKey????????????????????????
     * 09-????????????????????????????????????????????????????????????????????????
     * 10-?????????????????????????????????
     * ???????????? + ?????? +?????????    012020001
     * @param id
     */
    @Resource
    FiveAssetComputerService fiveAssetComputerService;
    public void getEquipmentExamineNo(int id){
        FiveOaInformationEquipmentExamineDto modelById = getModelById(id);
        CommonCodeDto commonCodeDto = commonCodeService.getDataByCatalogAndName(MccConst.APP_CODE, "?????????????????????", modelById.getOsInstallTime());
        String eqNo=commonCodeDto.getCode();
        int year=MyDateUtil.getYear();
        Map map=Maps.newHashMap();
        map.put("deleted",false);
        map.put("equipmentType",modelById.getOsInstallTime());
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
           fiveOaInformationEquipmentExamineMapper.updateByPrimaryKey(modelById);

       }
        //???????????? ??????  ????????????????????????
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("computerNo",modelById.getFormNo());
        if (fiveAssetComputerMapper.selectAll(params).size()==0){
            if (!modelById.getSecretRank().equals("??????")){
                int computer = fiveAssetComputerService.getModelByEquipmentExamine(id,true);
            }
        }
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
        params.put("startTime1",startTime1);
        params.put("endTime1",endTime1);
        List<FiveOaInformationEquipmentExamine> fiveOaInformationEquipmentExamines=fiveOaInformationEquipmentExamineMapper.selectAll(params);
        for (FiveOaInformationEquipmentExamine dto:fiveOaInformationEquipmentExamines){
            Map map1=new LinkedHashMap();
            map1.put("????????????",dto.getEquipmentName());
            map1.put("???????????????",dto.getEquipmentNo());
            map1.put("????????????",dto.getFormNo());
            map1.put("????????????",dto.getDeptName());
            map1.put("????????????",dto.getSecretRank());
            map1.put("?????????",dto.getChargeManName() );
            map1.put("???????????????", dto.getChargeMan());
            map1.put("?????????",dto.getUserName() );
            map1.put("????????????",dto.getStartTime() );
            map1.put("????????????", dto.getBrand());
            map1.put("????????????",dto.getType());
            map1.put("??????",dto.getUseType());
            map1.put("????????????",dto.getAddress());
            map1.put("???????????????",dto.getDisplayNo());
            map1.put("????????????",dto.getOsInstallTime());
            map1.put("??????????????????",dto.getDiskNo());
            map1.put("?????????????????????",dto.getCheckPrice());
            map1.put("??????",dto.getMacAddress());
            map1.put("????????????",dto.getFixedAssetNo());
            map1.put("?????????",dto.getCreatorName());
            map1.put("????????????",MyDateUtil.dateToStr(dto.getGmtCreate()));
            list.add(map1);
        }
        return list;
    }

}
