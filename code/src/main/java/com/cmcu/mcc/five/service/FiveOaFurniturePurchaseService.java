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
import com.cmcu.mcc.five.dao.FiveOaFurniturePurchaseDetailMapper;
import com.cmcu.mcc.five.dao.FiveOaFurniturePurchaseMapper;
import com.cmcu.mcc.five.dto.FiveOaFurniturePurchaseDto;
import com.cmcu.mcc.five.entity.FiveOaEquipmentAcceptanceDetail;
import com.cmcu.mcc.five.entity.FiveOaFurniturePurchase;
import com.cmcu.mcc.five.entity.FiveOaFurniturePurchaseDetail;
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
public class FiveOaFurniturePurchaseService extends BaseService {
    @Resource
    FiveOaFurniturePurchaseMapper fiveOaFurniturePurchaseMapper;
    @Resource
    FiveOaFurniturePurchaseDetailMapper fiveOaFurniturePurchaseDetailMapper;
    @Resource
    HandleFormService handleFormService;
    @Resource
    MyActService myActService;
    @Resource
    SelectEmployeeService selectEmployeeService;
    @Resource
    ProcessQueryService processQueryService;
    @Resource
    TaskHandleService taskHandleService;

    public void remove(int id,String userLogin){
        FiveOaFurniturePurchase item = fiveOaFurniturePurchaseMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

        //流程作废
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

    }

    public void update(FiveOaFurniturePurchaseDto item){
        FiveOaFurniturePurchase model = fiveOaFurniturePurchaseMapper.selectByPrimaryKey(item.getId());
        model.setDeptId(item.getDeptId());
        model.setDeptName(item.getDeptName());
        model.setTotalPrice(item.getTotalPrice());
        model.setPurchaseReason(item.getPurchaseReason());
        model.setGmtModified(new Date());
        model.setRemark(item.getRemark());
        Map variables = Maps.newHashMap();
        variables.put("processDescription","办公家具采购:"+model.getDeptName());
        variables.put("deptChargeMen",selectEmployeeService.getParentDeptChargeMen(item.getDeptId(),1,true));//发起者部门领导
        variables.put("deptOtherChargeMen",selectEmployeeService.getParentDeptChargeMen(item.getDeptId(),4,true));//发起者部门分管领导
        myActService.setVariables(model.getProcessInstanceId(),variables);
        BeanValidator.check(model);
        fiveOaFurniturePurchaseMapper.updateByPrimaryKey(model);

    }

