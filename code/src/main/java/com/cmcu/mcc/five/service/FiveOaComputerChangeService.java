package com.cmcu.mcc.five.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyDateUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.dto.ActHistoryDto;
import com.cmcu.mcc.five.dao.FiveAssetComputerMapper;
import com.cmcu.mcc.five.dao.FiveOaComputerChangeMapper;
import com.cmcu.mcc.five.dto.FiveOaComputerChangeDto;
import com.cmcu.mcc.five.entity.FiveAssetComputer;
import com.cmcu.mcc.five.entity.FiveOaComputerChange;
import com.cmcu.mcc.five.entity.FiveOaInformationEquipmentExamine;
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

@Service
public class FiveOaComputerChangeService extends BaseService {
    @Resource
    FiveOaComputerChangeMapper fiveOaComputerChangeMapper;
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
    @Autowired
    FiveAssetComputerMapper fiveAssetComputerMapper;
    @Resource
    FiveAssetComputerService fiveAssetComputerService;


    public void remove(int id,String userLogin){
        FiveOaComputerChange item = fiveOaComputerChangeMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"??????????????????"+item.getCreatorName()+"??????");

        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

    }

   public void update(FiveOaComputerChangeDto fiveOaComputerChangeDto){
       FiveOaComputerChange model = fiveOaComputerChangeMapper.selectByPrimaryKey(fiveOaComputerChangeDto.getId());
       model.setAssetId(fiveOaComputerChangeDto.getAssetId());
       model.setComputerNo(fiveOaComputerChangeDto.getComputerNo());
       model.setComputerName(fiveOaComputerChangeDto.getComputerName());
       model.setAssetNo(fiveOaComputerChangeDto.getAssetNo());
       model.setMacAddress(fiveOaComputerChangeDto.getMacAddress());
       model.setApplyUserLogin(fiveOaComputerChangeDto.getApplyUserLogin());
       model.setApplyUserName(fiveOaComputerChangeDto.getApplyUserName());
       model.setApplyPhone(fiveOaComputerChangeDto.getApplyPhone());
       model.setDeptName(fiveOaComputerChangeDto.getDeptName());
       model.setDutyLogin(fiveOaComputerChangeDto.getDutyLogin());
       model.setDutyName(fiveOaComputerChangeDto.getDutyName());
       model.setDutySecurity(fiveOaComputerChangeDto.getDutySecurity());
       model.setDutyDeptId(fiveOaComputerChangeDto.getDutyDeptId());
       model.setDutyDeptName(fiveOaComputerChangeDto.getDutyDeptName());
       model.setUsersName(fiveOaComputerChangeDto.getUsersName());
       model.setUsersLogin(fiveOaComputerChangeDto.getUsersLogin());
       model.setUsersSecurty(fiveOaComputerChangeDto.getUsersSecurty());
       model.setUsersDeptId(fiveOaComputerChangeDto.getUsersDeptId());
       model.setUsersDeptName(fiveOaComputerChangeDto.getUsersDeptName());
       model.setDeptId(fiveOaComputerChangeDto.getDeptId());
       model.setDeptName(fiveOaComputerChangeDto.getDeptName());
       model.setUseSituation(fiveOaComputerChangeDto.getUseSituation());
       model.setUseWay(fiveOaComputerChangeDto.getUseWay());
       model.setUseType(fiveOaComputerChangeDto.getUseType());
       model.setNetwork(fiveOaComputerChangeDto.getNetwork());
       model.setRoom(fiveOaComputerChangeDto.getRoom());
       model.setUsb(fiveOaComputerChangeDto.getUsb());
       model.setHardDisk(fiveOaComputerChangeDto.getHardDisk());
       model.setReason(fiveOaComputerChangeDto.getReason());
       model.setNewDutyName(fiveOaComputerChangeDto.getNewDutyName());
       model.setNewDutyLogin(fiveOaComputerChangeDto.getNewDutyLogin());
       model.setNewDutySecurity(fiveOaComputerChangeDto.getNewDutySecurity());
       model.setNewDutyDeptId(fiveOaComputerChangeDto.getNewDutyDeptId());
       model.setNewDutyDeptName(fiveOaComputerChangeDto.getNewDutyDeptName());
       model.setNewUsersName(fiveOaComputerChangeDto.getNewUsersName());
       model.setNewUsersLogin(fiveOaComputerChangeDto.getNewUsersLogin());
       model.setNewUsersSecurty(fiveOaComputerChangeDto.getNewUsersSecurty());
       model.setNewUsersDeptId(fiveOaComputerChangeDto.getNewUsersDeptId());
       model.setNewUsersDeptName(fiveOaComputerChangeDto.getNewUsersDeptName());
       model.setNewDeptId(fiveOaComputerChangeDto.getNewDeptId());
       model.setNewDeptName(fiveOaComputerChangeDto.getNewDeptName());
       model.setNewUseSituation(fiveOaComputerChangeDto.getNewUseSituation());
       model.setNewUseWay(fiveOaComputerChangeDto.getNewUseWay());
       model.setNewUseType(fiveOaComputerChangeDto.getNewUseType());
       model.setNewNetwork(fiveOaComputerChangeDto.getNewNetwork());
       model.setNewRoom(fiveOaComputerChangeDto.getNewRoom());
       model.setNewHardDisk(fiveOaComputerChangeDto.getNewHardDisk());
       model.setNewUsb(fiveOaComputerChangeDto.getNewUsb());


       model.setGmtModified(new Date());

       BeanValidator.check(model);

       fiveOaComputerChangeMapper.updateByPrimaryKey(model);
       Map variables = Maps.newHashMap();
       int change=0;
       int  flag=0;
       if(fiveOaComputerChangeDto.getAssetId()!=0){
           if(!model.getNewDutyLogin().equals(model.getDutyLogin())){
               change=1;
               variables.put("endDutyMan", fiveOaComputerChangeDto.getNewDutyLogin());//??????????????????
               //20210325 hnz ???????????????????????? ????????????????????????????????????????????????
               flag=1;
               variables.put("administrative",selectEmployeeService.getDeptChargeMen(67));//????????????????????????
           }
           if (!model.getDeptName().equals(model.getNewDeptName())){
               flag=1;
               variables.put("administrative",selectEmployeeService.getDeptChargeMen(67));//????????????????????????
           }
       }
       variables.put("change", change);
       variables.put("flag", flag);
       variables.put("beforeDutyMan", model.getDutyLogin());//??????????????????
       variables.put("deviceChargeMen", selectEmployeeService.getDeptChargeMen(fiveOaComputerChangeDto.getDeptId()));
       variables.put("administrative",selectEmployeeService.getDeptChargeMen(67));//????????????????????????
       variables.put("processDescription", "??????????????????????????????????????????"+model.getDeptName());
       myActService.setVariables(model.getProcessInstanceId(),variables);

   }

    public FiveOaComputerChangeDto getModelById(int id){

        return getDto(fiveOaComputerChangeMapper.selectByPrimaryKey(id));
    }

    public FiveOaComputerChangeDto getDto(FiveOaComputerChange item) {
        FiveOaComputerChangeDto dto=FiveOaComputerChangeDto.adapt(item);
        dto.setProcessName("?????????");

        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");
            if(customProcessInstance == null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaComputerChangeMapper.updateByPrimaryKey(dto);
                //2021-01-8HNZ ????????????????????????????????????
                fiveAssetComputerService.updateByComputerChange(dto.getId());
            }

            if(customProcessInstance != null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());

            }
        }

        return dto;
    }

    public int getNewModel(String userLogin){

        FiveOaComputerChange item=new FiveOaComputerChange();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setApplyUserLogin(hrEmployeeDto.getUserLogin());
        item.setApplyUserName(hrEmployeeDto.getUserName());
        item.setApplyPhone(hrEmployeeDto.getMobile());
        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());

        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setDutySecurity(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????").toString());
        item.setUsersSecurty(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????").toString());
/*
        item.setComputerPrincipalChange(true);
        item.setStationChange(true);
        item.setUsbOpen(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"usb??????").toString());
        item.setSecurityLevel(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????").toString());*/
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        ModelUtil.setNotNullFields(item);


        fiveOaComputerChangeMapper.insert(item);
        String businessKey= EdConst.FIVE_OA_COMPUTER_CHANGE+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "??????????????????????????????????????????"+item.getCreatorName());
       // variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDeptId()));//?????????????????????


        variables.put("computerLeader",selectEmployeeService.getUserListByRoleName("??????????????????????????????"));//?????????????????????
        variables.put("informationEquipmentMen",selectEmployeeService.getUserListByRoleName("???????????????????????????(???????????????)"));//??????????????????????????????????????????????????????
        //administrative

        item.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_COMPUTER_CHANGE,item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        fiveOaComputerChangeMapper.updateByPrimaryKey(item);
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
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaComputerChangeMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaComputerChange item=(FiveOaComputerChange)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaComputerChange item = fiveOaComputerChangeMapper.selectByPrimaryKey(id);
        FiveAssetComputer asset=fiveAssetComputerMapper.selectByPrimaryKey(item.getAssetId());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String number = sdf.format(new Date())+item.getCreator().substring((item.getCreator()).length() - 4);
        data.put("tableNo",number);

        data.put("applyUserName",item.getApplyUserName());
        data.put("applyPhone",item.getApplyPhone());
        data.put("computerNo",item.getComputerNo());
        data.put("computerName",item.getComputerName());
        data.put("assetNo",item.getAssetNo());
        data.put("macAddress",item.getMacAddress());
        data.put("reason",item.getReason());
        data.put("dutyName",item.getDutyName());
        data.put("dutyLogin",item.getDutyLogin());
        data.put("dutySecurity",item.getDutySecurity());
        data.put("dutyDeptName",item.getDutyDeptName());
        data.put("usersName",item.getUsersName());
        data.put("usersLogin",item.getUsersLogin());
        data.put("usersSecurty",item.getUsersSecurty());
        data.put("usersDeptName",item.getUsersDeptName());
        data.put("deptId",asset.getDeptId());
        data.put("deptName",item.getDeptName());
        data.put("useSituation",item.getUseSituation());
        data.put("useWay",item.getUseWay());
        data.put("useType",item.getUseType());
        data.put("network",item.getNetwork());
        data.put("room",item.getRoom());
        data.put("hardDisk",item.getHardDisk());
        data.put("usb",item.getUsb());


        data.put("newDutyName",item.getNewDutyName());
        data.put("newDutyLogin",item.getNewDutyLogin());
        data.put("newDutySecurity",item.getNewDutySecurity());
        data.put("newDutyDeptId",item.getNewDutyDeptId());
        data.put("newDutyDeptName",item.getNewDutyDeptName());
        data.put("newUsersName",item.getNewUsersName());
        data.put("newUsersLogin",item.getNewUsersLogin());
        data.put("newUsersSecurty",item.getNewUsersSecurty());
        data.put("newUsersDeptId",item.getNewUsersDeptName());
        data.put("newUsersDeptName",item.getNewUsersDeptName());
        data.put("newDeptId",item.getNewDeptId());
        data.put("newDeptName",item.getNewDeptName());
        data.put("newUseSituation",item.getNewUseSituation());
        data.put("newUseWay",item.getNewUseWay());
        data.put("newUseType",item.getNewUseType());
        data.put("newNetwork",item.getNewNetwork());
        data.put("newRoom",item.getNewRoom());
        data.put("newHardDisk",item.getNewHardDisk());
        data.put("newUsb",item.getNewUsb());


        List<ActHistoryDto> actHistoryDtos = actService.listPassedHistoryDto(item.getProcessInstanceId());
        for (ActHistoryDto dto:actHistoryDtos){
            if (dto.getActivityName()==null){
                break;
            }
            if ("?????????".equals(dto.getActivityName())){
                data.put("initiator",dto);
            }
            if ("??????????????????".equals(dto.getActivityName())){
                data.put("beforeDutyMan",dto);
            }
            if ("??????????????????".equals(dto.getActivityName())){
                data.put("endDutyMan",dto);
            }
            if ("???????????????????????????".equals(dto.getActivityName())){
                data.put("deviceChargeMen",dto);
            }
            if ("???????????????".equals(dto.getActivityName())){
                data.put("administrative",dto);
            }
            if ("???????????????????????????".equals(dto.getActivityName())){
                data.put("informationEquipmentMen",dto);
            }
            if ("??????????????????-????????????".equals(dto.getActivityName())){
                data.put("computerLeader",dto);
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


        List<FiveOaComputerChange> fiveOaComputerChanges=fiveOaComputerChangeMapper.selectAll(params);
        for (FiveOaComputerChange dto:fiveOaComputerChanges){
            Map map1=new LinkedHashMap();
            map1.put("?????????",dto.getApplyUserName());
            map1.put("????????????",dto.getApplyPhone());
            map1.put("?????????????????????",dto.getComputerNo());
            map1.put("????????????",dto.getDeptName());
            map1.put("????????????",dto.getAssetNo());
            map1.put("MAC??????",dto.getMacAddress() );
            map1.put("????????????", dto.getReason()  );
            map1.put("?????????",dto.getCreatorName() );
            map1.put("????????????", MyDateUtil.dateToStr(dto.getGmtCreate()));

            map1.put("?????????-???????????????", dto.getNewDutyName());
            map1.put("?????????-??????????????????",dto.getNewDutyLogin());
            map1.put("?????????-???????????????",dto.getNewDutySecurity());
            map1.put("?????????-?????????????????????",dto.getNewDutyDeptName());
            map1.put("?????????-???????????????",dto.getNewUsersName());
            map1.put("?????????-??????????????????",dto.getNewUsersLogin());
            map1.put("?????????-???????????????",dto.getNewUsersSecurty());
            map1.put("?????????-?????????????????????",dto.getNewUsersDeptName());
            map1.put("?????????-??????",dto.getNewUseWay());
            map1.put("?????????-????????????",dto.getNewUseType());
            map1.put("?????????-????????????",dto.getNewNetwork());
            map1.put("?????????-????????????",dto.getNewRoom());
            map1.put("?????????-????????????",dto.getNewHardDisk());
            map1.put("?????????-USB??????",dto.getNewUsb());
            list.add(map1);
        }


        return list;
    }
}
