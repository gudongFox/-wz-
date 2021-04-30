package com.cmcu.common.service;

import com.cmcu.common.dto.*;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
public class CommonUserService {


    @Resource
    IUserDataService userDataService;

    public static String DEFAULT_PWD="M/jlxP2Xu5wu3KuPQ/rmbQ==";


    /**
     * 只包含基本信息
     * @param tenetId
     * @param enLoginList
     * @return
     */
    public List<FastUserDto> listFastUserByEnLoginList(String tenetId,List<String> enLoginList) {
        List<FastUserDto> list = Lists.newArrayList();
        enLoginList.forEach(p -> {
            FastUserDto userDto = getFastByEnLogin(p);
            if (userDto != null) list.add(userDto);
        });
        return list;
    }

    /**
     * 只包含基本信息
     * @param enLogin
     * @return
     */
    public FastUserDto getFastByEnLogin(String enLogin) {
        FastUserDto userDto = new FastUserDto();
        Map keyInfo = userDataService.getKeyInfoByEnLogin(enLogin);
        if (keyInfo != null) {
            userDto.setCnName(keyInfo.getOrDefault("cnName", "").toString());
            userDto.setEnLogin(keyInfo.getOrDefault("enLogin", "").toString());
            userDto.setDeptName(keyInfo.getOrDefault("deptName", "").toString());
            userDto.setDeptId(Integer.parseInt(keyInfo.getOrDefault("deptId", 0).toString()));
            userDto.setTenetId(keyInfo.getOrDefault("tenetId", "").toString());
            userDto.setWxId(keyInfo.getOrDefault("wxId", "").toString());
            String headAttachId = keyInfo.getOrDefault("headAttachId", "").toString();
            String avatar = keyInfo.getOrDefault("avatar", "").toString();
            String signPicAttachId=keyInfo.getOrDefault("signPicAttachId", "").toString();
            userDto.setSignPicUrl("/common/attach/download/"+signPicAttachId);
            if (StringUtils.isNotEmpty(headAttachId)) {
                userDto.setAvatar("/common/attach/download/" + headAttachId);
            } else if (StringUtils.isNotEmpty(avatar) && avatar.startsWith("http")) {
                userDto.setAvatar(avatar);
            }
        }
        ModelUtil.setNotNullFields(userDto);
        return userDto;
    }


    public String getTenetId(String enLogin) {
        return userDataService.getTenetId(enLogin);
    }

    public List<UserDto> selectAll(String tenetId) {
        List<UserDto> users = userDataService.selectAllUser(tenetId);
        return users;
    }

    public List<DeptDto> selectAllDept(String tenetId) {
        List<DeptDto> deptList = userDataService.selectAllDept(tenetId);
        return deptList;
    }

    public List<UserDto> listUserByEnLoginList(String tenetId,List<String> enLoginList){
        if(enLoginList.size()==0) return Lists.newArrayList();
        if(StringUtils.isEmpty(tenetId)){
            String enLogin=enLoginList.get(0);
            tenetId=getTenetId(enLogin);
        }
        List<UserDto> users=userDataService.selectAllUser(tenetId);
        return users.stream().filter(p->enLoginList.contains(p.getEnLogin())).collect(Collectors.toList());
    }

    public void updateProperty(String enLogin,Map data){
        userDataService.updateProperty(enLogin,data);
    }

    public List<UserDto> listUser(String enLogin,int deptId) {
        String tenetId = getTenetId(enLogin);
        List<UserDto> users = userDataService.selectAllUser(tenetId);
        if (deptId == 0) {
            List<DeptDto> all = userDataService.selectAllDept(tenetId);
            if (all.stream().filter(p -> p.getParentId() == 0).count() == 1) {
                DeptDto root = all.stream().filter(p -> p.getParentId() == 0).findFirst().get();
                return users.stream().filter(p -> p.getDeptId() == root.getId()).collect(Collectors.toList());
            }
        }
        return users.stream().filter(p -> p.getDeptId() == deptId).collect(Collectors.toList());
    }

