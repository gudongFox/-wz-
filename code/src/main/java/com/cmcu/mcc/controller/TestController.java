package com.cmcu.mcc.controller;

import com.cmcu.common.JsonData;
import com.cmcu.common.dao.CommonCodeMapper;
import com.cmcu.common.dao.CommonFileMapper;
import com.cmcu.common.dao.CommonFormDetailMapper;
import com.cmcu.common.dto.InputConfigDto;
import com.cmcu.common.dto.UserDto;
import com.cmcu.common.entity.CommonBlack;
import com.cmcu.common.entity.CommonCode;
import com.cmcu.common.entity.CommonFile;
import com.cmcu.common.entity.CommonFormDetail;
import com.cmcu.common.service.*;
import com.cmcu.common.util.JsonMapper;
import com.cmcu.common.util.MyDateUtil;
import com.cmcu.common.util.MyPoiUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.business.dao.*;
import com.cmcu.mcc.business.entity.*;
import com.cmcu.mcc.business.service.BusinessPurchaseService;
import com.cmcu.mcc.business.service.BusinessRecordService;
import com.cmcu.mcc.business.service.BusinessSubpackageService;
import com.cmcu.mcc.finance.dao.FiveFinanceStampTaxMapper;
import com.cmcu.mcc.finance.entity.FiveFinanceStampTax;
import com.cmcu.mcc.five.dao.FiveOaInformationEquipmentExamineMapper;
import com.cmcu.mcc.five.entity.FiveOaInformationEquipmentExamine;
import com.cmcu.mcc.five.service.FiveAssetComputerService;
import com.cmcu.mcc.five.service.FiveOaInformationEquipmentExamineService;
import com.cmcu.mcc.five.service.FiveOaStampApplyOfficeService;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.business.service.FiveBusinessContractReviewService;
import com.cmcu.mcc.hr.dao.HrEmployeeSysMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.entity.HrEmployee;
import com.cmcu.mcc.hr.entity.HrEmployeeSys;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.oa.dao.OaNoticeMapper;
import com.cmcu.mcc.oa.entity.OaNotice;
import com.cmcu.mcc.service.TempFormService;import com.common.wx.service.BaseService;
import com.common.wx.service.MeetingRoomService;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    CommonFormDataService commonFormDataService;
    @Autowired
    TempFormService tempFormService;

    @Resource
    CommonUserService commonUserService;

    @Resource
    CommonFormDetailMapper commonFormDetailMapper;


    @RequestMapping("/meeting.json")
    public JsonData TestMeeting() {
        return JsonData.success( MeetingRoomService.listData());
    }

    @RequestMapping("/getToken.json")
    public JsonData getAccessToken() {
        return JsonData.success( BaseService.getAccessToken());
    }


    @RequestMapping("/do")
    public void JustTest() {
        Map params=Maps.newHashMap();
        params.put("inputCode","projectName");
        List<CommonFormDetail> details=commonFormDetailMapper.selectAll(params);
        for(CommonFormDetail detail:details){
            InputConfigDto inputConfigDto=InputConfigDto.getInstance(detail.getInputConfig());
            if(!inputConfigDto.isKeyInfo()) {
                inputConfigDto.setKeyInfo(true);
                detail.setInputConfig(JsonMapper.obj2String(inputConfigDto));
                commonFormDetailMapper.updateByPrimaryKey(detail);
            }
        }
        System.out.println("done");
    }
    @RequestMapping("/addHrBasic")
    public void addHrBasic(){
        tempFormService.addHrBasic();
    }


    @RequestMapping("/addHrPatent")
    public void addHrPatent() {
        tempFormService.addHrPatent();
    }

    @RequestMapping("/addHrEducation")
    public void addHrEducation() {
        tempFormService.addHrEducation();
    }

    @RequestMapping("/addHrTechnology")
    public void addHrTechnology() {
        tempFormService.addHrTechnology();
    }

    @RequestMapping("/addHrProfession")
    public void addHrProfession() {
        tempFormService.addHrProfession();
    }

    @RequestMapping("/addHrFamily")
    public void addHrFamily() {
        tempFormService.addHrFamily();
    }

    @RequestMapping("/addHrExamine")
    public void addHrExamine() {
        tempFormService.addHrExamine();
    }

    @RequestMapping("/addHrPartyPosition")
    public void addHrPartyPosition() {
        tempFormService.addHrPartyPosition();
    }

    @RequestMapping("/addHrImportantGroup")
    public void addHrImportantGroup() {
        tempFormService.addHrImportantGroup();
    }

    @RequestMapping("/addHrPartTimeJob")
    public void addHrPartTimeJob() {
        tempFormService.addHrPartTimeJob();
    }

    @RequestMapping("/addHrQualification")
    public void addHrQualification() {
        tempFormService.addHrQualification();
    }

    @RequestMapping("/addHrAward")
    public void addHrAward() {
        tempFormService.addHrAward();
    }

    @RequestMapping("/addHrHonor")
    public void addHrHonor() {
        tempFormService.addHrHonor();
    }

    @RequestMapping("/addHrPaper")
    public void addHrPaper() {
        tempFormService.addHrPaper();
    }

    @RequestMapping("/addHrPunish")
    public void addHrPunish() {
        tempFormService.addHrPunish();
    }

    @RequestMapping("/addHrGoAbroad")
    public void addHrGoAbroad() {
        tempFormService.addHrGoAbroad();
    }

    @RequestMapping("/addHrStandard")
    public void addHrStandard() {
        tempFormService.addHrStandard();
    }

    @RequestMapping("/addHrTrain")
    public void addHrTrain() {
        tempFormService.addHrTrain();
    }

    @RequestMapping("/addHrCourse")
    public void addHrCourse() {
        tempFormService.addHrCourse();
    }

    @RequestMapping("/addHrProject")
    public void addHrProject() {
        tempFormService.addHrProject();
    }

    @RequestMapping("/addHrResume")
    public void addHrResume() {
        tempFormService.addHrResume();
    }

    @RequestMapping("/addHrTrainMember")
    public void addHrTrainMember() {
        tempFormService.addHrTrainMember();
    }


    @RequestMapping("/insertStampTax")
    public void insertStampTax() {
        fiveBusinessContractReviewService.insertStampTax(35);
    }


    @Resource
    HrEmployeeMapper hrEmployeeMapper;

    @Resource
    HrEmployeeSysMapper hrEmployeeSysMapper;
    @Autowired
    BusinessSubpackageMapper businessSubpackageMapper;




    @Resource
    TaskService taskService;

    @RequestMapping("/updateAssignee")
    public void test2(String taskId,String userLogin){
        taskService.setAssignee(taskId,userLogin);
    }
    @Resource
    BusinessRecordMapper businessRecordMapper;
    @Resource
    BusinessRecordService businessRecordService;
    @Autowired
    MyActService myActService;
    @Autowired
    SelectEmployeeService selectEmployeeService;
    @RequestMapping("/updateAllProjectNo")
    public JsonData updateAllProjectNo() {
        Map map = new HashMap();
        map.put("deleted",false);
        List<BusinessRecord> businessRecords = businessRecordMapper.selectAll(map);
        for(BusinessRecord record:businessRecords){
            record.setProjectNo("");
            businessRecordMapper.updateByPrimaryKey(record);
        }
        for(BusinessRecord record:businessRecords){
            String projectNo ="";
            try{
                projectNo = businessRecordService.getProjectNo(record.getId());
            }catch (Exception e){
                record.setProjectNo(projectNo);
                businessRecordMapper.updateByPrimaryKey(record);
            }

            record.setProjectNo(projectNo);
            businessRecordMapper.updateByPrimaryKey(record);
        }
        return JsonData.success();
    }
    @Resource
    FiveBusinessContractReviewMapper fiveBusinessContractReviewMapper;

    @Resource

    FiveBusinessContractReviewService fiveBusinessContractReviewService;
    @RequestMapping("/updateAllContractNo")
    public JsonData updateAllContractNo() {
        Map map = new HashMap();
        map.put("deleted",false);
        List<FiveBusinessContractReview> contractReviews = fiveBusinessContractReviewMapper.selectAll(map);
        //先清空所有
        for(FiveBusinessContractReview review:contractReviews){
            review.setContractNo("");
            fiveBusinessContractReviewMapper.updateByPrimaryKey(review);
        }
        for(FiveBusinessContractReview review:contractReviews){
            String contractNo ="";
            try{
                contractNo = fiveBusinessContractReviewService.getContractNo(review.getId());
            }catch (Exception e){
                review.setContractNo(contractNo);
                fiveBusinessContractReviewMapper.updateByPrimaryKey(review);
            }
            review.setContractNo(contractNo);
            fiveBusinessContractReviewMapper.updateByPrimaryKey(review);
        }
        return JsonData.success();
    }

    @RequestMapping("/updateProcessVariables")
    public JsonData updateProcessVariables() {
        int i =0;
        try{
            Map map = new HashMap();
            map.put("deleted",false);
            List<FiveBusinessContractReview> contractReviews = fiveBusinessContractReviewMapper.selectAll(map);
            for(FiveBusinessContractReview contractReview:contractReviews){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = sdf.parse("2020-11-30");
                if(contractReview.getGmtCreate().before(date)){
                    i++;
                    Map variables=Maps.newHashMap();
                    variables.put("flag",contractReview.getReviewLevel());
                    myActService.setVariables(contractReview.getProcessInstanceId(),variables);
                }
            }
        }catch (Exception e){
            System.out.println(i);
        }

        return JsonData.success(i);
    }
    @Autowired
    FiveFinanceStampTaxMapper fiveFinanceStampTaxMapper;
    @Autowired
    FiveBusinessContractLibraryMapper fiveBusinessContractLibraryMapper;
    @Autowired
    FiveBusinessContractLibrarySubpackageMapper fiveBusinessContractLibrarySubpackageMapper;
    @Autowired
    CommonFileService commonFileService;
    //计算 印花税的合同承接部门
    @RequestMapping("/updateStampTaxDept")
    public JsonData updateStampTaxDept() {
        Map map = new HashMap();
        map.put("deleted",false);
        List<FiveFinanceStampTax> fiveFinanceStampTaxes = fiveFinanceStampTaxMapper.selectAll(map);
        for(FiveFinanceStampTax stampTax:fiveFinanceStampTaxes){
            if(stampTax.getContractId()!=0){
                FiveBusinessContractLibrary library = fiveBusinessContractLibraryMapper.selectByPrimaryKey(stampTax.getContractId());
                stampTax.setContractDeptId(library.getDeptId());
                stampTax.setContractDeptName(library.getDeptName());
            }else if(stampTax.getContractSubpackageId()!=0){
                FiveBusinessContractLibrary library = fiveBusinessContractLibraryMapper.selectByPrimaryKey(stampTax.getContractSubpackageId());
                stampTax.setContractDeptId(library.getDeptId());
                stampTax.setContractDeptName(library.getDeptName());
            }
        }
        return JsonData.success();
    }

    @Resource
    CommonPrintTableService commonPrintTableService;
    @RequestMapping("/printData")
    public JsonData printData(){

        Map luodong = commonPrintTableService.getPrintDate("oaStampApplyOffice_1751", "luodong");
        return JsonData.success(luodong);
    }


    @RequestMapping("/getProcessVariable")
    public JsonData insertStampTask(String taskId){
        Map<String, Object> variables = taskService.getVariables(taskId);
        return JsonData.success(variables);
    }

    @Resource
    CommonBlackService commonBlackService;
    @RequestMapping("/blackUsr")
    public JsonData test20(){
        int update=0;
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        List<HrEmployeeDto> list = hrEmployeeMapper.selectAll(params);
        String userlongs="";
        for( HrEmployeeDto dto:list){
            HrEmployeeSys hrEmployeeSys = hrEmployeeSysMapper.selectByUserLogin(dto.getUserLogin());
            String twoDay = MyDateUtil.getTwoDay(MyDateUtil.dateToStr(hrEmployeeSys.getGmtModified()), "2021-01-01");
            if (hrEmployeeSys.getPassword().equals("HDazSj6HQF6ToVYlCHTS1Q==")&&Integer.parseInt(twoDay)<0){
                CommonBlack black=new CommonBlack();
                black.setTargetIp("*");
                black.setTenetId("wuzhou");
                black.setGmtCreate(new Date());
                black.setGmtModified(new Date());
                black.setGmtExpired(MyDateUtil.strToDateLong("2099-01-01 00:00:00"));
                black.setId(0);
                black.setTargetUser(hrEmployeeSys.getUserLogin());
                black.setForbidden(true);
                commonBlackService.save(black);
                update++;
                userlongs+=black.getTargetUser()+"-"+hrEmployeeMapper.selectByUserLoginOrNo(black.getTargetUser()).getUserName();
                }

            }
        return JsonData.success("修改数量："+update+"人员："+userlongs);

        }

    @Resource
    FiveOaStampApplyOfficeService fiveOaStampApplyOfficeService;
    @RequestMapping("/listDateByFormKey.json")
    public JsonData listDateByFormKey(){
        fiveOaStampApplyOfficeService.listDateByFormKey();
        return JsonData.success();
    }
    }
