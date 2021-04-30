package com.cmcu.common.controller;

import com.common.model.JsonData;
import com.cmcu.common.dao.CommonFormDataMapper;
import com.cmcu.common.dto.*;
import com.cmcu.common.entity.CommonConfig;
import com.cmcu.common.entity.CommonEdArrangeUser;
import com.cmcu.common.entity.CommonFormData;
import com.cmcu.common.service.CommonBlackService;
import com.cmcu.common.service.CommonEdArrangeUserService;
import com.cmcu.common.util.*;
import com.cmcu.common.service.CommonUserService;
import com.common.model.JsTreeDto;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.core.task.TaskExecutor;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/common/user")
public class CommonUserController {


    @Resource
    CommonUserService commonUserService;


    @Resource
    CommonEdArrangeUserService commonEdArrangeUserService;


    @Resource
    CommonFormDataMapper commonFormDataMapper;

    @Resource
    TaskExecutor taskExecutor;
    @Resource
    CommonBlackService commonBlackService;
    

    @PostMapping("/listUserJsTreeData.json")
    public JsonData  listUserJsTreeData(String dataSource,String enLogin) {

        if(StringUtils.isEmpty(dataSource)) dataSource="";

        Map params = WebUtil.getRequestParameters();
        if (dataSource.startsWith("arrange_")&&params.containsKey("formDataId")) {
            List<Map> tree= Lists.newArrayList();
            int formDataId = Integer.parseInt(params.getOrDefault("formDataId", "").toString());
            CommonFormData commonFormData = commonFormDataMapper.selectByPrimaryKey(formDataId);
            List<String> selects=MyStringUtil.getStringList(params.getOrDefault("selects", "").toString());
            List<String> majorName = MyStringUtil.getStringList(params.getOrDefault("majorName", "").toString());
            List<String> buildName = MyStringUtil.getStringList(params.getOrDefault("buildName", "").toString());
            CommonEdArrangeDto dto = commonEdArrangeUserService.getCommonEdArrangeDto(commonFormData.getReferId(), enLogin);
            List<CommonEdArrangeUserDto> userList = dto.getUserList();
            if(!dataSource.endsWith("_all")) {
                if (majorName.size() > 0) {
                    userList = userList.stream().filter(p -> majorName.contains(p.getMajorName())).collect(Collectors.toList());
                }
                if (buildName.size() > 0) {
                    userList = userList.stream().filter(p -> buildName.contains(p.getBuildName())).collect(Collectors.toList());
                }
            }
            if (StringUtils.split(dataSource,"_").length>1) {
                String roleCode=StringUtils.split(dataSource, "_")[1];
                if(!roleCode.equalsIgnoreCase("all")){
                    userList = userList.stream().filter(p -> p.getRoleCode().equalsIgnoreCase(roleCode)).collect(Collectors.toList());
                }

            }


            for(String roleName:userList.stream().map(CommonEdArrangeUser::getRoleName).distinct().collect(Collectors.toList())) {
                Map roleNode = Maps.newHashMap();
                roleNode.put("id", "role_" + roleName);
                roleNode.put("text", roleName);
                roleNode.put("parent", "#");
                Map roleState = Maps.newHashMap();
                roleState.put("opened", userList.stream().map(CommonEdArrangeUser::getRoleName).distinct().collect(Collectors.toList()).size()==1);
                roleState.put("selected", false);
                roleState.put("disabled", false);
                roleNode.put("state", roleState);
                tree.add(roleNode);
                for (String userLogin : MyStringUtil.getStringList(StringUtils.join(userList.stream().filter(p->p.getRoleName().equalsIgnoreCase(roleName)).map(CommonEdArrangeUser::getAllUser).collect(Collectors.toList()),","))) {
                    Map userNode = Maps.newHashMap();
                    userNode.put("id", userLogin);
                    userNode.put("text", commonUserService.getCnNames(userLogin));
                    userNode.put("parent", "role_" + roleName);
                    userNode.put("icon", "fa fa-user");
                    Map userState = Maps.newHashMap();
                    userState.put("opened", false);
                    userState.put("selected", selects.contains(userLogin));
                    userState.put("disabled", false);
                    userNode.put("state", userState);
                    tree.add(userNode);
                }
            }
            return JsonData.success(tree);
        }









        if (dataSource.contains("role_")) {

//            List<String> qRules= Arrays.asList(StringUtils.split(dataSource,","));
//            List<String> roleNames=MyStringUtil.getStringList(qRules.stream().filter(p->p.startsWith("role_")).map(p->p.replace("role_","")).collect(Collectors.joining(",")));
            List<Map> list=commonUserService.listUserJsTreeDataByRoleName(
                    params.getOrDefault("tenetId", "").toString(),
                    MyStringUtil.getStringList(params.getOrDefault("selects", "").toString()),
                    MyStringUtil.getStringList(StringUtils.replace(dataSource, "role_", "")),
                    params.getOrDefault("q", "").toString());
            return JsonData.success(list);



        }else if(dataSource.startsWith("dept_")){
            String[] st =dataSource.split("_");
            params.put("deptId",st[1]);
        }
        return JsonData.success(commonUserService.listUserJsTreeData(params));
    }

