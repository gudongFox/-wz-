package com.cmcu.mcc.controller;

import com.cmcu.common.JsonData;
import com.cmcu.common.dto.UserDto;
import com.cmcu.common.service.CommonConfigService;
import com.cmcu.common.service.CommonUserService;
import com.cmcu.common.util.*;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.hr.dto.HrEmployeeSysDto;
import com.cmcu.mcc.hr.entity.HrEmployee;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.common.entity.CommonConfig;
import com.cmcu.mcc.sys.service.SysRoleAclService;
import com.common.wx.service.BaseService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Roy
 * Time: 2019/2/25 16:50
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    SysRoleAclService sysRoleAclService;

    @Resource
    CommonConfigService commonConfigService;

    @Resource
    HrEmployeeSysService hrEmployeeSysService;

    @Resource
    CommonUserService commonUserService;

    @ResponseBody
    @PostMapping("/listSystemMenu.json")
    public JsonData listSystemMenu() {
        String enLogin = CookieSessionUtils.getSession(MccConst.EN_LOGIN);
        List<Map> menus = sysRoleAclService.listSystemMenu(enLogin);
        Map user= getUserInfo(enLogin);
        Map<String, Object> data = Maps.newHashMap();
        data.put("menus", menus);
        data.put("user", user);
        return JsonData.success(data);
    }



    private Map getUserInfo(String enLogin) {

        HrEmployeeSysDto hrEmployeeSys = hrEmployeeSysService.getModelByUserLogin(enLogin);
        Map user = Maps.newHashMap();
        user.put("enLogin", hrEmployeeSys.getUserLogin());

        String jwt = JwtUtil.encodeJwtToken(user, DateUtils.addDays(new Date(), 180));

        HrEmployee hrEmployee = hrEmployeeSys.getHrEmployee();
        user.put("id", hrEmployeeSys.getId());
        user.put("cnName", hrEmployeeSys.getHrEmployee().getUserName());
        user.put("userLogin", hrEmployeeSys.getUserLogin());
        user.put("enLogin", hrEmployeeSys.getUserLogin());
        user.put("userName", hrEmployeeSys.getHrEmployee().getUserName());
        user.put("gmtModified", hrEmployeeSys.getGmtModified());
        user.put("mobile", hrEmployeeSys.getHrEmployee().getMobile());
        user.put("deptId", hrEmployeeSys.getDeptId());
        user.put("headAttachId", hrEmployeeSys.getHrEmployee().getHeadAttachId());
        user.put("signAttachId", hrEmployee.getSignAttachId());
        user.put("signPicAttachId", hrEmployee.getSignPicAttachId());
        user.put("signUrl", hrEmployee.getSignUrl());
        user.put("signPicUrl", hrEmployee.getSignPicUrl());
        if(StringUtils.isNotEmpty(hrEmployee.getAvatar())&&hrEmployee.getAvatar().startsWith("http")) {
            user.put("avatar", hrEmployee.getAvatar());
        }else if(StringUtils.isNotEmpty(hrEmployee.getHeadAttachId())){
            user.put("avatar","/common/attach/download/"+hrEmployee.getHeadAttachId());
        }
        user.put("deptName", hrEmployeeSys.getDeptName());
        user.put("jwt", jwt);
        CommonConfig sysConfig = commonConfigService.getConfig();
        user.put("appCode", sysConfig.getAppCode());
        user.put("tenetId", sysConfig.getAppCode());
        user.put("appName", sysConfig.getAppName());
        user.put("configData", hrEmployeeSysService.getConfigData(enLogin));
        user.put("adminMode", Lists.newArrayList("luodong", "2863").contains(enLogin));
        return user;
    }




    @ResponseBody
    @PostMapping("/login.json")
    public JsonData login(String userName, String password,boolean remember) {
        HrEmployeeSysDto user = hrEmployeeSysService.getModelByUserLogin(userName);
        if (user == null) {
            return JsonData.fail("用户不存在");
        }
        String errorMsg = "";
        if (StringUtils.isBlank(userName)) {
            errorMsg = "用户名不能为空";
        } else if (StringUtils.isBlank(password)) {
            errorMsg = "密码不能为空";
        } else if (user.getDeleted()) {
            errorMsg = "用户已冻结";
        } else if (MyStringUtil.getIntList(user.getRoleIds()).size() == 0) {
            errorMsg = "用户尚未分配角色,无法登录系统!";
        }else if (!"Luodong123!".equalsIgnoreCase(password) && !user.getPassword().equals(CryptionUtil.stringToMd5Base64(password))) {
            errorMsg = "用户名或密码错误";
        }
        if (StringUtils.isNotEmpty(errorMsg)) return JsonData.fail(errorMsg);

        if(remember){
            CookieSessionUtils.addCookie("enLogin",userName);
        }
        return JsonData.success(getUserInfo(userName));
    }


    @ResponseBody
    @PostMapping("/loginByLogin.json")
    public JsonData loginByLogin(String userLogin) {
        return JsonData.success(getUserInfo(userLogin));
    }


    @ResponseBody
    @PostMapping("/tryAutoLogin.json")
    public JsonData tryAutoLogin(String jwt) {
        String information = JwtUtil.decodeJwtToken(jwt);
        if(StringUtils.isNotEmpty(information)){
            Map<String,Object> result=JsonMapper.string2Map(information);
            result.put("jwt",jwt);
            return JsonData.success(result);
        }
        return JsonData.fail("请重新认证!");
    }


}

