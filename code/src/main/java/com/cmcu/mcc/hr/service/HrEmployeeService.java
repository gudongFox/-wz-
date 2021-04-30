package com.cmcu.mcc.hr.service;

import com.cmcu.common.exception.ParamException;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.*;

import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.hr.dao.HrEducationMapper;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dao.HrEmployeeSysMapper;
import com.cmcu.mcc.hr.dao.HrPositionMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.dto.HrEmployeeSimpleDto;
import com.cmcu.mcc.hr.entity.*;
import com.cmcu.mcc.sys.dao.SysUserAclMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class HrEmployeeService {
    @Resource
    GuavaCacheService guavaCacheService;
    @Autowired
    HrDeptService hrDeptService;
    @Resource
    HrEmployeeMapper hrEmployeeMapper;
    @Resource
    HrEmployeeSysMapper hrEmployeeSysMapper;
    @Resource
    CommonCodeService commonCodeService;
    @Resource
    HrEducationMapper hrEducationMapper;
    @Resource
    SysUserAclMapper sysUserAclMapper;
    @Resource
    HrQualifyService hrQualifyService;
    @Resource
    HrPositionMapper hrPositionMapper;


    public Map getNewModel() {
        Map data=Maps.newHashMap();
        data.put("userType",commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"员工类型"));
        data.put("userLogin","");
        data.put("userName","");
        data.put("deptId",1);
        data.put("mobile","");

        HrEmployeeSys hrEmployeeSys=hrEmployeeSysMapper.selectLastOne();
        if(hrEmployeeSys!=null) {
            HrEmployee hrEmployee=hrEmployeeMapper.selectByUserLoginOrNo(hrEmployeeSys.getUserLogin());
            data.put("deptId", hrEmployeeSys.getDeptId());
            data.put("userType",hrEmployee.getUserType());
        }
        return data;
    }

    public HrEmployee update(HrEmployeeDto item) {

        HrEmployee model=hrEmployeeMapper.selectByPrimaryKey(item.getId());
        if (checkExist(item.getUserLogin(), item.getId())) {
            throw new ParamException("用户名" + item.getUserLogin() + "已存在!");
        }

        model.setUserNo(item.getUserNo());
        model.setIdCardType(item.getIdCardType());
        model.setIdCardNo(item.getIdCardNo());
        model.setGender(item.getGender());
        model.setAge(item.getAge());
        model.setBirthDay(item.getBirthDay());
        model.setCountry(item.getCountry());
        model.setNation(item.getNation());
        model.setBirthPlace(item.getBirthPlace());
        model.setPolitics(item.getPolitics());
        model.setPoliticsTime(item.getPoliticsTime());
        model.setJoinWorkTime(item.getJoinWorkTime());
        model.setJoinCompanyTime(item.getJoinCompanyTime());
        model.setLeaveCompanyTime(item.getLeaveCompanyTime());
        model.setIdAddress(item.getIdAddress());
        model.setLiveAddress(item.getLiveAddress());
        model.setMobile(item.getMobile());
        model.setOfficeTel(item.getOfficeTel());
        model.setHomeTel(item.getHomeTel());
        model.setEnEmail(item.getEnEmail());
        model.setCityId(item.getCityId());
        model.setAvatar(model.getAvatar());
        model.setSignUrl(item.getSignUrl());
        model.setSignPicUrl(item.getSignPicUrl());
        model.setHeadAttachId(item.getHeadAttachId());
        model.setSignAttachId(item.getSignAttachId());
        model.setSignPicAttachId(item.getSignPicAttachId());
        model.setEducationBackground(item.getEducationBackground());
        model.setCarNo(item.getCarNo());
        model.setUserType(item.getUserType());
        model.setUserStatus(item.getUserStatus());
        model.setSpecialty(item.getSpecialty());
        model.setOtherSpecialty(item.getOtherSpecialty());
        model.setRemark(item.getRemark());
        model.setGmtModified(new Date());
        model.setRanks(item.getRanks());
        model.setRankTime(item.getRankTime());
        model.setLogoGram(item.getLogoGram());
        model.setAvatar(item.getAvatar());
        //ModelUtil.setNotNullFields(model);
       // BeanValidator.check(model);
     /*   //修改登录名 和登录账号
        if (model.getUserName()!=item.getUserName()||model.getUserLogin()!=item.getUserLogin()){

             //更新 用户信息hrEmployeeSys表 登录账号
            HrEmployeeSys hrEmployeeSys = hrEmployeeSysMapper.selectByUserLogin(model.getUserLogin());
            hrEmployeeSys.setUserLogin(item.getUserLogin());
            hrEmployeeSysMapper.updateByPrimaryKey(hrEmployeeSys);
            //更新hrQualify  登录名 登录账号
            HrQualify hrQualify = hrQualifyService.selectLatestByUserLogin(model.getUserLogin());

            HrQualifyDto modelById = hrQualifyService.getModelById(hrQualify.getId());
            modelById.setUserLogin(item.getUserLogin());
            modelById.setUserName(item.getUserName());
            hrQualifyService.update(modelById);

            model.setUserName(item.getUserName());
            model.setUserLogin(item.getUserLogin());
        }
*/
        hrEmployeeMapper.updateByPrimaryKey(model);

        return model;
    }
    /**
     * 判断用户名是否唯一
     * @param userLogin
     * @param userId
     * @return
     */
    private boolean checkExist(String userLogin,Integer userId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("userLogin", userLogin);
        List<HrEmployeeDto> hrEmployees = hrEmployeeMapper.selectAll(params);
        if (hrEmployees.size() == 0) return false;
        if (hrEmployees.size() > 1) return true;
        if(userId==null) return true;
        if(hrEmployees.get(0).getId().equals(userId)) return false;
        return true;
    }

    public HrEmployeeDto getModelById(int id) {
        HrEmployeeDto dto=hrEmployeeMapper.selectByPrimaryKey(id);
        replenishDto(dto);
        return dto;
    }

    public PageInfo<HrEmployeeSimpleDto> listSimplePagedData(Map<String,Object> params, int pageNum, int pageSize) {
        HrQualify hrQualify = hrQualifyService.hrQualifyMapper.selectLatest();
        Assert.state(hrQualify != null, "请联系管理员先初始化设计资格数据!");
        params.put("checkYear", hrQualify.getCheckYear());
        if (params.containsKey("parentDeptId")) {
            int parentDeptId = Integer.parseInt(params.get("parentDeptId").toString());
            List<Integer> deptIds = hrDeptService.listChild(parentDeptId).stream().map(HrDept::getId).collect(Collectors.toList());
            deptIds.add(parentDeptId);
            params.put("deptIds", deptIds);
        }

        String qQualify = params.getOrDefault("qQualify", "").toString();
        System.out.println(qQualify);

        if(qQualify.contains("总师")||qQualify.contains("总设计师")){
            params.put("chiefDesigner",true);
        }
        else if(qQualify.contains("设计")){
            params.put("design",true);
        }
        else if(qQualify.contains("校核")){
            params.put("proofread",true);
        }
        else if(qQualify.contains("审核")){
            params.put("audit",true);
        }
        else if(qQualify.contains("审定")){
            params.put("approve",true);
        }
        else if(qQualify.contains("专业负责人")){
            params.put("majorCharge",true);
        }
        else if(qQualify.contains("项目负责人")){
            params.put("projectCharge",true);
        }
        PageInfo<HrEmployeeSimpleDto> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> hrEmployeeMapper.selectSimpleAll(params));
        pageInfo.getList().forEach(p-> {
            p.setPositionNames(StringUtils.join(MyStringUtil.getStringList(p.getPositionNames()), ","));
            p.setShortPositionNames(p.getPositionNames());
            if(p.getPositionNames().length()>10){
                p.setShortPositionNames(p.getShortPositionNames().substring(0,10)+"...");
            }

            p.setRoleNames(StringUtils.join(MyStringUtil.getStringList(p.getRoleNames()), ","));
            p.setShortRoleNames(p.getRoleNames());
            if(p.getRoleNames().length()>10){
                p.setShortRoleNames(p.getShortRoleNames().substring(0,10)+"...");
            }
        });
        return pageInfo;
    }

    public PageInfo<HrEmployeeSimpleDto> listSimpleDeptPagedData(Map<String,Object> params, int pageNum, int pageSize) {

        if (params.containsKey("parentDeptId")) {
            int parentDeptId = Integer.parseInt(params.get("parentDeptId").toString());
            List<Integer> deptIds = hrDeptService.listChild(parentDeptId).stream().map(HrDept::getId).collect(Collectors.toList());
            deptIds.add(parentDeptId);
            params.put("deptIds", deptIds);
        }

        PageInfo<HrEmployeeSimpleDto> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> hrEmployeeMapper.selectOaSimpleAll(params));
        pageInfo.getList().forEach(p-> {
            p.setPositionNames(StringUtils.join(MyStringUtil.getStringList(p.getPositionNames()), ","));
            p.setShortPositionNames(p.getPositionNames());
            if(p.getPositionNames().length()>10){
                p.setShortPositionNames(p.getShortPositionNames().substring(0,10)+"...");
            }

            p.setRoleNames(StringUtils.join(MyStringUtil.getStringList(p.getRoleNames()), ","));
            p.setShortRoleNames(p.getRoleNames());
            if(p.getRoleNames().length()>10){
                p.setShortRoleNames(p.getShortRoleNames().substring(0,10)+"...");
            }
        });
        return pageInfo;
    }

    public PageInfo<HrEmployeeSimpleDto> listSimpleRolePagedData(Map<String,Object> params, int pageNum, int pageSize) {

        Map map= new HashMap();
        map.put("roleId",params.get("parentRoleId"));
        map.put("key",params.get("key"));

        PageInfo<HrEmployeeSimpleDto> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> hrEmployeeMapper.selectOaRoleSimpleAll(map));
        pageInfo.getList().forEach(p-> {
            p.setPositionNames(StringUtils.join(MyStringUtil.getStringList(p.getPositionNames()), ","));
            p.setShortPositionNames(p.getPositionNames());
            if(p.getPositionNames().length()>10){
                p.setShortPositionNames(p.getShortPositionNames().substring(0,10)+"...");
            }

            p.setRoleNames(StringUtils.join(MyStringUtil.getStringList(p.getRoleNames()), ","));
            p.setShortRoleNames(p.getRoleNames());
            if(p.getRoleNames().length()>20){
                p.setShortRoleNames(p.getShortRoleNames().substring(0,20)+"...");
            }
        });
        return pageInfo;
    }

    public List<HrEmployeeSimpleDto> selectAllSimple() {
        List<HrEmployeeSimpleDto> hrEmployeeSimpleDtoList = guavaCacheService.get("selectAllSimple", () -> {
            HrQualify hrQualify = hrQualifyService.hrQualifyMapper.selectLatest();
            Assert.state(hrQualify != null, "请联系管理员先初始化设计资格数据!");
            Map params=Maps.newHashMap();
            params.put("checkYear", hrQualify.getCheckYear());
            List<HrEmployeeSimpleDto> list = hrEmployeeMapper.selectSimpleAll(params);
            list.forEach(p -> {
                p.setPositionNames(StringUtils.join(MyStringUtil.getStringList(p.getPositionNames()), ","));
                p.setShortPositionNames(p.getPositionNames());
                if (p.getPositionNames().length() > 10) {
                    p.setShortPositionNames(p.getShortPositionNames().substring(0, 15) + "...");
                }
            });
            return Optional.ofNullable(list);
        });
        return hrEmployeeSimpleDtoList;
    }

    public PageInfo<HrEmployeeDto> loadPagedData(Map<String,Object> params, int pageNum, int pageSize) {
        params.put("deleted", 0);
        params.put("q", params.get("q"));
       // params.put("userStatus","在职");
        if (params.containsKey("parentDeptId")) {
            int parentDeptId = Integer.parseInt(params.get("parentDeptId").toString());
            List<Integer> deptIds = hrDeptService.listChild(parentDeptId).stream().map(HrDept::getId).collect(Collectors.toList());
            deptIds.add(parentDeptId);
            params.put("deptIds", deptIds);
        }

        PageInfo<HrEmployeeDto> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> hrEmployeeMapper.selectAll(params));
        pageInfo.getList().forEach(this::replenishDto);
        return pageInfo;
    }

    public List<HrEmployeeDto> listEmployeeByDeptId(int deptId,boolean containChild) {
        Map params = Maps.newHashMap();
        params.put("deleted",false);

        if(containChild) {
            List<Integer> deptIds = hrDeptService.listChild(deptId).stream().map(HrDept::getId).collect(Collectors.toList());
            deptIds.add(deptId);
            params.put("deptIds", deptIds);
        }else{
            params.put("deptId",deptId);
        }
        List<HrEmployeeDto> list = hrEmployeeMapper.selectAll(params);
        list.forEach(this::replenishDto);
        return list;
    }

    public List<HrEmployeeDto> listEmployeeByRoleId(int roleId,String q) {
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("roleId", roleId);
        params.put("q",q);
        List<HrEmployeeDto> list = hrEmployeeMapper.selectAll(params);
        list.forEach(this::replenishDto);
        return list;
    }

    /**
     * 分页查找
     * @param params
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<HrEmployeeDto> listPagedDataByRoleId(Map<String,Object> params, int pageNum, int pageSize) {
        params.put("deleted",false);
        params.put("userStatus","在职");
        PageInfo<HrEmployeeDto> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> hrEmployeeMapper.selectAll(params));
        return pageInfo;
    }

    public PageInfo<HrEmployeeDto> listPagedDataByPositionId(Map<String,Object> params, int pageNum, int pageSize) {
        params.put("deleted",false);
        params.put("userStatus","在职");
        PageInfo<HrEmployeeDto> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> hrEmployeeMapper.selectAll(params));
        return pageInfo;
    }

    /**
     * 通过职务名称去对应的人
     * @param positionName
     * @return
     */
    public List<String> selectUserByPositionName(String positionName){
        Map variables = Maps.newHashMap();
        variables.put("deleted", false);
        variables.put("positionName",positionName);
        List<String> userList=Lists.newArrayList();
        Map map = Maps.newHashMap();
        map.put("deleted", false);
        map.put("positionName",positionName);
        if (hrPositionMapper.selectAll(variables).size()>0){
           HrPosition hrPosition = hrPositionMapper.selectAll(variables).get(0);
           map.put("positionId", hrPosition.getId());
        }

        List<HrEmployeeSys> hrEmployeeDtos = hrEmployeeSysMapper.selectAll(map);
        for (HrEmployeeSys dto:hrEmployeeDtos){
            userList.add(dto.getUserLogin());
        }
        return userList;
    }

    /**
     * 通过角色名称去对应的人
     * @param roleName
     * @return
     */
    public List<String> selectUserByRoleNames(String roleName){
        List<String> userList=Lists.newArrayList();
        Map map = Maps.newHashMap();
        map.put("deleted", false);
        map.put("roleName",","+roleName+",");

        List<HrEmployeeSys> hrEmployeeDtos = hrEmployeeSysMapper.selectAll(map);
        for (HrEmployeeSys dto:hrEmployeeDtos){
            if (hrEmployeeMapper.selectByUserLoginOrNo(dto.getUserLogin()).getUserStatus().equals("在职"))
            userList.add(dto.getUserLogin());
        }
        return userList;
    }
    /**
     * 通过角色名获取 默认第一个用户
     * */
    public HrEmployee selectDefualtUserByRoleName(String roleName){
        HrEmployee rValue=null;
        Map map = Maps.newHashMap();
        map.put("deleted", false);
        map.put("roleName",roleName);
        List<HrEmployeeSys> hrEmployeeDtos = hrEmployeeSysMapper.selectAll(map);
        for (HrEmployeeSys dto:hrEmployeeDtos){
            rValue=hrEmployeeMapper.selectByUserLoginOrNo(dto.getUserLogin());
            break;
        }
        return rValue;
    }
    /**
     * 获取所有排序正职领导
     * 顺序： 监事会主席 总经理 董事长
     * @return
     */
     public  List<String> getPrincipalLeader(){
         String message="";
        List<String> list=new ArrayList<>(3);
        List<String> board = selectUserByPositionName("监事会主席");
        if (board.size()>0){
            list.add(board.get(0));
        }
        List<String> general = selectUserByPositionName("总经理");

        if (general.size()>0){
            list.add(general.get(0));
        }
        List<String> director = selectUserByPositionName("董事长");

        if (director.size()>0){
            list.add(director.get(0));
        }
        return list;
    }

    /**
     * 判断 是否还有正职领导 含有正职领导返回 true  没有返回false
     * @param userLogins
     * @return
     */
     public boolean IsPrincipalLeader(String userLogins){
         List<String> principalLeader = getPrincipalLeader();
        for (String principal:principalLeader){
            if (userLogins.contains(principal)){
                return true;
            }
        }
         return false;
     }

    /**
     * 获取list中所有副职领导 不排序
     * @param userLogins
     * @return
     */
    public  List<String> getViceLeader(String userLogins){
        List<String> viceLeaderList=Lists.newArrayList();
        String principalLeader =StringUtils.join(getPrincipalLeader(),",");
        List<String> selectLeader = MyStringUtil.getStringList(userLogins);
        for (String select:selectLeader){
            if (!principalLeader.contains(select)){
                viceLeaderList.add(select);
            }
        }
        return viceLeaderList;
    }

    /**
     * 以选择的公司正职领导 按照 监事会主席 - 总经理 - 董事长
     * 注：最后结果只包含属于 这三个的职务的人
     * @param principals
     * @return
     */
    public List<String> getPrincipal(String principals){
        List<String> sortList=Lists.newArrayList();
        List<String> principalList = MyStringUtil.getStringList(principals);
        String board =StringUtils.join(selectUserByPositionName("监事会主席"),",") ;
         for (String principal:principalList){
             if (board.contains(principal)){
                 sortList.add(principal);
             }
         }
        String general =StringUtils.join(selectUserByPositionName("总经理"),",") ;
        for (String principal:principalList){
            if (general.contains(principal)){
                sortList.add(principal);
            }
        }
        String director =StringUtils.join(selectUserByPositionName("董事长"),",") ;
        for (String principal:principalList){
            if (director.contains(principal)){
                sortList.add(principal);
            }
        }
         return sortList;
    }

    public List<HrEmployeeDto> selectAll() {
        List<HrEmployeeDto> aclList = guavaCacheService.get("selectAllEmployee", () -> {
            Map params = Maps.newHashMap();
            params.put("deleted", false);
            List<HrEmployeeDto> list = hrEmployeeMapper.selectAll(params);
            list.forEach(this::replenishDto);
            return Optional.ofNullable(list);
        });
        return aclList;
    }

    /**
     * 查询所有 指定部门的人（包括下级部门）
     * @param deptId
     * @return
     */
    public List<HrEmployeeDto> selectAllByDeptId(int deptId) {
        List<HrEmployeeDto> aclList = guavaCacheService.get("selectAllEmployee_"+deptId, () -> {
            Map params = Maps.newHashMap();
            params.put("deleted", false);
            List<Integer> deptIds = hrDeptService.listChild(deptId).stream().map(HrDept::getId).collect(Collectors.toList());
            deptIds.add(deptId);
            params.put("deptIds", deptIds);
            List<HrEmployeeDto> list = hrEmployeeMapper.selectAll(params);
            list.forEach(this::replenishDto);
            return Optional.ofNullable(list);
        });
        return aclList;
    }

    public HrEmployeeDto getModelByUserLogin(String userLogin){
        HrEmployeeDto dto=hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        if (dto==null){
            return null;
        }else {
            replenishDto(dto);
            return dto;
        }
    }




    /**
     * 通过登录名（多用名） 查用户名
     * @param userLogins
     * @return
     */
    public  Map<String,Object> listByUserLogin(String userLogins){
        Map<String,Object> map= Maps.newHashMap();
        if (userLogins==null&&userLogins.equals("")){
            return map;
        }else {
            String[] split = userLogins.split(",");
            List<String> userNames= Lists.newArrayList();
            List<String> phones=Lists.newArrayList();
            List<HrEmployeeDto> hrEmployeeDtoList=Lists.newArrayList();
            Map params= Maps.newHashMap();
            params.put("deleted",false);
            for (String userLogin:split){
                if (userLogin!=null&&!userLogin.equals("")){
                    params.put("userLogin",userLogin);
                    HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectAll(params).get(0);
                    userNames.add(hrEmployeeDto.getUserName());
                    phones.add(hrEmployeeDto.getMobile());
                    hrEmployeeDtoList.add(hrEmployeeDto);
                }
            }
            map.put("userNames",StringUtils.join(userNames,","));
            map.put("phones",StringUtils.join(phones,","));
            map.put("hrEmployeeDtoList",hrEmployeeDtoList);
            return map;
        }
    }


    private void replenishDto(HrEmployeeDto dto) {
        dto.setPositionNames(StringUtils.join(StringUtils.split(dto.getPositionNames(), ","), ","));
        dto.setRoleNames(StringUtils.join(StringUtils.split(dto.getRoleNames(), ","), ","));
        dto.setHrQualify(hrQualifyService.selectLatestByUserLogin(dto.getUserLogin()));
        Map map = new HashMap();
        map.put("userLogin", dto.getUserLogin());
        dto.setAclConfig(PageHelper.count(() -> sysUserAclMapper.selectAll(map)) > 0);

        if(StringUtils.isEmpty(dto.getAvatar())||!dto.getAvatar().startsWith("http")){
            if(StringUtils.isNotEmpty(dto.getHeadAttachId())){
                dto.setAvatar("/common/attach/download/"+dto.getHeadAttachId());
            }
        }


        List<HrEducation> hrEducations = hrEducationMapper.getGraduateSchool(map);
        if (hrEducations.size() > 0) {
            dto.setGraduateCollege(hrEducations.get(0).getSchoolName());
        } else {
            dto.setGraduateCollege(" ");
        }
    }


    public List<HrEmployeeDto> listDataByUserLoginList(String users){
        List<HrEmployeeDto> list=Lists.newArrayList();
        List<String> userLoginList= MyStringUtil.getStringList(users);
        for(String userLogin:userLoginList){
            HrEmployeeDto hrEmployeeDto=getModelByUserLogin(userLogin);
            if(hrEmployeeDto!=null) {
                list.add(hrEmployeeDto);
            }
        }
        return list;
    }

    public List<HrEmployeeDto> listDataByUserLoginList(List<String> users){
        List<HrEmployeeDto> list=Lists.newArrayList();
        for(String userLogin:users){
            HrEmployeeDto hrEmployeeDto=getModelByUserLogin(userLogin);
            if(hrEmployeeDto!=null) {
                list.add(hrEmployeeDto);
            }
        }
        return list;
    }

    public Object selectByUserLoginList(String users) {
        List<HrEmployeeDto> list=Lists.newArrayList();
        List<String> userLoginList= MyStringUtil.getStringList(users);
        //根节点
        HrEmployeeDto foot = new HrEmployeeDto();
        foot.setId(9999);
        foot.setParentId(0);
        foot.setUserName("备选人员");
        foot.setUserLogin("");
        list.add(foot);


        for(String userLogin:userLoginList){
            HrEmployeeDto hrEmployeeDto=getModelByUserLogin(userLogin);
            if(hrEmployeeDto!=null) {
                hrEmployeeDto.setParentId(9999);
                hrEmployeeDto.setIcon("icon-user");
                list.add(hrEmployeeDto);
            }
        }
        return list;
    }

    public void updateMobile(String enLogin,String mobile){
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(enLogin);
        hrEmployeeDto.setMobile(mobile);
        hrEmployeeMapper.updateByPrimaryKey(hrEmployeeDto);
    }
}
