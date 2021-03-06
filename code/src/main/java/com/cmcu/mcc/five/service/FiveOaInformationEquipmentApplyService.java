package com.cmcu.mcc.five.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.*;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.dto.ActHistoryDto;
import com.cmcu.mcc.five.dao.FiveOaInformationEquipmentApplyDetailMapper;
import com.cmcu.mcc.five.dao.FiveOaInformationEquipmentApplyMapper;
import com.cmcu.mcc.five.dto.FiveOaInformationEquipmentApplyDto;
import com.cmcu.mcc.five.entity.FiveOaInformationEquipmentApply;
import com.cmcu.mcc.five.entity.FiveOaInformationEquipmentApplyDetail;
import com.cmcu.mcc.five.entity.FiveOaInformationEquipmentProcurement;
import com.cmcu.mcc.five.entity.FiveOaInformationEquipmentProcurementDetail;
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
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FiveOaInformationEquipmentApplyService extends BaseService {
    @Resource
    FiveOaInformationEquipmentApplyMapper fiveOaInformationEquipmentApplyMapper;
    @Resource
    FiveOaInformationEquipmentApplyDetailMapper fiveOaInformationEquipmentApplyDetailMapper;
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
        FiveOaInformationEquipmentApply item = fiveOaInformationEquipmentApplyMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"??????????????????"+item.getCreatorName()+"??????");

        //????????????
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

    }

    public void update(FiveOaInformationEquipmentApplyDto item){
        FiveOaInformationEquipmentApply model = fiveOaInformationEquipmentApplyMapper.selectByPrimaryKey(item.getId());
        model.setDeptId(item.getDeptId());
        model.setDeptName(item.getDeptName());
        model.setApplyTime(item.getApplyTime());
        model.setLinkMan(item.getLinkMan());
        model.setLinkManName(item.getLinkManName());
        model.setLinkManPhone(item.getLinkManPhone());
        model.setPlan(item.getPlan());
        model.setEquipmentUse(item.getEquipmentUse());
        model.setRemark(item.getRemark());
        model.setGmtModified(new Date());

        Map variables = Maps.newHashMap();
        variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDeptId()));//?????????????????????
        variables.put("otherDeptChargeMan", selectEmployeeService.getOtherDeptChargeMan(item.getDeptId()));//??????????????????
        variables.put("processDescription", "?????????????????????????????????:"+model.getDeptName());
        myActService.setVariables(model.getProcessInstanceId(),variables);
        BeanValidator.check(model);
        fiveOaInformationEquipmentApplyMapper.updateByPrimaryKey(model);


    }

    public FiveOaInformationEquipmentApplyDto getModelById(int id){

        return getDto(fiveOaInformationEquipmentApplyMapper.selectByPrimaryKey(id));
    }

    public FiveOaInformationEquipmentApplyDto getDto(FiveOaInformationEquipmentApply item) {
        FiveOaInformationEquipmentApplyDto dto=FiveOaInformationEquipmentApplyDto.adapt(item);
        dto.setProcessName("?????????");
        if (!dto.getProcessEnd()) {

            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if(customProcessInstance ==null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaInformationEquipmentApplyMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance!=null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());

            }
        }
        //?????? ?????????
        String totalFinalPrice= MyStringUtil.moneyToString("0");
        for (FiveOaInformationEquipmentApplyDetail detail:listDetail(item.getId())){
            totalFinalPrice = MyStringUtil.getNewAddMoney(totalFinalPrice,detail.getTotalPrice(),2);
        }
        if (!item.getTotalMoney().equals(totalFinalPrice)){
            item.setTotalMoney(totalFinalPrice);
            fiveOaInformationEquipmentApplyMapper.updateByPrimaryKey(item);
        }

        return dto;
    }

    public int getNewModel(String userLogin){

        FiveOaInformationEquipmentApply item=new FiveOaInformationEquipmentApply();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());

        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setLinkMan(userLogin);
        item.setLinkManName(hrEmployeeDto.getUserName());
        item.setPlan(false);
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setApplyTime(MyDateUtil.getStringToday());
        ModelUtil.setNotNullFields(item);

        fiveOaInformationEquipmentApplyMapper.insert(item);
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "?????????????????????????????????:"+item.getDeptName());


        variables.put("financeDeptChargeMan", selectEmployeeService.getDeptChargeMen(18));//????????????????????????
        variables.put("administrativeDeptChargeMan", selectEmployeeService.getDeptChargeMen(67));//????????????????????????


        String businessKey=EdConst.FIVE_OA_INFORMATIONEQUIPMENTAPPLY+ "_"+item.getId();

        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_INFORMATIONEQUIPMENTAPPLY, businessKey , variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);
        Map map = Maps.newHashMap();
        map.put("deleted",false);
        int seq = fiveOaInformationEquipmentApplyMapper.selectAll(map).stream().filter(p->p.getGmtCreate().after(MyDateUtil.getNowYear())).collect(Collectors.toList()).size();
        item.setFormNo(MyFormNo.getFormNo(taskHandleService.getDeploymentId(EdConst.FIVE_OA_INFORMATIONEQUIPMENTAPPLY),seq));
        fiveOaInformationEquipmentApplyMapper.updateByPrimaryKey(item);
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

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaInformationEquipmentApplyMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaInformationEquipmentApply item=(FiveOaInformationEquipmentApply)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    //??????????????? ???????????????????????????????????????
    public PageInfo<Object> listPagedDataExamine(Map<String,Object> params, String userLogin,int pageNum, int pageSize) {
        params.put("deleted",false);
        params.put("isLikeSelect",true);
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptListOa(userLogin,"five.oaInformationEquipmentApply");
        if (deptIdList.size()==0){
            params.put("creator",userLogin);
        }else {
            params.put("deptIdList",deptIdList);
        }

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaInformationEquipmentApplyMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaInformationEquipmentApply item=(FiveOaInformationEquipmentApply)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }


    public void removeDetail(int id){
        FiveOaInformationEquipmentApplyDetail model = fiveOaInformationEquipmentApplyDetailMapper.selectByPrimaryKey(id);

        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveOaInformationEquipmentApplyDetailMapper.updateByPrimaryKey(model);
    }

    public void updateDetail(FiveOaInformationEquipmentApplyDetail item){
        FiveOaInformationEquipmentApplyDetail model = fiveOaInformationEquipmentApplyDetailMapper.selectByPrimaryKey(item.getId());
        model.setDeptId(item.getDeptId());
        model.setDeptName(item.getDeptName());
        model.setEquipmentName(item.getEquipmentName());
        model.setBrand(item.getBrand());
        model.setEquipmentType(item.getEquipmentType());
        model.setNumber(item.getNumber());
        model.setNetType(item.getNetType());
        model.setOtherRequirement(item.getOtherRequirement());
        model.setTotalPrice(MyStringUtil.moneyToString(item.getTotalPrice(),2));
        model.setRemark(item.getRemark());
        model.setPrice(MyStringUtil.moneyToString(item.getPrice(),2));
        fiveOaInformationEquipmentApplyDetailMapper.updateByPrimaryKey(model);
    }

    public FiveOaInformationEquipmentApplyDetail getNewModelDetail(int  id){
        FiveOaInformationEquipmentApplyDetail item=new FiveOaInformationEquipmentApplyDetail();
        item.setInformationEquipmentApplyId(id+"");
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);
        ModelUtil.setNotNullFields(item);
        fiveOaInformationEquipmentApplyDetailMapper.insert(item);
        return item;

    }

    public FiveOaInformationEquipmentApplyDetail getModelDetailById(int id){
        return fiveOaInformationEquipmentApplyDetailMapper.selectByPrimaryKey(id);
    }

    public List<FiveOaInformationEquipmentApplyDetail> listDetail(int id){
        Map <String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("informationEquipmentApplyId",id);
        List<FiveOaInformationEquipmentApplyDetail> list = fiveOaInformationEquipmentApplyDetailMapper.selectAll(params);
        return list;
    }

    public FiveOaInformationEquipmentApply getModelByNo(String formNo){
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("formNo",formNo);
        if (fiveOaInformationEquipmentApplyMapper.selectAll(params).size()>0){
            return   fiveOaInformationEquipmentApplyMapper.selectAll(params).get(0);
        }else {
            return null;
        }

    }

    //??????
    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaInformationEquipmentApply item = fiveOaInformationEquipmentApplyMapper.selectByPrimaryKey(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String number = sdf.format(new Date())+item.getCreator().substring((item.getCreator()).length() - 4);
        data.put("tableNo",number);
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
        data.put("nodes",actHistoryDtos);

        return data;
    }

    /**
     * ??????excl
     * @param startTime1 ????????????
     * @param endTime1 ????????????
     * @return
     */
    public List<Map> listMapData(Map<String,Object> params,String uiSref,String startTime1,String endTime1,String userLogin,String userName){
        List<Map> list = new ArrayList<>();
        /*Map map=new LinkedHashMap();
        map.put("??????????????????","");
        map.put("????????????","");
        map.put("??????????????????","");
        map.put("?????????","");
        map.put("????????????","");
        map.put("???????????????????????????","");
        map.put("?????????","");
        map.put("????????????","");
        map.put("????????????","");
        map.put("??????????????????","");
        map.put("??????","");
        map.put("????????????","");
        map.put("????????????","");
        map.put("???????????????","");
        map.put("???????????????","");
        map.put("????????????","");
        map.put("??????","");
        list.add(map);*/

        //??????????????????
        Map head=Maps.newHashMap();
        head.put("myDeptData",false);
        head.put("uiSref",uiSref);
        head.put("enLogin",userLogin);

        //??????????????????
        params.put("isLikeSelect",true);
        params.putAll(getDefaultRightParams(head));
        params.remove("userLogin");//???????????????userLogin ??????????????????
        //????????? ???????????????
        params.put("deleted",false);
        params.put("processEnd",true);
        //???????????????
        params.put("startTime1",startTime1);
        params.put("endTime1",endTime1);
        params.put("creator",userName);

        List<FiveOaInformationEquipmentApply> fiveOaInformationEquipmentApplies=fiveOaInformationEquipmentApplyMapper.selectAll(params);
        for (FiveOaInformationEquipmentApply dto:fiveOaInformationEquipmentApplies){
            List<FiveOaInformationEquipmentApplyDetail> fiveOaInformationEquipmentApplyDetails=listDetail(dto.getId());

            for (FiveOaInformationEquipmentApplyDetail detail:fiveOaInformationEquipmentApplyDetails){
                Map map=new LinkedHashMap();
                map.put("??????????????????",dto.getFormNo());
                map.put("????????????",dto.getDeptName());
                map.put("??????????????????",dto.getPlan());
                map.put("?????????",dto.getLinkManName());
                map.put("????????????",dto.getLinkManPhone());
                map.put("???????????????????????????",dto.getEquipmentUse());
                map.put("?????????",dto.getCreatorName());
                map.put("????????????",MyDateUtil.dateToStr(dto.getGmtCreate()));
                map.put("????????????",detail.getEquipmentName());
                map.put("??????????????????",detail.getDeptName());
                map.put("??????",detail.getBrand());
                map.put("????????????",detail.getEquipmentType());
                map.put("????????????",detail.getNumber());
                map.put("???????????????",detail.getPrice());
                map.put("???????????????",detail.getTotalPrice());
                map.put("????????????",detail.getOtherRequirement());
                map.put("??????",detail.getRemark());
                list.add(map);
            }
        }

        return list;
    }



}