    public List<DeptDto> listDept(String enLogin,int parentId) {
        String tenetId = getTenetId(enLogin);
        List<DeptDto> all = userDataService.selectAllDept(tenetId);
        if (parentId == 0) {
            //最上级只有一个为了友好去掉第一个显示
            if (all.stream().filter(p -> p.getParentId() == 0).count() == 1) {
                DeptDto root = all.stream().filter(p -> p.getParentId() == 0).findFirst().get();
                return all.stream().filter(p -> p.getParentId() == root.getId()).collect(Collectors.toList());
            }
        }
        return all.stream().filter(p -> p.getParentId() == parentId).collect(Collectors.toList());
    }


    /**
     * 遍历增加子部门
     * @param parentId
     * @param all
     * @param list
     */
    public void recursiveAddChildDept(int parentId, List<DeptDto> all,List<DeptDto> list) {
        DeptDto parent = all.stream().filter(p -> p.getId() == parentId).findFirst().get();
        list.add(parent);
        for (int childId : all.stream().filter(p -> p.getParentId() == parentId).map(p -> p.getId()).collect(Collectors.toList())) {
            recursiveAddChildDept(childId, all, list);
        }
    }

    public UserDto selectByEnLogin(String enLogin){
       return userDataService.selectByEnLogin(enLogin);
    }

    public UserDto selectByName(String tenetId,String cnName){
        List<UserDto> users= selectAll(tenetId);
        if(users.stream().anyMatch(p->p.getCnName().equalsIgnoreCase(cnName))){
            return users.stream().filter(p->p.getCnName().equalsIgnoreCase(cnName)).findFirst().get();
        }
        return null;
    }

    public UserDto getUserByWxId(String tenetId,String wxId) {
        List<UserDto> users = selectAll(tenetId);
        Optional<UserDto> optionalUserDto = users.stream().filter(p -> wxId.equalsIgnoreCase(p.getWxId())|| wxId.equalsIgnoreCase(p.getEnLogin())).findFirst();
        if (optionalUserDto.isPresent()) {
            return optionalUserDto.get();
        }
        return null;
    }

    public DeptDto getDept(int deptId) {
        List<DeptDto> deptList = userDataService.selectAllDept("");
        if (deptList.stream().anyMatch(p -> p.getId() == deptId)) {
            return deptList.stream().filter(p -> p.getId() == deptId).findFirst().get();
        }
        return null;
    }

    public DeptDto getDeptByName(String name) {
        List<DeptDto> deptList = userDataService.selectAllDept("");
        if (deptList.stream().anyMatch(p -> p.getName() .equals(name) )) {
            return deptList.stream().filter(p -> p.getName() .equals(name) ).findFirst().get();
        }
        return null;
    }

    public String getCnNames(String enLogins) {
        List<String> names = Lists.newArrayList();
        for (String enLogin : MyStringUtil.getStringList(enLogins)) {
            String name = userDataService.getNameByEnLogin(enLogin);
            if (StringUtils.isNotEmpty(name)) names.add(name);
        }
        return StringUtils.join(names, ",");
    }