    public FiveOaFurniturePurchaseDto getDto(FiveOaFurniturePurchase item) {
        FiveOaFurniturePurchaseDto dto=FiveOaFurniturePurchaseDto.adapt(item);

        dto.setProcessName("已完成");

        if (!dto.getProcessEnd()) {

            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if(customProcessInstance ==null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaFurniturePurchaseMapper.updateByPrimaryKey(dto);
            }

            if(customProcessInstance!=null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }
        List<FiveOaFurniturePurchaseDetail> list = listDetail(item.getId());
        //计算总价
        String totalContractPrice= MyStringUtil.moneyToString("0",2);
        for (FiveOaFurniturePurchaseDetail detail :list){
            if (StringUtils.isEmpty(detail.getTotalPrice()))continue;
            totalContractPrice = MyStringUtil.getNewAddMoney(totalContractPrice,detail.getTotalPrice(),2);
        }
        //如果数据不相同跟新总价
        if (item.getTotalPrice()!=totalContractPrice){
            dto.setTotalPrice(totalContractPrice);
            fiveOaFurniturePurchaseMapper.updateByPrimaryKey(dto);
        }
        return dto;
    }

    public FiveOaFurniturePurchaseDto getModelById(int id){

        return getDto(fiveOaFurniturePurchaseMapper.selectByPrimaryKey(id));
    }

    public int getNewModel(String userLogin){


        FiveOaFurniturePurchase item=new FiveOaFurniturePurchase();

        HrEmployeeDto hrEmployeeDto = selectEmployeeService.selectByUserLogin(userLogin);

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

        fiveOaFurniturePurchaseMapper.insert(item);
        String businessKey= EdConst.FIVE_OA_FURNITURE_PURCHASE+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "办公家具采购:"+item.getDeptName());
        variables.put("deptChargeMen",selectEmployeeService.getParentDeptChargeMen(item.getDeptId(),1,true));//发起者部门领导
        variables.put("deptOtherChargeMen",selectEmployeeService.getParentDeptChargeMen(item.getDeptId(),4,true));//发起者部门分管领导
        variables.put("administrativeLeader",selectEmployeeService.getParentDeptChargeMen(67,1,true));//行政事务部负责人

        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_FURNITURE_PURCHASE,businessKey, variables, MccConst.APP_CODE);
        item.setBusinessKey(businessKey);
        item.setProcessInstanceId(processInstanceId);
        fiveOaFurniturePurchaseMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref, int pageNum, int pageSize) {
        params.put("deleted",false);
        params.put("isLikeSelect",true);

        Map map =new HashMap();
        map.put("myDeptData",false);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaFurniturePurchaseMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaFurniturePurchase item=(FiveOaFurniturePurchase)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }



    /**
     * 导出excel
     * @param uiSref
     * @param startTime1
     * @param endTime1
     * @return
     */
    public List<Map> listMapData( String uiSref,String startTime1 ,String endTime1,String userName){
        List<Map> list = new ArrayList<>();
        Map map=new LinkedHashMap();
        map.put("采购部门","");
        map.put("采购理由","");
        map.put("备注","");
        map.put("创建人","");
        map.put("创建时间","");
        map.put("名称","");
        map.put("家具数量","");
        map.put("单价(元)","");
        map.put("总价(元)","");
        map.put("备注","");
        list.add(map);

        //数据权限验证
        Map head=Maps.newHashMap();
        head.put("myDeptData",false);
        head.put("uiSref",uiSref);

        Map <String,Object> params=Maps.newHashMap();
        //模糊匹配查询
        params.put("isLikeSelect",true);
        params.putAll(getDefaultRightParams(head));
        params.remove("userLogin");//字段中含有userLogin 排除干扰因素
        //为删除 流程已完成
        params.put("deleted",false);
        params.put("processEnd",true);
        //时间端参数
        params.put("startTime1",startTime1);
        params.put("endTime1",endTime1);
        params.put("creator",userName);


        List<FiveOaFurniturePurchase> fiveOaFurniturePurchases=fiveOaFurniturePurchaseMapper.selectAll(params);
        for (FiveOaFurniturePurchase dto:fiveOaFurniturePurchases){
            List<FiveOaFurniturePurchaseDetail> fiveOaFurniturePurchaseDetails=listDetail(dto.getId());
            for (FiveOaFurniturePurchaseDetail detail:fiveOaFurniturePurchaseDetails){
                Map map1=new LinkedHashMap();
                map1.put("采购部门",dto.getDeptName());
                map1.put("采购理由",dto.getPurchaseReason());
                map1.put("备注",dto.getRemark());
                map1.put("创建人",dto.getCreatorName());
                map1.put("创建时间", MyDateUtil.dateToStr(dto.getGmtCreate()));
                map1.put("名称",detail.getFurnitureName());
                map1.put("家具数量",detail.getNumber());
                map1.put("单价(元)",detail.getPrice());
                map1.put("总价(元)",detail.getTotalPrice());
                map1.put("详情备注",detail.getRemark());
                list.add(map1);
            }
        }
        return list;
    }


    public void removeDetail(int id){
        FiveOaFurniturePurchaseDetail model = fiveOaFurniturePurchaseDetailMapper.selectByPrimaryKey(id);

        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveOaFurniturePurchaseDetailMapper.updateByPrimaryKey(model);
    }

    public void updateDetail(FiveOaFurniturePurchaseDetail item){
        FiveOaFurniturePurchaseDetail model = fiveOaFurniturePurchaseDetailMapper.selectByPrimaryKey(item.getId());
        model.setRemark(item.getRemark());
        model.setFurnitureName(item.getFurnitureName());
        model.setNumber(item.getNumber());
        model.setPrice(item.getPrice());
        model.setAssetCode(item.getAssetCode());
        model.setTotalPrice(item.getTotalPrice());//MyStringUtil.getNewMultiplyMoney(model.getPrice(),model.getNumber())
        fiveOaFurniturePurchaseDetailMapper.updateByPrimaryKey(model);
    }

    public FiveOaFurniturePurchaseDetail getNewModelDetail(int  id){

        FiveOaFurniturePurchaseDetail item=new FiveOaFurniturePurchaseDetail();
        item.setFurnitureId(id);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);

        ModelUtil.setNotNullFields(item);
        fiveOaFurniturePurchaseDetailMapper.insert(item);
        return item;

    }

    public FiveOaFurniturePurchaseDetail getModelDetailById(int id){
        FiveOaFurniturePurchaseDetail detail = fiveOaFurniturePurchaseDetailMapper.selectByPrimaryKey(id);
        return detail;
    }

    public List<FiveOaFurniturePurchaseDetail> listDetail(int id){
        Map <String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("furnitureId",id);
        List<FiveOaFurniturePurchaseDetail> details = fiveOaFurniturePurchaseDetailMapper.selectAll(params);
        return details;
    }

}
