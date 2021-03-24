package com.cmcu.mcc.five.service;

import com.cmcu.act.dto.CustomProcessInstance;
import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.dto.ActHistoryDto;
import com.cmcu.mcc.five.dao.FiveOaCarApplyMapper;
import com.cmcu.mcc.five.dao.FiveOaCarMapper;
import com.cmcu.mcc.five.dto.FiveOaCarApplyDto;
import com.cmcu.mcc.five.entity.FiveOaCar;
import com.cmcu.mcc.five.entity.FiveOaCarApply;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.service.ActService;
import com.cmcu.mcc.service.impl.HandleFormService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class FiveOaCarApplyService {

    @Resource
    FiveOaCarApplyMapper fiveOaCarApplyMapper;

    @Autowired
    FiveOaCarService fiveOaCarService;

    @Autowired
    SelectEmployeeService selectEmployeeService;

    @Autowired
    ActService actService;

    @Autowired
    FiveOaCarMapper fiveOaCarMapper;

    @Resource
    TaskHandleService taskHandleService;
    @Autowired
    CommonCodeService commonCodeService;
    @Autowired
    MyActService myActService;
    @Autowired
   ProcessQueryService processQueryService;
    @Autowired
    HrEmployeeSysService hrEmployeeSysService;
    @Resource
    HandleFormService handleFormService;

    public void remove(int id,String userLogin) {
        FiveOaCarApply item = fiveOaCarApplyMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin),"只允许创建人删除！");
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    public void update(FiveOaCarApplyDto model) throws ParseException {
        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date beginTime = simpleDateFormat.parse(model.getBeginTime());
        Date endTime = simpleDateFormat.parse(model.getEndTime());
/*        Assert.state(!endTime.before(beginTime), "开始时间小于结束时间!");
        Assert.state(now.before(beginTime), "申请时间不能小于当前时间!");*/
        if(model.getApplyType().equalsIgnoreCase("用车赴外地")){
            Assert.state(Strings.isNullOrEmpty(model.getDriver()), "用车赴外地 司机不能为空!");
            Assert.state(Strings.isNullOrEmpty(model.getPassenger()), "用车赴外地 乘客不能为空!");
        }

        //判断该车辆 时间段是否使用
        FiveOaCarApply item = fiveOaCarApplyMapper.selectByPrimaryKey(model.getId());
        FiveOaCar fiveOaCar = fiveOaCarMapper.selectByPrimaryKey(item.getCarId());
        item.setDeptId(model.getDeptId());
        item.setDeptName(model.getDeptName());
        item.setCarId(model.getCarId());
        item.setCarInfo(model.getCarInfo());
        item.setApplyReason(model.getApplyReason());
        item.setBeginTime(model.getBeginTime());
        item.setEndTime(model.getEndTime());
        item.setGmtModified(new Date());
        item.setApplyType(model.getApplyType());
        item.setDestination(model.getDestination());
        item.setMileage(model.getMileage());
        item.setUserNum(model.getUserNum());
        item.setDriver(model.getDriver());
        item.setDriverName(model.getDriverName());
        item.setPassenger(model.getPassenger());
        item.setPartPay(model.getPartPay());
        item.setPassPay(model.getPassPay());
        item.setSoilPay(model.getSoilPay());
        item.setSelfDrive(model.getSelfDrive());
        item.setCarName(model.getCarName());

        item.setRemark(model.getRemark());
        model.setGmtModified(new Date());
        BeanValidator.check(item);
        fiveOaCarApplyMapper.updateByPrimaryKey(item);

        Map variables = Maps.newHashMap();
        if (item.getDeptId()!=0) {
            variables.put("deptChargeMen", selectEmployeeService.getDeptChargeMen(model.getDeptId()));
        }
        if (item.getDeptId()!=0) {
            variables.put("otherDeptChargeMen", selectEmployeeService.getOtherDeptChargeMan(model.getDeptId()));
        }
        variables.put("flag", item.getApplyType());
        List<String> copyMan = new ArrayList<>();
        //公司办用车管理人：武彦宁
        copyMan.add("2802");
        //各单位车辆管理人：
        if(fiveOaCar!=null&&!Strings.isNullOrEmpty(fiveOaCar.getDeptName())){
            if(fiveOaCar.getDeptName().contains("二院")) {
                copyMan.add("2135");
            }else if(fiveOaCar.getDeptName().contains("钢结构")) {
                copyMan.add("9311");
            }else if(fiveOaCar.getDeptName().contains("环境能源")) {
                copyMan.add("2722");
            }else if(fiveOaCar.getDeptName().contains("石油")) {
                copyMan.add("2898");
            } else if(fiveOaCar.getDeptName().contains("五环")) {
                copyMan.add("1364");
            } else if(fiveOaCar.getDeptName().contains("五特")) {
                copyMan.add("9523");
            }
        }
        //公司办用车管理人：武彦宁
        List<String> companyCarChargeMen =selectEmployeeService.getUserListByRoleName("公司办用车管理人");
        variables.put("carAdminMan",copyMan);
        variables.put("companyCarChargeMen", companyCarChargeMen);
        if(fiveOaCar!=null&&!Strings.isNullOrEmpty(fiveOaCar.getDeptName())){
            variables.put("otherDeptChargeMen", selectEmployeeService.getDeptChargeMen(fiveOaCar.getDeptId()));//车辆单位负责人
        }
        if (item.getSelfDrive()){//自驾  发起人当司机
            variables.put("driver",item.getCreator());
        }else {
            variables.put("driver",item.getDriver());
        }

        myActService.setVariables(model.getProcessInstanceId(),variables);
    }

    private String checkTimeIsAvailable(int id,int carId,Date beginTime, Date endTime) throws ParseException {
        String existTime = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        //查询该车辆的所有申请
        Map params = new HashMap();
        params.put("deleted",false);
        params.put("carId",carId);
        List<FiveOaCarApply> fiveOaCarApplys = fiveOaCarApplyMapper.selectAll(params);

        for(FiveOaCarApply item :fiveOaCarApplys){
            //排除修改本来数据
            if(!item.getId().equals(id)){
                //判断 开始时间 是否落在 已申请时间的 区间内 有则返回 冲突时间段
                if(beginTime.after(simpleDateFormat.parse(item.getBeginTime()))&&beginTime.before(simpleDateFormat.parse(item.getEndTime()))){
                    existTime = item.getBeginTime()+" - "+item.getEndTime();
                    break;
                }
                if(endTime.after(simpleDateFormat.parse(item.getBeginTime()))&&beginTime.before(simpleDateFormat.parse(item.getEndTime()))){
                    existTime = item.getBeginTime()+" - "+item.getEndTime();
                    break;
                }
            }
        }
        return existTime;
    }

    public FiveOaCarApplyDto getDto(FiveOaCarApply item) {
        FiveOaCarApplyDto fiveOaCarApplyDto = FiveOaCarApplyDto.adapt(item);
        fiveOaCarApplyDto.setProcessName("已完成");
        if(item.getCarId()!=0){
            fiveOaCarApplyDto.setCarName(fiveOaCarService.getModelById(item.getCarId()).getCarNo());
        }
        if (!fiveOaCarApplyDto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(item.getProcessInstanceId(), "", "");

            if(customProcessInstance == null || customProcessInstance.isFinished()){
                item.setProcessEnd(true);
                fiveOaCarApplyMapper.updateByPrimaryKey(item);
            }
            if (customProcessInstance != null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())) {
                fiveOaCarApplyDto.setProcessName(customProcessInstance.getCurrentTaskName());
                fiveOaCarApplyDto.setBusinessKey(customProcessInstance.getInstance().getBusinessKey());
            }

        }

        //fiveOaCarApplyDto.setAttendUser(myHistoryService.getAttendUser(fiveOaCarApplyDto.getProcessInstanceId()));
        return fiveOaCarApplyDto;
    }

    public FiveOaCarApplyDto getModelById(int id) {
        return getDto(fiveOaCarApplyMapper.selectByPrimaryKey(id));
    }

    public int getNewModel(String userLogin,String type){
        HrEmployeeDto hrEmployeeDto = selectEmployeeService.selectByUserLogin(userLogin);
        FiveOaCarApply item = new FiveOaCarApply();
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        item.setBeginTime(sdf.format(new Date()));
        item.setEndTime(sdf.format(new Date()));
        item.setApplyType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"用车类别").toString());
        item.setApplyReason(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"用车事项").toString());
        item.setType(type);
        item.setGmtCreate(new Date());
        item.setSelfDrive(false);
        item.setGmtModified(new Date());
        item.setProcessEnd(false);
        ModelUtil.setNotNullFields(item);
        fiveOaCarApplyMapper.insert(item);
        Map variables = Maps.newHashMap();
        variables.put("userLogin",userLogin);

        variables.put("deptChargeMen", selectEmployeeService.getDeptChargeMen(hrEmployeeDto.getDeptId()));//申请人 上级领导
        String businessKey="";
        String processInstanceId="";
        if ("self".equals(type)){
            businessKey=EdConst.FIVE_OA_SELF_CAR_APPLY+'_'+item.getId();
            variables.put("processDescription","个人车辆申请"+item.getCreatorName());
            processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_SELF_CAR_APPLY,businessKey ,variables, MccConst.APP_CODE);
        }else {
            businessKey=EdConst.FIVE_OA_LEADER_CAR_APPLY+'_'+item.getId();
            variables.put("processDescription","领导车辆申请"+item.getCreatorName());
            processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_LEADER_CAR_APPLY,businessKey ,variables, MccConst.APP_CODE);
        }

        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);
        fiveOaCarApplyMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
         List<Integer> deptIdList=hrEmployeeSysService.getMyDeptListOa(userLogin,uiSref);
         if (deptIdList.size()==0){
            params.put("creator",userLogin);
        }else {
            params.put("deptIdList",deptIdList);
        }
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaCarApplyMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaCarApply item=(FiveOaCarApply)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }


    public HSSFWorkbook downloadTotalExcel() throws IOException {
        Map params = new HashMap();
        params.put("deleted",false);
        List<FiveOaCarApply> fiveOaCarApplies = fiveOaCarApplyMapper.selectAll(params);

        String classpath = this.getClass().getResource("/").getPath().replaceFirst("/", "");
        String webappRoot = classpath.replaceAll("WEB-INF/classes/", "");
        String filePath = webappRoot + "assets/doc/导出模板/车辆申请导出模板.xls";
        //得到模板文件
        InputStream in = new FileInputStream(filePath);

        HSSFWorkbook workbook = new HSSFWorkbook(in);
        /* workbook.getSheet(0).getRow(0).getCell()*/
        HSSFSheet sheet = workbook.getSheetAt(0);
        //模板cell样式
        HSSFCellStyle cellStyle = sheet.getRow(2).getCell(1).getCellStyle();

        int rowIndex = 3;//起始行
        for (FiveOaCarApply carApply : fiveOaCarApplies) {
            HSSFRow row = sheet.createRow(rowIndex++);
            if(carApply.getCarId()!=0){
                //车辆基本信息
                FiveOaCar fiveOaCar = fiveOaCarMapper.selectByPrimaryKey(carApply.getCarId());
                HSSFCell cell = row.createCell(0);
                cell.setCellValue(fiveOaCar.getCarNo());
                cell.setCellStyle(cellStyle);
                HSSFCell cell1 = row.createCell(1);
                cell1.setCellValue(fiveOaCar.getCarColor());
                cell1.setCellStyle(cellStyle);
                HSSFCell cell2 = row.createCell(2);
                cell2.setCellValue(fiveOaCar.getCarBrand());
                cell2.setCellStyle(cellStyle);
                HSSFCell cell3 = row.createCell(3);
                cell3.setCellValue(fiveOaCar.getCarCc());
                cell3.setCellStyle(cellStyle);
                HSSFCell cell4 = row.createCell(4);
                cell4.setCellValue(fiveOaCar.getCarPrice().toString());
                cell4.setCellStyle(cellStyle);
                HSSFCell cell5 = row.createCell(5);
                cell5.setCellValue(fiveOaCar.getCarType());
                cell5.setCellStyle(cellStyle);
                HSSFCell cell6 = row.createCell(6);
                cell6.setCellValue(fiveOaCar.getBuyDate());
                cell6.setCellStyle(cellStyle);
            }


            HSSFCell cell7 = row.createCell(7);
            cell7.setCellValue(carApply.getDeptName());
            cell7.setCellStyle(cellStyle);
            HSSFCell cell8 = row.createCell(8);
            cell8.setCellValue(carApply.getApplyReason());
            cell8.setCellStyle(cellStyle);
            HSSFCell cell9 = row.createCell(9);
            cell9.setCellValue(carApply.getBeginTime());
            cell9.setCellStyle(cellStyle);
            HSSFCell cell10 = row.createCell(10);
            cell10.setCellValue(carApply.getEndTime());
            cell10.setCellStyle(cellStyle);
            HSSFCell cell11 = row.createCell(11);
            cell11.setCellValue(carApply.getCarInfo());
            cell11.setCellStyle(cellStyle);
            HSSFCell cell12 = row.createCell(12);
            cell12.setCellValue(getDto(carApply).getProcessName());
            cell12.setCellStyle(cellStyle);
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        workbook.write(os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        IOUtils.toByteArray(is);
        return workbook;
    }

    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaCarApply item = fiveOaCarApplyMapper.selectByPrimaryKey(id);
        FiveOaCarApplyDto dtoo = getDto(item);
        data.put("beginTime",item.getBeginTime());
        data.put("endTime",item.getEndTime());
        data.put("carName",dtoo.getCarName());
        data.put("deptName",item.getDeptName());
        data.put("applyType",item.getApplyType());
        data.put("applyReason",item.getApplyReason());
        data.put("driverName",item.getDriverName());
        data.put("destination",item.getDestination());

        data.put("passenger",item.getPassenger());
        data.put("userNum",item.getUserNum());
        data.put("carInfo",item.getCarInfo());
        data.put("remark",item.getRemark());
        data.put("mileage",item.getMileage());
        data.put("soilPay",item.getSoilPay());
        data.put("passPay",item.getPassPay());
        data.put("partPay",item.getPartPay());

        data.put("creatorName",item.getCreatorName());
        data.put("gmtCreate",item.getGmtCreate());

        List<ActHistoryDto> actHistoryDtos = actService.listPassedHistoryDto(item.getProcessInstanceId());
        for (ActHistoryDto dto:actHistoryDtos){
            if (dto.getActivityName()==null){
                break;
            }
            if ("申请人".equals(dto.getActivityName())){
                data.put("startMen",dto);
            }
            if ("直接上级(部门领导)".equals(dto.getActivityName())){
                data.put("deptMen",dto);
            }
            if ("单位负责人".equals(dto.getActivityName())){
                data.put("deptChargeMen",dto);
            }
            if ("公司办负责人".equals(dto.getActivityName())){
                data.put("companyChargeMen",dto);
            }
        }
        data.put("nodes",actHistoryDtos);

        return data;
    }
}
