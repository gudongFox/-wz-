package com.cmcu.mcc.hr.service;

import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.hr.dao.HrDeptMapper;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dao.HrEmployeeSysMapper;
import com.cmcu.mcc.hr.dao.HrPositionMapper;
import com.cmcu.mcc.hr.dto.HrDeptDto;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.entity.HrDept;
import com.cmcu.mcc.hr.entity.HrEmployee;
import com.cmcu.mcc.hr.entity.HrEmployeeSys;
import com.cmcu.mcc.hr.entity.HrPosition;
import com.cmcu.mcc.sys.entity.SysRole;
import com.cmcu.mcc.sys.service.SysRoleAclService;
import com.cmcu.mcc.sys.service.SysRoleService;
import com.common.model.JsTreeDto;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SelectEmployeeService {

    @Autowired
    HrEmployeeService hrEmployeeService;

    @Autowired
    HrPositionMapper hrPositionMapper;

    @Autowired
    HrDeptMapper hrDeptMapper;

    @Autowired
    HrPositionService hrPositionService;

    @Resource
    HrEmployeeMapper hrEmployeeMapper;

    @Resource
    HrEmployeeSysMapper hrEmployeeSysMapper;

    @Resource
    SysRoleAclService sysRoleAclService;

    @Autowired
    SysRoleService sysRoleService;

    @Autowired
    HrDeptService hrDeptService;

    public String getNameByUserLogin(String userLogin) {
        if (StringUtils.isEmpty(userLogin)) return "";
        return hrEmployeeMapper.getNameByUserLogin(userLogin);
    }

    /**
     * 查询排序 按照sql 排序
     *
     * @param userLogins
     * @return
     */
    public String getNameByUserLogins(String userLogins) {
        if (StringUtils.isEmpty(userLogins)) return "";
        String[] split = userLogins.split(",");
        String userName = "";
        List<HrEmployee> list = Lists.newArrayList();
        for (String userlogin : split) {
            if (!StringUtils.isEmpty(userlogin)) {
                list.add(hrEmployeeService.getModelByUserLogin(userlogin));
            }
        }
        list.stream().sorted(Comparator.comparing(HrEmployee::getSeq)).collect(Collectors.toList());

        /*   userName=userName+","+hrEmployeeMapper.getNameByUserLogin(userlogin);*/
        for (HrEmployee hr : list) {
            userName += hr.getUserName() + ",";
        }

        return userName.replaceAll("^,*|,*$", "");
    }

    /**
     * 得到某个功能点 某个用户可以访问的按部门划分的数据
     *
     * @param uiSref
     * @param userLogin
     * @return
     */
    public List<Integer> getMyDeptList(String userLogin, String uiSref) {
        //是否有针对用户的单独配置
        Map sysUserAcl = sysRoleAclService.getAclInfoByUserLogin(userLogin, uiSref);
        if (sysUserAcl != null) {
            return MyStringUtil.getIntList(sysUserAcl.get("selectDepts").toString());
        }
        return Lists.newArrayList();
    }

    /**
     * 获取部门 所属事业部Id
     *
     * @param deptId
     * @return
     */
    public int getHeadDeptId(int deptId) {
        HrDept hrDept = hrDeptMapper.selectByPrimaryKey(deptId);
        if (hrDept.getParentId() == 1) {
            return hrDept.getId();
        } else {
            while (hrDept.getParentId() != 1) {
                hrDept = hrDeptMapper.selectByPrimaryKey(hrDept.getParentId());
            }
            return hrDept.getId();
        }
    }

    /**
     * 获取部门 所属事业部名称
     *
     * @param deptId
     * @return
     */
    public String getHeadDeptName(int deptId) {

        HrDept hrDept = hrDeptMapper.selectByPrimaryKey(deptId);
        if (deptId == 1) {
            return hrDept.getName();
        } else if (hrDept.getParentId() == 1) {
            return hrDept.getName();
        } else {
            while (hrDept.getParentId() != 1) {
                hrDept = hrDeptMapper.selectByPrimaryKey(hrDept.getParentId());
            }
            return hrDept.getName();
        }
    }

    public HrEmployeeDto selectByUserLogin(String userLogin) {
        return hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
    }

    public List<HrEmployeeDto> selectByUserLogins(String userLogins) {
        List<HrEmployeeDto> list = Lists.newArrayList();
        String[] split = userLogins.split(",");
        for (String userLogin : split) {
            if (userLogin != null && !"".equals(userLogin)) {
                list.add(selectByUserLogin(userLogin));
            }

        }
        return list.stream().sorted(Comparator.comparing(HrEmployee::getSeq)).collect(Collectors.toList());
    }

    /**
     * 各单位部门负责人 优先正职 >副职
     * 如果当前部门没取到正副职，就取上级部门的正职+副值
     *
     * @param deptId
     * @return
     */
    public List<String> getDeptChargeMen(int deptId) {
        //五洲 一院 二院 环能院部门领导 默认到对应的领导部门
        HrDept hrDept = hrDeptMapper.selectByPrimaryKey(deptId);
        if (hrDept.getParentId() == 75) {
            deptId = 23;
            hrDept = hrDeptMapper.selectByPrimaryKey(deptId);
        } else if (hrDept.getParentId() == 74) {
            deptId = 69;
            hrDept = hrDeptMapper.selectByPrimaryKey(deptId);
        } else if (hrDept.getParentId() == 76) {
            deptId = 31;
            hrDept = hrDeptMapper.selectByPrimaryKey(deptId);
        } else if (hrDept.getParentId() == 125) {
            deptId = 125;
            hrDept = hrDeptMapper.selectByPrimaryKey(deptId);
        } else if (hrDept.getParentId() == 47) { //建研院
            deptId = 47;
            hrDept = hrDeptMapper.selectByPrimaryKey(deptId);
        }
        if (!Strings.isNullOrEmpty(hrDept.getDeptFirstLeader())) {
            return MyStringUtil.getStringList(hrDept.getDeptFirstLeader());
        } else if (!Strings.isNullOrEmpty(hrDept.getDeptSecondLeader())) {//如果正职领导为空 取副职领导作为流程负责人
            return MyStringUtil.getStringList(hrDept.getDeptSecondLeader());
        } else if (Strings.isNullOrEmpty(hrDept.getDeptSecondLeader()) && hrDept.getParentId() != 1 && hrDept.getId() != 1) {//如果当前部门没取到正副职，就取上级部门的正职+副值
            HrDept part = hrDeptMapper.selectByPrimaryKey(hrDept.getParentId());
            return MyStringUtil.getStringList(part.getDeptFirstLeader());
        } else {
            return Lists.newArrayList();
        }
    }

    /**
     * 各单位部门负责人 正职+副值
     *
     * @param deptId
     * @return
     */
    public List<String> getDeptAllChargeMen(int deptId) {
        //五洲 一院 二院 环能院部门领导 默认到对应的领导部门
        HrDept hrDept = hrDeptMapper.selectByPrimaryKey(deptId);
        if (hrDept.getParentId() == 75) {
            deptId = 23;
            hrDept = hrDeptMapper.selectByPrimaryKey(deptId);
        } else if (hrDept.getParentId() == 74) {
            deptId = 69;
            hrDept = hrDeptMapper.selectByPrimaryKey(deptId);
        } else if (hrDept.getParentId() == 76) {
            deptId = 31;
            hrDept = hrDeptMapper.selectByPrimaryKey(deptId);
        } else if (hrDept.getParentId() == 47) {//建研院
            deptId = 47;
            hrDept = hrDeptMapper.selectByPrimaryKey(deptId);
        } else if (hrDept.getParentId() == 125) {//勘察院
            deptId = 156;
            hrDept = hrDeptMapper.selectByPrimaryKey(deptId);
        }
        if (!Strings.isNullOrEmpty(hrDept.getDeptFirstLeader())) {
            return MyStringUtil.getStringList(hrDept.getDeptFirstLeader() + "," + hrDept.getDeptSecondLeader());
        } else {
            return Lists.newArrayList();
        }
    }

    /**
     * 获取当前部门负责人
     * 2020-12-16 HNZ 修改满足多数取领导需求 暂时取消了领导为空的错误提示
     *
     * @param deptId
     * @param parent true:deptId取二级单位领导； false：deptId单位领导
     * @return
     * @Param state  1:优先正职 2:优先副职 3:正副职都取 4:分管领导
     */
    public List<String> getParentDeptChargeMen(int deptId, int state, boolean parent) {
        List<String> list = Lists.newArrayList();

        HrDept hrDept = hrDeptMapper.selectByPrimaryKey(deptId);

        if (parent) {
            //如果不是最上级部门 继续取上级部门
            while (hrDept.getParentId() != 1 && hrDept.getId() != 1) {
                hrDept = hrDeptMapper.selectByPrimaryKey(hrDept.getParentId());
            }
        }

        if (state == 1) {
            //如果取正值 只取二级单位正职
            while (hrDept.getParentId() != 1 && hrDept.getId() != 1) {
                hrDept = hrDeptMapper.selectByPrimaryKey(hrDept.getParentId());
            }
            if (!Strings.isNullOrEmpty(hrDept.getDeptFirstLeader())) {//正职>副值
                list.addAll(MyStringUtil.getStringList(hrDept.getDeptFirstLeader()));
            } else if (!Strings.isNullOrEmpty(hrDept.getDeptSecondLeader())) {
                list.addAll(MyStringUtil.getStringList(hrDept.getDeptSecondLeader()));
            }
        } else if (state == 2) {
            //2020-12-17HNZ 建研院的副职 优先取正值 满足用印审批需求
            if (hrDept.getParentId() == 47) {
                if (!Strings.isNullOrEmpty(hrDept.getDeptFirstLeader())) {//正职>副值
                    list.addAll(MyStringUtil.getStringList(hrDept.getDeptFirstLeader()));
                } else if (!Strings.isNullOrEmpty(hrDept.getDeptSecondLeader())) {
                    list.addAll(MyStringUtil.getStringList(hrDept.getDeptSecondLeader()));
                }
            } else {
                if (!Strings.isNullOrEmpty(hrDept.getDeptSecondLeader())) {//副职>正职
                    list.addAll(MyStringUtil.getStringList(hrDept.getDeptSecondLeader()));
                } else if (!Strings.isNullOrEmpty(hrDept.getDeptFirstLeader())) {
                    list.addAll(MyStringUtil.getStringList(hrDept.getDeptFirstLeader()));
                }
            }

        } else if (state == 3) {
            list.addAll(MyStringUtil.getStringList(hrDept.getDeptFirstLeader() + "," + hrDept.getDeptSecondLeader()));
        } else if (state == 4) {
            list.addAll(MyStringUtil.getStringList(hrDept.getDeptChargeMan()));
        } else {

        }
        if (hrDept.getId() != 1 && state <= 3) {
            //Assert.state(list.size()==0,"请联系管理员配置<"+hrDept.getName()+">相关部门领导");
        }
        return list;

    }

    /**
     * 取二级单位的ID 如果本身就是二级单位 取自己的id
     * 2020-12-16 HNZ  通用逻辑 取当前部门二级单位部门ID
     *
     * @param deptId
     * @return
     */
    public int getSecondDeptId(int deptId) {
        HrDept hrDept = hrDeptMapper.selectByPrimaryKey(deptId);
        //如果不是最上级部门 继续取上级部门
        while (hrDept.getParentId() != 1 && hrDept.getId() != 1) {
            hrDept = hrDeptMapper.selectByPrimaryKey(hrDept.getParentId());
        }

        return hrDept.getId();
    }

    /**
     * 各单位部门室主任 -子部门的部门领导 如果子部门没有正副部门领导
     * 去上级部门 部门正职
     *
     * @param deptId
     * @return
     */
    public List<String> getDeptDirector(int deptId) {
        //五洲 一院 二院 环能院部门领导 默认到对应的领导部门
        HrDept hrDept = hrDeptMapper.selectByPrimaryKey(deptId);
        if (!Strings.isNullOrEmpty(hrDept.getDeptFirstLeader())) {
            return MyStringUtil.getStringList(hrDept.getDeptFirstLeader());
        } else if (!Strings.isNullOrEmpty(hrDept.getDeptSecondLeader())) {//如果正职领导为空 取副职领导作为流程负责人
            return MyStringUtil.getStringList(hrDept.getDeptSecondLeader());
        } else {
            return Lists.newArrayList();
        }
    }

    /**
     * 各单位分管领导
     *
     * @param deptId
     * @return
     */
    public List<String> getOtherDeptChargeMan(int deptId) {
        HrDept hrDept = hrDeptMapper.selectByPrimaryKey(deptId);
        if (!"".equals(hrDept.getDeptChargeMan())) {
            return MyStringUtil.getStringList(hrDept.getDeptChargeMan());
        } else {
            HrDept parenDept = hrDeptMapper.selectByPrimaryKey(hrDept.getParentId());
            if (!"".equals(parenDept.getDeptChargeMan())) {
                return MyStringUtil.getStringList(parenDept.getDeptChargeMan());
            }
        }
        return new ArrayList<>();
    }

    /**
     * 各单位财务人员
     *
     * @param deptId
     * @return
     */
    public List<String> getFinanceChargeMen(int deptId) {
        HrDept hrDept = hrDeptMapper.selectByPrimaryKey(deptId);
        if (hrDept.getDeptFinanceMan() != null && !"".equals(hrDept.getDeptFinanceMan())) {
            return MyStringUtil.getStringList(hrDept.getDeptFinanceMan());
        } else {
            HrDept parenDept = hrDeptMapper.selectByPrimaryKey(hrDept.getParentId());
            if (!"".equals(parenDept.getDeptFinanceMan())) {
                return MyStringUtil.getStringList(parenDept.getDeptFinanceMan());
            }
        }
        return new ArrayList<>();
    }

    /**
     * 根据行政职务对应部门的用户
     *
     * @param positionName 职务名称
     * @param deptId       当前部门Id 如果传入部门ID==1 则取所有的职务人员
     * @return
     */
    public List<String> listUserByPositionName(String positionName, int deptId) {
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("positionName", positionName);
        List<String> list = Lists.newArrayList();
        //根据部门来判断职务是否对应
        HrDeptDto hrDept = hrDeptService.getModelById(deptId);
        //循环取到传入部门最上级 (二级单位)
        while (hrDept.getParentId() != 1 && hrDept.getId() != 1) {
            hrDept = hrDeptService.getModelById(hrDept.getParentId());
        }
        List<HrEmployeeDto> employees = hrEmployeeMapper.selectAll(params);

        if (hrDept.getId() == 1) {
            for (HrEmployeeDto hr : employees) {
                list.add(hr.getUserLogin());
            }
        } else {
            //获取有效部门下所有子部门
            List<HrDeptDto> listSureDept = hrDeptService.listChild(hrDept.getId());
            listSureDept.add(hrDept);
            for (HrEmployeeDto hr : employees) {
                if (listSureDept.stream().anyMatch(p -> p.getId().equals(hr.getDeptId()))) {
                    list.add(hr.getUserLogin());
                }
            }
        }
        return list;
    }

    /**
     * 市场管理部登记 项目投标报名申请表
     *
     * @return
     */
    public HrEmployeeDto getMarketRegister() {
        return null;
    }

    public HrEmployeeDto getDeptProfessional(int deptId) {
        String[] keyPositionNames = new String[]{"主任工程师"};
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("deptId", deptId);
        for (String positionName : keyPositionNames) {
            params.put("positionName", positionName);
            if (PageHelper.count(() -> hrEmployeeMapper.selectAll(params)) > 0) {
                return hrEmployeeMapper.selectAll(params).get(0);
            }
        }
        return null;
    }

    /**
     * 得到某部门 某专业的 专业负责人
     *
     * @param majorName
     * @param deptId
     * @return
     */
    public HrEmployeeDto getMajorChargeMan(String majorName, int deptId) {
        String[] keyMajorNames = new String[]{majorName + "专业负责人"};
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("deptId", deptId);
        for (String positionName : keyMajorNames) {
            params.put("positionName", positionName);
            if (PageHelper.count(() -> hrEmployeeMapper.selectAll(params)) > 0) {
                return hrEmployeeMapper.selectAll(params).get(0);
            }
        }
        return null;
    }

    /**
     * 判读一个人  职务是否有部门负责人
     *
     * @param userLogin
     * @return
     */
    public boolean getPrincipalByUserLogin(String userLogin) {
        HrEmployeeDto hrEmployeeDto = selectByUserLogin(userLogin);
        if (!"".equals(hrEmployeeDto.getPositionIds())) {
            String positionIds = hrEmployeeDto.getPositionIds();
            String[] split = positionIds.split(",");
            for (String id : split) {
                if (!id.isEmpty()) {
                    if (hrPositionService.getModelById(Integer.parseInt(id)).getDeptCharge()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 五洲获取 公司正职领导
     */
    public List<String> getCompanyLeader() {
        List<String> list = new ArrayList<>(3);
        List<String> board = hrEmployeeService.selectUserByPositionName("监事会主席");
        if (board.size() > 0) {
            list.add(board.get(0));
        }
        List<String> general = hrEmployeeService.selectUserByPositionName("总经理");

        if (general.size() > 0) {
            list.add(general.get(0));
        }
        List<String> director = hrEmployeeService.selectUserByPositionName("董事长");

        if (director.size() > 0) {
            list.add(director.get(0));
        }
        return list;
    }

    /**
     * 五洲获取 公司副职领导
     */
    public List<String> getViceLeader() {
        List<String> list = hrEmployeeService.selectUserByPositionName("副总经理");
        return list;
    }

    /**
     * 获取公文归档人员
     *
     * @return
     */
    public List<String> getDocumentFileMan() {
        return MyStringUtil.getStringList("2802");
    }

    /**
     * 得到某个部门的分管领导
     *
     * @param deptId
     * @return
     */

    public List getChargeLeaderByDeptId(int deptId) {
        List list = new ArrayList();
        list.add("liujian2");//设计
        list.add("wangxi");//勘察
        return list;
    }

    /**
     * 得到主任工程师
     *
     * @return
     */
    public String getEngineerChargeMan(int deptId) {
        return "luodong";
    }

    public String getMarketChargeMan() {
        return "taoyuxia";
    }

    public String getContractChargeMan() {
        return "chenyanlin";
    }

    public String getTechChargeMan() {
        return "chenxueqing";
    }

    public String getFinanceChargeMan() {
        return "zhoujing2";
    }

    public String getEpcMisChargeMan() {
        return "caodan";
    }

    public String getSecChargeMan() {
        return "zhoujianlan";
    }

    public String getLawChargeMan() {
        return "wangsisi";
    }

    /**
     * 按照 角色名称 得到相应人员
     *
     * @return
     */
    public List<String> getUserListByRoleName(String roleName) {
        SysRole sysRole = sysRoleService.selectByName(roleName);
        Assert.state(sysRole.getId() != null, "未找到对应角色:" + roleName);
        Map params = new HashMap();
        params.put("deleted", false);
        params.put("roleId", sysRole.getId());
        List<HrEmployeeDto> hrEmployeeDtos = hrEmployeeMapper.selectAll(params);
        List<String> users = new ArrayList<>();
        for (HrEmployeeDto dto : hrEmployeeDtos) {
            users.add(dto.getUserLogin());
        }
        if (sysRole.getId() == 0) {
            return new ArrayList<>();
        }
        return users;
    }

    /**
     * 获取只能管理部门负责人
     *
     * @return
     */
    public List<String> getFunctionDeptChargeMen() {
        List<String> functionDeptChargeMen = new ArrayList<>();
        functionDeptChargeMen.addAll(getDeptChargeMen(48));//经营发展部领导
        functionDeptChargeMen.addAll(getDeptChargeMen(11));//质量技术部领导
        functionDeptChargeMen.addAll(getDeptChargeMen(18));//财务金融部领导
        functionDeptChargeMen.addAll(getDeptChargeMen(59));//公司办公室部领导
        functionDeptChargeMen.addAll(getDeptChargeMen(29));//工程管理部领导
        functionDeptChargeMen.addAll(getDeptChargeMen(9));//纪检工作部领导
        return functionDeptChargeMen;
    }

    /**
     * 获取只能管理部门 分管领导
     *
     * @return
     */
    public List<String> getFunctionDeptOtherChargeMen() {
        List<String> functionDeptOtherChargeMen = new ArrayList<>();
        functionDeptOtherChargeMen.addAll(getOtherDeptChargeMan(48));//经营发展部领导
        functionDeptOtherChargeMen.addAll(getOtherDeptChargeMan(11));//质量技术部领导
        functionDeptOtherChargeMen.addAll(getOtherDeptChargeMan(18));//财务金融部领导
        functionDeptOtherChargeMen.addAll(getOtherDeptChargeMan(59));//公司办公室部领导
        functionDeptOtherChargeMen.addAll(getOtherDeptChargeMan(29));//工程管理部领导
        functionDeptOtherChargeMen.addAll(getOtherDeptChargeMan(9));//纪检工作部领导
        return functionDeptOtherChargeMen;
    }

    /**
     * 通过职务名称 和 deptId 去对应的人
     *
     * @param positionName
     * @return
     */
    public List<String> selectUserByPositionNameAndDeptId(String positionName, int deptId) {
        Map variables = Maps.newHashMap();
        variables.put("deleted", false);
        variables.put("positionName", positionName);
        List<String> userList = Lists.newArrayList();
        Map map = Maps.newHashMap();
        map.put("deleted", false);
        map.put("positionName", positionName);
        map.put("deptId", deptId);
        if (hrPositionMapper.selectAll(variables).size() > 0) {
            HrPosition hrPosition = hrPositionMapper.selectAll(variables).get(0);
            map.put("positionId", hrPosition.getId());
        }

        List<HrEmployeeSys> hrEmployeeDtos = hrEmployeeSysMapper.selectAll(map);
        for (HrEmployeeSys dto : hrEmployeeDtos) {
            userList.add(dto.getUserLogin());
        }
        return userList;
    }

    //判断是否为 总师角色
    public boolean isTotalChargeMan(String userLogin) {
        List<String> totalChargeMen = new ArrayList<>();
        totalChargeMen.addAll(getUserListByRoleName("总师"));
        totalChargeMen.addAll(getUserListByRoleName("一院总师"));
        totalChargeMen.addAll(getUserListByRoleName("二院总师"));
        totalChargeMen.addAll(getUserListByRoleName("钢结构总师"));
        totalChargeMen.addAll(getUserListByRoleName("轨道交通总师"));
        totalChargeMen.addAll(getUserListByRoleName("环能总师"));
        totalChargeMen.addAll(getUserListByRoleName("建研院总师"));
        totalChargeMen.addAll(getUserListByRoleName("五特总师"));
        totalChargeMen.addAll(getUserListByRoleName("五源总师"));
        if (totalChargeMen.contains(userLogin)) {
            return true;
        } else {
            return false;
        }
    }

    //获取部门列表 的人员微信id
    public List<String> getUserWxIdListByDeptIds(String deptIds) {
        List<Integer> deptLists = MyStringUtil.getIntList(deptIds);
        List<String> wxIds = new ArrayList<>();
        for (int deptId : deptLists) {
            List<HrEmployeeDto> hrEmployeeDtos = hrEmployeeService.listEmployeeByDeptId(deptId, true);
            for (HrEmployeeDto dto : hrEmployeeDtos) {
                HrEmployeeSys hrEmployeeSys = hrEmployeeSysMapper.selectByUserLogin(dto.getUserLogin());
                if (!Strings.isNullOrEmpty(hrEmployeeSys.getWxId())) {
                    if (!wxIds.contains(hrEmployeeSys.getWxId())) {
                        wxIds.add(hrEmployeeSys.getWxId());
                    }
                }
            }
        }
        return wxIds;
    }

    /**
     * 获取各单位经营人员
     * 2020-12-23 HNZ修改获取经营人员方式 满足动态配置角色取人。
     */
    public List<String> getBusinessMenByDeptId(int deptId) {
        List<String> businessMen = new ArrayList<>();
        int headDeptId = getHeadDeptId(deptId);

        SysRole sysRole = sysRoleService.selectByName("经营授权人员（合同）");
        Map params = new HashMap();
        params.put("deleted", false);
        params.put("roleId", sysRole.getId());
        List<HrEmployeeDto> hrEmployeeDtos = hrEmployeeMapper.selectAll(params);
        for (HrEmployeeDto dto : hrEmployeeDtos) {
            //判断人员当前部门 是否属于 需求部门 headDeptId 2020-12-23 HNZ
            if (dto.getDeptId() == headDeptId) {
                businessMen.add(dto.getUserLogin());
            } else {
                //判断人员上级部门是否 属于需求部门 headDeptId 2020-12-23 HNZ
                int userParentDeptId = getHeadDeptId(dto.getDeptId());
                if (userParentDeptId == headDeptId) {
                    businessMen.add(dto.getUserLogin());
                }
            }
        }

       /* //第一设计研究院
        if(headDeptId==75){
           //businessMen.addAll(getUserListByRoleName("第一设计院经营人员"));
            //刘云兵
            businessMen.add("2020");
            //崔锴
            businessMen.add("2668");
            //李达超
            businessMen.add("9300");
        }
        else if(headDeptId==74){//第二设计研究院
            //冯海军
            businessMen.add("0881");
            //姜晓云
            businessMen.add("9409");
        }
        else if(headDeptId==76){//环境与能源设计研究院
            //刘颖彭
            businessMen.add("2722");
            //张茜
            businessMen.add("2774");
            //王庆海
            businessMen.add("2884");
        }
        else if(headDeptId==47){//建筑规划研究院
            //侯瑾
            businessMen.add("2813");
            //顾萌萌
            businessMen.add("9503");
            //孙亚俊
            businessMen.add("9548");
        }
        else if(headDeptId==45){//五环国际
            //史金忠
            businessMen.add("2160");
            //翟金
            businessMen.add("2746");
        }
        else if(headDeptId==12){//五洲总兴
            //杨天保
            businessMen.add("2695");
            //蔡君艳
            businessMen.add("2708");
        }
        else if(headDeptId==34){//五特
            //张宇
            businessMen.add("2205");
            //万静
            businessMen.add("2993");
        }
        else if(headDeptId==63){//轨道
            //路建峰
            businessMen.add("9555");
            //李漫 2020-12-23HNZ 删除
            ///businessMen.add("2915");
        }
        else if(headDeptId==53){//石化
            //何逸
            businessMen.add("2898");
        }
        else if(headDeptId==36){//五源现代
            //刘亚军
            businessMen.add("1683");
        }
        else if(headDeptId==20){//钢结构
            //刘威
            businessMen.add("2358");
            //刘洪
            businessMen.add("9311");
        }
        else if(headDeptId==125){//勘察所
            //肖坚
            businessMen.add("0805");
            //武迪
            businessMen.add("9311");
        }
        else if(headDeptId==7){//炸药中心
            //赵鑫
            businessMen.add("2447");
        }else if(headDeptId==2){//开发小组测试
            businessMen.add("2863");
        }*/

        return businessMen;

    }


    //获取独立法人单位
    public List<Integer> getIndependentDeptIds() {
        List<Integer> deptIds = new ArrayList<>();
        deptIds.add(125);//勘察所
        deptIds.add(45);//五环
        deptIds.add(34);//五特
        deptIds.add(12);//中兴
        deptIds.add(75);//一院
        deptIds.add(74);//二院
        deptIds.add(47);//建研院
        deptIds.add(76);//环能院
        deptIds.add(20);//钢结构中心
        deptIds.add(36);//五源
        deptIds.add(53);//石化
        deptIds.add(63);//轨道
        deptIds.add(7);//火炸药院
        deptIds.add(50);//计算所
        deptIds.add(13);//成品室
        return deptIds;
    }

    //获取生产/辅助部门
    public List<Integer> getProductDeptIds() {
        List<Integer> deptIds = new ArrayList<>();
        deptIds.add(75);//一院
        deptIds.add(74);//二院
        deptIds.add(47);//建研院
        deptIds.add(76);//环能院
        deptIds.add(20);//钢结构中心
        deptIds.add(36);//五源
        deptIds.add(53);//石化
        deptIds.add(63);//轨道
        deptIds.add(7);//火炸药院
        deptIds.add(50);//计算所
        deptIds.add(13);//成品室
        deptIds.add(158);//建研院总部
        return deptIds;
    }

    //获取职能部门
    public List<Integer> getFunctionDeptIds() {
        List<Integer> deptIds = new ArrayList<>();
        deptIds.add(59);//公司办
        deptIds.add(100);//保密
        deptIds.add(72);//党群
        deptIds.add(48);//经营
        deptIds.add(18);//财务
        deptIds.add(38);//人资
        deptIds.add(29);//工程
        deptIds.add(9);//纪检
        deptIds.add(101);//科研
        deptIds.add(67);//行政
        deptIds.add(58);//档案
        deptIds.add(66);//物业
        deptIds.add(11);//信息
        deptIds.add(2);//开发小组
        return deptIds;
    }


    //获取部门的财务人员 部门没有则查询事业部财务人员
    public List<String> getDeptFinanceMan(int deptId) {
        List<String> financeMen = new ArrayList<>();
        HrDept hrDept = hrDeptMapper.selectByPrimaryKey(deptId);
        if (Strings.isNullOrEmpty(hrDept.getDeptFinanceManName())) {
            int headDeptId = getHeadDeptId(deptId);
            HrDept headDept = hrDeptMapper.selectByPrimaryKey(headDeptId);
            financeMen.addAll(MyStringUtil.getStringList(headDept.getDeptFinanceMan()));
        } else {
            financeMen.addAll(MyStringUtil.getStringList(hrDept.getDeptFinanceMan()));
        }
        return financeMen;

    }

    //获取查看 全部预算的人员
    public List<String> getBudgetAdmins() {
        List<String> budgetAdmins = new ArrayList<>();
        budgetAdmins.add("2863");
        budgetAdmins.add("2819");
        return budgetAdmins;
    }

    //获取公司领导 与 部门负责人（中层领导）
    public List<String> getCompanyLeaders() {
        List<String> companyLeaders = new ArrayList<>();
        //公司领导
        List<HrEmployeeSys> hrEmployeeSys = hrDeptService.selectEmployee(16);
        for (HrEmployeeSys hrEmployee : hrEmployeeSys) {
            if (!companyLeaders.contains(hrEmployee.getUserLogin())) {
                companyLeaders.add(hrEmployee.getUserLogin());
            }
        }
        //所有部门
        List<HrDeptDto> hrDeptDtos = hrDeptService.selectAll();
        for (HrDeptDto dto : hrDeptDtos) {
            //部门负责人
            List<String> deptChargeMen = getDeptAllChargeMen(dto.getId());
            for (String deptChargeMan : deptChargeMen) {
                if (!companyLeaders.contains(deptChargeMan)) {
                    companyLeaders.add(deptChargeMan);
                }
            }
        }

        return companyLeaders;
    }

    //获取自己负责财务的部门 及其子部门
    public List<Integer> getMyFinanceDeptIds(String userLogin) {
        List<Integer> deptIdLists = new ArrayList<>();
        List<HrDeptDto> hrDeptDtos = hrDeptService.selectAll();
        for (HrDeptDto dto : hrDeptDtos) {
            if (dto.getDeptFinanceMan() != null && dto.getDeptFinanceMan().contains(userLogin)) {
                deptIdLists.add(dto.getId());
                List<Integer> deptIds = hrDeptService.listChild(dto.getId()).stream().map(HrDept::getId).collect(Collectors.toList());
                deptIdLists.addAll(deptIds);
            }
        }
        return deptIdLists;
    }

    /**
     * 获取 指定部门 指定角色的人员列
     *
     * @param deptId
     * @param roleName
     * @return
     */
    public List<String> getDeptRoleUser(int deptId, String roleName) {
        List<String> listUser = Lists.newArrayList();
        SysRole sysRole = sysRoleService.selectByName(roleName);
        Assert.state(sysRole.getId() > 0, "为查找到指定角色" + roleName + "，请联系管理员！");

        Map params = new HashMap();
        params.put("deleted", false);
        params.put("roleId", sysRole.getId());

        //根据部门来判断职务是否对应
        HrDeptDto hrDept = hrDeptService.getModelById(deptId);
        //循环取到传入部门最上级 (二级单位)
        while (hrDept.getParentId() != 1 && hrDept.getId() != 1) {
            hrDept = hrDeptService.getModelById(hrDept.getParentId());
        }

        List<HrEmployeeDto> employees = hrEmployeeMapper.selectAll(params);

        if (hrDept.getId() == 1) {
            for (HrEmployeeDto hr : employees) {
                listUser.add(hr.getUserLogin());
            }
        } else {
            //获取有效部门下所有子部门
            List<HrDeptDto> listSureDept = hrDeptService.listChild(hrDept.getId());
            listSureDept.add(hrDept);
            for (HrEmployeeDto hr : employees) {
                if (listSureDept.stream().anyMatch(p -> p.getId().equals(hr.getDeptId()))) {
                    listUser.add(hr.getUserLogin());
                }
            }
        }
        Assert.state(listUser.size() > 0, "未查到" + hrDeptMapper.selectByPrimaryKey(deptId).getName() + "部门下指定" + roleName + "角色人员");
        return listUser;

    }


    public List<JsTreeDto> listJsTreeData(String tenetId, String selects, int parentId, String disabled) {
        List<JsTreeDto> list = org.apache.commons.compress.utils.Lists.newArrayList();
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("tenetId", tenetId);
        List<HrDept> dataList = hrDeptMapper.selectAll(params).stream().sorted(Comparator.comparing(HrDept::getSeq)).collect(Collectors.toList());

        recursiveAddChildNode(parentId, list, dataList);
        List<Integer> selectIdList = MyStringUtil.getIntList(selects);
        if (selectIdList.size()>0){
            list.stream().filter(p -> selectIdList.contains(p.getId())).forEach(p -> {
                p.getState().setSelected(true);
            });
        }else {
            if (list.size()>0){
                list.get(0).getState().setSelected(true);
            }
        }

        if (org.apache.commons.lang3.StringUtils.isNotEmpty(disabled)) {
            List<Integer> disableIdList = MyStringUtil.getIntList(disabled);
            list.stream().filter(p -> disableIdList.contains(p.getId())).forEach(p -> {
                p.getState().setDisabled(true);
            });
        }
        list.stream().forEach(p -> {
            if (p.getParent().equals(parentId)) {
                p.setParent("#");
            }
        });
        return list;
    }

    private void recursiveAddChildNode(int parentId, List<JsTreeDto> list, List<HrDept> dataList) {
        List<HrDept> childList = dataList.stream().filter(p -> p.getParentId().equals(parentId)).collect(Collectors.toList());
        childList.forEach(p -> {
            JsTreeDto item = new JsTreeDto();
            item.setId(p.getId());
            item.setText(p.getName());
            item.setParent(p.getParentId());
            item.setData(p);

            list.add(item);
            recursiveAddChildNode(p.getId(), list, dataList);
        });
    }


}