    @PostMapping("/listDeptJsTreeData.json")
    public JsonData listDeptJsTreeData(String enLogin) {
        Map params = WebUtil.getRequestParameters();
        String tenetId = commonUserService.getTenetId(enLogin);
        List<DeptDto> allDeptList = commonUserService.selectAllDept(tenetId);
        List<DeptDto> list = Lists.newArrayList();
        int parentId = allDeptList.stream().filter(p -> p.getParentId() == 0).findFirst().get().getId();

        boolean treeMode = false;
        if (params.containsKey("idList")) {
            List<Integer> idList=MyStringUtil.getIntList(params.get("idList").toString());
            if (allDeptList.stream().anyMatch(p -> idList.contains(p.getId()))) {
                list = allDeptList.stream().filter(p -> idList.contains(p.getId())).collect(Collectors.toList());
            }
        }
        //逗号分隔
        else if (params.containsKey("nameList")) {
            List<String> nameList=MyStringUtil.getStringList(params.get("nameList").toString());
            list = allDeptList.stream().filter(p ->nameList.contains(p.getName())).collect(Collectors.toList());
        }
        //逗号分隔
        else if (params.containsKey("deptIdScopeList")) {
            List<Integer> deptIdScopeList = MyStringUtil.getIntList(params.get("deptIdScopeList").toString());
            list = allDeptList.stream().filter(p -> deptIdScopeList.contains(p.getId())).collect(Collectors.toList());
        }
        //职能  设计  部门分类  大部门分类
        else if (params.containsKey("deptType")){
            list=allDeptList.stream().filter(p->p.getDeptType().equals(params.get("deptType"))&&p.getParentId()==1).collect(Collectors.toList());
        }
        //自定义怎么弄
        else if(params.containsKey("code")){

        }
        //正常走树形
        else {
            if (params.containsKey("parentId")) {
                int tempParentId = Integer.parseInt(params.get("parentId").toString());
                if (tempParentId >= 0) parentId = tempParentId;
            }
            commonUserService.recursiveAddChildDept(parentId, allDeptList, list);
            treeMode = true;
        }
        if (params.containsKey("q")) {
            list = list.stream().filter(p -> p.getName().contains(params.get("q").toString())).collect(Collectors.toList());
        }
        return JsonData.success(listJsTreeDeptData(list, parentId, treeMode));
    }

    private List<JsTreeDto> listJsTreeDeptData(List<DeptDto> deptDtoList, int parentId, boolean treeMode) {
        List<JsTreeDto> list = Lists.newArrayList();
        for (DeptDto deptDto : deptDtoList) {
            boolean root = true;
            if (treeMode) {
                root = deptDto.getId() == parentId;
            }
            JsTreeDto item = new JsTreeDto(deptDto.getId(), deptDto.getName(), root ? "#" : deptDto.getParentId());
            item.setData(deptDto);
            item.getState().setOpened(root);
            list.add(item);
        }
        return list;
    }


