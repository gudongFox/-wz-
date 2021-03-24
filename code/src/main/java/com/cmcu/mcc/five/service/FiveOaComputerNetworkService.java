package com.cmcu.mcc.five.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyDateUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.dto.ActHistoryDto;
import com.cmcu.mcc.five.dao.FiveOaComputerNetworkDetailMapper;
import com.cmcu.mcc.five.dao.FiveOaComputerNetworkMapper;
import com.cmcu.mcc.five.dto.FiveOaComputerNetworkDto;

import com.cmcu.mcc.five.entity.FiveOaComputerNetwork;
import com.cmcu.mcc.five.entity.FiveOaComputerNetworkDetail;

import com.cmcu.mcc.five.entity.FiveOaEquipmentAcceptance;
import com.cmcu.mcc.five.entity.FiveOaEquipmentAcceptanceDetail;
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
public class FiveOaComputerNetworkService extends BaseService {
    @Resource
    FiveOaComputerNetworkMapper fiveOaComputerNetworkMapper;
    @Resource
    FiveOaComputerNetworkDetailMapper fiveOaComputerNetworkDetailMapper;
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
    HandleFormService handleFormService;
    @Resource
    FiveAssetComputerService fiveAssetComputerService;

    public void remove(int id,String userLogin){
        FiveOaComputerNetwork item = fiveOaComputerNetworkMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

        //流程作废
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

    }

    public void update(FiveOaComputerNetworkDto fiveOaComputerNetworkDto){
        FiveOaComputerNetwork model = fiveOaComputerNetworkMapper.selectByPrimaryKey(fiveOaComputerNetworkDto.getId());
        model.setGmtModified(new Date());
        model.setRemark(fiveOaComputerNetworkDto.getRemark());
        model.setDeptId(fiveOaComputerNetworkDto.getDeptId());
        model.setDeptName(fiveOaComputerNetworkDto.getDeptName());
        model.setEquipmentNo(fiveOaComputerNetworkDto.getEquipmentNo());
        model.setEquipmentName(fiveOaComputerNetworkDto.getEquipmentName());
        model.setEquipmentType(fiveOaComputerNetworkDto.getEquipmentType());
        model.setChargeMan(fiveOaComputerNetworkDto.getChargeMan());
        model.setChargeManName(fiveOaComputerNetworkDto.getChargeManName());
        model.setUserLogin(fiveOaComputerNetworkDto.getUserLogin());
        model.setUserName(fiveOaComputerNetworkDto.getUserName());
        model.setLinkPhone(fiveOaComputerNetworkDto.getLinkPhone());
        model.setNetworkAlone(fiveOaComputerNetworkDto.getNetworkAlone());
        model.setNetworkEach(fiveOaComputerNetworkDto.getNetworkEach());
        model.setNetworkMiddle(fiveOaComputerNetworkDto.getNetworkMiddle());
        model.setNetworkNoSecret(fiveOaComputerNetworkDto.getNetworkNoSecret());
        model.setNetworkOther(fiveOaComputerNetworkDto.getNetworkOther());
        model.setNetworkOtherRemark(fiveOaComputerNetworkDto.getNetworkOtherRemark());
        model.setModelCd(fiveOaComputerNetworkDto.getModelCd());
        model.setModelUsb(fiveOaComputerNetworkDto.getModelUsb());
        model.setModelOther(fiveOaComputerNetworkDto.getModelOther());
        model.setModelOtherRemark(fiveOaComputerNetworkDto.getModelOtherRemark());
        model.setApplyReason(fiveOaComputerNetworkDto.getApplyReason());

        model.setOperatingSystem(fiveOaComputerNetworkDto.getOperatingSystem());
        model.setOperatingSystemTime(fiveOaComputerNetworkDto.getOperatingSystemTime());
        model.setHardDiskNo(fiveOaComputerNetworkDto.getHardDiskNo());
        model.setMacAddress(fiveOaComputerNetworkDto.getMacAddress());
        model.setSerialNo( fiveOaComputerNetworkDto.getSerialNo());

        model.setIpAddress(fiveOaComputerNetworkDto.getIpAddress());
        BeanValidator.check(model);
        fiveOaComputerNetworkMapper.updateByPrimaryKey(model);
    }

    public FiveOaComputerNetworkDto getModelById(int id){

        return getDto(fiveOaComputerNetworkMapper.selectByPrimaryKey(id));
    }

