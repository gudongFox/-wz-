package com.cmcu.mcc.five.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyDateUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.dto.ActHistoryDto;
import com.cmcu.mcc.five.dao.FiveOaNonSecretEquipmentScrapDetailMapper;
import com.cmcu.mcc.five.dao.FiveOaNonSecretEquipmentScrapMapper;
import com.cmcu.mcc.five.dto.FiveOaNonSecretEquipmentScrapDto;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class FiveOaNonSecretEquipmentScrapService extends BaseService {
    @Resource
    FiveOaNonSecretEquipmentScrapMapper fiveOaNonSecretEquipmentScrapMapper;

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
    @Autowired
    FiveAssetComputerService fiveAssetComputerService;

    public void remove(int id, String userLogin) {
        FiveOaNonSecretEquipmentScrap item = fiveOaNonSecretEquipmentScrapMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin), "该数据是用户" + item.getCreatorName() + "创建");

        //流程作废
        handleFormService.removeProcessInstance(item.getBusinessKey(), userLogin);

    }

    public void update(FiveOaNonSecretEquipmentScrapDto item) {
        FiveOaNonSecretEquipmentScrap model = fiveOaNonSecretEquipmentScrapMapper.selectByPrimaryKey(item.getId());
        model.setDeptId(item.getDeptId());
        model.setDeptName(item.getDeptName());
        model.setApplyMan(item.getApplyMan());
        model.setApplyManName(item.getApplyManName());
        model.setEquipmentName(item.getEquipmentName());
        model.setEquipmentNo(item.getEquipmentNo());
        model.setEquipmentSerial(item.getEquipmentSerial());
        model.setEquipmentType(item.getEquipmentType());
        model.setHardNo(item.getHardNo());
        model.setAssetsNo(item.getAssetsNo());
        model.setScrapReason(item.getScrapReason());
        model.setSecretHard(item.getSecretHard());
        model.setSecretHardNo(item.getSecretHardNo());
        model.setSecretMemory(item.getSecretMemory());
        model.setSecretMemoryNo(item.getSecretMemoryNo());
        model.setSecretOther(item.getSecretOther());
        model.setSecretOtherNo(item.getSecretOtherNo());
        model.setDeptChargeMen(item.getDeptChargeMen());
        model.setDeptChargeMenName(item.getDeptChargeMenName());
        model.setStartTime(item.getStartTime());
        model.setOriginalValue(item.getOriginalValue());
        model.setDepreciationYear(item.getDepreciationYear());
        model.setDepreciationAlready(item.getDepreciationAlready());
        model.setNetWorth(item.getNetWorth());
        model.setGmtModified(new Date());
        model.setRemark(item.getRemark());
        model.setDisposeAsset(item.getDisposeAsset());
        Map variables = Maps.newHashMap();

        variables.put("processDescription", "非密计算机及外设报废申请:" + model.getDeptName());
        variables.put("deptChargeMenList", MyStringUtil.getStringList(model.getDeptChargeMen()));//发起者部门领导
        variables.put("administrative", selectEmployeeService.getUserListByRoleName("行政事务部人员(设备台帐)"));//行政事务部（固定资产岗）
        variables.put("chiefAccountant", hrEmployeeService.selectUserByPositionName("总会计师"));//总会计师



        //是否为固定资产
        if (model.getDisposeAsset()){
            //copyFinanceMan 抄送各单位财务
            variables.put("sign", true);
            variables.put("copyFinanceMan",MyStringUtil.listToString(selectEmployeeService.getDeptFinanceMan(item.getDeptId())));
        }else {
            variables.put("sign", false);
            variables.put("deptFinanceMan",selectEmployeeService.getDeptFinanceMan(item.getDeptId()));
        }
        myActService.setVariables(model.getProcessInstanceId(), variables);
        BeanValidator.check(model);
        fiveOaNonSecretEquipmentScrapMapper.updateByPrimaryKey(model);


    }

    public FiveOaNonSecretEquipmentScrapDto getModelById(int id) {

        return getDto(fiveOaNonSecretEquipmentScrapMapper.selectByPrimaryKey(id));
    }

    public FiveOaNonSecretEquipmentScrapDto getDto(FiveOaNonSecretEquipmentScrap item) {
        FiveOaNonSecretEquipmentScrapDto dto = FiveOaNonSecretEquipmentScrapDto.adapt(item);
        dto.setProcessName("已完成");
        if (!dto.getProcessEnd()) {
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if(customProcessInstance ==null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaNonSecretEquipmentScrapMapper.updateByPrimaryKey(dto);
                //todo tth 设置对应台账 信息报废
                //fiveAssetComputerMapper 使用情况设置位报废
            }
            if(customProcessInstance!=null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }

        return dto;
    }

    public int getNewModel(String userLogin) {

        FiveOaNonSecretEquipmentScrap item = new FiveOaNonSecretEquipmentScrap();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setApplyMan(hrEmployeeDto.getUserLogin());
        item.setApplyManName(hrEmployeeDto.getUserName());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setDeptChargeMen(StringUtils.join(selectEmployeeService.getDeptChargeMen(item.getDeptId()), ","));
        item.setDeptChargeMenName(selectEmployeeService.getNameByUserLogins(item.getDeptChargeMen()));
        item.setSecretHard(false);
        item.setSecretOther(false);
        item.setSecretMemory(false);
        item.setDisposeAsset(false);
        item.setStartTime(MyDateUtil.getStringToday());
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        ModelUtil.setNotNullFields(item);

        fiveOaNonSecretEquipmentScrapMapper.insert(item);
        String businessKey = EdConst.FIVE_OA_NONSECRETEQUIPMENTSCRAP + "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "非密计算机及外设报废申请");
        variables.put("deptChargeMenList", selectEmployeeService.getDeptChargeMen(item.getDeptId()));//发起者部门领导

        variables.put("financeDeptChargeMan", selectEmployeeService.getDeptChargeMen(18));//财务金融部负责人
        variables.put("administrativeDeptChargeMan", selectEmployeeService.getDeptChargeMen(67));//行政事务部负责人
        variables.put("financeChargeMan", selectEmployeeService.getDeptChargeMen(18));//财务金融部负责人
        variables.put("informationDept", selectEmployeeService.getDeptChargeMen(11));//信息化建设与管理部负责人
        variables.put("administrativeDeptChargeMan", selectEmployeeService.getDeptChargeMen(67));//行政事务部负责人
        variables.put("administrativeDept", selectEmployeeService.getOtherDeptChargeMan(67));//行政事务部主管领导

        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_NONSECRETEQUIPMENTSCRAP, businessKey, variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);
        fiveOaNonSecretEquipmentScrapMapper.updateByPrimaryKey(item);
        return item.getId();
    }


    public PageInfo<Object> listPagedData(Map<String, Object> params, String userLogin, String uiSref, int pageNum, int pageSize) {
        params.put("deleted", false);
        params.put("isLikeSelect",true);

        Map map =new HashMap();
        map.put("myDeptData",false);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaNonSecretEquipmentScrapMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaNonSecretEquipmentScrap item = (FiveOaNonSecretEquipmentScrap) p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }


    // 需要修改打印页
    public Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaNonSecretEquipmentScrap item = fiveOaNonSecretEquipmentScrapMapper.selectByPrimaryKey(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String number = sdf.format(new Date())+item.getCreator().substring((item.getCreator()).length() - 4);
        data.put("tableNo",number);
        data.put("deptName", item.getDeptName());
        data.put("applyManName", item.getApplyManName());
        data.put("deptChargeMenName", item.getDeptChargeMenName());//部门负责人
        data.put("equipmentName", item.getEquipmentName());
        data.put("equipmentNo", item.getEquipmentNo());
        data.put("equipmentSerial", item.getEquipmentSerial());
        data.put("hardNo", item.getHardNo());
        data.put("assetsNo", item.getAssetsNo());
        data.put("scrapReason", item.getScrapReason());

        data.put("secretHard", item.getSecretHard());
        data.put("secretHardNo", item.getSecretHardNo());
        data.put("secretMemory", item.getSecretMemory());
        data.put("secretMemoryNo", item.getSecretMemoryNo());
        data.put("secretOther", item.getSecretOther());
        data.put("secretOtherNo", item.getSecretOtherNo());


        data.put("creatorName", item.getCreatorName());
        data.put("gmtCreate", item.getGmtCreate());

        List<ActHistoryDto> actHistoryDtos = actService.listPassedHistoryDto(item.getProcessInstanceId());
        for (ActHistoryDto dto : actHistoryDtos) {
            if (dto.getActivityName() == null) {
                break;
            }
            if ("发起人部门负责人".equals(dto.getActivityName())) {
                data.put("deptChargeMen", dto);
            }
            if ("信息化建设与管理部-设备".equals(dto.getActivityName())) {
                data.put("informationEquipmentMen", dto);
            }
            if ("财务及金融部负责人".equals(dto.getActivityName())) {
                data.put("financeChargeMen", dto);
            }
            if ("行政事务部负责人".equals(dto.getActivityName())) {
                data.put("administrationChargeMen", dto);
            }
        }
        data.put("nodes", actHistoryDtos);

        return data;
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


        List<FiveOaNonSecretEquipmentScrap> fiveOaNonSecretEquipmentScraps=fiveOaNonSecretEquipmentScrapMapper.selectAll(params);
        for (FiveOaNonSecretEquipmentScrap dto:fiveOaNonSecretEquipmentScraps){
                Map map1=new LinkedHashMap();
                map1.put("设备所属单位",dto.getDeptName());
                map1.put("部门负责人",dto.getDeptChargeMenName());
                map1.put("申请人",dto.getApplyManName());
                map1.put("开始时间",dto.getStartTime());
                map1.put("资产编号",dto.getAssetsNo());
                map1.put("设备名称",dto.getEquipmentName());
                map1.put("信息化设备编号",dto.getEquipmentNo());
                map1.put("设备序列号",dto.getEquipmentSerial());
                map1.put("硬盘序列号",dto.getHardNo());
                map1.put("报废原因",dto.getScrapReason());
                String secretHard="";
                if(dto.getSecretHard()){
                    secretHard+="是";
                }
                map1.put("安全处理记录-硬盘",secretHard);
                map1.put("安全处理-硬盘序列号",dto.getSecretHardNo());
                String secretMemory="";
                if(dto.getSecretMemory()){
                    secretMemory+="是";
                }
                map1.put("安全处理记录-内存",secretMemory);
                map1.put("安全处理-内存序列号",dto.getSecretMemoryNo());
                String secretOther="";
                if(dto.getSecretOther()){
                    secretOther+="是";
                }
                map1.put("安全处理记录-其他",secretOther);
                map1.put("安全处理-其他序列号",dto.getSecretOtherNo());
                map1.put("原值",dto.getOriginalValue());
                map1.put("折旧年限",dto.getDepreciationYear());
                map1.put("已提折旧",dto.getDepreciationAlready());
                map1.put("净值",dto.getNetWorth());
                map1.put("台账是否处理","");//
                map1.put("创建人",dto.getCreatorName());
                map1.put("创建时间",MyDateUtil.dateToStr(dto.getGmtCreate()));
                list.add(map1);
        }
        return list;
    }
}
