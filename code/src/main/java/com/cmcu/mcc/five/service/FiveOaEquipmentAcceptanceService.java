package com.cmcu.mcc.five.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.service.CommonFileService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyDateUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.five.dao.FiveOaEquipmentAcceptanceDetailMapper;
import com.cmcu.mcc.five.dao.FiveOaEquipmentAcceptanceMapper;
import com.cmcu.mcc.five.dto.FiveOaEquipmentAcceptanceDto;
import com.cmcu.mcc.five.entity.FiveOaEquipmentAcceptance;
import com.cmcu.mcc.five.entity.FiveOaEquipmentAcceptanceDetail;
import com.cmcu.mcc.five.entity.FiveOaInformationEquipmentExamineListDetail;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.service.impl.HandleFormService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.*;


@Service
public class FiveOaEquipmentAcceptanceService extends BaseService {
    @Resource
    FiveOaEquipmentAcceptanceMapper fiveOaEquipmentAcceptanceMapper;
    @Resource
    FiveOaEquipmentAcceptanceDetailMapper fiveOaEquipmentAcceptanceDetailMapper;
    @Resource
    HandleFormService handleFormService;
    @Resource
    HrEmployeeMapper hrEmployeeMapper;
    @Resource
    SelectEmployeeService selectEmployeeService;
    @Resource
    TaskHandleService taskHandleService;
    @Resource
    ProcessQueryService processQueryService;
    @Resource
    CommonFileService commonFileService;
    @Resource
    MyActService myActService;

