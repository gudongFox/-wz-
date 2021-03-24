package com.cmcu.mcc.oa.service;

import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.oa.dao.OaMeetingRoomApplyMapper;
import com.cmcu.mcc.oa.dao.OaMeetingRoomMapper;
import com.cmcu.mcc.oa.dto.OaMeetingRoomDto;
import com.cmcu.mcc.oa.entity.OaMeetingRoom;
import com.cmcu.mcc.oa.entity.OaMeetingRoomApply;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OaMeetingRoomService {

    @Resource
    OaMeetingRoomMapper oaMeetingRoomMapper;

    @Autowired
    SelectEmployeeService selectEmployeeService;
    @Resource
    CommonCodeService commonCodeService;
    @Autowired
    OaMeetingRoomApplyMapper oaMeetingRoomApplyMapper;


    public void remove(int id,String userLogin)
    {
        OaMeetingRoom item = oaMeetingRoomMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin),"只允许创建人删除!");
        item.setGmtModified(new Date());
        item.setDeleted(true);
        oaMeetingRoomMapper.updateByPrimaryKey(item);
    }

    public void update(OaMeetingRoomDto oaMeetingRoomDto)
    {
        OaMeetingRoom item = oaMeetingRoomMapper.selectByPrimaryKey(oaMeetingRoomDto.getId());
        item.setDeptName(oaMeetingRoomDto.getDeptName());
        item.setRoomName(oaMeetingRoomDto.getRoomName());
        item.setRoomAddress(oaMeetingRoomDto.getRoomAddress());
        item.setRoomCapacity(oaMeetingRoomDto.getRoomCapacity());
        item.setRoomDesc(oaMeetingRoomDto.getRoomDesc());
        item.setRoomStatus(oaMeetingRoomDto.getRoomStatus());
        item.setRemark(oaMeetingRoomDto.getRemark());
        item.setGmtModified(new Date());
        BeanValidator.check(item);
        oaMeetingRoomMapper.updateByPrimaryKey(item);
    }

    public OaMeetingRoomDto getModelById(int id)
    {
        return getDto(oaMeetingRoomMapper.selectByPrimaryKey(id));
    }


    public OaMeetingRoomDto getDto(OaMeetingRoom item)
    {
        OaMeetingRoomDto oaMeetingRoomDto = OaMeetingRoomDto.adapt(item);
        return oaMeetingRoomDto;
    }

    public int getNewModel(String userLogin)
    {
        HrEmployeeDto hrEmployeeDto = selectEmployeeService.selectByUserLogin(userLogin);   //创建者信息
        OaMeetingRoom item = new OaMeetingRoom();
        item.setCreator(userLogin);
        item.setRoomStatus(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"会议室状态").toString());
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());

        ModelUtil.setNotNullFields(item);
        oaMeetingRoomMapper.insert(item);
        item.setBusinessKey("oaMeetRoom_"+item.getId());
        oaMeetingRoomMapper.updateByPrimaryKey(item);

        return item.getId();
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params,String userLogin,int pageNum,int pageSize) throws ParseException {
        //跟新 会议室状态
        updateRoomState();

        params.put("deleted",false);
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum,pageSize).doSelectPageInfo(()->oaMeetingRoomMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p ->{
            OaMeetingRoom item = (OaMeetingRoom)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public List<OaMeetingRoom> listAllRoom() throws ParseException {
        //跟新 会议室状态
        updateRoomState();

        Map params = new HashMap();
        params.put("roomStatus","正常");
        params.put("deleted",false);
        List<OaMeetingRoom> list = oaMeetingRoomMapper.selectAll(params);
        return list;
    }
    //跟新会议室状态
    public void updateRoomState() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date now = new Date();
        Map roomParams = new HashMap();
        roomParams.put("deleted",false);
        List<OaMeetingRoom> oaMeetingRooms = oaMeetingRoomMapper.selectAll(roomParams);
        for(OaMeetingRoom room:oaMeetingRooms){
            //查询该会议室的所有申请 且流程已完结
            Map params = new HashMap();
            params.put("deleted",false);
            params.put("meetingRoomId",room.getId());
            params.put("isProcessEnd",true);
            List<OaMeetingRoomApply> oaMeetingRoomApplys = oaMeetingRoomApplyMapper.selectAll(params);
            for(OaMeetingRoomApply roomApply :oaMeetingRoomApplys){
                //判断当前时间 是否落在申请区域
                if(now.after(simpleDateFormat.parse(roomApply.getBeginTime()))&&now.before(simpleDateFormat.parse(roomApply.getEndTime()))){
                    //跟新状态为 使用中
                    OaMeetingRoom oaMeetingRoom = oaMeetingRoomMapper.selectByPrimaryKey(room.getId());
                    oaMeetingRoom.setRoomStatus("使用中");
                    oaMeetingRoomMapper.updateByPrimaryKey(oaMeetingRoom);
                } else{
                    //跟新状态为 正常 --不用每次修改 待优化
                    OaMeetingRoom oaMeetingRoom = oaMeetingRoomMapper.selectByPrimaryKey(room.getId());
                    oaMeetingRoom.setRoomStatus("正常");
                    oaMeetingRoomMapper.updateByPrimaryKey(oaMeetingRoom);
                }
            }

            if(oaMeetingRoomApplys.size()==0){
                OaMeetingRoom oaMeetingRoom = oaMeetingRoomMapper.selectByPrimaryKey(room.getId());
                oaMeetingRoom.setRoomStatus("正常");
                oaMeetingRoomMapper.updateByPrimaryKey(oaMeetingRoom);
            }
        }
    }


}
