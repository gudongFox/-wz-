package com.cmcu.mcc.sys.service;

import com.cmcu.common.util.GuavaCacheService;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dao.HrPositionMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.entity.HrPosition;
import com.cmcu.mcc.sys.dao.*;
import com.cmcu.mcc.sys.dto.SysAclDto;
import com.cmcu.mcc.sys.dto.SysRoleAclDto;
import com.cmcu.mcc.sys.entity.*;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SysRoleAclService {

    @Resource
    GuavaCacheService guavaCacheService;

    @Resource
    SysRoleAclMapper sysRoleAclMapper;

    @Resource
    SysAclModuleMapper sysAclModuleMapper;

    @Resource
    SysAclMapper sysAclMapper;

    @Resource
    HrEmployeeMapper hrEmployeeMapper;

    @Resource
    SysUserAclMapper sysUserAclMapper;

    @Resource
    SysRoleMapper sysRoleMapper;

    @Resource
    HrPositionMapper hrPositionMapper;

    public List<SysRoleAclDto> listAclByRoleId(String q,int roleId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("q", q);
        params.put("roleId", roleId);
        params.put("deleted", false);
        return sysRoleAclMapper.selectExtendAll(params);
    }

    public void delete(int id){
        sysRoleAclMapper.deleteByPrimaryKey(id);
    }

    public List<Map> getAclTreeByUserLogin(String userLogin){
        return getAclTree(listMyAclByUserLogin(userLogin));
    }

    public List<Map> getAclTreeByRoleId(int roleId) {
        List<SysRoleAcl> sysRoleAclList = sysRoleAclMapper.selectAllByRoleId(roleId);
        List<Map> myAclList = Lists.newArrayList();
        for(SysRoleAcl sysRoleAcl:sysRoleAclList){
            Map myAcl=Maps.newHashMap();
            myAcl.put("aclId",sysRoleAcl.getAclId());
            myAcl.put("selectOpts",sysRoleAcl.getSelectOpts());
            myAclList.add(myAcl);
        }
        return getAclTree(myAclList);
    }

    /**
     * 得到用户的权限列表(aclId,selectOpts,selectDepts);
     * @param userLogin
     * @return
     */
    public List<Map> listMyAclByUserLogin(String userLogin) {
        List<Map> myAclList = Lists.newArrayList();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        if (hrEmployeeDto != null) {
            List<Integer> roleIds = MyStringUtil.getIntList(hrEmployeeDto.getRoleIds());
            List<Integer> positionIds = MyStringUtil.getIntList(hrEmployeeDto.getPositionIds());
            //该用户的角色
            List<SysRole> sysRoles = Lists.newArrayList();
            //该用户的职位
            List<HrPosition> hrPositions = Lists.newArrayList();
            //该用户的角色权限
            List<SysRoleAcl> sysRoleAcls = Lists.newArrayList();
            roleIds.forEach(roleId -> {
                SysRole sysRole = sysRoleMapper.selectByPrimaryKey(roleId);
                if (sysRole != null) {
                    List<SysRoleAcl> sysRoleAclList = sysRoleAclMapper.selectAllByRoleId(roleId);
                    sysRoles.add(sysRole);
                    sysRoleAcls.addAll(sysRoleAclList);
                }
            });
            positionIds.forEach(positionId -> {
                HrPosition hrPosition = hrPositionMapper.selectByPrimaryKey(positionId);
                if (hrPosition != null) {
                    hrPositions.add(hrPosition);
                }
            });
            List<Integer> positionDeptIds = MyStringUtil.getIntList(StringUtils.join(hrPositions.stream()
                    .map(HrPosition::getChildDeptIds).collect(Collectors.toList()), ","));


            Map params = Maps.newHashMap();
            params.put("userLogin", userLogin);
            List<SysUserAcl> sysUserAclList = sysUserAclMapper.selectAll(params);
            //该用户角色权限的id
            List<Integer> defaultAclIds = sysRoleAcls.stream().map(SysRoleAcl::getAclId).distinct().collect(Collectors.toList());
            for (Integer aclId : defaultAclIds) {
                //角色权限信息
                List<SysRoleAcl> sysRoleAclList = sysRoleAcls.stream().filter(p -> p.getAclId().equals(aclId)).collect(Collectors.toList());
                //角色Id
                List<Integer> roleIdList = sysRoleAcls.stream().map(SysRoleAcl::getRoleId).distinct().collect(Collectors.toList());
                Map myAcl = Maps.newHashMap();
                myAcl.put("aclId", aclId);
                myAcl.put("selectOpts", "");
                myAcl.put("selectDepts", "");
                String selectOpts = "," + StringUtils.join(MyStringUtil.getStringList(StringUtils.join(sysRoleAclList.stream().map(SysRoleAcl::getSelectOpts).collect(Collectors.toList()), ",")), ",") + ",";
                if (MyStringUtil.getStringList(selectOpts).size() > 0) {
                    myAcl.put("selectOpts", selectOpts);
                }
/*                List<Integer> childDeptIds = MyStringUtil.getIntList(
                        StringUtils.join(sysRoles.stream().filter(p -> roleIdList.contains(p.getId()))
                                .map(SysRole::getChildDeptIds).collect(Collectors.toList()),","));*/
                //获取 有该权限的角色 数据权限
                List<Integer> childDeptIds = new ArrayList<>();
                for (SysRole sysRole : sysRoles) {
                    if (roleIdList.contains(sysRole.getId())) {
                        List<SysRoleAcl> sysRoleAcls1 = sysRoleAclMapper.selectAllByRoleId(sysRole.getId());
                        for (SysRoleAcl sysRoleAcl : sysRoleAcls1) {
                            if (sysRoleAcl.getAclId().equals(aclId)) {
                                childDeptIds.addAll(MyStringUtil.getIntList(sysRole.getChildDeptIds()));
                            }
                        }
                    }
                }
                //childDeptIds.add(hrEmployeeDto.getDeptId());
                childDeptIds.addAll(positionDeptIds);
                if (childDeptIds.size() > 0) {
                    myAcl.put("selectDepts", "," + StringUtils.join(childDeptIds.stream().distinct().collect(Collectors.toList()), ",") + ",");
                }
                myAcl.put("oSelectOpts", myAcl.get("selectOpts"));
                myAcl.put("oSelectDepts", myAcl.get("selectDepts"));
                myAcl.put("optConfig", false);
                myAcl.put("deptConfig", false);
                myAcl.put("allConfig", false);
                myAclList.add(myAcl);
                params.put("aclId", aclId);
                if (sysUserAclList.stream().anyMatch(p -> p.getAclId().equals(aclId))) {
                    SysUserAcl sysUserAcl = sysUserAclList.stream().filter(p -> p.getAclId().equals(aclId)).findFirst().get();
                    if (sysUserAcl.getOwned()) {
                        if (StringUtils.isEmpty(sysUserAcl.getSelectOpts()) && StringUtils.isEmpty(sysUserAcl.getSelectDepts())) {
                            sysUserAclMapper.deleteByPrimaryKey(sysUserAcl.getId());
                        } else {
                            myAcl.put("id", sysUserAcl.getId());
                            if (StringUtils.isNotEmpty(sysUserAcl.getSelectOpts())) {
                                myAcl.put("selectOpts", sysUserAcl.getSelectOpts());
                                myAcl.put("optConfig", true);
                            }
                            if (StringUtils.isNotEmpty(sysUserAcl.getSelectDepts())) {
                                myAcl.put("selectDepts", sysUserAcl.getSelectDepts());
                                myAcl.put("deptConfig", true);
                            }
                        }
                    } else {
                        myAclList.remove(myAcl);
                    }

                }
            }

            List<Integer> roleDeptIds = MyStringUtil.getIntList(StringUtils.join(sysRoles.stream().map(SysRole::getChildDeptIds).collect(Collectors.toList()), ","));
            List<Integer> childDeptIds = Lists.newArrayList();
            childDeptIds.addAll(roleDeptIds);
            childDeptIds.addAll(positionDeptIds);
            childDeptIds.add(hrEmployeeDto.getDeptId());

            for (SysUserAcl sysUserAcl : sysUserAclList.stream().filter(SysUserAcl::getOwned).filter(p -> !defaultAclIds.contains(p.getAclId())).collect(Collectors.toList())) {

                Map myAcl = Maps.newHashMap();
                myAcl.put("aclId", sysUserAcl.getAclId());
                myAcl.put("selectOpts", sysUserAcl.getSelectOpts());
                myAcl.put("selectDepts", sysUserAcl.getSelectDepts());
                myAcl.put("optConfig", false);
                myAcl.put("deptConfig", false);
                myAcl.put("allConfig", true);
                if (MyStringUtil.getStringList(sysUserAcl.getSelectOpts()).size() == 0) {
                    SysAcl sysAcl = sysAclMapper.selectByPrimaryKey(sysUserAcl.getAclId());
                    myAcl.put("selectOpts", MyStringUtil.getMultiDotString(sysAcl.getOpts()));
                }
                if (MyStringUtil.getIntList(sysUserAcl.getSelectDepts()).size() == 0) {
                    myAcl.put("selectDepts", "," + StringUtils.join(childDeptIds, ",") + ",");
                }
                myAclList.add(myAcl);
            }
        }
        return myAclList;
    }


    /**
     * 得到用户的权限列表(aclId,selectOpts,selectDepts);
     * @param userLogin
     * @return
     */
    public Map getAclInfoByUserLogin(String userLogin,String uiSref) {
        Map params=Maps.newHashMap();
        params.put("uiSref",uiSref);
        params.put("deleted",false);
        if(PageHelper.count(()->sysAclMapper.selectAll(params))>0) {
            for(SysAclDto sysAclDto: sysAclMapper.selectAll(params)) {
                List<Map> list = listMyAclInfoByUserLogin(userLogin);
                if (list.stream().anyMatch(p -> p.get("aclId").equals(sysAclDto.getId()))) {
                    Map aclId = list.stream().filter(p -> p.get("aclId").equals(sysAclDto.getId())).findFirst().get();
                    String selectOpts = aclId.getOrDefault("selectOpts","无").toString();
                    if (StringUtils.isNotEmpty(selectOpts)){
                        aclId.put("add", false);
                        aclId.put("edit", false);
                        aclId.put("deleted", false);
                        aclId.put("checked", false);
                        if (selectOpts.contains("查看")) {
                            aclId.put("checked", true);
                        }
                        if (selectOpts.contains("增加")) {
                            aclId.put("add", true);
                        }
                        if (selectOpts.contains("修改")) {
                            aclId.put("edit", true);
                        }
                        if (selectOpts.contains("删除")) {
                            aclId.put("deleted", true);
                        }
                    }
                    return aclId;
                }
            }
        }
        return null;
    }

    /**
     * 得到用户的权限列表信息
     * @param userLogin
     * @return
     */
    public List<Map> listMyAclInfoByUserLogin(String userLogin) {
        List<Map> data = guavaCacheService.get("listMyAclInfoByUserLogin_"+userLogin, () -> {
            List<Map> list = Lists.newArrayList();
            List<Map> oList = listMyAclByUserLogin(userLogin);
            List<SysAclDto> all = sysAclMapper.selectAll(Maps.newHashMap());
            oList.forEach(p -> {
                int aclId = Integer.parseInt(p.get("aclId").toString());
                if (all.stream().anyMatch(o -> o.getId().equals(aclId))) {
                    SysAclDto sysAclDto = all.stream().filter(o -> o.getId().equals(aclId)).findFirst().get();
                    Map map = Maps.newHashMap();
                    map.put("aclId", sysAclDto.getId());
                    map.put("optConfig", p.get("optConfig"));
                    map.put("deptConfig", p.get("deptConfig"));
                    map.put("selectOpts", p.get("selectOpts"));
                    map.put("selectDepts", p.get("selectDepts"));
                    map.put("name", sysAclDto.getName());
                    map.put("type", sysAclDto.getType());
                    map.put("uiSref", sysAclDto.getUiSref());
                    map.put("allConfig", p.get("allConfig"));
                    list.add(map);
                }
            });
            return Optional.ofNullable(list);
        });
        return data;
    }

    /**
     * 保存用户关于某个功能点数据权限配置
     * @param userLogin
     * @param aclId
     * @param selectDepts
     */
    public void saveUserAclDeptConfig(String userLogin,int aclId,String selectDepts){
        Map params=Maps.newHashMap();
        params.put("userLogin",userLogin);
        params.put("aclId",aclId);
        if(PageHelper.count(()->sysUserAclMapper.selectAll(params))>0){
            SysUserAcl sysUserAcl=sysUserAclMapper.selectAll(params).get(0);
            sysUserAcl.setSelectDepts(selectDepts);
            sysUserAclMapper.updateByPrimaryKey(sysUserAcl);
        }else{
            SysUserAcl sysUserAcl=new SysUserAcl();
            sysUserAcl.setSelectOpts("");
            sysUserAcl.setSelectDepts(selectDepts);
            sysUserAcl.setUserLogin(userLogin);
            sysUserAcl.setAclId(aclId);
            sysUserAcl.setOwned(true);
            sysUserAclMapper.insert(sysUserAcl);
        }
    }

    public void removeUserAcl(String userLogin,int aclId,String type){
        Map params=Maps.newHashMap();
        params.put("userLogin",userLogin);
        params.put("aclId",aclId);
        if(PageHelper.count(()->sysUserAclMapper.selectAll(params))>0) {
            SysUserAcl sysUserAcl=sysUserAclMapper.selectAll(params).get(0);
            if("all".equalsIgnoreCase(type)){
                sysUserAclMapper.deleteByPrimaryKey(sysUserAcl.getId());
            }else if("dept".equalsIgnoreCase(type)) {
                sysUserAcl.setSelectDepts("");
                sysUserAclMapper.updateByPrimaryKey(sysUserAcl);
            }else{
                sysUserAcl.setSelectOpts("");
                sysUserAclMapper.updateByPrimaryKey(sysUserAcl);
            }

        }
    }

    /**
     * 得到树形
     * @param myAclList 拥有的权限列表
     * aclId int
     * selectOpts string
     * @return
     */
    public List<Map> getAclTree(List<Map> myAclList) {
        List<Map> mapList = Lists.newArrayList();
        Map<String, Object> params = Maps.newHashMap();
        params.put("deleted", false);
        List<SysAclModule> modules = sysAclModuleMapper.selectAll(params).stream().sorted(Comparator.comparing(SysAclModule::getSeq)).collect(Collectors.toList());
        List<SysAclDto> allAclList = sysAclMapper.selectAll(params).stream().sorted(Comparator.comparing(SysAcl::getSeq)).collect(Collectors.toList());
        for (SysAclModule aclModule : modules) {
            long childModuleCount=modules.stream().filter(p->p.getParentId().equals(aclModule.getId())).count();
            List<SysAclDto> acls=allAclList.stream().filter(p -> p.getAclModuleId().equals(aclModule.getId())).collect(Collectors.toList());
            if(childModuleCount>0||acls.size()>0) {
                Map map = Maps.newHashMap();
                map.put("id", "module_" + aclModule.getId());
                map.put("text", aclModule.getName());
                Map stateMap = Maps.newHashMap();
                stateMap.put("selected", false);
                stateMap.put("disabled", false);
                if (aclModule.getParentId() == 0) {
                    map.put("parent", "#");
                    stateMap.put("opened", true);
                } else {
                    map.put("parent", "module_" + aclModule.getParentId());
                    stateMap.put("opened", false);
                }
                map.put("state", stateMap);
                mapList.add(map);
                for (SysAclDto sysAclDto : acls) {
                    Map aclMap = Maps.newHashMap();
                    aclMap.put("id", "acl_" + sysAclDto.getId());
                    aclMap.put("text", sysAclDto.getName());
                    aclMap.put("parent", "module_" + sysAclDto.getAclModuleId());
                    Map aclStateMap = Maps.newHashMap();
                    aclStateMap.put("selected", false);
                    aclStateMap.put("opened", false);
                    aclStateMap.put("disabled", false);
                    String selectOpts="";
                    if (myAclList.stream().anyMatch(p -> p.get("aclId").equals(sysAclDto.getId()))) {
                        selectOpts=myAclList.stream().filter(p -> p.get("aclId").equals(sysAclDto.getId())).findFirst().get().get("selectOpts").toString();
                    }
                    aclMap.put("state", aclStateMap);
                    String icon = "glyphicon glyphicon-tree-deciduous font-green-sharp";
                    if (sysAclDto.getType() == 2) {
                        icon = "glyphicon glyphicon-play font-blue";
                    } else if (sysAclDto.getType() == 3) {
                        icon = "glyphicon glyphicon-repeat";
                    }
                    aclMap.put("icon", icon);
                    mapList.add(aclMap);

                    List<String> opts=MyStringUtil.getStringList(sysAclDto.getOpts());
                    if(opts.size()==0){
                        aclStateMap.put("selected", myAclList.stream().anyMatch(p -> p.get("aclId").equals(sysAclDto.getId())));
                    }else {
                        for (String opt : opts) {
                            Map optMap = Maps.newHashMap();
                            optMap.put("parent", "acl_" + sysAclDto.getId());
                            optMap.put("text", opt);
                            optMap.put("id", "opt_" + sysAclDto.getId() + "_" + opt);
                            optMap.put("icon", "glyphicon glyphicon-star");
                            Map optStateMap = Maps.newHashMap();
                            optStateMap.put("selected", selectOpts.contains(opt));
                            optStateMap.put("opened", false);
                            optStateMap.put("disabled", false);
                            optMap.put("state",optStateMap);
                            mapList.add(optMap);
                        }
                    }
                }
            }
        }
        return mapList;
    }


    @Transactional
    public void saveRoleAclList(int roleId,String selects) {
        sysRoleAclMapper.deleteByRoleId(roleId);
        List<SysAclDto> sysAclDtoList = sysAclMapper.selectAll(Maps.newHashMap());
        Set<Integer> doneAclIds = new HashSet<>();
        List<String> selectList = MyStringUtil.getStringList(selects);
        for (String _id : selectList) {
            if (_id.contains("acl_")||_id.contains("opt_")) {
                int aclId = Integer.parseInt(StringUtils.split(_id, '_')[1]);
                doneAclIds.add(aclId);
            }
        }

        for (int aclId : doneAclIds) {
            if (sysAclDtoList.stream().anyMatch(p -> p.getId().equals(aclId))) {
                doneAclIds.add(aclId);
                List<String> selectOpts = selectList.stream().filter(p -> p.contains("opt_" + aclId + "_")).map(p -> StringUtils.split(p, "_")[2]).distinct().collect(Collectors.toList());
                SysRoleAcl sysRoleAcl = new SysRoleAcl();
                sysRoleAcl.setAclId(aclId);
                sysRoleAcl.setRoleId(roleId);
                sysRoleAcl.setSelectOpts(StringUtils.join(selectOpts, ","));
                sysRoleAclMapper.insert(sysRoleAcl);
            }
        }

    }

    @Transactional
    public void saveUserAclList(String userLogin,String selects) {

        Set<Integer> doneAclIds = new HashSet<>();
        List<String> selectList = MyStringUtil.getStringList(selects);
        for (String _id : selectList) {
            if (_id.contains("acl_") || _id.contains("opt_")) {
                int aclId = Integer.parseInt(StringUtils.split(_id, '_')[1]);
                doneAclIds.add(aclId);
            }
        }

        Map userAclParams = Maps.newHashMap();
        userAclParams.put("owned", false);
        userAclParams.put("userLogin", userLogin);

        List<Map> exist = listMyAclByUserLogin(userLogin);
        //处理存在的权限
        for (int aclId : doneAclIds) {
            List<String> opts = selectList.stream().filter(p -> p.contains("opt_" + aclId + "_")).map(p -> StringUtils.split(p, "_")[2]).distinct().sorted().collect(Collectors.toList());
            String selectOpts = "";
            if (opts.size() > 0) selectOpts = "," + StringUtils.join(opts, ",") + ",";
            if (exist.stream().noneMatch(p -> p.get("aclId").equals(aclId))) {
                userAclParams.put("aclId", aclId);
                if (PageHelper.count(() -> sysUserAclMapper.selectAll(userAclParams)) > 0) {
                    SysUserAcl sysUserAcl = sysUserAclMapper.selectAll(userAclParams).get(0);
                    sysUserAcl.setSelectOpts(selectOpts);
                    sysUserAcl.setOwned(true);
                    sysUserAclMapper.updateByPrimaryKey(sysUserAcl);
                } else {
                    SysUserAcl sysUserAcl = new SysUserAcl();
                    sysUserAcl.setSelectOpts(selectOpts);
                    sysUserAcl.setSelectDepts("");
                    sysUserAcl.setAclId(aclId);
                    sysUserAcl.setOwned(true);
                    sysUserAcl.setUserLogin(userLogin);
                    sysUserAclMapper.insert(sysUserAcl);
                }
            } else {
                Map existItem = exist.stream().filter(p -> p.get("aclId").equals(aclId)).findFirst().get();
                List<String> existOpts = MyStringUtil.getStringList(existItem.get("selectOpts").toString()).stream().sorted().collect(Collectors.toList());
                String existSelectOpts = "";
                if (existOpts.size() > 0) existSelectOpts = "," + StringUtils.join(existOpts, ",") + ",";
                if (!existSelectOpts.equalsIgnoreCase(selectOpts)) {
                    if (existItem.containsKey("id")) {
                        SysUserAcl sysUserAcl = sysUserAclMapper.selectByPrimaryKey((int) existItem.get("id"));
                        sysUserAcl.setSelectOpts(selectOpts);
                        sysUserAclMapper.updateByPrimaryKey(sysUserAcl);
                    } else {
                        userAclParams.put("aclId", aclId);
                        if (PageHelper.count(() -> sysUserAclMapper.selectAll(userAclParams)) > 0) {
                            SysUserAcl sysUserAcl = sysUserAclMapper.selectAll(userAclParams).get(0);
                            sysUserAcl.setSelectOpts(selectOpts);
                            sysUserAcl.setOwned(true);
                            sysUserAclMapper.updateByPrimaryKey(sysUserAcl);
                        } else {
                            SysUserAcl sysUserAcl = new SysUserAcl();
                            sysUserAcl.setSelectOpts(selectOpts);
                            sysUserAcl.setSelectDepts("");
                            sysUserAcl.setAclId(aclId);
                            sysUserAcl.setOwned(true);
                            sysUserAcl.setUserLogin(userLogin);
                            sysUserAclMapper.insert(sysUserAcl);
                        }
                    }
                }
            }
        }


        //处理未被选中的权限
        for(Map existItem:exist){
            int aclId=(int)existItem.get("aclId");
            if(!doneAclIds.contains(aclId)){

                userAclParams.put("aclId", aclId);
                userAclParams.put("owned",true);
                if (PageHelper.count(() -> sysUserAclMapper.selectAll(userAclParams)) > 0) {
                    SysUserAcl sysUserAcl = sysUserAclMapper.selectAll(userAclParams).get(0);
                    sysUserAcl.setOwned(false);
                    sysUserAclMapper.updateByPrimaryKey(sysUserAcl);
                }else {
                    SysUserAcl sysUserAcl = new SysUserAcl();
                    sysUserAcl.setSelectOpts("");
                    sysUserAcl.setSelectDepts("");
                    sysUserAcl.setAclId(aclId);
                    sysUserAcl.setUserLogin(userLogin);
                    sysUserAcl.setOwned(false);
                    sysUserAclMapper.insert(sysUserAcl);
                }
            }
        }
    }



    public List<Map> listSystemMenu(String enLogin) {
        List<Map> menus = guavaCacheService.get("listSystemMenu_" + enLogin, () -> {
            List<Map> list = Lists.newArrayList();
            //得到用户的功能列表Id
            List<Integer> aclIds = listMyAclByUserLogin(enLogin).stream().map(p -> Integer.parseInt(p.get("aclId").toString())).distinct().collect(Collectors.toList());
            if (aclIds.size() > 0) {
                //得到用户的菜单功能列表
                Map aclMapParams = Maps.newHashMap();
                aclMapParams.put("type", 1);
                aclMapParams.put("deleted", false);
                aclMapParams.put("aclIds", aclIds);
                List<SysAclDto> aclAll = sysAclMapper.selectAll(aclMapParams);

                Map params = Maps.newHashMap();
                params.put("deleted", false);
                List<SysAclModule> moduleList = sysAclModuleMapper.selectAll(params);
                //需要的功能模块Id列表
                List<Integer> aclModuleIds = aclAll.stream().map(SysAcl::getAclModuleId).distinct().collect(Collectors.toList());
                for (Integer aclModuleId : aclModuleIds) {
                    addParentModule(aclModuleId, list, moduleList, aclAll);
                }
                list.sort(Comparator.comparing(p -> Integer.parseInt(p.get("seq").toString())));
            }
            return Optional.ofNullable(list);
        });
        return menus;
    }

    private  void addParentModule(Integer aclModuleId,List<Map> menus,List<SysAclModule> moduleList,List<SysAclDto> aclAll){


        if(menus.stream().noneMatch(p->p.get("id").equals(aclModuleId))&&moduleList.stream().anyMatch(p->p.getId().equals(aclModuleId))) {
            SysAclModule currentModule = moduleList.stream().filter(p -> p.getId().equals(aclModuleId)).findFirst().get();
            Map map = Maps.newHashMap();
            map.put("code", currentModule.getCode());
            map.put("name", currentModule.getName());
            map.put("left", currentModule.getLeft());
            map.put("top", currentModule.getTop());
            map.put("icon", currentModule.getIcon());
            map.put("parentId", currentModule.getParentId());
            map.put("id", currentModule.getId());
            map.put("seq", currentModule.getSeq());
            map.put("remark", currentModule.getRemark());
            map.put("aclList", aclAll.stream().filter(p -> p.getAclModuleId().equals(aclModuleId)).collect(Collectors.toList()));
            menus.add(map);
            if (!currentModule.getId().equals(1)) {
                addParentModule(currentModule.getParentId(), menus, moduleList, aclAll);
            }
        }
    }


}
