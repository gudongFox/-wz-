package com.cmcu.mcc.service.impl;


import com.cmcu.common.dto.DeptDto;
import com.cmcu.common.dto.PositionDto;
import com.cmcu.common.dto.RoleDto;
import com.cmcu.common.service.IUserDataService;
import com.cmcu.common.util.JsonMapper;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.hr.dao.HrEmployeeSysMapper;
import com.cmcu.mcc.hr.dao.HrPositionMapper;
import com.cmcu.mcc.hr.entity.*;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.HrQualifyService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.sys.dao.SysRoleMapper;
import com.cmcu.mcc.sys.entity.SysRole;
import com.cmcu.common.dto.UserDto;
import com.cmcu.common.util.GuavaCacheService;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.hr.dao.HrDeptMapper;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserDataService implements IUserDataService {



    @Resource
    HrEmployeeMapper hrEmployeeMapper;

    @Resource
    HrEmployeeSysMapper hrEmployeeSysMapper;

    @Resource
    HrDeptMapper hrDeptMapper;

    @Resource
    HrPositionMapper hrPositionMapper;

    @Resource
    SysRoleMapper sysRoleMapper;

    @Resource
    GuavaCacheService guavaCacheService;

    @Resource
    SelectEmployeeService selectEmployeeService;

    @Resource
    HrQualifyService hrQualifyService;

    @Resource
    HrEmployeeSysService hrEmployeeSysService;


    public static  String USER_PROVIDER_CACHE_KEY="userDataService_SelectAllUser";

    public static  String DEPT_PROVIDER_CACHE_KEY="userDataService_SelectAllDept";


    public List<UserDto> selectAllUser(String tenetId) {
        List<UserDto> list = guavaCacheService.get(USER_PROVIDER_CACHE_KEY, () -> {
            List<UserDto> oList = Lists.newArrayList();
            Map userParams = Maps.newHashMap();
            userParams.put("deleted",false);
            List<HrEmployeeDto> hrEmployeeList = hrEmployeeMapper.selectAll(userParams);
            for (HrEmployeeDto employee : hrEmployeeList) {
                oList.add(getUserDto(employee));
            }
            return Optional.ofNullable(oList);
        });
        return list;
    }

    public List<DeptDto> selectAllDept(String tenetId) {
        List<DeptDto> list = guavaCacheService.get(DEPT_PROVIDER_CACHE_KEY, () -> {
            List<UserDto> userDtoList=selectAllUser(tenetId);
            List<DeptDto> oList = Lists.newArrayList();
            Map params = Maps.newHashMap();
            params.put("deleted",false);
            List<HrDept> deptList = hrDeptMapper.selectAll(params).stream().sorted(Comparator.comparing(HrDept::getSeq)).collect(Collectors.toList());
            for (HrDept hrDept : deptList) {
                DeptDto item = new DeptDto();
                item.setName(hrDept.getName());
                item.setParentId(hrDept.getParentId());
                item.setId(hrDept.getId());
                item.setDeptType(hrDept.getDeptType());
                //2020-12-16 HNZ 取各个部门的正职副职 分管领导
                item.setDeptChargeMen(selectEmployeeService.getParentDeptChargeMen(hrDept.getId(),1,false));//当前部门正职
                item.setDeptViceChargeMen(selectEmployeeService.getParentDeptChargeMen(hrDept.getId(),2,false));//当前部门副值
                item.setDeptDivisionMen(selectEmployeeService.getParentDeptChargeMen(hrDept.getId(),4,false));//当前部门分管领导
                item.setUserCount(userDtoList.stream().filter(p -> p.getDeptId() == hrDept.getId()).count());
                item.setDeptCount(deptList.stream().filter(p -> p.getParentId().equals(hrDept.getId())).count());


                //2020-12-16 HNZ 取各个部门的正职副职 分管领导
                item.setSecondDeptId(selectEmployeeService.getSecondDeptId(hrDept.getId()));//二级单位ID
                item.setSecondLeaderMen(selectEmployeeService.getParentDeptChargeMen(hrDept.getId(),1,true));//二级单位正职
                item.setSecondViceLeaderMen(selectEmployeeService.getParentDeptChargeMen(hrDept.getId(),2,true));//二级单位副职
                item.setSecondChargeMen(selectEmployeeService.getParentDeptChargeMen(hrDept.getId(),4,true));//二级单位部门分管领导
                oList.add(item);
            }
            return Optional.ofNullable(oList);
        });
        return list;
    }

    @Override
    public List<DeptDto> selectAllDept(String tenetId, Integer parentId) {
        List<DeptDto> list = Lists.newArrayList();
        recursiveAddChildDeptList(parentId, selectAllDept(tenetId), list);
        return list;
    }


    private void recursiveAddChildDeptList(int parentId, List<DeptDto> all, List<DeptDto> list){
        List<DeptDto> childList=all.stream().filter(p->p.getParentId()==parentId).collect(Collectors.toList());
        list.addAll(childList);
        childList.forEach(p-> recursiveAddChildDeptList(p.getId(),all,list));
    }








    public List<PositionDto> selectAllPosition(String cCode) {
        List<PositionDto> list=Lists.newArrayList();
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        List<UserDto> userDtoList=selectAllUser(cCode);
        List<HrPosition> positionList=hrPositionMapper.selectAll(params);
        for(HrPosition hrPosition:positionList){
            PositionDto item=new PositionDto();
            item.setId(hrPosition.getId().toString());
            item.setName(hrPosition.getPositionName());
            item.setChildDeptIdList(hrPosition.getChildDeptIds());
            item.setUserCount((int)userDtoList.stream().filter(p->p.getPositionIdList().contains(item.getId())).count());
            list.add(item);
        }
        return list;
    }


    public List<RoleDto> selectAllRole(String tenetId) {
        List<RoleDto> list=Lists.newArrayList();
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        List<UserDto> userDtoList=selectAllUser(tenetId);
        List<SysRole> sysRoles=sysRoleMapper.selectAll(params);
        for(SysRole sysRole:sysRoles){
            RoleDto item=new RoleDto();
            item.setId(sysRole.getId().toString());
            item.setName(sysRole.getName());
            item.setChildDeptIdList(sysRole.getChildDeptIds());
            item.setUserCount((int)userDtoList.stream().filter(p->p.getRoleIdList().contains(item.getId())).count());
            list.add(item);
        }
        return list;
    }

    @Override
    public UserDto selectByEnLogin( String enLogin) {
        if (StringUtils.isNotEmpty(enLogin)) {
            HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(enLogin);
            return getUserDto(hrEmployeeDto);
        }
        UserDto userDto = new UserDto();
        userDto.setEnLogin(enLogin);
       // userDto.setUserNo(enLogin);
        userDto.setTenetId(MccConst.APP_CODE);
        ModelUtil.setNotNullFields(userDto);
        return userDto;
    }












    @Override
    public void updateProperty(String enLogin, Map property) {
        if(property!=null&&property.keySet().size()>0) {
            HrEmployee item = hrEmployeeMapper.selectByUserLoginOrNo(enLogin);
            Assert.state(item != null, "未找到对应用户!");
            if (property.containsKey("signUrl")) {
                item.setSignUrl(property.get("signUrl").toString());
                hrEmployeeMapper.updateByPrimaryKey(item);
            }
            if (property.containsKey("orderMethod")) {
                HrEmployeeSys hrEmployeeSys = hrEmployeeSysMapper.selectByUserLogin(enLogin);
                Map config = Maps.newHashMap();
                if (StringUtils.isNotEmpty(hrEmployeeSys.getConfigData())) {
                    config = JsonMapper.string2Map(hrEmployeeSys.getConfigData());
                }
                config.put("orderMethod", property.get("orderMethod"));
                hrEmployeeSys.setConfigData(JsonMapper.obj2String(config));
                hrEmployeeSysMapper.updateByPrimaryKey(hrEmployeeSys);
            }
            HrEmployeeSys hrEmployeeSys = hrEmployeeSysMapper.selectByUserLogin(enLogin);
            boolean update = false;
            if (property.containsKey("wxId")) {
                String wxId = property.get("wxId").toString();
                if (!hrEmployeeSys.getWxId().equalsIgnoreCase(wxId)) {
                    hrEmployeeSys.setWxId(wxId);
                    hrEmployeeSys.setGmtModified(new Date());
                    hrEmployeeSysMapper.updateByPrimaryKey(hrEmployeeSys);
                }
            }
            if (StringUtils.isEmpty(item.getHeadAttachId()) && property.containsKey("avatar")) {
                item.setAvatar(property.get("avatar").toString());
                update = true;
            }
            if (StringUtils.isEmpty(item.getEnEmail()) && property.containsKey("email")) {
                item.setEnEmail(property.get("email").toString());
                update = true;
            }
            if (property.containsKey("gender")) {
                Boolean gender = Integer.parseInt(property.get("gender").toString()) == 1;
                if (item.getGender() != gender) {
                    item.setGender(gender);
                    update = true;
                }
            }
            if (update) {
                item.setGmtModified(new Date());
                hrEmployeeMapper.updateByPrimaryKey(item);
                clearCache("");
            }

        }
    }

    @Override
    public void clearCache(String tenetId) {
        guavaCacheService.invalidate(USER_PROVIDER_CACHE_KEY);
        guavaCacheService.invalidate(DEPT_PROVIDER_CACHE_KEY);
    }

    @Override
    public List<Integer> getMyDeptList(String enLogin, String uiSref) {
        return hrEmployeeSysService.getMyDeptList(enLogin,uiSref);
    }

    @Override
    public Map getKeyInfoByEnLogin(String enLogin) {
        Map map= hrEmployeeSysMapper.getKeyInfoByEnLogin(enLogin);
        if(map!=null){
            map.put("tenetId",MccConst.APP_CODE);
        }
        return map;
    }

    @Override
    public String getTenetId(String enLogin) {
        return MccConst.APP_CODE;
    }


    private  UserDto getUserDto(HrEmployeeDto employee) {
        if(employee==null) return null;
        UserDto userDto = new UserDto();
        userDto.setId(employee.getId());
        userDto.setTenetId(MccConst.APP_CODE);
        userDto.setCnName(employee.getUserName());
        userDto.setEnLogin(employee.getUserLogin());
        userDto.setUserNo(employee.getUserNo());
        userDto.setDeptId(employee.getDeptId());
        userDto.setDeptName(employee.getDeptName());
        userDto.setRoleIdList(MyStringUtil.getStringList(employee.getRoleIds()));
        userDto.setPositionIdList(MyStringUtil.getStringList(employee.getPositionIds()));
        if(StringUtils.isNotEmpty(employee.getHeadAttachId())){
            userDto.setAvatar("/common/attach/download/" + employee.getHeadAttachId());
        }else if(StringUtils.isNotEmpty(employee.getAvatar())&&employee.getAvatar().startsWith("http")){
            userDto.setAvatar(employee.getAvatar());
        }
        userDto.setWxId(employee.getWxId());
        userDto.setLogoGram(employee.getLogoGram());
        userDto.setUserStatus(employee.getUserStatus());
        HrEmployeeSys hrEmployeeSys = hrEmployeeSysMapper.selectByUserLogin(userDto.getEnLogin());
        Map config = Maps.newHashMap();
        if (StringUtils.isNotEmpty(hrEmployeeSys.getConfigData())) {
            config = JsonMapper.string2Map(hrEmployeeSys.getConfigData());
        }
        userDto.setOrderMethod(config.getOrDefault("orderMethod", "name").toString());
        if(StringUtils.isNotEmpty(employee.getSignUrl())&&employee.getSignUrl().startsWith("http")){
            userDto.setSignUrl(employee.getSignUrl());
        }else if(StringUtils.isNotEmpty(employee.getSignAttachId())) {
            userDto.setSignUrl("/common/attach/download/" + employee.getSignAttachId());
        }
        userDto.setEncryptPwd(hrEmployeeSys.getPassword());
        if (StringUtils.isNotEmpty(employee.getWxId())) {
            userDto.setWxId(employee.getWxId());
        }
        HrQualify hrQualify=hrQualifyService.selectLatestByUserLogin(userDto.getEnLogin());
        if(hrQualify!=null){
            if(hrQualify.getApprove()) userDto.getQualifyList().add("approve");
            if(hrQualify.getAudit()) userDto.getQualifyList().add("audit");
            if(hrQualify.getProofread()) userDto.getQualifyList().add("proofread");
            if(hrQualify.getDesign()) {
                userDto.getQualifyList().add("design");
                userDto.getQualifyList().add("criterion");
            }
            if(hrQualify.getMajorCharge()) userDto.getQualifyList().add("majorCharge");
            if(hrQualify.getProjectCharge()) userDto.getQualifyList().add("projectCharge");
            if(hrQualify.getProjectExeCharge()) userDto.getQualifyList().add("projectExeCharge");
            if(hrQualify.getProChief()) userDto.getQualifyList().add("totalDesigner");
        }
        ModelUtil.setNotNullFields(userDto);
        return userDto;
    }

}
