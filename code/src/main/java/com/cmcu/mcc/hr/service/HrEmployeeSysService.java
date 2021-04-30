package com.cmcu.mcc.hr.service;

import com.cmcu.common.service.CommonConfigService;
import com.cmcu.common.util.*;
import com.cmcu.mcc.hr.dao.HrDeptMapper;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dao.HrEmployeeSysMapper;
import com.cmcu.mcc.hr.dao.HrPositionMapper;
import com.cmcu.mcc.hr.dto.HrDeptDto;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.dto.HrEmployeeSysDto;
import com.cmcu.mcc.hr.entity.HrDept;
import com.cmcu.mcc.hr.entity.HrEmployee;
import com.cmcu.mcc.hr.entity.HrEmployeeSys;
import com.cmcu.mcc.hr.entity.HrPosition;
import com.cmcu.common.entity.CommonConfig;
import com.cmcu.mcc.sys.entity.SysRole;
import com.cmcu.mcc.sys.service.SysRoleAclService;
import com.cmcu.mcc.sys.service.SysRoleService;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import javax.annotation.Resource;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HrEmployeeSysService {

    @Autowired
    SysRoleService sysRoleService;

    @Resource
    HrEmployeeSysMapper hrEmployeeSysMapper;

    @Resource
    HrEmployeeMapper hrEmployeeMapper;

    @Resource
    HrDeptMapper hrDeptMapper;

    @Resource
    HrPositionMapper hrPositionMapper;

    @Resource
    CommonConfigService commonConfigService;



    @Autowired
    HrDeptService hrDeptService;
    @Autowired
    HrPositionService hrPositionService;

    @Autowired
    SysRoleAclService sysRoleAclService;

    @Autowired
    HrQualifyService hrQualifyService;

    @Autowired
    GuavaCacheService guavaCacheService;

    /**
     * 得到某个功能点 某个用户可以访问的按部门划分的数据
     * @param uiSref
     * @param userLogin
     * @return
     */
    public List<Integer> getMyDeptList(String userLogin,String uiSref) {
        //是否有针对用户的单独配置
        Map sysUserAcl = sysRoleAclService.getAclInfoByUserLogin(userLogin, uiSref);
        if (sysUserAcl != null) {
            return MyStringUtil.getIntList(sysUserAcl.getOrDefault("selectDepts","").toString());
        }
        return Lists.newArrayList();
    }





    /**
     * 得到某个功能点 默认 登录人 所属部门权限
     * @param uiSref
     * @param userLogin
     * @return
     */
    public List<Integer> getMyDeptList2(String userLogin,String uiSref) {
        //是否有针对用户的单独配置
        Map sysUserAcl = sysRoleAclService.getAclInfoByUserLogin(userLogin, uiSref);
        if (sysUserAcl != null) {
            List<Integer> selectDepts = Lists.newArrayList();

            if (MyStringUtil.getIntList(sysUserAcl.get("selectDepts").toString()).size()==0){
                int deptId = selectByUserLogin(userLogin).getDeptId();
                HrDeptDto modelById = hrDeptService.getModelById(deptId);
                if (!modelById.getParentId().equals(1)){
                    selectDepts=  MyStringUtil.getIntList(modelById.getParentId().toString());
                }else {
                    selectDepts=  MyStringUtil.getIntList(modelById.getId().toString());
                }
            }
            return selectDepts;
        }
        return Lists.newArrayList();
    }

    /**
     * 如果没有特殊配置，部门领导人 拥有当前部门权限
     * @param userLogin
     * @param uiSref
     * @return
     */
    public List<Integer> getMyDeptListOa(String userLogin,String uiSref) {
        //是否有针对用户的单独配置
        Map sysUserAcl = sysRoleAclService.getAclInfoByUserLogin(userLogin, uiSref);
        if (sysUserAcl != null) {
            HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
            HrDept hrDept = hrDeptMapper.selectByPrimaryKey(hrEmployeeDto.getDeptId());
            String deptCharge=hrDept.getDeptChargeMan()+","+hrDept.getDeptFirstLeader()+","+hrDept.getDeptSecondLeader();
            String selectDepts1 = sysUserAcl.getOrDefault("selectDepts","").toString();
            List<Integer> selectDepts = MyStringUtil.getIntList(selectDepts1);
            if (selectDepts.size()==1&&hrEmployeeDto.getDeptId()==selectDepts.get(0)){
                if (deptCharge.contains(userLogin)){
                    return MyStringUtil.getIntList(sysUserAcl.get("selectDepts").toString());
                }else {
                    return Lists.newArrayList();
                }
            }else {
                return MyStringUtil.getIntList(sysUserAcl.getOrDefault("selectDepts","").toString());
            }

        }
        return Lists.newArrayList();
    }


    /**
     * 根据用户的职务、角色得到拥有的数据权限
     * @param userLogin
     * @return
     */
    public List<Integer> getChildDeptList(String userLogin) {
        List<Integer> deptIdList = Lists.newArrayList();
        HrEmployeeSys hrEmployeeSys = getModelByUserLogin(userLogin);
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        //取到用户职位和角色的数据权限合集
        List<Integer> positionIds = MyStringUtil.getIntList(hrEmployeeSys.getPositionIds());
        List<Integer> roleIds = MyStringUtil.getIntList(hrEmployeeSys.getRoleIds());
        if (positionIds.size() > 0) {
            hrPositionMapper.selectAll(params).stream()
                    .filter(p -> positionIds.contains(p.getId()))
                    .map(HrPosition::getChildDeptIds).collect(Collectors.toList())
                    .forEach(p -> deptIdList.addAll(MyStringUtil.getIntList(p)));
        }
        if (roleIds.size() > 0) {
            sysRoleService.selectAll().stream()
                    .filter(p -> roleIds.contains(p.getId()))
                    .map(SysRole::getChildDeptIds).collect(Collectors.toList())
                    .forEach(p -> deptIdList.addAll(MyStringUtil.getIntList(p)));
        }
        deptIdList.add(hrEmployeeSys.getDeptId());
        return deptIdList.stream().distinct().collect(Collectors.toList());
    }

    /**
     * 得到某个用户可以访问哪些部门的Id
     * @param userLogin
     * @return
     */
    public List<Integer> getMyDeptList(String userLogin){
        HrEmployeeSys hrEmployeeSys=getModelByUserLogin(userLogin);
        List<Integer> deptIdList=Lists.newArrayList();
        deptIdList.add(hrEmployeeSys.getDeptId());
        return deptIdList;
    }

    public HrEmployeeSysDto getModelByUserLogin(String userLogin){
        HrEmployeeSys item=selectByUserLogin(userLogin);
        return getDto(item);
    }

    public String getConfigData(String userLogin){
        HrEmployeeSys model=selectByUserLogin(userLogin);
        if(StringUtils.isEmpty(model.getConfigData())) {
            Map config = Maps.newHashMap();
            CommonConfig sysConfig = commonConfigService.getConfig();
            config.put("rootPath", "c:\\" + sysConfig.getAppCode());
            config.put("autoClean", false);
            model.setConfigData(JsonMapper.obj2String(config));
            hrEmployeeSysMapper.updateByPrimaryKey(model);
        }
        return model.getConfigData();
    }

    public void updateConfigData(String userLogin,String configData) {
        HrEmployeeSys model = selectByUserLogin(userLogin);
        model.setConfigData(configData);
        hrEmployeeSysMapper.updateByPrimaryKey(model);
    }

    public void updateRoleIds(String userLogin,String roleIds) {
        HrEmployeeSys model = selectByUserLogin(userLogin);
        List<String> ids=Arrays.asList(StringUtils.split(roleIds, ",")).stream().filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
        List<SysRole> roles=sysRoleService.selectAll();
        for(int i=ids.size()-1;i>=0;i--){
            String id=ids.get(i);
            if(roles.stream().noneMatch(p->p.getId().equals(Integer.parseInt(id)))){
                ids.remove(id);
            }
        }
        List<String> roleNames= Lists.newArrayList();
        for(SysRole sysRole:roles){
            if(ids.contains(sysRole.getId().toString())){
                roleNames.add(sysRole.getName());
            }
        }
        model.setRoleIds("," + StringUtils.join(ids, ",") + ",");
        model.setRoleNames("," + StringUtils.join(roleNames, ",") + ",");
        hrEmployeeSysMapper.updateByPrimaryKey(model);
    }


    public void updatePositionIds(String userLogin,String positionIds){
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        HrEmployeeSys model = selectByUserLogin(userLogin);
        List<String> ids=Arrays.asList(StringUtils.split(positionIds, ",")).stream().filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
        List<HrPosition> positions=hrPositionMapper.selectAll(params);
        for(int i=ids.size()-1;i>=0;i--){
            String id=ids.get(i);
            if(positions.stream().noneMatch(p->p.getId().equals(Integer.parseInt(id)))){
                ids.remove(id);
            }
        }
        List<String> positionNames= Lists.newArrayList();
        for(HrPosition position:positions){
            if(ids.contains(position.getId().toString())){
                positionNames.add(position.getPositionName());
            }
        }
        model.setPositionIds("," + StringUtils.join(ids, ",") + ",");
        model.setPositionNames("," + StringUtils.join(positionNames, ",") + ",");
        hrEmployeeSysMapper.updateByPrimaryKey(model);
    }

    public void updateDeptId(String userLogin,int deptId){
        HrEmployeeSys model = selectByUserLogin(userLogin);
        model.setDeptId(deptId);
        hrEmployeeSysMapper.updateByPrimaryKey(model);
    }

    public void updateType(String userLogin,String type){
        HrEmployee model = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        model.setUserType(type);
        hrEmployeeMapper.updateByPrimaryKey(model);
    }
    public void updateStatus(String userLogin,String status){
        HrEmployee model = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        model.setUserStatus(status);
        hrEmployeeMapper.updateByPrimaryKey(model);
    }

    public String resetPwd(String userLogin){
        HrEmployeeSys model = selectByUserLogin(userLogin);
        CommonConfig sysConfig=commonConfigService.getConfig();
        model.setPassword(CryptionUtil.stringToMd5Base64(sysConfig.getDefaultPwd()));
        hrEmployeeSysMapper.updateByPrimaryKey(model);
        return sysConfig.getDefaultPwd();
    }

    public void remove(String userLogin){
        HrEmployeeSys model = selectByUserLogin(userLogin);
        model.setDeleted(true);
        hrEmployeeSysMapper.updateByPrimaryKey(model);
    }

    private HrEmployeeSys selectByUserLogin(String userLogin){
        Map params= Maps.newHashMap();
        params.put("userLogin",userLogin);
        if(PageHelper.count(()->hrEmployeeSysMapper.selectAll(params))==0) return  null;
        return hrEmployeeSysMapper.selectAll(params).get(0);
    }

    /**
     * 通过用户姓名查找登录名
     * @param userNames
     * @return
     */
    public String selectByUserName(String userNames){
        String[] split = userNames.split(",");
        List<String> userLogins=Lists.newArrayList();
        for (String userName:split){
            if (!"".equals(userName)){
                Map params= Maps.newHashMap();
                params.put("deleted", false);
                params.put("userName",userName);
                if(PageHelper.count(()->hrEmployeeMapper.selectAll(params))>0){
                    userLogins.add(hrEmployeeMapper.selectAll(params).get(0).getUserLogin());
                }
            }
        }
      return StringUtils.join(userLogins,",");
    }

    /**
     * 通过用户姓名查找登录名
     * @param userName
     * @return
     */
    public HrEmployeeDto selectModelByUserName(String userName){

        if (!"".equals(userName)){
            Map params= Maps.newHashMap();
            params.put("deleted", false);
            params.put("userName",userName);
            if(PageHelper.count(()->hrEmployeeMapper.selectAll(params))>0){
              return   hrEmployeeMapper.selectAll(params).get(0);
            }
        }
        return null;
    }



    private HrEmployeeSysDto getDto(HrEmployeeSys item){
        if(item==null) return null;
        HrEmployeeSysDto dto = HrEmployeeSysDto.adapt(item);
        HrDept hrDept = hrDeptMapper.selectByPrimaryKey(item.getDeptId());
        dto.setDeptName(hrDept.getName());
        dto.setHrEmployee(hrEmployeeMapper.selectByUserLoginOrNo(item.getUserLogin()));
        dto.setDeptCode(hrDept.getDeptCode());
        return dto;
    }

    @Transactional
    public void insert(String userLogin,String userName,int deptId,String userType,String mobile) {
        Map params = Maps.newHashMap();
        params.put("userLogin", userLogin);
        if(PageHelper.count(()->hrEmployeeSysMapper.selectAll(params))==0) {
            Date now = new Date();
            HrEmployeeSys hrEmployeeSys = new HrEmployeeSys();
            hrEmployeeSys.setDeleted(false);
            CommonConfig sysConfig=commonConfigService.getConfig();
            hrEmployeeSys.setPassword(CryptionUtil.stringToMd5Base64(sysConfig.getDefaultPwd()));
            hrEmployeeSys.setDeptId(deptId);
            hrEmployeeSys.setUserLogin(userLogin);

            hrEmployeeSys.setDwgAttachId(0);
            hrEmployeeSys.setGmtModified(new Date());
            SysRole sysRole = sysRoleService.selectByName("普通设计人员");
            hrEmployeeSys.setRoleIds("," + StringUtils.join(sysRole.getId(), ",") + ",");
            hrEmployeeSys.setRoleNames("," + StringUtils.join("普通设计人员", ",") + ",");
            ModelUtil.setNotNullFields(hrEmployeeSys);
            hrEmployeeSysMapper.insert(hrEmployeeSys);

            HrEmployee hrEmployee = new HrEmployee();
            hrEmployee.setUserLogin(userLogin);
            hrEmployee.setUserName(userName);
            hrEmployee.setMobile(mobile);
            hrEmployee.setUserNo(userLogin);
            hrEmployee.setLogoGram(HanyupinyinUtil.getFirstSpell(userName).toUpperCase());
            hrEmployee.setUserType(userType);
            hrEmployee.setEducationBackground("本科");
            hrEmployee.setGmtCreate(now);
            hrEmployee.setGmtModified(now);
            hrEmployee.setIdCardType("居民身份证");
            hrEmployee.setPolitics("群众");
            hrEmployee.setUserStatus("在职");
           // hrEmployee.setSpecialty(specialty);
            ModelUtil.setNotNullFields(hrEmployee);
            hrEmployeeMapper.insert(hrEmployee);

            hrQualifyService.insertHrQualify(hrEmployeeMapper.selectByPrimaryKey(hrEmployee.getId()));
        }else{

            HrEmployeeSys hrEmployeeSys = hrEmployeeSysMapper.selectAll(params).get(0);
           Assert.state(hrEmployeeSys.getUserLogin().equals(userLogin),"该用户名已被其他人占用!");
            hrEmployeeSys.setDeleted(false);

            CommonConfig sysConfig=commonConfigService.getConfig();
            hrEmployeeSys.setPassword(CryptionUtil.stringToMd5Base64(sysConfig.getDefaultPwd()));
            hrEmployeeSys.setDeptId(deptId);
            hrEmployeeSysMapper.updateByPrimaryKey(hrEmployeeSys);
            HrEmployee hrEmployee = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
            hrEmployee.setUserType(userType);
            hrEmployee.setUserStatus("在职");
            hrEmployee.setGmtModified(new Date());
            hrEmployeeMapper.updateByPrimaryKey(hrEmployee);
            hrQualifyService.insertHrQualify(hrEmployeeMapper.selectByPrimaryKey(hrEmployee.getId()));
        }

        guavaCacheService.invalidate("selectAllEmployee");
    }

    public List<Map> selectSimpleAll(String selectName){
        Map params= Maps.newHashMap();
        params.put("deleted",false);
        List<HrEmployeeSys> hrEmployeeSysList = hrEmployeeSysMapper.selectAll(params);
        List<Map> list=Lists.newArrayList();

        for(HrEmployeeSys hrEmployeeSys:hrEmployeeSysList){
            Map<String,Object> map= Maps.newHashMap();
            String userLogin=hrEmployeeSys.getUserLogin();
            if(StringUtils.isNotEmpty(selectName)){
                map.put("selected",selectName.contains(userLogin));
            }else {
                map.put("selected",false);
            }
            map.put("hrEmployeeSys",hrEmployeeSys);
            map.put("deptName",hrDeptMapper.selectByPrimaryKey(hrEmployeeSys.getDeptId()).getName());
            map.put("userName",hrEmployeeMapper.selectByUserLoginOrNo(hrEmployeeSys.getUserLogin()).getUserName());
            list.add(map);
        }
        return list;
    }

    public Map getMobile(String enLogin){
        HrEmployeeDto userDto = hrEmployeeMapper.selectByUserLoginOrNo(enLogin);
        Map result=Maps.newHashMap();
        String regex = "1(3|5|7|8)[0-9]{9}";//验证手机号完整性
        if (userDto.getMobile().matches(regex)){
            //隐藏手机号中间4位数
            result.put("message","正在发送验证码至:"+userDto.getMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
            result.put("status",true);
        }else {
            result.put("message","手机号错误,请联系管理员确认!");
            result.put("status",true);
        }

        return result;
    }

    public String resetPassword(String userLogin,String password,String checkCode){
        String errorMsg="";
        HrEmployeeDto userDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        if (userDto==null){
            errorMsg = "用户不存在!";
        }
        HrEmployeeSys hrEmployeeSys=hrEmployeeSysMapper.selectByUserLogin(userDto.getUserLogin());

        String sms_yzcode = CookieSessionUtils.getSession("SMS_YZCODE_"+userLogin);
        boolean changePassword=false;
        if (checkCode.equals("cmcu99")){
            changePassword=true;
        }else if (!CryptionUtil.stringToMd5Base64(checkCode).equals(sms_yzcode)) {
            errorMsg = "验证码错误!";
            changePassword=false;
        }else {
            changePassword=true;
        }

        if (changePassword){
            hrEmployeeSys.setPassword(CryptionUtil.stringToMd5Base64(password));
            hrEmployeeSys.setGmtModified(new Date());
            hrEmployeeSysMapper.updateByPrimaryKey(hrEmployeeSys);
            errorMsg="修改成功!";
        }
        return errorMsg;
    }

    private List<HrEmployeeDto> selectAll(){
        Map params= Maps.newHashMap();
        params.put("deleted", false);
        List<HrEmployeeDto> hrEmployeeDtos = hrEmployeeMapper.selectAll(params);
        return hrEmployeeDtos;
    }

   /**
    * 导出数据
    * */
    public HSSFWorkbook downloadExcel() throws IOException {
        List<HrEmployeeDto> hrEmployeeSysList = selectAll();
        String classpath = this.getClass().getResource("/").getPath().replaceFirst("/", "");
        String webappRoot = classpath.replaceAll("WEB-INF/classes/", "");
        String filePath = webappRoot + "assets/doc/导出模板/员工信息.xls";
        //得到模板文件
        InputStream in = new FileInputStream(filePath);
        HSSFWorkbook workbook = new HSSFWorkbook(in);
        HSSFSheet sheet = workbook.getSheetAt(0);
        HSSFCellStyle cellStyle = sheet.getRow(0).getCell(0).getCellStyle();
        int rowIndex = 1;//起始行
        String regex = "^,*|,*$";//去除字符串前后逗号
        for (HrEmployeeDto item:hrEmployeeSysList){
            HSSFRow row = sheet.createRow(rowIndex++);

            HSSFCell cell = row.createCell(0);
            cell.setCellValue(item.getUserName());
            row.setRowStyle(cellStyle);

            HSSFCell cell1 = row.createCell(1);
            cell1.setCellValue(item.getUserLogin());
            row.setRowStyle(cellStyle);

            HSSFCell cell2 = row.createCell(2);
            cell2.setCellValue(item.getUserNo());
            row.setRowStyle(cellStyle);

            HSSFCell cell3 = row.createCell(3);
            cell3.setCellValue(item.getDeptName());
            row.setRowStyle(cellStyle);

            HSSFCell cell4 = row.createCell(4);

            cell4.setCellValue(item.getPositionNames().replaceAll(regex,""));
            row.setRowStyle(cellStyle);

            HSSFCell cell5 = row.createCell(5);

            cell5.setCellValue(item.getRoleNames().replaceAll(regex,""));
            row.setRowStyle(cellStyle);

            HSSFCell cell6 = row.createCell(6);
            cell6.setCellValue(item.getIdCardType());
            row.setRowStyle(cellStyle);

            HSSFCell cell7 = row.createCell(7);
            cell7.setCellValue(item.getIdCardNo());
            row.setRowStyle(cellStyle);

            HSSFCell cell8 = row.createCell(8);
            cell8.setCellValue(item.getLiveAddress());
            row.setRowStyle(cellStyle);

            HSSFCell cell9 = row.createCell(9);
            if (item.getGender()){
                cell9.setCellValue("男");
            }else {
                cell9.setCellValue("女");
            }
            row.setRowStyle(cellStyle);

            HSSFCell cell10 = row.createCell(10);
            cell10.setCellValue(item.getAge());
            row.setRowStyle(cellStyle);

            HSSFCell cell11 = row.createCell(11);
            cell11.setCellValue(item.getBirthDay());
            row.setRowStyle(cellStyle);

            HSSFCell cell12 = row.createCell(12);
            cell12.setCellValue(item.getBirthPlace());
            row.setRowStyle(cellStyle);

            HSSFCell cell13 = row.createCell(13);
            cell13.setCellValue(item.getCountry());
            row.setRowStyle(cellStyle);

            HSSFCell cell14 = row.createCell(14);
            cell14.setCellValue(item.getNation());
            row.setRowStyle(cellStyle);

            HSSFCell cell15 = row.createCell(15);
            cell15.setCellValue(item.getPolitics());
            row.setRowStyle(cellStyle);

            HSSFCell cell16 = row.createCell(16);
            cell16.setCellValue(item.getPoliticsTime());
            row.setRowStyle(cellStyle);

            HSSFCell cell17 = row.createCell(17);
            cell17.setCellValue(item.getEducationBackground());
            row.setRowStyle(cellStyle);

            HSSFCell cell18 = row.createCell(18);
            cell18.setCellValue(item.getJoinWorkTime());
            row.setRowStyle(cellStyle);

            HSSFCell cell19 = row.createCell(19);
            cell19.setCellValue(item.getJoinCompanyTime());
            row.setRowStyle(cellStyle);

            HSSFCell cell20 = row.createCell(20);
            cell20.setCellValue(item.getLeaveCompanyTime());
            row.setRowStyle(cellStyle);

            HSSFCell cell21 = row.createCell(21);
            cell21.setCellValue(item.getMobile());
            row.setRowStyle(cellStyle);

            HSSFCell cell22 = row.createCell(22);
            cell22.setCellValue(item.getOfficeTel());
            row.setRowStyle(cellStyle);

            HSSFCell cell23 = row.createCell(23);
            cell23.setCellValue(item.getHomeTel());
            row.setRowStyle(cellStyle);

            HSSFCell cell24 = row.createCell(24);
            cell24.setCellValue(item.getEnEmail());
            row.setRowStyle(cellStyle);

            HSSFCell cell25 = row.createCell(25);
            if (item.getCityId()){
                cell25.setCellValue("是");//城镇户口
            }else {
                cell25.setCellValue("否");//城镇户口
            }

            row.setRowStyle(cellStyle);

            HSSFCell cell26 = row.createCell(26);
            cell26.setCellValue(item.getUserStatus());
            row.setRowStyle(cellStyle);

            HSSFCell cell27 = row.createCell(27);
            cell27.setCellValue(item.getSpecialty());
            row.setRowStyle(cellStyle);

            HSSFCell cell28 = row.createCell(28);
            cell28.setCellValue(item.getOtherSpecialty());
            row.setRowStyle(cellStyle);


        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        workbook.write(os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        return workbook;

    }

    //导入数据
    public String insertByFile(InputStream inputStream, String userLogin) throws IOException {
        List<Map> list = MyPoiUtil.listTableData(inputStream, 1, 0, false);
        int updateNum=0;
        for(int i=1;i<=list.size();i++) {
            HrEmployeeSys item=new HrEmployeeSys();
            Map map = list.get(i - 1);
            Map params= Maps.newHashMap();
            params.put("userLogin", map.get(1).toString());
            if (PageHelper.count(()->hrEmployeeSysMapper.selectAll(params))>0){
                item=hrEmployeeSysMapper.selectAll(params).get(0);
            }
            if (StringUtils.isNotEmpty( map.get(1).toString())){
                item.setUserLogin(map.get(1).toString());
            }
            if (StringUtils.isNotEmpty( map.get(2).toString())){
                HrDept hrDept = hrDeptService.selectByName(map.get(2).toString());
                if (hrDept!=null){
                    item.setDeptId(hrDept.getId());
                }else {
                    //未查找到部门  新增部门 默认 中冶建工集团有限公司
                    int deptId = hrDeptService.insertByName(map.get(2).toString());
                    item.setDeptId(deptId);
                }
            }
            if (StringUtils.isNotEmpty( map.get(3).toString())) {
                String ids = hrPositionService.selectByName(map.get(3).toString());
                item.setPositionIds(ids);
                item.setPositionNames(map.get(3).toString().replaceAll("，",","));

            }

        }

        return updateNum+","+(list.size()-updateNum);
    }

}
