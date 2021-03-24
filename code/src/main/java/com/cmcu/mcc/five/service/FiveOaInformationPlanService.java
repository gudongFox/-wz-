package com.cmcu.mcc.five.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
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
import com.cmcu.mcc.five.dao.FiveOaInformationPlanDetailMapper;
import com.cmcu.mcc.five.dao.FiveOaInformationPlanMapper;
import com.cmcu.mcc.five.dto.FiveOaInformationPlanDto;
import com.cmcu.mcc.five.entity.FiveOaInformationPlan;
import com.cmcu.mcc.five.entity.FiveOaInformationPlanDetail;
import com.cmcu.mcc.five.entity.FiveOaSignQuote;
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
public class FiveOaInformationPlanService extends BaseService {
    @Resource
    FiveOaInformationPlanMapper fiveOaInformationPlanMapper;
    @Resource
    FiveOaInformationPlanDetailMapper fiveOaInformationPlanDetailMapper;
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
    CommonCodeService commonCodeService;

    public void remove(int id,String userLogin){
        FiveOaInformationPlan item = fiveOaInformationPlanMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

        //流程作废
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    public void update(FiveOaInformationPlanDto fiveOaInformationPlanDto){
        FiveOaInformationPlan model = fiveOaInformationPlanMapper.selectByPrimaryKey(fiveOaInformationPlanDto.getId());

        model.setDeptName(fiveOaInformationPlanDto.getDeptName());
        model.setDeptId(fiveOaInformationPlanDto.getDeptId());
        model.setGmtModified(new Date());
        model.setRemark(fiveOaInformationPlanDto.getRemark());
        model.setTotalNumber(fiveOaInformationPlanDto.getTotalNumber());
        model.setTotalMoney(fiveOaInformationPlanDto.getTotalMoney());

        BeanValidator.check(model);
        fiveOaInformationPlanMapper.updateByPrimaryKey(model);
    }

    public FiveOaInformationPlanDto getModelById(int id){
        return getDto(fiveOaInformationPlanMapper.selectByPrimaryKey(id));
    }

    public FiveOaInformationPlanDto getDto(FiveOaInformationPlan item) {
        FiveOaInformationPlanDto dto=FiveOaInformationPlanDto.adapt(item);
        dto.setProcessName("已完成");
        if (!dto.getProcessEnd()) {
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if(customProcessInstance ==null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaInformationPlanMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance!=null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }

        List<FiveOaInformationPlanDetail> fiveOaInformationPlanDetails = listDetail(item.getId());
        //总包 总尾款
        String totalFinalPrice= MyStringUtil.moneyToString("0");
        for (FiveOaInformationPlanDetail detail:fiveOaInformationPlanDetails){
            totalFinalPrice = MyStringUtil.getNewAddMoney(totalFinalPrice,detail.getSoftwareTotal(),2);
        }
        if (!item.getTotalMoney().equals(totalFinalPrice)){
            item.setTotalMoney(totalFinalPrice);
            fiveOaInformationPlanMapper.updateByPrimaryKey(item);
        }


        return dto;
    }

    public int getNewModel(String userLogin){
        FiveOaInformationPlan item=new FiveOaInformationPlan();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setYear((MyDateUtil.getYear()+1)+"");
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        ModelUtil.setNotNullFields(item);
        fiveOaInformationPlanMapper.insert(item);
        String businessKey= EdConst.FIVE_OA_INFORMATION_PLAN+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "年度软件采购预算表："+item.getDeptName());
        variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDeptId()));//发起者部门领导

        item.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_INFORMATION_PLAN,item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        fiveOaInformationPlanMapper.updateByPrimaryKey(item);
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
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaInformationPlanMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaInformationPlan item=(FiveOaInformationPlan)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public void removeDetail(int id){
        FiveOaInformationPlanDetail model = fiveOaInformationPlanDetailMapper.selectByPrimaryKey(id);

        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveOaInformationPlanDetailMapper.updateByPrimaryKey(model);
    }

