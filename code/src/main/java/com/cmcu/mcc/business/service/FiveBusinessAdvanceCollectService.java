package com.cmcu.mcc.business.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.util.*;
import com.cmcu.mcc.act.service.MyActService;

import com.cmcu.mcc.business.dao.FiveBusinessAdvanceCollectDetailMapper;
import com.cmcu.mcc.business.dao.FiveBusinessAdvanceCollectMapper;
import com.cmcu.mcc.business.dao.FiveBusinessAdvanceMapper;
import com.cmcu.mcc.business.dto.FiveBusinessAdvanceCollectDto;
import com.cmcu.mcc.business.entity.FiveBusinessAdvance;
import com.cmcu.mcc.business.entity.FiveBusinessAdvanceCollect;

import com.cmcu.mcc.business.entity.FiveBusinessAdvanceCollectDetail;
import com.cmcu.mcc.business.entity.FiveBusinessAdvanceDetail;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.five.entity.FiveOaProjectfundplanDetail;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.HrEmployeeService;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.service.impl.HandleFormService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.*;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FiveBusinessAdvanceCollectService extends BaseService {

    @Resource
    FiveBusinessAdvanceCollectMapper fiveBusinessAdvanceCollectMapper;
    @Resource
    FiveBusinessAdvanceCollectDetailMapper fiveBusinessAdvanceCollectDetailMapper;
    @Resource
    HrEmployeeMapper hrEmployeeMapper;
    @Autowired
    ProcessQueryService processQueryService;
    @Autowired
    MyActService myActService;
    @Autowired
    SelectEmployeeService selectEmployeeService;
    @Autowired
    TaskHandleService taskHandleService;
    @Autowired
    HrEmployeeService hrEmployeeService;
    @Autowired
    HrEmployeeSysService hrEmployeeSysService;
    @Resource
    HandleFormService handleFormService;
    @Resource
    FiveBusinessAdvanceMapper fiveBusinessAdvanceMapper;
    @Resource
    FiveBusinessAdvanceSevice fiveBusinessAdvanceSevice;

    public void remove(int id,String userLogin){
        FiveBusinessAdvanceCollect item = fiveBusinessAdvanceCollectMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"??????????????????"+item.getCreatorName()+"??????");
        //????????????
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    //update
    public void update(FiveBusinessAdvanceCollectDto fiveBusinessAdvanceCollectDto){
        FiveBusinessAdvanceCollect model = fiveBusinessAdvanceCollectMapper.selectByPrimaryKey(fiveBusinessAdvanceCollectDto.getId());
        model.setDeptId(fiveBusinessAdvanceCollectDto.getDeptId());
        model.setCreator(fiveBusinessAdvanceCollectDto.getCreator());
        model.setCreatorName(fiveBusinessAdvanceCollectDto.getCreatorName());
        model.setDeptName(fiveBusinessAdvanceCollectDto.getDeptName());
        model.setGmtCreate(fiveBusinessAdvanceCollectDto.getGmtCreate());
        model.setGmtModified(new Date());
        model.setFormNo(fiveBusinessAdvanceCollectDto.getFormNo());
        model.setRemark(fiveBusinessAdvanceCollectDto.getRemark());
        model.setCollectYear(fiveBusinessAdvanceCollectDto.getCollectYear());
        model.setCollectMonth(fiveBusinessAdvanceCollectDto.getCollectMonth());
        model.setDeclareType(fiveBusinessAdvanceCollectDto.getDeclareType());

        fiveBusinessAdvanceCollectMapper.updateByPrimaryKey(model);

    }


    public FiveBusinessAdvanceCollectDto getModelById(int id){
        return getDto(fiveBusinessAdvanceCollectMapper.selectByPrimaryKey(id));
    }

    public FiveBusinessAdvanceCollectDto getDto(FiveBusinessAdvanceCollect item) {
        FiveBusinessAdvanceCollectDto dto= FiveBusinessAdvanceCollectDto.adapt(item);
        dto.setProcessName("?????????");

        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");
            if(customProcessInstance == null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveBusinessAdvanceCollectMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance != null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }

        return dto;
    }

    public int getNewModel(String userLogin){

        FiveBusinessAdvanceCollect item=new FiveBusinessAdvanceCollect();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        if(MyDateUtil.getMonth()<10){
            item.setCollectMonth(MyDateUtil.getYear()+"-0"+MyDateUtil.getMonth());
        }else{
            item.setCollectMonth(MyDateUtil.getYear()+"-"+MyDateUtil.getMonth());
        }

        item.setCollectYear(MyDateUtil.getYear()+"");

        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
       
        item.setDeclareType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????").toString());
        ModelUtil.setNotNullFields(item);

        fiveBusinessAdvanceCollectMapper.insert(item);
        String businessKey= EdConst.FIVE_BUSINESS_ADVANCE_COLLECT+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);

        variables.put("businessLeader", selectEmployeeService.getDeptChargeMen(48));//?????????????????????
        variables.put("businessChargeLeader", selectEmployeeService.getOtherDeptChargeMan(48));//???????????????????????????
        variables.put("totalManger", hrEmployeeService.selectUserByPositionName("?????????"));//?????????
        variables.put("totalAccount", hrEmployeeService.selectUserByPositionName("????????????"));//????????????

        variables.put("businessPerf", selectEmployeeService.getUserListByRoleName("???????????????(?????????)"));//???????????????(?????????)
        variables.put("copyMan", MyStringUtil.listToString(selectEmployeeService.getUserListByRoleName("???????????????(?????????)")));//???????????????(?????????)


        variables.put("processDescription", "??????/??????????????????????????????"+item.getCreatorName());

        item.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_BUSINESS_ADVANCE_COLLECT,item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        fiveBusinessAdvanceCollectMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref, int pageNum, int pageSize) {
        params.put("deleted",false);
        params.put("isLikeSelect",true);

        //???????????????????????????????????????  myDeptData true?????????????????? false???????????????
        Map map =new HashMap();
        map.put("myDeptData",false);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveBusinessAdvanceCollectMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveBusinessAdvanceCollect item=(FiveBusinessAdvanceCollect) p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }


    public HSSFWorkbook downCollect(int collectId) throws IOException {
        FiveBusinessAdvanceCollectDto advanceCollectDto = getModelById(collectId);
        Map params = new HashMap();
        params.put("deleted",false);
        params.put("processEnd",true);
        if(advanceCollectDto.getDeclareType().equals("??????????????????")){
            params.put("month",advanceCollectDto.getCollectMonth().trim());
            params.put("declareType","??????????????????");
        }else if(advanceCollectDto.getDeclareType().equals("?????????")){
            params.put("searchYear",advanceCollectDto.getCollectMonth().split("-")[0]);
            params.put("declareType","?????????");
        }else{
            params.put("month",advanceCollectDto.getCollectMonth().trim());
            params.put("declareType","??????");
        }

        List<FiveBusinessAdvance> fiveBusinessAdvances = fiveBusinessAdvanceMapper.selectAll(params);


        String classpath = this.getClass().getResource("/").getPath().replaceFirst("/", "");
        String webappRoot = classpath.replaceAll("WEB-INF/classes/", "");
        String filePath = webappRoot + "assets/doc/????????????/???????????????????????????.xls";
        //??????????????????
        InputStream in = new FileInputStream(filePath);

        HSSFWorkbook workbook = new HSSFWorkbook(in);
        HSSFSheet sheet = workbook.getSheetAt(0);

        //??????
        String year =advanceCollectDto.getCollectMonth().split("-")[0];
        String month =advanceCollectDto.getCollectMonth().split("-")[1];

        HSSFCell headCell =  sheet.getRow(0).getCell(0);
        if(advanceCollectDto.getDeclareType().equals("??????????????????")) {
            headCell.setCellValue(year + "???" + month + "???" + "?????????????????????");
        }else{
            headCell.setCellValue(year + "??????????????????");
        }
        HSSFCell headCell1 =  sheet.getRow(1).getCell(0);
        if(advanceCollectDto.getDeclareType().equals("??????????????????")) {
            headCell1.setCellValue("???????????????????????????????????????????????????????????????????????????????????????????????????????????????" + year + "???" + month + "???" + "??????????????????????????????????????????");
        }else{
            headCell1.setCellValue("???????????????????????????????????????????????????????????????????????????????????????????????????????????????" + year + "???" + "??????????????????????????????????????????");
        }
        Double colloctMoney = 0.0;
        //??????
        colloctMoney = getCollectByDeptId(fiveBusinessAdvances,75);
        for(int i=1;i<=3;i++){
            HSSFCell cell = sheet.getRow(3).getCell(i);
            cell.setCellValue(colloctMoney);
        }
        //??????
        colloctMoney = getCollectByDeptId(fiveBusinessAdvances,74);
        for(int i=1;i<=3;i++){
            HSSFCell cell = sheet.getRow(4).getCell(i);
            cell.setCellValue(colloctMoney);
        }
        //?????????
        colloctMoney = getCollectByDeptId(fiveBusinessAdvances,76);
        for(int i=1;i<=3;i++){
            HSSFCell cell = sheet.getRow(5).getCell(i);
            cell.setCellValue(colloctMoney);
        }
        //?????????
        colloctMoney = getCollectByDeptId(fiveBusinessAdvances,47);
        for(int i=1;i<=3;i++){
            HSSFCell cell = sheet.getRow(6).getCell(i);
            cell.setCellValue(colloctMoney);
        }
        //??????
        colloctMoney = getCollectByDeptId(fiveBusinessAdvances,34);
        for(int i=1;i<=3;i++){
            HSSFCell cell = sheet.getRow(7).getCell(i);
            cell.setCellValue(colloctMoney);
        }
        //??????
        colloctMoney = getCollectByDeptId(fiveBusinessAdvances,45);
        for(int i=1;i<=3;i++){
            HSSFCell cell = sheet.getRow(8).getCell(i);
            cell.setCellValue(colloctMoney);
        }
        //???????????????????????????
        colloctMoney = getCollectByDeptId(fiveBusinessAdvances,63);
        for(int i=1;i<=3;i++){
            HSSFCell cell = sheet.getRow(9).getCell(i);
            cell.setCellValue(colloctMoney);
        }
        //?????????
        colloctMoney = getCollectByDeptId(fiveBusinessAdvances,20);
        for(int i=1;i<=3;i++){
            HSSFCell cell = sheet.getRow(10).getCell(i);
            cell.setCellValue(colloctMoney);
        }
        //??????
        colloctMoney = getCollectByDeptId(fiveBusinessAdvances,53);
        for(int i=1;i<=3;i++){
            HSSFCell cell = sheet.getRow(11).getCell(i);
            cell.setCellValue(colloctMoney);
        }
        //?????????
        colloctMoney = getCollectByDeptId(fiveBusinessAdvances,7);
        for(int i=1;i<=3;i++){
            HSSFCell cell = sheet.getRow(12).getCell(i);
            cell.setCellValue(colloctMoney);
        }
        //?????????
        colloctMoney = getCollectByDeptId(fiveBusinessAdvances,36);
        for(int i=1;i<=3;i++){
            HSSFCell cell = sheet.getRow(13).getCell(i);
            cell.setCellValue(colloctMoney);
        }
        //????????????
        colloctMoney = getCollectByDeptId(fiveBusinessAdvances,12);
        for(int i=1;i<=3;i++){
            HSSFCell cell = sheet.getRow(14).getCell(i);
            cell.setCellValue(colloctMoney);
        }

        //?????????
        colloctMoney = getCollectByDeptId(fiveBusinessAdvances,13);
        for(int i=1;i<=3;i++){
            HSSFCell cell = sheet.getRow(16).getCell(i);
            cell.setCellValue(colloctMoney);
        }
        //????????????
        colloctMoney = getCollectByDeptId(fiveBusinessAdvances,50);
        for(int i=1;i<=3;i++){
            HSSFCell cell = sheet.getRow(17).getCell(i);
            cell.setCellValue(colloctMoney);
        }

        //?????????
        colloctMoney = getCollectByDeptId(fiveBusinessAdvances,59);
        for(int i=1;i<=3;i++){
            HSSFCell cell = sheet.getRow(19).getCell(i);
            cell.setCellValue(colloctMoney);
        }
        //??????
        colloctMoney = getCollectByDeptId(fiveBusinessAdvances,72);
        for(int i=1;i<=3;i++){
            HSSFCell cell = sheet.getRow(20).getCell(i);
            cell.setCellValue(colloctMoney);
        }
        //??????
        colloctMoney = getCollectByDeptId(fiveBusinessAdvances,48);
        for(int i=1;i<=3;i++){
            HSSFCell cell = sheet.getRow(21).getCell(i);
            cell.setCellValue(colloctMoney);
        }
        //?????????
        colloctMoney = getCollectByDeptId(fiveBusinessAdvances,11);
        for(int i=1;i<=3;i++){
            HSSFCell cell = sheet.getRow(22).getCell(i);
            cell.setCellValue(colloctMoney);
        }
        //??????
        colloctMoney = getCollectByDeptId(fiveBusinessAdvances,101);
        for(int i=1;i<=3;i++){
            HSSFCell cell = sheet.getRow(23).getCell(i);
            cell.setCellValue(colloctMoney);
        }
        //??????
        colloctMoney = getCollectByDeptId(fiveBusinessAdvances,58);
        for(int i=1;i<=3;i++){
            HSSFCell cell = sheet.getRow(24).getCell(i);
            cell.setCellValue(colloctMoney);
        }
        //??????
        colloctMoney = getCollectByDeptId(fiveBusinessAdvances,18);
        for(int i=1;i<=3;i++){
            HSSFCell cell = sheet.getRow(25).getCell(i);
            cell.setCellValue(colloctMoney);
        }
        //??????
        colloctMoney = getCollectByDeptId(fiveBusinessAdvances,38);
        for(int i=1;i<=3;i++){
            HSSFCell cell = sheet.getRow(26).getCell(i);
            cell.setCellValue(colloctMoney);
        }
        //??????
        colloctMoney = getCollectByDeptId(fiveBusinessAdvances,29);
        for(int i=1;i<=3;i++){
            HSSFCell cell = sheet.getRow(27).getCell(i);
            cell.setCellValue(colloctMoney);
        }
        //??????
        colloctMoney = getCollectByDeptId(fiveBusinessAdvances,9);
        for(int i=1;i<=3;i++){
            HSSFCell cell = sheet.getRow(28).getCell(i);
            cell.setCellValue(colloctMoney);
        }
        //??????
        colloctMoney = getCollectByDeptId(fiveBusinessAdvances,67);
        for(int i=1;i<=3;i++){
            HSSFCell cell = sheet.getRow(29).getCell(i);
            cell.setCellValue(colloctMoney);
        }


        ByteArrayOutputStream os = new ByteArrayOutputStream();
        workbook.write(os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        IOUtils.toByteArray(is);
        return workbook;
    }
    //??????????????????????????? ?????????  ???????????? ???????????????
    private Double getCollectByDeptId(List<FiveBusinessAdvance> fiveBusinessAdvances, int deptId) {
        for(FiveBusinessAdvance advance:fiveBusinessAdvances){
            if(advance.getDeptId().equals(deptId)){
                return Double.valueOf(MyStringUtil.getMoneyW(advance.getTotalPrice()));
            }
        }
        return 0.0;
    }


    //Map("deptName",list)
    public List<Map> downData(int collectId,String userLogin) {
        List<Map> list = Lists.newArrayList();

        FiveBusinessAdvanceCollectDto advanceCollectDto = getModelById(collectId);
        Map params = new HashMap();
        params.put("deleted",false);
        params.put("processEnd",true);
        if(advanceCollectDto.getDeclareType().equals("??????????????????")){
            params.put("month",advanceCollectDto.getCollectMonth().trim());
            params.put("declareType","??????????????????");
        }else if(advanceCollectDto.getDeclareType().equals("?????????")){
            params.put("searchYear",advanceCollectDto.getCollectMonth().split("-")[0]);
            params.put("declareType","?????????");
        }else{
            params.put("month",advanceCollectDto.getCollectMonth().trim());
            params.put("declareType","??????");
        }
        List<FiveBusinessAdvance> fiveBusinessAdvances = fiveBusinessAdvanceMapper.selectAll(params);

        for(FiveBusinessAdvance fiveBusinessAdvance :fiveBusinessAdvances){
            //???????????? ????????????????????????
            boolean flag =false;
            for(Map map:list){
                if(map.getOrDefault("deptName","").equals(fiveBusinessAdvance.getDeptName())){
                    flag=true;
                }
            }
            if(flag){
                break;
            }
            HashMap data = Maps.newHashMap();
            data.put("deptName",fiveBusinessAdvance.getDeptName());
            List<FiveBusinessAdvanceDetail> details = fiveBusinessAdvanceSevice.listDetail(fiveBusinessAdvance.getId());
            List<Map> detailLists = new ArrayList<>();
            for(FiveBusinessAdvanceDetail detail:details){
                Map map=new LinkedHashMap();
                map.put("????????????",detail.getPersonNo());
                map.put("??????",detail.getPersonName());
                map.put("??????",detail.getDeptName());
                map.put("????????????",detail.getPersonnelCategory());
                map.put("??????",detail.getProjectBonus() );
                map.put("??????",detail.getRemark() );
                detailLists.add(map);
            }
            data.put("list",detailLists);
            list.add(data);
        }

        return list;
    }


    //detail
    public void getNewModelDetail(int collectId){
        Map<String,String> map = new LinkedHashMap<>();
        map.put("75","??????");
        map.put("74","??????");
        map.put("76","?????????");
        map.put("47","?????????");
        map.put("34","????????????");
        map.put("45","????????????");
        map.put("63","???????????????????????????");
        map.put("20","???????????????");
        map.put("53","???????????????");
        map.put("7","???????????????");
        map.put("36","???????????????");
        map.put("12","????????????");
        map.put("13","?????????");
        map.put("50","??????????????????");
        map.put("59","???????????????");
        map.put("72","???????????????");
        map.put("48","???????????????");
        map.put("11","???????????????????????????");
        map.put("101","???????????????");
        map.put("58","???????????????");
        map.put("18","???????????????");
        map.put("38","???????????????");
        map.put("29","???????????????");
        map.put("9","???????????????????????????");
        map.put("67","???????????????");
        FiveBusinessAdvanceCollectDetail item = new FiveBusinessAdvanceCollectDetail();
        FiveBusinessAdvanceCollectDto advanceCollectDto = getModelById(collectId);
        Iterator<Map.Entry<String,String>> it = map.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String,String> entry = it.next();
            item.setDeptName(entry.getValue());
            item.setDeptId(entry.getKey());
            item.setCollectId(advanceCollectDto.getId());
            item.setDeleted(false);
            item.setGmtModified(new Date());
            item.setGmtCreate(new Date());
            item.setApplyMoney("0");
            item.setCompanyMoney("0");
            item.setRealMoney("0");
            item.setDeclareType("??????????????????");
            fiveBusinessAdvanceCollectDetailMapper.insert(item);
        }


    }

    public FiveBusinessAdvanceCollectDetail getModelDetailById(int collectId){
        FiveBusinessAdvanceCollectDetail detail = fiveBusinessAdvanceCollectDetailMapper.selectByPrimaryKey(collectId);
        detail.setRealMoney(MyStringUtil.moneyToString(detail.getRealMoney(),6));
        detail.setApplyMoney(MyStringUtil.moneyToString(detail.getApplyMoney(),6));
        detail.setCompanyMoney(MyStringUtil.moneyToString(detail.getCompanyMoney(),6));
        return detail;
    }

    public void removeDetail(int id){
        FiveBusinessAdvanceCollectDetail model = fiveBusinessAdvanceCollectDetailMapper.selectByPrimaryKey(id);
        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveBusinessAdvanceCollectDetailMapper.updateByPrimaryKey(model);
    }

    public void updateDetails(FiveBusinessAdvanceCollectDto item){
        List<FiveBusinessAdvanceCollectDetail> list = listDetail(item.getId());
        Map params = new HashMap();
        params.put("deleted",false);
        params.put("processEnd",true);
        if(item.getDeclareType().equals("??????????????????")){
            params.put("month",item.getCollectMonth().trim());
            params.put("declareType","??????????????????");
        }else if(item.getDeclareType().equals("?????????")){
            params.put("searchYear",item.getCollectMonth().split("-")[0]);
            params.put("declareType","?????????");
        }else{
            params.put("month",item.getCollectMonth().trim());
            params.put("declareType","??????");
        }

        List<FiveBusinessAdvance> fiveBusinessAdvances = fiveBusinessAdvanceMapper.selectAll(params);
        for(FiveBusinessAdvanceCollectDetail each:list){
            each.setDeclareType(String.valueOf(params.get("declareType")));
            int deptId = Integer.parseInt(each.getDeptId());
            each.setRealMoney(MyStringUtil.moneyToString(String.valueOf(getCollectByDeptId(fiveBusinessAdvances,deptId)),8));
            each.setApplyMoney(MyStringUtil.moneyToString(String.valueOf(getCollectByDeptId(fiveBusinessAdvances,deptId)),8));
            each.setCompanyMoney(MyStringUtil.moneyToString(String.valueOf(getCollectByDeptId(fiveBusinessAdvances,deptId)),8));
            fiveBusinessAdvanceCollectDetailMapper.updateByPrimaryKey(each);
        };
    }

    public void updateDetail(FiveBusinessAdvanceCollectDetail detail){
        FiveBusinessAdvanceCollectDetail collectDetail = fiveBusinessAdvanceCollectDetailMapper.selectByPrimaryKey(detail.getId());
        collectDetail.setCompanyMoney(detail.getCompanyMoney());
        collectDetail.setApplyMoney(detail.getApplyMoney());
        collectDetail.setRealMoney(detail.getRealMoney());
        fiveBusinessAdvanceCollectDetailMapper.updateByPrimaryKey(collectDetail);
    }

    public List<FiveBusinessAdvanceCollectDetail> listDetail(int collectId){
        FiveBusinessAdvanceCollectDto advanceCollectDto = getModelById(collectId);
        Map <String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("collectId",collectId);//??????
        List<FiveBusinessAdvanceCollectDetail> list = fiveBusinessAdvanceCollectDetailMapper.selectAll(params).stream()
                .sorted(Comparator.comparing(FiveBusinessAdvanceCollectDetail::getId)).collect(Collectors.toList());
        for (FiveBusinessAdvanceCollectDetail each:list) {
            each.setRealMoney(MyStringUtil.moneyToString(each.getRealMoney(),6));
            each.setApplyMoney(MyStringUtil.moneyToString(each.getApplyMoney(),6));
            each.setCompanyMoney(MyStringUtil.moneyToString(each.getCompanyMoney(),6));
        }
        return list;
    }

    /**
     * ??????EXCL ???????????? ????????????
     * @param data
     * @param collectId
     * @param userLogin
     * @throws ParseException
     */
    @Transactional
    public void uploadExcl(List<Map> data,int collectId,String userLogin) throws ParseException {
        String type = "";
        if (data.get(0).get(0).toString().contains("?????????")){
            type = "?????????";
        }else if (data.get(0).get(0).toString().contains("??????")){
            type = "??????????????????";
        }
        Assert.state(data.size() > 1,"???????????????????????????????????????????????????????????????!");
        Date now = new Date();
        //Assert.state(data.get(0).size()>=9,"??????????????????12???(?????????????????????????????????)!");
        FiveBusinessAdvanceCollect model = fiveBusinessAdvanceCollectMapper.selectByPrimaryKey(collectId);
        model.setDeclareType(type);
        model.setGmtModified(new Date());
        fiveBusinessAdvanceCollectMapper.updateByPrimaryKey(model);
        if (data.size() > 0) {
            for (int i = 3; i < data.size(); i++) {
                try {
                    Map map = data.get(i);
                    if (map.get(0).toString().contains("??????")){
                        continue;
                    }else {
                        Map <String,Object> params=Maps.newHashMap();
                        params.put("collectId",collectId);
                        params.put("deptName",map.get(0).toString());
                        List<FiveBusinessAdvanceCollectDetail> list=fiveBusinessAdvanceCollectDetailMapper.selectAll(params);
                        if (list !=null && list.size()>0){
                            FiveBusinessAdvanceCollectDetail detail = list.get(0);
                            detail.setDeptName(map.get(0).toString());
                            String applyMoney= MyStringUtil.moneyToString(map.getOrDefault(1,0.000000).toString());
                            String companyMoney= MyStringUtil.moneyToString(map.getOrDefault(2,0.000000).toString());
                            String realMoney= MyStringUtil.moneyToString(map.getOrDefault(3,0.000000).toString());

                            detail.setApplyMoney(applyMoney);
                            detail.setCompanyMoney(companyMoney);
                            detail.setRealMoney(realMoney);
                            detail.setDeleted(false);
                            detail.setGmtModified(new Date());
                            detail.setCreator(userLogin);
                            detail.setDeclareType(type);
                            Map <String,Object> params2=Maps.newHashMap();
                            detail.setCreatorName(selectEmployeeService.getNameByUserLogin(userLogin));
                            fiveBusinessAdvanceCollectDetailMapper.updateByPrimaryKey(detail);
                        }
                    }
                }catch (Exception e){
                    Assert.state(true,"?????????????????????????????????");
                }

            }
        }
    }

}
