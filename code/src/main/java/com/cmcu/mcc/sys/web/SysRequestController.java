package com.cmcu.mcc.sys.web;

import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.sys.entity.SysRequest;
import com.cmcu.mcc.sys.service.SysRequestService;
import com.github.pagehelper.PageInfo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;


@Controller
@RequestMapping("/sys/request")
public class SysRequestController {

    @Autowired
    SysRequestService sysRequestService;
    @Autowired
    SelectEmployeeService selectEmployeeService;

    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest req) {
        Object attribute = req.getSession().getAttribute(MccConst.EN_LOGIN);
        HrEmployeeDto hrEmployeeDto = selectEmployeeService.selectByUserLogin((String) attribute);
        if (hrEmployeeDto.getRoleNames().contains("系统审计员")) {
            return new ModelAndView("sys/requestAudit");
        } else if (hrEmployeeDto.getRoleNames().contains("系统安全员")) {
            return new ModelAndView("sys/requestSafety");
        } else {
            return new ModelAndView("sys/request");
        }
    }


    @ResponseBody
    @PostMapping("/listPagedRequest.json")
    public PageInfo<SysRequest> listPagedRequest(String q, int pageNum, int pageSize) {
        PageInfo<SysRequest> pageInfo = sysRequestService.listPagedData(q, pageNum, pageSize);
        return pageInfo;
    }

    @ResponseBody
    @PostMapping("/listSysAdminData.json")
    public PageInfo<SysRequest> listSysAdminData(String q, int pageNum, int pageSize) {
        PageInfo<SysRequest> pageInfo = sysRequestService.listSysAdminData(q, pageNum, pageSize);
        return pageInfo;
    }

    @ResponseBody
    @PostMapping("/listSafetyData.json")
    public PageInfo<SysRequest> listSafetyData(String q, int pageNum, int pageSize) {
        PageInfo<SysRequest> pageInfo = sysRequestService.listSafetyData(q, pageNum, pageSize);
        return pageInfo;
    }

    @ResponseBody
    @PostMapping("/listAuditData.json")
    public PageInfo<SysRequest> listAuditData(String q, int pageNum, int pageSize) {
        PageInfo<SysRequest> pageInfo = sysRequestService.listAuditData(q, pageNum, pageSize);
        return pageInfo;
    }

    @ResponseBody
    @PostMapping("/listUserData.json")
    public PageInfo<SysRequest> listUserData(String q, int pageNum, int pageSize) {
        PageInfo<SysRequest> pageInfo = sysRequestService.listUserData(q, pageNum, pageSize);
        return pageInfo;
    }
    /**导出数据
     * @param key 导出具体数据关键字
     * @param response
     * @throws IOException
     */
    @RequestMapping("/downloadExcel.json")
    public void downloadExcel(String key ,final HttpServletResponse response) throws IOException {
        String fileName=key+"系统日志.xls";
        HSSFWorkbook hssfWorkbook = sysRequestService.downloadExcel(key);
        response.reset();
        response.setContentType("application/csv");
        response.setHeader("Content-Disposition", "attachment; filename=" +  URLEncoder.encode(fileName, "utf-8"));
        hssfWorkbook.write(response.getOutputStream());
    }
}
