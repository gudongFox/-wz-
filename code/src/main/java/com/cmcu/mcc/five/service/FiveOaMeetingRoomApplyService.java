package com.cmcu.mcc.five.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyPoiUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.dto.ProcessInstanceDto;
import com.cmcu.mcc.five.dao.FiveOaMeetingRoomApplyMapper;
import com.cmcu.mcc.five.dao.FiveOaMeetingRoomMapper;
import com.cmcu.mcc.five.dto.FiveOaMeetingRoomApplyDto;
import com.cmcu.mcc.five.entity.FiveOaMeetingRoom;
import com.cmcu.mcc.five.entity.FiveOaMeetingRoomApply;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FiveOaMeetingRoomApplyService {

    @Resource
    FiveOaMeetingRoomApplyMapper fiveOaMeetingRoomApplyMapper;
    @Autowired
    FiveOaMeetingRoomService fiveOaMeetingRoomService;
    @Autowired
    SelectEmployeeService selectEmployeeService;
    @Autowired
    ActService actService;
    @Autowired
    FiveOaMeetingRoomMapper fiveOaMeetingRoomMapper;
    @Resource
    TaskHandleService taskHandleService;

    @Autowired
    ProcessQueryService processQueryService;
    @Autowired
    HrEmployeeSysService hrEmployeeSysService;
    @Resource
    HandleFormService handleFormService;
    @Resource
    MyActService myActService;


    public void remove(int id,String userLogin) {
        FiveOaMeetingRoomApply item = fiveOaMeetingRoomApplyMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin),"????????????????????????!");
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    public void update(FiveOaMeetingRoomApplyDto dto) throws ParseException {
        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        Date beginTime = simpleDateFormat.parse(dto.getBeginTime());
        Date endTime = simpleDateFormat.parse(dto.getEndTime());
        Assert.state(!endTime.before(beginTime), "??????????????????????????????!");
        Assert.state(now.before(beginTime), "????????????????????????????????????!");

        FiveOaMeetingRoomApply item = fiveOaMeetingRoomApplyMapper.selectByPrimaryKey(dto.getId());
        item.setDeptName(dto.getDeptName());
        item.setDeptId(dto.getDeptId());
        item.setMeetingRoomId(dto.getMeetingRoomId());
        item.setMeetingRoomInfo(dto.getMeetingRoomInfo());
        item.setApplyReason(dto.getApplyReason());
        item.setBeginTime(dto.getBeginTime());
        item.setEndTime(dto.getEndTime());
        item.setRemark(dto.getRemark());
        item.setHostMan(dto.getHostMan());
        item.setHostManName(dto.getHostManName());
        item.setChargeLeader(dto.getChargeLeader());
        item.setChargeLeaderName(dto.getChargeLeaderName());
        item.setAttendUser(dto.getAttendUser());
        item.setAttendUserName(dto.getAttendUserName());

        item.setGmtModified(new Date());
        Map variables = Maps.newHashMap();
        variables.put("deptChargeMan",selectEmployeeService.getDeptChargeMen(item.getDeptId()));
        myActService.setVariables(item.getProcessInstanceId(),variables);
        BeanValidator.check(item);

        fiveOaMeetingRoomApplyMapper.updateByPrimaryKey(item);
    }
    public void changeType(FiveOaMeetingRoomApplyDto fiveOaMeetingRoomApplyDto) throws ParseException {

        FiveOaMeetingRoomApply item = fiveOaMeetingRoomApplyMapper.selectByPrimaryKey(fiveOaMeetingRoomApplyDto.getId());
        if(fiveOaMeetingRoomApplyDto.getApplyState().equalsIgnoreCase("????????????")){
            item.setApplyComplete(true);
        }else{
            item.setApplyComplete(false);
        }

        item.setGmtModified(new Date());
        BeanValidator.check(item);
        fiveOaMeetingRoomApplyMapper.updateByPrimaryKey(item);
    }

    private String checkTimeIsAvailable(int id,int meetingRoomId,Date beginTime, Date endTime) throws ParseException {
        String existTime = "";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        //?????????????????????????????????
        Map params = new HashMap();
        params.put("deleted",false);
        params.put("meetingRoomId",meetingRoomId);
        List<FiveOaMeetingRoomApply> fiveOaMeetingRoomApplys = fiveOaMeetingRoomApplyMapper.selectAll(params);

        for(FiveOaMeetingRoomApply item :fiveOaMeetingRoomApplys){
            //????????????????????????
            if(!item.getId().equals(id)){
                //?????? ???????????? ???????????? ?????????????????? ????????? ???????????? ???????????????
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

    public FiveOaMeetingRoomApplyDto getModelById(int id) {
        return getDto(fiveOaMeetingRoomApplyMapper.selectByPrimaryKey(id));
    }


    public FiveOaMeetingRoomApplyDto getDto(FiveOaMeetingRoomApply item) {
        FiveOaMeetingRoomApplyDto dto = FiveOaMeetingRoomApplyDto.adapt(item);
        if(item.getApplyComplete()){
            dto.setApplyState("????????????");
        }else {
            dto.setApplyState("???????????????");
        }
        if (item.getMeetingRoomId()!=0){
            dto.setMeetingRoomName(fiveOaMeetingRoomService.getModelById(item.getMeetingRoomId()).getRoomName());
        }
        ProcessInstanceDto processInstanceDto = actService.getProcessInstanceDto(item.getProcessInstanceId());

        dto.setProcessName("?????????");

        if (!dto.getProcessEnd()) {
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if(customProcessInstance ==null || customProcessInstance.isFinished()){
                item.setProcessEnd(true);
                fiveOaMeetingRoomApplyMapper.updateByPrimaryKey(item);
            }
            if(customProcessInstance!=null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }
        dto.setBusinessKey(processInstanceDto.getBusinessKey());

        return dto;
    }

    public int getNewModel(String userLogin) {
        HrEmployeeDto hrEmployeeDto = selectEmployeeService.selectByUserLogin(userLogin);   //???????????????
        FiveOaMeetingRoomApply item = new FiveOaMeetingRoomApply();
//        item.setPublishUserName(hrEmployeeDto.getUserName());
//        item.setPublishDeptName(hrEmployeeDto.getDeptName());
//        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setDeptId(hrEmployeeDto.getDeptId());
//        item.setBeginTime(new Date());
//        item.setEndTime(new Date());
        item.setApplyComplete(true);
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setProcessEnd(false);
        ModelUtil.setNotNullFields(item);
        fiveOaMeetingRoomApplyMapper.insert(item);
        Map variables = Maps.newHashMap();
        variables.put("userLogin",userLogin);
        variables.put("deptChargeMan",selectEmployeeService.getDeptChargeMen(item.getDeptId()));
        variables.put("processDescription","???????????????"+item.getCreatorName());
        String processInstanceId =taskHandleService.startProcess(EdConst.FIVE_OA_MEETINGROME_APPLY, EdConst.FIVE_OA_MEETINGROME_APPLY+'_'+item.getId(),variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(EdConst.FIVE_OA_MEETINGROME_APPLY+'_'+item.getId());
        fiveOaMeetingRoomApplyMapper.updateByPrimaryKey(item);
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
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaMeetingRoomApplyMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaMeetingRoomApply item=(FiveOaMeetingRoomApply)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public HSSFWorkbook downloadExcel() throws IOException {
        Map params = new HashMap();
        params.put("deleted",false);
        List<FiveOaMeetingRoomApply> fiveOaMeetingRoomApplies = fiveOaMeetingRoomApplyMapper.selectAll(params);

        String classpath = this.getClass().getResource("/").getPath().replaceFirst("/", "");
        String webappRoot = classpath.replaceAll("WEB-INF/classes/", "");
        String filePath = webappRoot + "assets/doc/????????????/???????????????????????????.xls";
        //??????????????????
        InputStream in = new FileInputStream(filePath);

        HSSFWorkbook workbook = new HSSFWorkbook(in);
        /* workbook.getSheet(0).getRow(0).getCell()*/
        HSSFSheet sheet = workbook.getSheetAt(0);
        //??????cell??????
        HSSFCellStyle cellStyle = MyPoiUtil.getCellStyle(workbook);

        int rowIndex = 3;//?????????
        for (FiveOaMeetingRoomApply meetingRoomApply : fiveOaMeetingRoomApplies) {
            //??????????????????
            if(meetingRoomApply.getDeptId()!=0){
                FiveOaMeetingRoom fiveOaMeetingRoom = fiveOaMeetingRoomMapper.selectByPrimaryKey(meetingRoomApply.getMeetingRoomId());

                HSSFRow row = sheet.createRow(rowIndex++);
                HSSFCell cell = row.createCell(0);
                cell.setCellValue(fiveOaMeetingRoom.getRoomName());
                cell.setCellStyle(cellStyle);
                HSSFCell cell1 = row.createCell(1);
                cell1.setCellValue(fiveOaMeetingRoom.getRoomAddress());
                cell1.setCellStyle(cellStyle);
                HSSFCell cell2 = row.createCell(2);
                cell2.setCellValue(fiveOaMeetingRoom.getRoomCapacity());
                cell2.setCellStyle(cellStyle);
                HSSFCell cell3 = row.createCell(3);
                cell3.setCellValue(fiveOaMeetingRoom.getDeptName());
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
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        workbook.write(os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        IOUtils.toByteArray(is);
        return workbook;
    }


}
