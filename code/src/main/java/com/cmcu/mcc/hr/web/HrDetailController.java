package com.cmcu.mcc.hr.web;

import com.common.model.JsonData;
import com.cmcu.common.util.FileUtil;
import com.cmcu.common.util.MyPoiUtil;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.hr.service.HrDetailService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/hr/detail")
public class HrDetailController {

    @Resource
    HrDetailService hrDetailService;

    @ResponseBody
    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String referType,String owner,String enLogin){
        hrDetailService.getNewModel(referType,owner,enLogin,false);
        return JsonData.success();
    }


    @RequestMapping("/downloadExcel.json")
    public void  downloadModelExcel(final HttpServletResponse response, String owner,String referType) {
        Map data = hrDetailService.listExcelData(owner, referType);
        StringBuilder fileName = new StringBuilder();
        if (data.size() > 1) {
            fileName.append("人力信息");
        } else if (data.size() == 1) {
            fileName.append(data.keySet().toArray()[0].toString());
        }
        if (StringUtils.isNotEmpty(owner)) {
            fileName.append("-");
            fileName.append(owner);
        }
        fileName.append("-");
        fileName.append(DateFormatUtils.format(new Date(), "yyyyMMddHHmm"));
        fileName.append(".xls");
        MyPoiUtil.downloadExcel(data,false, FileUtil.getGoodFileName(fileName.toString()), response);
    }

}
