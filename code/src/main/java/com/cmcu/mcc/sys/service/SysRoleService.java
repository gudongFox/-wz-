package com.cmcu.mcc.sys.service;

import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dao.HrQualifyMapper;
import com.cmcu.mcc.hr.dto.HrDeptDto;
import com.cmcu.mcc.hr.dto.HrEmployeeSimpleDto;
import com.cmcu.mcc.hr.entity.HrDept;
import com.cmcu.mcc.hr.entity.HrQualify;
import com.cmcu.mcc.hr.service.HrDeptService;
import com.cmcu.mcc.hr.service.HrQualifyService;
import com.cmcu.mcc.sys.dao.SysRoleMapper;
import com.cmcu.mcc.sys.dto.SysAclDto;
import com.cmcu.mcc.sys.dto.SysRoleDto;
import com.cmcu.mcc.sys.entity.SysAcl;
import com.cmcu.mcc.sys.entity.SysAclModule;
import com.cmcu.mcc.sys.entity.SysRole;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: Roy
 * Time: 2019/2/26 16:09
 * To change this template use File | Settings | File Templates.
 */
@Service
public class SysRoleService {

    @Resource
    SysRoleMapper sysRoleMapper;
    @Autowired
    HrDeptService hrDeptService;
    @Autowired
    HrQualifyMapper hrQualifyMapper;
    @Autowired
    HrEmployeeMapper hrEmployeeMapper;



    public void insert(SysRole item) {
        ModelUtil.setNotNullFields(item);
        //BeanValidator.check(item);
        sysRoleMapper.insert(item);
    }

    public void update(SysRole item){
        ModelUtil.setNotNullFields(item);
        BeanValidator.check(item);
        sysRoleMapper.updateByPrimaryKey(item);
    }

    public SysRole getModelById(int id) {
        SysRole item = sysRoleMapper.selectByPrimaryKey(id);
        return getDto(item);
    }

    public SysRole getNewModel() {
        Map params= Maps.newHashMap();
        params.put("deleted",false);
        long total = PageHelper.count(() -> sysRoleMapper.selectAll(params));
        SysRole dto = new SysRole();
        dto.setDeleted(false);
        dto.setSeq((int) (total + 1));
        dto.setName("");
        return dto;
    }

    public List<SysRole> listSortedAll(String queryName){
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("queryName",queryName);
        List<SysRole> roles=sysRoleMapper.selectAll(params);
        //roles.stream().sorted(Comparator.comparing(SysRole::getSeq)).collect(Collectors.toList());
        return roles;
    }

    public List<SysRoleDto> listSortedByRoleIds(String roleIds){
        int sortId =9999;
        List<SysRoleDto> roles=new ArrayList<>();
        //根节点
        SysRoleDto root = new SysRoleDto();
        root.setId(sortId);
        root.setName("系统角色");
        root.setDeleted(false);
        root.setUserLogin("");
        root.setSeq(0);
        roles.add(root);

        List<String> ids = MyStringUtil.getStringList(roleIds);
        //如果传入的id有0 则为查询全部角色
        if(ids.contains("0")){
            List<SysRole> sysRoles = listSortedAll("");
            for(SysRole role:sysRoles){
                //排除普通设计人员
                if(role.getId()!=17){
                    SysRoleDto dto = getDto(role);
                    dto.setParentId(root.getId());
                    //dto.setIcon("icon-users");
                    dto.setUserLogin("");
                    roles.add(dto);
                    //添加该角色下的人员
                    Map map = new HashMap();
                    map.put("roleId", dto.getId());
                    List<HrEmployeeSimpleDto> hrEmployeeSimpleDtos = hrEmployeeMapper.selectOaRoleSimpleAll(map);
                    for (int i = 0; i < hrEmployeeSimpleDtos.size(); i++) {
                        SysRoleDto sysRoleDto = new SysRoleDto();
                        sysRoleDto.setParentId(dto.getId());
                        sysRoleDto.setName(hrEmployeeSimpleDtos.get(i).getUserName());
                        sysRoleDto.setUserLogin(hrEmployeeSimpleDtos.get(i).getUserLogin());
                        sysRoleDto.setIcon("icon-user");
                        sortId--;
                        sysRoleDto.setId(sortId);
                        sysRoleDto.setSeq(sortId);
                        roles.add(sysRoleDto);
                    }
                }
            }
        }else {
            for (String id : ids) {
                SysRoleDto dto = getDto(sysRoleMapper.selectByPrimaryKey(Integer.valueOf(id)));
                dto.setParentId(root.getId());
                //dto.setIcon("icon-users");
                dto.setUserLogin("");
                roles.add(dto);
                //添加该角色下的人员
                Map map = new HashMap();
                map.put("roleId", Integer.valueOf(id));
                List<HrEmployeeSimpleDto> hrEmployeeSimpleDtos = hrEmployeeMapper.selectOaRoleSimpleAll(map);
                for (int i = 0; i < hrEmployeeSimpleDtos.size(); i++) {
                    SysRoleDto sysRoleDto = new SysRoleDto();
                    sysRoleDto.setParentId(Integer.valueOf(id));
                    sysRoleDto.setName(hrEmployeeSimpleDtos.get(i).getUserName());
                    sysRoleDto.setUserLogin(hrEmployeeSimpleDtos.get(i).getUserLogin());
                    sysRoleDto.setIcon("icon-user");
                    sortId--;
                    sysRoleDto.setId(sortId);
                    sysRoleDto.setSeq(sortId);
                    roles.add(sysRoleDto);
                }
            }
        }
        List<SysRoleDto> lists =roles.stream().sorted(Comparator.comparing(SysRoleDto::getSeq)).collect(Collectors.toList());
        return lists;
    }

    public List<SysRole> selectAll(){
        Map params= Maps.newHashMap();
        params.put("deleted",false);
        return sysRoleMapper.selectAll(params);
    }

    public String getRoleNames(List<Integer> roleIds) {
        List<SysRole> sysRoles = selectAll();
        return StringUtils.join(sysRoles.stream().filter(p -> roleIds.contains(p.getId())).map(SysRole::getName).collect(Collectors.toList()), ",");
    }

     public SysRole selectByName(String name){
         Map params= Maps.newHashMap();
         params.put("deleted",false);
         params.put("name",name);
         if (PageHelper.count(()->sysRoleMapper.selectAll(params))>0){
             return  sysRoleMapper.selectAll(params).get(0);
         }else {
             return new SysRole();
         }

     }

    private SysRoleDto getDto(SysRole item){
        SysRoleDto dto = SysRoleDto.adapt(item);
        List<String> deptIds =MyStringUtil.getStringList(item.getChildDeptIds());
        String deptNames = "";
        for(String deptId:deptIds){
            HrDeptDto hrDept = hrDeptService.getModelById(Integer.parseInt(deptId));
            deptNames = deptNames + "," + hrDept.getName();
        }

        if(deptNames.length()>100) {
            dto.setChildDeptNames(deptNames.substring(1, 100) + "...");
        }else if(deptNames.length()>1){
            dto.setChildDeptNames(deptNames.substring(1,deptNames.length()));
        }else{
            dto.setChildDeptNames(deptNames);
        }
        return dto;
    }


}
