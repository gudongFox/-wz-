package com.cmcu.mcc.oa.service;

import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.dto.ProcessInstanceDto;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.oa.dao.OaCarApplyMapper;
import com.cmcu.mcc.oa.dao.OaCarMapper;
import com.cmcu.mcc.oa.dto.OaCarApplyDto;
import com.cmcu.mcc.oa.entity.OaCar;
import com.cmcu.mcc.oa.entity.OaCarApply;
import com.cmcu.mcc.service.ActService;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OaCarApplyService {

    @Resource
    OaCarApplyMapper oaCarApplyMapper;

    @Autowired
    OaCarService oaCarService;

    @Autowired
    SelectEmployeeService selectEmployeeService;

    @Autowired
    ActService actService;

    @Autowired
    OaCarMapper oaCarMapper;

    @Resource
    TaskHandleService taskHandleService;

    public void remove(int id,String userLogin) {
        OaCarApply item = oaCarApplyMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin),"只允许创建人删除！");
        if (StringUtils.isNotEmpty(item.getProcessInstanceId())) {
            Assert.state(actService.checkProcessOnUser(item.getProcessInstanceId(), userLogin), "任务在其他环节处理中,无法删除!");
        }
        if (StringUtils.isNotEmpty(item.getProcessInstanceId())) {
            actService.delete(item.getProcessInstanceId(),"",userLogin);
        }
        item.setGmtModified(new Date());
        item.setDeleted(true);
        oaCarApplyMapper.updateByPrimaryKey(item);
    }

    public void update(OaCarApplyDto oaCarApplyDto) throws ParseException {
        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date beginTime = simpleDateFormat.parse(oaCarApplyDto.getBeginTime());
        Date endTime = simpleDateFormat.parse(oaCarApplyDto.getEndTime());

        //判断该车辆 时间段是否使用
        String existTime = checkTimeIsAvailable(oaCarApplyDto.getId(),oaCarApplyDto.getCarId(),beginTime,endTime);
        Assert.state(existTime.equalsIgnoreCase(""), existTime+"该时间段已被占用!");


        int id = oaCarApplyDto.getId();
        int carId = oaCarApplyDto.getCarId();
        Map params = new HashMap();
        params.put("deleted",false);
        params.put("beginTime",beginTime);
        params.put("endTime",endTime);
        params.put("id",id);
        params.put("carId",carId);
        List<OaCarApply> list = oaCarApplyMapper.selectAll(params);
        Assert.state(list.size()==0, "当前时间段已被申请!");
        OaCarApply item = oaCarApplyMapper.selectByPrimaryKey(oaCarApplyDto.getId());
        item.setDeptName(oaCarApplyDto.getDeptName());
        item.setCarId(oaCarApplyDto.getCarId());
        item.setCarInfo(oaCarApplyDto.getCarInfo());
        item.setApplyReason(oaCarApplyDto.getApplyReason());
        item.setBeginTime(oaCarApplyDto.getBeginTime());
        item.setEndTime(oaCarApplyDto.getEndTime());
        item.setGmtModified(new Date());
        item.setRemark(oaCarApplyDto.getRemark());
        BeanValidator.check(item);
        oaCarApplyMapper.updateByPrimaryKey(item);
    }

    private String checkTimeIsAvailable(int id,int carId,Date beginTime, Date endTime) throws ParseException {
        String existTime = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        //查询该车辆的所有申请
        Map params = new HashMap();
        params.put("deleted",false);
        params.put("carId",carId);
        List<OaCarApply> oaCarApplys = oaCarApplyMapper.selectAll(params);

        for(OaCarApply item :oaCarApplys){
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

    public OaCarApplyDto getDto(OaCarApply item)
    {
        OaCarApplyDto oaCarApplyDto = OaCarApplyDto.adapt(item);
        if(item.getCarId()!=0){
            oaCarApplyDto.setCarName(oaCarService.getModelById(item.getCarId()).getCarNo());
        }
        ProcessInstanceDto processInstanceDto = actService.getProcessInstanceDto(item.getProcessInstanceId());
        oaCarApplyDto.setProcessName(processInstanceDto.getProcessName());
        oaCarApplyDto.setBusinessKey(processInstanceDto.getBusinessKey());
        if(!item.getProcessEnd()&&processInstanceDto.isProcessEnd()){
            item.setProcessEnd(true);
            oaCarApplyMapper.updateByPrimaryKey(item);
        }
        return oaCarApplyDto;
    }

    public OaCarApplyDto getModelById(int id)
    {
        return getDto(oaCarApplyMapper.selectByPrimaryKey(id));
    }

    public int getNewModel(String userLogin)      //新增一条数据
    {
        HrEmployeeDto hrEmployeeDto = selectEmployeeService.selectByUserLogin(userLogin);
        OaCarApply item = new OaCarApply();
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeptName(hrEmployeeDto.getDeptName());
//        item.setBeginTime(new Date());
//        item.setEndTime(new Date());
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setProcessEnd(false);
        ModelUtil.setNotNullFields(item);
        oaCarApplyMapper.insert(item);
        Map variables = Maps.newHashMap();
        variables.put("userLogin",userLogin);
        variables.put("description","车辆申请"+item.getCreatorName());
        variables.put("backOffices",selectEmployeeService.getUserListByRoleName("办公室后勤管理岗位"));
        String processInstanceId = taskHandleService.startProcess(EdConst.OA_CAR_APPLY,EdConst.OA_CAR_APPLY+'_'+item.getId(),variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        oaCarApplyMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params,String userLogin,int pageNum,int pageSize)
    {
        params.put("deleted",false);
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum,pageSize).doSelectPageInfo(()->oaCarApplyMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p ->{
            OaCarApply item = (OaCarApply) p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }


    public HSSFWorkbook downloadTotalExcel() throws IOException {
        Map params = new HashMap();
        params.put("deleted",false);
        List<OaCarApply> oaCarApplies = oaCarApplyMapper.selectAll(params);

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
        for (OaCarApply carApply : oaCarApplies) {
            //车辆基本信息
            OaCar oaCar = oaCarMapper.selectByPrimaryKey(carApply.getCarId());

            HSSFRow row = sheet.createRow(rowIndex++);
            HSSFCell cell = row.createCell(0);
            cell.setCellValue(oaCar.getCarNo());
            cell.setCellStyle(cellStyle);
            HSSFCell cell1 = row.createCell(1);
            cell1.setCellValue(oaCar.getCarColor());
            cell1.setCellStyle(cellStyle);
            HSSFCell cell2 = row.createCell(2);
            cell2.setCellValue(oaCar.getCarBrand());
            cell2.setCellStyle(cellStyle);
            HSSFCell cell3 = row.createCell(3);
            cell3.setCellValue(oaCar.getCarCc());
            cell3.setCellStyle(cellStyle);
            HSSFCell cell4 = row.createCell(4);
            cell4.setCellValue(oaCar.getCarPrice().toString());
            cell4.setCellStyle(cellStyle);
            HSSFCell cell5 = row.createCell(5);
            cell5.setCellValue(oaCar.getCarType());
            cell5.setCellStyle(cellStyle);
            HSSFCell cell6 = row.createCell(6);
            cell6.setCellValue(oaCar.getBuyDate());
            cell6.setCellStyle(cellStyle);

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


}
