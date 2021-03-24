package com.cmcu.mcc.sys.service;

import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.hr.dao.HrDeptMapper;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dao.HrEmployeeSysMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.entity.HrDept;
import com.cmcu.mcc.hr.entity.HrEmployee;
import com.cmcu.mcc.hr.entity.HrEmployeeSys;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysRoleUserService {



    @Resource
    HrDeptMapper hrDeptMapper;

    @Resource
    HrEmployeeMapper hrEmployeeMapper;

    @Resource
    HrEmployeeSysMapper hrEmployeeSysMapper;

    @Autowired
    SysRoleService sysRoleService;




    public List<Map> getUserTree(int roleId) {
        List<Map> mapList = Lists.newArrayList();
        Map<String, Object> params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("userStatus", "在职");
        List<HrDept> deptList = hrDeptMapper.selectAll(params);
        List<HrEmployeeDto> userList = hrEmployeeMapper.selectAll(params).stream().sorted(Comparator.comparing(HrEmployee::getUserName)).collect(Collectors.toList());
        for (HrDept dept : deptList) {
            Map map = Maps.newHashMap();
            Map stateMap=Maps.newHashMap();
            map.put("id", "dept_" + dept.getId());
            map.put("text", dept.getName());
            if (dept.getParentId() == 0) {
                map.put("parent", "#");
                stateMap.put("opened",true);
            } else {
                map.put("parent", "dept_" + dept.getParentId());
                stateMap.put("opened",false);
            }

            map.put("state",stateMap);
            mapList.add(map);
            List<HrEmployeeDto> employeeDtos=userList.stream().filter(p ->dept.getId().equals(p.getDeptId())).collect(Collectors.toList());
            for (HrEmployeeDto employee : employeeDtos) {
                Map aclMap = Maps.newHashMap();
                aclMap.put("id", employee.getUserLogin());
                aclMap.put("text", employee.getUserName());
                aclMap.put("parent", "dept_" + employee.getDeptId());
                Map aclStateMap = Maps.newHashMap();
                aclStateMap.put("opened",false);
                aclStateMap.put("disabled",false);
                aclStateMap.put("selected", employee.getRoleIds().contains(","+roleId+","));
                aclMap.put("icon","glyphicon glyphicon-user");
                aclMap.put("state", aclStateMap);
                mapList.add(aclMap);
            }
        }
        return mapList;
    }

    @Transactional
    public void saveRoleUserList(int roleId,String userList){
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("roleId", roleId);

        //处理以前的,删除被取消的角色权限
        List<HrEmployeeDto> employees=hrEmployeeMapper.selectAll(params);
        List<String> userLoginList= MyStringUtil.getStringList(userList).stream().filter(p->!p.contains("dept_")).collect(Collectors.toList());
        for(HrEmployeeDto hrEmployeeDto:employees){
            if(!userLoginList.contains(hrEmployeeDto.getUserLogin())){
                HrEmployeeSys hrEmployeeSys=hrEmployeeSysMapper.selectByUserLogin(hrEmployeeDto.getUserLogin());
                List<Integer> roleIds=MyStringUtil.getIntList(hrEmployeeSys.getRoleIds());
                roleIds.remove(roleIds.indexOf(roleId));
                hrEmployeeSys.setRoleIds(","+StringUtils.join(roleIds,",")+",");
                hrEmployeeSys.setRoleNames(","+sysRoleService.getRoleNames(roleIds)+",");
                hrEmployeeSysMapper.updateByPrimaryKey(hrEmployeeSys);
            }
        }

        //增加
        for(String userLogin:userLoginList) {
            HrEmployeeSys hrEmployeeSys=hrEmployeeSysMapper.selectByUserLogin(userLogin);
            List<Integer> roleIds=MyStringUtil.getIntList(hrEmployeeSys.getRoleIds());
            if(!roleIds.contains(roleId)){
                roleIds.add(roleId);
                hrEmployeeSys.setRoleIds(","+StringUtils.join(roleIds,",")+",");
                hrEmployeeSys.setRoleNames(","+sysRoleService.getRoleNames(roleIds)+",");
                hrEmployeeSysMapper.updateByPrimaryKey(hrEmployeeSys);
            }
        }
    }
}