    public void remove(int id,String userLogin) {
        FiveOaEquipmentAcceptance item = fiveOaEquipmentAcceptanceMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin),"只允许创建人删除！");

        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    public void update(FiveOaEquipmentAcceptance item){
        FiveOaEquipmentAcceptance model = fiveOaEquipmentAcceptanceMapper.selectByPrimaryKey(item.getId());
        model.setRemark(item.getRemark());
        model.setGmtModified(new Date());
        List<FiveOaEquipmentAcceptanceDetail> list = listDetail(item.getId());
        List<String> user=Lists.newArrayList();
        Map variables = Maps.newHashMap();

        variables.put("flag",false);
        variables.put("sign",false);
        for (FiveOaEquipmentAcceptanceDetail detail:list){
            if (StringUtils.isEmpty(detail.getTotalPrice()))continue;
            if (Double.valueOf(detail.getTotalPrice())>=5000){
                variables.put("sign",true);
                //抄送:网络运维中心人员,各单位财务
                variables.put("copyMen", MyStringUtil.listToString(selectEmployeeService.getFinanceChargeMen(item.getDeptId())));
            }else {
                variables.put("flag",true);
                variables.put("financeMan",selectEmployeeService.getFinanceChargeMen(item.getDeptId()));
            }
        }

        myActService.setVariables(model.getProcessInstanceId(),variables);
        fiveOaEquipmentAcceptanceMapper.updateByPrimaryKey(model);

    }

    public FiveOaEquipmentAcceptanceDto getModelById(int id){
        return getDto(fiveOaEquipmentAcceptanceMapper.selectByPrimaryKey(id)) ;
    }

    public int getNewModel(String userLogin){

        FiveOaEquipmentAcceptance item= new FiveOaEquipmentAcceptance();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());

        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());


        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        ModelUtil.setNotNullFields(item);

        fiveOaEquipmentAcceptanceMapper.insert(item);
        String businessKey= EdConst.FIVE_OA_EQUIPMENT_ACCEPTANCE+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "设备验收(办公家具):"+item.getCreatorName());
        variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDeptId()));//发起者部门领导
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_EQUIPMENT_ACCEPTANCE,businessKey, variables, MccConst.APP_CODE);
        item.setBusinessKey(businessKey);
        item.setProcessInstanceId(processInstanceId);
        fiveOaEquipmentAcceptanceMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref, int pageNum, int pageSize) {
        params.put("deleted",false);
        params.put("isLikeSelect",true);

       /* //2020-12-28 当前日期+1天
        if (params.get("endTime").toString()!=""){
            params.put("endTime",MyDateUtil.getNextDay(params.get("endTime").toString(),"1"));
        }*/
        Map map=Maps.newHashMap();
        map.put("myDeptData",false);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));




        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaEquipmentAcceptanceMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaEquipmentAcceptance item=(FiveOaEquipmentAcceptance)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public void removeDetail(int id){
        FiveOaEquipmentAcceptanceDetail model = fiveOaEquipmentAcceptanceDetailMapper.selectByPrimaryKey(id);

        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveOaEquipmentAcceptanceDetailMapper.updateByPrimaryKey(model);
    }

    public FiveOaEquipmentAcceptanceDto getDto(FiveOaEquipmentAcceptance item) {
        FiveOaEquipmentAcceptanceDto dto=FiveOaEquipmentAcceptanceDto.adapt(item);
        dto.setProcessName("已完成");

        if (!dto.getProcessEnd()) {
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if(customProcessInstance ==null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaEquipmentAcceptanceMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance!=null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }
        List<FiveOaEquipmentAcceptanceDetail> fiveOaEquipmentAcceptanceDetails = listDetail(item.getId());
        //计算总价
        String totalContractPrice=MyStringUtil.moneyToString("0",2);
        for (FiveOaEquipmentAcceptanceDetail detail :fiveOaEquipmentAcceptanceDetails){
            if (StringUtils.isEmpty(detail.getTotalPrice()))continue;
            totalContractPrice = MyStringUtil.getNewAddMoney(totalContractPrice,detail.getTotalPrice(),2);
        }
        //如果数据不相同跟新总价
        if (item.getTotalPrice()!=totalContractPrice){
            item.setTotalPrice(totalContractPrice);
            dto.setTotalPrice(totalContractPrice);
            fiveOaEquipmentAcceptanceMapper.updateByPrimaryKey(dto);
        }
       // dto.setTotalPrice(MyStringUtil.moneyToString(dto.getTotalPrice(),2));

        return dto;
    }

    public void updateDetail(FiveOaEquipmentAcceptanceDetail item){
        item.setGmtModified(new Date());
        item.setPrice(MyStringUtil.moneyToString(item.getPrice(),2));


       if( commonFileService.listData(item.getBusinessKey(),0,"").size()>0){
           item.setUploadfile(true);
       }else {
           item.setUploadfile(false);
       }
       item.setPrice(MyStringUtil.moneyToString(item.getPrice(),2));
       item.setTotalPrice(MyStringUtil.moneyToString(item.getTotalPrice(),2));
        fiveOaEquipmentAcceptanceDetailMapper.updateByPrimaryKey(item);
    }

    public FiveOaEquipmentAcceptanceDetail getNewModelDetail(int  id,String userLogin){

        FiveOaEquipmentAcceptanceDetail item=new FiveOaEquipmentAcceptanceDetail();

        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setEquipmentId(id);
        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());



        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);
        item.setUploadfile(false);
        ModelUtil.setNotNullFields(item);
        fiveOaEquipmentAcceptanceDetailMapper.insert(item);
        item.setBusinessKey("oaEquipmentAcceptanceDetail_"+item.getId());
        fiveOaEquipmentAcceptanceDetailMapper.updateByPrimaryKey(item);
        return item;

    }

    public FiveOaEquipmentAcceptanceDetail getModelDetailById(int id){
        return fiveOaEquipmentAcceptanceDetailMapper.selectByPrimaryKey(id);
    }

    public List<FiveOaEquipmentAcceptanceDetail> listDetail(int id){
        Map <String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("equipmentId",id);
        List<FiveOaEquipmentAcceptanceDetail> list = fiveOaEquipmentAcceptanceDetailMapper.selectAll(params);
        return list;
    }

    /**
     * 导出excl
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param userLogin 发起人
     * @return
     */
    public List<Map> listMapData(Map<String,Object> params,String uiSref,String startTime ,String endTime,String userLogin) {
        List<Map> list = new ArrayList<>();
//        Map map = new LinkedHashMap();
//        map.put("验收说明","");
//        map.put("创建人", "");
//        map.put("创建时间", "");
//        map.put("设备名称", "");
//        map.put("固定资产编号", "");
//        map.put("设备规格", "");
//        map.put("所属部门", "");
//        map.put("单价（元）", "");
//        map.put("数量", "");
//        map.put("总价(元)", "");
//        map.put("备注", "");
//        list.add(map);

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
        params.put("startTime",startTime);
        params.put("endTime",endTime);

        List<FiveOaEquipmentAcceptance> fiveOaEquipmentAcceptances = fiveOaEquipmentAcceptanceMapper.selectAll(params);
        for (FiveOaEquipmentAcceptance dto:fiveOaEquipmentAcceptances){
            List<FiveOaEquipmentAcceptanceDetail> fiveOaEquipmentAcceptanceDetails = listDetail(dto.getId());
            for (FiveOaEquipmentAcceptanceDetail detail:fiveOaEquipmentAcceptanceDetails){
                Map map1 = new LinkedHashMap();
                map1.put("验收说明",dto.getRemark());
                map1.put("创建人", dto.getCreatorName());
                map1.put("创建时间", MyDateUtil.dateToStr(dto.getGmtCreate()));//yyyy-MM-dd
                map1.put("设备名称",detail.getEquipmentName());
                map1.put("固定资产编号", detail.getFixedAssetNo());
                map1.put("设备规格", detail.getSpecification());
                map1.put("所属部门", detail.getDeptName());
                map1.put("单价（元）", detail.getPrice());
                map1.put("数量", detail.getNumber());
                map1.put("总价(元)", detail.getTotalPrice());
                map1.put("备注", detail.getRemark());
                list.add(map1);
            }
        }

        return list;
    }

}
