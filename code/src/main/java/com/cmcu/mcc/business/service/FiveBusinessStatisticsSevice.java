package com.cmcu.mcc.business.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.business.dao.FiveBusinessConsultingContractStatisticsMapper;
import com.cmcu.mcc.business.dao.FiveBusinessContractLibraryMapper;
import com.cmcu.mcc.business.dao.FiveBusinessStatisticsMapper;
import com.cmcu.mcc.business.dto.FiveBusinessStatisticsDto;
import com.cmcu.mcc.business.entity.FiveBusinessAdvanceDetail;
import com.cmcu.mcc.business.entity.FiveBusinessConsultingContractStatistics;
import com.cmcu.mcc.business.entity.FiveBusinessContractLibrary;
import com.cmcu.mcc.business.entity.FiveBusinessStatistics;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.HrDeptService;
import com.cmcu.mcc.hr.service.HrEmployeeService;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
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
import java.util.stream.Collectors;

@Service
public class FiveBusinessStatisticsSevice extends BaseService {

    @Resource
    FiveBusinessStatisticsMapper fiveBusinessStatisticsMapper;
    @Resource
    HrEmployeeMapper hrEmployeeMapper;
    @Resource
    FiveBusinessConsultingContractStatisticsMapper fiveBusinessConsultingContractStatisticsMapper;
    @Resource
    FiveBusinessContractLibraryMapper fiveBusinessContractLibraryMapper;
    @Autowired
    ProcessQueryService processQueryService;
    @Autowired
    MyActService myActService;
    @Autowired
    SelectEmployeeService selectEmployeeService;
    @Autowired
    TaskHandleService taskHandleService;
    @Autowired
    HrEmployeeService hrEmployeeService;
    @Autowired
    HrEmployeeSysService hrEmployeeSysService;
    @Resource
    HandleFormService handleFormService;
    @Resource
    HrDeptService hrDeptService;

    /*public void remove(int id,String userLogin){
        FiveBusinessStatistics item = fiveBusinessStatisticsMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");
        //流程作废
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }*/

    //update
    /*public void update(FiveBusinessStatisticsDto fiveBusinessStatisticsDto){
        FiveBusinessStatistics model = fiveBusinessStatisticsMapper.selectByPrimaryKey(fiveBusinessStatisticsDto.getId());
        model.setDeptId(fiveBusinessStatisticsDto.getDeptId());
        model.setCreator(fiveBusinessStatisticsDto.getCreator());
        model.setCreatorName(fiveBusinessStatisticsDto.getCreatorName());
        model.setDeptName(fiveBusinessStatisticsDto.getDeptName());
        model.setGmtCreate(fiveBusinessStatisticsDto.getGmtCreate());
        model.setFormNo(fiveBusinessStatisticsDto.getFormNo());
        model.setRemark(fiveBusinessStatisticsDto.getRemark());
        model.setGmtModified(new Date());
        model.setStartTime(fiveBusinessStatisticsDto.getStartTime());
        model.setEndTime(fiveBusinessStatisticsDto.getEndTime());
        fiveBusinessStatisticsMapper.updateByPrimaryKey(model);

        Map variables = Maps.newHashMap();
        variables.put("deptChargeMen",selectEmployeeService.getParentDeptChargeMen(model.getDeptId(),1,true));
        variables.put("businessLeader", selectEmployeeService.getDeptChargeMen(48));//经营发展部第一负责人

        myActService.setVariables(model.getProcessInstanceId(),variables);
    }*/

    /*public FiveBusinessStatisticsDto getModelById(int id){
        return getDto(fiveBusinessStatisticsMapper.selectByPrimaryKey(id));
    }*/

    public FiveBusinessStatisticsDto getDto(FiveBusinessStatistics item) {
        FiveBusinessStatisticsDto dto= FiveBusinessStatisticsDto.adapt(item);
        /*dto.setProcessName("已完成");
        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");
            if(customProcessInstance == null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveBusinessStatisticsMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance != null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }*/

        return dto;
    }