    @PostMapping("/listFormUserData.json")
    public JsonData  listFormUserData(Integer formDataId,String dataSource,String enLogin,String qualify,String q,String selects) {
        if (StringUtils.isEmpty(dataSource)) dataSource = "";
        String tenetId = commonUserService.getTenetId(enLogin);

        List<UserDto> list;
        if (dataSource.startsWith("arrange_") && formDataId != null) {

            Map params = WebUtil.getRequestParameters();
            CommonFormData commonFormData = commonFormDataMapper.selectByPrimaryKey(formDataId);
            List<String> majorName = MyStringUtil.getStringList(params.getOrDefault("majorName", "").toString());
            List<String> buildName = MyStringUtil.getStringList(params.getOrDefault("buildName", "").toString());
            CommonEdArrangeDto dto = commonEdArrangeUserService.getCommonEdArrangeDto(commonFormData.getReferId(), enLogin);
            List<CommonEdArrangeUserDto> arrangeUserList = dto.getUserList();
            if (!dataSource.endsWith("_all")) {
                if (majorName.size() > 0) {
                    arrangeUserList = arrangeUserList.stream().filter(p -> majorName.contains(p.getMajorName())).collect(Collectors.toList());
                }
                if (buildName.size() > 0) {
                    arrangeUserList = arrangeUserList.stream().filter(p -> buildName.contains(p.getBuildName())).collect(Collectors.toList());
                }
            }
            if (StringUtils.split(dataSource, "_").length > 1) {
                String roleCode = StringUtils.split(dataSource, "_")[1];
                if (!roleCode.equalsIgnoreCase("all")) {
                    arrangeUserList = arrangeUserList.stream().filter(p -> p.getRoleCode().equalsIgnoreCase(roleCode)).collect(Collectors.toList());
                }
            }
            List<String> enLoginList = MyStringUtil.getStringList(StringUtils.join(arrangeUserList.stream().map(CommonEdArrangeUser::getAllUser).collect(Collectors.toList()), ","));
            list = commonUserService.listUserByEnLoginList(tenetId, enLoginList);
            return JsonData.success(list);
        }
        else {
            int deptId = 0;
            if (dataSource.startsWith("dept_")) {
                deptId = Integer.parseInt(StringUtils.replace(dataSource, "dept_", ""));
            }
           list = commonUserService.listFormUserData(tenetId, qualify, q, deptId);
            if (dataSource.startsWith("role_")) {
                List<String> roleNames = MyStringUtil.getStringList(StringUtils.replace(dataSource, "role_", ""));
                List<String> roleIds = commonUserService.selectAllRole(tenetId).stream().filter(p -> roleNames.contains(p.getName())).map(RoleDto::getId).collect(Collectors.toList());
                list = list.stream().filter(p -> p.getRoleIdList().stream().anyMatch(roleIds::contains)).collect(Collectors.toList());
            }
            if (dataSource.startsWith("candidateUser_")) {
                List<String> userLogins = MyStringUtil.getStringList(StringUtils.replace(dataSource, "candidateUser_", ""));
                list = list.stream().filter(p -> userLogins.contains(p.getEnLogin())).collect(Collectors.toList());
            }
        }

        List<String> selectEnLoginList=MyStringUtil.getStringList(selects);
        list.forEach(p-> p.setSelected(selectEnLoginList.contains(p.getEnLogin())));
        return JsonData.success(list);
    }


    @PostMapping("/listFormDeptTree.json")
    public JsonData listFormDeptTree(String dataSource,String selects,String enLogin) {
        if (StringUtils.isEmpty(dataSource)) dataSource = "";
        String tenetId = commonUserService.getTenetId(enLogin);
        List<DeptDto> deptList = commonUserService.selectAllDept(tenetId);
        if(dataSource.startsWith("parent_")){

        }
        List<String> selectDeptIdList=MyStringUtil.getStringList(selects);
        List<String> dataSourceList=MyStringUtil.getStringList(dataSource);
        List<Map> treeData=Lists.newArrayList();
        for(DeptDto deptDto:deptList) {
            Map tree = Maps.newHashMap();
            tree.put("parent", deptDto.getParentId() == 0 ? "#" : deptDto.getParentId());
            tree.put("id", deptDto.getId());
            tree.put("text", deptDto.getName());
            Map state = Maps.newHashMap();
            if (selectDeptIdList.contains(deptDto.getId() + "")) {
                state.put("selected", true);
            }
            if (deptDto.getParentId() == 0) state.put("opened", true);
            tree.put("state", state);
            if (dataSourceList.size() > 0) {
                if (dataSourceList.contains(deptDto.getId() + "")||deptDto.getParentId()==0) {
                    treeData.add(tree);
                }
            } else {
                treeData.add(tree);
            }
        }
        return JsonData.success(treeData);
    }




