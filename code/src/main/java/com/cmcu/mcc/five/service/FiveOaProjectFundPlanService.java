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
import com.cmcu.mcc.five.dao.FiveOaProjectFundPlanDetailMapper;


import com.cmcu.mcc.five.dao.FiveOaProjectfundplanMapper;
import com.cmcu.mcc.five.dto.FiveOaProjectFundPlanDto;
import com.cmcu.mcc.five.entity.FiveOaProjectfundplan;
import com.cmcu.mcc.five.entity.FiveOaProjectfundplanDetail;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrDeptDto;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.HrDeptService;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FiveOaProjectFundPlanService extends BaseService {
    @Resource
    FiveOaProjectfundplanMapper fiveOaProjectFundPlanMapper;
    @Resource
    FiveOaProjectFundPlanDetailMapper fiveOaProjectFundPlanDetailMapper;
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
    HrDeptService hrDeptService;
    @Resource
    CommonCodeService commonCodeService;


    public void remove(int id,String userLogin){
        FiveOaProjectfundplan item = fiveOaProjectFundPlanMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"??????????????????"+item.getCreatorName()+"??????");

        //????????????
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

    }

    public void update(FiveOaProjectFundPlanDto item){
        FiveOaProjectfundplan model = fiveOaProjectFundPlanMapper.selectByPrimaryKey(item.getId());
        model.setProjectNo(item.getProjectNo());
        model.setContractNo(item.getContractNo());
        model.setDeptId(item.getDeptId());
        model.setDeptName(item.getDeptName());
        model.setProjectName(item.getProjectName());
        model.setTotalAccountant(item.getTotalAccountant());
        model.setTotalAccountantName(item.getTotalAccountantName());
        model.setTotalManger(item.getTotalManger());
        model.setTotalMangerName(item.getTotalMangerName());
        model.setChargeLeaderMan(item.getChargeLeaderMan());
        model.setChargeLeaderManName(item.getChargeLeaderManName());

        model.setDeptChargeMen(item.getDeptChargeMen());
        model.setDeptChargeMenName(item.getDeptChargeMenName());
        model.setPlanTime(item.getPlanTime());

        model.setGmtModified(new Date());
        model.setRemark(item.getRemark());
        Map variables = Maps.newHashMap();

        variables.put("processDescription","????????????????????????:"+ model.getProjectName()+"("+model.getPlanTime()+")");

        variables.put("projectLeader",selectEmployeeService.getDeptChargeMen(29) );//???????????????????????????
        variables.put("deptChargeMenList",MyStringUtil.getStringList(model.getDeptChargeMen()) );//?????????????????????
        variables.put("generalAccount",MyStringUtil.getStringList(model.getTotalAccountant()) );//???????????? generalManager
        variables.put("generalManager",MyStringUtil.getStringList(model.getTotalManger()) );//?????????
        variables.put("projectManager",MyStringUtil.getStringList(model.getChargeLeaderMan()) );//????????????
        variables.put("financeChargeMen",selectEmployeeService.getDeptChargeMen(18));//??????????????? ?????????
        variables.put("projectManagerChargeMen",selectEmployeeService.getOtherDeptChargeMan(29));//???????????? ????????????
        variables.put("financeMen",selectEmployeeService.getDeptFinanceMan(model.getDeptId()));//??????????????????
        variables.put("copyMen",StringUtils.join(selectEmployeeService.getDeptFinanceMan(model.getDeptId()),",")+"4003");//??????????????????+?????????

        myActService.setVariables(model.getProcessInstanceId(),variables);
        BeanValidator.check(model);
        fiveOaProjectFundPlanMapper.updateByPrimaryKey(model);

    }

    public FiveOaProjectFundPlanDto getModelById(int id){

        return getDto(fiveOaProjectFundPlanMapper.selectByPrimaryKey(id));
    }

    public FiveOaProjectFundPlanDto getDto(FiveOaProjectfundplan item) {
        FiveOaProjectFundPlanDto dto=FiveOaProjectFundPlanDto.adapt(item);
        dto.setProcessName("?????????");
        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");
            if(customProcessInstance==null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaProjectFundPlanMapper.updateByPrimaryKey(dto);
            }else {
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }

        //????????????
        //?????? ????????????
        String totalContractPrice=MyStringUtil.moneyToString("0",6);
        //?????? ???????????????
        String totalAccumulativePrice=MyStringUtil.moneyToString("0",6);
        //?????? ???????????????
        String totalReceivablePrice=MyStringUtil.moneyToString("0",6);
        //?????? ?????????
        String totalFinalPrice=MyStringUtil.moneyToString("0",6);

        //?????? ????????????
        String totalContractPrice2=MyStringUtil.moneyToString("0",6);
        //?????? ???????????????
        String totalAccumulativePrice2=MyStringUtil.moneyToString("0",6);
        //?????? ???????????????
        String totalReceivablePrice2=MyStringUtil.moneyToString("0",6);
        //?????? ?????????
        String totalFinalPrice2=MyStringUtil.moneyToString("0",6);

        List<FiveOaProjectfundplanDetail> deatils = listDetail(item.getId());
        for(FiveOaProjectfundplanDetail detail:deatils){
            if(detail.getType().equalsIgnoreCase("??????")){
                totalContractPrice = MyStringUtil.getNewAddMoney(totalContractPrice,detail.getContractPrice());

                totalAccumulativePrice = MyStringUtil.getNewAddMoney(totalAccumulativePrice,detail.getAccumulativePrice());

                totalReceivablePrice =MyStringUtil.getNewAddMoney(totalReceivablePrice,detail.getReceivablePrice());

                totalFinalPrice = MyStringUtil.getNewAddMoney(totalFinalPrice,detail.getFinalPrice());
            }
            if(detail.getType().equalsIgnoreCase("??????")){
                totalContractPrice2 =MyStringUtil.getNewAddMoney(totalContractPrice2,detail.getContractPrice());

                totalAccumulativePrice2 =MyStringUtil.getNewAddMoney(totalAccumulativePrice2,detail.getAccumulativePrice());

                totalReceivablePrice2 =MyStringUtil.getNewAddMoney(totalReceivablePrice2,detail.getReceivablePrice());

                totalFinalPrice2 = MyStringUtil.getNewAddMoney(totalFinalPrice2,detail.getFinalPrice());
            }
        }
        dto.setTotalContractPrice( MyStringUtil.moneyToString(totalContractPrice,6));
        dto.setTotalAccumulativePrice(MyStringUtil.moneyToString(totalAccumulativePrice,6));
        dto.setTotalReceivablePrice(MyStringUtil.moneyToString(totalReceivablePrice,6));
        dto.setTotalFinalPrice(MyStringUtil.moneyToString(totalFinalPrice,6));

        dto.setTotalContractPrice2(MyStringUtil.moneyToString(totalContractPrice2,6));
        dto.setTotalAccumulativePrice2(MyStringUtil.moneyToString(totalAccumulativePrice2,6));
        dto.setTotalReceivablePrice2(MyStringUtil.moneyToString(totalReceivablePrice2,6));
        dto.setTotalFinalPrice2(MyStringUtil.moneyToString(totalFinalPrice2,6));
        //??????????????????
        String temp = MyStringUtil.getNewAddMoney(totalAccumulativePrice,totalReceivablePrice);
        String temp2 = MyStringUtil.getNewSubMoney(temp,totalAccumulativePrice2);
        String allMoney = MyStringUtil.getNewSubMoney(temp2,totalReceivablePrice2);
        dto.setAllProjectMoney(MyStringUtil.moneyToString(allMoney,6));

        //??????????????????
        if (!item.getAllProjectMoney().equals(allMoney)){
            item.setTotalContractPriceReceipt(totalContractPrice);
            item.setTotalAccumulativePriceReceipt(totalAccumulativePrice);
            item.setTotalReceivablePriceReceipt(totalReceivablePrice);
            item.setTotalFinalPriceReceipt(totalFinalPrice);

            item.setTotalContractPricePay(totalContractPrice2);
            item.setTotalAccumulativePricePay(totalAccumulativePrice2);
            item.setTotalReceivablePricePay(totalReceivablePrice2);
            item.setTotalFinalPricePay(totalFinalPrice2);
            item.setGmtModified(new Date());
            item.setAllProjectMoney(allMoney);
            fiveOaProjectFundPlanMapper.updateByPrimaryKey(item);
        }
        return dto;
    }

    public FiveOaProjectfundplanDetail getDetailDto(FiveOaProjectfundplanDetail detail) {
        detail.setFinalPrice(MyStringUtil.moneyToString(detail.getFinalPrice(),6));
        detail.setReceivablePrice(MyStringUtil.moneyToString(detail.getReceivablePrice(),6));
        detail.setAccumulativePrice(MyStringUtil.moneyToString(detail.getAccumulativePrice(),6));
        detail.setContractPrice(MyStringUtil.moneyToString(detail.getContractPrice(),6));
        return detail;
    }

    public FiveOaProjectFundPlanDto getDtoList(FiveOaProjectfundplan item) {
        FiveOaProjectFundPlanDto dto=FiveOaProjectFundPlanDto.adapt(item);
        dto.setProcessName("?????????");
         if (!dto.getProcessEnd()){
             CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");
             if (customProcessInstance!=null){
                 if(customProcessInstance.isFinished()){
                     dto.setProcessEnd(true);
                     fiveOaProjectFundPlanMapper.updateByPrimaryKey(dto);
                 }else {
                     dto.setProcessName(customProcessInstance.getCurrentTaskName());
                 }
             }
         }
        return dto;
    }

    public int getNewModel(String userLogin){

        List<String> totalManger = hrEmployeeService.selectUserByPositionName("?????????");
        List<String> totalAccount = hrEmployeeService.selectUserByPositionName("????????????");
        FiveOaProjectfundplan item=new FiveOaProjectfundplan();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        HrDeptDto hrDeptDto = hrDeptService.getModelById(hrEmployeeDto.getDeptId());
        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());

        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());

        item.setDeptChargeMen(hrDeptDto.getDeptFirstLeader());
        item.setDeptChargeMenName(hrDeptDto.getDeptFirstLeaderName());

        item.setPlanTime(MyDateUtil.getUserDate("yyyy-MM"));

        item.setTotalManger(StringUtils.join(totalManger,","));
        item.setTotalMangerName(selectEmployeeService.getNameByUserLogins(item.getTotalManger()));
        item.setTotalAccountant(StringUtils.join(totalAccount,","));
        item.setTotalAccountantName(selectEmployeeService.getNameByUserLogins(item.getTotalAccountant()));

        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        ModelUtil.setNotNullFields(item);

        fiveOaProjectFundPlanMapper.insert(item);
        String businessKey= EdConst.FIVE_OA_PROJECTFUNDPLAN+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "????????????????????????:"+item.getCreatorName());
        variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDeptId()));//?????????????????????
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_PROJECTFUNDPLAN,businessKey, variables, MccConst.APP_CODE);
        item.setBusinessKey(businessKey);
        item.setProcessInstanceId(processInstanceId);
        fiveOaProjectFundPlanMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
         params.put("deleted",false);
         if(params.containsKey("processName")){
             if(params.get("processName").toString().equalsIgnoreCase("?????????")){
                 params.put("processEnd",true);
             }
             else{
                 params.put("processEnd",false);
             }
         }

        Map map =new HashMap();
        map.put("myDeptData",false);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaProjectFundPlanMapper.fuzzySearch(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaProjectfundplan item=(FiveOaProjectfundplan)p;
            list.add(getDtoList(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public void removeDetail(int id){
        FiveOaProjectfundplanDetail model = fiveOaProjectFundPlanDetailMapper.selectByPrimaryKey(id);

        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveOaProjectFundPlanDetailMapper.updateByPrimaryKey(model);
    }

    public void updateDetail(FiveOaProjectfundplanDetail item){
        FiveOaProjectfundplanDetail model = fiveOaProjectFundPlanDetailMapper.selectByPrimaryKey(item.getId());
        model.setContractNo(item.getContractNo());
        model.setDeptName(item.getDeptName());
        model.setProjectName(item.getProjectName());
        model.setContractPrice(MyStringUtil.moneyToString(item.getContractPrice()));
        model.setAccumulativePrice(MyStringUtil.moneyToString(item.getAccumulativePrice()));
        model.setReceivablePrice(MyStringUtil.moneyToString(item.getReceivablePrice()));
        model.setFinalPrice(MyStringUtil.moneyToString(item.getFinalPrice()));
        model.setRemark(item.getRemark());
        model.setPurchasePlatform(item.getPurchasePlatform());
        model.setPurchaseType(item.getPurchaseType());
        model.setSeq(item.getSeq());
        fiveOaProjectFundPlanDetailMapper.updateByPrimaryKey(model);
    }

    public FiveOaProjectfundplanDetail getNewModelDetail(int  id,String type){
        Map <String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("type",type);
        params.put("projectfundplanId",id);
        int  seq = fiveOaProjectFundPlanDetailMapper.selectAll(params).size()+1;
        FiveOaProjectfundplanDetail item=new FiveOaProjectfundplanDetail();
        item.setProjectfundplanId(id+"");
        item.setType(type);
        item.setContractPrice("0");
        item.setAccumulativePrice("0");
        item.setReceivablePrice("0");
        item.setFinalPrice("0");
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);
        item.setSeq(seq);
        item.setPurchasePlatform(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????").toString());
        item.setPurchaseType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????").toString());
        ModelUtil.setNotNullFields(item);
        fiveOaProjectFundPlanDetailMapper.insert(item);
        return item;

    }

    public FiveOaProjectfundplanDetail getModelDetailById(int id){
        FiveOaProjectfundplanDetail detail = fiveOaProjectFundPlanDetailMapper.selectByPrimaryKey(id);
        return getDetailDto(detail);
    }

    public List<FiveOaProjectfundplanDetail> listDetail(int id){
        Map <String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("projectfundplanId",id);
        List<FiveOaProjectfundplanDetail> details = fiveOaProjectFundPlanDetailMapper.selectAll(params);
        List<FiveOaProjectfundplanDetail> list = new ArrayList<>();
        for(FiveOaProjectfundplanDetail detail:details){
            list.add(getDetailDto(detail));
        }
        return list;
    }

    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaProjectfundplan item = fiveOaProjectFundPlanMapper.selectByPrimaryKey(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String number = sdf.format(new Date())+item.getCreator().substring((item.getCreator()).length() - 4);
        data.put("tableNo",number);
        data.put("deptName",item.getDeptName());
        data.put("projectName",item.getProjectName());
        data.put("planTime",item.getPlanTime());

        data.put("creatorName",item.getCreatorName());
        data.put("gmtCreate",item.getGmtCreate());

        Map map =new HashMap();
        map.put("projectfundplanId",item.getId());
        map.put("deleted",false);
        List<FiveOaProjectfundplanDetail> projectFundPlanDetails = fiveOaProjectFundPlanDetailMapper.selectAll (map);
        data.put("projectFundPlanDetails",projectFundPlanDetails);

        //?????? ????????????
        String totalContractPrice=MyStringUtil.moneyToString("0");;
        //?????? ???????????????
        String totalAccumulativePrice=MyStringUtil.moneyToString("0");;
        //?????? ???????????????
        String totalReceivablePrice=MyStringUtil.moneyToString("0");;
        //?????? ?????????
        String totalFinalPrice=MyStringUtil.moneyToString("0");;

        //?????? ????????????
        String totalContractPrice2=MyStringUtil.moneyToString("0");;
        //?????? ???????????????
        String totalAccumulativePrice2=MyStringUtil.moneyToString("0");;
        //?????? ???????????????
        String totalReceivablePrice2=MyStringUtil.moneyToString("0");;
        //?????? ?????????
        String totalFinalPrice2=MyStringUtil.moneyToString("0");;
        //??????
        String residue=MyStringUtil.moneyToString("0");;
        List<FiveOaProjectfundplanDetail> deatils = listDetail(item.getId());
        for(FiveOaProjectfundplanDetail detail:deatils){
            if(detail.getType().equalsIgnoreCase("??????")){
                totalContractPrice = MyStringUtil.getNewAddMoney(totalContractPrice,detail.getContractPrice());

                totalAccumulativePrice = MyStringUtil.getNewAddMoney(totalAccumulativePrice,detail.getAccumulativePrice());

                totalReceivablePrice =MyStringUtil.getNewAddMoney(totalReceivablePrice,detail.getReceivablePrice());

                totalFinalPrice = MyStringUtil.getNewAddMoney(totalFinalPrice,detail.getFinalPrice());
            }
            if(detail.getType().equalsIgnoreCase("??????")){
                totalContractPrice2 =MyStringUtil.getNewAddMoney(totalContractPrice2,detail.getContractPrice());

                totalAccumulativePrice2 =MyStringUtil.getNewAddMoney(totalAccumulativePrice2,detail.getAccumulativePrice());

                totalReceivablePrice2 =MyStringUtil.getNewAddMoney(totalReceivablePrice2,detail.getReceivablePrice());

                totalFinalPrice2 = MyStringUtil.getNewAddMoney(totalFinalPrice2,detail.getFinalPrice());
            }
        }

        //??????????????????
        String temp = MyStringUtil.getNewAddMoney(totalAccumulativePrice,totalReceivablePrice);
        String temp2 = MyStringUtil.getNewSubMoney(temp,totalAccumulativePrice2);
        residue = MyStringUtil.getNewSubMoney(temp2,totalReceivablePrice2);
        data.put("totalContractPrice",totalContractPrice);
        data.put("totalAccumulativePrice",totalAccumulativePrice);
        data.put("totalReceivablePrice",totalReceivablePrice);
        data.put("totalFinalPrice",totalFinalPrice);
        data.put("totalContractPrice2",totalContractPrice2);
        data.put("totalAccumulativePrice2",totalAccumulativePrice2);
        data.put("totalReceivablePrice2",totalReceivablePrice2);
        data.put("totalFinalPrice2",totalFinalPrice2);
        data.put("residue",residue);
        List<ActHistoryDto> actHistoryDtos = actService.listPassedHistoryDto(item.getProcessInstanceId());
        List<ActHistoryDto> chargeList=Lists.newArrayList();
        for (ActHistoryDto dto:actHistoryDtos){
            if (dto.getActivityName()==null){
                break;
            }
            if ("????????????".equals(dto.getActivityName())){
                data.put("projectManager",dto);
            }
            if ("???????????????".equals(dto.getActivityName())){
                chargeList.add(dto);
            }
            if ("???????????????".equals(dto.getActivityName())){
                data.put("engineeringManagementDepartment",dto);
            }
            if ("????????????????????????".equals(dto.getActivityName())){
                data.put("financialDepartment",dto);
            }
            if ("???????????????????????????".equals(dto.getActivityName())){
                data.put("deputyManager",dto);
            }
            if ("????????????".equals(dto.getActivityName())){
                data.put("chiefAccountant",dto);
            }
            if (dto.getActivityName().contains("?????????")){
                data.put("chiefManager",dto);
            }
        }
        //????????????????????????
        List deptChargeMen = Lists.newArrayList();
        Map deptMans =new HashMap();
        for(int i = 0 ;i<chargeList.size();i++){
            //?????????1.2.3????????????
            int num =(i+1)%3;
            if(num == 0){
                num=3;
            }
            deptMans.put("userName"+num,chargeList.get(i).getUserName());
            deptMans.put("userLogin"+num,chargeList.get(i).getUserLogin());
            deptMans.put("end"+num,chargeList.get(i).getEnd());
            if(num==3){
                deptChargeMen.add(deptMans);
                deptMans = new HashMap();
            }
            //????????????????????????
            if(i==chargeList.size()-1&&num!=3){
                deptChargeMen.add(deptMans);
            }
        }
        data.put("deptChargeMen",deptChargeMen);
        data.put("nodes",actHistoryDtos);

        return data;
    }

    /**
     * ??????EXCL ???????????? ????????????
     * @param data
     * @param planId
     * @param userLogin
     * @throws ParseException
     */
    @Transactional
    public void uploadExcl(List<Map> data,int  planId,String userLogin) throws ParseException {

        Assert.state(data.size() > 1,"???????????????????????????????????????????????????????????????!");
        Date now = new Date();
        //Assert.state(data.get(0).size()>=9,"??????????????????12???(?????????????????????????????????)!");

        if (data.size() > 0) {
            for (int i = 1; i < data.size(); i++) {
                try {
                    Map map = data.get(i);
                    Map <String,Object> params=Maps.newHashMap();
                    params.put("projectfundplanId",planId);
                    params.put("contractNo",map.get(1).toString());
                    FiveOaProjectfundplanDetail detail=new FiveOaProjectfundplanDetail();
                    if (fiveOaProjectFundPlanDetailMapper.selectAll(params).size()>0){
                        //   detail=fiveOaProjectFundPlanDetailMapper.selectAll(params).get(0);
                    }
                    detail.setType(map.get(0).toString());
                    detail.setContractNo(map.get(1).toString());
                    detail.setPurchasePlatform(map.get(2).toString());
                    detail.setPurchaseType(map.get(3).toString());
                    detail.setDeptName(map.get(4).toString());
                    detail.setProjectName(map.get(5).toString());
                    String contractPrice= MyStringUtil.moneyToString(map.getOrDefault(6,0.000000).toString());
                    String accumulativePrice= MyStringUtil.moneyToString(map.getOrDefault(7,0.000000).toString());
                    String receivablePrice= MyStringUtil.moneyToString(map.getOrDefault(8,0.000000).toString());

                    String temp =MyStringUtil.getNewSubMoney(contractPrice,accumulativePrice);
                    String finalPrice=MyStringUtil.getNewSubMoney(temp,receivablePrice);

                    detail.setContractPrice(contractPrice);
                    detail.setAccumulativePrice(accumulativePrice);
                    detail.setReceivablePrice(receivablePrice);
                    detail.setFinalPrice(finalPrice);

                    detail.setRemark(map.get(10).toString());
                    detail.setProjectfundplanId(planId+"");
                    detail.setDeleted(false);
                    detail.setGmtModified(new Date());
                    detail.setCreator(userLogin);
                    Map <String,Object> params2=Maps.newHashMap();
                    params2.put("deleted",false);
                    params2.put("type",detail.getType());
                    params2.put("projectfundplanId",planId);
                    int  seq = fiveOaProjectFundPlanDetailMapper.selectAll(params2).size()+1;
                    detail.setSeq(seq);
                    detail.setCreatorName(selectEmployeeService.getNameByUserLogin(userLogin));
                    if (detail.getId()==null||detail.getId()==0){
                        detail.setGmtCreate(new Date());
                        fiveOaProjectFundPlanDetailMapper.insert(detail);
                    }else {
                        fiveOaProjectFundPlanDetailMapper.updateByPrimaryKey(detail);
                    }
                }catch (Exception e){
                    Assert.state(true,"?????????????????????????????????");
                }

            }
        }
    }

    /**
     * ??????????????????????????????????????? ????????????????????????
     * @param planId
     */
   public void getUpMonthsDate(int planId){
       FiveOaProjectFundPlanDto fiveOaProjectFundPlanDto = getModelById(planId);
       Assert.state(!fiveOaProjectFundPlanDto.getContractNo().equals(""),"??????????????????");
       Map parmas=Maps.newHashMap();
       parmas.put("deleted",false);
       parmas.put("contractNo",fiveOaProjectFundPlanDto.getContractNo());
       parmas.put("processEnd",true);
       List<FiveOaProjectfundplan> listProjectFundPlan = fiveOaProjectFundPlanMapper.selectAll(parmas);
       Assert.state(listProjectFundPlan.size()>0,"????????????????????????"+fiveOaProjectFundPlanDto.getContractNo()+"?????????????????????");
       listProjectFundPlan.stream().sorted(Comparator.comparing(FiveOaProjectfundplan::getGmtModified)).collect(Collectors.toList());

       FiveOaProjectfundplan fiveOaProjectfundplan = listProjectFundPlan.get(0);
       List<FiveOaProjectfundplanDetail> listDetail = listDetail(fiveOaProjectfundplan.getId());
       for (FiveOaProjectfundplanDetail detail:listDetail){
           //??????????????????????????????
           Map map=Maps.newHashMap();
           map.put("deleted",false);
           map.put("contractNo",detail.getContractNo());
           map.put("projectName",detail.getProjectName());
           map.put("contractPrice",detail.getContractPrice());
           map.put("projectfundplanId",fiveOaProjectFundPlanDto.getId());
           if (fiveOaProjectFundPlanDetailMapper.selectAll(map).size()>0){
               continue;
           }
           detail.setGmtCreate(new Date());
           detail.setGmtModified(new Date());
           detail.setProjectfundplanId(planId+"");
           detail.setDeleted(false);
           detail.setId(0);
           fiveOaProjectFundPlanDetailMapper.insert(detail);
       }
   }

}
