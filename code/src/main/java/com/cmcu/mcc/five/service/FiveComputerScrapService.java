package com.cmcu.mcc.five.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.service.CommonFileService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyDateUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.dto.ActHistoryDto;
import com.cmcu.mcc.five.dao.FiveComputerScrapDetailMapper;
import com.cmcu.mcc.five.dao.FiveComputerScrapMapper;
import com.cmcu.mcc.five.dto.FiveComputerScrapDto;
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
import java.util.*;

@Service
public class FiveComputerScrapService extends BaseService {

    @Resource
    FiveComputerScrapMapper fiveComputerScrapMapper;
    @Resource
    HrEmployeeMapper hrEmployeeMapper;
    @Resource
    CommonFileService commonFileService;
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
    FiveComputerScrapDetailMapper fiveComputerScrapDetailMapper;

  public List<FiveComputerScrap> listDate(String userLogin){
      Map map= Maps.newHashMap();
      map.put("deleted",false);
      map.put("",userLogin);
      List<FiveComputerScrap> fiveComputerScraps = fiveComputerScrapMapper.selectAll(map);
      return  fiveComputerScraps;
  }

    public void remove(int id,String userLogin){
        FiveComputerScrap item = fiveComputerScrapMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");
        //流程作废
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

    }

    public void update(FiveComputerScrapDto item){
        FiveComputerScrap model = fiveComputerScrapMapper.selectByPrimaryKey(item.getId());
        model.setFormNo(item.getFormNo());
        model.setApplicant(item.getApplicant());
        model.setApplicantName(item.getApplicantName());
        model.setApplicantReason(item.getApplicantReason());
        model.setDeptId(item.getDeptId());
        model.setDeptName(item.getDeptName());
        model.setAuthenticateResult(item.getApplicantReason());
        model.setAuthenticateResult(item.getAuthenticateResult());
        model.setHandleResult(item.getHandleResult());
        model.setRemark(item.getRemark());
        model.setGmtModified(new Date());
        ModelUtil.setNotNullFields(model);
        fiveComputerScrapMapper.updateByPrimaryKey(model);
        Map variables = Maps.newHashMap();
        if(item.getDeptId()!=0) {
            variables.put("deptChargeMan", selectEmployeeService.getDeptChargeMen(item.getDeptId()));//发起者部门领导
        }

        BeanValidator.check(model);
        myActService.setVariables(model.getProcessInstanceId(),variables);

    }


    public FiveComputerScrapDto getModelById(int id){

        return getDto(fiveComputerScrapMapper.selectByPrimaryKey(id));
    }

