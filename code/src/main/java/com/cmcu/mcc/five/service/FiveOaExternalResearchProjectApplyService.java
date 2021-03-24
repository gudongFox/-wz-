package com.cmcu.mcc.five.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.service.CommonFileService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyDateUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.five.dao.FiveOaExternalResearchProjectApplyMapper;
import com.cmcu.mcc.five.dto.FiveOaExternalResearchProjectApplyDto;
import com.cmcu.mcc.five.entity.FiveOaExternalResearchProjectApply;
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
import java.util.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class FiveOaExternalResearchProjectApplyService extends BaseService {
    @Resource
    FiveOaExternalResearchProjectApplyMapper fiveOaExternalResearchProjectApplyMapper;
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
    @Autowired
    CommonFileService commonFileService;
    @Resource
    HandleFormService handleFormService;
    @Resource
    FiveOaResearchProjectLibraryService fiveOaResearchProjectLibraryService;


    public void remove(int id,String userLogin) {
        FiveOaExternalResearchProjectApply item = fiveOaExternalResearchProjectApplyMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin),"只允许创建人删除！");

        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    public void update(FiveOaExternalResearchProjectApply fiveOaExternalResearchProjectApplyDto){
        FiveOaExternalResearchProjectApply model = fiveOaExternalResearchProjectApplyMapper.selectByPrimaryKey(fiveOaExternalResearchProjectApplyDto.getId());

        model.setFormNo(fiveOaExternalResearchProjectApplyDto.getFormNo());
        model.setProjectNo(fiveOaExternalResearchProjectApplyDto.getProjectNo());
        model.setProjectName(fiveOaExternalResearchProjectApplyDto.getProjectName());
        model.setAchivement(fiveOaExternalResearchProjectApplyDto.getAchivement());
        model.setRemark(fiveOaExternalResearchProjectApplyDto.getRemark());
        model.setCompletionDate(fiveOaExternalResearchProjectApplyDto.getCompletionDate());
        model.setCommencementDate(fiveOaExternalResearchProjectApplyDto.getCommencementDate());
        model.setTaskCharger(fiveOaExternalResearchProjectApplyDto.getTaskCharger());
        model.setTaskChargerName(fiveOaExternalResearchProjectApplyDto.getTaskChargerName());
        model.setMainParticipant(fiveOaExternalResearchProjectApplyDto.getMainParticipant());
        model.setMainParticipantName(fiveOaExternalResearchProjectApplyDto.getMainParticipantName());
        model.setOutsidePayment(fiveOaExternalResearchProjectApplyDto.getOutsidePayment());
        model.setYearExceptPayment(fiveOaExternalResearchProjectApplyDto.getYearExceptPayment());
        model.setYearRepayment(fiveOaExternalResearchProjectApplyDto.getYearRepayment());
        model.setDeptName(fiveOaExternalResearchProjectApplyDto.getDeptName());
        model.setDeptId(fiveOaExternalResearchProjectApplyDto.getDeptId());
        model.setGmtModified(new Date());

        BeanValidator.validateObject(model);
        ModelUtil.setNotNullFields(model);
        Map variables = Maps.newHashMap();


        fiveOaExternalResearchProjectApplyMapper.updateByPrimaryKey(model);
        variables.put("deptScientificMen", selectEmployeeService.getDeptRoleUser(model.getDeptId(),"各单位科研人员"));
        //获取部门总工 如果不存在 则选择部门正副或签
        List<String> chiefEngineer = selectEmployeeService.listUserByPositionName("部门总工", model.getDeptId());

        if (chiefEngineer.size()>0){
            variables.put("chiefEngineer",chiefEngineer);//部门总工
        }else {
            variables.put("chiefEngineer",selectEmployeeService.getParentDeptChargeMen(model.getDeptId(),3,false));
        }

        variables.put("processDescription","外部科研项目申请表:"+model.getProjectName());
        BeanValidator.check(model);
        myActService.setVariables(model.getProcessInstanceId(),variables);

    }

    public FiveOaExternalResearchProjectApplyDto getModelById(int id){
        return getDto(fiveOaExternalResearchProjectApplyMapper.selectByPrimaryKey(id)) ;
    }

    public int getNewModel(String userLogin){
        FiveOaExternalResearchProjectApply item= new FiveOaExternalResearchProjectApply();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

        item.setCreator(hrEmployeeDto.getUserLogin());//初始化登录用户数据
        item.setCreatorName(hrEmployeeDto.getUserName());

        item.setTaskCharger(hrEmployeeDto.getUserLogin());
        item.setTaskChargerName(hrEmployeeDto.getUserName());

        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());

        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        ModelUtil.setNotNullFields(item);



        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "外部科研项目申请:"+item.getCreatorName());





        variables.put("scientificMen",selectEmployeeService.getUserListByRoleName("科研与技术质量部(科研岗)"));//取角色


        fiveOaExternalResearchProjectApplyMapper.insert(item);


        String BusinessKey= EdConst.FIVE_OA_EXTERNAL_RESEARCH_PROJECT_APPLY+ "_" + item.getId();//BusinessKey赋值，业务名称+编号
        item.setBusinessKey(BusinessKey);//给实体类传入BusinessKey数据

        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_EXTERNAL_RESEARCH_PROJECT_APPLY,item.getBusinessKey(), variables, MccConst.APP_CODE);
        //创建流程id，调用流程相关中的启动流程方法，传入业务名称、获取并传入item对应实体类的getBusinessKey、传入variables中的

        item.setProcessInstanceId(processInstanceId);//为实体类的流程id传入获取的值
        //流程相关
        fiveOaExternalResearchProjectApplyMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref, int pageNum, int pageSize) {
        params.put("deleted",false);
        params.put("isLikeSelect",true);

        /*//2020-12-28 当前日期+1天
        if (params.get("endTime").toString()!=""){
            params.put("endTime", MyDateUtil.getNextDay(params.get("endTime").toString(),"1"));
        }*/
        Map map=Maps.newHashMap();
        map.put("myDeptData",false);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));




        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaExternalResearchProjectApplyMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaExternalResearchProjectApply item=(FiveOaExternalResearchProjectApply)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public FiveOaExternalResearchProjectApplyDto getDto(FiveOaExternalResearchProjectApply item) {
        FiveOaExternalResearchProjectApplyDto dto=FiveOaExternalResearchProjectApplyDto.adapt(item);
        dto.setProcessName("已完成");

        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if(customProcessInstance ==null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaExternalResearchProjectApplyMapper.updateByPrimaryKey(dto);
                fiveOaResearchProjectLibraryService.genLibraryByResearchProject(dto);
            }
            if(customProcessInstance!=null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());

            }
        }

        return dto;
    }

    /**
     * 导出excl
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    public List<Map> listMapData(Map<String,Object> params,  String uiSref,String startTime ,String endTime,String userLogin){
        List<Map>list=new ArrayList<>();

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
        params.put("startTime",startTime);
        params.put("endTime",endTime);

        List<FiveOaExternalResearchProjectApply>fiveOaExternalResearchProjectApplies=fiveOaExternalResearchProjectApplyMapper.selectAll(params);
        for (FiveOaExternalResearchProjectApply dto:fiveOaExternalResearchProjectApplies) {

            Map map1 = new LinkedHashMap();
            map1.put("课题名称", dto.getProjectName());
            map1.put("申请单位", dto.getDeptName());
            map1.put("开工日期", dto.getCommencementDate());
            map1.put("完工日期", dto.getCompletionDate());
            map1.put("课题负责人", dto.getTaskChargerName());
            map1.put("主要参加人", dto.getMainParticipantName());
            map1.put("外拨款（万元）", dto.getOutsidePayment());
            map1.put("本年度预计拨付（万元）", dto.getYearExceptPayment());
            map1.put("本年度还款（万元）", dto.getYearRepayment());
            map1.put("成果形式及预期效益", dto.getAchivement());

            map1.put("备注", dto.getRemark());
            list.add(map1);
        }
//获取表头并加载表格第一列 表头设置


        return list;
    }

}