    public List<Map> listUserJsTreeDataPre(Map params) {

        List<Map> tree = Lists.newArrayList();
        String enLogin=params.get("enLogin").toString();
        String tenetId=getTenetId(enLogin);

        List<DeptDto> deptList = userDataService.selectAllDept(tenetId).stream().filter(p->p.getDeptCount()>0||p.getUserCount()>0).collect(Collectors.toList());
        recursiveSetDeptLevel(0,0,deptList);


        int minLevel=0;

        int parentDeptId = Integer.parseInt(params.getOrDefault("parentDeptId", 0).toString());
        if(parentDeptId!=0){
            if(deptList.stream().anyMatch(p->p.getId()==parentDeptId)){
                int currentLevel=deptList.stream().filter(p->p.getId()==parentDeptId).findFirst().get().getLevel();
                deptList=deptList.stream().filter(p->p.getLevel()>=currentLevel).collect(Collectors.toList());
                minLevel=currentLevel;
            }
        }
        List<UserDto> users = userDataService.selectAllUser(tenetId);
        String q=params.getOrDefault("q","").toString();
        if(StringUtils.isNotEmpty(q)) {
            users = users.stream().filter(p -> p.getEnLogin().contains(q) || p.getCnName().contains(q)).collect(Collectors.toList());
        }

//        if(params.containsKey("enLogin")){
//            users=users.stream().filter(p->p.getEnLogin().contains(params.get("enLogin").toString())).collect(Collectors.toList());
//        }
//        if(params.containsKey("cnName")){
//            users=users.stream().filter(p->p.getCnName().contains(params.get("cnName").toString())).collect(Collectors.toList());
//        }
        List<String> selects=Lists.newArrayList();
        boolean multiple=Boolean.parseBoolean(params.getOrDefault("multiple",false).toString());
        if(params.containsKey("q")) {
            if (deptList.stream().anyMatch(p -> p.getName().contains(q))) {
                minLevel=deptList.stream().filter(p -> p.getName().contains(q)).sorted(Comparator.comparing(DeptDto::getLevel)).map(DeptDto::getLevel).collect(Collectors.toList()).get(0);
                int filterLevel=minLevel;
                deptList=deptList.stream().filter(p->p.getLevel()>filterLevel||p.getName().contains(q)).collect(Collectors.toList());
            } else {
                users = users.stream().filter(p -> p.getCnName().contains(q) || p.getEnLogin().contains(q)).collect(Collectors.toList());
                List<Integer> userDeptIdList = users.stream().map(UserDto::getDeptId).distinct().collect(Collectors.toList());
                List<Integer> deptLevelList = deptList.stream().filter(p -> userDeptIdList.contains(p.getId())).sorted(Comparator.comparing(DeptDto::getLevel)).map(DeptDto::getLevel).collect(Collectors.toList());
                if (deptLevelList.size() == 0) {
                    return Lists.newArrayList();
                }
                minLevel = deptLevelList.get(0);
                int filterLevel = minLevel;
                deptList = deptList.stream().filter(p -> p.getLevel() > filterLevel || userDeptIdList.contains(p.getId())).collect(Collectors.toList());
            }
        }
        if(params.containsKey("selects")){
            selects=MyStringUtil.getStringList(params.get("selects").toString());
        }

        for(DeptDto deptDto:deptList) {
            if(deptDto.getLevel()==minLevel) {
                addChild(deptDto.getId(), deptList, users, tree,selects,multiple);
            }
        }
        return tree;
    }


