package com.cmcu.mcc.five.service;


import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.dto.CommonCodeDto;
import com.cmcu.common.entity.CommonCode;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.dto.ActHistoryDto;
import com.cmcu.mcc.five.dao.FiveOaNewsexamineMapper;
import com.cmcu.mcc.five.dto.FiveOaNewsexamineDto;
import com.cmcu.mcc.five.entity.FiveOaNewsexamine;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FiveOaNewsExamineService extends BaseService {

    @Resource
    FiveOaNewsexamineMapper fiveOaNewsexamineMapper;
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
    ActService actService;
    @Resource
    HandleFormService handleFormService;


   public void remove(int id,String userLogin){
       FiveOaNewsexamine item = fiveOaNewsexamineMapper.selectByPrimaryKey(id);
       Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

       //流程作废
       handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

   }

   public void update(FiveOaNewsexamineDto item){

       FiveOaNewsexamine model = fiveOaNewsexamineMapper.selectByPrimaryKey(item.getId());
       model.setTitle(item.getTitle());
       model.setDeptId(item.getDeptId());
       model.setDeptName(item.getDeptName());
       model.setFormNo(item.getFormNo());
       model.setSendMan(item.getSendMan());
       model.setSendManName(item.getSendManName());
       model.setSendManLink(item.getSendManLink());
       model.setSendTime(item.getSendTime());
       model.setAuthor(item.getAuthor());
       model.setAuthorName(item.getAuthorName());
       model.setAuthorLink(item.getAuthorLink());
       model.setPublishingPlatform(item.getPublishingPlatform());
       model.setDeptChargeMan(item.getDeptChargeMan());
       model.setDeptChargeManName(item.getDeptChargeManName());
       model.setPartyChargeMan(item.getPartyChargeMan());
       model.setPartyChargeManName(item.getPartyChargeManName());
       model.setDeptExamineTips(item.getDeptExamineTips());
       model.setPartyComment(item.getPartyComment());

       model.setRemark(item.getRemark());

       model.setGmtModified(new Date());
       BeanValidator.validateObject(model);
       ModelUtil.setNotNullFields(model);
       Map variables = Maps.newHashMap();
      if (item.getDeptId()!=model.getId()){
          variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(model.getDeptId()));
          model.setDeptName(item.getDeptName());
          model.setDeptId(item.getDeptId());
      }
       fiveOaNewsexamineMapper.updateByPrimaryKey(model);
       variables.put("processDescription","新闻宣传及信息报送审查:"+item.getTitle());
       BeanValidator.check(model);
       myActService.setVariables(model.getProcessInstanceId(),variables);


   }


    public FiveOaNewsexamineDto getModelById(int id){

           return getDto(fiveOaNewsexamineMapper.selectByPrimaryKey(id));
    }

    public FiveOaNewsexamineDto getDto(FiveOaNewsexamine item) {
        FiveOaNewsexamineDto dto=FiveOaNewsexamineDto.adapt(item);
        dto.setProcessName("已完成");
        if (!dto.getProcessEnd()) {
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if(customProcessInstance ==null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaNewsexamineMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance!=null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }
        return dto;
    }

   public int getNewModel(String userLogin){
       FiveOaNewsexamine item=new FiveOaNewsexamine();
       HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

       item.setDeptId(hrEmployeeDto.getDeptId());
       item.setDeptName(hrEmployeeDto.getDeptName());
       item.setCreator(hrEmployeeDto.getUserLogin());
       item.setCreatorName(hrEmployeeDto.getUserName());
       item.setPublishingPlatform(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"建议发布平台").toString());
       List<CommonCodeDto> codes = commonCodeService.listDataByCatalog(MccConst.APP_CODE,"新闻宣传审查要点");
       String deptExamineTips ="";
       for(CommonCode code:codes){
           deptExamineTips=deptExamineTips+'*'+code.getCode()+'\n';
       }
       item.setDeptExamineTips(deptExamineTips);
       item.setDeleted(false);
       item.setProcessEnd(false);
       item.setCreator(userLogin);
       item.setGmtModified(new Date());
       item.setGmtCreate(new Date());
       ModelUtil.setNotNullFields(item);
       fiveOaNewsexamineMapper.insert(item);

       String businessKey= EdConst.FIVE_OA_NEWSEXAMINE+ "_" + item.getId();
       Map variables = Maps.newHashMap();
       variables.put("userLogin", userLogin);
       variables.put("processDescription","新闻宣传及信息报送审查:"+item.getCreatorName());
       variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDeptId()));
       variables.put("partyChargeMen",selectEmployeeService.getDeptChargeMen(72));//党群工作办负责人

       String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_NEWSEXAMINE,businessKey, variables, MccConst.APP_CODE);
       item.setBusinessKey(businessKey);
       item.setProcessInstanceId(processInstanceId);
       fiveOaNewsexamineMapper.updateByPrimaryKey(item);
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

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaNewsexamineMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaNewsexamine item=(FiveOaNewsexamine)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaNewsexamine item = fiveOaNewsexamineMapper.selectByPrimaryKey(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String number = sdf.format(new Date())+item.getCreator().substring((item.getCreator()).length() - 4);
        data.put("tableNo",number);
        data.put("deptName",item.getDeptName());
        data.put("sendTime",item.getSendTime());
        data.put("title",item.getTitle());
        data.put("authorName",item.getAuthorName());
        data.put("authorLink",item.getAuthorLink());
        data.put("sendManName",item.getSendManName());
        data.put("sendManLink",item.getSendManLink());
        data.put("publishingPlatform",item.getPublishingPlatform());
        data.put("deptExamineTips",item.getDeptExamineTips());
        data.put("partyComment",item.getPartyComment());
        data.put("deptChargeManName",item.getDeptChargeManName());


        data.put("creatorName",item.getCreatorName());
        data.put("gmtCreate",item.getGmtCreate());

        List<ActHistoryDto> actHistoryDtos = actService.listPassedHistoryDto(item.getProcessInstanceId());
        for (ActHistoryDto dto:actHistoryDtos){
            if (dto.getActivityName()==null){
                break;
            }
            if ("部门领导".equals(dto.getActivityName())){
                data.put("deptChargeMen",dto);
            }
            if ("党群工作部负责人".equals(dto.getActivityName())){
                data.put("partyChargeMen",dto);
            }
        }
        data.put("nodes",actHistoryDtos);

        return data;
    }

}
