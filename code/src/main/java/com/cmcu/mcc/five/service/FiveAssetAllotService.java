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
import com.cmcu.mcc.five.dao.FiveAssetAllotDetailMapper;
import com.cmcu.mcc.five.dao.FiveAssetAllotMapper;
import com.cmcu.mcc.five.dto.FiveAssetAllotDto;
import com.cmcu.mcc.five.entity.FiveAssetAllot;
import com.cmcu.mcc.five.entity.FiveAssetAllotDetail;
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
import java.util.*;

@Service
public class FiveAssetAllotService extends BaseService {
    
    @Resource
    FiveAssetAllotMapper fiveAssetAllotMapper;
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
    FiveAssetAllotDetailMapper fiveAssetAllotDetailMapper;

  public List<FiveAssetAllot> listDate(String userLogin){
      Map map= Maps.newHashMap();
      map.put("deleted",false);
      map.put("",userLogin);
      List<FiveAssetAllot> fiveAssetAllots = fiveAssetAllotMapper.selectAll(map);
      return  fiveAssetAllots;
  }

    public void remove(int id,String userLogin){
        FiveAssetAllot item = fiveAssetAllotMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");
        //流程作废
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

    }

    public void update(FiveAssetAllotDto item){
        FiveAssetAllot model = fiveAssetAllotMapper.selectByPrimaryKey(item.getId());
        model.setFormNo(item.getFormNo());
        model.setApplicant(item.getApplicant());
        model.setApplicantName(item.getApplicantName());
        model.setDeptId(item.getDeptId());
        model.setDeptName(item.getDeptName());
        model.setEnterTime(item.getEnterTime());
        model.setAssetCode(item.getAssetCode());
        model.setAssetName(item.getAssetName());
        model.setModel(item.getModel());
        model.setUseUnit(item.getUseUnit());
        model.setUseUnitId(item.getUseUnitId());
        model.setUseMan(item.getUseMan());
        model.setUseManName(item.getUseManName());
        model.setTransferUnit(item.getTransferUnit());
        model.setTransferUnitId(item.getTransferUnitId());
        model.setReserveMan(item.getReserveMan());
        model.setReserveManName(item.getReserveManName());
        model.setReceive(item.getReceive());
        model.setReceiveName(item.getReceiveName());
        model.setRemark(item.getRemark());
        model.setGmtModified(new Date());
        ModelUtil.setNotNullFields(model);
        fiveAssetAllotMapper.updateByPrimaryKey(model);
        Map variables = Maps.newHashMap();
        if(item.getDeptId()!=0) {
            variables.put("deptChargeMan", selectEmployeeService.getDeptChargeMen(item.getDeptId()));//发起者部门领导
        }
        if(item.getReserveMan()!=null) {
            variables.put("receiveMan", model.getReserveMan());//选择接收人=现保管人
        }
        variables.put("administrative", selectEmployeeService.getDeptChargeMen(67));//行政事务部领导
        variables.put("processDescription","固定资产调拨单(办公家具)："+item.getAssetName());
        BeanValidator.check(model);
        myActService.setVariables(model.getProcessInstanceId(),variables);

    }


    public FiveAssetAllotDto getModelById(int id){

        return getDto(fiveAssetAllotMapper.selectByPrimaryKey(id));
    }

    public FiveAssetAllotDto getDto(FiveAssetAllot item) {
        FiveAssetAllotDto dto=FiveAssetAllotDto.adapt(item);
        dto.setProcessName("已完成");

        if(!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(),dto.getBusinessKey(),"");

            if(customProcessInstance==null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveAssetAllotMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance!=null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }

        return dto;
    }

    public int getNewModel(String userLogin){
        FiveAssetAllot item=new FiveAssetAllot();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setApplicant(hrEmployeeDto.getUserLogin());
        item.setApplicantName(hrEmployeeDto.getUserName());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        ModelUtil.setNotNullFields(item);

        fiveAssetAllotMapper.insert(item);

        String businessKey= EdConst.FIVE_ASSET_ALLOT+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription","固定资产调拨单(办公家具)："+item.getCreatorName());
        variables.put("deptChargeMan",selectEmployeeService.getDeptChargeMen(item.getDeptId()));

        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_ASSET_ALLOT,businessKey, variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);
        fiveAssetAllotMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref, int pageNum, int pageSize) {
        params.put("deleted",false);
        params.put("isLikeSelect",true);
       /* List<Integer> deptIdList=hrEmployeeSysService.getMyDeptListOa(userLogin,uiSref);
        if (deptIdList.size()==0){
            params.put("creator",userLogin);
        }else {
            params.put("deptIdList",deptIdList);
        }*/
        Map map=Maps.newHashMap();
        map.put("myDeptData",false);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveAssetAllotMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveAssetAllot item=(FiveAssetAllot)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public void updateDetail(FiveAssetAllotDetail item){
        FiveAssetAllotDetail model = fiveAssetAllotDetailMapper.selectByPrimaryKey(item.getId());

        model.setEnterTime(item.getEnterTime());
        model.setAssetCode(item.getAssetCode());
        model.setAssetName(item.getAssetName());
        model.setModel(item.getModel());
        model.setUseUnit(item.getUseUnit());
        model.setUseUnit(item.getUseUnit());
        model.setUseMan(item.getUseMan());
        model.setUseManName(item.getUseManName());
        model.setTransferUnit(item.getTransferUnit());
        model.setReserveMan(item.getReserveMan());
        model.setReserveManName(item.getReserveManName());

        model.setRemark(item.getRemark());
        fiveAssetAllotDetailMapper.updateByPrimaryKey(model);
    }

    public FiveAssetAllotDetail getNewModelDetail(int  id,String userLogin){
        FiveAssetAllotDetail item=new FiveAssetAllotDetail();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);

        ModelUtil.setNotNullFields(item);
        fiveAssetAllotDetailMapper.insert(item);
        return item;

    }

    public FiveAssetAllotDetail getModelDetailById(int id){
        return fiveAssetAllotDetailMapper.selectByPrimaryKey(id);
    }

    public List<FiveAssetAllotDetail> listDetail(int id){
        Map <String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("assetAllotId",id);//小写
        List<FiveAssetAllotDetail> list = fiveAssetAllotDetailMapper.selectAll(params);
        return list;
    }

    public void removeDetail(int id){
        FiveAssetAllotDetail model = fiveAssetAllotDetailMapper.selectByPrimaryKey(id);
        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveAssetAllotDetailMapper.updateByPrimaryKey(model);
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


        List<FiveAssetAllot> fiveAssetAllots=fiveAssetAllotMapper.selectAll(params);
        for (FiveAssetAllot dto:fiveAssetAllots){
            Map map1=new LinkedHashMap();
            map1.put("申请人",dto.getApplicantName());
            map1.put("申请部门",dto.getDeptName());
            map1.put("名称",dto.getAssetName());
            map1.put("入账时间",dto.getEnterTime());
            map1.put("规格型号",dto.getModel());
            map1.put("原使用单位",dto.getUseUnit() );
            map1.put("原保管人", dto.getUseManName());
            map1.put("转入单位",dto.getTransferUnit() );
            map1.put("现保管人",dto.getReserveManName() );
            map1.put("备注", dto.getRemark());
            map1.put("创建人",dto.getCreatorName());
            map1.put("创建时间", MyDateUtil.dateToStr(dto.getGmtCreate()));//时间格式转换 2021-01-01
            list.add(map1);
        }


        return list;
    }
}
