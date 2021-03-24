package com.cmcu.mcc.five.service;

import com.cmcu.common.entity.CommonEdBuild;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.five.dao.FiveOaMeetingRoomApplyMapper;
import com.cmcu.mcc.five.dao.FiveOaMeetingRoomDetailMapper;
import com.cmcu.mcc.five.dao.FiveOaMeetingRoomMapper;
import com.cmcu.mcc.five.dto.FiveOaMeetingRoomDto;
import com.cmcu.mcc.five.entity.FiveOaMeetingRoom;
import com.cmcu.mcc.five.entity.FiveOaMeetingRoomApply;
import com.cmcu.mcc.five.entity.FiveOaMeetingRoomDetail;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class FiveOaMeetingRoomService {

    @Resource
    FiveOaMeetingRoomMapper fiveOaMeetingRoomMapper;

    @Autowired
    SelectEmployeeService selectEmployeeService;
    @Resource
    CommonCodeService commonCodeService;
    @Autowired
    FiveOaMeetingRoomApplyMapper fiveOaMeetingRoomApplyMapper;
    @Resource
    FiveOaMeetingRoomDetailMapper fiveOaMeetingRoomDetailMapper;


    public void remove(int id,String userLogin) {
        FiveOaMeetingRoom item = fiveOaMeetingRoomMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin),"只允许创建人删除!");
        item.setGmtModified(new Date());
        item.setDeleted(true);
        fiveOaMeetingRoomMapper.updateByPrimaryKey(item);
    }

    public void update(FiveOaMeetingRoomDto fiveOaMeetingRoomDto) {
        FiveOaMeetingRoom item = fiveOaMeetingRoomMapper.selectByPrimaryKey(fiveOaMeetingRoomDto.getId());
        item.setDeptName(fiveOaMeetingRoomDto.getDeptName());
        item.setRoomName(fiveOaMeetingRoomDto.getRoomName());
        item.setRoomAddress(fiveOaMeetingRoomDto.getRoomAddress());
        item.setRoomCapacity(fiveOaMeetingRoomDto.getRoomCapacity());
        item.setRoomDesc(fiveOaMeetingRoomDto.getRoomDesc());
        item.setRoomStatus(fiveOaMeetingRoomDto.getRoomStatus());
        item.setRemark(fiveOaMeetingRoomDto.getRemark());
        item.setGmtModified(new Date());
        BeanValidator.check(item);
        fiveOaMeetingRoomMapper.updateByPrimaryKey(item);
    }

    public FiveOaMeetingRoomDto getModelById(int id)
    {
        FiveOaMeetingRoom fiveOaMeetingRoom = fiveOaMeetingRoomMapper.selectByPrimaryKey(id);
        return getDto(fiveOaMeetingRoom);
    }


    public FiveOaMeetingRoomDto getDto(FiveOaMeetingRoom item) {
        FiveOaMeetingRoomDto fiveOaMeetingRoomDto = FiveOaMeetingRoomDto.adapt(item);
        return fiveOaMeetingRoomDto;
    }

    public int getNewModel(String userLogin) {
        HrEmployeeDto hrEmployeeDto = selectEmployeeService.selectByUserLogin(userLogin);   //创建者信息
        FiveOaMeetingRoom item = new FiveOaMeetingRoom();
        item.setCreator(userLogin);
        item.setRoomStatus(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"会议室状态").toString());
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());

        ModelUtil.setNotNullFields(item);
        fiveOaMeetingRoomMapper.insert(item);
        item.setBusinessKey("fiveOaMeetRoom_"+item.getId());
        fiveOaMeetingRoomMapper.updateByPrimaryKey(item);

        return item.getId();
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params,String userLogin,int pageNum,int pageSize) throws ParseException {
        //跟新 会议室状态
        updateRoomState();

        params.put("deleted",false);
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum,pageSize).doSelectPageInfo(()->fiveOaMeetingRoomMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p ->{
            FiveOaMeetingRoom item = (FiveOaMeetingRoom)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public List<FiveOaMeetingRoom> listAllRoom(int applyId) throws ParseException {
        //跟新 会议室状态
        updateRoomState();
        FiveOaMeetingRoomApply fiveOaMeetingRoomApply = fiveOaMeetingRoomApplyMapper.selectByPrimaryKey(applyId);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        List<FiveOaMeetingRoom> list = getMeetingRoomState(simpleDateFormat.parse(fiveOaMeetingRoomApply.getBeginTime()),simpleDateFormat.parse(fiveOaMeetingRoomApply.getEndTime()),applyId);
        return list;
    }
    //跟新会议室状态
    public void updateRoomState() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date now = new Date();
        Map roomParams = new HashMap();
        roomParams.put("deleted",false);
        List<FiveOaMeetingRoom> fiveOaMeetingRooms = fiveOaMeetingRoomMapper.selectAll(roomParams);
        for(FiveOaMeetingRoom room:fiveOaMeetingRooms){
            //查询该会议室的所有申请 且流程已完结
            Map params = new HashMap();
            params.put("deleted",false);
            params.put("meetingRoomId",room.getId());
            params.put("isProcessEnd",true);
            List<FiveOaMeetingRoomApply> fiveOaMeetingRoomApplys = fiveOaMeetingRoomApplyMapper.selectAll(params);
            for(FiveOaMeetingRoomApply roomApply :fiveOaMeetingRoomApplys){
                //判断当前时间 是否落在申请区域
                if(now.after(simpleDateFormat.parse(roomApply.getBeginTime()))&&now.before(simpleDateFormat.parse(roomApply.getEndTime()))){
                    //跟新状态为 使用中
                    FiveOaMeetingRoom fiveOaMeetingRoom = fiveOaMeetingRoomMapper.selectByPrimaryKey(room.getId());
                    if (!fiveOaMeetingRoom.getRoomStatus().equalsIgnoreCase("维修中")) {
                        fiveOaMeetingRoom.setRoomStatus("使用中");
                        fiveOaMeetingRoomMapper.updateByPrimaryKey(fiveOaMeetingRoom);
                    }
                } else{
                    //跟新状态为 正常 --不用每次修改 待优化
                    FiveOaMeetingRoom fiveOaMeetingRoom = fiveOaMeetingRoomMapper.selectByPrimaryKey(room.getId());
                    if (!fiveOaMeetingRoom.getRoomStatus().equalsIgnoreCase("维修中")) {
                        fiveOaMeetingRoom.setRoomStatus("正常");
                        fiveOaMeetingRoomMapper.updateByPrimaryKey(fiveOaMeetingRoom);
                    }
                }
            }

            if(fiveOaMeetingRoomApplys.size()==0){
                FiveOaMeetingRoom fiveOaMeetingRoom = fiveOaMeetingRoomMapper.selectByPrimaryKey(room.getId());
                if (!fiveOaMeetingRoom.getRoomStatus().equalsIgnoreCase("维修中")) {
                    fiveOaMeetingRoom.setRoomStatus("正常");
                    fiveOaMeetingRoomMapper.updateByPrimaryKey(fiveOaMeetingRoom);
                }
            }
        }
    }
    //查询时间段会议室状态
    public List<FiveOaMeetingRoom> getMeetingRoomState(Date startTime, Date endTime, int applyId) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date now = new Date();
        Map meetingParams = new HashMap();
        meetingParams.put("deleted",false);
        List<FiveOaMeetingRoom> fiveOaMeetingRooms = fiveOaMeetingRoomMapper.selectAll(meetingParams);
        //冲突的meetingRoomid
        List<Integer> conflictMeetingRoomIds =new ArrayList<>();
        for(FiveOaMeetingRoom room:fiveOaMeetingRooms){
            //查询该会议室的所有申请 且流程已完结
            Map params = new HashMap();
            params.put("deleted",false);
            params.put("meetingRoomId",room.getId());
            params.put("isProcessEnd",true);
            List<FiveOaMeetingRoomApply> fiveOaMeetingRoomApplies = fiveOaMeetingRoomApplyMapper.selectAll(params);
            for(FiveOaMeetingRoomApply roomApply :fiveOaMeetingRoomApplies){
                //排除当前申请
                if(roomApply.getId()!=applyId) {
                    //判断当前时间 是否落在申请区域
                    if (endTime.before(simpleDateFormat.parse(roomApply.getBeginTime())) || startTime.after(simpleDateFormat.parse(roomApply.getEndTime()))) {
                        //修改状态为 正常
                        if (!room.getRoomStatus().equalsIgnoreCase("维修中")) {
                            //该会议室已在冲突Id中 就不跟新状态
                            if(!conflictMeetingRoomIds.contains(room.getId())){
                                room.setRoomStatus("正常");
                            }
                        }
                    } else {
                        //修改状态为 使用中
                        if (!room.getRoomStatus().equalsIgnoreCase("维修中")) {
                            room.setRoomStatus("使用中");
                            room.setRemark(roomApply.getRemark()+"<br>"+roomApply.getBeginTime()+" - "+roomApply.getEndTime());
                        }
                    }
                }
            }
        }
        return fiveOaMeetingRooms;

    }

    public List<FiveOaMeetingRoomDto> listRoomStatusByDay(int applyId) throws ParseException {
        updateRoomState();
        List<FiveOaMeetingRoomDto> meetingRoomDtos =new ArrayList<>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        Map meetingParams = new HashMap();
        meetingParams.put("deleted",false);
        List<FiveOaMeetingRoom> fiveOaMeetingRooms = fiveOaMeetingRoomMapper.selectAll(meetingParams);

        for(FiveOaMeetingRoom room:fiveOaMeetingRooms){
            FiveOaMeetingRoomDto dto = getDto(room);
            List<Map> usedTimes =new ArrayList<>();
            //查询该会议室的所有申请 且流程已完结
            Map params = new HashMap();
            params.put("deleted",false);
            params.put("meetingRoomId",room.getId());
            params.put("applyComplete",true);
           // params.put("isProcessEnd",true);
            List<FiveOaMeetingRoomApply> fiveOaMeetingRoomApplies = fiveOaMeetingRoomApplyMapper.selectAll(params);
            for(FiveOaMeetingRoomApply roomApply :fiveOaMeetingRoomApplies){
                //排除当前申请
                    Map map = new HashMap();
                    map.put("beginTime",simpleDateFormat.parse(roomApply.getBeginTime()).getTime());
                    map.put("endTime",simpleDateFormat.parse(roomApply.getEndTime()).getTime());
                    if(roomApply.getId()!=applyId) {
                        map.put("nowApply",false);
                    }else {
                        map.put("nowApply",true);
                    }
                    usedTimes.add(map);
            }
            dto.setUsedTimes(usedTimes);
            meetingRoomDtos.add(dto);
        }
        return meetingRoomDtos;
    }

    public void removeDetail(int id){
        FiveOaMeetingRoomDetail detail = fiveOaMeetingRoomDetailMapper.selectByPrimaryKey(id);
        detail.setDeleted(false);
        detail.setGmtModified(new Date());
        fiveOaMeetingRoomDetailMapper.updateByPrimaryKey(detail);
    }

    public void updateDetail(FiveOaMeetingRoomDetail item){
        ModelUtil.setNotNullFields(item);
        BeanValidator.check(item);
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("project", item.getProject());
        params.put("meetingId", item.getMeetingId());
        List<FiveOaMeetingRoomDetail> builds = fiveOaMeetingRoomDetailMapper.selectAll(params);
        if (item.getId() == null || item.getId() == 0){
            Assert.state(builds.size() == 0, item.getProject() + "名称已存在!");
            item.setGmtCreate(new Date());
            item.setGmtModified(new Date());
            fiveOaMeetingRoomDetailMapper.insert(item);
        }else {
            Assert.state(builds.size() == 0 || builds.get(0).getId().equals(item.getId()), item.getProject() + "名称已存在!");
            fiveOaMeetingRoomDetailMapper.updateByPrimaryKey(item);
        }
    }

    public FiveOaMeetingRoomDetail getNewModelDetail(int meetingId){
        FiveOaMeetingRoomDetail item=new FiveOaMeetingRoomDetail();
        item.setId(0);
        item.setMeetingId(meetingId);
        item.setProjectNum(0);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);
        return item;
    }

    public FiveOaMeetingRoomDetail getModelDetailById(int id){
        return fiveOaMeetingRoomDetailMapper.selectByPrimaryKey(id);
    }

    public List<FiveOaMeetingRoomDetail> listDetail(int meetingId){
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("meetingId",meetingId);
        return fiveOaMeetingRoomDetailMapper.selectAll(params);
    }
}
