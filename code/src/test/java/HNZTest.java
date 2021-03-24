import com.cmcu.act.service.ProcessHandleService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.dao.CommonAttachMapper;
import com.cmcu.common.dao.CommonFileMapper;
import com.cmcu.common.dao.CommonFormDetailMapper;
import com.cmcu.common.entity.*;
import com.cmcu.common.service.*;
import com.cmcu.common.util.*;
import com.cmcu.mcc.business.dao.BusinessSubpackageMapper;
import com.cmcu.mcc.business.dao.FiveBusinessContractLibraryMapper;
import com.cmcu.mcc.business.entity.FiveBusinessContractLibrary;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.finance.dao.FiveFinanceStampTaxMapper;
import com.cmcu.mcc.finance.entity.FiveFinanceStampTax;
import com.cmcu.mcc.five.dao.FiveOaDispatchMapper;
import com.cmcu.mcc.five.dao.FiveOaInformationEquipmentExamineMapper;

import com.cmcu.mcc.five.service.FiveAssetComputerService;
import com.cmcu.mcc.five.service.FiveOaDecisionMakingService;
import com.cmcu.mcc.five.service.FiveOaInformationEquipmentExamineService;
import com.cmcu.mcc.five.service.FiveOaStampApplyOfficeService;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dao.HrEmployeeSysMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.entity.HrDept;
import com.cmcu.mcc.hr.entity.HrEmployeeSys;
import com.cmcu.mcc.hr.service.HrDeptService;
import com.cmcu.mcc.hr.service.HrEmployeeService;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.oa.dao.OaNoticeMapper;
import com.cmcu.mcc.oa.entity.OaNotice;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.impl.util.json.JSONObject;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})


public class HNZTest {
    @Resource
    HrDeptService hrDeptService;
    @Resource
    HrEmployeeSysMapper hrEmployeeSysMapper;
    @Resource
    HrEmployeeSysService hrEmployeeSysService;
    @Resource
    CommonFormDetailMapper commonFormDetailMapper;


    @Resource
    TaskService taskService;
    @Resource
    HrEmployeeService hrEmployeeService;