    public List<Map> listUserJsTreeData(Map params) {

        List<Map> tree = Lists.newArrayList();
        String enLogin = params.getOrDefault("enLogin","").toString();
        String tenetId = getTenetId(enLogin);
        boolean multiple = Boolean.parseBoolean(params.getOrDefault("multiple", false).toString());
        List<DeptDto> deptList = userDataService.selectAllDept(tenetId).stream().filter(p -> p.getDeptCount() > 0 || p.getUserCount() > 0).collect(Collectors.toList());
        recursiveSetDeptLevel(0, 0, deptList);
        //获取某部门的人
        List<UserDto> users = new ArrayList<>();
        if(StringUtils.isNotEmpty(params.getOrDefault("deptId", "").toString())){
             users =listUser(enLogin,Integer.valueOf(params.getOrDefault("deptId", "").toString()));
        }else{
             users = userDataService.selectAllUser(tenetId);
        }

        String q = params.getOrDefault("q", "").toString();
        if (StringUtils.isNotEmpty(q)) {
            if(users.stream().anyMatch(p->p.getEnLogin().contains(q)||p.getCnName().contains(q)||p.getLogoGram().contains(q))) {
                users = users.stream().filter(p -> p.getEnLogin().contains(q) || p.getCnName().contains(q)||p.getLogoGram().contains(q)).collect(Collectors.toList());
            }else{
                deptList=deptList.stream().filter(p->p.getName().contains(q)).collect(Collectors.toList());
            }
        }

        String qualify = params.getOrDefault("qualify", "").toString();
        if(StringUtils.isNotEmpty(qualify)) {
            if (users.stream().anyMatch(p -> p.getQualifyList().contains(qualify))) {
                users = users.stream().filter(p -> p.getQualifyList().contains(qualify)).collect(Collectors.toList());
            }
        }


        List<String> selects = Lists.newArrayList();
        if (params.containsKey("selects")) {
            selects = MyStringUtil.getStringList(params.get("selects").toString());
        }

        for(int i=0;i<10;i++){
            for (int j=deptList.size()-1;j>=0;j--) {
                DeptDto deptDto=deptList.get(j);
                if(users.stream().noneMatch(p->p.getDeptId()==deptDto.getId())&&deptList.stream().noneMatch(p->p.getParentId()==deptDto.getId())){
                    deptList.remove(deptDto);
                }
            }
        }


        for (DeptDto deptDto : deptList) {
            addChild(deptDto.getId(), deptList, users, tree, selects, multiple);
        }
        return tree;
    }


    /**
     * 获取用户数据
     * @param tenetId
     * @param qualify
     * @param q
     * @param deptId
     * @return
     */
    public List<UserDto>  listFormUserData(String tenetId,String qualify,String q,int deptId) {
        List<UserDto> userList = userDataService.selectAllUser(tenetId);
        List<DeptDto> deptList = userDataService.selectAllDept(tenetId, deptId);
        List<Integer> deptIdList = deptList.stream().map(DeptDto::getId).collect(Collectors.toList());
        deptIdList.add(deptId);
        userList = userList.stream().filter(p -> deptIdList.contains(p.getDeptId())).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(qualify)) {
            if (userList.stream().anyMatch(p -> p.getQualifyList().contains(qualify))) {
                userList = userList.stream().filter(p -> p.getQualifyList().contains(qualify)).collect(Collectors.toList());
            }
        }

        if (StringUtils.isNotEmpty(q)) {
            userList = userList.stream().filter(p -> p.getEnLogin().contains(q) || p.getCnName().contains(q) || p.getLogoGram().contains(q)).collect(Collectors.toList());
        }

