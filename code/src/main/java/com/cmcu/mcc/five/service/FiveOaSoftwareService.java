package com.cmcu.mcc.five.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyDateUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.dto.ActHistoryDto;
import com.cmcu.mcc.five.dao.FiveOaSoftwareCostMapper;
import com.cmcu.mcc.five.dao.FiveOaSoftwareMapper;
import com.cmcu.mcc.five.dto.FiveOaSoftwareDto;
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
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class FiveOaSoftwareService extends BaseService {
     @Resource
    FiveOaSoftwareMapper fiveOaSoftwareMapper;
     @Resource
    FiveOaSoftwareCostMapper fiveOaSoftwareCostMapper;
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
        FiveOaSoftware item = fiveOaSoftwareMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"??????????????????"+item.getCreatorName()+"??????");

        //????????????
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

    }

    public void update(FiveOaSoftwareDto fiveOaSoftwareDto){
        FiveOaSoftware model = fiveOaSoftwareMapper.selectByPrimaryKey(fiveOaSoftwareDto.getId());
        model.setDeptId(fiveOaSoftwareDto.getDeptId());
        model.setDeptName(fiveOaSoftwareDto.getDeptName());
        model.setApplyStyle(fiveOaSoftwareDto.getApplyStyle());
        model.setApplyTime(fiveOaSoftwareDto.getApplyTime());
        model.setSoftwareName(fiveOaSoftwareDto.getSoftwareName());
        model.setSoftwareLicenceStyle(fiveOaSoftwareDto.getSoftwareLicenceStyle());
        model.setSoftwareLicenceCount(fiveOaSoftwareDto.getSoftwareLicenceCount());
        model.setSoftwareCompanyName(fiveOaSoftwareDto.getSoftwareCompanyName());
        model.setSoftwareCompanyUrl(fiveOaSoftwareDto.getSoftwareCompanyUrl());
        model.setSoftwareLicenceCount(fiveOaSoftwareDto.getSoftwareLicenceCount());
        model.setSoftwareOffer(fiveOaSoftwareDto.getSoftwareOffer());
        model.setSoftwarePrice(MyStringUtil.moneyToString(fiveOaSoftwareDto.getSoftwarePrice(),6));
        model.setSoftwareLink(fiveOaSoftwareDto.getSoftwareLink());
        model.setSoftwarePhone(fiveOaSoftwareDto.getSoftwarePhone());
        model.setSoftwareUseMajor(fiveOaSoftwareDto.getSoftwareUseMajor());
        model.setSoftwareUseWay(fiveOaSoftwareDto.getSoftwareUseWay());
        model.setSoftwareInstall(fiveOaSoftwareDto.getSoftwareInstall());
        model.setRemark(fiveOaSoftwareDto.getRemark());
        model.setGmtModified(new Date());
        model.setPlan(fiveOaSoftwareDto.getPlan());
        fiveOaSoftwareMapper.updateByPrimaryKey(model);
        Map variables = Maps.newHashMap();
        if (!Strings.isNullOrEmpty(model.getSoftwareOffer())){
            Double sum=Double.valueOf(model.getSoftwareOffer());
            if(sum>=3.0){
                variables.put("flag", 1);
            }else{
                variables.put("flag", 0);
            }
        }
        variables.put("copyMen",selectEmployeeService.getUserListByRoleName("??????????????????????????????"));
        variables.put("processDescription", "?????????????????????"+model.getSoftwareName());
        List<String> deptChargeMenList=Lists.newArrayList();
        List<FiveOaSoftwareCost> fiveOaSoftwareCosts = listDetail(model.getId());
        //2020-12-16 HNZ ?????????????????????????????????
       // deptChargeMenList.addAll(selectEmployeeService.getDeptChargeMen(model.getDeptId()));
        for (FiveOaSoftwareCost softwareCost:fiveOaSoftwareCosts){
           deptChargeMenList.addAll(selectEmployeeService.getDeptChargeMen(softwareCost.getSoftwareUseId()));
        }

        variables.put("deptChargeMenList",deptChargeMenList);//?????????????????????
        BeanValidator.check(model);
        myActService.setVariables(model.getProcessInstanceId(),variables);

    }

    public FiveOaSoftwareDto getModelById(int id){

        return getDto(fiveOaSoftwareMapper.selectByPrimaryKey(id));
    }

    public FiveOaSoftwareDto getDto(FiveOaSoftware item) {
        FiveOaSoftwareDto dto=FiveOaSoftwareDto.adapt(item);
        dto.setProcessName("?????????");
        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if(customProcessInstance==null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaSoftwareMapper.updateByPrimaryKey(dto);
            }else {
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }

        }

        return dto;
    }

    public int getNewModel(String userLogin){


        FiveOaSoftware item=new FiveOaSoftware();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        List<String> business = selectEmployeeService.getOtherDeptChargeMan(hrEmployeeDto.getDeptId());
        Assert.state(business.size()>0,"?????? ??????????????????");
        item.setApplyStyle(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"??????????????????").toString());
        item.setSoftwareLicenceStyle(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"??????????????????").toString());
        item.setApplyTime(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"??????????????????").toString());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setPlan(false);
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        ModelUtil.setNotNullFields(item);
        fiveOaSoftwareMapper.insert(item);
        String businessKey= EdConst.FIVE_OA_SOFTWARE+ "_" + item.getId();
        Map variables = Maps.newHashMap();



        variables.put("userLogin", userLogin);
        variables.put("processDescription", "?????????????????????"+item.getDeptName());

        variables.put("generalManger",hrEmployeeService.selectUserByPositionName("?????????"));//?????????
        variables.put("companyLeader",business);//????????????

        item.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_SOFTWARE,item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        fiveOaSoftwareMapper.updateByPrimaryKey(item);
        return item.getId();
    }


    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);

        params.put("isLikeSelect",true);

        Map map =new HashMap();
        map.put("myDeptData",false);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaSoftwareMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaSoftware item=(FiveOaSoftware)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public void removeDetail(int id){
        FiveOaSoftwareCost model = fiveOaSoftwareCostMapper.selectByPrimaryKey(id);

        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveOaSoftwareCostMapper.updateByPrimaryKey(model);
    }

    public void updateDetail(FiveOaSoftwareCost fiveOaSoftwareCost){
        FiveOaSoftwareCost model = fiveOaSoftwareCostMapper.selectByPrimaryKey(fiveOaSoftwareCost.getId());
        model.setBusinessKey(fiveOaSoftwareCost.getBusinessKey());
        model.setSoftwareId(fiveOaSoftwareCost.getSoftwareId());
        model.setSoftwareUseId(fiveOaSoftwareCost.getSoftwareUseId());
        model.setSoftwareUserName(fiveOaSoftwareCost.getSoftwareUserName());
        model.setSoftwareCostRatio(fiveOaSoftwareCost.getSoftwareCostRatio());
        model.setRemark(fiveOaSoftwareCost.getRemark());
        fiveOaSoftwareCostMapper.updateByPrimaryKey(model);
    }

    public int getNewModelDetail(int softwareId){
        FiveOaSoftware fiveOaSoftware = fiveOaSoftwareMapper.selectByPrimaryKey(softwareId);
        FiveOaSoftwareCost item=new FiveOaSoftwareCost();
        item.setBusinessKey(fiveOaSoftware.getBusinessKey());
        item.setSoftwareId(fiveOaSoftware.getId());
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setCreator(fiveOaSoftware.getCreator());
        item.setCreatorName(fiveOaSoftware.getCreatorName());
        item.setDeleted(false);
        ModelUtil.setNotNullFields(item);
        fiveOaSoftwareCostMapper.insert(item);
        return item.getId();
    }

    public FiveOaSoftwareCost getModelDetailById(int id){
        return fiveOaSoftwareCostMapper.selectByPrimaryKey(id);
    }

    public List<FiveOaSoftwareCost> listDetail(int softwareId){
        Map <String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("softwareId",softwareId);
        List<FiveOaSoftwareCost> list = fiveOaSoftwareCostMapper.selectAll(params);
        return list;
    }

    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaSoftware item = fiveOaSoftwareMapper.selectByPrimaryKey(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String number = sdf.format(new Date())+item.getCreator().substring((item.getCreator()).length() - 4);
        data.put("tableNo",number);
        data.put("deptName",item.getDeptName());
        data.put("applyStyle",item.getApplyStyle());
        data.put("softwareName",item.getSoftwareName());
        data.put("applyTime",item.getApplyTime());
        data.put("softwareCompanyName",item.getSoftwareCompanyName());
        data.put("softwareLink",item.getSoftwareLink());
        data.put("softwareCompanyUrl",item.getSoftwareCompanyUrl());
        data.put("softwarePhone",item.getSoftwarePhone());
        data.put("softwareUseMajor",item.getSoftwareUseMajor());
        data.put("softwareLicenceStyle",item.getSoftwareLicenceStyle());
        data.put("softwareLicenceCount",item.getSoftwareLicenceCount());
        data.put("softwareOffer",item.getSoftwareOffer());
        data.put("softwarePrice",item.getSoftwarePrice());
        data.put("softwareUseWay",item.getSoftwareUseWay());
        data.put("softwareInstall",item.getSoftwareInstall());

        Map map =new HashMap();
        map.put("softwareId",item.getId());
        map.put("deleted",false);
        List<FiveOaSoftwareCost> softwareCostDetails = fiveOaSoftwareCostMapper.selectAll (map);
        data.put("softwareCostDetails",softwareCostDetails);

        List<ActHistoryDto> actHistoryDtos = actService.listPassedHistoryDto(item.getProcessInstanceId());
        for (ActHistoryDto dto:actHistoryDtos){
            if (dto.getActivityName()==null){
                break;
            }
            if ("??????????????????-??????".equals(dto.getActivityName())){
                data.put("computerDept",dto);
            }
            if ("?????????????????????".equals(dto.getActivityName())){
                data.put("financialDept",dto);
            }
            if ("???????????????-??????".equals(dto.getActivityName())){
                data.put("scienceDept",dto);
            }
            if ("????????????".equals(dto.getActivityName())){
                data.put("companyLeader",dto);
            }
            if ("????????????-??????".equals(dto.getActivityName())){
                data.put("chiefAccountant",dto);
            }
            if ("???????????????-??????".equals(dto.getActivityName())){
                data.put("chiefManager",dto);
            }
        }
        data.put("nodes",actHistoryDtos);

        return data;
    }

    /**
     * ??????excl
     * @param startTime1 ????????????
     * @param endTime1 ????????????
     * @return
     */
    public List<Map> listMapData(Map<String,Object> params,String uiSref,String startTime1,String endTime1,String userLogin){
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


        List<FiveOaSoftware> fiveOaSoftwares=fiveOaSoftwareMapper.selectAll(params);
        for (FiveOaSoftware dto:fiveOaSoftwares){
            Map map1=new LinkedHashMap();
            map1.put("????????????",dto.getDeptName());
            map1.put("????????????",dto.getApplyStyle());
            map1.put("????????????",dto.getSoftwareName());
            map1.put("??????????????????",dto.getApplyTime());
            map1.put("??????????????????",dto.getSoftwareCompanyName());
            map1.put("?????????????????????",dto.getSoftwareLink() );
            map1.put("??????????????????", dto.getSoftwareCompanyUrl());
            map1.put("????????????????????????",dto.getSoftwarePhone() );
            map1.put("??????????????????",dto.getSoftwareUseMajor() );
            String plan="";
            if(dto.getPlan()){
                plan+="???";
            }else {
                plan+="???";
            }
            map1.put("???????????????", plan);
            map1.put("??????????????????",dto.getSoftwareLicenceCount());
            map1.put("????????????????????????",dto.getSoftwareOffer());
            map1.put("???????????????????????????",dto.getSoftwareUseWay());
            map1.put("??????????????????",dto.getSoftwareInstall());
            map1.put("?????????",dto.getCreatorName());
            map1.put("????????????", MyDateUtil.dateToStr(dto.getGmtCreate()));
            list.add(map1);
        }


        return list;
    }
}
