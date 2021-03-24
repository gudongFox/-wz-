package com.cmcu.mcc.five.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.dto.ActHistoryDto;
import com.cmcu.mcc.dto.ProcessInstanceDto;
import com.cmcu.mcc.five.dao.FiveOaCarMaintainMapper;
import com.cmcu.mcc.five.dao.FiveOaCarMapper;
import com.cmcu.mcc.five.dto.FiveOaCarMaintainDto;
import com.cmcu.mcc.five.entity.FiveOaCar;
import com.cmcu.mcc.five.entity.FiveOaCarMaintain;
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
public class FiveOaCarMaintainService {
    @Resource
    FiveOaCarMaintainMapper fiveOaCarMaintainMapper;
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
        FiveOaCarMaintain item = fiveOaCarMaintainMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin),"只允许创建人删除！");

        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    public void update(FiveOaCarMaintainDto model) throws ParseException {
        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");


        //判断该车辆 时间段是否使用
        FiveOaCarMaintain item = fiveOaCarMaintainMapper.selectByPrimaryKey(model.getId());
        item.setDeptId(model.getDeptId());
        item.setDeptName(model.getDeptName());
        item.setCarId(model.getCarId());
        item.setSoilTime(model.getSoilTime());
        item.setSoilMoney(model.getSoilMoney());
        item.setUpkeepCourse(model.getUpkeepCourse());
        item.setUpkeepTime(model.getUpkeepTime());
        item.setUpkeepFactory(model.getUpkeepFactory());
        item.setUpkeepMoney(model.getUpkeepMoney());
        item.setUpkeepInvoiceNo(model.getUpkeepInvoiceNo());
        item.setUpkeepInvoiceMoney(model.getUpkeepInvoiceMoney());
        item.setCheckTime(model.getCheckTime());
        item.setCheckMoney(model.getCheckMoney());
        item.setCheckAddress(model.getCheckAddress());
        item.setEtcMoney(model.getEtcMoney());
        item.setEtcTime(model.getEtcTime());
        item.setOtherType(model.getOtherType());
        item.setOtherMoney(model.getOtherMoney());
        item.setCarNo(model.getCarNo());
        item.setType(model.getType());
        item.setGmtModified(new Date());
        item.setRemark(model.getRemark());
        //BeanValidator.check(item);
        fiveOaCarMaintainMapper.updateByPrimaryKey(item);

        Map variables = new HashMap();

        myActService.setVariables(model.getProcessInstanceId(),variables);
    }


    public FiveOaCarMaintainDto getDto(FiveOaCarMaintain item) {
        FiveOaCarMaintainDto dto = FiveOaCarMaintainDto.adapt(item);
        dto.setProcessName("已完成");
        if(item.getCarId()!=0){
            dto.setCarName(fiveOaCarService.getModelById(item.getCarId()).getCarNo());
        }
        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if(customProcessInstance == null || customProcessInstance.isFinished()){
                item.setProcessEnd(true);
                fiveOaCarMaintainMapper.updateByPrimaryKey(item);
            }
            if (customProcessInstance!=null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }

        return dto;
    }

    public FiveOaCarMaintainDto getModelById(int id) {
        return getDto(fiveOaCarMaintainMapper.selectByPrimaryKey(id));
    }

    public int getNewModel(String userLogin){
        HrEmployeeDto hrEmployeeDto = selectEmployeeService.selectByUserLogin(userLogin);
        FiveOaCarMaintain item = new FiveOaCarMaintain();
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setProcessEnd(false);
        ModelUtil.setNotNullFields(item);
        fiveOaCarMaintainMapper.insert(item);
        Map variables = Maps.newHashMap();
        variables.put("userLogin",userLogin);
        variables.put("processDescription","车辆申请"+item.getCreatorName());
        variables.put("deptChargeMen", selectEmployeeService.getDeptChargeMen(hrEmployeeDto.getDeptId()));//申请人 上级领导
        String processInstanceId = taskHandleService.startProcess(EdConst.OA_CAR_MAINTAIN, EdConst.OA_CAR_MAINTAIN+'_'+item.getId(),variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(EdConst.OA_CAR_MAINTAIN+'_'+item.getId());
        fiveOaCarMaintainMapper.updateByPrimaryKey(item);
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
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaCarMaintainMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaCarMaintain item=(FiveOaCarMaintain)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }


    public HSSFWorkbook downloadTotalExcel() throws IOException {
        Map params = new HashMap();
        params.put("deleted",false);
        List<FiveOaCarMaintain> fiveOaCarApplies = fiveOaCarMaintainMapper.selectAll(params);

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
        for (FiveOaCarMaintain carMaintain : fiveOaCarApplies) {
            HSSFRow row = sheet.createRow(rowIndex++);
            if(carMaintain.getCarId()!=0){
                //车辆基本信息
                FiveOaCar fiveOaCar = fiveOaCarMapper.selectByPrimaryKey(carMaintain.getCarId());
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
            cell7.setCellValue(carMaintain.getDeptName());
            cell7.setCellStyle(cellStyle);
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        workbook.write(os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        IOUtils.toByteArray(is);
        return workbook;
    }

    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaCarMaintain item = fiveOaCarMaintainMapper.selectByPrimaryKey(id);
        FiveOaCarMaintainDto dtoo = getDto(item);


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
