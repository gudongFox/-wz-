package com.cmcu.mcc.business.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.JsonMapper;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.business.dto.BusinessPreContractDto;
import com.cmcu.mcc.business.service.BusinessPreContractService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;

@RestController
@RequestMapping("/business/preContract")
public class BusinessPreContractController {


    @Autowired
    BusinessPreContractService BusinessPreContractService;

    @PostMapping("/loadPagedData.json")
    public JsonData loadPagedData(String userLogin,String uiSref,int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        return JsonData.success(BusinessPreContractService.listPagedData(params,userLogin,uiSref,pageNum, pageSize));
    }

    @PostMapping("/update.json")
    public JsonData update(@RequestBody BusinessPreContractDto item) {
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()), "操作人不能为空!");
        BusinessPreContractService.update(item);
        return JsonData.success();
    }

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id) {
        return JsonData.success(BusinessPreContractService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin,String uiSref) {
        return JsonData.success(BusinessPreContractService.getNewModel(userLogin,uiSref));
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id, String userLogin) {
        BusinessPreContractService.remove(id, userLogin);
        return JsonData.success();
    }

    @PostMapping("/selectNotHaveContract.json")
    public JsonData selectNotHaveContract(int contractId) {
        return JsonData.success(BusinessPreContractService.selectNotHaveContract(contractId));
    }

    @PostMapping("/selectNotHaveInput.json")
    public JsonData selectNotHaveInput(int contractId) {
        return JsonData.success(BusinessPreContractService.selectNotHaveInput(contractId));
    }

    @RequestMapping("/downloadModel.json")
    public void downloadModel(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        String classpath = this.getClass().getResource("/").getPath().replaceFirst("/", "");
        String webappRoot = classpath.replaceAll("WEB-INF/classes/", "");
        String filePath = webappRoot + "assets/doc/导出模板/合同台账.xls";
        downloadingFile(filePath, "合同台账模板.xls", response);
    }

    private void downloadingFile(String filePath, String fileName, HttpServletResponse response) throws IOException {
        File file = new File(filePath);
        if (file.exists() && file.length() > 0) {
            byte[] data = FileCopyUtils.copyToByteArray(file);
            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "utf-8").replace("+", "%20"));
            response.addHeader("Content-Length", "" + data.length);
            response.setContentType("multipart/form-data;charset=UTF-8");
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();
        }
    }

    @PostMapping("/getProjectNo.json")
    public JsonData getProjectNo(int id) {
        return JsonData.success(BusinessPreContractService.getProjectNo(id));
    }

    @PostMapping("/insertContractLibrary.json")
    public JsonData insertContractLibrary(int preContractId){
        return JsonData.success(BusinessPreContractService.insertContractLibrary(preContractId));
    }
    @PostMapping("/addReviewContract.json")
    public JsonData addReviewContract(int preContractId){
        return JsonData.success(BusinessPreContractService.addReviewContract(preContractId));
    }
    @PostMapping("/addCustomer.json")
    public JsonData addCustomer(String userLogin,String preContractId) {
        return JsonData.success( BusinessPreContractService.addCustomer(preContractId,userLogin));
    }



}
