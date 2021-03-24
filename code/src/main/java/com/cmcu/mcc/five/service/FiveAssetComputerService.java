package com.cmcu.mcc.five.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.util.*;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.five.dao.FiveAssetComputerMapper;
import com.cmcu.mcc.five.dto.FiveAssetComputerDto;
import com.cmcu.mcc.five.dto.FiveOaComputerChangeDto;
import com.cmcu.mcc.five.dto.FiveOaComputerNetworkDto;
import com.cmcu.mcc.five.dto.FiveOaInformationEquipmentExamineDto;
import com.cmcu.mcc.five.entity.FiveAssetComputer;
import com.cmcu.mcc.five.entity.FiveOaInformationEquipmentExamineListDetail;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.entity.HrDept;
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
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FiveAssetComputerService extends BaseService {
    
    @Resource
    FiveAssetComputerMapper fiveAssetComputerMapper;
    @Resource
    HrEmployeeMapper hrEmployeeMapper;

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
    FiveOaInformationEquipmentExamineService fiveOaInformationEquipmentExamineService;
    @Resource
    HandleFormService handleFormService;
    @Autowired
    HrDeptService hrDeptService ;


    public List<FiveAssetComputer> listDate(String userLogin){
      Map map= Maps.newHashMap();
      map.put("deleted",false);
      map.put("processEnd",true);
     //按照权限设置
      /*    Map map1=Maps.newHashMap();
        map1.put("myDeptData",true);
        map1.put("uiSref","five.assetComputer");
        map.put("enLogin",userLogin);
        map.putAll(getDefaultRightParams(map1));
        */
      map.put("chargeOrUser",userLogin);
      List<FiveAssetComputer> fiveAssetComputers = fiveAssetComputerMapper.selectAll(map).stream().filter(p->!Strings.isNullOrEmpty(p.getComputerNo()))
              .collect(Collectors.toList());
      return  fiveAssetComputers;
  }

    public void remove(int id,String userLogin){
        FiveAssetComputer model = fiveAssetComputerMapper.selectByPrimaryKey(id);
        Assert.state(model.getCreator().equals(userLogin),"该数据是用户"+model.getCreatorName()+"创建");
        //流程作废
        handleFormService.removeProcessInstance(model.getBusinessKey(),userLogin);
    }

    public void update(FiveAssetComputerDto item){
        FiveAssetComputer model = fiveAssetComputerMapper.selectByPrimaryKey(item.getId());
        model.setDeptId(item.getDeptId());
        model.setDeptName(item.getDeptName());
        model.setUseLogin(item.getUseLogin());
        model.setUseName(item.getUseName());
        model.setComputerNo(item.getComputerNo());
        model.setComputerName(item.getComputerName());
        model.setComputerBrand(item.getComputerBrand());
        model.setSecurityLevel(item.getSecurityLevel());
        model.setUseWay(item.getUseWay());
        model.setRoom(item.getRoom());
        model.setOperatingSystem(item.getOperatingSystem());
        model.setOperatingSystemTime(item.getOperatingSystemTime());
        model.setSnNo(item.getSnNo());
        model.setHardDiskNo(item.getHardDiskNo());
        model.setMacAddress(item.getMacAddress());
        model.setNetwork(item.getNetwork());
        model.setIpAddress(item.getIpAddress());
        model.setDisplayType(item.getDisplayType());
        model.setCdType(item.getCdType());
        model.setUsbStatus(item.getUsbStatus());
        model.setUseTime(item.getUseTime());
        model.setUseStatus(item.getUseStatus());
        model.setRemark(item.getRemark());
        model.setChargeMan(item.getChargeMan());
        model.setChargeManName(item.getChargeManName());
        model.setEquipmentNo(item.getEquipmentNo());
        model.setEquipmentType(item.getEquipmentType());
        model.setFixedAssetNo(item.getFixedAssetNo());
        model.setGmtModified(new Date());
        ModelUtil.setNotNullFields(model);

        checkUniqueNo(model);

        fiveAssetComputerMapper.updateByPrimaryKey(model);

        Map variables = Maps.newHashMap();
        if(item.getDeptId()!=0) {
            variables.put("deptChargeMan", selectEmployeeService.getDeptChargeMen(item.getDeptId()));//发起者部门领导
        }
        BeanValidator.check(model);
        myActService.setVariables(model.getProcessInstanceId(),variables);
    }

    /**
     * 验证是否唯一设备编号和设备序列号
     * @param model
     */
    public   void checkUniqueNo(FiveAssetComputer model){
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        //设备编号为空不验证
        if (!Strings.isNullOrEmpty(model.getComputerNo())&&!model.getComputerNo().equals("无")) {
            params.put("computerNo", model.getComputerNo());
            List<FiveAssetComputer> computerList = fiveAssetComputerMapper.selectAll(params);
            Assert.state(computerList.size() == 0 || computerList.stream().anyMatch(p -> p.getId().equals(model.getId())), "设备编号" + model.getComputerNo() + "已存在！");
        }

        if (!Strings.isNullOrEmpty(model.getEquipmentNo())) {
            params.remove("computerNo");
            params.put("equipmentNo", model.getEquipmentNo());
            List<FiveAssetComputer> computerList = fiveAssetComputerMapper.selectAll(params);
            Assert.state(computerList.size() == 0 || computerList.stream().anyMatch(p -> p.getId().equals(model.getId())), "设备序列号" + model.getEquipmentNo() + "已存在！");
        }
    }

    public FiveAssetComputerDto getModelById(int id){

        return getDto(fiveAssetComputerMapper.selectByPrimaryKey(id));
    }

    /**
     * 根据设备编号 查询指定台账
     * @param computerNo
     * @return
     */
    public FiveAssetComputerDto getModelByComputerNo(String computerNo){
        Map map= Maps.newHashMap();
        map.put("deleted",false);
        map.put("computerNo",computerNo);//责任人
        if (fiveAssetComputerMapper.selectAll(map).size()>0){
            return getDto(fiveAssetComputerMapper.selectAll(map).get(0));
        }
        return new FiveAssetComputerDto();
    }

    public FiveAssetComputerDto getDto(FiveAssetComputer item) {
        FiveAssetComputerDto dto=FiveAssetComputerDto.adapt(item);
        dto.setProcessName("已完成");

        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), "", "");

            if(customProcessInstance ==null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveAssetComputerMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance!=null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }

        return dto;
    }

    /**
     * 台账直接新增 有流程
     * @param userLogin
     * @return
     */
    public int getNewModel(String userLogin){
        FiveAssetComputer item=new FiveAssetComputer();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setUseLogin(hrEmployeeDto.getUserLogin());
        item.setUseName(hrEmployeeDto.getUserName());
        item.setChargeMan(hrEmployeeDto.getUserLogin());
        item.setChargeManName(hrEmployeeDto.getUserName());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setUseTime(MyDateUtil.getStringToday());
        item.setOperatingSystemTime(MyDateUtil.getStringToday());

        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        ModelUtil.setNotNullFields(item);

        fiveAssetComputerMapper.insert(item);

        String businessKey= EdConst.FIVE_OA_ASSET_COMPUTER+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription","非密信息化台账补录："+item.getCreatorName());

        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_ASSET_COMPUTER,businessKey, variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);
        item.setProcessEnd(false);
        fiveAssetComputerMapper.updateByPrimaryKey(item);
        return item.getId();
    }


    /**
     *  管理员查询看所有台账、普通员工查看责任人或者使用人是自己的
     * @param params
     * @param userLogin
     * @param uiSref
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref, int pageNum, int pageSize) {
        params.put("deleted",false);
        params.put("isLikeSelect",true);

        Map map=Maps.newHashMap();
        map.put("myDeptData",true);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));

        params.remove("userLogin");//字段中含有userLogin 排除干扰因素
        //如果只查看自己的权限 查询看所有台账、普通员工查看责任人或者使用人是自己的
        if (params.containsKey("creator")){
            params.put("chargeOrUser",userLogin);
        }
        //流程相关
        if (!params.containsKey("flag")){
            params.put("processEnd",true);
            params.remove("flag");
        }


        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveAssetComputerMapper.selectAll(params));
         List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveAssetComputer item=(FiveAssetComputer)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    @Resource
    FiveOaInformationEquipmentExamineListService fiveOaInformationEquipmentExamineListService;
    //验收单 创建新的台账
    public int getModelByEquipmentExamine(int equpmentId,boolean type){
        FiveAssetComputer item=new FiveAssetComputer();
        if (type){
            FiveOaInformationEquipmentExamineDto equipmentExamineDto = fiveOaInformationEquipmentExamineService.getModelById(equpmentId);
            item.setEquipmentNo(equipmentExamineDto.getEquipmentNo());//设备序列号
            item.setDeptName(equipmentExamineDto.getDeptName());//所属部门
            item.setDeptId(equipmentExamineDto.getDeptId());
            item.setChargeManName(equipmentExamineDto.getChargeManName());//责任人
            item.setChargeMan(equipmentExamineDto.getChargeMan());
            item.setUseName(equipmentExamineDto.getUserName());//使用人
            item.setUseLogin(equipmentExamineDto.getUser());
            item.setComputerNo(equipmentExamineDto.getFormNo()); //设备编号
            item.setComputerName(equipmentExamineDto.getEquipmentName());//设备名称
            item.setUseWay(equipmentExamineDto.getUseType());
            item.setSecurityLevel(equipmentExamineDto.getSecretRank());
            item.setEquipmentType(equipmentExamineDto.getOsInstallTime());//设备类型
            item.setComputerBrand(equipmentExamineDto.getBrand());//品牌
            item.setRoom(equipmentExamineDto.getAddress());//放置地点
            item.setUseTime(equipmentExamineDto.getStartTime());//初次使用时间
            int twoDay = Integer.parseInt(MyDateUtil.getTwoDay(item.getUseTime(), MyDateUtil.getStringToday()));//判断启用时间和当前时间之前差的天数
            if (twoDay<0){
                item.setUsbStatus("未启用");//联网类型
            }else {
                item.setUsbStatus("启用");//联网类型
            }
            item.setUseTime(equipmentExamineDto.getStartTime());//初次器用时间
            item.setFixedAssetNo(equipmentExamineDto.getFixedAssetNo());//固定资产编号
            item.setSnNo(equipmentExamineDto.getType());//设备型号
            item.setDisplayType(equipmentExamineDto.getDisplayNo());//显示器型号);
            item.setDeleted(false);
            item.setProcessEnd(false);
            item.setCreator(equipmentExamineDto.getCreator());
            item.setCreatorName(equipmentExamineDto.getCreatorName());
            item.setGmtModified(new Date());
            item.setGmtCreate(new Date());
        }else {
            FiveOaInformationEquipmentExamineListDetail detail = fiveOaInformationEquipmentExamineListService.getModelDetailById(equpmentId);
            item.setEquipmentNo(detail.getEquipmentNo());//设备序列号
            item.setDeptName(detail.getDeptName());//所属部门
            item.setDeptId(detail.getDeptId());
            item.setChargeManName(detail.getChargeManName());//责任人
            item.setChargeMan(detail.getChargeMan());
            item.setUseName(detail.getUserName());//使用人
            item.setUseLogin(detail.getUser());
            item.setComputerNo(detail.getFormNo()); //设备编号
            item.setComputerName(detail.getEquipmentName());//设备名称
            item.setUseWay(detail.getUseType());
            item.setSecurityLevel(detail.getSecretRank());
            item.setEquipmentType(detail.getOsInstallTime());//设备类型
            item.setComputerBrand(detail.getBrand());//品牌
            item.setRoom(detail.getAddress());//放置地点
            item.setUseTime(detail.getStartTime());//初次使用时间
            int twoDay = Integer.parseInt(MyDateUtil.getTwoDay(item.getUseTime(), MyDateUtil.getStringToday()));//判断启用时间和当前时间之前差的天数
            if (twoDay<0){
                item.setUsbStatus("未启用");//联网类型
            }else {
                item.setUsbStatus("启用");//联网类型
            }
            item.setUseTime(detail.getStartTime());//初次器用时间
            item.setFixedAssetNo(detail.getFixedAssetNo());//固定资产编号
            item.setSnNo(detail.getType());//设备型号
            item.setDisplayType(detail.getDisplayNo());//显示器型号);
            item.setDeleted(false);
            item.setProcessEnd(false);
            item.setCreator(detail.getCreator());
            item.setCreatorName(detail.getCreatorName());
            item.setGmtModified(new Date());
            item.setGmtCreate(new Date());
        }

        item.setProcessEnd(true);//不走流程直接完成
        ModelUtil.setNotNullFields(item);
       fiveAssetComputerMapper.insert(item);
       item.setBusinessKey(EdConst.FIVE_OA_ASSET_COMPUTER+"_"+item.getId());
       fiveAssetComputerMapper.updateByPrimaryKey(item);

        return item.getId();
    }

    @Resource
    FiveOaComputerNetworkService fiveOaComputerNetworkService;

    public void updateByComputerNetWork(int  networkId){
        FiveOaComputerNetworkDto networkDto = fiveOaComputerNetworkService.getModelById(networkId);
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("computerNo",networkDto.getEquipmentNo());
        if (fiveAssetComputerMapper.selectAll(params).size()>0){
            FiveAssetComputer item = fiveAssetComputerMapper.selectAll(params).get(0);
            //硬盘序列号
            item.setHardDiskNo(networkDto.getHardDiskNo());
            //操作系统版本
            item.setOperatingSystem(networkDto.getOperatingSystem());
            //操作系统安装日期
            item.setOperatingSystemTime(networkDto.getOperatingSystemTime());
            //IP
            item.setIpAddress(networkDto.getIpAddress());
            //MAC
            item.setMacAddress(networkDto.getMacAddress());
            //入网类型
            if (networkDto.getNetworkEach()){
                item.setNetwork("互联网");
            }else if (networkDto.getNetworkAlone()){
                item.setNetwork("单机");
            }else if (networkDto.getNetworkNoSecret()){
                item.setNetwork("非密内网");
            }else if (networkDto.getNetworkMiddle()){
                item.setNetwork("中间机");
            } else if (networkDto.getNetworkOther()){
                item.setNetwork("其他");
                item.setRemark(item.getRemark()+"入网类型其他备注："+networkDto.getNetworkOtherRemark()+";");
            }

            //USB口状态
            if (networkDto.getModelUsb()){
                item.setUsbStatus("可用");
            }else {
                item.setUsbStatus("封闭");
            }
            //光驱
            if (networkDto.getModelCd()){
                item.setCdType("可用");
            }else {
                item.setCdType("封闭");
            }
            if (networkDto.getModelOther()){
                item.setRemark(item.getRemark()+"申请开放权限其他备注："+networkDto.getModelOtherRemark()+";");
            }
            item.setUseStatus("在用");
            fiveAssetComputerMapper.updateByPrimaryKey(item);
        }

    }


    @Resource
    FiveOaComputerChangeService fiveOaComputerChangeService;

    //计算机变更数据回填
    public void updateByComputerChange(int changeId){
        FiveOaComputerChangeDto changeDto = fiveOaComputerChangeService.getModelById(changeId);
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("computerNo",changeDto.getComputerNo());
        if (fiveAssetComputerMapper.selectAll(params).size()>0){
            FiveAssetComputer item = fiveAssetComputerMapper.selectAll(params).get(0);
            //责任人
            if (!Strings.isNullOrEmpty(changeDto.getNewDutyLogin())&&changeDto.getNewDutyLogin()!=item.getChargeMan()){
                item.setChargeMan(changeDto.getNewDutyLogin());
                item.setChargeManName(changeDto.getNewDutyName());
            }
            //使用人
            if (!Strings.isNullOrEmpty(changeDto.getNewUsersLogin())&&changeDto.getNewUsersLogin()!=item.getUseLogin()){
                item.setUseLogin(changeDto.getNewUsersLogin());
                item.setUseName(changeDto.getNewUsersName());
            }
            //设备所属单位
            if (!Strings.isNullOrEmpty(changeDto.getNewDeptName())&&changeDto.getNewDeptName()!=item.getDeptName()){
                item.setDeptName(changeDto.getNewDeptName());
                item.setDeptId(changeDto.getNewDeptId());
            }
            //使用情况
            if (!Strings.isNullOrEmpty(changeDto.getNewUseSituation())&&changeDto.getNewUseSituation()!=item.getUseStatus()){
                item.setUseStatus(changeDto.getUseSituation());
            }
            //用途
            if (!Strings.isNullOrEmpty(changeDto.getNewUseWay())&&changeDto.getNewUseWay()!=item.getUseWay()){
                item.setUseWay(changeDto.getNewUseWay());
            }
            //使用类型
            if (!Strings.isNullOrEmpty(changeDto.getNewUseType())&&changeDto.getNewUseType()!=item.getSecurityLevel()){
                item.setSecurityLevel(changeDto.getNewUseType());
            }
            //联网类型
            if (!Strings.isNullOrEmpty(changeDto.getNewNetwork())&&changeDto.getNewNetwork()!=item.getNetwork()){
                item.setNetwork(changeDto.getNewNetwork());
            }
            //放置地点
            if (!Strings.isNullOrEmpty(changeDto.getNewRoom())&&changeDto.getNewRoom()!=item.getRoom()){
                item.setRoom(changeDto.getNewRoom());
            }
            //光驱状态
            if (!Strings.isNullOrEmpty(changeDto.getNewHardDisk())&&changeDto.getNewHardDisk()!=item.getCdType()){
                item.setCdType(changeDto.getNewHardDisk());
            }
            //USB状态
            if (!Strings.isNullOrEmpty(changeDto.getNewUsb())&&changeDto.getNewUsb()!=item.getUsbStatus()){
                item.setUsbStatus(changeDto.getNewUsb());
            }

            item.setGmtModified(new Date());
            fiveAssetComputerMapper.updateByPrimaryKey(item);
        }

    }

    //导出模板
    public List<Map> listMapData1() {
        List<Map> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Map map = new LinkedHashMap();
            map.put("       设备编号       ","");
            map.put("  设备所属单位  ", "");
            map.put(" 责任人 ", "");
            map.put(" 责任人职工号 ", "");
            map.put(" 使用人 ", "");
            map.put(" 使用人职工号 ", "");
            map.put(" 设备序列号 ", "");
            map.put(" 设备名称 ", "");
            map.put(" 用途 (设计,办公)", "");
            map.put(" 使用类别 (内部,涉密,公开)", "");
            map.put(" 联网类型 (互联网,非密内网,单机,中间机,其他)", "");
            map.put(" 初次启用时间 (请按照 2021.01.01 格式填写)", "");
            map.put(" 品牌 ", "");
            map.put(" 型号 ", "");
            map.put(" 放置地点 ", "");
            map.put(" 设备类型 (台式机计算机,便携式计算机,移动存储设备,服务器,网络设备,安全设备,加密USBkey,声像设备,其他)", "");
            map.put(" 显示器类型 ", "");
            map.put(" 使用情况 (在用,未启用,停用,报废) ", "");
            map.put(" 光驱类型 (无,封闭,启用)", "");
            map.put(" USB口状态 (无,封闭,可用)", "");
            map.put(" 硬盘序列号 ", "");
            map.put(" 操作系统 ", "");
            map.put(" 操作系统安装时间 (请按照 2021.01.01 格式填写)", "");
            map.put(" MAC地址 ", "");
            map.put(" IP地址 ", "");
            map.put(" 固定资产编号 ", "");
            map.put("   备注 ", "");

            list.add(map);
        }
        return list;
    }

    /**
     * 导出excl
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    public List<Map<String, String>> listMapData(Map<String,Object> params, String uiSref, String startTime , String endTime, String userLogin){
        List<Map<String, String>> list = new ArrayList<>();

        //模糊匹配查询
        params.put("isLikeSelect",true);
        //数据权限验证
        Map head=Maps.newHashMap();
        head.put("myDeptData",false);
        head.put("uiSref",uiSref);
        head.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(head));
        params.remove("userLogin");//字段中含有userLogin 排除干扰因素
        //为删除 流程已完成
        params.put("deleted",false);
        params.put("processEnd",true);
        //时间端参数
        params.put("startTime",startTime);
        params.put("endTime",endTime);

        List<FiveAssetComputer> computerList = fiveAssetComputerMapper.selectAll(params);
        for (FiveAssetComputer dto:computerList){
            Map<String, String> newMap = new LinkedHashMap<>();
            newMap.put("设备编号",dto.getComputerNo());
            newMap.put("设备所属单位", dto.getDeptName());
            newMap.put("责任人", dto.getChargeManName());
            newMap.put("责任人职工号", dto.getChargeMan());
            newMap.put("使用人", dto.getUseName());

            newMap.put("使用人职工号", dto.getUseLogin());
            newMap.put("设备序列号", dto.getEquipmentNo());
            newMap.put("设备名称", dto.getComputerName());
            newMap.put("用途", dto.getUseWay());
            newMap.put("使用类别", dto.getSecurityLevel());

            newMap.put("联网类型", dto.getNetwork());
            newMap.put("初次启用时间", dto.getUseTime());
            newMap.put("品牌", dto.getComputerBrand());
            newMap.put("型号", dto.getSnNo());
            newMap.put("放置地点", dto.getRoom());

            newMap.put("设备类型", dto.getEquipmentType());
            newMap.put("显示器类型", dto.getDisplayType());
            newMap.put("使用情况", dto.getUseStatus());
            newMap.put("光驱类型", dto.getCdType());
            newMap.put("USB口状态",dto.getUsbStatus());

            newMap.put("硬盘序列号", dto.getHardDiskNo());
            newMap.put("操作系统", dto.getOperatingSystem());
            newMap.put("操作系统安装时间", dto.getOperatingSystemTime());
            newMap.put("MAC地址", dto.getMacAddress());
            newMap.put("IP地址", dto.getIpAddress());

            newMap.put("固定资产编号", dto.getFixedAssetNo());
            newMap.put("备注", dto.getRemark());
            newMap.put("创建人", dto.getCreatorName());
            newMap.put("创建时间", MyDateUtil.dateToStr(dto.getGmtCreate()));//时间格式转换 2021-01-01

            list.add(newMap);
        }
        return list;
    }

    public String insertBatch(InputStream inputStream, String userLogin) throws IOException {
        HrEmployeeDto dto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        List<Map> list = MyPoiUtil.listTableData(inputStream, 1, 0, false);
        int updateNum = 0;
        int insertNum = 0;
        String contractNoA = "";

        for (int i = 0; i < list.size(); i++) {
            Map map = list.get(i);
            FiveAssetComputer item = new FiveAssetComputer();
            //设备编号1computerNo
            String computerNo = map.getOrDefault(1,"").toString();
            item.setComputerNo(computerNo);
            HrEmployeeDto hrEmployeeDto = hrEmployeeService.getModelByUserLogin(userLogin);

            //设备所属单位2deptName****
            String deptName = map.getOrDefault(2,"").toString();
            HrDept hrDept = hrDeptService.selectByName(deptName);//未找到选用登陆人所在的部门
            if (hrDept != null) {
                item.setDeptName(hrDept.getName());
                item.setDeptId(hrDept.getId());
            }else {
                item.setDeptName(hrEmployeeDto.getDeptName());
                item.setDeptId(hrEmployeeDto.getDeptId());
            }
            //责任人3
            String chargeManName = map.getOrDefault(3,"").toString();
            item.setChargeManName(chargeManName);
            //责任人职工号4
            String chargeMan = map.getOrDefault(4,"").toString();
            if (StringUtils.isNotEmpty(chargeMan)){
                item.setChargeMan(  chargeMan.substring(0,4));
            }

            //使用人5
            String useName = map.getOrDefault(5,"").toString();
            item.setUseName(useName);
            //使用人职工号6useLogin
            String useLogin = map.getOrDefault(6,"").toString();
            if (StringUtils.isNotEmpty(useLogin)){
                item.setUseLogin(useLogin.substring(0,4));
            }
            //设备序列号7
            String equipmentNo = map.getOrDefault(7,"").toString();
            item.setEquipmentNo(equipmentNo);
            //设备名称8
            String computerName = map.getOrDefault(8,"").toString();
            item.setComputerName(computerName);
            //用途9
            String useWay = map.getOrDefault(9,"设计").toString();
            item.setUseWay(useWay);
            //使用类别10
            String securityLevel = map.getOrDefault(10,"").toString();
            item.setSecurityLevel(securityLevel);
            //联网类型11
            String netWork = map.getOrDefault(11,"").toString();
            item.setNetwork(netWork);
            //初次启用时间12
            String useTime =map.getOrDefault(12,"").toString();
            item.setUseTime(useTime);
            //品牌13
            String computerBrand =map.getOrDefault(13,"").toString();
            item.setComputerBrand(computerBrand);
            //型号14snNo
            String snNo =map.getOrDefault(14,"").toString();
            item.setSnNo(snNo);
            //放置地点15
            String room=map.getOrDefault(15,"").toString();
            item.setRoom(room);
            //设备类型16
            String equipmentType = map.getOrDefault(16,"台式计算机").toString();
            item.setEquipmentType(equipmentType);
            //显示器类型17
            String displayType=map.getOrDefault(17,"").toString();
            item.setDisplayType(displayType);
            //使用情况18  在用未启用报废停用
            String useStatus = map.getOrDefault(18,"").toString();
            item.setUseStatus(useStatus);
            //光驱类型19
            String cdType = map.getOrDefault(19,"").toString();
            item.setCdType(cdType);
            //USB口状态20
            String usbStatus = map.getOrDefault(20,"可用").toString();
            item.setUsbStatus(usbStatus);
            //硬盘序列号21
            String hardDiskNo=map.getOrDefault(21,"").toString();
            item.setHardDiskNo(hardDiskNo);
            //操作系统22
            String operatingSystem=map.getOrDefault(22,"").toString();
            item.setOperatingSystem(operatingSystem);
            //操作系统安装时间23
            String operatingSystemTime=map.getOrDefault(23,"").toString();
            item.setOperatingSystemTime(operatingSystemTime);
            //MAC地址24
            String macAddress=map.getOrDefault(24,"").toString();
            item.setMacAddress(macAddress);
            //IP地址25
            String ipAddress=map.getOrDefault(25,"").toString();
            item.setIpAddress(ipAddress);

            //固定资产编号26
            String fixedAssetNo=map.getOrDefault(26,"").toString();
            item.setFixedAssetNo(fixedAssetNo);
            //备注
            String remark = map.getOrDefault(27,"").toString();
            item.setRemark(remark);




            String creatorName= map.getOrDefault(28,"").toString();
            if (StringUtils.isNotEmpty(creatorName)){
                item.setCreatorName(creatorName);
                item.setCreator(hrEmployeeSysService.selectByUserName(creatorName));
            }else {
                item.setCreator(hrEmployeeDto.getUserLogin());
                item.setCreatorName(hrEmployeeDto.getUserName());
            }


            //判读是否更新(取没有删除的有编号为computerNo的数据，有就做更新，没有就做新建)
            Map<String, java.io.Serializable> map1 = new HashMap<>();
            map1.put("deleted", false);
            map1.put("computerNo", item.getComputerNo());
            Boolean flag = true;
            if (fiveAssetComputerMapper.selectAll(map1).size() > 0) {
                item = fiveAssetComputerMapper.selectAll(map1).get(0);
                flag = false;
            }
            /*if(item.getComputerNo()==null){
                flag=false;
            }*/

            //插入类型  1补录 2导入  0 新增  数据库没有字段，需要添加
            //item.setInsertType(2);
            //item.setReviewLevel("院级");
            //item.setBackletter(false);//是否保函
            item.setDeleted(false);
            item.setGmtModified(new Date());
            item.setGmtCreate(new Date());
            item.setRemark("系统导入");
            item.setProcessEnd(true);
            ModelUtil.setNotNullFields(item);
            if (flag) {
                fiveAssetComputerMapper.insert(item);
                String businessKey = EdConst.FIVE_OA_ASSET_COMPUTER + "_" + item.getId();
                item.setBusinessKey(businessKey);
                fiveAssetComputerMapper.updateByPrimaryKey(item);
                insertNum++;
            } else {
                fiveAssetComputerMapper.updateByPrimaryKey(item);
                updateNum++;
            }

        }
        return  insertNum + "," + updateNum;
    }

}
