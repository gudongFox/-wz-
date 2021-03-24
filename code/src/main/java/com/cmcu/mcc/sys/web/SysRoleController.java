package com.cmcu.mcc.sys.web;

import com.cmcu.common.JsonData;
import com.cmcu.mcc.sys.dto.SysRoleAclDto;
import com.cmcu.mcc.sys.dto.SysRoleDto;
import com.cmcu.mcc.sys.entity.SysRole;
import com.cmcu.mcc.sys.service.SysRoleAclService;
import com.cmcu.mcc.sys.service.SysRoleService;
import com.cmcu.mcc.sys.service.SysRoleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/sys/role")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysRoleAclService sysRoleAclService;

    @Autowired
    private SysRoleUserService sysRoleUserService;

    @RequestMapping("/index")
    public ModelAndView index(){
        ModelAndView modelAndView=new ModelAndView("/sys/role");
        return modelAndView;
    }

    @ResponseBody
    @PostMapping("/insert.json")
    public JsonData insert(@RequestBody SysRole item){
        sysRoleService.insert(item);
        return JsonData.success();
    }

    @ResponseBody
    @PostMapping("/update.json")
    public JsonData update(@RequestBody SysRole item){
        sysRoleService.update(item);
        return JsonData.success();
    }

    @ResponseBody
    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
        SysRole dto= sysRoleService.getModelById(id);
        return JsonData.success(dto);
    }

    @ResponseBody
    @PostMapping("/getNewModel.json")
    public JsonData getNewModel( ){
        SysRole dto= sysRoleService.getNewModel();
        return JsonData.success(dto);
    }


    @ResponseBody
    @PostMapping("/listSortedAll.json")
    public JsonData listSortedAll(String queryName) {
        List<SysRole> list = sysRoleService.listSortedAll(queryName);
        return JsonData.success(list);
    }
    @ResponseBody
    @PostMapping("/listSortedByRoleIds.json")
    public JsonData listSortedByRoleIds(String roleIds) {
        List<SysRoleDto> list = sysRoleService.listSortedByRoleIds(roleIds);
        return JsonData.success(list);
    }

    @ResponseBody
    @PostMapping("/selectAll.json")
    public JsonData selectAll() {
        List<SysRole> list = sysRoleService.selectAll();
        return JsonData.success(list);
    }

    @ResponseBody
    @PostMapping("/listAclByRoleId.json")
    public JsonData listAclByRoleId(String q,int roleId) {
        List<SysRoleAclDto> list = sysRoleAclService.listAclByRoleId(q, roleId);
        return JsonData.success(list);
    }

    @ResponseBody
    @PostMapping("/deleteAcl.json")
    public JsonData deleteAcl(int id) {
        sysRoleAclService.delete(id);
        return JsonData.success();
    }

    @ResponseBody
    @PostMapping("/getAclTreeByRoleId.json")
    public JsonData getAclTreeByRoleId(int roleId) {
         List<Map> treeData=sysRoleAclService.getAclTreeByRoleId(roleId);
        return JsonData.success(treeData);
    }

    @ResponseBody
    @PostMapping("/getAclTreeByUserLogin.json")
    public JsonData getAclTreeByUserLogin(String userLogin) {
        List<Map> treeData=sysRoleAclService.getAclTreeByUserLogin(userLogin);
        return JsonData.success(treeData);
    }

    @ResponseBody
    @PostMapping("/listMyAclInfoByUserLogin.json")
    public JsonData listMyAclInfoByUserLogin(String userLogin) {
        List<Map> aclInfoList=sysRoleAclService.listMyAclInfoByUserLogin(userLogin);
        return JsonData.success(aclInfoList);
    }


    @ResponseBody
    @PostMapping("/saveUserAclDeptConfig.json")
    public JsonData saveUserAclDeptConfig (String userLogin,int aclId,String selectDepts) {
        sysRoleAclService.saveUserAclDeptConfig(userLogin,aclId,selectDepts);
        return JsonData.success();
    }

    @ResponseBody
    @PostMapping("/removeUserAcl.json")
    public JsonData removeUserAcl (String userLogin,int aclId,String type) {
        sysRoleAclService.removeUserAcl(userLogin,aclId,type);
        return JsonData.success();
    }

    @ResponseBody
    @PostMapping("/saveRoleAclList.json")
    public JsonData saveRoleAclList(int roleId,String aclList) {
        sysRoleAclService.saveRoleAclList(roleId,aclList);
        return JsonData.success();
    }

    @ResponseBody
    @PostMapping("/saveUserAclList.json")
    public JsonData saveUserAclList(String userLogin,String aclList) {
        sysRoleAclService.saveUserAclList(userLogin,aclList);
        return JsonData.success();
    }


    @ResponseBody
    @PostMapping("/getUserTree.json")
    public JsonData getUserTree(int roleId) {
        List<Map> treeData=sysRoleUserService.getUserTree(roleId);
        return JsonData.success(treeData);
    }

    @ResponseBody
    @PostMapping("/saveRoleUserList.json")
    public JsonData saveRoleUserList(int roleId,String userList) {
        sysRoleUserService.saveRoleUserList(roleId,userList);
        return JsonData.success();
    }


    @ResponseBody
    @PostMapping("/getAclInfoByUserLogin.json")
    public JsonData getAclInfoByUserLogin(String userLogin,String uiSref){
        return JsonData.success(sysRoleAclService.getAclInfoByUserLogin(userLogin,uiSref));
    }
}
