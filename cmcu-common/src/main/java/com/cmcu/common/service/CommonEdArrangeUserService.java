package com.cmcu.common.service;

import com.common.model.JsonData;
import com.cmcu.common.dao.*;
import com.cmcu.common.dto.*;
import com.cmcu.common.entity.*;
import com.cmcu.common.util.JsonMapper;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.ed.entity.EdConfigRole;
import com.common.wx.model.User;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommonEdArrangeUserService {


    @Resource
    CommonEdBuildMapper commonEdBuildMapper;

    @Resource
    public CommonEdArrangeMapper commonEdArrangeMapper;

    @Resource
    public CommonEdArrangeUserMapper commonEdArrangeUserMapper;

    @Resource
    CommonFormDataMapper commonFormDataMapper;

    @Resource
    CommonEdRoleMapper commonEdRoleMapper;

    @Resource
    CommonUserService commonUserService;

    @Resource
    CommonCoDirService commonCoDirService;

    @Resource
    CommonEdBuildService commonEdBuildService;

    @Resource
    IEdDataService edDataService;

    @Resource
    TaskExecutor taskExecutor;

    public List<Map> listData(String businessKey,String buildBusinessKey) {
        if (StringUtils.isEmpty(buildBusinessKey)) buildBusinessKey = businessKey;
        Date now = new Date();
        CommonFormData commonFormData = commonFormDataMapper.selectByBusinessKey(businessKey);
        String tenetId = commonFormData.getTenetId();
        Map data = JsonMapper.string2Map(commonFormData.getFormData());
        List<String> majorList = JsonMapper.getListValue(data, "majorList");
        List<String> roleList = JsonMapper.getListValue(data, "roleList");


        List<CommonEdRole> edRoleList = commonEdRoleMapper.selectAll(Maps.newHashMap()).stream().filter(p -> !p.getDeleted()).filter(p -> p.getTenetId().equalsIgnoreCase(tenetId)).collect(Collectors.toList());

        Map params = Maps.newHashMap();
        params.put("businessKey", buildBusinessKey);
        params.put("deleted", false);
        List<CommonEdBuild> buildList = commonEdBuildMapper.selectAll(params);
        params.put("businessKey", businessKey);
        List<CommonEdArrangeUser> users = commonEdArrangeUserMapper.selectAll(params);
        params.remove("deleted",false);
        List<CommonEdArrange> arrangeList = commonEdArrangeMapper.selectAll(params);

        for(CommonEdArrange arrange:arrangeList){
            if(!arrange.getDeleted()){
                if(!majorList.contains(arrange.getMajorName())||buildList.stream().noneMatch(p->p.getId().equals(arrange.getBuildId()))){
                    arrange.setDeleted(true);
                    arrange.setGmtModified(now);
                    commonEdArrangeMapper.updateByPrimaryKey(arrange);
                }
            }
        }



        List<Map> dataList = Lists.newArrayList();
        Map head=Maps.newHashMap();
        head.put("majorName","");
        head.put("buildName","");
        List<CommonCodeDto> headPropertyList = Lists.newArrayList();
        head.put("propertyList",headPropertyList);
        headPropertyList.add(new CommonCodeDto("子项"));
        headPropertyList.add(new CommonCodeDto("专业"));
        roleList.forEach(p -> headPropertyList.add(new CommonCodeDto(p)));
        dataList.add(head);

        for (CommonEdBuild build : buildList) {
            for (String majorName : majorList) {
                int buildId = build.getId();
                String buildName = build.getBuildName();

                CommonEdArrange commonEdArrange = new CommonEdArrange();
                if (arrangeList.stream().filter(p -> p.getMajorName().equalsIgnoreCase(majorName)).anyMatch(p -> p.getBuildId().equals(buildId))) {
                    //存在有效的
                    commonEdArrange = arrangeList.stream().filter(p -> p.getMajorName().equalsIgnoreCase(majorName)).filter(p -> p.getBuildId().equals(buildId)).findFirst().get();
                    if (!commonEdArrange.getBuildName().equalsIgnoreCase(buildName)||commonEdArrange.getDeleted()) {
                        commonEdArrange.setBuildName(buildName);
                        commonEdArrange.setDeleted(false);
                        commonEdArrange.setGmtModified(new Date());
                        commonEdArrangeMapper.updateByPrimaryKey(commonEdArrange);
                    }
                } else {
                    commonEdArrange.setBuildName(buildName);
                    commonEdArrange.setBuildId(buildId);
                    commonEdArrange.setBusinessKey(businessKey);
                    commonEdArrange.setDeleted(false);
                    commonEdArrange.setMajorName(majorName);
                    commonEdArrange.setSeq(build.getSeq());
                    commonEdArrange.setGmtCreate(now);
                    commonEdArrange.setGmtModified(now);
                    ModelUtil.setNotNullFields(commonEdArrange);
                    commonEdArrangeMapper.insert(commonEdArrange);
                }


                int arrangeId = commonEdArrange.getId();

                Map item=Maps.newHashMap();
                item.put("majorName",majorName);
                item.put("buildName",buildName);
                List<CommonCodeDto> propertyList = Lists.newArrayList();
                propertyList.add(new CommonCodeDto(buildId + "", buildName));
                propertyList.add(new CommonCodeDto(majorName));

                for (int i = 0; i < roleList.size(); i++) {
                    String roleName = roleList.get(i);
                    Map config = Maps.newHashMap();
                    config.put("arrangeId", arrangeId);
                    config.put("roleName", roleName);
                    config.put("roleCode", "");
                    config.put("multiple", false);

                    if (edRoleList.stream().anyMatch(p -> p.getCnName().equalsIgnoreCase(roleName))) {
                        CommonEdRole role = edRoleList.stream().filter(p -> p.getCnName().equalsIgnoreCase(roleName)).findFirst().get();
                        config.put("multiple", role.getMultiple());
                        config.put("roleCode", role.getEnName());
                    }
                    if (users.stream().filter(p -> p.getArrangeId().equals(arrangeId)).anyMatch(p -> p.getRoleName().equalsIgnoreCase(roleName))) {
                        //存在数据
                        CommonEdArrangeUser edArrangeUser = users.stream().filter(p -> p.getArrangeId().equals(arrangeId)).filter(p -> p.getRoleName().equalsIgnoreCase(roleName)).findFirst().get();
                        propertyList.add(new CommonCodeDto(edArrangeUser.getAllUser(), edArrangeUser.getAllUserName(), config));
                        if (StringUtils.isEmpty(config.get("roleCode").toString())) {
                            config.put("roleCode", edArrangeUser.getRoleCode());
                        }
                    } else {
                        propertyList.add(new CommonCodeDto("", "", config));
                    }
                }
                item.put("propertyList",propertyList);
                dataList.add(item);
            }
        }


        return dataList;
    }

    public void setUser(int arrangeId,String roleName,String roleCode,String allUser){
        Map params = Maps.newHashMap();
        params.put("arrangeId", arrangeId);
        params.put("roleCode", roleCode);
        params.put("roleName",roleName);
        Date now=new Date();
        CommonEdArrange commonEdArrange=commonEdArrangeMapper.selectByPrimaryKey(arrangeId);
        CommonFormData commonFormData=commonFormDataMapper.selectByBusinessKey(commonEdArrange.getBusinessKey());
        List<FastUserDto> allUsers=commonUserService.listFastUserByEnLoginList(commonFormData.getTenetId(),MyStringUtil.getStringList(allUser));
        String allUserLogin=StringUtils.join(allUsers.stream().map(FastUserDto::getEnLogin).distinct().collect(Collectors.toList()),",");
        String allUserName=StringUtils.join(allUsers.stream().map(FastUserDto::getCnName).distinct().collect(Collectors.toList()),",");
        CommonEdArrangeUser commonEdArrangeUser=new CommonEdArrangeUser();
        if(PageHelper.count(()->commonEdArrangeUserMapper.selectAll(params))==0){
            commonEdArrangeUser.setBusinessKey(commonFormData.getBusinessKey());
            commonEdArrangeUser.setArrangeId(arrangeId);
            commonEdArrangeUser.setRoleName(roleName);
            commonEdArrangeUser.setRoleCode(roleCode);
            commonEdArrangeUser.setAllUser(allUserLogin);
            commonEdArrangeUser.setAllUserName(allUserName);
            commonEdArrangeUser.setDeleted(false);
            commonEdArrangeUser.setGmtCreate(now);
            commonEdArrangeUser.setGmtModified(now);
            ModelUtil.setNotNullFields(commonEdArrangeUser);
            commonEdArrangeUserMapper.insert(commonEdArrangeUser);
        }else{
            commonEdArrangeUser=commonEdArrangeUserMapper.selectAll(params).get(0);
            commonEdArrangeUser.setDeleted(false);
            commonEdArrangeUser.setAllUser(allUserLogin);
            commonEdArrangeUser.setAllUserName(allUserName);
            commonEdArrangeUser.setGmtModified(now);
            commonEdArrangeUserMapper.updateByPrimaryKey(commonEdArrangeUser);
        }
        setAllUserAsync(arrangeId);
    }

    private void setAllUserAsync(int arrangeId){
        taskExecutor.execute(()->{
            Map params = Maps.newHashMap();
            params.put("arrangeId", arrangeId);
            params.put("deleted",false);
            List<CommonEdArrangeUser> users=commonEdArrangeUserMapper.selectAll(params);
            String allUser=MyStringUtil.getMultiDotString(StringUtils.join(users.stream().filter(p->StringUtils.isNotEmpty(p.getAllUser())).map(CommonEdArrangeUser::getAllUser).distinct().collect(Collectors.toList()),","));
            CommonEdArrange commonEdArrange=commonEdArrangeMapper.selectByPrimaryKey(arrangeId);
            commonEdArrange.setAllUser(allUser);
            commonEdArrange.setGmtModified(new Date());
            commonEdArrangeMapper.updateByPrimaryKey(commonEdArrange);

            edDataService.checkAttendUser(commonEdArrange.getBusinessKey());
        });
    }

    public CommonEdArrangeDto getCommonEdArrangeDto(int referId,String enLogin) {
        CommonEdArrangeDto item = new CommonEdArrangeDto();
        item.setEnLogin(enLogin);
        String businessKey = getDefaultArrangeBusinessKey(referId);
        item.setBusinessKey(businessKey);
        if (StringUtils.isNotEmpty(businessKey)) {
            List<CommonEdArrangeUserDto> userList = listUser(businessKey);
            for (CommonEdArrangeUserDto user : userList) {
                if (MyStringUtil.getStringList(user.getAllUser()).contains(enLogin)) {
                    item.getMyMajorNameList().add(user.getMajorName());
                    item.getMyBuildNameList().add(user.getBuildName());
                }
                item.getMajorNameList().add(user.getMajorName());
                item.getBuildNameList().add(user.getBuildName());
            }
            item.setMajorNameList(item.getMajorNameList().stream().distinct().collect(Collectors.toList()));
            item.setMyMajorNameList(item.getMyMajorNameList().stream().distinct().collect(Collectors.toList()));
            item.setBuildNameList(item.getBuildNameList().stream().distinct().collect(Collectors.toList()));
            item.setMyBuildNameList(item.getMyBuildNameList().stream().distinct().collect(Collectors.toList()));
            item.setUserList(userList);
        }


        taskExecutor.execute(()-> edDataService.checkAttendUser(businessKey));

        return item;
    }

    /**
     * 得到起效的businessKey
     * @param referId
     * @return
     */
    public String getDefaultArrangeBusinessKey(int referId) {
        List<String> referTypeList = Lists.newArrayList("fiveEdArrange");
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("referTypeList", referTypeList);
        params.put("referId", referId);

        List<CommonFormData> formDataList = commonFormDataMapper.selectAll(params).stream().sorted(Comparator.comparing(CommonFormData::getId).reversed()).collect(Collectors.toList());
        if (formDataList.size() > 0) {
            for (CommonFormData formData : formDataList) {
                if (formData.getProcessEnd()) {
                    return formData.getBusinessKey();
                }
            }
            return formDataList.get(0).getBusinessKey();
        }
        return "";
    }

    public List<CommonEdArrangeUserDto> listUser(String businessKey) {
        List<CommonEdArrangeUserDto> list = Lists.newArrayList();
        Map params = Maps.newHashMap();
        params.put("businessKey", businessKey);
        params.put("deleted", false);
        List<CommonEdArrange> arrangeList = commonEdArrangeMapper.selectAll(params);
        List<CommonEdArrangeUser> userList = commonEdArrangeUserMapper.selectAll(params);

        userList.forEach(p -> {
            CommonEdArrangeUserDto dto = CommonEdArrangeUserDto.adapt(p);
            dto.setAllUserNameWithLogin(commonUserService.listUserByEnLoginList("", MyStringUtil.getStringList(dto.getAllUser())).stream().map(u -> u.getCnName() + "(" + u.getEnLogin() + ")").collect(Collectors.joining(",")));
            if (arrangeList.stream().anyMatch(o -> o.getId().equals(dto.getArrangeId()))) {
                CommonEdArrange arrange = arrangeList.stream().filter(o -> o.getId().equals(dto.getArrangeId())).findFirst().get();
                dto.setBuildId(arrange.getBuildId());
                dto.setBuildName(arrange.getBuildName());
                dto.setMajorName(arrange.getMajorName());
            }
            list.add(dto);
        });
        return list;
    }

    public void buildCoDirData(int referId,String buildBusinessKey) {
        taskExecutor.execute(() -> {

            String businessKey = getDefaultArrangeBusinessKey(referId);

            Map params = Maps.newHashMap();
            params.put("businessKey", businessKey);
            params.put("deleted", false);
            if (StringUtils.isNotEmpty(buildBusinessKey)) {
                params.put("businessKey", buildBusinessKey);
            }

            List<CommonEdBuild> buildOList = commonEdBuildMapper.selectAll(params);
            List<Map> buildList = Lists.newArrayList();
            for (CommonEdBuild build : buildOList) {
                Map map = Maps.newHashMap();
                map.put("name", build.getBuildName());
                map.put("id", build.getId());
                map.put("seq", build.getSeq());
                buildList.add(map);
            }

            CommonFormData commonFormData = commonFormDataMapper.selectByBusinessKey(businessKey);
            Map data = JsonMapper.string2Map(commonFormData.getFormData());
            commonCoDirService.buildCoDir(referId, buildList, JsonMapper.getListValue(data, "majorList"),Lists.newArrayList("项目资料", "设计管理", "作图区", "发布区"), commonFormData.getCreator());

        });
    }

    public List<Map> listExcelTemplateData(String businessKey,String buildBusinessKey) {
        if (StringUtils.isEmpty(buildBusinessKey)) buildBusinessKey = businessKey;

        CommonFormData commonFormData = commonFormDataMapper.selectByBusinessKey(businessKey);
        Map data = JsonMapper.string2Map(commonFormData.getFormData());
        List<String> majorList = JsonMapper.getListValue(data, "majorList");
        List<String> roleList = JsonMapper.getListValue(data, "roleList");
        List<Map> list=Lists.newArrayList();
        List<CommonEdBuild> buildList = commonEdBuildService.listData(buildBusinessKey);
        List<CommonEdArrangeUserDto> userList=listUser(businessKey);
        for(CommonEdBuild build:buildList){
            for(String majorName:majorList){
                LinkedHashMap item=Maps.newLinkedHashMap();
                item.put("子项",build.getBuildName());
                item.put("专业",majorName);
                for(String roleName:roleList){
                    Optional<CommonEdArrangeUserDto> optionalUser=userList.stream()
                            .filter(p->p.getBuildName().equalsIgnoreCase(build.getBuildName()))
                            .filter(p->p.getMajorName().equalsIgnoreCase(majorName))
                            .filter(p->p.getRoleName().equalsIgnoreCase(roleName)).findFirst();
                    item.put(roleName,optionalUser.isPresent()?optionalUser.get().getAllUserNameWithLogin():"");
                }
                list.add(item);
            }
        }
        return list;
    }

    public void uploadExcelData(List<Map> newData,String businessKey,String buildBusinessKey,String enLogin){
        if (StringUtils.isEmpty(buildBusinessKey)) buildBusinessKey = businessKey;

        CommonFormData commonFormData = commonFormDataMapper.selectByBusinessKey(businessKey);
        String tenetId=commonFormData.getTenetId();
        Map data = JsonMapper.string2Map(commonFormData.getFormData());
        List<String> majorList = JsonMapper.getListValue(data, "majorList");
        List<String> roleList = JsonMapper.getListValue(data, "roleList");
        List<CommonEdBuild> buildList = commonEdBuildService.listData(buildBusinessKey);
        List<CommonEdRole> edRoleList = commonEdRoleMapper.selectAll(Maps.newHashMap()).stream().filter(p -> !p.getDeleted()).filter(p -> p.getTenetId().equalsIgnoreCase(commonFormData.getTenetId())).collect(Collectors.toList());

        Map head=newData.get(0);
        for(int i=3;i<head.size();i++) {
            String roleName = head.get(i).toString();
            Assert.state(roleList.stream().anyMatch(p -> p.equalsIgnoreCase(roleName)), roleName + "是未匹配的角色!");
            Assert.state(edRoleList.stream().anyMatch(p -> p.getCnName().equalsIgnoreCase(roleName)), roleName + "不是系统内有效的角色!");
        }


        List<CommonEdArrangeUserDto> newList=Lists.newArrayList();

        for(int i=1;i<newData.size();i++){
            Map item=newData.get(i);
            String buildName=item.get(1).toString();
            String majorName=item.get(2).toString();
            Assert.state(buildList.stream().anyMatch(p->p.getBuildName().equalsIgnoreCase(buildName)),i+"行："+buildName+"是未匹配的子项!");
            Assert.state(majorList.stream().anyMatch(p->p.equalsIgnoreCase(majorName)),i+"行："+majorName+"是未匹配的专业!");
            int buildId=buildList.stream().filter(p->p.getBuildName().equalsIgnoreCase(buildName)).findFirst().get().getId();
            for(int j=3;j<item.size();j++) {

                String roleName = head.get(j).toString();
                CommonEdArrangeUserDto newUser = new CommonEdArrangeUserDto();
                newUser.setMajorName(majorName);
                newUser.setBuildName(buildName);


                CommonEdRole edRole = edRoleList.stream().filter(p -> p.getCnName().equalsIgnoreCase(roleName)).findFirst().get();
                newUser.setRoleName(edRole.getCnName());
                newUser.setRoleCode(edRole.getEnName());

                List<String> nameList = MyStringUtil.getStringList(item.get(j).toString());
                List<String> enLoginList=Lists.newArrayList();
                List<String> cnNameList=Lists.newArrayList();
                for(String name:nameList) {
                    name = StringUtils.replace(name, "（", "(");
                    name = StringUtils.replace(name, "）", ")");
                    UserDto userDto = null;
                    if (name.indexOf("(") > 0) {
                        userDto = commonUserService.selectByEnLogin(name.substring(name.indexOf("(")+1, name.length() - 1));
                    }
                    if (userDto == null) {
                        userDto = commonUserService.selectByName(tenetId, name);
                    }
                    if (userDto == null) {
                        userDto = commonUserService.selectByEnLogin(name);
                    }
                    Assert.state(userDto != null && StringUtils.isNotEmpty(userDto.getCnName()), name + "未匹配上的用户!");
                    enLoginList.add(userDto.getEnLogin());
                    cnNameList.add(userDto.getCnName());
                }
                if (!edRole.getMultiple()) {
                    Assert.state(enLoginList.size() <= 1, roleName + "只能设置一个用户!");
                }
                newUser.setAllUser(StringUtils.join(enLoginList,","));
                newUser.setAllUserName(StringUtils.join(cnNameList,","));
                newUser.setBuildId(buildId);
                newList.add(newUser);
            }
        }



        for(CommonEdArrangeUserDto newUser:newList){
            Map params=Maps.newHashMap();
            params.put("businessKey",businessKey);
            params.put("deleted",false);
            params.put("buildId",newUser.getBuildId());
            params.put("majorName",newUser.getMajorName());
            CommonEdArrange edArrange=commonEdArrangeMapper.selectAll(params).get(0);
            setUser(edArrange.getId(),newUser.getRoleName(),newUser.getRoleCode(),newUser.getAllUser());
        }

    }

}