    public FiveOaComputerNetworkDto getDto(FiveOaComputerNetwork item) {
        FiveOaComputerNetworkDto dto=FiveOaComputerNetworkDto.adapt(item);
        dto.setProcessName("已完成");

        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if(customProcessInstance == null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaComputerNetworkMapper.updateByPrimaryKey(dto);
                //入网完成-转台账存储
                fiveAssetComputerService.updateByComputerNetWork(dto.getId());
            }

            if(customProcessInstance != null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }

        return dto;
    }

    public int getNewModel(String userLogin){

        FiveOaComputerNetwork item=new FiveOaComputerNetwork();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setNetworkAlone(false);
        item.setNetworkEach(false);
        item.setNetworkMiddle(false);
        item.setNetworkNoSecret(false);
        item.setNetworkOther(false);
        ModelUtil.setNotNullFields(item);
        fiveOaComputerNetworkMapper.insert(item);
        String businessKey= EdConst.FIVE_OA_COMPUTER_NETWORK+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "非密信息化设备安全准入审批："+item.getDeptName());
        variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDeptId()));//发起者部门领导
        variables.put("computerLeader",selectEmployeeService.getUserListByRoleName("网络运维中心网络处理"));//网络运维中心网络处理
        variables.put("informationEquipmentMen",selectEmployeeService.getUserListByRoleName("信息化建设与管理部(信息化设备)"));//信息化建设与管理部人员（信息化设备）
        item.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_COMPUTER_NETWORK,item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        fiveOaComputerNetworkMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
        params.put("isLikeSelect",true);

         List<Integer> deptIdList=hrEmployeeSysService.getMyDeptListOa(userLogin,uiSref);
         if (deptIdList.size()==0){
            params.put("creator",userLogin);
        }else {
            params.put("deptIdList",deptIdList);
        }
        params.remove("userLogin");
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaComputerNetworkMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaComputerNetwork item=(FiveOaComputerNetwork)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public void removeDetail(int id){
        FiveOaComputerNetworkDetail model = fiveOaComputerNetworkDetailMapper.selectByPrimaryKey(id);

        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveOaComputerNetworkDetailMapper.updateByPrimaryKey(model);
    }

    public void updateDetail(FiveOaComputerNetworkDetail fiveOaComputerNetworkDetail){
        FiveOaComputerNetworkDetail model = fiveOaComputerNetworkDetailMapper.selectByPrimaryKey(fiveOaComputerNetworkDetail.getId());
      model.setDeptId(fiveOaComputerNetworkDetail.getDeptId());
      model.setDeptName(fiveOaComputerNetworkDetail.getDeptName());
      model.setUserName(fiveOaComputerNetworkDetail.getUserName());
      model.setRoom(fiveOaComputerNetworkDetail.getRoom());
      model.setUserLogin(fiveOaComputerNetworkDetail.getUserLogin());
      model.setPhone(fiveOaComputerNetworkDetail.getPhone());
      model.setComputerType(fiveOaComputerNetworkDetail.getComputerType());
      model.setComputerMacAddress(fiveOaComputerNetworkDetail.getComputerMacAddress());
      model.setNetworkType(fiveOaComputerNetworkDetail.getNetworkType());
      fiveOaComputerNetworkDetailMapper.updateByPrimaryKey(model);
    }

    public int getNewModelDetail(int networkId){
        FiveOaComputerNetworkDetail item=new FiveOaComputerNetworkDetail();
        FiveOaComputerNetwork fiveOaComputerNetwork = fiveOaComputerNetworkMapper.selectByPrimaryKey(networkId);
        item.setComputerNetworkId(networkId);
        item.setDeptId(fiveOaComputerNetwork.getDeptId());
        item.setDeptName(fiveOaComputerNetwork.getDeptName());

        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);
        ModelUtil.setNotNullFields(item);
        fiveOaComputerNetworkDetailMapper.insert(item);
        return item.getId();

    }

    public FiveOaComputerNetworkDetail getModelDetailById(int id){
        return fiveOaComputerNetworkDetailMapper.selectByPrimaryKey(id);
    }

    public List<FiveOaComputerNetworkDetail> listDetail(int networkId){
        Map <String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("computerNetworkId",networkId);
        List<FiveOaComputerNetworkDetail> list = fiveOaComputerNetworkDetailMapper.selectAll(params);
        return list;
    }

    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaComputerNetwork item = fiveOaComputerNetworkMapper.selectByPrimaryKey(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String number = sdf.format(new Date())+item.getCreator().substring((item.getCreator()).length() - 4);
        data.put("tableNo",number);
        data.put("deptName",item.getDeptName());
        data.put("creatorName",item.getCreatorName());
        data.put("gmtCreate",item.getGmtCreate());
        data.put("userLogin",item.getUserLogin());
        data.put("userName",item.getUserName());
        data.put("remark",item.getRemark());

        data.put("equipmentNo",item.getEquipmentNo());
        data.put("equipmentName",item.getEquipmentName());
        data.put("equipmentType",item.getEquipmentType());
        data.put("chargeMan",item.getChargeMan());
        data.put("chargeManName",item.getChargeManName());
        data.put("linkPhone",item.getLinkPhone());
        data.put("networkEach",item.getNetworkEach());
        data.put("applyReason",item.getApplyReason());
        data.put("networkAlone",item.getNetworkAlone());
        data.put("networkMiddle",item.getNetworkMiddle());
        data.put("networkNoSecret",item.getNetworkNoSecret());
        data.put("networkOther",item.getNetworkOther());
        data.put("networkOtherRemark",item.getNetworkOtherRemark());
        data.put("modelCd",item.getModelCd());
        data.put("modelUsb",item.getModelUsb());
        data.put("modelOther",item.getModelOther());
        data.put("modelOtherRemark",item.getModelOtherRemark());
      /*  Map map =new HashMap();
        map.put("computerNetworkId",item.getId());
        map.put("deleted",false);
        List<FiveOaComputerNetworkDetail> computerNetworkDetails = fiveOaComputerNetworkDetailMapper.selectAll (map);
        data.put("computerNetworkDetails",computerNetworkDetails);*/

        List<ActHistoryDto> actHistoryDtos = actService.listPassedHistoryDto(item.getProcessInstanceId());
        for (ActHistoryDto dto:actHistoryDtos){
            if (dto.getActivityName()==null){
                break;
            }
            if ("部门领导".equals(dto.getActivityName())){
                data.put("deptChargeMen",dto);
            }
            if ("信息化建设与管理部人员（信息化设备）".equals(dto.getActivityName())){
                data.put("informationEquipmentMen",dto);
            }
            if ("网络运维中心网络处理".equals(dto.getActivityName())){
                data.put("computerLeader",dto);
            }
        }
        data.put("nodes",actHistoryDtos);

        return data;
    }

    /**
     * 导出excl
     * @param startTime1 开始时间
     * @param endTime1 结束时间
     * @param userName 发起人
     * @return
     */
    public List<Map> listMapData(Map<String,Object> params,  String uiSref,String startTime1 ,String endTime1,String userLogin,String userName){
        List<Map> list = new ArrayList<>();
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
        params.put("startTime1",startTime1);
        params.put("endTime1",endTime1);
        params.put("creator",userName);



        List<FiveOaComputerNetwork> fiveOaComputerNetworks = fiveOaComputerNetworkMapper.selectAll(params);
        for (FiveOaComputerNetwork dto:fiveOaComputerNetworks){
                Map map1 = new LinkedHashMap();
                map1.put("申请单位",dto.getDeptName());
                map1.put("信息化设备编号",dto.getEquipmentNo());
                map1.put("责任人",dto.getChargeManName());
                map1.put("责任人职工号",dto.getChargeMan());
                map1.put("使用人",dto.getUserName());
                map1.put("使用人职工号",dto.getUserLogin());
                map1.put("设备名称",dto.getEquipmentName());
                map1.put("设备类型",dto.getEquipmentType());
                map1.put("联系电话",dto.getLinkPhone());
                map1.put("设备序列号",dto.getSerialNo());

                String network="";
                if(dto.getNetworkEach()==true) {
                    network= "互联网";
                }
                if(dto.getNetworkAlone()==true) {
                    network ="单机";
                }
                if(dto.getNetworkMiddle()==true) {
                    network= "中间机";
                }
                if(dto.getNetworkOther()){
                    network="其他";
                }
                map1.put("接入网络", network);

                String modelAble="";
                if (dto.getModelCd()){
                    modelAble+="光驱,";
                }if (dto.getModelUsb()){
                    modelAble+="USB,";
                }if (dto.getModelOther()){
                    modelAble+="其他,";
                }
                map1.put("申请开放权限",modelAble);

                map1.put("申请用途",dto.getApplyReason());
                map1.put("备注",dto.getRemark());

                map1.put("硬盘序列号",dto.getHardDiskNo());
                map1.put("操作系统", dto.getOperatingSystem());
                map1.put("操作系统安装时间", dto.getOperatingSystemTime());
                map1.put("MAC地址", dto.getMacAddress());
                map1.put("IP地址", dto.getIpAddress());
                map1.put("创建人", dto.getCreatorName());
                map1.put("创建时间", MyDateUtil.dateToStr(dto.getGmtCreate()));//yyyy-MM-dd


                /*map1.put("变更后-责任人姓名","");*/

            list.add(map1);

        }

        return list;
    }
}
