package com.cmcu.mcc.hr.service;

import com.cmcu.common.dao.CommonFormDataMapper;
import com.cmcu.common.dto.UserDto;
import com.cmcu.common.entity.CommonForm;
import com.cmcu.common.entity.CommonFormData;
import com.cmcu.common.service.CommonFormDataService;
import com.cmcu.common.service.CommonFormService;
import com.cmcu.common.service.IUserDataService;
import com.cmcu.common.util.JsonMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HrDetailService {

    @Resource
    CommonFormDataService commonFormDataService;

    @Resource
    CommonFormService commonFormService;

    @Resource
    IUserDataService userDataService;

    /**
     * 新增新数据
     * @param referType
     * @param owner
     * @param enLogin
     * @return
     */
    public CommonFormData getNewModel(String referType,String owner,String enLogin,boolean forceNoProcess) {
        UserDto userDto = userDataService.selectByEnLogin(enLogin);
        CommonForm commonForm = commonFormService.getModel(userDto.getTenetId(), referType, 0);
        Assert.state(commonForm != null, "未发现表单:" + referType);
        Map variables = Maps.newHashMap();
        if ("hrBasic".equalsIgnoreCase(referType)) {
            variables = getHrBasic(owner);
        }
        variables.putAll(commonFormDataService.getUserBasicVariable(enLogin));
        variables.remove("deptName");
        int referId =0;
        if(StringUtils.isNotEmpty(owner)) {
            UserDto ownerDto = userDataService.selectByEnLogin(owner);
            variables.put("userLogin", ownerDto.getEnLogin());
            variables.put("userLoginName", ownerDto.getCnName());
            referId =ownerDto.getId();
        }
        return commonFormDataService.getNewModel(referId, commonForm, variables, userDto,forceNoProcess);
    }

    /**
     * 得到用户基本信息
     * @param enLogin
     * @return
     */
    private Map getHrBasic(String enLogin){
        Map data=Maps.newLinkedHashMap();
        UserDto userDto = userDataService.selectByEnLogin(enLogin);
        if(userDto!=null){
            Map params = Maps.newHashMap();
            params.put("referType", "hrBasic");
            params.put("referId",userDto.getId());
            params.put("deleted", false);
            List<CommonFormData> oList = commonFormDataService.commonFormDataMapper.selectAll(params).stream()
                    .sorted(Comparator.comparing(CommonFormData::getId).reversed()).collect(Collectors.toList());
            if(oList.stream().anyMatch(p->StringUtils.isEmpty(p.getProcessInstanceId())||p.getProcessEnd())) {
                oList = oList.stream().filter(p -> StringUtils.isEmpty(p.getProcessInstanceId()) || p.getProcessEnd()).collect(Collectors.toList());
            }
            if(oList.size()>0) return JsonMapper.string2Map(oList.get(0).getFormData());
        }
        return data;
    }


    /**
     * 下载数据
     * @param owner 指定用户
     * @param referType 指定sheet
     * @return
     */
    public  Map listExcelData(String owner,String referType) {
        List<String> enLoginList = Lists.newArrayList();
        List<ExcelCell> sheetNames= listExcelSheets();
        UserDto ownerDto = userDataService.selectByEnLogin(owner);
        if (ownerDto != null && ownerDto.getId() > 0) {
            enLoginList.add(ownerDto.getEnLogin());
        } else {
            enLoginList = userDataService.selectAllUser("").stream().map(UserDto::getEnLogin).distinct().collect(Collectors.toList());
        }
        if (StringUtils.isNotEmpty(referType)) {
            sheetNames=sheetNames.stream().filter(p->p.getKey().equalsIgnoreCase(referType)).collect(Collectors.toList());
        }
        Map data=Maps.newLinkedHashMap();
        for(ExcelCell sheet:sheetNames) {
            data.put(sheet.getValue(), listExcelData(sheet.getKey(), enLoginList));
        }
        return data;
    }

    /**
     * 上传数据
     * @param data sheetName+list
     * @param enLogin 上传人
     */
    public void uploadExcelData(Map data, String enLogin) {
        for (Object key : data.keySet()) {
            String sheetName = key.toString();
            Assert.state(listExcelSheets().stream().anyMatch(p -> p.getValue().equalsIgnoreCase(sheetName)), sheetName + ",未匹配上的sheet名称!");
            ExcelCell sheetInfo = listExcelSheets().stream().filter(p -> p.getValue().equalsIgnoreCase(sheetName)).findFirst().get();
            List<Map> maps = (List<Map>) data.get(key);
            if (maps.size() > 0) {
                String referType = sheetInfo.getKey();
                List<ExcelCell> excelCells = listExcelCells(referType);
                if(excelCells.stream().anyMatch(ExcelCell::isKeyColumn)) {
                    List<ExcelCell> keyExcelCells=excelCells.stream().filter(ExcelCell::isKeyColumn).collect(Collectors.toList());
                    for (Map map : maps) {
                        int referId = 0;
                        UserDto ownerDto = getExcelUserDto(map);
                        if (ownerDto != null) {
                            referId = ownerDto.getId();
                        }
                        Map params = Maps.newHashMap();
                        params.put("referType", referType);
                        params.put("referId", referId);
                        params.put("deleted", false);
                        CommonFormData item = null;
                        List<CommonFormData> oList = commonFormDataService.commonFormDataMapper.selectAll(params);
                        if (oList.size()>0&&excelCells.stream().anyMatch(p -> p.keyColumn)) {
                            for (ExcelCell excelCell : keyExcelCells) {
                                String newValue = map.getOrDefault(excelCell.getValue(), "").toString();
                                if (excelCell.getValue().contains("工号")) newValue = getGoodEnLogin(newValue);
                                if (StringUtils.isNotEmpty(newValue)) {
                                    List<CommonFormData> tempList = Lists.newArrayList();
                                    for (CommonFormData formData : oList) {
                                        Map existData = JsonMapper.string2Map(formData.getFormData());
                                        if (existData.getOrDefault(excelCell.getKey(), "").toString().equalsIgnoreCase(newValue)) {
                                            tempList.add(formData);
                                        }
                                    }
                                    oList = tempList;
                                }
                            }
                        }
                        if (oList.size() > 0) {
                            item = oList.get(0);
                        } else {
                            item = getNewModel(referType, ownerDto==null?"":ownerDto.getEnLogin(), enLogin,true);
                        }
                        setCommonFormData(excelCells, item, map, ownerDto);
                        item.setGmtModified(new Date());
                        commonFormDataService.commonFormDataMapper.updateByPrimaryKey(item);
                    }
                }
            }
        }
    }



    private void setCommonFormData(List<ExcelCell> excelCells,CommonFormData item,Map map,UserDto currentUserDto){
        Map data = JsonMapper.string2Map(item.getFormData());
        for (Object k : map.keySet()) {
            if (StringUtils.isNotEmpty(k.toString())) {
                Optional<ExcelCell> optionalExcelCell = excelCells.stream().filter(p -> p.getValue().equalsIgnoreCase(k.toString())).findFirst();
                if (optionalExcelCell.isPresent()) {
                    if (k.toString().contains("工号")) {
                        //如果是工号、则用特殊处理过的
                        data.put(optionalExcelCell.get().getKey(), currentUserDto.getEnLogin());
                    } else {
                        data.put(optionalExcelCell.get().getKey(), map.get(k).toString());
                    }
                }
            }
        }
        item.setFormData(JsonMapper.obj2String(data));
    }

    private UserDto getExcelUserDto(Map data) {
        List<String> keys = Arrays.stream(data.keySet().toArray()).map(Object::toString).collect(Collectors.toList());
        if (keys.stream().anyMatch(p -> p.contains("工号"))) {
            String key = keys.stream().filter(p -> p.contains("工号")).findFirst().get();
            String currentEnLogin = getGoodEnLogin(data.get(key).toString());
            return userDataService.selectByEnLogin(currentEnLogin);
        }
        return null;
    }


    private String getGoodEnLogin(String currentEnLogin){
        if (StringUtils.isEmpty(currentEnLogin)) return null;
        if (currentEnLogin.startsWith("k") || currentEnLogin.length() == 5)
            currentEnLogin = currentEnLogin.substring(1);
        if (currentEnLogin.contains(".")) currentEnLogin = StringUtils.split(currentEnLogin, ".")[0];
        return currentEnLogin;
    }

    private List<Map> listExcelData(String referType,List<String> enLoginList) {
        List<Map> list = Lists.newArrayList();
        List<Map> oList = listCommonFormData(referType);
        if (referType.equalsIgnoreCase("hrBasic")) {
            if (oList.size() > 1) oList = oList.subList(0, 1);
        }
        List<ExcelCell> excelCells = listExcelCells(referType);
        for (String enLogin : enLoginList) {
            if (oList.stream().anyMatch(p -> enLogin.equalsIgnoreCase(p.getOrDefault("userLogin","").toString()))) {
                LinkedHashMap data = Maps.newLinkedHashMap();
                Map map = oList.stream().filter(p -> enLogin.equalsIgnoreCase(p.getOrDefault("userLogin","").toString())).findFirst().get();
                for (ExcelCell excelCell : excelCells) {
                    data.put(excelCell.getValue(), map.getOrDefault(excelCell.getKey(), "").toString());
                }
                list.add(data);
            }
        }
        return list;
    }

    private List<Map> listCommonFormData(String referType) {
        List<Map> list = Lists.newArrayList();
        Map params = Maps.newHashMap();
        params.put("referType", referType);
        params.put("deleted", false);
        List<CommonFormData> oList = commonFormDataService.commonFormDataMapper.selectAll(params).stream()
//                .filter(p->StringUtils.isEmpty(p.getProcessInstanceId())||p.getProcessEnd())
                .sorted(Comparator.comparing(CommonFormData::getId).reversed()).collect(Collectors.toList());
        oList.forEach(p -> list.add(JsonMapper.string2Map(p.getFormData())));
        return list;
    }

    private List<ExcelCell> listExcelSheets() {
        List<ExcelCell> excelNames = Lists.newArrayList();
        excelNames.add(new ExcelCell("hrBasic", "人员基本信息表"));
        excelNames.add(new ExcelCell("hrResume", "简历情况"));
        excelNames.add(new ExcelCell("hrEducation", "学历学位"));
        excelNames.add(new ExcelCell("hrTechnology", "专业技术职务"));
        excelNames.add(new ExcelCell("hrProfession", "职业技能资格"));
        excelNames.add(new ExcelCell("hrFamily", "家庭成员"));
        excelNames.add(new ExcelCell("hrExamine", "考核记录"));
        excelNames.add(new ExcelCell("hrPartyPosition", "党政职务"));
        excelNames.add(new ExcelCell("hrImportantGroup", "重要群体"));
        excelNames.add(new ExcelCell("hrPartTimeJob", "兼任职务"));
        excelNames.add(new ExcelCell("hrQualification", "职（执）业资格"));
        excelNames.add(new ExcelCell("hrAward", "奖励情况"));
        excelNames.add(new ExcelCell("hrHonor", "荣誉称号"));
        excelNames.add(new ExcelCell("hrPaper", "论文著作"));
        excelNames.add(new ExcelCell("hrPatent", "专利情况"));
        excelNames.add(new ExcelCell("hrPunish", "惩处记录"));
        excelNames.add(new ExcelCell("hrStandard", "标准规范"));
        excelNames.add(new ExcelCell("hrGoAbroad", "出国情况"));
        excelNames.add(new ExcelCell("hrTrain", "培训计划"));
        excelNames.add(new ExcelCell("hrTrainMember", "培训参与人员"));
        excelNames.add(new ExcelCell("hrCourse", "授课情况"));
        excelNames.add(new ExcelCell("hrProject", "重点项目"));
        return excelNames;
    }

    private List<ExcelCell> listExcelCells(String referType){
        try {
            Class clazz = HrDetailService.class;
            Method excelMethod = clazz.getDeclaredMethod("list" + StringUtils.replace(referType, "hr", "Hr") + "ExcelCell");
            List<ExcelCell> list = (List<ExcelCell>) excelMethod.invoke(new HrDetailService());
            return list;
        }catch (Exception ex){
            log.error("获取EXCEL配置数据失败,可能是方法名错误!",ex);
            return Lists.newArrayList();
        }
    }

    private List<ExcelCell> listHrBasicExcelCell() {
        List<ExcelCell> list = Lists.newArrayList();
        list.add(new ExcelCell("userLogin","*工号",true));
        list.add(new ExcelCell("userLoginName","*姓名"));
        list.add(new ExcelCell("classify","*人员分类（选择）"));
        list.add(new ExcelCell("formerName","曾用名"));
        list.add(new ExcelCell("idNumber","*身份证号"));
        list.add(new ExcelCell("gender","*性别（选择）"));
        list.add(new ExcelCell("birthDate","*出生日期"));
        list.add(new ExcelCell("ethnic","*民族（选择）"));
        list.add(new ExcelCell("politicsStatus","*政治面貌（选择）"));
        list.add(new ExcelCell("joinDate","加入时间"));
        list.add(new ExcelCell("enterWorkTime","*参加工作时间"));
        list.add(new ExcelCell("enterDeptDate","*进入单位时间"));
        list.add(new ExcelCell("enterType","*员工进入方式（选择）"));
        list.add(new ExcelCell("secretRelatedType","涉密类别（选择）"));
        list.add(new ExcelCell("military","是否军转干部（选择）"));
        list.add(new ExcelCell("secretRelated","*密级（选择）"));
        list.add(new ExcelCell("deptName","*所在部门"));
        list.add(new ExcelCell("station","*岗位"));
        list.add(new ExcelCell("practitionerType","*从业人员类型（选择）"));
        list.add(new ExcelCell("personnelIdentity","人员身份（选择）"));
        list.add(new ExcelCell("nationality","*国籍"));
        list.add(new ExcelCell("birthplace","*出生地"));
        list.add(new ExcelCell("housePlace","*籍贯"));
        list.add(new ExcelCell("residence","*户口所在地"));
        list.add(new ExcelCell("maritalStatus","*婚姻状况（选择）"));
        list.add(new ExcelCell("physicalCondition","*健康状况（选择）"));
        list.add(new ExcelCell("major","*熟悉专业有何特长"));
        list.add(new ExcelCell("phone","*手机号码"));
        list.add(new ExcelCell("domain","*分类领域（选择）"));
        list.add(new ExcelCell("stationSort","技能岗分类（选择）"));
        list.add(new ExcelCell("workType","工种类别（选择）"));
        list.add(new ExcelCell("sort","*人员排序标识"));
        list.add(new ExcelCell("statistics","是否统计标志（选择）"));
        list.add(new ExcelCell("remark","备注"));

        return list;
    }
    private List<ExcelCell> listHrResumeExcelCell(){
        List<ExcelCell> list = Lists.newArrayList();
        list.add(new ExcelCell("userLogin","*工号",true));
        list.add(new ExcelCell("startDate","*开始时间"));
        list.add(new ExcelCell("endDate","终止时间"));
        list.add(new ExcelCell("deptName","*所在单位及部门",true));
        list.add(new ExcelCell("performWork","*从事工作及职务"));
        list.add(new ExcelCell("proveMen","*证明人"));
        list.add(new ExcelCell("remark","简历备注（选择）"));
        return list;
    }
    private List<ExcelCell> listHrEducationExcelCell(){
        List<ExcelCell> list = Lists.newArrayList();
        list.add(new ExcelCell("userLogin","*工号",true));
        list.add(new ExcelCell("education","*学历（选择）",true));
        list.add(new ExcelCell("degree","学位（选择）"));
        list.add(new ExcelCell("educationDetail","*学历注释（选择）"));
        list.add(new ExcelCell("educationType","学历类别（选择）"));
        list.add(new ExcelCell("college","*毕业学校及单位",true));
        list.add(new ExcelCell("collegeType","学校类别（选择）"));
        list.add(new ExcelCell("department","所在院系"));
        list.add(new ExcelCell("major","所学专业",true));
        list.add(new ExcelCell("studyForm","学习形式（选择）"));
        list.add(new ExcelCell("enterDate","*入学时间"));
        list.add(new ExcelCell("endDate","*毕业时间"));
        list.add(new ExcelCell("studyLength","学制（年）"));
        list.add(new ExcelCell("proveMen","证明人"));
        list.add(new ExcelCell("remark","其他说明（选择）"));
        list.add(new ExcelCell("record","当前记录标识（选择）"));

        return list;
    }
    private List<ExcelCell> listHrTechnologyExcelCell(){
        List<ExcelCell> list = Lists.newArrayList();
        list.add(new ExcelCell("userLogin","*工号",true));
        list.add(new ExcelCell("technologyRank","*专业技术资格等级（选择）",true));
        list.add(new ExcelCell("technologySeq","专业技术资格序列（选择）",true));
        list.add(new ExcelCell("technologyJob","聘任专业技术职务（选择）"));
        list.add(new ExcelCell("acquireDate","取得资格时间"));
        list.add(new ExcelCell("acquireDateWay","取得资格途径（选择）",true));
        list.add(new ExcelCell("number","文号"));
        list.add(new ExcelCell("appointDate","聘任时间"));
        list.add(new ExcelCell("firedDate","解聘时间"));
        list.add(new ExcelCell("record","当前记录标识（选择）"));

        return list;
    }
    private List<ExcelCell> listHrProfessionExcelCell(){
        List<ExcelCell> list = Lists.newArrayList();
        list.add(new ExcelCell("userLogin","*工号",true));
        list.add(new ExcelCell("workName","工种名称",true));
        list.add(new ExcelCell("technologyRank","*技能资格等级（选择）"));
        list.add(new ExcelCell("technologyJob","聘任技能资格等级（选择）"));
        list.add(new ExcelCell("acquireDate","取得时间",true));
        list.add(new ExcelCell("appointDate","聘任时间"));
        list.add(new ExcelCell("firedDate","解聘时间"));
        list.add(new ExcelCell("record","*当前记录标识（选择）"));

        return list;
    }
    private List<ExcelCell> listHrFamilyExcelCell(){
        List<ExcelCell> list = Lists.newArrayList();
        list.add(new ExcelCell("userLogin","*工号",true));
        list.add(new ExcelCell("familyMemberName","*家属姓名",true));
        list.add(new ExcelCell("relation","*与本人关系（选择）",true));
        list.add(new ExcelCell("gender","*性别（选择）",true));
        list.add(new ExcelCell("politicsStatus","*政治面貌（选择）"));
        list.add(new ExcelCell("unitAndPosition","*单位及职务"));
        list.add(new ExcelCell("ethnic","*民族（选择）"));
        list.add(new ExcelCell("nativePlace","籍贯"));
        list.add(new ExcelCell("birthDate","出生日期"));
        list.add(new ExcelCell("education","学历（选择）"));
        list.add(new ExcelCell("major","所学专业"));
        list.add(new ExcelCell("university","毕业院校"));
        list.add(new ExcelCell("phone","联系电话"));
        return list;
    }
    private List<ExcelCell> listHrExamineExcelCell(){
        List<ExcelCell> list = Lists.newArrayList();
        list.add(new ExcelCell("userLogin","*工号",true));
        list.add(new ExcelCell("examineYear","*考核年份",true));
        list.add(new ExcelCell("examineResult","*考核结果"));
        list.add(new ExcelCell("remark","备注"));
        return list;
    }
    private List<ExcelCell> listHrPartyPositionExcelCell(){
        List<ExcelCell> list = Lists.newArrayList();
        list.add(new ExcelCell("userLogin","*工号",true));
        list.add(new ExcelCell("appointDate","*任职时间",true));
        list.add(new ExcelCell("firedDate","解聘、免职时间",true));
        list.add(new ExcelCell("dept","*任职单位",true));
        list.add(new ExcelCell("positionName","*职务名称"));
        list.add(new ExcelCell("positionRank","*职务能级（选择）"));
        list.add(new ExcelCell("privilege","*管理权限（选择）"));
        list.add(new ExcelCell("secondIdentify","二线标识"));
        list.add(new ExcelCell("branchWork","分管工作"));
        list.add(new ExcelCell("appointWay","任职方式（选择）"));
        list.add(new ExcelCell("appointNo","任职文号"));
        list.add(new ExcelCell("deposeReason","免职原因"));
        list.add(new ExcelCell("record","*当前记录标识（选择）"));
        return list;
    }
    private List<ExcelCell> listHrImportantGroupExcelCell(){
        List<ExcelCell> list = Lists.newArrayList();
        list.add(new ExcelCell("userLogin","*工号",true));
        list.add(new ExcelCell("importantKey","*重要群体标识（选择）",true));
        list.add(new ExcelCell("acquireDate","*取得时间",true));
        list.add(new ExcelCell("fireDate","解聘时间"));
        list.add(new ExcelCell("positionName","参政议政界别会议名称和职务"));
        list.add(new ExcelCell("positionRank","带头人界别"));
        list.add(new ExcelCell("remark","备注"));
        return list;
    }
    private List<ExcelCell> listHrPartTimeJobExcelCell(){
        List<ExcelCell> list = Lists.newArrayList();
        list.add(new ExcelCell("userLogin","*工号",true));
        list.add(new ExcelCell("deptName","*组织机构名称",true));
        list.add(new ExcelCell("positionName","*岗位名称",true));
        list.add(new ExcelCell("type","*类型（选择）",true));
        list.add(new ExcelCell("startDate","*开始日期"));
        list.add(new ExcelCell("end","是否结束（选择）"));
        list.add(new ExcelCell("endDate","结束日期"));
        return list;
    }
    private List<ExcelCell> listHrQualificationExcelCell(){
        List<ExcelCell> list = Lists.newArrayList();
        list.add(new ExcelCell("userLogin","*工号",true));
        list.add(new ExcelCell("acquireTime","*获得时间"));
        list.add(new ExcelCell("qualifyType","*职（执）业资格类别（选择）",true));
        list.add(new ExcelCell("qualifyName","*职（执）业资格名称",true));
        list.add(new ExcelCell("certificateNo","证书编号",true));
        list.add(new ExcelCell("certificateDept","发证单位"));
        list.add(new ExcelCell("remark","备注"));
        return list;
    }
    private List<ExcelCell> listHrAwardExcelCell(){
        List<ExcelCell> list = Lists.newArrayList();
        list.add(new ExcelCell("userLogin","*工号",true));
        list.add(new ExcelCell("acquireDate","*获得时间"));
        list.add(new ExcelCell("awardType","*奖项类别（选择）"));
        list.add(new ExcelCell("awardName","*具体奖项名称",true));
        list.add(new ExcelCell("awardDept","*授予机构",true));
        list.add(new ExcelCell("awardRank","*奖励级别（选择）",true));
        list.add(new ExcelCell("awardProjectName","获奖项目名称",true));
        list.add(new ExcelCell("certificateNo","*奖励证书号"));
        list.add(new ExcelCell("personalRank","*个人排名"));
        list.add(new ExcelCell("collaborate","主要合作者"));
        list.add(new ExcelCell("personalContribution","本人作用和主要贡献"));
        return list;
    }
    private List<ExcelCell> listHrPaperExcelCell(){
        List<ExcelCell> list = Lists.newArrayList();
        list.add(new ExcelCell("userLogin","*工号",true));
        list.add(new ExcelCell("paperType","*论著类别",true));
        list.add(new ExcelCell("paperDate","*论著发表时间"));
        list.add(new ExcelCell("paperName","*论著名称",true));
        list.add(new ExcelCell("deptName","*主办机构和刊物名称",true));
        list.add(new ExcelCell("level","隶属层次（选择）",true));
        list.add(new ExcelCell("write","编著角色（选择）"));
        list.add(new ExcelCell("authorSeq","作者排序"));
        list.add(new ExcelCell("collaborate","主要合作者"));
        list.add(new ExcelCell("personalContribution","本人作用和主要贡献"));
        list.add(new ExcelCell("magazine","是否被SCI、EI、SSCI、CSSCI收录"));
        return list;
    }
    private List<ExcelCell> listHrPunishExcelCell(){
        List<ExcelCell> list = Lists.newArrayList();
        list.add(new ExcelCell("userLogin","*工号",true));
        list.add(new ExcelCell("punishName","*处分名称",true));
        list.add(new ExcelCell("punishDate","*处分时间",true));
        list.add(new ExcelCell("punishNo","*处分文号",true));
        list.add(new ExcelCell("punishRank","*处分级别（选择）"));
        list.add(new ExcelCell("punishReason","处分原因"));
        list.add(new ExcelCell("approveDept","批准单位"));
        return list;
    }
    private List<ExcelCell> listHrGoAbroadExcelCell(){
        List<ExcelCell> list = Lists.newArrayList();
        list.add(new ExcelCell("userLogin","*工号",true));
        list.add(new ExcelCell("character","*出国（出境）性质（选择）",true));
        list.add(new ExcelCell("reason","出国原因",true));
        list.add(new ExcelCell("date","*出国（出境）时间",true));
        list.add(new ExcelCell("endDate","出国（出境）结束时间"));
        list.add(new ExcelCell("studyWay","出国培训学习方式"));
        list.add(new ExcelCell("studyMajor","学习专业"));
        list.add(new ExcelCell("Path","*出国路线"));
        list.add(new ExcelCell("inDept","所去单位"));
        list.add(new ExcelCell("outDept","派出单位"));
        return list;
    }
    private List<ExcelCell> listHrStandardExcelCell(){
        List<ExcelCell> list = Lists.newArrayList();
        list.add(new ExcelCell("userLogin","*工号",true));
        list.add(new ExcelCell("publishDate","*发表时间",true));
        list.add(new ExcelCell("standardName","*标准名称",true));
        list.add(new ExcelCell("standardRank","*标准级别（选择）",true));
        list.add(new ExcelCell("seq","*排名"));
        list.add(new ExcelCell("originator","*主要创作者"));
        list.add(new ExcelCell("mainContribution","本人主要贡献和作用"));
        return list;
    }
    private List<ExcelCell> listHrTrainExcelCell() {
        List<ExcelCell> list = Lists.newArrayList();
        list.add(new ExcelCell("trainKey","*主键",true));
        list.add(new ExcelCell("trainContent","培训内容"));
        list.add(new ExcelCell("trainNo","*培训编号"));
        list.add(new ExcelCell("trainName","*培训名称"));
        list.add(new ExcelCell("trainType","培训类型（选择）"));
        list.add(new ExcelCell("trainChannel","*培训渠道（选择）"));
        list.add(new ExcelCell("trainOffice","培训处室"));
        list.add(new ExcelCell("sponsor","主办单位"));
        list.add(new ExcelCell("startDate","*开始时间"));
        list.add(new ExcelCell("endDate","结束时间"));
        list.add(new ExcelCell("trainPlace","培训地点（选择）"));
        list.add(new ExcelCell("trainForm","学习形式（选择）"));
        list.add(new ExcelCell("trainInstitution","培训机构"));
        list.add(new ExcelCell("trainPeople","培训人数"));
        list.add(new ExcelCell("trainTime","培训学时"));
        list.add(new ExcelCell("trainPoint","培训学分"));
        list.add(new ExcelCell("remark","备注"));

        return list;
    }
    private List<ExcelCell> listHrCourseExcelCell(){
        List<ExcelCell> list = Lists.newArrayList();
        list.add(new ExcelCell("userLogin","*工号",true));
        list.add(new ExcelCell("courseDate","*授课时间",true));
        list.add(new ExcelCell("trainName","*授课所在培训班或会议名称",true));
        list.add(new ExcelCell("coursePlace","*授课地点",true));
        list.add(new ExcelCell("courseName","*授课名称"));
        list.add(new ExcelCell("studyPerson","*学习人员"));
        list.add(new ExcelCell("courseHour","*授课课时"));
        list.add(new ExcelCell("courseContent","*授课内容"));

        return list;
    }
    private List<ExcelCell> listHrTrainMemberExcelCell(){
        List<ExcelCell> list = Lists.newArrayList();
        list.add(new ExcelCell("userLogin","*工号",true));
        list.add(new ExcelCell("trainKey","*培训主键",true));
        list.add(new ExcelCell("trainResult","培训结果（选择）"));
        list.add(new ExcelCell("trainTime","学时"));
        list.add(new ExcelCell("trainPoint","取得学分",true));
        list.add(new ExcelCell("remark","备注"));
        return list;
    }
    private List<ExcelCell> listHrProjectExcelCell(){
        List<ExcelCell> list = Lists.newArrayList();
        list.add(new ExcelCell("userLogin","*工号",true));
        list.add(new ExcelCell("projectName","*项目名称",true));
        list.add(new ExcelCell("projectTime","*承担项目时间",true));
        list.add(new ExcelCell("undertakePosition","*担任职务（选择）"));
        list.add(new ExcelCell("definiteUndertakePosition","具体担任职务"));
        list.add(new ExcelCell("projectRank","*项目层级（选择）"));
        list.add(new ExcelCell("projectSource","*项目来源（选择）",true));
        list.add(new ExcelCell("projectCost","项目经费"));
        list.add(new ExcelCell("playRole","发挥作用"));
        return list;
    }
    private List<ExcelCell> listHrHonorExcelCell(){
        List<ExcelCell> list = Lists.newArrayList();
        list.add(new ExcelCell("userLogin","*工号",true));
        list.add(new ExcelCell("acquireDate","*获得时间",true));
        list.add(new ExcelCell("honorType","*荣誉类别（选择）"));
        list.add(new ExcelCell("awardName","*具体荣誉称号名称"));
        list.add(new ExcelCell("awardDept","*授予机构",true));
        list.add(new ExcelCell("awardRank","*荣誉等级（选择）",true));
        list.add(new ExcelCell("certificateNo","*证书编号",true));
        list.add(new ExcelCell("remark","其他说明"));
        return list;
    }
    private List<ExcelCell> listHrPatentExcelCell(){
        List<ExcelCell> list = Lists.newArrayList();
        list.add(new ExcelCell("userLogin","*工号",true));
        list.add(new ExcelCell("patentNo","*专利号",true));
        list.add(new ExcelCell("acquireDate","*获得专利日期",true));
        list.add(new ExcelCell("validDate","有效截止日期"));
        list.add(new ExcelCell("patentName","*专利名称"));
        list.add(new ExcelCell("patentType","*专利类别（选择）"));
        list.add(new ExcelCell("rankNo","个人排名"));
        list.add(new ExcelCell("togetherUser","主要合作者"));
        list.add(new ExcelCell("mainContribution","本人作用和主要贡献"));
        return list;
    }

    @Data
    class ExcelCell{
        private String key;

        private String value;

        private boolean keyColumn;

        public ExcelCell(String key, String value) {
            this.key = key;
            this.value = value;
            this.keyColumn=false;
        }

        public ExcelCell(String key, String value,boolean keyColumn) {
            this.key = key;
            this.value = value;
            this.keyColumn=keyColumn;
        }
    }
}