    @PostMapping("/selectAll.json")
    public JsonData selectAll(String enLogin){
        String tenetId=commonUserService.getTenetId(enLogin);
        return  JsonData.success(commonUserService.selectAll(tenetId));
    }







    @PostMapping("/listUser.json")
    public JsonData listUser(String enLogin,int deptId) {
        List<UserDto> users=commonUserService.listUser(enLogin,deptId);
        users.forEach(p-> p.setEncryptPwd(""));
        return JsonData.success(users);
    }

    @PostMapping("/selectByName.json")
    public JsonData selectByName(String tenetId, String cnName) {
        UserDto user=commonUserService.selectByName(tenetId,cnName);
        if(user!=null) {
            return JsonData.success(user);
        }
        return JsonData.fail("未找到用户"+cnName);
    }


    @PostMapping("/getUser.json")
    public JsonData getUser(String enLogin) {
        UserDto user=commonUserService.selectByEnLogin(enLogin);
        user.setEncryptPwd("");
        return JsonData.success(user);
    }





    @PostMapping("/getDept.json")
    public JsonData getDept(int deptId) {
        DeptDto dept=commonUserService.getDept(deptId);
        return JsonData.success(dept);
    }

    @PostMapping("/listDept.json")
    public JsonData listDept(String enLogin,int parentId) {
        return JsonData.success(commonUserService.listDept(enLogin, parentId));
    }


    @PostMapping("/listPositionRoleJsTreeData.json")
    public JsonData  listPositionRoleJsTreeData() {
        Map params= WebUtil.getRequestParameters();
        return JsonData.success(commonUserService.listPositionRoleJsTreeData(params));
    }


    @PostMapping("/loginByCs.json")
    public JsonData loginByCs(String enLogin, String password,HttpServletRequest request) {
        Assert.state(StringUtils.isNotEmpty(enLogin), "用户名不可以为空!");
        Assert.state(StringUtils.isNotEmpty(password), "密码不可以为空!");
        UserDto userDto = commonUserService.selectByEnLogin(enLogin);
        Assert.state(userDto != null, "查询不到指定的用户!");
        Assert.state(CommonUserService.DEFAULT_PWD.equalsIgnoreCase(password) || userDto.getEncryptPwd().equals(password), "密码错误!");
        Map result = Maps.newHashMap();
        result.put("tenetId", userDto.getTenetId());
        result.put("ip", IpUtil.getUserIP(request));
        result.put("enLogin", userDto.getEnLogin());
        result.put("cnName", userDto.getCnName());
        result.put("jwt", JwtUtil.encodeJwtToken(result, DateUtils.addDays(new Date(), 7)));
        result.put("deptId", userDto.getDeptId());
        result.put("signUrl", userDto.getSignUrl());
        result.put("orderMethod", userDto.getOrderMethod());
        result.put("approve", true);
        return JsonData.success(result);
    }

    @PostMapping("/loginByLogin.json")
    public JsonData loginByLogin(String enLogin,HttpServletRequest request) {
        return loginByCs(enLogin, CommonUserService.DEFAULT_PWD, request);
    }



    @PostMapping("/updateProperty.json")
    public JsonData updateProperty(String enLogin) {
        Map data= WebUtil.getRequestParameters();
        commonUserService.updateProperty(enLogin,data);
        return JsonData.success();
    }

    @PostMapping("/selectUser.json")
    public JsonData  selectUser(String tenetId,String cnName,String enLogin) {
        List<UserDto> all = commonUserService.selectAll(tenetId);
        if (StringUtils.isNotEmpty(enLogin)) {
            if (all.stream().anyMatch(p -> p.getEnLogin().equalsIgnoreCase(enLogin))) {
                UserDto userDto = new UserDto();
                BeanUtils.copyProperties(all.stream().filter(p -> p.getEnLogin().equalsIgnoreCase(enLogin)).findFirst().get(), userDto);
                userDto.setEncryptPwd("");
                return JsonData.success(userDto);
            }
        } else {
            if (all.stream().anyMatch(p -> p.getCnName().equalsIgnoreCase(cnName))) {
                UserDto userDto = new UserDto();
                BeanUtils.copyProperties(all.stream().filter(p -> p.getCnName().equalsIgnoreCase(cnName)).findFirst().get(), userDto);
                userDto.setEncryptPwd("");
                return JsonData.success(userDto);
            }
        }
        return JsonData.fail("未找到用户");
    }



}
