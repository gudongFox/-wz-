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
       Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

       //流程作废
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
       model.setCompanyLeader(item.getCompanyLeader());//公司领导-会签
       model.setCompanyLeaderName(item.getCompanyLeaderName());
       model.setInstructLeader(item.getInstructLeader());//批示领导
       model.setInstructLeaderName(item.getInstructLeaderName());
       model.setInstructDeptId(item.getInstructDeptId());//通用会签 部门
       model.setInstructDeptName(item.getInstructDeptName());
       model.setFlag(item.getFlag());//会签
       model.setSign(item.getSign());//法律审查
       model.setTab(item.getTab());//总法律顾问
       model.setMeetingType(item.getMeetingType());
       model.setGmtModified(new Date());
       model.setBelongType(item.getBelongType());
       model.setBelongContent(item.getBelongContent());

       BeanValidator.validateObject(model);
       ModelUtil.setNotNullFields(model);
       fiveOaSignQuoteMapper.updateByPrimaryKey(model);

       Map variables = Maps.newHashMap();
       if ("不会签".equals(model.getFlag())){
           //不会签 直接到公司办公司节点
           variables.put("flag",0);
           variables.put("sign",0);
           variables.put("table",0);

       }else if ("通用会签".equals(model.getFlag())){
           //通用会签 是否法律审查
           variables.put("flag",1);
           variables.put("table",0);
           if (!"".equals( model.getInstructDeptId())){
               String[] split = model.getInstructDeptId().split(",");
               List<String> departmentMenList=Lists.newArrayList();
               for (String deptId:split){
                   departmentMenList.addAll( selectEmployeeService.getDeptChargeMen(Integer.parseInt(deptId)));
               }
               variables.put("departmentMenList",departmentMenList);//部门会签
           }
           if ("否".equals(model.getSign())){
               variables.put("sign",0);
           }else {
               variables.put("sign",1);
               variables.put("legalReviewMan",selectEmployeeService.getUserListByRoleName("法律审查"));//法律审查
           }
       }else {
           //制度会签 是否 基本制度 走总法律顾问审查
           variables.put("flag",2);
           variables.put("sign",0);
           List<String> reviewMenList=Lists.newArrayList();
           variables.put("reviewProcessMan",selectEmployeeService.getUserListByRoleName("流程审查"));//流程审查
           variables.put("reviewTextMan",selectEmployeeService.getUserListByRoleName("文本审查"));//文本审查
           variables.put("legalReviewMan",selectEmployeeService.getUserListByRoleName("法律审查"));//法律审查

           reviewMenList.addAll(selectEmployeeService.getUserListByRoleName("流程审查"));
           reviewMenList.addAll(selectEmployeeService.getUserListByRoleName("文本审查"));
           reviewMenList.addAll(selectEmployeeService.getUserListByRoleName("法律审查"));

           variables.put("reviewMenList",reviewMenList);//审查会签
           String[] split = model.getInstructDeptId().split(",");
           List<String> departmentMenList=Lists.newArrayList();
           for (String deptId:split){
               departmentMenList.addAll( selectEmployeeService.getDeptChargeMen(Integer.parseInt(deptId)));
           }
           variables.put("departmentMenList",departmentMenList);//部门会签

           if ("基本制度".equals(model.getTab())){
               variables.put("table",1);
               variables.put("totalLegalReviewMan",selectEmployeeService.getUserListByRoleName("总法律顾问"));//总法律顾问
           }else {
               variables.put("table",0);
           }
       }

       variables.put("officeChargeMan",MyStringUtil.getStringList(model.getCompanyCheckMan()));//公司办公室负责人
       variables.put("companyLeader", MyStringUtil.getStringList(model.getInstructLeader()));//公司领导批示

       //领导会签 2020-12-18HNZ 如果没有领导 直接通过 有领导 是否有正职领导(排序串行会签) 副职并行会签
        if (StringUtils.isNotBlank(model.getCompanyLeader())){
            //是否含有正职领导
            if (hrEmployeeService.IsPrincipalLeader(model.getCompanyLeader())){
                variables.put("lable",2);
                //设置副职领导
                variables.put("viceLeaderList",hrEmployeeService.getViceLeader(model.getCompanyLeader()));
                //设置正职领导
                variables.put("principalLeaderList",hrEmployeeService.getPrincipal(model.getCompanyLeader()));
            }else {
                //设置副职领导
                variables.put("lable",1);
                //设置副职领导
                variables.put("viceLeaderList",hrEmployeeService.getViceLeader(model.getCompanyLeader()));
            }
        }else {
            variables.put("lable",0);
        }

       //是否召开决策会议
       if("".equals(model.getMeetingType())){
           variables.put("meet",0);
       }else {
           variables.put("meet",1);
          // variables.put("processDescription","决策会议："+model.getItem());
           variables.put("companyLeaders", MyStringUtil.getStringList(model.getCompanyLeader()));
           List<String> decisinMakingList=new ArrayList<>();
           if(item.getMeetingType().contains("党委会")){
               decisinMakingList.add("2412");
           }
           if(item.getMeetingType().contains("办公会")){
               decisinMakingList.add("2802");
           }
           if(item.getMeetingType().contains("董事会")){
               decisinMakingList.add("2055");
           }
           //决策会议管理员
           //2020-12-18HNZ 决策会管理员 串行议会签 兼容12-10以前发起得流程不是会签流程问题。
           int  twoDay =Integer.parseInt(MyDateUtil.getTwoDay("2020-12-10",MyDateUtil.dateToStr(item.getGmtCreate()) )) ;
           if (twoDay>0){
               //12-10以前发起得流程
               variables.put("decisionMakingMan",decisinMakingList);
           }else {
               //12-10以后发起得流程 会签
               variables.put("decisionMakingMen",decisinMakingList);
           }
       }
       variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDeptId()));//部门领导
       variables.put("processDescription","签报："+item.getItem());
       BeanValidator.check(model);
       myActService.setVariables(model.getProcessInstanceId(),variables);


   }

    public FiveOaSignQuoteDto getModelById(int id){
           return getDto(fiveOaSignQuoteMapper.selectByPrimaryKey(id));
    }

    public FiveOaSignQuoteDto getDto(FiveOaSignQuote item) {
        FiveOaSignQuoteDto dto=FiveOaSignQuoteDto.adapt(item);
        dto.setProcessName("已完成");

        CustomProcessInstance customProcessInstance = processQueryService.getCustomProcessInstance(dto.getProcessInstanceId(), "", "");
        if(customProcessInstance!=null) {
            dto.setProcessName(customProcessInstance.getCurrentTaskName());
            //成文日期取 领导全签
            if (customProcessInstance.getCurrentTaskName().contains("机要秘书-分发部门")&&(customProcessInstance.getClaimTime()!=null)) {
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
     * list页面调用简单的流程获取方法 加快访问速度
     */
    public FiveOaSignQuoteDto getDtoList(FiveOaSignQuote item) {
        FiveOaSignQuoteDto dto=FiveOaSignQuoteDto.adapt(item);
        dto.setProcessName("已完成");
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
       List<String> secretary =  hrEmployeeService.selectUserByRoleNames("机要秘书");
       Assert.state(secretary.size()>0,"未找到职务为 机要秘书 人员");

       FiveOaSignQuote item=new FiveOaSignQuote();
       HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
       HrDeptDto deptDto = hrDeptService.getModelById(hrEmployeeDto.getDeptId());

       item.setDeptId(hrEmployeeDto.getDeptId());
       item.setDeptName(hrEmployeeDto.getDeptName());
       //部门负责人
       item.setDeptChargeMan(deptDto.getDeptFirstLeader());
       item.setDeptChargeManName(deptDto.getDeptFirstLeaderName());
       //经办人
       item.setAgent(hrEmployeeDto.getUserLogin());
       item.setAgentName(hrEmployeeDto.getUserName());

       item.setCreator(hrEmployeeDto.getUserLogin());
       item.setCreatorName(hrEmployeeDto.getUserName());

       item.setRemark("<p>\n" +
                       "\t<strong><span style=\"font-size:18px;font-family:&quot;\">公司领导：</span></strong> \n" +
                       "</p>\n" +
                       "<p>\n" +
                       "\t<strong><span style=\"font-size:18px;\"><span style=\"font-family:&quot;\">&nbsp; &nbsp; &nbsp;<span style=\"color:#E53333;\"> <span style=\"color:#000000;\">&nbsp;请填写请示内容。</span><span style=\"color:#000000;\">&nbsp;</span></span></span></span></strong> \n" +
                       "</p>\n" +
                       "<p>\n" +
                       "\t<strong><span style=\"font-size:18px;\"><span style=\"font-family:&quot;\"></span></span></strong> \n" +
                       "</p>\n" +
                       "<p class=\"MsoNormal\">\n" +
                       "\t<strong><span style=\"font-size:18px;\">妥否，请批示！</span></strong> \n" +
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
       item.setCompanyCheckMan(StringUtils.join(selectEmployeeService.getParentDeptChargeMen(59,1,false),","));//2020-12-22HNZ 公司办核稿默认2人竞争审批
       item.setCompanyCheckManName(selectEmployeeService.getNameByUserLogins(item.getCompanyCheckMan()));
       item.setDeleted(false);
       item.setProcessEnd(false);
       item.setCreator(userLogin);
       item.setGmtModified(new Date());
       item.setGmtCreate(new Date());
       item.setBelongThreeOne("");
       item.setFlag("不会签");
       item.setSign("");
       item.setTab(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"制度会签类型").toString());
       item.setSubmitTime(MyDateUtil.dateToStrLong(new Date()));
       ModelUtil.setNotNullFields(item);
       fiveOaSignQuoteMapper.insert(item);

       String businessKey= EdConst.FIVE_OA_SIGNQUOTE+ "_" + item.getId();
       Map variables = Maps.newHashMap();
       variables.put("userLogin", userLogin);
       variables.put("processDescription","签报："+item.getCreatorName());
       variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDeptId()));//部门领导
       variables.put("secretary",secretary);//机要秘书

       String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_SIGNQUOTE,businessKey, variables, MccConst.APP_CODE);
       item.setProcessInstanceId(processInstanceId);
       item.setBusinessKey(businessKey);
       //公司办编号
       //item.setCompanyNo(MyFormNo.getFormNo(taskHandleService.getDeploymentId(EdConst.FIVE_OA_SIGNQUOTE),item.getId()));
       fiveOaSignQuoteMapper.updateByPrimaryKey(item);
       return item.getId();
   }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
        params.put("isLikeSelect",true);

        //如果有数据权限判断数据权限  myDeptData true查看当前部门 false查看创建人
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
    //打印
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

        List<Map> functionList=Lists.newArrayList();//职能部门
        List<Map> designList=Lists.newArrayList();//设计部门
        List<ActHistoryDto> leaderList=Lists.newArrayList();


        for (ActHistoryDto dto:actHistoryDtos){
            if (dto.getActivityName()==null){
                break;
            }
            if ("公司办-核稿".equals(dto.getActivityName())){
                data.put("companyOffice",dto);
            }
            //2020-12-18HNZ 多个审查内容区分
            if ("法律/文本/流程审查".equals(dto.getActivityName())){
                if (dto.getUserLogin().contains(StringUtils.join(selectEmployeeService.getUserListByRoleName("流程审查"),","))){
                    data.put("reviewProcess",dto);//流程审查
                }
                if (dto.getUserLogin().contains(StringUtils.join(selectEmployeeService.getUserListByRoleName("文本审查"),","))){
                    data.put("reviewText",dto);//文本审查
                }
                if (dto.getUserLogin().contains(StringUtils.join(selectEmployeeService.getUserListByRoleName("法律审查"),","))){
                    data.put("legalReview",dto);//法鲁审查
                }

            }
            if (dto.getActivityName()!=null){
                if (dto.getActivityName().contains("领导")&&!dto.getActivityName().contains("机要秘书")){
                    leaderList.add(dto);
                }
            }
            if ("各单位部门-会签".equals(dto.getActivityName())){
                Map<String,Object> map=Maps.newHashMap();
                HrEmployeeSys hrEmployeeSys = hrEmployeeSysMapper.selectByUserLogin(dto.getUserLogin());
                HrDeptDto hrDeptDto = hrDeptService.getModelById(hrEmployeeSys.getDeptId());
                map.put("deptName",hrDeptDto.getName());
                map.put("actHistory",dto);

                if ("设计".equals(hrDeptDto.getDeptType())){
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


        List<FiveOaSignQuote> fiveOaSignQuotes=fiveOaSignQuoteMapper.selectAll(params);
        for (FiveOaSignQuote dto:fiveOaSignQuotes){
            Map map1=new LinkedHashMap();
            map1.put("事项",dto.getItem());
            map1.put("公司办编号",dto.getCompanyNo());
            map1.put("会签类型",dto.getFlag());
            map1.put("批示领导",dto.getInstructLeaderName());
            map1.put("阅办领导",dto.getCompanyLeaderName());
            map1.put("公司办核收",dto.getCompanyCheckManName() );
            map1.put("是否属于“三重一大”事项", dto.getBelongThreeOne());
            map1.put("送签部门",dto.getDeptName() );
            map1.put("部门负责人",dto.getDeptChargeManName() );
            map1.put("会议类型", dto.getMeetingType());
            map1.put("经办人",dto.getAgentName());
            map1.put("成文日期",dto.getSubmitTime());
            list.add(map1);
        }


        return list;
    }
}
