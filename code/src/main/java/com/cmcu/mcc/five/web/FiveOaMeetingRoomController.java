package com.cmcu.mcc.five.web;

import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.five.dto.FiveOaMeetingRoomDto;
import com.cmcu.mcc.five.entity.FiveOaMeetingRoom;
import com.cmcu.mcc.five.entity.FiveOaMeetingRoomDetail;
import com.cmcu.mcc.five.service.FiveOaMeetingRoomService;
import com.common.model.JsonData;
import com.common.wx.model.MeetingRoom;
import com.common.wx.model.MeetingRoomBook;
import com.common.wx.model.MeetingRoomSchedule;
import com.common.wx.service.MeetingRoomService;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("five/oa/meetingRoom")
public class FiveOaMeetingRoomController {

    @Autowired
    FiveOaMeetingRoomService fiveOaMeetingRoomService;


    @PostMapping("/selectAll.json")
    public JsonData selectAll() {
       return JsonData.success(MeetingRoomService.listData());
    }

    @PostMapping("/save.json")
    public JsonData save(@RequestBody MeetingRoom item){
        return MeetingRoomService.save(item);
    }

    @PostMapping("/remove.json")
    public JsonData remove(String meetingroom_id) {
        MeetingRoomService.delete(meetingroom_id);
        return JsonData.success();
    }

    @PostMapping("/getModelById.json")
    public JsonData getModelById(String meetingroom_id) {
        MeetingRoom item = MeetingRoomService.getModelById(meetingroom_id);
        return JsonData.success(item);
    }

    @PostMapping("/listBookData.json")
    public JsonData listBookData(String meetingroom_id,String date) {
        return JsonData.success(MeetingRoomService.listBookData(meetingroom_id, date));
    }

    /**
     * 获取最近一周的
     * @param meetingroom_id
     * @param date
     * @return
     * @throws ParseException
     */
    @PostMapping("/listRecentBookData.json")
    public JsonData listRecentBookData(String meetingroom_id,String date) throws ParseException {
        if(StringUtils.isEmpty(date)){
            date= DateFormatUtils.format(new Date(),"yyyy-MM-dd");
        }
        List<MeetingRoomSchedule> list=Lists.newArrayList();
        Date startDate= DateUtils.parseDate(date,"yyyy-MM-dd");
        for(int i=0;i<5;i++) {
            list.addAll(MeetingRoomService.listBookData(meetingroom_id, DateFormatUtils.format(DateUtils.addDays(startDate, i), "yyyy-MM-dd")));
        }
        return JsonData.success(list);
    }


    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin, int pageNum,int pageSize) throws ParseException {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveOaMeetingRoomService.listPagedData(params,userLogin,pageNum,pageSize);
        return JsonData.success(pageInfo);
    }

    @PostMapping("/listAllRoom.json")
    public JsonData listAllRoom(int applyId) throws ParseException {
        List<FiveOaMeetingRoom> list = fiveOaMeetingRoomService.listAllRoom(applyId);
        return JsonData.success(list);
    }
    @PostMapping("/listRoomStatusByDay.json")
    public JsonData listRoomStatusByDay(int applyId) throws ParseException {
        List<FiveOaMeetingRoomDto> list = fiveOaMeetingRoomService.listRoomStatusByDay(applyId);
        return JsonData.success(list);
    }

    @PostMapping("/getModelById2.json")
    public JsonData getModelById2(int id) {
        return  JsonData.success(fiveOaMeetingRoomService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin)
    {
        return JsonData.success(fiveOaMeetingRoomService.getNewModel(userLogin));
    }



    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveOaMeetingRoomDto item) {
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        fiveOaMeetingRoomService.update(item);
        return  JsonData.success();
    }

    @PostMapping("/updateDetail.json")
    public JsonData updateDetail(@RequestBody FiveOaMeetingRoomDetail item) {
        fiveOaMeetingRoomService.updateDetail(item);
        return  JsonData.success();
    }

    @PostMapping("/removeDetail.json")
    public JsonData removeDetail(int id) {
        fiveOaMeetingRoomService.removeDetail(id);
        return  JsonData.success();
    }
    @PostMapping("/getNewModelDetail.json")
    public JsonData getNewModelDetail(int meetingId) {
        return  JsonData.success(fiveOaMeetingRoomService.getNewModelDetail(meetingId));
    }
    @PostMapping("/getModelDetailById.json")
    public JsonData getModelDetailById(int id) {
        return  JsonData.success(fiveOaMeetingRoomService.getModelDetailById(id));
    }

    @PostMapping("/listDetail.json")
    public JsonData listDetail(int meetingId) {
        return  JsonData.success(fiveOaMeetingRoomService.listDetail(meetingId));
    }
}