    public FiveComputerScrapDto getDto(FiveComputerScrap item) {
        FiveComputerScrapDto dto=FiveComputerScrapDto.adapt(item);
        dto.setProcessName("已完成");

        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");
            if(customProcessInstance == null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveComputerScrapMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance != null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());

            }
        }

        return dto;
    }

    public int getNewModel(String userLogin){
        FiveComputerScrap item=new FiveComputerScrap();
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

        fiveComputerScrapMapper.insert(item);

        String businessKey= EdConst.FIVE_COMPUTER_SCRAP+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription","涉密计算机及外设设备报废审批："+item.getCreatorName());
        variables.put("deptChargeMan",selectEmployeeService.getDeptChargeMen(item.getDeptId()));
        variables.put("financeLeader",selectEmployeeService.getDeptChargeMen(18));//财务管理部
        variables.put("qualityLeader",selectEmployeeService.getDeptChargeMen(11));//技术质量管理部
        variables.put("administrative", selectEmployeeService.getDeptChargeMen(67));//行政事务部领导
        variables.put("administrativeCharge", selectEmployeeService.getOtherDeptChargeMan(67));//行政事务部领导
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_COMPUTER_SCRAP,businessKey, variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);
        fiveComputerScrapMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref, int pageNum, int pageSize) {
        params.put("deleted",false);
        params.put("isLikeSelect",true);

        //如果有数据权限判断数据权限  myDeptData true查看当前部门 false查看创建人
        Map map =new HashMap();
        map.put("myDeptData",false);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveComputerScrapMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveComputerScrap item=(FiveComputerScrap)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public void updateDetail(FiveComputerScrapDetail item){
        FiveComputerScrapDetail model = fiveComputerScrapDetailMapper.selectByPrimaryKey(item.getId());

        model.setDeviceType(item.getDeviceType());
        model.setDeviceNo(item.getDeviceNo());
        model.setDeviceModel(item.getDeviceModel());
        model.setSecretLevel(item.getSecretLevel());
        model.setDutyPerson(item.getDutyPerson());
        model.setHardDiskNo(item.getHardDiskNo());
        model.setStartTime(item.getStartTime());
        model.setOriginalValue(item.getOriginalValue());
        model.setDepreciableLife(item.getDepreciableLife());
        model.setSubmitted(item.getSubmitted());
        model.setNetWorth(item.getNetWorth());

        model.setRemark(item.getRemark());

        if( commonFileService.listData(item.getBusinessKey(),0,"").size()>0){
            model.setUploadfile(true);
        }else {
            model.setUploadfile(false);
        }

        fiveComputerScrapDetailMapper.updateByPrimaryKey(model);
    }

    public FiveComputerScrapDetail getNewModelDetail(int  computerScrapId,String userLogin){
        FiveComputerScrapDetail item=new FiveComputerScrapDetail();
        item.setComputerScrapId(computerScrapId);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);
        item.setUploadfile(false);
        ModelUtil.setNotNullFields(item);
        fiveComputerScrapDetailMapper.insert(item);
        item.setBusinessKey("fiveComputerScrapDetail_"+item.getId());
        fiveComputerScrapDetailMapper.updateByPrimaryKey(item);
        return item;

    }

    public FiveComputerScrapDetail getModelDetailById(int id){
        return fiveComputerScrapDetailMapper.selectByPrimaryKey(id);
    }

    public List<FiveComputerScrapDetail> listDetail(int id){
        Map <String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("computerScrapId",id);//小写
        List<FiveComputerScrapDetail> list = fiveComputerScrapDetailMapper.selectAll(params);
        return list;
    }

    public void removeDetail(int id){
        FiveComputerScrapDetail model = fiveComputerScrapDetailMapper.selectByPrimaryKey(id);
        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveComputerScrapDetailMapper.updateByPrimaryKey(model);
    }

    /**
     * 导出excl
     * @param startTime1 开始时间
     * @param endTime1 结束时间
     * @return
     */
    public List<Map> listMapData(Map<String,Object> params,String uiSref,String startTime1,String endTime1,String userLogin,String userName){
        List<Map> list = new ArrayList<>();
        Map map=new LinkedHashMap();
        map.put("申请人","");
        map.put("申请部门","");
        map.put("申请理由","");
        map.put("计算机所设备检定意见","");
        map.put("设备处理过程意见","");
        map.put("创建人","");
        map.put("创建时间","");
        map.put("设备类型","");
        map.put("资产编号","");
        map.put("品牌型号","");
        map.put("责任人","");
        map.put("启用时间","");
        map.put("原值","");
        map.put("折旧年限","");
        map.put("已提折旧","");
        map.put("净值","");
        list.add(map);

        //数据权限验证
        Map head=Maps.newHashMap();
        head.put("myDeptData",false);
        head.put("uiSref",uiSref);
        head.put("enLogin",userLogin);

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


        List<FiveComputerScrap> fiveComputerScraps=fiveComputerScrapMapper.selectAll(params);
        for (FiveComputerScrap dto:fiveComputerScraps){
            List<FiveComputerScrapDetail> fiveComputerScrapDetails=listDetail(dto.getId());

            for (FiveComputerScrapDetail detail:fiveComputerScrapDetails){
                Map map1=new LinkedHashMap();
                map1.put("申请人",dto.getApplicantName());
                map1.put("申请部门",dto.getDeptName());
                map1.put("申请理由",dto.getApplicantReason());
                map1.put("计算机所设备检定意见",dto.getAuthenticateResult());
                map1.put("设备处理过程意见",dto.getHandleResult());
                map1.put("创建人",dto.getCreatorName());
                map1.put("创建时间", MyDateUtil.dateToStr(dto.getGmtCreate()));
                map1.put("设备类型",detail.getDeviceType());
                map1.put("资产编号",detail.getDeviceNo());
                map1.put("品牌型号",detail.getDeviceModel());
                map1.put("责任人",detail.getDutyPerson());
                map1.put("启用时间",detail.getStartTime());
                map1.put("原值",detail.getOriginalValue());
                map1.put("折旧年限",detail.getDepreciableLife());
                map1.put("已提折旧",detail.getSubmitted());
                map1.put("净值",detail.getNetWorth());
                list.add(map1);
            }
        }


        return list;
    }



}
