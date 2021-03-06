package com.cmcu.mcc.five.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.five.dao.FiveOaReportMapper;
import com.cmcu.mcc.five.dto.FiveOaReportDto;
import com.cmcu.mcc.five.entity.FiveOaReport;
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
public class FiveOaReportService extends BaseService {
    @Resource
    FiveOaReportMapper fiveOaReportMapper;
    @Resource
    HrEmployeeMapper hrEmployeeMapper;
    @Resource
    CommonCodeService commonCodeService;
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

    public void remove(int id,String userLogin){
        FiveOaReport item = fiveOaReportMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"??????????????????"+item.getCreatorName()+"??????");

        //????????????
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);


    }

    public void update(FiveOaReportDto fiveOaReportDto){
        FiveOaReport model = fiveOaReportMapper.selectByPrimaryKey(fiveOaReportDto.getId());
         model.setChargeMan(fiveOaReportDto.getChargeMan());
         model.setChargeManName(fiveOaReportDto.getChargeManName());
         model.setDeptId(fiveOaReportDto.getDeptId());
         model.setDeptName(fiveOaReportDto.getDeptName());
         model.setCompanyLeader(fiveOaReportDto.getCompanyLeader());
         model.setCompanyLeaderName(fiveOaReportDto.getCompanyLeaderName());
         model.setViceLeader(fiveOaReportDto.getViceLeader());
         model.setViceLeaderName(fiveOaReportDto.getViceLeaderName());
         model.setOfficeNo(fiveOaReportDto.getOfficeNo());
         model.setReportContent(fiveOaReportDto.getReportContent());
         model.setFlag(fiveOaReportDto.getFlag());
         model.setCompanyOfficeMan(fiveOaReportDto.getCompanyOfficeMan());
         model.setCompanyOfficeManName(fiveOaReportDto.getCompanyOfficeManName());
         model.setReportTime(fiveOaReportDto.getReportTime());
         model.setGmtModified(new Date());
         model.setRemark(fiveOaReportDto.getRemark());


         Map variables = Maps.newHashMap();//"2863"
         variables.put("companyOfficeMan",MyStringUtil.getStringList(model.getCompanyOfficeMan()));//???????????????
         variables.put("companyLeader", MyStringUtil.getStringList(model.getCompanyLeader()));//??????????????????


         if (model.getFlag().contains("??????")){
             variables.put("flag", 0); //??????????????????
         }else if ("??????????????????".equals(model.getFlag())){
             List<String> principalLeader = hrEmployeeService.getPrincipalLeader();
             variables.put("principalLeaders", principalLeader);
             variables.put("flag", 2);
         }else if ("?????????????????????".equals(model.getFlag())){
             List<String> principalLeader = hrEmployeeService.getPrincipalLeader();
             variables.put("principalLeaders", principalLeader);
             variables.put("viceLeaders", MyStringUtil.getStringList(model.getViceLeader()));//????????????
             variables.put("flag", 1);//????????????
         }

         myActService.setVariables(model.getProcessInstanceId(),variables);
        BeanValidator.check(model);
         fiveOaReportMapper.updateByPrimaryKey(model);

    }

    public FiveOaReportDto getModelById(int id){
        return getDto(fiveOaReportMapper.selectByPrimaryKey(id));
    }

    public FiveOaReportDto getDto(FiveOaReport item) {

        FiveOaReportDto dto = FiveOaReportDto.adapt(item);
        dto.setProcessName("?????????");
        if (!dto.getProcessEnd()) {
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");
            if (customProcessInstance == null || customProcessInstance.isFinished()) {
                dto.setProcessEnd(true);
                fiveOaReportMapper.updateByPrimaryKey(dto);
            } else {
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }
        return dto;
    }

    public int getNewModel(String userLogin){
        List<String> secretary =  hrEmployeeService.selectUserByRoleNames("????????????");
        Assert.state(secretary.size()>0,"?????????????????? ???????????? ??????");
        FiveOaReport item=new FiveOaReport();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());

        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setRemark("???????????????" +
                "");

        item.setFlag(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????????????????").toString());
        //?????????????????????
        List<String> deptChargeMen = selectEmployeeService.getDeptChargeMen(59);

        item.setCompanyOfficeMan(deptChargeMen.get(0));
        item.setCompanyOfficeManName(hrEmployeeMapper.selectByUserLoginOrNo(deptChargeMen.get(0)).getUserName());
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        ModelUtil.setNotNullFields(item);
        fiveOaReportMapper.insert(item);

        String businessKey= EdConst.FIVE_OA_REPORT+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "????????????"+item.getCreatorName());

        variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDeptId()));//????????????????????????
        variables.put("secretary",secretary);//????????????
        item.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_REPORT,item.getBusinessKey(), variables, MccConst.APP_CODE);

        item.setProcessInstanceId(processInstanceId);
        fiveOaReportMapper.updateByPrimaryKey(item);
        return item.getId();
    }


    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
        params.put("isLikeSelect",true);
       /*  List<Integer> deptIdList=hrEmployeeSysService.getMyDeptListOa(userLogin,uiSref);
         if (deptIdList.size()==0){
            params.put("creator",userLogin);
        }else {
            params.put("deptIdList",deptIdList);
        }*/
        //???????????????????????????????????????  myDeptData true?????????????????? false???????????????
        Map map =Maps.newHashMap();
        map.put("myDeptData",false);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaReportMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaReport item=(FiveOaReport)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaReport item = fiveOaReportMapper.selectByPrimaryKey(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String number = sdf.format(new Date())+item.getCreator().substring((item.getCreator()).length() - 4);
        data.put("tableNo",number);
        data.put("companyOfficeManName",item.getCompanyOfficeManName());//???????????????
        data.put("companyLeaderName",item.getCompanyLeaderName());//????????????
        data.put("flag",item.getFlag());//????????????
        data.put("viceLeaderName",item.getViceLeaderName());//????????????
        data.put("deptName",item.getDeptName());//????????????

        data.put("chargeManName",item.getChargeManName());//???????????????
        data.put("officeNo",item.getOfficeNo());//???????????????
        data.put("reportContent",item.getReportContent());//??????
        data.put("reportTime",item.getReportTime());//????????????
        data.put("creatorName",item.getCreatorName());//?????????
        data.put("gmtCreate",item.getGmtCreate());

        data.put("nodes",actService.listPassedHistoryDto(item.getProcessInstanceId()));

        return data;
    }


    /**
     * ??????excel
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


        List<FiveOaReport> fiveOaReports=fiveOaReportMapper.selectAll(params);
        for (FiveOaReport dto:fiveOaReports){

            Map map1=new LinkedHashMap();
            map1.put("???????????????",dto.getCompanyOfficeManName());
            map1.put("????????????",dto.getCompanyLeaderName());
            map1.put("????????????",dto.getFlag());
            map1.put("????????????",dto.getDeptName());
            map1.put("?????????",dto.getChargeManName());
            map1.put("???????????????", dto.getOfficeNo());
            map1.put("????????????",dto.getReportTime());
            map1.put("??????",dto.getReportContent());
            map1.put("?????????",dto.getCreatorName());
            map1.put("????????????",dto.getGmtCreate());

            list.add(map1);
        }


        return list;
    }
}
