package com.cmcu.mcc.oa.web;

import com.common.model.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.oa.dto.OaMeetingRoomDto;
import com.cmcu.mcc.oa.entity.OaMeetingRoom;
import com.cmcu.mcc.oa.service.OaMeetingRoomService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/oa/meetingRoom")
public class OaMeetingRoomController {

    @Autowired
    OaMeetingRoomService oaMeetingRoomService;

    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin, int pageNum,int pageSize) throws ParseException {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = oaMeetingRoomService.listPagedData(params,userLogin,pageNum,pageSize);
        return JsonData.success(pageInfo);
    }

    @PostMapping("/listAllRoom.json")
    public JsonData listAllRoom() throws ParseException {
        List<OaMeetingRoom> list = oaMeetingRoomService.listAllRoom();
        return JsonData.success(list);
    }

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id)
    {
        return  JsonData.success(oaMeetingRoomService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin)
    {
        return JsonData.success(oaMeetingRoomService.getNewModel(userLogin));
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin)
    {
        oaMeetingRoomService.remove(id,userLogin);
        return JsonData.success();
    }

    @PostMapping("/update.json")
    public JsonData update(@RequestBody OaMeetingRoomDto item)
    {
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        oaMeetingRoomService.update(item);
        return  JsonData.success();
    }


}
