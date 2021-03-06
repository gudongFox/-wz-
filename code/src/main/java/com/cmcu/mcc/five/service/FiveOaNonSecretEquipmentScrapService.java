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
        Assert.state(item.getCreator().equals(userLogin), "??????????????????" + item.getCreatorName() + "??????");

        //????????????
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

        variables.put("processDescription", "????????????????????????????????????:" + model.getDeptName());
        variables.put("deptChargeMenList", MyStringUtil.getStringList(model.getDeptChargeMen()));//?????????????????????
        variables.put("administrative", selectEmployeeService.getUserListByRoleName("?????????????????????(????????????)"));//????????????????????????????????????
        variables.put("chiefAccountant", hrEmployeeService.selectUserByPositionName("????????????"));//????????????



        //?????????????????????
        if (model.getDisposeAsset()){
            //copyFinanceMan ?????????????????????
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
        dto.setProcessName("?????????");
        if (!dto.getProcessEnd()) {
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if(customProcessInstance ==null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaNonSecretEquipmentScrapMapper.updateByPrimaryKey(dto);
                //todo tth ?????????????????? ????????????
                //fiveAssetComputerMapper ???????????????????????????
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
        variables.put("processDescription", "????????????????????????????????????");
        variables.put("deptChargeMenList", selectEmployeeService.getDeptChargeMen(item.getDeptId()));//?????????????????????

        variables.put("financeDeptChargeMan", selectEmployeeService.getDeptChargeMen(18));//????????????????????????
        variables.put("administrativeDeptChargeMan", selectEmployeeService.getDeptChargeMen(67));//????????????????????????
        variables.put("financeChargeMan", selectEmployeeService.getDeptChargeMen(18));//????????????????????????
        variables.put("informationDept", selectEmployeeService.getDeptChargeMen(11));//????????????????????????????????????
        variables.put("administrativeDeptChargeMan", selectEmployeeService.getDeptChargeMen(67));//????????????????????????
        variables.put("administrativeDept", selectEmployeeService.getOtherDeptChargeMan(67));//???????????????????????????

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


    // ?????????????????????
    public Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaNonSecretEquipmentScrap item = fiveOaNonSecretEquipmentScrapMapper.selectByPrimaryKey(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String number = sdf.format(new Date())+item.getCreator().substring((item.getCreator()).length() - 4);
        data.put("tableNo",number);
        data.put("deptName", item.getDeptName());
        data.put("applyManName", item.getApplyManName());
        data.put("deptChargeMenName", item.getDeptChargeMenName());//???????????????
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
            if ("????????????????????????".equals(dto.getActivityName())) {
                data.put("deptChargeMen", dto);
            }
            if ("???????????????????????????-??????".equals(dto.getActivityName())) {
                data.put("informationEquipmentMen", dto);
            }
            if ("???????????????????????????".equals(dto.getActivityName())) {
                data.put("financeChargeMen", dto);
            }
            if ("????????????????????????".equals(dto.getActivityName())) {
                data.put("administrationChargeMen", dto);
            }
        }
        data.put("nodes", actHistoryDtos);

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


        List<FiveOaNonSecretEquipmentScrap> fiveOaNonSecretEquipmentScraps=fiveOaNonSecretEquipmentScrapMapper.selectAll(params);
        for (FiveOaNonSecretEquipmentScrap dto:fiveOaNonSecretEquipmentScraps){
                Map map1=new LinkedHashMap();
                map1.put("??????????????????",dto.getDeptName());
                map1.put("???????????????",dto.getDeptChargeMenName());
                map1.put("?????????",dto.getApplyManName());
                map1.put("????????????",dto.getStartTime());
                map1.put("????????????",dto.getAssetsNo());
                map1.put("????????????",dto.getEquipmentName());
                map1.put("?????????????????????",dto.getEquipmentNo());
                map1.put("???????????????",dto.getEquipmentSerial());
                map1.put("???????????????",dto.getHardNo());
                map1.put("????????????",dto.getScrapReason());
                String secretHard="";
                if(dto.getSecretHard()){
                    secretHard+="???";
                }
                map1.put("??????????????????-??????",secretHard);
                map1.put("????????????-???????????????",dto.getSecretHardNo());
                String secretMemory="";
                if(dto.getSecretMemory()){
                    secretMemory+="???";
                }
                map1.put("??????????????????-??????",secretMemory);
                map1.put("????????????-???????????????",dto.getSecretMemoryNo());
                String secretOther="";
                if(dto.getSecretOther()){
                    secretOther+="???";
                }
                map1.put("??????????????????-??????",secretOther);
                map1.put("????????????-???????????????",dto.getSecretOtherNo());
                map1.put("??????",dto.getOriginalValue());
                map1.put("????????????",dto.getDepreciationYear());
                map1.put("????????????",dto.getDepreciationAlready());
                map1.put("??????",dto.getNetWorth());
                map1.put("??????????????????","");//
                map1.put("?????????",dto.getCreatorName());
                map1.put("????????????",MyDateUtil.dateToStr(dto.getGmtCreate()));
                list.add(map1);
        }
        return list;
    }
}