    public int getNewModel(String userLogin){

        FiveBusinessStatistics item=new FiveBusinessStatistics();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeptId(selectEmployeeService.getHeadDeptId(hrEmployeeDto.getDeptId()));
        item.setDeptName(selectEmployeeService.getHeadDeptName(hrEmployeeDto.getDeptId()));

        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);

        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());

        ModelUtil.setNotNullFields(item);

        fiveBusinessStatisticsMapper.insert(item);
        String businessKey= EdConst.FIVE_BUSINESS_STATISTICS+ "_" + item.getId();

        fiveBusinessStatisticsMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref, int pageNum, int pageSize) {
        params.put("deleted",false);
        params.put("isLikeSelect",true);

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveBusinessContractLibraryMapper.selectCount(params));
//        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveBusinessStatisticsMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveBusinessContractLibrary each = (FiveBusinessContractLibrary) p;
            FiveBusinessStatistics item=new FiveBusinessStatistics();
            item.setDeptName(each.getDeptName());
            item.setCompleteCount(each.getCount());
            item.setLastYear(each.getCount());
            item.setIncrease(String.valueOf(Integer.valueOf(item.getCompleteCount())-Integer.valueOf(item.getLastYear())));
            item.setCompleteErcent("0%");
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }


    public void updateDetail(FiveBusinessConsultingContractStatistics item){
        FiveBusinessConsultingContractStatistics model = fiveBusinessConsultingContractStatisticsMapper.selectByPrimaryKey(item.getId());
        model.setDeptId(item.getDeptId());
        model.setDeptName(item.getDeptName());
        fiveBusinessConsultingContractStatisticsMapper.updateByPrimaryKey(model);

    }

    /*public FiveBusinessConsultingContractStatistics getNewModelDetail(int statisticsId){
        FiveBusinessConsultingContractStatistics item=new FiveBusinessConsultingContractStatistics();
        FiveBusinessStatistics model = getModelById(statisticsId);
        int seq=listDetail(statisticsId).size()+1;
        item.setStatisticsId(statisticsId);
        item.setConsultingContractTarget("0");
        item.setLastYearComplete("0");
        item.setConsultingContractComplete("0");
        ModelUtil.setNotNullFields(item);
//        fiveBusinessConsultingContractStatisticsMapper.insert(item);
        return item;

    }*/

    public FiveBusinessConsultingContractStatistics getModelDetailById(int id){
        return fiveBusinessConsultingContractStatisticsMapper.selectByPrimaryKey(id);
    }

    public List<FiveBusinessConsultingContractStatistics> listDetail(int statisticsId){
        Map <String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("statisticsId",statisticsId);//小写
        List<FiveBusinessConsultingContractStatistics> list = fiveBusinessConsultingContractStatisticsMapper.selectAll(params).stream()
                .sorted(Comparator.comparing(FiveBusinessConsultingContractStatistics::getId)).collect(Collectors.toList());
        return list;
    }

    public void removeDetail(int id){
        FiveBusinessConsultingContractStatistics model = fiveBusinessConsultingContractStatisticsMapper.selectByPrimaryKey(id);

//        model.setDeleted(true);
//        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveBusinessConsultingContractStatisticsMapper.updateByPrimaryKey(model);
    }

    public List<FiveBusinessConsultingContractStatistics> getReport(FiveBusinessStatistics fiveBusinessStatistics){
        Map map = new HashMap();
        map.put("projectNature","工程咨询");
        map.put("deleted",false);
        List<FiveBusinessContractLibrary> libraryList = fiveBusinessContractLibraryMapper.selectAll(map);
        return null;

    }

/*    public List<Map> listMapData(Map<String,Object> params,  String uiSref,String userLogin,int advanceId){
        List<Map> list = new ArrayList<>();
        params.put("deleted",false);
        params.put("advanceId",advanceId);

        List<FiveBusinessAdvanceDetail> fiveBusinessAdvanceDetails=fiveBusinessAdvanceDetailMapper.selectAll(params);
        for (FiveBusinessAdvanceDetail dto:fiveBusinessAdvanceDetails){
            Map map1=new LinkedHashMap();
            map1.put("人员编号",dto.getPersonNo());
            map1.put("姓名",dto.getPersonName());
            map1.put("部门",dto.getDeptName());
            map1.put("人员类别",dto.getPersonnelCategory());
            map1.put("金额",dto.getProjectBonus() );
            map1.put("备注",dto.getRemark() );
            list.add(map1);
        }
        //总计
        Map map1=new LinkedHashMap();
        map1.put("人员编号","");
        map1.put("姓名","总计");
        map1.put("部门","");
        map1.put("人员类别","");
        map1.put("金额",getModelById(advanceId).getTotalPrice());
        map1.put("备注","");
        list.add(map1);
        return list;
    }

    *//**
     * 上传EXCL 导入数据 子表数据
     * @param data
     * @param advanceId
     * @param userLogin
     * @throws ParseException
     *//*
    @Transactional
    public void uploadExcl(List<Map> data,int  advanceId,String userLogin) throws ParseException {
        Assert.state(data.size() > 1,"数据为空、数据填写异常，请准确按照模板填写!");
        Date now = new Date();
        //Assert.state(data.get(0).size()>=9,"每行数据应为12列(请严格按照模板填写数据)!");
        //删除原子表数据
        Map<String, Object> params = Maps.newHashMap();
        params.put("advanceId", advanceId);
        params.put("deleted",false);
        List<FiveBusinessAdvanceDetail> fiveBusinessAdvanceDetails = fiveBusinessAdvanceDetailMapper.selectAll(params);
        for(FiveBusinessAdvanceDetail detail:fiveBusinessAdvanceDetails){
            detail.setDeleted(true);
            fiveBusinessAdvanceDetailMapper.updateByPrimaryKey(detail);
        }

        if (data.size() > 0) {
            for (int i = 1; i < data.size(); i++) {
                try {
                    Map map = data.get(i);
                    Assert.state(!map.get(1).toString().equals(""),"请准确填写职工号");

                    FiveBusinessAdvanceDetail detail = new FiveBusinessAdvanceDetail();

                    detail.setAdvanceId(advanceId);
                    detail.setPersonNo(map.get(1).toString().replace(".0",""));
                    detail.setPersonName(map.get(2).toString());
                    //detail.setSeq(Integer.valueOf(map.get(0).toString()));

                    detail.setDeptName(map.get(3).toString());
                    HrDeptDto hrDeptDto = hrDeptService.getModelByName(detail.getDeptName());
                    if(hrDeptDto!=null){
                        detail.setDeptId(hrDeptDto.getId() + "");
                    }else{
                        detail.setDeptId(0+"");
                    }


                    detail.setPersonnelCategory(map.get(4).toString());
                    detail.setProjectBonus(MyStringUtil.moneyToString(map.get(5).toString(),2));
                    detail.setRemark(map.get(6).toString());
                    if (detail.getId() == null || detail.getId() == 0) {
                        detail.setGmtCreate(new Date());
                        detail.setGmtModified(new Date());
                        detail.setDeleted(false);
                        ModelUtil.setNotNullFields(detail);
                        fiveBusinessAdvanceDetailMapper.insert(detail);
                    } else {
                        fiveBusinessAdvanceDetailMapper.updateByPrimaryKey(detail);
                    }

                }catch (Exception e){
                    Assert.state(true,"数据异常请查后重新上传");
                }

            }
        }
    }*/



}
