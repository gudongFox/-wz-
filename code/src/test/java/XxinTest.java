
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.MyPoiUtil;

import com.cmcu.common.util.SmsTencentUtils;
import com.cmcu.mcc.business.dao.BusinessRecordMapper;
import com.cmcu.mcc.business.service.BusinessContractService;
import com.cmcu.mcc.business.service.BusinessInputContractService;
import com.cmcu.mcc.business.service.BusinessRecordService;
import com.cmcu.mcc.comm.exportDoc.CheckTextRenderPolicy;
import com.cmcu.mcc.comm.exportDoc.ExportUtils;
import com.cmcu.mcc.comm.exportDoc.FlowsTableRenderPolicy;
import com.cmcu.mcc.five.service.FiveOaWordSizeService;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dao.HrEmployeeSysMapper;
import com.cmcu.mcc.hr.dao.HrQualifyMapper;
import com.cmcu.mcc.hr.dto.HrDeptDto;
import com.cmcu.mcc.hr.entity.HrDept;
import com.cmcu.mcc.hr.entity.HrEmployee;
import com.cmcu.mcc.hr.entity.HrEmployeeSys;
import com.cmcu.mcc.hr.entity.HrQualify;
import com.cmcu.mcc.hr.service.*;
import com.cmcu.mcc.oa.service.OaNoticeService;

import com.cmcu.mcc.sys.service.SysRoleService;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.TextRenderData;
import com.deepoove.poi.data.style.Style;
import com.deepoove.poi.policy.DynamicTableRenderPolicy;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.xml.rpc.ServiceException;
import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;



/**
 * @ProjectName: mcc
 * @Package: PACKAGE_NAME
 * @ClassName: XxinTest
 * @Author: Administrator
 * @Description:
 * @Date: 2019/6/17 8:54
 * @Version: 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})
public class XxinTest {
    @Autowired
    BusinessContractService businessContractService;
    @Autowired
    HrEmployeeSysService hrEmployeeSysService;
    @Autowired
    HrEmployeeSysMapper hrEmployeeSysMapper;
    @Autowired
    HrDeptService hrDeptService;
    @Autowired
    HrPositionService hrPositionService;
    @Autowired
    HrEmployeeService hrEmployeeService;
    @Autowired
    BusinessInputContractService businessInputContractService;
    @Autowired
    SysRoleService sysRoleService;
    @Autowired
    OaNoticeService oaNoticeService;
    @Autowired
    ProcessEngine processEngine;
    @Autowired
    SelectEmployeeService selectEmployeeService;

    @Autowired
    FiveOaWordSizeService fiveOaWordSizeService;
    @Resource
    HrQualifyMapper hrQualifyMapper;
    @Autowired
    TaskService taskService;
    @Autowired
    BusinessRecordService businessRecordService;
    @Autowired
    BusinessRecordMapper businessRecordMapper;
    @Autowired
    HistoryService historyService;

    //添加抄送任务
    @Test
    public void addCopy(){
        //被抄送任务id
        String taskId="11069236";
        //抄送人
        List<String> users = new ArrayList<>();
       /* users.add("4048");//李佳珊
        users.add("2877");//黄涛
        users.add("4047");//陈琦*/
        users.add("4047");

        HistoricTaskInstanceQuery taskQuery = historyService.createHistoricTaskInstanceQuery()
                .includeProcessVariables().taskId(taskId);
        List<HistoricTaskInstance> oList = taskQuery.list();
        HistoricTaskInstance task = oList.get(0);
        for(String user:users){
            Task ccTask = taskService.newTask();
            ccTask.setDescription(task.getDescription());
            ccTask.setTenantId(task.getTenantId());
            ccTask.setOwner(user);
            ccTask.setAssignee(user);
            ccTask.setCategory(task.getCategory());
            ccTask.setDelegationState(DelegationState.RESOLVED);
            ccTask.setName("[抄送]" + task.getName());
            ccTask.setParentTaskId(task.getId());//父任务id
            taskService.saveTask(ccTask);
        }

    }

    @Test
    public void sengMss(){
        SmsTencentUtils.sendSms("电话号码","123456");
    }

}