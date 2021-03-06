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
     * EXCL ??????????????????
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

            //??????
            if (StringUtils.isNotEmpty(map.get(2).toString())) {
                String name = map.get(2).toString();
                HrDept hrDept = hrDeptService.selectByName(name);
                if (hrDept != null) {
                    deptId = hrDept.getId();
                }
            }

            //??????????????????????????????????????? ????????????
            if (PageHelper.count(() -> hrEmployeeSysMapper.selectAll(params)) > 0) {
                HrEmployeeSys hrEmployeeSys = hrEmployeeSysMapper.selectAll(params).get(0);
                HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(hrEmployeeSys.getUserLogin());
                hrEmployeeDto.setUserNo(map.get(3).toString());

                hrEmployeeService.update(hrEmployeeDto);

                hrEmployeeSysService.updateDeptId(substring,deptId);
            }
            updateNum++;
        }
        System.out.println("???????????????"+updateNum);
    }

    /**
     * EXCL ????????????????????????ID ??????
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

        System.out.println("???????????????"+updateNum);
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



    /** ?????????????????????*/
    @Test
    public void test2(){
        taskService.setAssignee("10246819","2136");
    }

    /** ???????????????*/
    @Test
    public void test22(){
        //7673130  7672773  7360893
        taskService.deleteTask("7360893","?????????????????????????????? 2863-luodong");
    }

    /** ??????????????????*/
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
        String path = "C:/Users/CMCUBIM/Desktop/?????????????????????.xls";
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

            //?????????
            item.setContractNo(map.get(0).toString());






            String contractType=map.get(1).toString();
            if ("??????,??????".contains(contractType)){
                item.setContractType("??????????????????????????????????????????");
                item.setProjectNature("??????");
            }else if (contractType.equals("????????????")){
                item.setContractType("????????????");
                item.setProjectNature("????????????");
            }else if (contractType.equals("????????????")){
                item.setContractType("????????????");
                item.setProjectNature("????????????");
            }

            item.setSignTime(map.get(2).toString());//????????????
            item.setProjectName(map.get(3).toString());//????????????
            item.setContractName(map.get(3).toString());
            item.setRecordProjectName(map.get(3).toString());//??????????????????
            String address=map.getOrDefault(4,"").toString()+map.getOrDefault(5,"").toString()+map.getOrDefault(6,"").toString();
            item.setConstructionArea(address);//????????????

            item.setCustomerName(map.get(7).toString());//????????????
            item.setLinkMan(map.getOrDefault(8,"").toString());//?????????
            item.setLinkTel(map.getOrDefault(9,"").toString());
            item .setCustomerAddress(map.getOrDefault(10,"").toString());

            item.setContractMoney(map.get(11).toString());

            item.setDeptName(map.get(12).toString());
            item.setDeptId(hrDeptService.getModelByName(item.getDeptName()).getId());


            item.setCreator(hrEmployeeSysService.selectByUserName(map.get(13).toString()));
            item.setCreatorName(map.get(13).toString());
            item.setBusinessChargeLeader(item.getCreator());//??????
            item.setBusinessChargeLeaderName(item.getCreatorName());
            item.setProjectManager(item.getCreator());//????????????
            item.setProjectManagerName(item.getCreatorName());


            item.setRemark(map.get(14).toString());
            item.setConstructionScale(map.get(15).toString());

            item.setReviewLevel("??????");

            item.setInOrOut("");
            item.setMilitaryMark("??????");
            item.setCivilMark("????????????");
            item.setForeignTradeMark("????????????");
            //item.setProjectMajorTypeFirst(commonCodeService.selectDefaultCodeValue("wuzhou","????????????????????????").toString());

            item.setInvestSource("????????????");
            item.setCustomerId(0);
            item.setBid(false);
            item.setAttach(false);
            item.setDeleted(false);
           // item.setMain(false);//???????????????
            item.setBackletter(false);//????????????

            item.setGmtModified(new Date());
            item.setGmtCreate(new Date());
            ModelUtil.setNotNullFields(item);
            fiveBusinessContractLibraryMapper.insert(item);
            String businessKey= EdConst.FIVE_BUSINESSCONTRACTLIBRARY+ "_" + item.getId();
            item.setBusinessKey(businessKey);
            //????????????
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
     //  taskHandleService.backTask("7343567","????????????",candidateUsers,"luodong",true);
    }
   /*???????????????*/
    @Test
    public void test6(){
        String path = "C:/Users/CMCUBIM/Desktop/???????????????.xls";
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

            //?????????
            item.setContractNo(map.get(4).toString());


            item.setContractType("????????????");
            item.setProjectNature("????????????");

          /*  String contractType=map.get(1).toString();
            if ("??????,??????".contains(contractType)){
                item.setContractType("??????????????????????????????????????????");
                item.setProjectNature("??????");
            }else if (contractType.equals("????????????")){

            }else if (contractType.equals("????????????")){
                item.setContractType("????????????");
                item.setProjectNature("????????????");
            }else {

            }*/

            item.setSignTime(map.get(2).toString());//????????????
            item.setProjectName(map.get(0).toString());//????????????
            item.setContractName(map.get(0).toString());
            item.setRecordProjectName(map.get(0).toString());//??????????????????
            //String address=map.getOrDefault(4,"").toString()+map.getOrDefault(5,"").toString()+map.getOrDefault(6,"").toString();
            item.setConstructionArea("");//????????????

            item.setCustomerName(map.get(1).toString());//????????????
          /*  item.setLinkMan(map.getOrDefault(8,"").toString());//?????????
            item.setLinkTel(map.getOrDefault(9,"").toString());
            item .setCustomerAddress(map.getOrDefault(10,"").toString());*/

            item.setContractMoney(map.get(5).toString());

            item.setDeptName("??????????????????????????????");
            item.setDeptId(76);


            item.setCreator(hrEmployeeSysService.selectByUserName(map.get(3).toString()));
            item.setCreatorName(map.get(3).toString());
            /*item.setBusinessChargeLeader(item.getCreator());//??????
            item.setBusinessChargeLeaderName(item.getCreatorName());*/
            item.setProjectManager(item.getCreator());//????????????
            item.setProjectManagerName(item.getCreatorName());


            item.setRemark("");
            item.setConstructionScale("");

            item.setReviewLevel("??????");

            item.setInOrOut("");
            item.setMilitaryMark("??????");
            item.setCivilMark("????????????");
            item.setForeignTradeMark("????????????");
            //item.setProjectMajorTypeFirst(commonCodeService.selectDefaultCodeValue("wuzhou","????????????????????????").toString());

            item.setInvestSource("????????????");
            item.setCustomerId(0);
            item.setBid(false);
            item.setAttach(false);
            item.setDeleted(false);
            // item.setMain(false);//???????????????
            item.setBackletter(false);//????????????
            item.setMain(false);

            item.setGmtModified(new Date());
            item.setGmtCreate(new Date());
            ModelUtil.setNotNullFields(item);
            fiveBusinessContractLibraryMapper.insert(item);
            String businessKey= EdConst.FIVE_BUSINESSCONTRACTLIBRARY+ "_" + item.getId();
            item.setBusinessKey(businessKey);
          /*  //????????????
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

    /*????????????*/
    @Test
    public void test7(){
        String path = "C:/Users/CMCUBIM/Desktop/??????.xls";
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

            //?????????
            item.setContractNo(map.get(5).toString());


            item.setContractType("????????????");
            item.setProjectNature("????????????");

          /*  String contractType=map.get(1).toString();
            if ("??????,??????".contains(contractType)){
                item.setContractType("??????????????????????????????????????????");
                item.setProjectNature("??????");
            }else if (contractType.equals("????????????")){

            }else if (contractType.equals("????????????")){
                item.setContractType("????????????");
                item.setProjectNature("????????????");
            }else {

            }*/

            item.setSignTime(map.get(6).toString());//????????????
            item.setProjectName(map.get(2).toString());//????????????
            item.setContractName(map.get(2).toString());
            item.setRecordProjectName(map.get(2).toString());//??????????????????
            //String address=map.getOrDefault(4,"").toString()+map.getOrDefault(5,"").toString()+map.getOrDefault(6,"").toString();
            item.setConstructionArea("");//????????????

            item.setCustomerName(map.get(1).toString());//????????????
          /*  item.setLinkMan(map.getOrDefault(8,"").toString());//?????????
            item.setLinkTel(map.getOrDefault(9,"").toString());
            item .setCustomerAddress(map.getOrDefault(10,"").toString());*/

            item.setContractMoney(map.get(7).toString());

            item.setDeptName("?????????????????????");
            item.setDeptId(75);


            item.setCreator(hrEmployeeSysService.selectByUserName(map.get(3).toString()));
            item.setCreatorName(map.get(3).toString());
            /*item.setBusinessChargeLeader(item.getCreator());//??????
            item.setBusinessChargeLeaderName(item.getCreatorName());*/
          /*  item.setProjectManager(item.getCreator());//????????????
            item.setProjectManagerName(item.getCreatorName());*/
            item.setTotalDesigner(item.getCreator());//??????
            item.setTotalDesignerName(item.getCreatorName());


            item.setRemark("??????:"+map.get(0).toString());
            item.setConstructionScale("");

            item.setReviewLevel("??????");

            item.setInOrOut("");
            if (map.get(3).toString().equals("jp")){
                item.setMilitaryMark("??????");
                item.setCivilMark("????????????");
                item.setInvestSource("??????");
            }else {
                item.setMilitaryMark("??????");
                item.setInvestSource("????????????");
            }

            item.setCivilMark("????????????");
            item.setForeignTradeMark("????????????");
            //item.setProjectMajorTypeFirst(commonCodeService.selectDefaultCodeValue("wuzhou","????????????????????????").toString());


            item.setCustomerId(0);
            item.setBid(false);
            item.setAttach(false);
            item.setDeleted(false);
            // item.setMain(false);//???????????????
            item.setBackletter(false);//????????????
            item.setMain(false);

            item.setGmtModified(new Date());
            item.setGmtCreate(new Date());
            ModelUtil.setNotNullFields(item);
            fiveBusinessContractLibraryMapper.insert(item);
            String businessKey= EdConst.FIVE_BUSINESSCONTRACTLIBRARY+ "_" + item.getId();
            item.setBusinessKey(businessKey);
          /*  //????????????
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
            if ("?????????,?????????".contains(his.getName())){
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


    /*????????????*/
    @Test
    public void test11(){
        String path = "C:/Users/CMCUBIM/Desktop/??????.xls";
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

            //?????????
            //item.setContractNo(new BigDecimal(map.get(0).toString()).toPlainString());
            item.setContractNo(map.get(0).toString());

            item.setContractType("????????????");
            item.setProjectNature("????????????");

          /*  String contractType=map.get(1).toString();
            if ("??????,??????".contains(contractType)){
                item.setContractType("??????????????????????????????????????????");
                item.setProjectNature("??????");
            }else if (contractType.equals("????????????")){

            }else if (contractType.equals("????????????")){
                item.setContractType("????????????");
                item.setProjectNature("????????????");
            }else {

            }*/
          //  String[] split = map.get(5).toString().split("\\.");

           // item.setSignTime("2020-"+split[1]+"-"+split[2]);//????????????
            item.setProjectName(map.get(2).toString());//????????????
            item.setContractName(map.get(2).toString());
            item.setRecordProjectName(map.get(2).toString());//??????????????????
            //String address=map.getOrDefault(4,"").toString()+map.getOrDefault(5,"").toString()+map.getOrDefault(6,"").toString();
            item.setConstructionArea("");//????????????

            item.setCustomerName(map.get(1).toString());//????????????
          /*  item.setLinkMan(map.getOrDefault(8,"").toString());//?????????
            item.setLinkTel(map.getOrDefault(9,"").toString());
            item .setCustomerAddress(map.getOrDefault(10,"").toString());*/

            item.setContractMoney(map.get(3).toString());

            item.setDeptName("??????????????????????????????");
            item.setDeptId(36);


            item.setCreator("1683");
            item.setCreatorName("?????????");
            /*;*/
          /*  item.setProjectManager(item.getCreator());//????????????
            item.setProjectManagerName(item.getCreatorName());*/
            item.setTotalDesigner(item.getCreator());//??????
            item.setTotalDesignerName(item.getCreatorName());

            //item.setBusinessChargeLeader(hrEmployeeSysService.selectByUserName(map.get(8).toString()));//??????
            //item.setBusinessChargeLeaderName(map.get(8).toString());

            item.setRemark("");
            item.setConstructionScale("");

            item.setReviewLevel("??????");

            item.setInOrOut("");


            item.setMilitaryMark("??????");
            item.setInvestSource("????????????");

            item.setCivilMark("????????????");
            item.setForeignTradeMark("????????????");
            //item.setProjectMajorTypeFirst(commonCodeService.selectDefaultCodeValue("wuzhou","????????????????????????").toString());


            item.setCustomerId(0);
            item.setBid(false);
            item.setAttach(false);
            item.setDeleted(false);
            // item.setMain(false);//???????????????
            item.setBackletter(false);//????????????
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
    /*????????????*/
    @Test
    public void test12(){
        String path = "C:/Users/CMCUBIM/Desktop/??????.xls";
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

            //?????????
            item.setContractNo(new BigDecimal(map.get(4).toString()).toPlainString());
            // item.setContractNo(map.get(0).toString());

            item.setContractType("????????????");
            item.setProjectNature("????????????");

          /*  String contractType=map.get(1).toString();
            if ("??????,??????".contains(contractType)){
                item.setContractType("??????????????????????????????????????????");
                item.setProjectNature("??????");
            }else if (contractType.equals("????????????")){

            }else if (contractType.equals("????????????")){
                item.setContractType("????????????");
                item.setProjectNature("????????????");
            }else {

            }*/
            String[] split = map.get(2).toString().split("\\.");
            if(split.length>3){
                item.setSignTime(split[0]+"-"+split[1]+"-"+split[2]);//????????????
            }else {
                item.setSignTime(split[0]+"_"+split[1]);//????????????
            }

            item.setProjectName(map.get(0).toString());//????????????
            item.setContractName(map.get(0).toString());
            item.setRecordProjectName(map.get(0).toString());//??????????????????
            //String address=map.getOrDefault(4,"").toString()+map.getOrDefault(5,"").toString()+map.getOrDefault(6,"").toString();
            item.setConstructionArea("");//????????????

            item.setCustomerName(map.get(1).toString());//????????????
          /*  item.setLinkMan(map.getOrDefault(8,"").toString());//?????????
            item.setLinkTel(map.getOrDefault(9,"").toString());
            item .setCustomerAddress(map.getOrDefault(10,"").toString());*/

            item.setContractMoney(map.get(5).toString());

            item.setDeptName("??????????????????????????????");
            item.setDeptId(76);


            item.setCreator(hrEmployeeSysService.selectByUserName(map.get(3).toString()));
            item.setCreatorName(map.get(3).toString());
            /*;*/
          /*  item.setProjectManager(item.getCreator());//????????????
            item.setProjectManagerName(item.getCreatorName());*/
            item.setTotalDesigner(item.getCreator());//??????
            item.setTotalDesignerName(item.getCreatorName());


            item.setRemark("");
            item.setConstructionScale("");

            item.setReviewLevel("??????");

            item.setInOrOut("");

            item.setMilitaryMark("??????");
            item.setInvestSource("????????????");
            item.setCivilMark("????????????");
            item.setForeignTradeMark("????????????");
            //item.setProjectMajorTypeFirst(commonCodeService.selectDefaultCodeValue("wuzhou","????????????????????????").toString());


            item.setCustomerId(0);
            item.setBid(false);
            item.setAttach(false);
            item.setDeleted(false);
            // item.setMain(false);//???????????????
            item.setBackletter(false);//????????????
            item.setMain(false);

            item.setGmtModified(new Date());
            item.setGmtCreate(new Date());
            ModelUtil.setNotNullFields(item);
            fiveBusinessContractLibraryMapper.insert(item);
            String businessKey= EdConst.FIVE_BUSINESSCONTRACTLIBRARY+ "_" + item.getId();
            item.setBusinessKey(businessKey);
          /*  //????????????
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

    /*?????????*/
    @Test
    public void test13(){
        String path = "C:/Users/CMCUBIM/Desktop/?????????.xls";
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

            //?????????
            item.setContractNo(new BigDecimal(map.get(0).toString()).toPlainString());
            // item.setContractNo(map.get(0).toString());

            item.setContractType("????????????");
            item.setProjectNature("????????????");

          /*  String contractType=map.get(1).toString();
            if ("??????,??????".contains(contractType)){
                item.setContractType("??????????????????????????????????????????");
                item.setProjectNature("??????");
            }else if (contractType.equals("????????????")){

            }else if (contractType.equals("????????????")){
                item.setContractType("????????????");
                item.setProjectNature("????????????");
            }else {

            }*/
           /* String[] split = map.get(2).toString().split("\\.");
            if(split.length>3){
                item.setSignTime("2020-"+split[1]+"-"+split[2]);//????????????
            }else {
                item.setSignTime("2020-"+split[1]);//????????????
            }*/

            item.setProjectName(map.get(1).toString());//????????????
            item.setContractName(map.get(1).toString());
            item.setRecordProjectName(map.get(1).toString());//??????????????????
            //String address=map.getOrDefault(4,"").toString()+map.getOrDefault(5,"").toString()+map.getOrDefault(6,"").toString();
            item.setConstructionArea("");//????????????

            item.setCustomerName(map.get(3).toString());//????????????
          /*  item.setLinkMan(map.getOrDefault(8,"").toString());//?????????
            item.setLinkTel(map.getOrDefault(9,"").toString());
            item .setCustomerAddress(map.getOrDefault(10,"").toString());*/

            item.setContractMoney(map.get(6).toString());

            item.setDeptName("???????????????????????????");
            item.setDeptId(53);


            item.setCreator("2898");
            item.setCreatorName("??????");
            /*;*/
          /*  item.setProjectManager(item.getCreator());//????????????
            item.setProjectManagerName(item.getCreatorName());*/
           /* item.setTotalDesigner(item.getCreator());//??????
            item.setTotalDesignerName(item.getCreatorName());*/


            item.setRemark("");
            item.setConstructionScale("");

            item.setReviewLevel("??????");

            item.setInOrOut("");

            item.setMilitaryMark("??????");
            item.setInvestSource("????????????");
            item.setCivilMark("????????????");
            item.setForeignTradeMark("????????????");
            //item.setProjectMajorTypeFirst(commonCodeService.selectDefaultCodeValue("wuzhou","????????????????????????").toString());


            item.setCustomerId(0);
            item.setBid(false);
            item.setAttach(false);
            item.setDeleted(false);
            // item.setMain(false);//???????????????
            item.setBackletter(false);//????????????
            item.setMain(false);

            item.setGmtModified(new Date());
            item.setGmtCreate(new Date());
            ModelUtil.setNotNullFields(item);
            fiveBusinessContractLibraryMapper.insert(item);
            String businessKey= EdConst.FIVE_BUSINESSCONTRACTLIBRARY+ "_" + item.getId();
            item.setBusinessKey(businessKey);
          /*  //????????????
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
     /*????????????*/
    @Test
    public void test14(){

        String exchangeLogin1="2399";
        String exchangeLogin2="2192";
        String tempChangeLogin="justtemp";
        List<Task> list = taskService.createTaskQuery().taskAssignee("2192").list();
        List<Task> list1 = taskService.createTaskQuery().taskAssignee("justtemp").list();
        List<Task> list2 = taskService.createTaskQuery().taskAssignee("2399").list();
        //??????luodond?????????????????????temp
    /*    for(Task task:taskService.createTaskQuery().taskAssignee(exchangeLogin1).list()){
            taskService.setAssignee(task.getId(),tempChangeLogin);
        }*/
        //???hnz????????????luodong
      /*  for(Task task:taskService.createTaskQuery().taskAssignee(exchangeLogin2).list()){
            taskService.setAssignee(task.getId(),exchangeLogin1);
        }*/
        //???temp????????????hnz
        for(Task task:taskService.createTaskQuery().taskAssignee(tempChangeLogin).list()){
            taskService.setAssignee(task.getId(),exchangeLogin2);
        }
    }

    /*????????????*/
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

                //?????????
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

                item.setContractType("????????????");
                item.setProjectNature("????????????");

                String[] split = map.get(4).toString().split("\\.");
                if (Integer.parseInt(split[0])>90){
                    item.setSignTime("19"+split[0]+"-"+split[1]+"-"+split[2]);//????????????
                }else {
                    item.setSignTime("20"+split[0]+"-"+split[1]+"-"+split[2]);//????????????
                }

                item.setProjectName(map.get(1).toString());//????????????
                item.setContractName(map.get(1).toString());
                item.setRecordProjectName(map.get(1).toString());//??????????????????
                item.setProjectNo("");//?????????
                //item.setConstructionArea(map.getOrDefault(12,"").toString());//????????????

                item.setCustomerName(map.get(5).toString());//????????????
                item.setLinkMan("");//?????????
                item.setLinkTel("");
                item .setCustomerAddress("");

                item.setContractMoney(map.get(6).toString());
                String deptName=map.get(3).toString();
                HrDept hrDept = hrDeptService.selectByName(deptName);
                if (hrDept!=null){
                    item.setDeptName(hrDept.getName());
                    item.setDeptId(hrDept.getId());
                }else {
                    System.out.println("?????????????????????"+insertNum+"?????????????????????"+updateNum);
                    System.out.println(deptName+"????????????????????????: "+ item.getContractNo());
                    return;
                }
                    //??????	2623
                if (map.get(7).toString().equals("")){
                    item.setCreator("2623");
                    item.setCreatorName("?????????");
                }else {
                    if (hrEmployeeSysService.selectByUserName(map.get(7).toString()).equals("")){
                        item.setCreator("2623");
                        item.setCreatorName("?????????");
                    }else {
                        item.setCreator(hrEmployeeSysService.selectByUserName(map.get(7).toString()));
                        item.setCreatorName(map.get(7).toString());
                    }
                }
                //item.setTotalDesigner(hrEmployeeSysService.selectByUserName(map.get(16).toString()));//??????
                //item.setTotalDesignerName(hrEmployeeSysService.selectByUserName(map.get(16).toString()));
               /* item.setRemark(map.get(17).toString());*/
                item.setConstructionScale("");

                item.setReviewLevel("??????");

                item.setInOrOut("");
            /*    if (map.get(5).toString().equals("JP")){
                    item.setMilitaryMark("??????");
                    item.setCivilMark("????????????");
                    item.setInvestSource("??????");
                }else if (map.get(5).toString().equals("CL,MB,NJ")){
                    item.setMilitaryMark("??????");
                    item.setInvestSource("????????????");
                }else {
                    item.setMilitaryMark("??????");
                    item.setInvestSource("????????????");
                }*/
                item.setMilitaryMark("??????");
                item.setInvestSource("????????????");
                item.setForeignTradeMark("????????????");
                //item.setProjectMajorTypeFirst(commonCodeService.selectDefaultCodeValue("wuzhou","????????????????????????").toString());


                item.setCustomerId(0);
                item.setBid(false);
                item.setAttach(false);
                item.setDeleted(false);
                // item.setMain(false);//???????????????
                item.setBackletter(false);//????????????
                item.setMain(false);

                item.setGmtModified(new Date());
                item.setGmtCreate(new Date());
                item.setRemark("????????????");
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
                System.out.println(contractNoA+":??????"+updateNum);

            }
            System.out.println("?????????????????????"+insertNum+"?????????????????????"+updateNum);
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
    /*??????????????????*/
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

            //?????????
            //item.setContractNo(new BigDecimal(map.get(0).toString()).toPlainString());

            item.setDeleted(false);
            // item.setMain(false);//???????????????
            item.setBackletter(false);//????????????

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
        params.put("noticeType", "????????????");
        List<OaNotice> oaNotices = oaNoticeMapper.selectAll(params);
        for (OaNotice item:oaNotices){
            params.put("noticeType", "????????????");
            params.put("noticeTitle", item.getNoticeTitle());
            if (oaNoticeMapper.selectAll(params).size()>0){
                OaNotice oaNotice = oaNoticeMapper.selectAll(params).get(0);
                item.setViewMans(oaNotice.getViewMans());
                item.setViewMansName(oaNotice.getViewMansName());
                oaNoticeMapper.updateByPrimaryKey(item);
            }else {
                params.put("noticeTitle", item.getNoticeTitle());
                params.put("noticeType", "????????????");
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
    @Resource
    GuavaCacheService guavaCacheService;
    @Test
    public void test(){
        guavaCacheService.invalidate("COMMON_BLACK_ALL");
    }


}
