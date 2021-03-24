package com.cmcu.mcc.five.service;


import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.dto.ActHistoryDto;
import com.cmcu.mcc.five.dao.FiveOaTechnologyArticleExamineMapper;
import com.cmcu.mcc.five.dto.FiveOaTechnologyArticleExamineDto;
import com.cmcu.mcc.five.entity.FiveOaTechnologyArticleExamine;
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
public class FiveOaTechnologyArticleExamineService extends BaseService {

    @Resource
    FiveOaTechnologyArticleExamineMapper fiveOaTechnologyArticleExamineMapper;
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
    ActService actService;
    @Resource
    HandleFormService handleFormService;

   public void remove(int id,String userLogin){
       FiveOaTechnologyArticleExamine item = fiveOaTechnologyArticleExamineMapper.selectByPrimaryKey(id);
       Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

       //流程作废
       handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

   }

   public void update(FiveOaTechnologyArticleExamineDto item){

       FiveOaTechnologyArticleExamine model = fiveOaTechnologyArticleExamineMapper.selectByPrimaryKey(item.getId());

       model.setFormNo(item.getFormNo());
       model.setTitle(item.getTitle());
       model.setDeptId(item.getDeptId());
       model.setDeptName(item.getDeptName());
       model.setSubmitTime(item.getSubmitTime());
       model.setAuthor(item.getAuthor());
       model.setAuthorName(item.getAuthorName());
       model.setAuthorLink(item.getAuthorLink());
       model.setPeriodical(item.getPeriodical());
       model.setExamineTips(item.getExamineTips());
       model.setDeptChargeMan(item.getDeptChargeMan());
       model.setDeptChargeManName(item.getDeptChargeManName());
       model.setDeptChargeManComment(item.getDeptChargeManComment());
       model.setTechnologyChargeMan(item.getTechnologyChargeMan());
       model.setTechnologyChargeManName(item.getTechnologyChargeManName());
       model.setTechnologyChargeManComment(item.getTechnologyChargeManComment());
       model.setRemark(item.getRemark());
       model.setGmtModified(new Date());

       BeanValidator.check(model);
       BeanValidator.validateObject(model);
       ModelUtil.setNotNullFields(model);

       Map variables = Maps.newHashMap();

       BeanValidator.check(model);
       fiveOaTechnologyArticleExamineMapper.updateByPrimaryKey(model);
       variables.put("processDescription","对外发表科技论文审查单："+item.getTitle());
       variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(model.getDeptId()));//部门领导
       myActService.setVariables(model.getProcessInstanceId(),variables);


   }


    public FiveOaTechnologyArticleExamineDto getModelById(int id){

           return getDto(fiveOaTechnologyArticleExamineMapper.selectByPrimaryKey(id));
    }

    public FiveOaTechnologyArticleExamineDto getDto(FiveOaTechnologyArticleExamine item) {
        FiveOaTechnologyArticleExamineDto dto=FiveOaTechnologyArticleExamineDto.adapt(item);
        dto.setProcessName("已完成");
        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");
            if(customProcessInstance==null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaTechnologyArticleExamineMapper.updateByPrimaryKey(dto);
            }else {
                dto.setProcessName(customProcessInstance.getCurrentTaskName());

            }
        }

        return dto;
    }

   public int getNewModel(String userLogin){
       FiveOaTechnologyArticleExamine item=new FiveOaTechnologyArticleExamine();
       HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

       item.setCreator(hrEmployeeDto.getUserLogin());
       item.setCreatorName(hrEmployeeDto.getUserName());
       item.setExamineTips("1.论文不涉及国际秘密和公司商业秘密;\n2.论文无政治性错误;\n3.论文无技术性、概念性错误，真实准确");
       item.setDeptId(hrEmployeeDto.getDeptId());
       item.setDeptName(hrEmployeeDto.getDeptName());
       item.setDeleted(false);
       item.setProcessEnd(false);
       item.setCreator(userLogin);
       item.setGmtModified(new Date());
       item.setGmtCreate(new Date());
       ModelUtil.setNotNullFields(item);
       fiveOaTechnologyArticleExamineMapper.insert(item);

       String businessKey= EdConst.FIVE_OA_TECHNOLOGYARTICLEEXAMINE+ "_" + item.getId();
       Map variables = Maps.newHashMap();
       variables.put("userLogin", userLogin);
       variables.put("processDescription","对外发表科技论文审查单："+item.getCreatorName());
       variables.put("deptChargeMen",selectEmployeeService.getChargeLeaderByDeptId(item.getDeptId()));//部门领导
       String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_TECHNOLOGYARTICLEEXAMINE,businessKey, variables, MccConst.APP_CODE);
       item.setBusinessKey(businessKey);
       item.setProcessInstanceId(processInstanceId);
       fiveOaTechnologyArticleExamineMapper.updateByPrimaryKey(item);
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

       /*
         List<Integer> deptIdList=hrEmployeeSysService.getMyDeptListOa(userLogin,uiSref);
         if (deptIdList.size()==0){
            params.put("creator",userLogin);
        }else {
            params.put("deptIdList",deptIdList);
        }*/

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaTechnologyArticleExamineMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaTechnologyArticleExamine item=(FiveOaTechnologyArticleExamine)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaTechnologyArticleExamine item = fiveOaTechnologyArticleExamineMapper.selectByPrimaryKey(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String number = sdf.format(new Date())+item.getCreator().substring((item.getCreator()).length() - 4);
        data.put("tableNo",number);
        data.put("title",item.getTitle());
        data.put("deptName",item.getDeptName());
        data.put("authorName",item.getAuthorName());
        data.put("authorLink",item.getAuthorLink());
        data.put("deptChargeManName",item.getDeptChargeManName());
        data.put("technologyChargeManName",item.getTechnologyChargeManName());
        data.put("periodical",item.getPeriodical());
        data.put("examineTips",item.getExamineTips());
        data.put("deptChargeManComment",item.getDeptChargeManComment());
        data.put("technologyChargeManComment",item.getTechnologyChargeManComment());

        data.put("creatorName",item.getCreatorName());
        data.put("gmtCreate",item.getGmtCreate());

        List<ActHistoryDto> actHistoryDtos = actService.listPassedHistoryDto(item.getProcessInstanceId());
        for (ActHistoryDto dto:actHistoryDtos){
            if (dto.getActivityName()==null){
                break;
            }
            if ("部门领导-审批".equals(dto.getActivityName())){
                data.put("deptChargeMen",dto);
            }
            if ("信息化建设与管理部-审批".equals(dto.getActivityName())){
                data.put("informationEquipmentMen",dto);
            }
        }
        data.put("nodes",actHistoryDtos);

        return data;
    }

}