        return userList;
    }





    public List<Map> listUserJsTreeDataByRoleName(String tenetId,List<String> selectedList,List<String> roleNames,String q){
        List<UserDto> users = userDataService.selectAllUser(tenetId);
        List<RoleDto> roleDtoList=userDataService.selectAllRole(tenetId);
        List<Map> tree=Lists.newArrayList();
        int i=0;
        for(String roleName:roleNames) {
            if(roleDtoList.stream().anyMatch(p->p.getName().equalsIgnoreCase(roleName))) {
                String roleId=roleDtoList.stream().filter(p->p.getName().equalsIgnoreCase(roleName)).findFirst().get().getId();
                Map roleNode = Maps.newHashMap();
                roleNode.put("id", "role_"+roleId);
                roleNode.put("text", roleName);
                roleNode.put("parent", "#");
                Map roleState = Maps.newHashMap();
                roleState.put("opened", i==0);
                roleState.put("selected",false);
                roleState.put("disabled", false);
                roleNode.put("state", roleState);
                tree.add(roleNode);
                for (UserDto user : users.stream().filter(p->p.getRoleIdList().contains(roleId)).filter(p->p.getCnName().contains(q)).collect(Collectors.toList())) {
                    Map userNode = Maps.newHashMap();
                    userNode.put("id", user.getEnLogin());
                    userNode.put("text", user.getCnName());
                    userNode.put("parent", "role_" + roleId);
                    userNode.put("icon", "fa fa-user");
                    Map userState = Maps.newHashMap();
                    userState.put("opened", false);
                    userState.put("selected", selectedList.contains(user.getEnLogin()));
                    userState.put("disabled", false);
                    userNode.put("state", userState);
                    tree.add(userNode);
                }
                i++;
            }

        }
        return tree;
    }


    public List<Map> listUserJsTreeDataByArrange(String tenetId,List<String> selectedList,List<String> roleNames,String q){
        List<UserDto> users = userDataService.selectAllUser(tenetId);
        List<RoleDto> roleDtoList=userDataService.selectAllRole(tenetId);
        List<Map> tree=Lists.newArrayList();
        int i=0;
        for(String roleName:roleNames) {
            if(roleDtoList.stream().anyMatch(p->p.getName().equalsIgnoreCase(roleName))) {
                String roleId=roleDtoList.stream().filter(p->p.getName().equalsIgnoreCase(roleName)).findFirst().get().getId();
                Map roleNode = Maps.newHashMap();
                roleNode.put("id", "role_"+roleId);
                roleNode.put("text", roleName);
                roleNode.put("parent", "#");
                Map roleState = Maps.newHashMap();
                roleState.put("opened", i==0);
                roleState.put("selected",false);
                roleState.put("disabled", false);
                roleNode.put("state", roleState);
                tree.add(roleNode);
                for (UserDto user : users.stream().filter(p->p.getRoleIdList().contains(roleId)).filter(p->p.getCnName().contains(q)).collect(Collectors.toList())) {
                    Map userNode = Maps.newHashMap();
                    userNode.put("id", user.getEnLogin());
                    userNode.put("text", user.getCnName());
                    userNode.put("parent", "role_" + roleId);
                    userNode.put("icon", "fa fa-user");
                    Map userState = Maps.newHashMap();
                    userState.put("opened", false);
                    userState.put("selected", selectedList.contains(user.getEnLogin()));
                    userState.put("disabled", false);
                    userNode.put("state", userState);
                    tree.add(userNode);
                }
                i++;
            }

        }
        return tree;
    }



    public List<Map> listPositionRoleJsTreeData(Map params){
        List<PositionDto> positionDtoList=userDataService.selectAllPosition("");
        List<RoleDto> roleDtoList=userDataService.selectAllRole("");
        List<String> selectList=MyStringUtil.getStringList(params.getOrDefault("selects","").toString());
        String q=params.getOrDefault("q","").toString();
        List<Map> tree = Lists.newArrayList();

        positionDtoList.stream().filter(p->p.getName().contains(q)).forEach(p->{
            Map node = Maps.newHashMap();
            String id="pr_职务_"+p.getName();
            node.put("id", id);
            node.put("text", p.getName()+"("+p.getUserCount()+")");
            node.put("icon", "glyphicon glyphicon-ok-sign");
            node.put("parent", "#");
            Map state = Maps.newHashMap();
            state.put("opened", false);
            state.put("selected", selectList.contains(id));
            state.put("disabled", false);
            node.put("state", state);
            tree.add(node);
        });

        roleDtoList.stream().filter(p->p.getName().contains(q)).forEach(p->{
            Map node = Maps.newHashMap();
            String id="pr_角色_"+p.getName();
            node.put("id", id);
            node.put("text", p.getName()+"("+p.getUserCount()+")");
            node.put("icon", "glyphicon glyphicon-th-list");
            node.put("parent", "#");
            Map state = Maps.newHashMap();
            state.put("opened", false);
            state.put("selected", selectList.contains(id));
            state.put("disabled", false);
            node.put("state", state);
            tree.add(node);
        });
        return tree;
    }

    public List<String> listUserByPR(String tenetId,List<String> prList) {
        List<String> list = Lists.newArrayList();
        List<UserDto> users = userDataService.selectAllUser(tenetId);
        List<RoleDto> roleDtoList=userDataService.selectAllRole(tenetId);
        List<PositionDto> positionDtoList=userDataService.selectAllPosition(tenetId);
        for (String pr: prList) {
            if (pr.contains("_角色_")) {
                String roleName = StringUtils.split(pr, "_")[StringUtils.split(pr, "_").length - 1];
                if (roleDtoList.stream().anyMatch(p -> p.getName().equalsIgnoreCase(roleName))) {
                    String roleId = roleDtoList.stream().filter(p -> p.getName().equalsIgnoreCase(roleName)).findFirst().get().getId();
                    list.addAll(users.stream().filter(p -> p.getRoleIdList().contains(roleId)).map(UserDto::getEnLogin).collect(Collectors.toList()));
                }
            } else if (pr.contains("_职务_")) {
                String positionName = StringUtils.split(pr, "_")[StringUtils.split(pr, "_").length - 1];
                if (positionDtoList.stream().anyMatch(p -> p.getName().equalsIgnoreCase(positionName))) {
                    String positionId = positionDtoList.stream().filter(p -> p.getName().equalsIgnoreCase(positionName)).findFirst().get().getId();
                    list.addAll(users.stream().filter(p -> p.getPositionIdList().contains(positionId)).map(UserDto::getEnLogin).collect(Collectors.toList()));
                }
            }
        }
        return list.stream().distinct().collect(Collectors.toList());
    }

    private void addChild(int deptId, List<DeptDto> deptList,List<UserDto> users, List<Map> tree,List<String> selects,boolean multiple) {

        //去重,不一定从最开始来
        if(tree.stream().noneMatch(p->p.get("id").toString().equalsIgnoreCase("dept_"+deptId))) {

            List<Integer> childDeptIdList = deptList.stream().filter(p -> p.getParentId()==deptId).map(DeptDto::getId).distinct().collect(Collectors.toList());
            List<UserDto> deptUsers = users.stream().filter(p -> p.getDeptId()==deptId).collect(Collectors.toList());

            if(childDeptIdList.size()>0||deptUsers.size()>0) {
                DeptDto single = deptList.stream().filter(p -> p.getId() == deptId).findFirst().get();
                Optional<DeptDto> parentDept = deptList.stream().filter(p -> p.getId() == single.getParentId()).findFirst();
                Map deptNode = Maps.newHashMap();
                deptNode.put("id", "dept_" + deptId);
                deptNode.put("text", single.getName());
                //deptNode.put("icon", "glyphicon glyphicon-th-list fa fa-list");
                Map deptState = Maps.newHashMap();
                deptState.put("opened", false);
                deptState.put("selected", false);
                deptState.put("disabled", false);
                deptNode.put("state", deptState);
                if (!parentDept.isPresent()) {
                    deptState.put("opened", true);
                    deptNode.put("parent", "#");
                } else {
                    deptNode.put("parent", "dept_" + single.getParentId());
                }
                tree.add(deptNode);

                for (UserDto user : deptUsers) {
                    Map userNode = Maps.newHashMap();
                    userNode.put("id", user.getEnLogin());
                    userNode.put("text", user.getCnName());
                    userNode.put("parent", "dept_" + deptId);
                    userNode.put("icon", "glyphicon glyphicon-user fa fa-user");
                    Map userState = Maps.newHashMap();
                    userState.put("opened", false);
                    userState.put("selected", selects.contains(user.getEnLogin()));
                    userState.put("disabled", false);
                    userNode.put("state", userState);
                    tree.add(userNode);
                }
                childDeptIdList.forEach(p -> addChild(p, deptList, users, tree, selects, multiple));
            }
        }
    }

    private void recursiveSetDeptLevel(int parentDeptId,int level,List<DeptDto> all){
        List<DeptDto> childList=all.stream().filter(p->p.getParentId()==parentDeptId).collect(Collectors.toList());
        childList.forEach(p->{
            p.setLevel(level);
            recursiveSetDeptLevel(p.getId(),level+1,all);
        });
    }


    public List<RoleDto> selectAllRole(String tenetId){
        return userDataService.selectAllRole(tenetId);
    }

}
