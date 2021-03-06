package com.cmcu.mcc.five.service;


import com.cmcu.act.dto.CustomProcessInstance;
import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.*;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.dto.ActHistoryDto;
import com.cmcu.mcc.five.dao.FiveOaSignQuoteMapper;
import com.cmcu.mcc.five.entity.FiveOaSignQuote;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dao.HrEmployeeSysMapper;
import com.cmcu.mcc.hr.dto.HrDeptDto;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.five.dto.FiveOaSignQuoteDto;
import com.cmcu.mcc.hr.entity.HrEmployeeSys;
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
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.*;

@Service
public class FiveOaSignQuoteService extends BaseService {

    @Resource
    FiveOaSignQuoteMapper fiveOaSignQuoteMapper;
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
    @Autowired
    HrDeptService hrDeptService;
    @Resource
    HrEmployeeSysMapper hrEmployeeSysMapper;
    @Resource
    HandleFormService handleFormService;



   public void remove(int id,String userLogin){
       FiveOaSignQuote item = fiveOaSignQuoteMapper.selectByPrimaryKey(id);
       Assert.state(item.getCreator().equals(userLogin),"??????????????????"+item.getCreatorName()+"??????");

       //????????????
       handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

   }

    public void update(FiveOaSignQuoteDto item){

       FiveOaSignQuote model = fiveOaSignQuoteMapper.selectByPrimaryKey(item.getId());
       model.setDeptId(item.getDeptId());
       model.setDeptName(item.getDeptName());
       model.setFormNo(item.getFormNo());
       model.setBelongThreeOne(item.getBelongThreeOne());
       model.setItem(item.getItem());
       model.setDeptChargeMan(item.getDeptChargeMan());
       model.setDeptChargeManName(item.getDeptChargeManName());
       model.setCompanyNo(item.getCompanyNo());
       model.setAgent(item.getAgent());
       model.setAgentName(item.getAgentName());
       model.setCompanyCheckMan(item.getCompanyCheckMan());
       model.setCompanyCheckManName(item.getCompanyCheckManName());
       model.setSubmitTime(item.getSubmitTime());
       model.setRemark(item.getRemark());
       model.setCompanyLeader(item.getCompanyLeader());//????????????-??????
       model.setCompanyLeaderName(item.getCompanyLeaderName());
       model.setInstructLeader(item.getInstructLeader());//????????????
       model.setInstructLeaderName(item.getInstructLeaderName());
       model.setInstructDeptId(item.getInstructDeptId());//???????????? ??????
       model.setInstructDeptName(item.getInstructDeptName());
       model.setFlag(item.getFlag());//??????
       model.setSign(item.getSign());//????????????
       model.setTab(item.getTab());//???????????????
       model.setMeetingType(item.getMeetingType());
       model.setGmtModified(new Date());
       model.setBelongType(item.getBelongType());
       model.setBelongContent(item.getBelongContent());

       BeanValidator.validateObject(model);
       ModelUtil.setNotNullFields(model);
       fiveOaSignQuoteMapper.updateByPrimaryKey(model);

       Map variables = Maps.newHashMap();
       if ("?????????".equals(model.getFlag())){
           //????????? ??????????????????????????????
           variables.put("flag",0);
           variables.put("sign",0);
           variables.put("table",0);

       }else if ("????????????".equals(model.getFlag())){
           //???????????? ??????????????????
           variables.put("flag",1);
           variables.put("table",0);
           if (!"".equals( model.getInstructDeptId())){
               String[] split = model.getInstructDeptId().split(",");
               List<String> departmentMenList=Lists.newArrayList();
               for (String deptId:split){
                   departmentMenList.addAll( selectEmployeeService.getDeptChargeMen(Integer.parseInt(deptId)));
               }
               variables.put("departmentMenList",departmentMenList);//????????????
           }
           if ("???".equals(model.getSign())){
               variables.put("sign",0);
           }else {
               variables.put("sign",1);
               variables.put("legalReviewMan",selectEmployeeService.getUserListByRoleName("????????????"));//????????????
           }
       }else {
           //???????????? ?????? ???????????? ????????????????????????
           variables.put("flag",2);
           variables.put("sign",0);
           List<String> reviewMenList=Lists.newArrayList();
           variables.put("reviewProcessMan",selectEmployeeService.getUserListByRoleName("????????????"));//????????????
           variables.put("reviewTextMan",selectEmployeeService.getUserListByRoleName("????????????"));//????????????
           variables.put("legalReviewMan",selectEmployeeService.getUserListByRoleName("????????????"));//????????????

           reviewMenList.addAll(selectEmployeeService.getUserListByRoleName("????????????"));
           reviewMenList.addAll(selectEmployeeService.getUserListByRoleName("????????????"));
           reviewMenList.addAll(selectEmployeeService.getUserListByRoleName("????????????"));

           variables.put("reviewMenList",reviewMenList);//????????????
           String[] split = model.getInstructDeptId().split(",");
           List<String> departmentMenList=Lists.newArrayList();
           for (String deptId:split){
               departmentMenList.addAll( selectEmployeeService.getDeptChargeMen(Integer.parseInt(deptId)));
           }
           variables.put("departmentMenList",departmentMenList);//????????????

           if ("????????????".equals(model.getTab())){
               variables.put("table",1);
               variables.put("totalLegalReviewMan",selectEmployeeService.getUserListByRoleName("???????????????"));//???????????????
           }else {
               variables.put("table",0);
           }
       }

       variables.put("officeChargeMan",MyStringUtil.getStringList(model.getCompanyCheckMan()));//????????????????????????
       variables.put("companyLeader", MyStringUtil.getStringList(model.getInstructLeader()));//??????????????????

       //???????????? 2020-12-18HNZ ?????????????????? ???????????? ????????? ?????????????????????(??????????????????) ??????????????????
        if (StringUtils.isNotBlank(model.getCompanyLeader())){
            //????????????????????????
            if (hrEmployeeService.IsPrincipalLeader(model.getCompanyLeader())){
                variables.put("lable",2);
                //??????????????????
                variables.put("viceLeaderList",hrEmployeeService.getViceLeader(model.getCompanyLeader()));
                //??????????????????
                variables.put("principalLeaderList",hrEmployeeService.getPrincipal(model.getCompanyLeader()));
            }else {
                //??????????????????
                variables.put("lable",1);
                //??????????????????
                variables.put("viceLeaderList",hrEmployeeService.getViceLeader(model.getCompanyLeader()));
            }
        }else {
            variables.put("lable",0);
        }

       //????????????????????????
       if("".equals(model.getMeetingType())){
           variables.put("meet",0);
       }else {
           variables.put("meet",1);
          // variables.put("processDescription","???????????????"+model.getItem());
           variables.put("companyLeaders", MyStringUtil.getStringList(model.getCompanyLeader()));
           List<String> decisinMakingList=new ArrayList<>();
           if(item.getMeetingType().contains("?????????")){
               decisinMakingList.add("2412");
           }
           if(item.getMeetingType().contains("?????????")){
               decisinMakingList.add("2802");
           }
           if(item.getMeetingType().contains("?????????")){
               decisinMakingList.add("2055");
           }
           //?????????????????????
           //2020-12-18HNZ ?????????????????? ??????????????? ??????12-10????????????????????????????????????????????????
           int  twoDay =Integer.parseInt(MyDateUtil.getTwoDay("2020-12-10",MyDateUtil.dateToStr(item.getGmtCreate()) )) ;
           if (twoDay>0){
               //12-10?????????????????????
               variables.put("decisionMakingMan",decisinMakingList);
           }else {
               //12-10????????????????????? ??????
               variables.put("decisionMakingMen",decisinMakingList);
           }
       }
       variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDeptId()));//????????????
       variables.put("processDescription","?????????"+item.getItem());
       BeanValidator.check(model);
       myActService.setVariables(model.getProcessInstanceId(),variables);


   }

    public FiveOaSignQuoteDto getModelById(int id){
           return getDto(fiveOaSignQuoteMapper.selectByPrimaryKey(id));
    }

    public FiveOaSignQuoteDto getDto(FiveOaSignQuote item) {
        FiveOaSignQuoteDto dto=FiveOaSignQuoteDto.adapt(item);
        dto.setProcessName("?????????");

        CustomProcessInstance customProcessInstance = processQueryService.getCustomProcessInstance(dto.getProcessInstanceId(), "", "");
        if(customProcessInstance!=null) {
            dto.setProcessName(customProcessInstance.getCurrentTaskName());
            //??????????????? ????????????
            if (customProcessInstance.getCurrentTaskName().contains("????????????-????????????")&&(customProcessInstance.getClaimTime()!=null)) {
                dto.setSubmitTime(DateFormatUtils.format(customProcessInstance.getClaimTime(),"yyyy-MM-dd HH:mm:ss"));
                fiveOaSignQuoteMapper.updateByPrimaryKey(dto);
            }
            if (!item.getProcessEnd() && customProcessInstance.isFinished()) {
                dto.setProcessEnd(true);
                fiveOaSignQuoteMapper.updateByPrimaryKey(dto);
            }
        }
      
        return dto;
    }

    /**
     * 2021-01-11HNZ
     * list??????????????????????????????????????? ??????????????????
     */
    public FiveOaSignQuoteDto getDtoList(FiveOaSignQuote item) {
        FiveOaSignQuoteDto dto=FiveOaSignQuoteDto.adapt(item);
        dto.setProcessName("?????????");
         if (!dto.getProcessEnd()) {
             CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), "", "");
             if (customProcessInstance == null || customProcessInstance.isFinished()) {
                 dto.setProcessEnd(true);
                 fiveOaSignQuoteMapper.updateByPrimaryKey(dto);
             } else {
                 dto.setProcessName(customProcessInstance.getCurrentTaskName());
             }
         }


        return dto;
    }


    public int getNewModel(String userLogin){
       List<String> secretary =  hrEmployeeService.selectUserByRoleNames("????????????");
       Assert.state(secretary.size()>0,"?????????????????? ???????????? ??????");

       FiveOaSignQuote item=new FiveOaSignQuote();
       HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
       HrDeptDto deptDto = hrDeptService.getModelById(hrEmployeeDto.getDeptId());

       item.setDeptId(hrEmployeeDto.getDeptId());
       item.setDeptName(hrEmployeeDto.getDeptName());
       //??????????????? ????????? ?????????
        if(deptDto.getDeptFirstLeader().equals("")){
            item.setDeptChargeMan(deptDto.getDeptSecondLeader());
            item.setDeptChargeManName(deptDto.getDeptSecondLeaderName());
        }else {
            item.setDeptChargeMan(deptDto.getDeptFirstLeader());
            item.setDeptChargeManName(deptDto.getDeptFirstLeaderName());
        }

       //?????????
       item.setAgent(hrEmployeeDto.getUserLogin());
       item.setAgentName(hrEmployeeDto.getUserName());

       item.setCreator(hrEmployeeDto.getUserLogin());
       item.setCreatorName(hrEmployeeDto.getUserName());

       item.setRemark("<p>\n" +
                       "\t<strong><span style=\"font-size:18px;font-family:&quot;\">???????????????</span></strong> \n" +
                       "</p>\n" +
                       "<p>\n" +
                       "\t<strong><span style=\"font-size:18px;\"><span style=\"font-family:&quot;\">&nbsp; &nbsp; &nbsp;<span style=\"color:#E53333;\"> <span style=\"color:#000000;\">&nbsp;????????????????????????</span><span style=\"color:#000000;\">&nbsp;</span></span></span></span></strong> \n" +
                       "</p>\n" +
                       "<p>\n" +
                       "\t<strong><span style=\"font-size:18px;\"><span style=\"font-family:&quot;\"></span></span></strong> \n" +
                       "</p>\n" +
                       "<p class=\"MsoNormal\">\n" +
                       "\t<strong><span style=\"font-size:18px;\">?????????????????????</span></strong> \n" +
                       "</p>\n" +
                       "<p style=\"text-align:right;\">\n" +
                       "\t<span style=\"font-size:16px;font-family:&quot;\">"+item.getDeptName()+"</span> \n" +
                       "</p>\n" +
                       "<p style=\"text-align:right;\">\n" +
                       "\t<span style=\"font-size:16px;font-family:&quot;\">"+MyDateUtil.getStringDateShort()+"</span> \n" +
                       "</p>\n" +
                       "<p>\n" +
                       "\t<br />\n" +
                       "</p>"
       );
       item.setCompanyCheckMan(StringUtils.join(selectEmployeeService.getParentDeptChargeMen(59,1,false),","));//2020-12-22HNZ ?????????????????????2???????????????
       item.setCompanyCheckManName(selectEmployeeService.getNameByUserLogins(item.getCompanyCheckMan()));
       item.setDeleted(false);
       item.setProcessEnd(false);
       item.setCreator(userLogin);
       item.setGmtModified(new Date());
       item.setGmtCreate(new Date());
       item.setBelongThreeOne("");
       item.setFlag("?????????");
       item.setSign("");
       item.setTab(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"??????????????????").toString());
       item.setSubmitTime(MyDateUtil.dateToStrLong(new Date()));
       ModelUtil.setNotNullFields(item);
       fiveOaSignQuoteMapper.insert(item);

       String businessKey= EdConst.FIVE_OA_SIGNQUOTE+ "_" + item.getId();
       Map variables = Maps.newHashMap();
       variables.put("userLogin", userLogin);
       variables.put("processDescription","?????????"+item.getCreatorName());
       variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDeptId()));//????????????
       variables.put("secretary",secretary);//????????????

       String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_SIGNQUOTE,businessKey, variables, MccConst.APP_CODE);
       item.setProcessInstanceId(processInstanceId);
       item.setBusinessKey(businessKey);
       //???????????????
       //item.setCompanyNo(MyFormNo.getFormNo(taskHandleService.getDeploymentId(EdConst.FIVE_OA_SIGNQUOTE),item.getId()));
       fiveOaSignQuoteMapper.updateByPrimaryKey(item);
       return item.getId();
   }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
        params.put("isLikeSelect",true);

        //???????????????????????????????????????  myDeptData true?????????????????? false???????????????
        Map map =new HashMap();
        map.put("myDeptData",false);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaSignQuoteMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaSignQuote item=(FiveOaSignQuote)p;
            list.add(getDtoList(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }
    //??????
    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaSignQuote item = fiveOaSignQuoteMapper.selectByPrimaryKey(id);
        data.put("deptName",item.getDeptName());
        data.put("deptChargeManName",item.getDeptChargeManName());
        data.put("item",item.getItem());
        data.put("companyNo",item.getCompanyNo());
        data.put("flag",item.getFlag());
        data.put("belongThreeOne",item.getBelongThreeOne());
        data.put("companyCheckManName",item.getCompanyCheckManName());
        data.put("agentName",item.getAgentName());//submitTime
        data.put("submitTime",item.getSubmitTime());

        data.put("creatorName",item.getCreatorName());
        data.put("gmtCreate",item.getGmtCreate());

        List<ActHistoryDto> actHistoryDtos = actService.listPassedHistoryDto(item.getProcessInstanceId());

        List<Map> functionList=Lists.newArrayList();//????????????
        List<Map> designList=Lists.newArrayList();//????????????
        List<ActHistoryDto> leaderList=Lists.newArrayList();


        for (ActHistoryDto dto:actHistoryDtos){
            //?????? pc^ ????????????
            if(dto.getComment()!=null){
                dto.setComment(dto.getComment().replace("pc^",""));
            }

            if (dto.getActivityName()==null){
                break;
            }
            if ("?????????-??????".equals(dto.getActivityName())){
                data.put("companyOffice",dto);
            }
            //2020-12-18HNZ ????????????????????????
            if ("??????/??????/????????????".equals(dto.getActivityName())){
                if (dto.getUserLogin().contains(StringUtils.join(selectEmployeeService.getUserListByRoleName("????????????"),","))){
                    data.put("reviewProcess",dto);//????????????
                }
                if (dto.getUserLogin().contains(StringUtils.join(selectEmployeeService.getUserListByRoleName("????????????"),","))){
                    data.put("reviewText",dto);//????????????
                }
                if (dto.getUserLogin().contains(StringUtils.join(selectEmployeeService.getUserListByRoleName("????????????"),","))){
                    data.put("legalReview",dto);//????????????
                }

            }
            if (dto.getActivityName()!=null){
                if (dto.getActivityName().contains("??????")&&!dto.getActivityName().contains("????????????")){
                    leaderList.add(dto);
                }
            }
            if ("???????????????-??????".equals(dto.getActivityName())){
                Map<String,Object> map=Maps.newHashMap();
                HrEmployeeSys hrEmployeeSys = hrEmployeeSysMapper.selectByUserLogin(dto.getUserLogin());
                HrDeptDto hrDeptDto = hrDeptService.getModelById(hrEmployeeSys.getDeptId());
                map.put("deptName",hrDeptDto.getName());
                map.put("actHistory",dto);

                if ("??????".equals(hrDeptDto.getDeptType())){
                    designList.add(map);
                }else {
                    functionList.add(map);
                }
            }
        }

        data.put("functionList",functionList);
        data.put("designList",designList);
        data.put("nodes",actHistoryDtos);
        data.put("leaderList",leaderList);
        return data;
    }

    /**
     * ??????excel
     * @param params
     * @param uiSref
     * @param startTime1
     * @param endTime1
     * @param userLogin
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


        List<FiveOaSignQuote> fiveOaSignQuotes=fiveOaSignQuoteMapper.selectAll(params);
        for (FiveOaSignQuote dto:fiveOaSignQuotes){
            Map map1=new LinkedHashMap();
            map1.put("??????",dto.getItem());
            map1.put("???????????????",dto.getCompanyNo());
            map1.put("????????????",dto.getFlag());
            map1.put("????????????",dto.getInstructLeaderName());
            map1.put("????????????",dto.getCompanyLeaderName());
            map1.put("???????????????",dto.getCompanyCheckManName() );
            map1.put("????????????????????????????????????", dto.getBelongThreeOne());
            map1.put("????????????",dto.getDeptName() );
            map1.put("???????????????",dto.getDeptChargeManName() );
            map1.put("????????????", dto.getMeetingType());
            map1.put("?????????",dto.getAgentName());
            map1.put("????????????",dto.getSubmitTime());
            list.add(map1);
        }


        return list;
    }
}