    /**
     * EXCL 导入人员信息
     * @throws IOException
     */
    @Test
    public void insertByFile() throws IOException {

        String path = "C:/Users/Administrator/Desktop/99.xls";
        List<Map> list = null;
        try {
            list = MyPoiUtil.listTableData(new FileInputStream(new File(path)), 1, 0, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int updateNum = 0;
        for (int i = 1; i <= list.size(); i++) {
            int deptId = 1;

            Map map = list.get(i - 1);
            String substring = map.get(3).toString().substring(map.get(3).toString().length() - 4);
            Map params = Maps.newHashMap();
            params.put("userLogin", substring);

            //部门
            if (StringUtils.isNotEmpty(map.get(2).toString())) {
                String name = map.get(2).toString();
                HrDept hrDept = hrDeptService.selectByName(name);
                if (hrDept != null) {
                    deptId = hrDept.getId();
                }
            }

            //判断系统是否已经录入改人员 跟新部门
            if (PageHelper.count(() -> hrEmployeeSysMapper.selectAll(params)) > 0) {
                HrEmployeeSys hrEmployeeSys = hrEmployeeSysMapper.selectAll(params).get(0);
                HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(hrEmployeeSys.getUserLogin());
                hrEmployeeDto.setUserNo(map.get(3).toString());

                hrEmployeeService.update(hrEmployeeDto);

                hrEmployeeSysService.updateDeptId(substring,deptId);
            }
            updateNum++;
        }
        System.out.println("新增数目："+updateNum);
    }

    /**
     * EXCL 导入人员企业微信ID 信息
     * @throws IOException
     */
    @Resource
    SelectEmployeeService selectEmployeeService;


    @Test
    public void insertByFile1() throws IOException {
        String path = "C:/Users/CMCUBIM/Desktop/99.xls";
        List<Map> list = null;
        try {
            list = MyPoiUtil.listTableData(new FileInputStream(new File(path)), 1, 0, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int updateNum = 0;
        for (int i = 1; i <= list.size(); i++) {
            Map map = list.get(i - 1);
           String name=map.get(0).toString();
            String userName="";
           if (name.length()>4){
               userName =name.substring(0,2);
               if (hrEmployeeSysService.selectModelByUserName(userName)!=null){
                   HrEmployeeDto hrEmployeeDto = hrEmployeeSysService.selectModelByUserName(userName);
                 if ( !StringUtils.isNotBlank(hrEmployeeDto.getWxId())){

                     HrEmployeeSys hrEmployeeSys = hrEmployeeSysMapper.selectByUserLogin(hrEmployeeDto.getUserLogin());
                     hrEmployeeSys.setWxId(map.get(1).toString());
                     hrEmployeeSysMapper.updateByPrimaryKey(hrEmployeeSys);
                     updateNum++;

                 }
               }else {
                   userName =  name.substring(0,3);
                   if (hrEmployeeSysService.selectModelByUserName(userName) != null) {
                       HrEmployeeDto hrEmployeeDto = hrEmployeeSysService.selectModelByUserName(userName);
                       if (!StringUtils.isNotBlank(hrEmployeeDto.getWxId())) {

                           HrEmployeeSys hrEmployeeSys = hrEmployeeSysMapper.selectByUserLogin(hrEmployeeDto.getUserLogin());
                           hrEmployeeSys.setWxId(map.get(1).toString());
                           hrEmployeeSysMapper.updateByPrimaryKey(hrEmployeeSys);
                           updateNum++;

                       }
                   }
               }
           }else {
               userName=name;
               if (hrEmployeeSysService.selectModelByUserName(userName) != null) {
                   HrEmployeeDto hrEmployeeDto = hrEmployeeSysService.selectModelByUserName(userName);
                   if (!StringUtils.isNotBlank(hrEmployeeDto.getWxId())) {
                       HrEmployeeSys hrEmployeeSys = hrEmployeeSysMapper.selectByUserLogin(hrEmployeeDto.getUserLogin());
                       hrEmployeeSys.setWxId(map.get(1).toString());
                       hrEmployeeSysMapper.updateByPrimaryKey(hrEmployeeSys);
                       updateNum++;

                   }
               }
           }
            updateNum++;
            }

        System.out.println("新增数目："+updateNum);
    }


    @Test
    public void test1(){
        Map map=Maps.newHashMap();
        map.put("deleted",false);
        map.put("inputCode","creator");
        List<CommonFormDetail> commonFormDetails = commonFormDetailMapper.selectAll(map);
        for (CommonFormDetail detail:commonFormDetails){
            detail.setListHidden(true);
            detail.setDetailHidden(true);
            commonFormDetailMapper.updateByPrimaryKey(detail);
        }

    }



    /** 切换流程处理人*/
    @Test
    public void test2(){
        taskService.setAssignee("10246819","2136");
    }

    /** 删除处理人*/
    @Test
    public void test22(){
        //7673130  7672773  7360893
        taskService.deleteTask("7360893","抄送任务错误数据删除 2863-luodong");
    }

    /** 添加流程变量*/
    @Test
    public void test222(){
        taskService.setVariable("10442518","financeMan","2955");
    }


    @Resource
    CommonAttachMapper commonAttachMapper;
    @Resource
    HrEmployeeMapper hrEmployeeMapper;


    @Resource
    FiveBusinessContractLibraryMapper fiveBusinessContractLibraryMapper;
    @Resource
    CommonCodeService commonCodeService;
    @Test
    public void test4(){
        String path = "C:/Users/CMCUBIM/Desktop/建研院合同信息.xls";
        List<Map> list = null;
        try {
            list = MyPoiUtil.listTableData(new FileInputStream(new File(path)), 1, 0, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int updateNum = 0;
        for (int i = 1; i <= list.size(); i++) {
            Map map = list.get(i - 1);
            FiveBusinessContractLibrary item=new FiveBusinessContractLibrary();

            //合同号
            item.setContractNo(map.get(0).toString());






            String contractType=map.get(1).toString();
            if ("其它,框架".contains(contractType)){
                item.setContractType("销售合同、纪要、协议以及其他");
                item.setProjectNature("其他");
            }else if (contractType.equals("工程设计")){
                item.setContractType("设计合同");
                item.setProjectNature("工程设计");
            }else if (contractType.equals("咨询服务")){
                item.setContractType("技术合同");
                item.setProjectNature("工程咨询");
            }

            item.setSignTime(map.get(2).toString());//签约日期
            item.setProjectName(map.get(3).toString());//合同名称
            item.setContractName(map.get(3).toString());
            item.setRecordProjectName(map.get(3).toString());//备案合同名称
            String address=map.getOrDefault(4,"").toString()+map.getOrDefault(5,"").toString()+map.getOrDefault(6,"").toString();
            item.setConstructionArea(address);//建设地址

            item.setCustomerName(map.get(7).toString());//客户名称
            item.setLinkMan(map.getOrDefault(8,"").toString());//联系人
            item.setLinkTel(map.getOrDefault(9,"").toString());
            item .setCustomerAddress(map.getOrDefault(10,"").toString());

            item.setContractMoney(map.get(11).toString());

            item.setDeptName(map.get(12).toString());
            item.setDeptId(hrDeptService.getModelByName(item.getDeptName()).getId());


            item.setCreator(hrEmployeeSysService.selectByUserName(map.get(13).toString()));
            item.setCreatorName(map.get(13).toString());
            item.setBusinessChargeLeader(item.getCreator());//副总
            item.setBusinessChargeLeaderName(item.getCreatorName());
            item.setProjectManager(item.getCreator());//项目经理
            item.setProjectManagerName(item.getCreatorName());


            item.setRemark(map.get(14).toString());
            item.setConstructionScale(map.get(15).toString());

            item.setReviewLevel("院级");

            item.setInOrOut("");
            item.setMilitaryMark("民品");
            item.setCivilMark("民用项目");
            item.setForeignTradeMark("国内项目");
            //item.setProjectMajorTypeFirst(commonCodeService.selectDefaultCodeValue("wuzhou","项目专业分类一级").toString());

            item.setInvestSource("民营企业");
            item.setCustomerId(0);
            item.setBid(false);
            item.setAttach(false);
            item.setDeleted(false);
           // item.setMain(false);//是否主合同
            item.setBackletter(false);//是否保函

            item.setGmtModified(new Date());
            item.setGmtCreate(new Date());
            ModelUtil.setNotNullFields(item);
            fiveBusinessContractLibraryMapper.insert(item);
            String businessKey= EdConst.FIVE_BUSINESSCONTRACTLIBRARY+ "_" + item.getId();
            item.setBusinessKey(businessKey);
            //补充合同
            String mainNo= item.getContractNo().split("-")[0];
            Map parve=Maps.newHashMap();
            parve.put("contractNo",mainNo);
            int size = fiveBusinessContractLibraryMapper.selectAll(parve).size();
            if (size>0){
                FiveBusinessContractLibrary main = fiveBusinessContractLibraryMapper.selectAll(parve).get(0);
                item.setMainContractLibraryNo(mainNo);
                item.setMainContractLibraryId(main.getId());
                item.setMainContractLibraryName(main.getContractName());
                item.setMain(true);
                main.setMainReviewIds(item.getId()+",");
                fiveBusinessContractLibraryMapper.updateByPrimaryKey(main);
            }else {
                item.setMain(false);
            }


            fiveBusinessContractLibraryMapper.updateByPrimaryKey(item);
        }
    }
    @Resource
    TaskHandleService taskHandleService;
    @Test
    public void test5(){
        String[] candidateUsers={"taskUser11|2766","taskUser11"};
     //  taskHandleService.backTask("7343567","系统打回",candidateUsers,"luodong",true);
    }
   /*环能院合同*/
    @Test
    public void test6(){
        String path = "C:/Users/CMCUBIM/Desktop/环能院合同.xls";
        List<Map> list = null;
        try {
            list = MyPoiUtil.listTableData(new FileInputStream(new File(path)), 1, 0, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int updateNum = 0;
        for (int i = 1; i <= list.size(); i++) {
            Map map = list.get(i - 1);
            FiveBusinessContractLibrary item=new FiveBusinessContractLibrary();

            //合同号
            item.setContractNo(map.get(4).toString());


            item.setContractType("设计合同");
            item.setProjectNature("工程设计");

          /*  String contractType=map.get(1).toString();
            if ("其它,框架".contains(contractType)){
                item.setContractType("销售合同、纪要、协议以及其他");
                item.setProjectNature("其他");
            }else if (contractType.equals("工程设计")){

            }else if (contractType.equals("咨询服务")){
                item.setContractType("技术合同");
                item.setProjectNature("工程咨询");
            }else {

            }*/

            item.setSignTime(map.get(2).toString());//签约日期
            item.setProjectName(map.get(0).toString());//合同名称
            item.setContractName(map.get(0).toString());
            item.setRecordProjectName(map.get(0).toString());//备案合同名称
            //String address=map.getOrDefault(4,"").toString()+map.getOrDefault(5,"").toString()+map.getOrDefault(6,"").toString();
            item.setConstructionArea("");//建设地址

            item.setCustomerName(map.get(1).toString());//客户名称
          /*  item.setLinkMan(map.getOrDefault(8,"").toString());//联系人
            item.setLinkTel(map.getOrDefault(9,"").toString());
            item .setCustomerAddress(map.getOrDefault(10,"").toString());*/

            item.setContractMoney(map.get(5).toString());

            item.setDeptName("环境与能源设计研究院");
            item.setDeptId(76);


            item.setCreator(hrEmployeeSysService.selectByUserName(map.get(3).toString()));
            item.setCreatorName(map.get(3).toString());
            /*item.setBusinessChargeLeader(item.getCreator());//副总
            item.setBusinessChargeLeaderName(item.getCreatorName());*/
            item.setProjectManager(item.getCreator());//项目经理
            item.setProjectManagerName(item.getCreatorName());


            item.setRemark("");
            item.setConstructionScale("");

            item.setReviewLevel("院级");

            item.setInOrOut("");
            item.setMilitaryMark("民品");
            item.setCivilMark("民用项目");
            item.setForeignTradeMark("国内项目");
            //item.setProjectMajorTypeFirst(commonCodeService.selectDefaultCodeValue("wuzhou","项目专业分类一级").toString());

            item.setInvestSource("民营企业");
            item.setCustomerId(0);
            item.setBid(false);
            item.setAttach(false);
            item.setDeleted(false);
            // item.setMain(false);//是否主合同
            item.setBackletter(false);//是否保函
            item.setMain(false);

            item.setGmtModified(new Date());
            item.setGmtCreate(new Date());
            ModelUtil.setNotNullFields(item);
            fiveBusinessContractLibraryMapper.insert(item);
            String businessKey= EdConst.FIVE_BUSINESSCONTRACTLIBRARY+ "_" + item.getId();
            item.setBusinessKey(businessKey);
          /*  //补充合同
            String mainNo= item.getContractNo().split("-")[0];
            Map parve=Maps.newHashMap();
            parve.put("contractNo",mainNo);
            int size = fiveBusinessContractLibraryMapper.selectAll(parve).size();
            if (size>0){
                FiveBusinessContractLibrary main = fiveBusinessContractLibraryMapper.selectAll(parve).get(0);
                item.setMainContractLibraryNo(mainNo);
                item.setMainContractLibraryId(main.getId());
                item.setMainContractLibraryName(main.getContractName());
                item.setMain(true);
                main.setMainReviewIds(item.getId()+",");
                fiveBusinessContractLibraryMapper.updateByPrimaryKey(main);
            }else {
                item.setMain(false);
            }*/


            fiveBusinessContractLibraryMapper.updateByPrimaryKey(item);
        }
    }

    /*一院合同*/
    @Test
    public void test7(){
        String path = "C:/Users/CMCUBIM/Desktop/一院.xls";
        List<Map> list = null;
        try {
            list = MyPoiUtil.listTableData(new FileInputStream(new File(path)), 1, 0, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int updateNum = 0;
        for (int i = 1; i <= list.size(); i++) {
            Map map = list.get(i - 1);
            FiveBusinessContractLibrary item=new FiveBusinessContractLibrary();

            //合同号
            item.setContractNo(map.get(5).toString());


            item.setContractType("设计合同");
            item.setProjectNature("工程设计");

          /*  String contractType=map.get(1).toString();
            if ("其它,框架".contains(contractType)){
                item.setContractType("销售合同、纪要、协议以及其他");
                item.setProjectNature("其他");
            }else if (contractType.equals("工程设计")){

            }else if (contractType.equals("咨询服务")){
                item.setContractType("技术合同");
                item.setProjectNature("工程咨询");
            }else {

            }*/

            item.setSignTime(map.get(6).toString());//签约日期
            item.setProjectName(map.get(2).toString());//合同名称
            item.setContractName(map.get(2).toString());
            item.setRecordProjectName(map.get(2).toString());//备案合同名称
            //String address=map.getOrDefault(4,"").toString()+map.getOrDefault(5,"").toString()+map.getOrDefault(6,"").toString();
            item.setConstructionArea("");//建设地址

            item.setCustomerName(map.get(1).toString());//客户名称
          /*  item.setLinkMan(map.getOrDefault(8,"").toString());//联系人
            item.setLinkTel(map.getOrDefault(9,"").toString());
            item .setCustomerAddress(map.getOrDefault(10,"").toString());*/

            item.setContractMoney(map.get(7).toString());

            item.setDeptName("第一设计研究院");
            item.setDeptId(75);


            item.setCreator(hrEmployeeSysService.selectByUserName(map.get(3).toString()));
            item.setCreatorName(map.get(3).toString());
            /*item.setBusinessChargeLeader(item.getCreator());//副总
            item.setBusinessChargeLeaderName(item.getCreatorName());*/
          /*  item.setProjectManager(item.getCreator());//项目经理
            item.setProjectManagerName(item.getCreatorName());*/
            item.setTotalDesigner(item.getCreator());//总师
            item.setTotalDesignerName(item.getCreatorName());


            item.setRemark("代号:"+map.get(0).toString());
            item.setConstructionScale("");

            item.setReviewLevel("院级");

            item.setInOrOut("");
            if (map.get(3).toString().equals("jp")){
                item.setMilitaryMark("民品");
                item.setCivilMark("民用项目");
                item.setInvestSource("政府");
            }else {
                item.setMilitaryMark("军品");
                item.setInvestSource("民营企业");
            }

            item.setCivilMark("民用项目");
            item.setForeignTradeMark("国内项目");
            //item.setProjectMajorTypeFirst(commonCodeService.selectDefaultCodeValue("wuzhou","项目专业分类一级").toString());


            item.setCustomerId(0);
            item.setBid(false);
            item.setAttach(false);
            item.setDeleted(false);
            // item.setMain(false);//是否主合同
            item.setBackletter(false);//是否保函
            item.setMain(false);

            item.setGmtModified(new Date());
            item.setGmtCreate(new Date());
            ModelUtil.setNotNullFields(item);
            fiveBusinessContractLibraryMapper.insert(item);
            String businessKey= EdConst.FIVE_BUSINESSCONTRACTLIBRARY+ "_" + item.getId();
            item.setBusinessKey(businessKey);
          /*  //补充合同
            String mainNo= item.getContractNo().split("-")[0];
            Map parve=Maps.newHashMap();
            parve.put("contractNo",mainNo);
            int size = fiveBusinessContractLibraryMapper.selectAll(parve).size();
            if (size>0){
                FiveBusinessContractLibrary main = fiveBusinessContractLibraryMapper.selectAll(parve).get(0);
                item.setMainContractLibraryNo(mainNo);
                item.setMainContractLibraryId(main.getId());
                item.setMainContractLibraryName(main.getContractName());
                item.setMain(true);
                main.setMainReviewIds(item.getId()+",");
                fiveBusinessContractLibraryMapper.updateByPrimaryKey(main);
            }else {
                item.setMain(false);
            }*/


            fiveBusinessContractLibraryMapper.updateByPrimaryKey(item);
        }
    }


    @Resource
    HistoryService historyService;
    @Test
    public void test9(){
        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery().involvedUser("2813").variableValueNotEquals("initiator","2813");//.variableValueEqualsIgnoreCase("initiator","2813")
        List<HistoricProcessInstance> list1 = historicProcessInstanceQuery.list();

        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery().taskInvolvedUser("2813").taskAssignee("2813");//taskVariableValueEquals("initiator", "2813")
        List<HistoricTaskInstance> list = historicTaskInstanceQuery.list();
        List<HistoricTaskInstance> addList = Lists.newArrayList();
        for (HistoricTaskInstance his:list){
            if ("发起人,发起者".contains(his.getName())){
                addList.add(his);
            }
            Map<String, Object> processVariables = his.getProcessVariables();

            System.out.println(2);//7355343 7360074
        }
        System.out.println(1);
    }

    @Test
    public void test10(){
        Map variables = Maps.newHashMap();
        variables.put("deleted",false);
        List<HrEmployeeDto> hrEmployeeDtos = hrEmployeeMapper.selectAll(variables);
        for (HrEmployeeDto hr :hrEmployeeDtos){
           if ("".equals(hr.getLogoGram())){
               hr.setLogoGram(HanyupinyinUtil.getFirstSpell(hr.getUserName()).toUpperCase());
               hrEmployeeMapper.updateByPrimaryKey(hr);
           }
        }
    }


    /*五源合同*/
    @Test
    public void test11(){
        String path = "C:/Users/CMCUBIM/Desktop/五源.xls";
        List<Map> list = null;
        try {
            list = MyPoiUtil.listTableData(new FileInputStream(new File(path)), 1, 0, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int updateNum = 0;
        for (int i = 1; i <= list.size(); i++) {
            Map map = list.get(i - 1);
            FiveBusinessContractLibrary item=new FiveBusinessContractLibrary();

            //合同号
            //item.setContractNo(new BigDecimal(map.get(0).toString()).toPlainString());
            item.setContractNo(map.get(0).toString());

            item.setContractType("设计合同");
            item.setProjectNature("工程设计");

          /*  String contractType=map.get(1).toString();
            if ("其它,框架".contains(contractType)){
                item.setContractType("销售合同、纪要、协议以及其他");
                item.setProjectNature("其他");
            }else if (contractType.equals("工程设计")){

            }else if (contractType.equals("咨询服务")){
                item.setContractType("技术合同");
                item.setProjectNature("工程咨询");
            }else {

            }*/
          //  String[] split = map.get(5).toString().split("\\.");

           // item.setSignTime("2020-"+split[1]+"-"+split[2]);//签约日期
            item.setProjectName(map.get(2).toString());//合同名称
            item.setContractName(map.get(2).toString());
            item.setRecordProjectName(map.get(2).toString());//备案合同名称
            //String address=map.getOrDefault(4,"").toString()+map.getOrDefault(5,"").toString()+map.getOrDefault(6,"").toString();
            item.setConstructionArea("");//建设地址

            item.setCustomerName(map.get(1).toString());//客户名称
          /*  item.setLinkMan(map.getOrDefault(8,"").toString());//联系人
            item.setLinkTel(map.getOrDefault(9,"").toString());
            item .setCustomerAddress(map.getOrDefault(10,"").toString());*/

            item.setContractMoney(map.get(3).toString());

            item.setDeptName("五源现代项目管理中心");
            item.setDeptId(36);


            item.setCreator("1683");
            item.setCreatorName("刘亚军");
            /*;*/
          /*  item.setProjectManager(item.getCreator());//项目经理
            item.setProjectManagerName(item.getCreatorName());*/
            item.setTotalDesigner(item.getCreator());//总师
            item.setTotalDesignerName(item.getCreatorName());

            //item.setBusinessChargeLeader(hrEmployeeSysService.selectByUserName(map.get(8).toString()));//副总
            //item.setBusinessChargeLeaderName(map.get(8).toString());

            item.setRemark("");
            item.setConstructionScale("");

            item.setReviewLevel("院级");

            item.setInOrOut("");


            item.setMilitaryMark("民品");
            item.setInvestSource("民营企业");

            item.setCivilMark("民用项目");
            item.setForeignTradeMark("国内项目");
            //item.setProjectMajorTypeFirst(commonCodeService.selectDefaultCodeValue("wuzhou","项目专业分类一级").toString());


            item.setCustomerId(0);
            item.setBid(false);
            item.setAttach(false);
            item.setDeleted(false);
            // item.setMain(false);//是否主合同
            item.setBackletter(false);//是否保函
            item.setMain(false);
            item.setGmtModified(new Date());
            item.setGmtCreate(new Date());
            ModelUtil.setNotNullFields(item);
            fiveBusinessContractLibraryMapper.insert(item);
            String businessKey= EdConst.FIVE_BUSINESSCONTRACTLIBRARY+ "_" + item.getId();
            item.setBusinessKey(businessKey);
            fiveBusinessContractLibraryMapper.updateByPrimaryKey(item);
        }
    }
    /*环能合同*/
    @Test
    public void test12(){
        String path = "C:/Users/CMCUBIM/Desktop/环能.xls";
        List<Map> list = null;
        try {
            list = MyPoiUtil.listTableData(new FileInputStream(new File(path)), 1, 0, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int updateNum = 0;
        for (int i = 1; i <= list.size(); i++) {
            Map map = list.get(i - 1);
            FiveBusinessContractLibrary item=new FiveBusinessContractLibrary();

            //合同号
            item.setContractNo(new BigDecimal(map.get(4).toString()).toPlainString());
            // item.setContractNo(map.get(0).toString());

            item.setContractType("设计合同");
            item.setProjectNature("工程咨询");

          /*  String contractType=map.get(1).toString();
            if ("其它,框架".contains(contractType)){
                item.setContractType("销售合同、纪要、协议以及其他");
                item.setProjectNature("其他");
            }else if (contractType.equals("工程设计")){

            }else if (contractType.equals("咨询服务")){
                item.setContractType("技术合同");
                item.setProjectNature("工程咨询");
            }else {

            }*/
            String[] split = map.get(2).toString().split("\\.");
            if(split.length>3){
                item.setSignTime(split[0]+"-"+split[1]+"-"+split[2]);//签约日期
            }else {
                item.setSignTime(split[0]+"_"+split[1]);//签约日期
            }

            item.setProjectName(map.get(0).toString());//合同名称
            item.setContractName(map.get(0).toString());
            item.setRecordProjectName(map.get(0).toString());//备案合同名称
            //String address=map.getOrDefault(4,"").toString()+map.getOrDefault(5,"").toString()+map.getOrDefault(6,"").toString();
            item.setConstructionArea("");//建设地址

            item.setCustomerName(map.get(1).toString());//客户名称
          /*  item.setLinkMan(map.getOrDefault(8,"").toString());//联系人
            item.setLinkTel(map.getOrDefault(9,"").toString());
            item .setCustomerAddress(map.getOrDefault(10,"").toString());*/

            item.setContractMoney(map.get(5).toString());

            item.setDeptName("环境与能源设计研究院");
            item.setDeptId(76);


            item.setCreator(hrEmployeeSysService.selectByUserName(map.get(3).toString()));
            item.setCreatorName(map.get(3).toString());
            /*;*/
          /*  item.setProjectManager(item.getCreator());//项目经理
            item.setProjectManagerName(item.getCreatorName());*/
            item.setTotalDesigner(item.getCreator());//总师
            item.setTotalDesignerName(item.getCreatorName());


            item.setRemark("");
            item.setConstructionScale("");

            item.setReviewLevel("院级");

            item.setInOrOut("");

            item.setMilitaryMark("民品");
            item.setInvestSource("民营企业");
            item.setCivilMark("民用项目");
            item.setForeignTradeMark("国内项目");
            //item.setProjectMajorTypeFirst(commonCodeService.selectDefaultCodeValue("wuzhou","项目专业分类一级").toString());


            item.setCustomerId(0);
            item.setBid(false);
            item.setAttach(false);
            item.setDeleted(false);
            // item.setMain(false);//是否主合同
            item.setBackletter(false);//是否保函
            item.setMain(false);

            item.setGmtModified(new Date());
            item.setGmtCreate(new Date());
            ModelUtil.setNotNullFields(item);
            fiveBusinessContractLibraryMapper.insert(item);
            String businessKey= EdConst.FIVE_BUSINESSCONTRACTLIBRARY+ "_" + item.getId();
            item.setBusinessKey(businessKey);
          /*  //补充合同
            String mainNo= item.getContractNo().split("-")[0];
            Map parve=Maps.newHashMap();
            parve.put("contractNo",mainNo);
            int size = fiveBusinessContractLibraryMapper.selectAll(parve).size();
            if (size>0){
                FiveBusinessContractLibrary main = fiveBusinessContractLibraryMapper.selectAll(parve).get(0);
                item.setMainContractLibraryNo(mainNo);
                item.setMainContractLibraryId(main.getId());
                item.setMainContractLibraryName(main.getContractName());
                item.setMain(true);
                main.setMainReviewIds(item.getId()+",");
                fiveBusinessContractLibraryMapper.updateByPrimaryKey(main);
            }else {
                item.setMain(false);
            }*/


            fiveBusinessContractLibraryMapper.updateByPrimaryKey(item);
        }
    }

    /*石化院*/
    @Test
    public void test13(){
        String path = "C:/Users/CMCUBIM/Desktop/石化院.xls";
        List<Map> list = null;
        try {
            list = MyPoiUtil.listTableData(new FileInputStream(new File(path)), 1, 0, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int updateNum = 0;
        for (int i = 1; i <= list.size(); i++) {
            Map map = list.get(i - 1);
            FiveBusinessContractLibrary item=new FiveBusinessContractLibrary();

            //合同号
            item.setContractNo(new BigDecimal(map.get(0).toString()).toPlainString());
            // item.setContractNo(map.get(0).toString());

            item.setContractType("技术合同");
            item.setProjectNature("工程承包");

          /*  String contractType=map.get(1).toString();
            if ("其它,框架".contains(contractType)){
                item.setContractType("销售合同、纪要、协议以及其他");
                item.setProjectNature("其他");
            }else if (contractType.equals("工程设计")){

            }else if (contractType.equals("咨询服务")){
                item.setContractType("技术合同");
                item.setProjectNature("工程咨询");
            }else {

            }*/
           /* String[] split = map.get(2).toString().split("\\.");
            if(split.length>3){
                item.setSignTime("2020-"+split[1]+"-"+split[2]);//签约日期
            }else {
                item.setSignTime("2020-"+split[1]);//签约日期
            }*/

            item.setProjectName(map.get(1).toString());//合同名称
            item.setContractName(map.get(1).toString());
            item.setRecordProjectName(map.get(1).toString());//备案合同名称
            //String address=map.getOrDefault(4,"").toString()+map.getOrDefault(5,"").toString()+map.getOrDefault(6,"").toString();
            item.setConstructionArea("");//建设地址

            item.setCustomerName(map.get(3).toString());//客户名称
          /*  item.setLinkMan(map.getOrDefault(8,"").toString());//联系人
            item.setLinkTel(map.getOrDefault(9,"").toString());
            item .setCustomerAddress(map.getOrDefault(10,"").toString());*/

            item.setContractMoney(map.get(6).toString());

            item.setDeptName("石油化工设计研究院");
            item.setDeptId(53);


            item.setCreator("2898");
            item.setCreatorName("何逸");
            /*;*/
          /*  item.setProjectManager(item.getCreator());//项目经理
            item.setProjectManagerName(item.getCreatorName());*/
           /* item.setTotalDesigner(item.getCreator());//总师
            item.setTotalDesignerName(item.getCreatorName());*/


            item.setRemark("");
            item.setConstructionScale("");

            item.setReviewLevel("院级");

            item.setInOrOut("");

            item.setMilitaryMark("民品");
            item.setInvestSource("民营企业");
            item.setCivilMark("民用项目");
            item.setForeignTradeMark("国内项目");
            //item.setProjectMajorTypeFirst(commonCodeService.selectDefaultCodeValue("wuzhou","项目专业分类一级").toString());


            item.setCustomerId(0);
            item.setBid(false);
            item.setAttach(false);
            item.setDeleted(false);
            // item.setMain(false);//是否主合同
            item.setBackletter(false);//是否保函
            item.setMain(false);

            item.setGmtModified(new Date());
            item.setGmtCreate(new Date());
            ModelUtil.setNotNullFields(item);
            fiveBusinessContractLibraryMapper.insert(item);
            String businessKey= EdConst.FIVE_BUSINESSCONTRACTLIBRARY+ "_" + item.getId();
            item.setBusinessKey(businessKey);
          /*  //补充合同
            String mainNo= item.getContractNo().split("-")[0];
            Map parve=Maps.newHashMap();
            parve.put("contractNo",mainNo);
            int size = fiveBusinessContractLibraryMapper.selectAll(parve).size();
            if (size>0){
                FiveBusinessContractLibrary main = fiveBusinessContractLibraryMapper.selectAll(parve).get(0);
                item.setMainContractLibraryNo(mainNo);
                item.setMainContractLibraryId(main.getId());
                item.setMainContractLibraryName(main.getContractName());
                item.setMain(true);
                main.setMainReviewIds(item.getId()+",");
                fiveBusinessContractLibraryMapper.updateByPrimaryKey(main);
            }else {
                item.setMain(false);
            }*/


            fiveBusinessContractLibraryMapper.updateByPrimaryKey(item);
        }
    }
     /*流程换人*/
    @Test
    public void test14(){

        String exchangeLogin1="2399";
        String exchangeLogin2="2192";
        String tempChangeLogin="justtemp";
        List<Task> list = taskService.createTaskQuery().taskAssignee("2192").list();
        List<Task> list1 = taskService.createTaskQuery().taskAssignee("justtemp").list();
        List<Task> list2 = taskService.createTaskQuery().taskAssignee("2399").list();
        //先把luodond的设置给特殊的temp
    /*    for(Task task:taskService.createTaskQuery().taskAssignee(exchangeLogin1).list()){
            taskService.setAssignee(task.getId(),tempChangeLogin);
        }*/
        //把hnz的设置给luodong
      /*  for(Task task:taskService.createTaskQuery().taskAssignee(exchangeLogin2).list()){
            taskService.setAssignee(task.getId(),exchangeLogin1);
        }*/
        //把temp的设置给hnz
        for(Task task:taskService.createTaskQuery().taskAssignee(tempChangeLogin).list()){
            taskService.setAssignee(task.getId(),exchangeLogin2);
        }
    }

    /*二院合同*/
    @Test
    public void test8(){
        String path = "C:/Users/CMCUBIM/Desktop/19.xls";
        List<Map> list = null;
        try {
            list = MyPoiUtil.listTableData(new FileInputStream(new File(path)), 1, 0, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int updateNum = 0;
        int insertNum=0;
        String contractNoA="";
        for (int i = 1; i <= list.size(); i++) {
            try{
                Map map = list.get(i - 1);
                FiveBusinessContractLibrary item=new FiveBusinessContractLibrary();

                //合同号
                String contractNo= map.get(2).toString();
                if (contractNo.contains("-")||contractNo.contains("F")){
                    item.setContractNo(contractNo);
                }else {
                    item.setContractNo(new BigDecimal(contractNo).toPlainString());

                }
                Map map1=new HashMap();
                map1.put("deleted",false);
                map1.put("contractNo",item.getContractNo());
                Boolean flag=true;
                if (fiveBusinessContractLibraryMapper.selectAll(map1).size()>0){
                    item=fiveBusinessContractLibraryMapper.selectAll(map1).get(0);
                    flag=false;
                }

                item.setContractType("设计合同");
                item.setProjectNature("工程设计");

                String[] split = map.get(4).toString().split("\\.");
                if (Integer.parseInt(split[0])>90){
                    item.setSignTime("19"+split[0]+"-"+split[1]+"-"+split[2]);//签约日期
                }else {
                    item.setSignTime("20"+split[0]+"-"+split[1]+"-"+split[2]);//签约日期
                }

                item.setProjectName(map.get(1).toString());//合同名称
                item.setContractName(map.get(1).toString());
                item.setRecordProjectName(map.get(1).toString());//备案合同名称
                item.setProjectNo("");//项目号
                //item.setConstructionArea(map.getOrDefault(12,"").toString());//建设地址

                item.setCustomerName(map.get(5).toString());//客户名称
                item.setLinkMan("");//联系人
                item.setLinkTel("");
                item .setCustomerAddress("");

                item.setContractMoney(map.get(6).toString());
                String deptName=map.get(3).toString();
                HrDept hrDept = hrDeptService.selectByName(deptName);
                if (hrDept!=null){
                    item.setDeptName(hrDept.getName());
                    item.setDeptId(hrDept.getId());
                }else {
                    System.out.println("新增合同数目："+insertNum+"修改合同数目："+updateNum);
                    System.out.println(deptName+"未匹配到对应数据: "+ item.getContractNo());
                    return;
                }
                    //默认	2623
                if (map.get(7).toString().equals("")){
                    item.setCreator("2623");
                    item.setCreatorName("封金城");
                }else {
                    if (hrEmployeeSysService.selectByUserName(map.get(7).toString()).equals("")){
                        item.setCreator("2623");
                        item.setCreatorName("封金城");
                    }else {
                        item.setCreator(hrEmployeeSysService.selectByUserName(map.get(7).toString()));
                        item.setCreatorName(map.get(7).toString());
                    }
                }
                //item.setTotalDesigner(hrEmployeeSysService.selectByUserName(map.get(16).toString()));//总师
                //item.setTotalDesignerName(hrEmployeeSysService.selectByUserName(map.get(16).toString()));
               /* item.setRemark(map.get(17).toString());*/
                item.setConstructionScale("");

                item.setReviewLevel("院级");

                item.setInOrOut("");
            /*    if (map.get(5).toString().equals("JP")){
                    item.setMilitaryMark("军品");
                    item.setCivilMark("民用项目");
                    item.setInvestSource("政府");
                }else if (map.get(5).toString().equals("CL,MB,NJ")){
                    item.setMilitaryMark("民品");
                    item.setInvestSource("民营企业");
                }else {
                    item.setMilitaryMark("其他");
                    item.setInvestSource("民营企业");
                }*/
                item.setMilitaryMark("其他");
                item.setInvestSource("民营企业");
                item.setForeignTradeMark("国内项目");
                //item.setProjectMajorTypeFirst(commonCodeService.selectDefaultCodeValue("wuzhou","项目专业分类一级").toString());


                item.setCustomerId(0);
                item.setBid(false);
                item.setAttach(false);
                item.setDeleted(false);
                // item.setMain(false);//是否主合同
                item.setBackletter(false);//是否保函
                item.setMain(false);

                item.setGmtModified(new Date());
                item.setGmtCreate(new Date());
                item.setRemark("系统导入");
                ModelUtil.setNotNullFields(item);
                if (flag){
                    fiveBusinessContractLibraryMapper.insert(item);
                    String businessKey= EdConst.FIVE_BUSINESSCONTRACTLIBRARY+ "_" + item.getId();
                    item.setBusinessKey(businessKey);
                    fiveBusinessContractLibraryMapper.updateByPrimaryKey(item);
                    insertNum++;
                }else {
                    fiveBusinessContractLibraryMapper.updateByPrimaryKey(item);
                    updateNum++;
                }
                contractNoA=item.getContractNo();
            }catch (Exception e){
                System.out.println(e.getMessage());
                System.out.println(contractNoA+":序号"+updateNum);

            }
            System.out.println("新增合同数目："+insertNum+"修改合同数目："+updateNum);
        }
    }
    @Resource
    FiveFinanceStampTaxMapper fiveFinanceStampTaxMapper;
    @Test
    public void test15(){
        Map map=Maps.newHashMap();
        map.put("deleted",false);
        List<FiveFinanceStampTax> fiveFinanceStampTaxes = fiveFinanceStampTaxMapper.selectAll(map);
        for (FiveFinanceStampTax fax:fiveFinanceStampTaxes){
            fax.setStampTaxMoney(MyStringUtil.getNewMultiplyMoney1(fax.getContractMoney(),fax.getTaxNum()));
            fiveFinanceStampTaxMapper.updateByPrimaryKey(fax);
        }
    }

     @Resource
    BusinessSubpackageMapper businessSubpackageMapper;
    /*分包合同录入*/
   // @Test
 /*   public void test16(){
        String path = "C:/Users/CMCUBIM/Desktop/11.xls";
        List<Map> list = null;
        try {
            list = MyPoiUtil.listTableData(new FileInputStream(new File(path)), 1, 0, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int updateNum = 0;
        for (int i = 1; i <= list.size(); i++) {
            Map map = list.get(i - 1);
            BusinessSubpackage item=new BusinessSubpackage();

            //合同号
            //item.setContractNo(new BigDecimal(map.get(0).toString()).toPlainString());

            item.setDeleted(false);
            // item.setMain(false);//是否主合同
            item.setBackletter(false);//是否保函

            item.setGmtModified(new Date());
            item.setGmtCreate(new Date());
            ModelUtil.setNotNullFields(item);
            //fiveBusinessContractLibraryMapper.insert(item);
            String businessKey= "fiveBusinessSubpackage_" + item.getId();

            String mainNo= item.getContractNo().split("-")[0];
            Map parve=Maps.newHashMap();
            parve.put("contractNo",mainNo);
            int size = fiveBusinessContractLibraryMapper.selectAll(parve).size();
            if (size>0){
                FiveBusinessContractLibrary main = fiveBusinessContractLibraryMapper.selectAll(parve).get(0);
                main.setMainContractLibraryNo(mainNo);
                main.setMainContractLibraryId(item.getId());
                main.setMainContractLibraryName(main.getContractName());
                main.setMain(true);
                main.setMainReviewIds(item.getId()+",");
                fiveBusinessContractLibraryMapper.updateByPrimaryKey(main);
            }else {
                //item.setMain(false);
            }

            item.setBusinessKey(businessKey);
            fiveBusinessContractLibraryMapper.updateByPrimaryKey(item);


        }
    }*/
@Resource
    ProcessHandleService processHandleService;
 @Test
    public void test16(){
     List<String> ccuser=Lists.newArrayList();
     ccuser.add("2616");
     ccuser.add("1651");
     ccuser.add("0070");
     ccuser.add("2996");
     ccuser.add("2494");
     processHandleService.addCcUserTask("7835802",ccuser);
 }

   @Test
    public void test17(){
    // historyService.deleteHistoricTaskInstance( 8046086);
     //historyService.deleteHistoricTaskInstance("8046086");
 }
    @Resource
    OaNoticeMapper oaNoticeMapper;
    @Resource
    CommonFileMapper commonFileMapper;
    @Test
    public void test18(){
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("noticeType", "部门空间");
        List<OaNotice> oaNotices = oaNoticeMapper.selectAll(params);
        for (OaNotice item:oaNotices){
            params.put("noticeType", "通知公告");
            params.put("noticeTitle", item.getNoticeTitle());
            if (oaNoticeMapper.selectAll(params).size()>0){
                OaNotice oaNotice = oaNoticeMapper.selectAll(params).get(0);
                item.setViewMans(oaNotice.getViewMans());
                item.setViewMansName(oaNotice.getViewMansName());
                oaNoticeMapper.updateByPrimaryKey(item);
            }else {
                params.put("noticeTitle", item.getNoticeTitle());
                params.put("noticeType", "文件简报");
                if (oaNoticeMapper.selectAll(params).size()>0){
                    OaNotice oaNotice = oaNoticeMapper.selectAll(params).get(0);
                    item.setViewMans(oaNotice.getViewMans());
                    item.setViewMansName(oaNotice.getViewMansName());
                    oaNoticeMapper.updateByPrimaryKey(item);
                 }
            }

        }

    }
    @Resource
    FiveOaDispatchMapper fiveOaDispatchMapper;
    @Test
    public void test19(){
        List<String> parentDeptChargeMen = selectEmployeeService.getParentDeptChargeMen(150, 2, false);
    }

    @Resource
    FiveOaInformationEquipmentExamineMapper fiveOaInformationEquipmentExamineMapper;
    @Resource
    FiveOaInformationEquipmentExamineService fiveOaInformationEquipmentExamineService;
    @Test
    public void test20(){
        //
        List<String> businessMenByDeptId = selectEmployeeService.getBusinessMenByDeptId(12);
        System.out.println(businessMenByDeptId);
    }

    @Test
    public void test21(){
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("contractReviewId",0);
        params.put("contractPreId",0);
        params.put("insertType",0);
        List<FiveBusinessContractLibrary> list = fiveBusinessContractLibraryMapper.selectAll(params);
        for (FiveBusinessContractLibrary library:list){
            library.setContractMoney(MyStringUtil.moneyToString(library.getContractMoney()));
            fiveBusinessContractLibraryMapper.updateByPrimaryKey(library);
        }
    }
    @Resource
    FiveOaDecisionMakingService fiveOaDecisionMakingService ;
    @Resource
    FiveAssetComputerService fiveAssetComputerService; ;
    @Test
    public void Test22(){
          Map map=Maps.newHashMap();
        List list = fiveAssetComputerService.listMapData(map, "five.oaAssetComputer", "", "", "luodong");

    }
    @Resource
    FiveOaStampApplyOfficeService fiveOaStampApplyOfficeService;
    @Test
    public void test(){
        fiveOaStampApplyOfficeService.listDateByFormKey();
    }


}
