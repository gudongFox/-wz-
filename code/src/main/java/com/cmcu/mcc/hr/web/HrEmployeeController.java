package com.cmcu.mcc.hr.web;

import com.common.model.JsonData;
import com.cmcu.common.service.CommonAttachService;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.HrEmployeeService;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/hr/employee")
public class HrEmployeeController {


    @Autowired
    HrEmployeeService hrEmployeeService;

    @Autowired
    HrEmployeeSysService hrEmployeeSysService;

    @Resource
    CommonAttachService commonAttachService;

    @PostMapping("/update.json")
    public JsonData update(@RequestBody HrEmployeeDto item){
        HrEmployeeDto preDto = hrEmployeeService.getModelById(item.getId());
        if(item.getOperateUserLogin().equals(item.getUserLogin())&&!preDto.getUserStatus().equals(item.getUserStatus())){
            return JsonData.fail("不能修改当前登录人的员工状态状态信息");
        }
        return JsonData.success( hrEmployeeService.update(item));
    }

    @PostMapping("/getModelByUserLogin.json")
    public JsonData getModelByUserLogin(String userLogin){
        return JsonData.success(hrEmployeeService.getModelByUserLogin(userLogin));
    }


    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(hrEmployeeService.getModelById(id));
    }


    @PostMapping("/listByUserLogin.json")
    public JsonData listByUserLogin(String userLogins){
        return JsonData.success(hrEmployeeService.listByUserLogin(userLogins));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel() {
        return JsonData.success(hrEmployeeService.getNewModel());
    }


    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        return JsonData.success(hrEmployeeService.loadPagedData(params, pageNum, pageSize));
    }


    @PostMapping("/listSimplePagedData.json")
    public JsonData listSimplePagedData(int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        return JsonData.success(hrEmployeeService.listSimplePagedData(params, pageNum, pageSize));
    }
    @PostMapping("/listSimpleDeptPagedData.json")
    public JsonData listSimpleDeptPagedData(int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        return JsonData.success(hrEmployeeService.listSimpleDeptPagedData(params, pageNum, pageSize));
    }
    @PostMapping("/listSimpleRolePagedData.json")
    public JsonData listSimpleRolePagedData(int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        return JsonData.success(hrEmployeeService.listSimpleRolePagedData(params, pageNum, pageSize));
    }


    @PostMapping("/selectAllSimple.json")
    public JsonData selectAllSimple() {
        return JsonData.success(hrEmployeeService.selectAllSimple());
    }





    @PostMapping("/listEmployeeByDeptId.json")
    public JsonData listEmployeeByDeptId(int deptId, Boolean containChild) {
        return JsonData.success(hrEmployeeService.listEmployeeByDeptId(deptId,containChild));
    }

    @PostMapping("/listPagedDataByRoleId.json")
    public JsonData listPagedDataByRoleId(int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        return JsonData.success(hrEmployeeService.listPagedDataByRoleId(params,pageNum,pageSize));
    }

    @PostMapping("/listPagedDataByPositionId.json")
    public JsonData listPagedDataByPositionId(int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        return JsonData.success(hrEmployeeService.listPagedDataByPositionId(params,pageNum,pageSize));
    }
    @PostMapping("/listEmployeeByRoleId.json")
    public JsonData listEmployeeByRoleId(int roleId,String q) {
        return JsonData.success(hrEmployeeService.listEmployeeByRoleId(roleId,q));
    }
    @ResponseBody
    @PostMapping("/getConfigData.json")
    public JsonData getConfigData(String userLogin){
        return JsonData.success( hrEmployeeSysService.getConfigData(userLogin));
    }

    @ResponseBody
    @PostMapping("/updateConfigData.json")
    public JsonData updateConfigData(String userLogin,String configData) {
        hrEmployeeSysService.updateConfigData(userLogin, configData);
        return JsonData.success();
    }


    @ResponseBody
    @PostMapping("/updateRoleIds.json")
    public JsonData updateRoleIds(String userLogin,String roleIds) {
        hrEmployeeSysService.updateRoleIds(userLogin,roleIds);
        return JsonData.success();
    }

    @ResponseBody
    @PostMapping("/updatePositionIds.json")
    public JsonData updatePositionIds(String userLogin,String positionIds){
        hrEmployeeSysService.updatePositionIds(userLogin,positionIds);
        return JsonData.success();
    }
    @ResponseBody
    @PostMapping("/updateType.json")
    public JsonData updateType(String userLogin,String type){
        hrEmployeeSysService.updateType(userLogin,type);
        return JsonData.success();
    }
    @ResponseBody
    @PostMapping("/updateStatus.json")
    public JsonData updateStatus(String userLogin,String status){
        hrEmployeeSysService.updateStatus(userLogin,status);
        return JsonData.success();
    }


    @ResponseBody
    @PostMapping("/updateDeptId.json")
    public JsonData updateDeptId(String userLogin,int deptId){
        hrEmployeeSysService.updateDeptId(userLogin,deptId);
        return JsonData.success();
    }

    @ResponseBody
    @PostMapping("/resetPassword.json")
    public JsonData resetPassword(String userLogin,String password){
        hrEmployeeSysService.resetPassword(userLogin,password);
        return JsonData.success();
    }


    @ResponseBody
    @PostMapping("/resetPwd.json")
    public JsonData resetPwd(String userLogin){
        return JsonData.success(hrEmployeeSysService.resetPwd(userLogin));
    }

    @ResponseBody
    @PostMapping("/remove.json")
    public JsonData remove(String userLogin){
        hrEmployeeSysService.remove(userLogin);
        return JsonData.success();
    }

    @ResponseBody
    @PostMapping("/insert.json")
    public JsonData insert(String userLogin,String userName,int deptId,String userType){
        hrEmployeeSysService.insert(userLogin,userName,deptId,userType);
        return JsonData.success();
    }
    @ResponseBody
    @PostMapping("/selectSimpleAll.json")
    public JsonData selectSimpleAll(String selectName){
        List<Map> list = hrEmployeeSysService.selectSimpleAll(selectName);
        return JsonData.success(list);
    }

    @ResponseBody
    @PostMapping("/selectAll.json")
    public JsonData selectAll(){
        return JsonData.success( hrEmployeeService.selectAll());
    }



    @RequestMapping("/downloadModel.json")
    public void  downloadModel(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        String fileName="员工信息.xls";
        HSSFWorkbook hssfWorkbook = hrEmployeeSysService.downloadExcel();
        response.reset();
        response.setContentType("application/csv");
        response.setHeader("Content-Disposition", "attachment; filename=" +  URLEncoder.encode(fileName, "utf-8"));
        hssfWorkbook.write(response.getOutputStream());
    }



    @ResponseBody
    @PostMapping("/listDataByUserLoginList.json")
    public JsonData listDataByUserLoginList(String userLoginList){
        return JsonData.success( hrEmployeeService.listDataByUserLoginList(userLoginList));
    }
    @PostMapping("/selectByUserLoginList.json")
    public JsonData selectByUserLoginList(String userLoginList){
        return JsonData.success( hrEmployeeService.selectByUserLoginList(userLoginList));
    }



    @ResponseBody
    @RequestMapping("/receiveSignDwg.json")
    public JsonData receiveSignDwg(MultipartFile file, String enLogin) throws IOException {
        HrEmployeeDto item = hrEmployeeService.getModelByUserLogin(enLogin);
        int id = commonAttachService.insert(file, file.getOriginalFilename(), "", enLogin);
        item.setSignAttachId(id + "");
        item.setSignUrl(file.getOriginalFilename());
        hrEmployeeService.update(item);
        return JsonData.success(id);
    }


    @ResponseBody
    @RequestMapping("/receiveSignPic.json")
    public JsonData receiveSignPic(MultipartFile file, String enLogin) throws IOException {
        HrEmployeeDto item = hrEmployeeService.getModelByUserLogin(enLogin);
        int id = commonAttachService.insert(file, file.getOriginalFilename(), "", enLogin);
        item.setSignPicAttachId(id + "");
        item.setSignPicUrl(file.getOriginalFilename());
        hrEmployeeService.update(item);
        return JsonData.success(id);
    }

    @ResponseBody
    @RequestMapping("/receiveHead.json")
    public JsonData receiveHead(MultipartFile file, String enLogin) throws IOException {
        HrEmployeeDto item = hrEmployeeService.getModelByUserLogin(enLogin);
        int id = commonAttachService.insert(file, file.getOriginalFilename(), "", enLogin);
        item.setHeadAttachId(id + "");
        item.setAvatar(file.getOriginalFilename());
        hrEmployeeService.update(item);
        return JsonData.success(id);
    }


}
