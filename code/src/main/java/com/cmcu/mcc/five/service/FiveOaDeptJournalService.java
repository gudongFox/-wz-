package com.cmcu.mcc.five.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.five.dao.FiveOaDeptJournalMapper;
import com.cmcu.mcc.five.dto.FiveOaDeptJournalDto;
import com.cmcu.mcc.five.entity.FiveOaDeptJournal;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FiveOaDeptJournalService extends BaseService {
    @Resource
    FiveOaDeptJournalMapper fiveOaDeptJournalMapper;
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
        FiveOaDeptJournal item = fiveOaDeptJournalMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

        //流程作废
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

    }

    public void update(FiveOaDeptJournalDto fiveOaDeptJournalDto){
        FiveOaDeptJournal model = fiveOaDeptJournalMapper.selectByPrimaryKey(fiveOaDeptJournalDto.getId());
        model.setDeptId(fiveOaDeptJournalDto.getDeptId());
        model.setDeptName(fiveOaDeptJournalDto.getDeptName());
        model.setManuscriptTitle(fiveOaDeptJournalDto.getManuscriptTitle());
        model.setFirstAuthor(fiveOaDeptJournalDto.getFirstAuthor());
        model.setReader(fiveOaDeptJournalDto.getReader());
        model.setReaderName(fiveOaDeptJournalDto.getReaderName());
        model.setSubmitTime(fiveOaDeptJournalDto.getSubmitTime());
        model.setDeptOpinion(fiveOaDeptJournalDto.getDeptOpinion());
        model.setDeptSecrecyUser(fiveOaDeptJournalDto.getDeptSecrecyUser());
        model.setDeptSecrecyUserName(fiveOaDeptJournalDto.getDeptSecrecyUserName());
        model.setRecommendColumns(fiveOaDeptJournalDto.getRecommendColumns());
        model.setGmtModified(new Date());
        fiveOaDeptJournalMapper.updateByPrimaryKey(model);
        Map variables = Maps.newHashMap();
        variables.put("processDescription", "院刊审查："+model.getManuscriptTitle());
        variables.put("reader", model.getReader());
        variables.put("deptSecrecyUser", model.getDeptSecrecyUser());
        //variables.put("subeditor", MyStringUtil.getStringList("2646"));//主编
        //variables.put("primeEditor", MyStringUtil.getStringList("0061"));//副主编
        BeanValidator.check(model);
        myActService.setVariables(model.getProcessInstanceId(),variables);
    }
    public FiveOaDeptJournalDto getModelById(int id){

        return getDto(fiveOaDeptJournalMapper.selectByPrimaryKey(id));
    }

    public FiveOaDeptJournalDto getDto(FiveOaDeptJournal item) {
        FiveOaDeptJournalDto dto=FiveOaDeptJournalDto.adapt(item);
        dto.setProcessName("已完成");
        if (!dto.getProcessEnd()) {
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if (customProcessInstance == null || customProcessInstance.isFinished()) {
                dto.setProcessEnd(true);
                fiveOaDeptJournalMapper.updateByPrimaryKey(dto);
            }
            if (customProcessInstance != null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())) {
                dto.setProcessName(customProcessInstance.getCurrentTaskName());

            }
        }
        return dto;
    }

    public int getNewModel(String userLogin){

        List<String> secretary =  hrEmployeeService.selectUserByRoleNames("机要秘书");
        Assert.state(secretary.size()>0,"未找到职务为 机要秘书 人员");

        FiveOaDeptJournal item=new FiveOaDeptJournal();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());

        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());

        item.setDeptOpinion("政治法律方面：\n" +
                "有无政治性错误；\n" +
                "有无设计著作版权问题（抄袭、剽窃、虚假著名等）"
                +"论点是否正确（有无学术性、概念性错误，或含混、失实之处）；\n" +
                "技术是否先进（有无创新，创新的成分和程度如何）；\n" +
                "是否适合当前工作需要（可行性、实用性、普遍性如何）。"+
                "参 考 价 值:"+"理论意义如何；实用意义如何；有无广泛参考价值。\n" +
                "请根据以上理由，在下面审查意见表中写出您对文稿质量总体评价，以及具体应做补充、修改、完善，并建议：\n" +
                "1．刊用；2.修改后刊用；3.修改后重审；4.转他人审；5.不予刊用。"+
                "注：稿件要求详见当年征文通知");
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        ModelUtil.setNotNullFields(item);

        fiveOaDeptJournalMapper.insert(item);
        String businessKey= EdConst.FIVE_OA_DEPT_JOURNAL+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "院刊审查："+item.getCreatorName());
        //variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDeptId()));//发起者部门领导
        //variables.put("secretary",secretary);//公司领导批示

        item.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_DEPT_JOURNAL,item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        fiveOaDeptJournalMapper.updateByPrimaryKey(item);
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

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaDeptJournalMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaDeptJournal item=(FiveOaDeptJournal)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaDeptJournal item = fiveOaDeptJournalMapper.selectByPrimaryKey(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String number = sdf.format(new Date())+item.getCreator().substring((item.getCreator()).length() - 4);
        data.put("tableNo",number);
        data.put("manuscriptTitle",item.getManuscriptTitle());
        data.put("firstAuthor",item.getFirstAuthor());
        data.put("submitTime",item.getSubmitTime());
        data.put("deptSecrecyUserName",item.getDeptSecrecyUserName());
        data.put("readerName",item.getReaderName());
        data.put("deptOpinion",item.getDeptOpinion());
        data.put("recommendColumns",item.getRecommendColumns());

        data.put("creatorName",item.getCreatorName());
        data.put("gmtCreate",item.getGmtCreate());

        data.put("nodes",actService.listPassedHistoryDto(item.getProcessInstanceId()));

        return data;
    }
}
