package com.cmcu.mcc.five.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.five.dto.FiveOaMeetingRoomApplyDto;
import com.cmcu.mcc.five.service.FiveOaMeetingRoomApplyService;

import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping("five/oa/meetingRoomApply")
public class FiveOaMeetingRoomApplyController {

    @Autowired
    FiveOaMeetingRoomApplyService fiveOaMeetingRoomApplyService;

    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveOaMeetingRoomApplyService.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }


    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id)
    {
        return  JsonData.success(fiveOaMeetingRoomApplyService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin)
    {
        return JsonData.success(fiveOaMeetingRoomApplyService.getNewModel(userLogin));
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin)
    {
        fiveOaMeetingRoomApplyService.remove(id,userLogin);
        return JsonData.success();
    }

    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveOaMeetingRoomApplyDto item) throws ParseException {
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        fiveOaMeetingRoomApplyService.update(item);
        return  JsonData.success();
    }
    @PostMapping("/changeType.json")
    public JsonData changeType(@RequestBody FiveOaMeetingRoomApplyDto item) throws ParseException {
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        fiveOaMeetingRoomApplyService.changeType(item);
        return  JsonData.success();
    }
    /**统计数据
     *
     * @param response
     * @throws IOException
     */
    @RequestMapping("/downloadExcel.json")
    public void downlfiveOadExcel(String projectMajorMessageId ,final HttpServletResponse response) throws IOException {

        String fileName="综合管理-会议室申请数据.xls";
        HSSFWorkbook hssfWorkbook = fiveOaMeetingRoomApplyService.downloadExcel();
        response.reset();
        response.setContentType("application/csv");
        response.setHeader("Content-Disposition", "attachment; filename=" +  URLEncoder.encode(fileName, "utf-8"));
        hssfWorkbook.write(response.getOutputStream());
    }


}
