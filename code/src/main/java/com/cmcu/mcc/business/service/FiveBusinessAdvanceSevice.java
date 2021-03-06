package com.cmcu.mcc.business.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyDateUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.business.dao.FiveBusinessAdvanceDetailMapper;
import com.cmcu.mcc.business.dao.FiveBusinessAdvanceMapper;
import com.cmcu.mcc.business.dto.FiveBusinessAdvanceDto;
import com.cmcu.mcc.business.entity.FiveBusinessAdvance;
import com.cmcu.mcc.business.entity.FiveBusinessAdvanceDetail;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.five.entity.FiveOaInformationPlanDetail;
import com.cmcu.mcc.five.entity.FiveOaInlandProjectApply;
import com.cmcu.mcc.five.entity.FiveOaProjectfundplanDetail;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrDeptDto;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FiveBusinessAdvanceSevice extends BaseService {

    @Resource
    FiveBusinessAdvanceMapper fiveBusinessAdvanceMapper;
    @Resource
    HrEmployeeMapper hrEmployeeMapper;
    @Resource
    FiveBusinessAdvanceDetailMapper fiveBusinessAdvanceDetailMapper;
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

    public void remove(int id,String userLogin){
        FiveBusinessAdvance item = fiveBusinessAdvanceMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"??????????????????"+item.getCreatorName()+"??????");
        //????????????
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    //update
    public void update(FiveBusinessAdvanceDto fiveBusinessAdvanceDto){
        FiveBusinessAdvance model = fiveBusinessAdvanceMapper.selectByPrimaryKey(fiveBusinessAdvanceDto.getId());
        model.setDeptId(fiveBusinessAdvanceDto.getDeptId());
        model.setCreator(fiveBusinessAdvanceDto.getCreator());
        model.setCreatorName(fiveBusinessAdvanceDto.getCreatorName());
        model.setDeptName(fiveBusinessAdvanceDto.getDeptName());
        model.setGmtCreate(fiveBusinessAdvanceDto.getGmtCreate());

        model.setDeclareType(fiveBusinessAdvanceDto.getDeclareType());
        model.setFormNo(fiveBusinessAdvanceDto.getFormNo());
        model.setRemark(fiveBusinessAdvanceDto.getRemark());
        model.setMonth(fiveBusinessAdvanceDto.getMonth());
        model.setYear(fiveBusinessAdvanceDto.getYear());
        model.setTotalPrice(fiveBusinessAdvanceDto.getTotalPrice());
        model.setProjectName(fiveBusinessAdvanceDto.getProjectName());
        model.setGmtModified(new Date());
        fiveBusinessAdvanceMapper.updateByPrimaryKey(model);

        Map variables = Maps.newHashMap();
        variables.put("deptChargeMen",selectEmployeeService.getParentDeptChargeMen(model.getDeptId(),1,true));
        variables.put("businessLeader", selectEmployeeService.getDeptChargeMen(48));//??????????????????????????????

        myActService.setVariables(model.getProcessInstanceId(),variables);
    }

    public FiveBusinessAdvanceDto getModelById(int id){
        return getDto(fiveBusinessAdvanceMapper.selectByPrimaryKey(id));
    }

    public FiveBusinessAdvanceDto getDto(FiveBusinessAdvance item) {
        FiveBusinessAdvanceDto dto= FiveBusinessAdvanceDto.adapt(item);
        dto.setProcessName("?????????");
        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");
            if(customProcessInstance == null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveBusinessAdvanceMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance != null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }
        List<FiveBusinessAdvanceDetail> fiveBusinessAdvanceDetails = listDetail(item.getId());
        //?????????
        String totalFinalPrice= MyStringUtil.moneyToString("0");
        for (FiveBusinessAdvanceDetail detail:fiveBusinessAdvanceDetails){
            totalFinalPrice = MyStringUtil.getNewAddMoney(totalFinalPrice, detail.getProjectBonus());
        }
        if (!item.getTotalPrice().equals(totalFinalPrice)){
            item.setTotalPrice(totalFinalPrice);
            fiveBusinessAdvanceMapper.updateByPrimaryKey(item);
        }
        dto.setTotalPrice(MyStringUtil.moneyToString(totalFinalPrice,2));

        return dto;
    }

    public int getNewModel(String userLogin){

        FiveBusinessAdvance item=new FiveBusinessAdvance();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeptId(selectEmployeeService.getHeadDeptId(hrEmployeeDto.getDeptId()));
        item.setDeptName(selectEmployeeService.getHeadDeptName(hrEmployeeDto.getDeptId()));

        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);

        item.setMonth(dateString.substring(0,7));
        //item.setYear(MyDateUtil.getYear()+"");

        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
       
        item.setDeclareType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????").toString());
        ModelUtil.setNotNullFields(item);

        fiveBusinessAdvanceMapper.insert(item);
        String businessKey= EdConst.FIVE_BUSINESS_ADVANCE+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "??????????????????"+item.getCreatorName());
        variables.put("deptChargeMen",selectEmployeeService.getParentDeptChargeMen(item.getDeptId(),3,true));

        List<String> managePerfMan = hrEmployeeService.selectUserByRoleNames("???????????????(?????????)");
       // managePerfMan.addAll(selectEmployeeService.getDeptChargeMen(48));//??????????????????????????????
        variables.put("managePerfMan", managePerfMan);//???????????????(?????????)

        String copyMan = MyStringUtil.listToString(hrEmployeeService.selectUserByRoleNames("???????????????(?????????)"))+""
                +MyStringUtil.listToString(selectEmployeeService.getDeptChargeMen(38))+
                MyStringUtil.listToString(hrEmployeeService.selectUserByRoleNames("???????????????(?????????)"));
        variables.put("copyMan", copyMan);//???????????????(?????????)
        item.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_BUSINESS_ADVANCE,item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        fiveBusinessAdvanceMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref, int pageNum, int pageSize) {
        params.put("deleted",false);
        params.put("isLikeSelect",true);

        //???????????????????????????????????????  myDeptData true?????????????????? false???????????????
        Map map =new HashMap();
        map.put("myDeptData",false);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveBusinessAdvanceMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveBusinessAdvance item=(FiveBusinessAdvance) p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public void updateDetail(FiveBusinessAdvanceDetail item){
        FiveBusinessAdvanceDetail model = fiveBusinessAdvanceDetailMapper.selectByPrimaryKey(item.getId());
        model.setDeptId(item.getDeptId());
        model.setDeptName(item.getDeptName());
        model.setPerson(item.getPerson());
        model.setPersonName(item.getPersonName());
        model.setPersonNo(item.getPersonNo());
        model.setRemark(item.getRemark());
        model.setCreator(item.getCreator());
        model.setCreatorName(item.getCreatorName());
        model.setGmtCreate(new Date());
        model.setSeq(item.getSeq());
        model.setProjectBonus(MyStringUtil.moneyToString(item.getProjectBonus(),2));
        model.setPersonnelCategory(item.getPersonnelCategory());
        fiveBusinessAdvanceDetailMapper.updateByPrimaryKey(model);

    }

    public FiveBusinessAdvanceDetail getNewModelDetail(int advanceId){
        FiveBusinessAdvanceDetail item=new FiveBusinessAdvanceDetail();
        FiveBusinessAdvanceDto model = getModelById(advanceId);
        int seq=listDetail(advanceId).size()+1;
        item.setAdvanceId(advanceId);
        item.setPersonnelCategory("??????");

        item.setSeq(seq);
        item.setProjectBonus("0");
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);
        ModelUtil.setNotNullFields(item);
        fiveBusinessAdvanceDetailMapper.insert(item);
        return item;

    }

    public FiveBusinessAdvanceDetail getModelDetailById(int id){
        return fiveBusinessAdvanceDetailMapper.selectByPrimaryKey(id);
    }

    public List<FiveBusinessAdvanceDetail> listDetail(int advanceId){
        Map <String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("advanceId",advanceId);//??????
        List<FiveBusinessAdvanceDetail> list = fiveBusinessAdvanceDetailMapper.selectAll(params).stream()
                .sorted(Comparator.comparing(FiveBusinessAdvanceDetail::getId)).collect(Collectors.toList());
        return list;
    }

    public void removeDetail(int id){
        FiveBusinessAdvanceDetail model = fiveBusinessAdvanceDetailMapper.selectByPrimaryKey(id);

        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveBusinessAdvanceDetailMapper.updateByPrimaryKey(model);
    }

    public List<Map> listMapData(Map<String,Object> params,  String uiSref,String userLogin,int advanceId){
        List<Map> list = new ArrayList<>();
        params.put("deleted",false);
        params.put("advanceId",advanceId);

        List<FiveBusinessAdvanceDetail> fiveBusinessAdvanceDetails=fiveBusinessAdvanceDetailMapper.selectAll(params);
        for (FiveBusinessAdvanceDetail dto:fiveBusinessAdvanceDetails){
            Map map1=new LinkedHashMap();
            map1.put("????????????",dto.getPersonNo());
            map1.put("??????",dto.getPersonName());
            map1.put("??????",dto.getDeptName());
            map1.put("????????????",dto.getPersonnelCategory());
            map1.put("??????",dto.getProjectBonus() );
            map1.put("??????",dto.getRemark() );
            list.add(map1);
        }
        //??????
        Map map1=new LinkedHashMap();
        map1.put("????????????","");
        map1.put("??????","??????");
        map1.put("??????","");
        map1.put("????????????","");
        map1.put("??????",getModelById(advanceId).getTotalPrice());
        map1.put("??????","");
        list.add(map1);
        return list;
    }

    /**
     * ??????EXCL ???????????? ????????????
     * @param data
     * @param advanceId
     * @param userLogin
     * @throws ParseException
     */
    @Transactional
    public void uploadExcl(List<Map> data,int  advanceId,String userLogin) throws ParseException {
        Assert.state(data.size() > 1,"???????????????????????????????????????????????????????????????!");
        Date now = new Date();
        //Assert.state(data.get(0).size()>=9,"??????????????????12???(?????????????????????????????????)!");
        //?????????????????????
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
                    Assert.state(!map.get(1).toString().equals(""),"????????????????????????");

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
                    Assert.state(true,"?????????????????????????????????");
                }

            }
        }
    }



}
