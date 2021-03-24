package com.cmcu.mcc.oa.service;

import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.dto.ProcessInstanceDto;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.oa.dao.OaMeetingRoomApplyMapper;
import com.cmcu.mcc.oa.dao.OaMeetingRoomMapper;
import com.cmcu.mcc.oa.dto.OaMeetingRoomApplyDto;
import com.cmcu.mcc.oa.entity.OaMeetingRoom;
import com.cmcu.mcc.oa.entity.OaMeetingRoomApply;
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
public class OaMeetingRoomApplyService {

    @Resource
    OaMeetingRoomApplyMapper oaMeetingRoomApplyMapper;

    @Autowired
    OaMeetingRoomService oaMeetingRoomService;

    @Autowired
    SelectEmployeeService selectEmployeeService;

    @Autowired
    ActService actService;
    @Autowired
    OaMeetingRoomMapper oaMeetingRoomMapper;

    @Resource
    TaskHandleService taskHandleService;



    public void remove(int id,String userLogin)
    {
        OaMeetingRoomApply item = oaMeetingRoomApplyMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin),"只允许创建人删除!");
        if (StringUtils.isNotEmpty(item.getProcessInstanceId())) {
            Assert.state(actService.checkProcessOnUser(item.getProcessInstanceId(), userLogin), "任务在其他环节处理中,无法删除!");
        }
        if (StringUtils.isNotEmpty(item.getProcessInstanceId())) {
            actService.delete(item.getProcessInstanceId(),"",userLogin);
        }
        item.setGmtModified(new Date());
        item.setDeleted(true);
        oaMeetingRoomApplyMapper.updateByPrimaryKey(item);
    }

    public void update(OaMeetingRoomApplyDto oaMeetingRoomApplyDto) throws ParseException {
        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date beginTime = simpleDateFormat.parse(oaMeetingRoomApplyDto.getBeginTime());
        Date endTime = simpleDateFormat.parse(oaMeetingRoomApplyDto.getEndTime());
        Assert.state(oaMeetingRoomApplyDto.getMeetingRoomId()!=0, "请先选择申请的会议室");
        Assert.state(!endTime.before(beginTime), "开始时间小于结束时间!");
        Assert.state(now.before(beginTime), "申请时间不能小于当前时间!");
        //判断该会议室 时间段是否使用
        String existTime = checkTimeIsAvailable(oaMeetingRoomApplyDto.getId(),oaMeetingRoomApplyDto.getMeetingRoomId(),beginTime,endTime);
        Assert.state(existTime.equalsIgnoreCase(""), existTime+"该时间段已被占用!");

/*        int id = oaMeetingRoomApplyDto.getId();
        int meetingRoomId = oaMeetingRoomApplyDto.getMeetingRoomId();
        Map params = new HashMap();
        params.put("deleted",false);
        params.put("beginTime",beginTime);
        params.put("endTime",endTime);
        params.put("id",id);
        params.put("meetingRoomId",meetingRoomId);
        List<OaMeetingRoomApply> list = oaMeetingRoomApplyMapper.selectAll(params);
        Assert.state(list.size()==0, "当前时间段已被申请!");*/

        OaMeetingRoomApply item = oaMeetingRoomApplyMapper.selectByPrimaryKey(oaMeetingRoomApplyDto.getId());
        item.setDeptName(oaMeetingRoomApplyDto.getDeptName());
        item.setMeetingRoomId(oaMeetingRoomApplyDto.getMeetingRoomId());
        item.setMeetingRoomInfo(oaMeetingRoomApplyDto.getMeetingRoomInfo());
        item.setApplyReason(oaMeetingRoomApplyDto.getApplyReason());
        item.setBeginTime(oaMeetingRoomApplyDto.getBeginTime());
        item.setEndTime(oaMeetingRoomApplyDto.getEndTime());
        item.setRemark(oaMeetingRoomApplyDto.getRemark());
        item.setGmtModified(new Date());
        BeanValidator.check(item);
        oaMeetingRoomApplyMapper.updateByPrimaryKey(item);
    }

    private String checkTimeIsAvailable(int id,int meetingRoomId,Date beginTime, Date endTime) throws ParseException {
        String existTime = "";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        //查询该会议室的所有申请
        Map params = new HashMap();
        params.put("deleted",false);
        params.put("meetingRoomId",meetingRoomId);
        List<OaMeetingRoomApply> oaMeetingRoomApplys = oaMeetingRoomApplyMapper.selectAll(params);

        for(OaMeetingRoomApply item :oaMeetingRoomApplys){
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

    public OaMeetingRoomApplyDto getModelById(int id)
    {
        return getDto(oaMeetingRoomApplyMapper.selectByPrimaryKey(id));
    }


    public OaMeetingRoomApplyDto getDto(OaMeetingRoomApply item)
    {
        OaMeetingRoomApplyDto oaMeetingRoomApplyDto = OaMeetingRoomApplyDto.adapt(item);
        if (item.getMeetingRoomId()!=0){
            oaMeetingRoomApplyDto.setMeetingRoomName(oaMeetingRoomService.getModelById(item.getMeetingRoomId()).getRoomName());
        }
        ProcessInstanceDto processInstanceDto = actService.getProcessInstanceDto(item.getProcessInstanceId());
        oaMeetingRoomApplyDto.setProcessName(processInstanceDto.getProcessName());
        oaMeetingRoomApplyDto.setBusinessKey(processInstanceDto.getBusinessKey());
        if(!item.getProcessEnd()&&processInstanceDto.isProcessEnd())
        {
            item.setProcessEnd(true);
            oaMeetingRoomApplyMapper.updateByPrimaryKey(item);
        }

        return oaMeetingRoomApplyDto;
    }

    public int getNewModel(String userLogin)
    {
        HrEmployeeDto hrEmployeeDto = selectEmployeeService.selectByUserLogin(userLogin);   //创建者信息
        OaMeetingRoomApply item = new OaMeetingRoomApply();
//        item.setPublishUserName(hrEmployeeDto.getUserName());
//        item.setPublishDeptName(hrEmployeeDto.getDeptName());
//        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeptName(hrEmployeeDto.getDeptName());
//        item.setBeginTime(new Date());
//        item.setEndTime(new Date());
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setProcessEnd(false);
        ModelUtil.setNotNullFields(item);
        oaMeetingRoomApplyMapper.insert(item);
        Map variables = Maps.newHashMap();
        variables.put("userLogin",userLogin);
        variables.put("description","会议室申请"+item.getCreatorName());
        variables.put("backOffices",selectEmployeeService.getUserListByRoleName("办公室后勤管理岗位"));
        String processInstanceId =taskHandleService.startProcess(EdConst.OA_MEETINGROOM_APPLY,EdConst.OA_MEETINGROOM_APPLY+'_'+item.getId(),variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        oaMeetingRoomApplyMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params,String userLogin,int pageNum,int pageSize)
    {
        params.put("deleted",false);
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum,pageSize).doSelectPageInfo(()->oaMeetingRoomApplyMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p ->{
            OaMeetingRoomApply item = (OaMeetingRoomApply)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public HSSFWorkbook downloadTotalExcel() throws IOException {
        Map params = new HashMap();
        params.put("deleted",false);
        List<OaMeetingRoomApply> oaMeetingRoomApplies = oaMeetingRoomApplyMapper.selectAll(params);

        String classpath = this.getClass().getResource("/").getPath().replaceFirst("/", "");
        String webappRoot = classpath.replaceAll("WEB-INF/classes/", "");
        String filePath = webappRoot + "assets/doc/导出模板/会议室申请导出模板.xls";
        //得到模板文件
        InputStream in = new FileInputStream(filePath);

        HSSFWorkbook workbook = new HSSFWorkbook(in);
        /* workbook.getSheet(0).getRow(0).getCell()*/
        HSSFSheet sheet = workbook.getSheetAt(0);
        //模板cell样式
        HSSFCellStyle cellStyle = sheet.getRow(2).getCell(1).getCellStyle();

        int rowIndex = 3;//起始行
        for (OaMeetingRoomApply meetingRoomApply : oaMeetingRoomApplies) {
            //车辆基本信息
            OaMeetingRoom oaMeetingRoom = oaMeetingRoomMapper.selectByPrimaryKey(meetingRoomApply.getMeetingRoomId());

            HSSFRow row = sheet.createRow(rowIndex++);
            HSSFCell cell = row.createCell(0);
            cell.setCellValue(oaMeetingRoom.getRoomName());
            cell.setCellStyle(cellStyle);
            HSSFCell cell1 = row.createCell(1);
            cell1.setCellValue(oaMeetingRoom.getRoomAddress());
            cell1.setCellStyle(cellStyle);
            HSSFCell cell2 = row.createCell(2);
            cell2.setCellValue(oaMeetingRoom.getRoomCapacity());
            cell2.setCellStyle(cellStyle);
            HSSFCell cell3 = row.createCell(3);
            cell3.setCellValue(oaMeetingRoom.getDeptName());
            cell3.setCellStyle(cellStyle);

            HSSFCell cell4 = row.createCell(4);
            cell4.setCellValue(meetingRoomApply.getDeptName());
            cell4.setCellStyle(cellStyle);
            HSSFCell cell5 = row.createCell(5);
            cell5.setCellValue(meetingRoomApply.getApplyReason());
            cell5.setCellStyle(cellStyle);
            HSSFCell cell6 = row.createCell(6);
            cell6.setCellValue(meetingRoomApply.getBeginTime());
            cell6.setCellStyle(cellStyle);
            HSSFCell cell7 = row.createCell(7);
            cell7.setCellValue(meetingRoomApply.getEndTime());
            cell7.setCellStyle(cellStyle);
            HSSFCell cell8 = row.createCell(8);
            cell8.setCellValue(meetingRoomApply.getMeetingRoomInfo());
            cell8.setCellStyle(cellStyle);

            HSSFCell cell9 = row.createCell(9);
            cell9.setCellValue(getDto(meetingRoomApply).getProcessName());
            cell9.setCellStyle(cellStyle);
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        workbook.write(os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        IOUtils.toByteArray(is);
        return workbook;
    }


}
