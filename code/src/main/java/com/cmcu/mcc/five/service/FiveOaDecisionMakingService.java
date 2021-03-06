package com.cmcu.mcc.five.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.util.*;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.dto.ActHistoryDto;
import com.cmcu.mcc.five.dao.FiveOaDecisionMakingMapper;
import com.cmcu.mcc.five.dao.FiveOaDecisionMarkingDetailMapper;
import com.cmcu.mcc.five.dto.FiveOaDecisionMakingDto;
import com.cmcu.mcc.five.dto.FiveOaSignQuoteDto;
import com.cmcu.mcc.five.entity.FiveOaDecisionMaking;
import com.cmcu.mcc.five.entity.FiveOaDecisionMarkingDetail;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.entity.HrEmployee;
import com.cmcu.mcc.hr.service.HrEmployeeService;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.service.ActService;
import com.cmcu.mcc.service.impl.HandleFormService;
import com.cmcu.mcc.sys.entity.SysAttach;
import com.cmcu.mcc.sys.service.SysAttachService;
import com.cmcu.mcc.sys.web.SysAttachController;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.PictureRenderData;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FiveOaDecisionMakingService {
    @Resource
    FiveOaDecisionMakingMapper fiveOaDecisionMakingMapper;
    @Resource
    FiveOaDecisionMarkingDetailMapper fiveOaDecisionMarkingDetailMapper;
    @Resource
    HrEmployeeMapper hrEmployeeMapper;
    @Autowired
    FiveOaSignQuoteService fiveOaSignQuoteService;
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
    FiveOaDecisionMakingDetailService fiveOaDecisionMakingDetailService;
    @Autowired
    FiveActRelevanceService fiveActRelevanceService;
    @Autowired
    ActService actService;
    @Resource
    HandleFormService handleFormService;

    /**
     * ??????key??????Id
     * @param key
     * @return ??????Id
     */
    public static int getIdFromBusinessKey(String key){
        int rValue=-1;
        String id = key.split("_")[1];
        if(StringUtils.isNotEmpty(id)){
            rValue=Integer.parseInt(id);
        }
        return rValue;
    }

    public void remove(int id,String userLogin){
        FiveOaDecisionMaking item = fiveOaDecisionMakingMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"??????????????????"+item.getCreatorName()+"??????");

        //????????????
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

   public void update(FiveOaDecisionMakingDto fiveOaDecisionMaking){
       FiveOaDecisionMaking model = fiveOaDecisionMakingMapper.selectByPrimaryKey(fiveOaDecisionMaking.getId());
       model.setCompanyLeader(fiveOaDecisionMaking.getCompanyLeader());
       model.setCompanyLeaderName(fiveOaDecisionMaking.getCompanyLeaderName());
       model.setBusinessId(fiveOaDecisionMaking.getBusinessId());
       model.setMeetingType(fiveOaDecisionMaking.getMeetingType());
       model.setItem(fiveOaDecisionMaking.getItem());
       model.setMeetingTime(fiveOaDecisionMaking.getMeetingTime());
       model.setDeptId(fiveOaDecisionMaking.getDeptId());
       model.setDeptName(fiveOaDecisionMaking.getDeptName());
       model.setRemark(fiveOaDecisionMaking.getRemark());
       model.setGmtModified(new Date());
       model.setMeetingTimePlan(fiveOaDecisionMaking.getMeetingTimePlan());
       model.setMeetingResult(fiveOaDecisionMaking.getMeetingResult());
       model.setCompere(fiveOaDecisionMaking.getCompere());
       model.setCompereName(fiveOaDecisionMaking.getCompereName());
       model.setAttender(fiveOaDecisionMaking.getAttender());
       model.setAttenderName(fiveOaDecisionMaking.getAttenderName());
       model.setRecordMan(fiveOaDecisionMaking.getRecordMan());
       model.setRecordManName(fiveOaDecisionMaking.getRecordManName());
       model.setYear(fiveOaDecisionMaking.getYear());
       model.setStages(fiveOaDecisionMaking.getStages());
       model.setAbsentLeader(fiveOaDecisionMaking.getAbsentLeader());
       fiveOaDecisionMakingMapper.updateByPrimaryKey(model);
       Map variables = Maps.newHashMap();
       variables.put("companyLeaders", model.getCompanyLeader()+","+model.getAttender()+","+model.getRecordMan()+","+model.getCompere()+","+getMarkingAttendMen(model.getBusinessKey()));
       variables.put("sign",0);
       if (model.getMeetingType().contains("?????????")){
           variables.put("processDescription","????????????????????????"+model.getItem());
           variables.put("deptChargeMan",selectEmployeeService.getParentDeptChargeMen(72,1,false));
       }else if (model.getMeetingType().contains("??????")){
           variables.put("processDescription","????????????(??????)???"+model.getItem());
           variables.put("sign",1);
           variables.put("copyManList",model.getAttender()+","+getMarkingAttendMen(model.getBusinessKey()));//??????????????????+ 2021-01-05HNZ ???????????????
           variables.put("companyLeaderList",MyStringUtil.getStringList(model.getCompanyLeader()));
       }else if (model.getMeetingType().contains("?????????")){
           variables.put("processDescription","????????????????????????"+model.getItem());
       }

       BeanValidator.check(model);
       myActService.setVariables(model.getProcessInstanceId(),variables);
   }

    public FiveOaDecisionMakingDto getModelById(int id){
        return getDto(fiveOaDecisionMakingMapper.selectByPrimaryKey(id));
    }

    public FiveOaDecisionMakingDto getDto(FiveOaDecisionMaking item) {
        FiveOaDecisionMakingDto dto=FiveOaDecisionMakingDto.adapt(item);

        Map param=new HashMap();
        param.put("mainBusiness",item.getBusinessKey());
        dto.setMakingNum(fiveOaDecisionMarkingDetailMapper.selectAll(param).size());
        dto.setProcessName("?????????");

        if (!dto.getProcessEnd()) {
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if(customProcessInstance ==null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaDecisionMakingMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance!=null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }
        return dto;
    }

    /* *
    ????????????
    **/
    public int getNewModel(String userLogin){
        List<String> userLoginList1 =  hrEmployeeService.selectUserByRoleNames("????????????????????????????????????");
        List<String> userLoginList2 =  hrEmployeeService.selectUserByRoleNames("????????????????????????????????????");
        List<String> userLoginList3 =  hrEmployeeService.selectUserByRoleNames("????????????????????????????????????");
        FiveOaDecisionMaking item=new FiveOaDecisionMaking();

        String join1 = StringUtils.join(userLoginList1, ",");
        boolean flag=false;
        if (join1.contains(userLogin)){
            item.setMeetingType("?????????");
            flag=true;
        }
        String join2 = StringUtils.join(userLoginList2, ",");
        if (join2.contains(userLogin)){
            item.setMeetingType("?????????");
            flag=true;
        }
        String join3 = StringUtils.join(userLoginList3, ",");
        if (join3.contains(userLogin)){
            item.setMeetingType("?????????");
            flag=true;
        }
        Assert.state(flag,"??????????????????????????????????????????");
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        Map<String,Object> map=Maps.newHashMap();
        map.put("deleted",false);
        map.put("year",year);
        map.put("meetingType",item.getMeetingType());
        map.put("processEnd",true);
       int stage=  fiveOaDecisionMakingMapper.selectAll(map).size()+1;//


        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setYear(year);
        item.setStages(NumberChangeUtils.ToCH(stage));

        ModelUtil.setNotNullFields(item);
        fiveOaDecisionMakingMapper.insert(item);
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        String businessKey= EdConst.Five_OA_DECISION_MAKING+ "_" + item.getId();
        String processInstanceId="";
        if (item.getMeetingType().equals("?????????")){
             businessKey= EdConst.Five_OA_DECISION_MAKING_PARTY+ "_" + item.getId();
             variables.put("processDescription","????????????????????????"+item.getItem());
            variables.put("deptChargeMan",selectEmployeeService.getParentDeptChargeMen(72,1,false));
             processInstanceId = taskHandleService.startProcess(EdConst.Five_OA_DECISION_MAKING_PARTY,businessKey, variables, MccConst.APP_CODE);
        }else if (item.getMeetingType().equals("?????????")){
            businessKey= EdConst.Five_OA_DECISION_MAKING_DIRECTOR+ "_" + item.getId();
            variables.put("processDescription","????????????(??????)???"+item.getItem());
            processInstanceId = taskHandleService.startProcess(EdConst.Five_OA_DECISION_MAKING_DIRECTOR,businessKey, variables, MccConst.APP_CODE);
        }else if (item.getMeetingType().equals("?????????")){
            businessKey= EdConst.Five_OA_DECISION_MAKING_OFFICE+ "_" + item.getId();
            variables.put("processDescription","????????????????????????"+item.getItem());
            processInstanceId = taskHandleService.startProcess(EdConst.Five_OA_DECISION_MAKING_OFFICE,businessKey, variables, MccConst.APP_CODE);
        }



        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);
        fiveOaDecisionMakingMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    /*
    ???????????????????????????
    * */
    public void createDecisionBySignBusinessKey(String businessKey){
        //??????
        int id=getIdFromBusinessKey(businessKey);
        FiveOaSignQuoteDto fiveOaSignQuoteDto = fiveOaSignQuoteService.getModelById(id);
        //?????????????????? ??????????????????
        String[] types = fiveOaSignQuoteDto.getMeetingType().split(",");
        if ("".equals(fiveOaSignQuoteDto.getMeetingType())){
            return ;
        }
        for (String type:types){
            HrEmployee defualtUser=null;
            if(StringUtils.equalsIgnoreCase("?????????",type)){
                defualtUser =  hrEmployeeService.selectDefualtUserByRoleName("????????????????????????????????????");
            }
            else  if(StringUtils.equalsIgnoreCase("?????????",type)){
                defualtUser =  hrEmployeeService.selectDefualtUserByRoleName("????????????????????????????????????");
            }
            else  if(StringUtils.equalsIgnoreCase("????????????????????????",type)){
                defualtUser =  hrEmployeeService.selectDefualtUserByRoleName("????????????????????????????????????");
            }
            else if(StringUtils.equalsIgnoreCase("?????????",type)){
                defualtUser =  hrEmployeeService.selectDefualtUserByRoleName("????????????????????????????????????");
            }
            //?????????????????? ?????????????????????
            Map param=new HashMap();
            param.put("detailType",type);
            param.put("linkedBusiness",fiveOaSignQuoteDto.getBusinessKey());//linkedBusiness
            List<FiveOaDecisionMarkingDetail> exist=fiveOaDecisionMarkingDetailMapper.selectAll(param);
            if(exist==null||exist.size()==0) {
                FiveOaDecisionMarkingDetail model=new FiveOaDecisionMarkingDetail();
                if(defualtUser!=null)
                {
                    model.setCreator(defualtUser.getUserLogin());
                    model.setCreatorName(defualtUser.getUserName());
                }
                model.setGmtCreate(new Date());
                model.setGmtModified(new Date());
                model.setSeq(1);
                model.setDeleted(false);


                model.setDeptId(fiveOaSignQuoteDto.getDeptId());
                model.setDeptName(fiveOaSignQuoteDto.getDeptName());
                model.setTitle(fiveOaSignQuoteDto.getItem()+"(?????????"+fiveOaSignQuoteDto.getCompanyNo()+")");
                model.setLeader(fiveOaSignQuoteDto.getCompanyLeaderName());
                model.setLinkedBusiness(fiveOaSignQuoteDto.getBusinessKey());
                model.setDetailType(type);
                model.setIssueStatus("?????????");
                ModelUtil.setNotNullFields(model);
                fiveOaDecisionMarkingDetailMapper.insert(model);
                model.setBusinessKey("fiveOaDecisionMarkingDetail_"+model.getId());
                fiveOaDecisionMarkingDetailMapper.updateByPrimaryKey(model);
            }else {
                //2020-12-18HNZ ????????????????????????????????? ??????
                FiveOaDecisionMarkingDetail detail = exist.get(0);
                detail.setDeleted(false);
                detail.setGmtModified(new Date());
                fiveOaDecisionMarkingDetailMapper.updateByPrimaryKey(detail);
            }
        }
        //2020-12-18HNZ ?????????????????????????????????
        Map param=new HashMap();
        param.put("linkedBusiness",fiveOaSignQuoteDto.getBusinessKey());//linkedBusiness
        List<FiveOaDecisionMarkingDetail> exist=fiveOaDecisionMarkingDetailMapper.selectAll(param);
        if (types.length!=exist.size()){
            for (FiveOaDecisionMarkingDetail detail:exist){
                if (!fiveOaSignQuoteDto.getMeetingType().contains(detail.getDetailType())){
                    detail.setDeleted(true);
                    detail.setGmtModified(new Date());
                    fiveOaDecisionMarkingDetailMapper.updateByPrimaryKey(detail);
                }
            }
        }

    }

    public FiveOaDecisionMakingDto getModelByBusinessId(String  businessId){
        Map<String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("businessId",businessId);
        List<FiveOaDecisionMaking> selectAll = fiveOaDecisionMakingMapper.selectAll(params);
        if (fiveOaDecisionMakingMapper.selectAll(params).size()>0){
            return getDto(selectAll.get(0));
        }

        return new FiveOaDecisionMakingDto();
    }

    /**
     * ????????????
     * */
    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptListOa(userLogin,uiSref);
        if (deptIdList.size()==0){
            params.put("creator",userLogin);
        }else {
            params.put("deptIdList",deptIdList);
        }
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaDecisionMakingMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaDecisionMaking item=(FiveOaDecisionMaking)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    /**
     * 2021-01-05HNZ ?????????????????????????????? ???????????????
     * ??????????????????
     * @param mainBusiness ???????????????
     * @return
     */
    public String getMarkingAttendMen(String mainBusiness){
        Map<String,Object> map=Maps.newHashMap();
        map.put("deleted",false);
        map.put("mainBusiness",mainBusiness);
        List<FiveOaDecisionMarkingDetail> list = fiveOaDecisionMarkingDetailMapper.selectAll(map);
        String attendMen="";
        List<String> attender=Lists.newArrayList();
        for (FiveOaDecisionMarkingDetail detail:list){
            String[] split = detail.getAttendance().split(",");
            for (String name:split){
                if (name!=""){
                    attender.add(name);
                }
            }
        }
        attender= attender.stream().distinct().collect(Collectors.toList());//?????????
        attendMen=StringUtils.join(attender,",");
        return attendMen;
    }


    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaDecisionMaking item = fiveOaDecisionMakingMapper.selectByPrimaryKey(id);
        Map<String,Object> map=Maps.newHashMap();
        map.put("deleted",false);
        map.put("mainBusiness",item.getBusinessKey());
        List<FiveOaDecisionMarkingDetail> list = fiveOaDecisionMarkingDetailMapper.selectAll(map);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String number = sdf.format(new Date())+item.getCreator().substring((item.getCreator()).length() - 4);
        data.put("tableNo",number);
        data.put("item",item.getItem());
        data.put("stages",item.getStages());

        data.put("companyLeaderName",selectEmployeeService.getNameByUserLogins(item.getCompanyLeader()));
        data.put("companyLeaderName2",selectEmployeeService.getNameByUserLogins(item.getCompanyLeader()).replaceAll(",","???"));//???????????? xxx ???xxx


        if (!Strings.isNullOrEmpty(item.getMeetingTimePlan())){
            data.put("meetingTimePlan",MyDateUtil.strToDateSP(item.getMeetingTimePlan()));//??????2020???9???19??? ??????????????? ?????????13???00
            data.put("meetingTimePlan2",MyDateUtil.strToDateSP2(item.getMeetingTimePlan()));//?????? 2020???9???19???
            data.put("meetingTimePlan3",MyDateUtil.strToDateSP3(item.getMeetingTimePlan()));//?????? 9???19???
        }

        data.put("year",item.getYear());
        data.put("deptName",item.getDeptName());//??????
        data.put("meetingTime",item.getMeetingTime());//??????
        data.put("compereName",item.getCompereName());//?????????
        data.put("recordManName",item.getRecordManName());//?????????
        data.put("meetType",item.getMeetingType());//????????????
            if ("?????????".equals(item.getMeetingType())){
            data.put("meetingType","??????????????????");//????????????2
                data.put("deptName2","???????????????????????????");
        }else if ("?????????".equals(item.getMeetingType())){
            data.put("meetingType","???????????????");
                data.put("deptName2","??????????????????????????????");
        }else if ("?????????".equals(item.getMeetingType())){
            data.put("meetingType","?????????????????????");
                data.put("deptName2","???????????????????????????");
        }else if ("????????????????????????".equals(item.getMeetingType())){
                data.put("meetingType","????????????????????????");
                data.put("deptName2","??????????????????????????????");
        }
        data.put("meetingResult",item.getMeetingResult());
        data.put("creatorName",item.getCreatorName());
        data.put("gmtCreate",item.getGmtCreate());
        data.put("attenderName",selectEmployeeService.getNameByUserLogins(item.getAttender()));//????????????
        data.put("attenderName3",selectEmployeeService.getNameByUserLogins(item.getAttender()).replaceAll(",","???"));//???????????? ??????
        List<Map> attenderName2=Lists.newArrayList();
        List<String> attenderName3=Lists.newArrayList();
        List<String> attenderName5=Lists.newArrayList();//????????? + ?????????????????????
        List<String> attender=Lists.newArrayList();
        List<Map> detailList=Lists.newArrayList();//????????????
        List<Map> trafficList=Lists.newArrayList();//????????????
        List<Map> allList=Lists.newArrayList();//????????????
        Map attend=Maps.newHashMap();
        int array=0;
        int traffic=0;
        int allarray=0;
       for (int i=0;i<list.size();i++){
           Map map1=Maps.newHashMap();
           Map map2=Maps.newHashMap();
           Map map3=Maps.newHashMap();
           FiveOaDecisionMarkingDetail detail = list.get(i);
           String[] split = detail.getAttendance().split(",");

           for (String name:split){
               String s="";
               if (attend.get(name)!=null){
                    s=""+ attend.get(name).toString()+",";
               }
               attender.add(name);
               attend.put(name,s+(array+1));
           }

           map1.put("array",NumberChangeUtils.ToCH(array+1)+"???");//???????????? ????????????
           map1.put("array2",(array+1)+".");//???????????? ???1.???
           map1.put("title",detail.getTitle());//??????
           map1.put("conclusion",detail.getConclusion());//????????????

           map3.put("array",NumberChangeUtils.ToCH(allarray+1)+"???");//???????????? ????????????
           map3.put("array2",(allarray+1)+".");//???????????? ???1.???
           map3.put("title",detail.getTitle());//??????
           map3.put("conclusion",detail.getConclusion());//????????????

           map2.put("array",(traffic+1)+".");
           map2.put("title",detail.getTitle());//??????

           if (detail.getDetail().equals("????????????")){
               detailList.add(map1);
               array++;
           }else {
               trafficList.add(map2);
               traffic++;
           }
           allList.add(map3);
           allarray++;

       }
        attender= attender.stream().distinct().collect(Collectors.toList());//?????????
       // attenderName5.add(item.getCreatorName());
       for (String aname:attender){
           Map mapvalue=Maps.newHashMap();
           mapvalue.put("name",selectEmployeeService.getNameByUserLogin(aname) +"(?????????"  + attend.get(aname).toString()+"????????????");
           attenderName2.add(mapvalue);//????????????
           attenderName3.add(selectEmployeeService.getNameByUserLogin(aname));
           attenderName5.add(selectEmployeeService.getNameByUserLogin(aname));
       }

        data.put("attenderName2",attenderName2);//????????? ???????????? ???????????? 1???2 ?????????
        data.put("attenderName4",attenderName3);//????????? xxx,xxx
        data.put("attenderName5",attenderName5);//????????? ????????????+xxx,xxx
        data.put("detailList",detailList);
        data.put("trafficList",trafficList);
        data.put("allList",allList);

       // ?????????  ?????????????????????
        List<ActHistoryDto> actHistoryDtos = actService.listPassedHistoryDto(item.getProcessInstanceId());
        String attenderPic="";
        for (ActHistoryDto act:actHistoryDtos){
            if (act.getActivityName()==null){
                break;
            }
            if (act.getActivityName().indexOf("????????????")>=0){
              attenderPic+=act.getUserLogin()+",";
            }
        }
        data.put("companyLeader",selectEmployeeService.selectByUserLogins(attenderPic));//seq??????????????????

       if (StringUtils.isNotBlank(item.getAbsentLeader())){
           String[] absents = item.getAbsentLeader().replaceAll("???", ";").split(";");//????????????
           List<Map> absentList=Lists.newArrayList();
           for (String absent:absents){
               Map ab=Maps.newHashMap();
               ab.put("name",absent);
               absentList.add(ab);
           }
           data.put("absentList",absentList);
       }


        return data;
    }

    public List<FiveOaDecisionMarkingDetail> listDetail(String businessId,String userLogin){
        Map<String,Object> map=Maps.newHashMap();
        map.put("deleted",false);
        map.put("businessId",businessId);
        /*???????????? ?????? ????????????????????????*/
        map.put("attendance",userLogin);
        List<FiveOaDecisionMarkingDetail> fiveOaDecisionMarkingDetails = fiveOaDecisionMarkingDetailMapper.selectAll(map);
        return fiveOaDecisionMarkingDetails;
    }

    public void updateDecisionDetail(FiveOaDecisionMarkingDetail fiveOaDecisionMarkingDetail){
        FiveOaDecisionMarkingDetail model = fiveOaDecisionMarkingDetailMapper.selectByPrimaryKey(fiveOaDecisionMarkingDetail.getId());
        BeanUtils.copyProperties(fiveOaDecisionMarkingDetail, model);
        model.setGmtModified(new Date());
        fiveOaDecisionMarkingDetailMapper.updateByPrimaryKey(model);
    }


    public XWPFTemplate getXWPFTemplate(String filePath, int id) {
       // FiveOaDecisionMaking item = fiveOaDecisionMakingMapper.selectByPrimaryKey(id);
        Map printData = getPrintData(id);

        Configure.ConfigureBuilder configureBuilder = Configure.newBuilder();

        XWPFTemplate xwpfTemplate = XWPFTemplate.compile(filePath,configureBuilder.build()).render(new HashMap<String, Object>(){{
            //put("pic", new PictureRenderData(80, 30, new)); //{{@pic}}

            put("meetType",printData.getOrDefault("meetType",""));
            put("meetingType",printData.getOrDefault("meetingType",""));
            put("deptName2",printData.getOrDefault("deptName2",""));
            put("item",printData.getOrDefault("item",""));
            put("year",printData.getOrDefault("year",""));
            put("stages",printData.getOrDefault("stages",""));
            put("meetingTimePlan",printData.getOrDefault("meetingTimePlan",""));
            put("meetingTimePlan2",printData.getOrDefault("meetingTimePlan2",""));
            put("meetingTime",printData.getOrDefault("meetingTime",""));
            put("compereName",printData.getOrDefault("compereName",""));
            put("meetingResult",printData.getOrDefault("meetingResult",""));
            List<HashMap>detailList = (ArrayList)printData.getOrDefault("detailList",new ArrayList<>());
            String detail = "";
            for(HashMap m:detailList){
                detail =detail+m.getOrDefault("array","")+m.getOrDefault("title","")+'\n';
            }
            String detailConclusion = "";
            for(HashMap m:detailList){
                detailConclusion =detailConclusion+m.getOrDefault("array","")+m.getOrDefault("conclusion","")+'\n';
            }
            put("detailConclusion",detailConclusion);
            put("detail",detail);
            put("attenderName",printData.getOrDefault("attenderName",""));
            List<String> attenderName5=(List<String>) printData.getOrDefault("attenderName5", "");
            put("attenderName5",String.join(" ",attenderName5));
            put("companyLeaderName",printData.getOrDefault("companyLeaderName",""));
            put("deptName",printData.getOrDefault("deptName",""));
            put("recordManName",printData.getOrDefault("recordManName",""));

            List<HashMap> detailList2 = (ArrayList)printData.getOrDefault("attenderName2",new ArrayList<>());
            String detail2 = "";
            for(HashMap map:detailList2){
                detail2 =detail2+map.getOrDefault("name","")+'\n';
            }
            put("detail2",detail2);

        }});
        return xwpfTemplate;
    }

}