    public void updateDetail(FiveOaInformationPlanDetail fiveOaInventPaymentDetail){
        FiveOaInformationPlanDetail model = fiveOaInformationPlanDetailMapper.selectByPrimaryKey(fiveOaInventPaymentDetail.getId());
       model.setSoftwareName(fiveOaInventPaymentDetail.getSoftwareName());
        model.setSoftwareNumber(fiveOaInventPaymentDetail.getSoftwareNumber());
        model.setSoftwareModel(fiveOaInventPaymentDetail.getSoftwareModel());
        model.setSoftwareType(fiveOaInventPaymentDetail.getSoftwareType());
        model.setSoftwareUseWay(fiveOaInventPaymentDetail.getSoftwareUseWay());
        model.setUseMajor(fiveOaInventPaymentDetail.getUseMajor());
        model.setSoftwareNumber(fiveOaInventPaymentDetail.getSoftwareNumber());
        model.setSoftwarePrice(fiveOaInventPaymentDetail.getSoftwarePrice());
        model.setSoftwareTotal(fiveOaInventPaymentDetail.getSoftwareTotal());
        model.setRemark(fiveOaInventPaymentDetail.getRemark());
        model.setGmtModified(new Date());

        ModelUtil.setNotNullFields(model);
        fiveOaInformationPlanDetailMapper.updateByPrimaryKey(model);
    }

    public int getNewModelDetail(int planId){
        FiveOaInformationPlanDetail item=new FiveOaInformationPlanDetail();
        FiveOaInformationPlanDto modelById = getModelById(planId);
        item.setPlanId(planId);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);
        item.setCreator(modelById.getCreator());
        item.setCreatorName(modelById.getCreatorName());

        ModelUtil.setNotNullFields(item);
        fiveOaInformationPlanDetailMapper.insert(item);
        return item.getId();

    }

    public FiveOaInformationPlanDetail getModelDetailById(int id){
        return fiveOaInformationPlanDetailMapper.selectByPrimaryKey(id);
    }

    public List<FiveOaInformationPlanDetail> listDetail(int planId){
        Map <String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("planId",planId);
        List<FiveOaInformationPlanDetail> list = fiveOaInformationPlanDetailMapper.selectAll(params);
        return list;
    }

    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaInformationPlan item = fiveOaInformationPlanMapper.selectByPrimaryKey(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String number = sdf.format(new Date())+item.getCreator().substring((item.getCreator()).length() - 4);
        data.put("tableNo",number);
        data.put("deptName",item.getDeptName());
        data.put("year",item.getYear());
        data.put("remark",item.getRemark());

        data.put("creatorName",item.getCreatorName());
        data.put("gmtCreate",item.getGmtCreate());

        Map map =new HashMap();
        map.put("planId",item.getId());
        map.put("deleted",false);
        List<FiveOaInformationPlanDetail> paymentDetails = fiveOaInformationPlanDetailMapper.selectAll (map);
        FiveOaInformationPlanDetail total=new FiveOaInformationPlanDetail();
        total.setSoftwareTotal(item.getTotalMoney());
        paymentDetails.add(total);
        data.put("planDetails",paymentDetails);

        List<ActHistoryDto> actHistoryDtos = actService.listPassedHistoryDto(item.getProcessInstanceId());

        data.put("nodes",actHistoryDtos);

        return data;
    }

    /**
     * 导出excel
     * @param params
     * @param uiSref
     * @param startTime1
     * @param endTime1
     * @param userLogin
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


        List<FiveOaInformationPlan> fiveOaInformationPlans=fiveOaInformationPlanMapper.selectAll(params);
        for (FiveOaInformationPlan dto:fiveOaInformationPlans){
            List<FiveOaInformationPlanDetail> fiveOaInformationPlanDetails=listDetail(dto.getId());
            for (FiveOaInformationPlanDetail detail:fiveOaInformationPlanDetails){
                Map map1 =new LinkedHashMap();
                map1.put("申请单位",dto.getDeptName());
                map1.put("预算年份", dto.getCreatorName());
                map1.put("备注", dto.getRemark());
                map1.put("创建人", dto.getCreatorName());
                map1.put("创建时间", MyDateUtil.dateToStr(dto.getGmtCreate()));//yyyy-MM-dd

                map1.put("软件名称",detail.getSoftwareName());
                map1.put("功能模块", detail.getSoftwareModel());
                map1.put("类型", detail.getSoftwareType());
                map1.put("用途", detail.getSoftwareUseWay());
                map1.put("专业", detail.getUseMajor());
                map1.put("数量（套）", detail.getSoftwareNumber());
                map1.put("单价（元）", detail.getSoftwarePrice());
                map1.put("合计（元）", detail.getSoftwareTotal());
                map1.put("备注", detail.getRemark());
                list.add(map1);
            }
        }
        return list;
    }

}
