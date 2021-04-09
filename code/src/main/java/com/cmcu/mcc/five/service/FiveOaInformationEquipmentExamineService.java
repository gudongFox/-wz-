package com.cmcu.mcc.five.service;


import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.dto.CommonCodeDto;
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

    public void remove(int id,String userLogin){
       FiveOaInformationEquipmentExamine item = fiveOaInformationEquipmentExamineMapper.selectByPrimaryKey(id);
       Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

       //流程作废
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


      //如果设备编号生成后，已经录入台账 不在验证序列号重复
       if (!Strings.isNullOrEmpty(model.getEquipmentNo())&&Strings.isNullOrEmpty(model.getFormNo())&&!model.getEquipmentNo().equals("无")){
           ModelUtil.setNotNullFields(model);
           Map map=Maps.newHashMap();
           map.put("deleted",false);
           map.put("equipmentNo",model.getEquipmentNo());
           Assert.state(fiveOaInformationEquipmentExamineMapper.selectAll(map).size()<=1,"请勿在信息化设备验收审批中重复使用,设备序列号："+model.getEquipmentNo()+"");
           Assert.state(fiveAssetComputerMapper.selectAll(map).size()<=0,"已在台账中录入,设备序列号为："+model.getEquipmentNo()+"的设备，请勿重复录入！");

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
              //抄送:各单位财务
              variables.put("copyMen", MyStringUtil.listToString(selectEmployeeService.getFinanceChargeMen(item.getDeptId())));
          }
      }

       fiveOaInformationEquipmentExamineMapper.updateByPrimaryKey(model);
       variables.put("processDescription","信息化设备验收审批 "+item.getEquipmentName());
       BeanValidator.check(model);
       myActService.setVariables(model.getProcessInstanceId(),variables);


   }

    public FiveOaInformationEquipmentExamineDto getModelById(int id){

           return getDto(fiveOaInformationEquipmentExamineMapper.selectByPrimaryKey(id));
    }

    public FiveOaInformationEquipmentExamineDto getDto(FiveOaInformationEquipmentExamine item) {
        FiveOaInformationEquipmentExamineDto dto=FiveOaInformationEquipmentExamineDto.adapt(item);
        dto.setProcessName("已完成");

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
       item.setUseType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"使用方式").toString());
       item.setUsbType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"usb状态").toString());
       ModelUtil.setNotNullFields(item);
       fiveOaInformationEquipmentExamineMapper.insert(item);

       String businessKey= EdConst.FIVE_OA_INFORMATIONEQUIPMENTEXAMINE+ "_" + item.getId();
       Map variables = Maps.newHashMap();
       variables.put("userLogin", userLogin);
       variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDeptId()));
       variables.put("processDescription","信息化设备验收审批 ");
       variables.put("administrativeDeptEquipment", selectEmployeeService.getUserListByRoleName("行政事务部人员(设备台帐)"));//行政事务部人员（设备台帐）
       variables.put("financeDeptChargeMan", selectEmployeeService.getParentDeptChargeMen(18,1,false));//财务金融部负责人
       variables.put("administrativeDeptChargeMan", selectEmployeeService.getParentDeptChargeMen(67,1,false));//行政事务部负责人
       variables.put("qualityDeptChargeMen",selectEmployeeService.getParentDeptChargeMen(11,1,false));//信息化建设与管理部负责人

       String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_INFORMATIONEQUIPMENTEXAMINE,businessKey, variables, MccConst.APP_CODE);
       item.setProcessInstanceId(processInstanceId);
       item.setBusinessKey(businessKey);
       fiveOaInformationEquipmentExamineMapper.updateByPrimaryKey(item);
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

            if ("行政事务部人员(设备台帐)".equals(dto.getActivityName())&&dto.getActivityName()!=null){
                data.put("startMan",dto);
            }
            if ("行政事务部负责人".equals(dto.getActivityName())&&dto.getActivityName()!=null){
                data.put("affairMan",dto);
            }
            if ("信息化建设与管理部".equals(dto.getActivityName())&&dto.getActivityName()!=null){
                data.put("technologyMan",dto);
            }
            if (StringUtils.isNotBlank(dto.getActivityName())){
                if (dto.getActivityName().contains("财务")&&dto.getActivityName()!=null){
                    data.put("financeMan",dto);
                }
            }


        }
        data.put("nodes",actHistoryDtos);

        return data;
    }

    /**
     * 01-台式计算机：台式计算机、工作站等
     * 02-便携式计算机：便携式计算机、移动工作站等
     * 03-图文加工设备：打印机、扫描仪、复印机、晒图仪、绘图仪、多功能一体机等
     * 04-移动存储设备：U盘、移动硬盘等
     * 05-服务器
     * 06-网络设备：交换机、路由器、网关
     * 07-安全设备
     * 08-加密USBKey：加密狗、网盾等
     * 09-声像设备：投影仪、视频会议设备、数字化会议设备等
     * 10-其他：传真机、碎纸机等
     * 类型编号 + 年份 +流水号    012020001
     * @param id
     */
    @Resource
    FiveAssetComputerService fiveAssetComputerService;
    public void getEquipmentExamineNo(int id){
        FiveOaInformationEquipmentExamineDto modelById = getModelById(id);
        CommonCodeDto commonCodeDto = commonCodeService.getDataByCatalogAndName(MccConst.APP_CODE, "信息化设备类型", modelById.getOsInstallTime());
        String eqNo=commonCodeDto.getCode();
        int year=MyDateUtil.getYear();
        Map map=Maps.newHashMap();
        map.put("deleted",false);
        map.put("equipmentType",modelById.getOsInstallTime());
        map.put("formNoNull",eqNo+year);//查询 012020001
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
        //如果验收表设备编号为空 生成 否走不重新生成合同号
       if (!StringUtils.isNotBlank(modelById.getFormNo())){//null  "" " "都判断是没有编号
           modelById.setFormNo(computerNo);//设备编号
           modelById.setGmtModified(new Date());
           fiveOaInformationEquipmentExamineMapper.updateByPrimaryKey(modelById);

       }
        //同步生成 台账  涉密的不生成台账
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("computerNo",modelById.getFormNo());
        if (fiveAssetComputerMapper.selectAll(params).size()==0){
            if (!modelById.getSecretRank().equals("涉密")){
                int computer = fiveAssetComputerService.getModelByEquipmentExamine(id,true);
            }
        }
    }

    /**
     * 导出excl
     * @param startTime1 开始时间
     * @param endTime1 结束时间
     * @return
     */
    public List<Map> listMapData(Map<String,Object> params,  String uiSref,String startTime1 ,String endTime1,String userLogin){
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
        List<FiveOaInformationEquipmentExamine> fiveOaInformationEquipmentExamines=fiveOaInformationEquipmentExamineMapper.selectAll(params);
        for (FiveOaInformationEquipmentExamine dto:fiveOaInformationEquipmentExamines){
            Map map1=new LinkedHashMap();
            map1.put("设备名称",dto.getEquipmentName());
            map1.put("设备序列号",dto.getEquipmentNo());
            map1.put("设备编号",dto.getFormNo());
            map1.put("所属单位",dto.getDeptName());
            map1.put("使用类别",dto.getSecretRank());
            map1.put("责任人",dto.getChargeManName() );
            map1.put("责任人工号", dto.getChargeMan());
            map1.put("使用人",dto.getUserName() );
            map1.put("启用时间",dto.getStartTime() );
            map1.put("设备品牌", dto.getBrand());
            map1.put("设备型号",dto.getType());
            map1.put("用途",dto.getUseType());
            map1.put("放置地点",dto.getAddress());
            map1.put("显示器型号",dto.getDisplayNo());
            map1.put("设备类型",dto.getOsInstallTime());
            map1.put("采购审批编号",dto.getDiskNo());
            map1.put("验收价格（元）",dto.getCheckPrice());
            map1.put("备注",dto.getMacAddress());
            map1.put("资产编号",dto.getFixedAssetNo());
            map1.put("创建人",dto.getCreatorName());
            map1.put("创建时间",MyDateUtil.dateToStr(dto.getGmtCreate()));
            list.add(map1);
        }
        return list;
    }

}
