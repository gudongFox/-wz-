package com.cmcu.mcc.service;


import com.cmcu.common.dao.CommonFormDetailMapper;
import com.cmcu.common.dao.CommonFormMapper;
import com.cmcu.common.entity.CommonForm;
import com.cmcu.common.entity.CommonFormDetail;
import com.cmcu.common.util.ModelUtil;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

@Service
public class TempFormService {


    @Resource
    CommonFormMapper commonFormMapper;


    @Resource
    CommonFormDetailMapper commonFormDetailMapper;


    /**
     * 人员基本信息表
     */
    public void addHrBasic(){
        //1.增加common_form
        int formId=addCommonForm("人员基本信息表","hrBasic");

        //2.增加common_form_detail
        addCommonFormDetail(formId,"工号","userLogin");
        addCommonFormDetail(formId,"姓名","userLoginName");
        addCommonFormDetail(formId,"人员分类","classify");
        addCommonFormDetail(formId,"曾用名","formerName");
        addCommonFormDetail(formId,"身份证号","iDNumber");
        addCommonFormDetail(formId,"性别","gender");
        addCommonFormDetail(formId,"出生日期","birthDate");
        addCommonFormDetail(formId,"名族","ethnic");
        addCommonFormDetail(formId,"政治面貌","politicsStatus");
        addCommonFormDetail(formId,"加入时间","joinDate");
        addCommonFormDetail(formId,"参加工作时间","enterWorkTime");
        addCommonFormDetail(formId,"进入单位时间","enterDeptDate");
        addCommonFormDetail(formId,"员工进入方式","enterType");
        addCommonFormDetail(formId,"涉密类别","secretRelatedType");
        addCommonFormDetail(formId,"是否军转干部","military");
        addCommonFormDetail(formId,"密级","secretRelated");
        addCommonFormDetail(formId,"所在部门","deptName");
        addCommonFormDetail(formId,"岗位","station");
        addCommonFormDetail(formId,"从业人员类型","practitionerType");
        addCommonFormDetail(formId,"人员身份","personnelIdentity");
        addCommonFormDetail(formId,"国籍","nationality");
        addCommonFormDetail(formId,"出生地","birthplace");
        addCommonFormDetail(formId,"籍贯","housePlace");
        addCommonFormDetail(formId,"户口所在地","residence");
        addCommonFormDetail(formId,"婚姻状况","maritalStatus");
        addCommonFormDetail(formId,"健康状况","physicalCondition");
        addCommonFormDetail(formId,"熟悉专业有何特长","major");
        addCommonFormDetail(formId,"手机号码","phone");
        addCommonFormDetail(formId,"分类领域","domain");
        addCommonFormDetail(formId,"技能岗分类","stationSort ");
        addCommonFormDetail(formId,"工种类别","workType");
        addCommonFormDetail(formId,"人员排序标识","sort");
        addCommonFormDetail(formId,"是否统计标志","statistics");


        basicInsert(formId, "备注", "remark", 50, "{}",true);
        basicInsert(formId, "创建人", "creator", 110, "{}",true);
        basicInsert(formId, "创建人", "creatorName", 111, "{\"headStyle\":{\"width\":\"80px\"}}",true);
        basicInsert(formId, "创建时间", "gmtCreate", 112, "{\"headStyle\":{\"width\":\"160px\"}}",true);
        basicInsert(formId, "修改时间", "gmtModified", 113, "{\"headStyle\":{\"width\":\"160px\"}}",true);
    }

    /**
     * 简历情况
     */
    public void addHrResume(){
        //1.增加common_form
        int formId=addCommonForm("简历情况","hrResume");

        //2.增加common_form_detail
        addCommonFormDetail(formId,"工号","userLogin");
        addCommonFormDetail(formId,"开始时间","startDate");
        addCommonFormDetail(formId,"终止时间","endDate");
        addCommonFormDetail(formId,"所在单位及部门","dept");
        addCommonFormDetail(formId,"从事工作及职务","performWork");
        addCommonFormDetail(formId,"证明人","proveMen");

        basicInsert(formId, "备注", "remark", 50, "{}",true);
        basicInsert(formId, "创建人", "creator", 110, "{}",true);
        basicInsert(formId, "创建人", "creatorName", 111, "{\"headStyle\":{\"width\":\"80px\"}}",true);
        basicInsert(formId, "创建时间", "gmtCreate", 112, "{\"headStyle\":{\"width\":\"160px\"}}",true);
        basicInsert(formId, "修改时间", "gmtModified", 113, "{\"headStyle\":{\"width\":\"160px\"}}",true);
    }

    /**
     * 学历学位
     */
    public void addHrEducation(){
        //1.增加common_form
        int formId=addCommonForm("学历学位","hrEducation");

        //2.增加common_form_detail
        addCommonFormDetail(formId,"工号","userLogin");
        addCommonFormDetail(formId,"学历","education");
        addCommonFormDetail(formId,"学位","degree");
        addCommonFormDetail(formId,"学历注释","educationDetail");
        addCommonFormDetail(formId,"学历类别","educationType");
        addCommonFormDetail(formId,"毕业学校及单位","college");
        addCommonFormDetail(formId,"学校类别","collegeType");
        addCommonFormDetail(formId,"所在院系","department ");
        addCommonFormDetail(formId,"所学专业","major");
        addCommonFormDetail(formId,"学习形式","studyForm");
        addCommonFormDetail(formId,"入学时间","enterDate");
        addCommonFormDetail(formId,"毕业时间","endDate");
        addCommonFormDetail(formId,"学制","studyLength");
        addCommonFormDetail(formId,"证明人","proveMen");
        addCommonFormDetail(formId,"其他说明","remark");
        addCommonFormDetail(formId,"当前记录标识","record");

        basicInsert(formId, "备注", "remark", 50, "{}",true);
        basicInsert(formId, "创建人", "creator", 110, "{}",true);
        basicInsert(formId, "创建人", "creatorName", 111, "{\"headStyle\":{\"width\":\"80px\"}}",true);
        basicInsert(formId, "创建时间", "gmtCreate", 112, "{\"headStyle\":{\"width\":\"160px\"}}",true);
        basicInsert(formId, "修改时间", "gmtModified", 113, "{\"headStyle\":{\"width\":\"160px\"}}",true);
    }

    /**
     * 专业技术职务
     */
    public void addHrTechnology(){
        //1.增加common_form
        int formId=addCommonForm("专业技术职务","hrTechnology");

        //2.增加common_form_detail
        addCommonFormDetail(formId,"工号","userLogin");
        addCommonFormDetail(formId,"专业技术资格等级","technologyRank");
        addCommonFormDetail(formId,"专业技术资格序列","technologySeq");
        addCommonFormDetail(formId,"聘任专业技术职务","technologyJob");
        addCommonFormDetail(formId,"取得资格时间","acquireDate");
        addCommonFormDetail(formId,"取得资格途径","acquireDateWay");
        addCommonFormDetail(formId,"文号","number");
        addCommonFormDetail(formId,"聘任时间","appointDate ");
        addCommonFormDetail(formId,"解聘时间","firedDate");
        addCommonFormDetail(formId,"当前记录标识","record");

        basicInsert(formId, "备注", "remark", 50, "{}",true);
        basicInsert(formId, "创建人", "creator", 110, "{}",true);
        basicInsert(formId, "创建人", "creatorName", 111, "{\"headStyle\":{\"width\":\"80px\"}}",true);
        basicInsert(formId, "创建时间", "gmtCreate", 112, "{\"headStyle\":{\"width\":\"160px\"}}",true);
        basicInsert(formId, "修改时间", "gmtModified", 113, "{\"headStyle\":{\"width\":\"160px\"}}",true);
    }

    /**
     * 职业技能资格
     */
    public void addHrProfession(){
        //1.增加common_form
        int formId=addCommonForm("职业技能资格","hrProfession");

        //2.增加common_form_detail
        addCommonFormDetail(formId,"工号","userLogin");
        addCommonFormDetail(formId,"工种名称","workName");
        addCommonFormDetail(formId,"技能资格等级","technologyRank");
        addCommonFormDetail(formId,"聘任技能资格等级","technologyRank");
        addCommonFormDetail(formId,"聘任专业技术职务","technologyJob");
        addCommonFormDetail(formId,"取得时间","acquireDate");
        addCommonFormDetail(formId,"聘任时间","appointDate");
        addCommonFormDetail(formId,"解聘时间","firedDate");
        addCommonFormDetail(formId,"当前记录标识","record");

        basicInsert(formId, "备注", "remark", 50, "{}",true);
        basicInsert(formId, "创建人", "creator", 110, "{}",true);
        basicInsert(formId, "创建人", "creatorName", 111, "{\"headStyle\":{\"width\":\"80px\"}}",true);
        basicInsert(formId, "创建时间", "gmtCreate", 112, "{\"headStyle\":{\"width\":\"160px\"}}",true);
        basicInsert(formId, "修改时间", "gmtModified", 113, "{\"headStyle\":{\"width\":\"160px\"}}",true);
    }

    /**
     * 家庭成员
     */
    public void addHrFamily(){
        //1.增加common_form
        int formId=addCommonForm("家庭成员","hrFamily");

        //2.增加common_form_detail
        addCommonFormDetail(formId,"工号","userLogin");
        addCommonFormDetail(formId,"家属姓名","familyMemberName");
        addCommonFormDetail(formId,"与本人关系","relation");
        addCommonFormDetail(formId,"性别","gender");
        addCommonFormDetail(formId,"政治面貌","politicsStatus");
        addCommonFormDetail(formId,"单位及职务","unitAndPosition");
        addCommonFormDetail(formId,"名族","ethnic");
        addCommonFormDetail(formId,"籍贯","nativePlace ");
        addCommonFormDetail(formId,"出生日期","birthDate");
        addCommonFormDetail(formId,"学历","education");
        addCommonFormDetail(formId,"所学专业","major");
        addCommonFormDetail(formId,"毕业院校","university");
        addCommonFormDetail(formId,"联系电话","phone");

        basicInsert(formId, "备注", "remark", 50, "{}",true);
        basicInsert(formId, "创建人", "creator", 110, "{}",true);
        basicInsert(formId, "创建人", "creatorName", 111, "{\"headStyle\":{\"width\":\"80px\"}}",true);
        basicInsert(formId, "创建时间", "gmtCreate", 112, "{\"headStyle\":{\"width\":\"160px\"}}",true);
        basicInsert(formId, "修改时间", "gmtModified", 113, "{\"headStyle\":{\"width\":\"160px\"}}",true);
    }

    /**
     * 考核记录
     */
    public void addHrExamine(){
        //1.增加common_form
        int formId=addCommonForm("考核记录","hrExamine");

        //2.增加common_form_detail
        addCommonFormDetail(formId,"工号","userLogin");
        addCommonFormDetail(formId,"考核年份","examineYear");
        addCommonFormDetail(formId,"考核结果","examineResult");

        basicInsert(formId, "备注", "remark", 50, "{}",true);
        basicInsert(formId, "创建人", "creator", 110, "{}",true);
        basicInsert(formId, "创建人", "creatorName", 111, "{\"headStyle\":{\"width\":\"80px\"}}",true);
        basicInsert(formId, "创建时间", "gmtCreate", 112, "{\"headStyle\":{\"width\":\"160px\"}}",true);
        basicInsert(formId, "修改时间", "gmtModified", 113, "{\"headStyle\":{\"width\":\"160px\"}}",true);
    }

    /**
     * 党政职务
     */
    public void addHrPartyPosition(){
        //1.增加common_form
        int formId=addCommonForm("党政职务","hrPartyPosition");

        //2.增加common_form_detail
        addCommonFormDetail(formId,"工号","userLogin");
        addCommonFormDetail(formId,"任职时间","appointDate");
        addCommonFormDetail(formId,"解聘、免职时间","firedDate");
        addCommonFormDetail(formId,"任职单位","dept");
        addCommonFormDetail(formId,"职务名称","positionName");
        addCommonFormDetail(formId,"职务能级","positionRank");
        addCommonFormDetail(formId,"管理权限","privilege");
        addCommonFormDetail(formId,"二线标识","secondIdentify ");
        addCommonFormDetail(formId,"分管工作","branchWork");
        addCommonFormDetail(formId,"任职方式","appointWay");
        addCommonFormDetail(formId,"任职文号","appointNo");
        addCommonFormDetail(formId,"免职原因","deposeReason");
        addCommonFormDetail(formId,"当前记录标识","record");

        basicInsert(formId, "备注", "remark", 50, "{}",true);
        basicInsert(formId, "创建人", "creator", 110, "{}",true);
        basicInsert(formId, "创建人", "creatorName", 111, "{\"headStyle\":{\"width\":\"80px\"}}",true);
        basicInsert(formId, "创建时间", "gmtCreate", 112, "{\"headStyle\":{\"width\":\"160px\"}}",true);
        basicInsert(formId, "修改时间", "gmtModified", 113, "{\"headStyle\":{\"width\":\"160px\"}}",true);
    }

    /**
     * 重要群体
     */
    public void addHrImportantGroup(){
        //1.增加common_form
        int formId=addCommonForm("重要群体","hrImportantGroup");

        //2.增加common_form_detail
        addCommonFormDetail(formId,"工号","userLogin");
        addCommonFormDetail(formId,"重要群体标识","appointDate");
        addCommonFormDetail(formId,"获得时间","firedDate");
        addCommonFormDetail(formId,"解聘时间","dept");
        addCommonFormDetail(formId,"参政议政届别会议名称和职务","positionName");
        addCommonFormDetail(formId,"带头人届别","positionRank");

        basicInsert(formId, "备注", "remark", 50, "{}",true);
        basicInsert(formId, "创建人", "creator", 110, "{}",true);
        basicInsert(formId, "创建人", "creatorName", 111, "{\"headStyle\":{\"width\":\"80px\"}}",true);
        basicInsert(formId, "创建时间", "gmtCreate", 112, "{\"headStyle\":{\"width\":\"160px\"}}",true);
        basicInsert(formId, "修改时间", "gmtModified", 113, "{\"headStyle\":{\"width\":\"160px\"}}",true);
    }

    /**
     * 兼任职务
     */
    public void addHrPartTimeJob(){
        //1.增加common_form
        int formId=addCommonForm("兼任职务","hrPartTimeJob");

        //2.增加common_form_detail
        addCommonFormDetail(formId,"工号","userLogin");
        addCommonFormDetail(formId,"组织机构名称","deptName");
        addCommonFormDetail(formId,"岗位名称","positionName");
        addCommonFormDetail(formId,"类型","type");
        addCommonFormDetail(formId,"开始日期","startDate");
        addCommonFormDetail(formId,"是否结束","end");
        addCommonFormDetail(formId,"结束日期","endDate");

        basicInsert(formId, "备注", "remark", 50, "{}",true);
        basicInsert(formId, "创建人", "creator", 110, "{}",true);
        basicInsert(formId, "创建人", "creatorName", 111, "{\"headStyle\":{\"width\":\"80px\"}}",true);
        basicInsert(formId, "创建时间", "gmtCreate", 112, "{\"headStyle\":{\"width\":\"160px\"}}",true);
        basicInsert(formId, "修改时间", "gmtModified", 113, "{\"headStyle\":{\"width\":\"160px\"}}",true);
    }

    /**
     * 职业资格
     */
    public void addHrQualification(){
        //1.增加common_form
        int formId=addCommonForm("职业资格","hrQualification");

        //2.增加common_form_detail
        addCommonFormDetail(formId,"工号","userLogin");

        addCommonFormDetail(formId,"职（执）业资格类别（选择）","qualifyType");
        addCommonFormDetail(formId,"职（执）业资格名称","qualifyName");
        addCommonFormDetail(formId,"证书编号","certificateNo");
        addCommonFormDetail(formId,"发证单位","certificateDept");

        basicInsert(formId, "备注", "remark", 50, "{}",true);
        basicInsert(formId, "创建人", "creator", 110, "{}",true);
        basicInsert(formId, "创建人", "creatorName", 111, "{\"headStyle\":{\"width\":\"80px\"}}",true);
        basicInsert(formId, "创建时间", "gmtCreate", 112, "{\"headStyle\":{\"width\":\"160px\"}}",true);
        basicInsert(formId, "修改时间", "gmtModified", 113, "{\"headStyle\":{\"width\":\"160px\"}}",true);
    }

    /**
     * 奖励情况
     */
    public void addHrAward(){
        //1.增加common_form
        int formId=addCommonForm("奖励情况","hrAward");

        //2.增加common_form_detail
        addCommonFormDetail(formId,"工号","userLogin");

        addCommonFormDetail(formId,"获得时间","acquireDate");
        addCommonFormDetail(formId,"奖项类别","awardType");
        addCommonFormDetail(formId,"具体奖项名称","awardName");
        addCommonFormDetail(formId,"授予机构","awardDept");
        addCommonFormDetail(formId,"奖励级别","awardRank");
        addCommonFormDetail(formId,"获奖项目名称","awardName");
        addCommonFormDetail(formId,"获奖证书号","certificateNo");
        addCommonFormDetail(formId,"个人排名","personalRank");
        addCommonFormDetail(formId,"主要合作者","collaborate");
        addCommonFormDetail(formId,"本人作用和贡献","personalContribution");

        basicInsert(formId, "备注", "remark", 50, "{}",true);
        basicInsert(formId, "创建人", "creator", 110, "{}",true);
        basicInsert(formId, "创建人", "creatorName", 111, "{\"headStyle\":{\"width\":\"80px\"}}",true);
        basicInsert(formId, "创建时间", "gmtCreate", 112, "{\"headStyle\":{\"width\":\"160px\"}}",true);
        basicInsert(formId, "修改时间", "gmtModified", 113, "{\"headStyle\":{\"width\":\"160px\"}}",true);
    }

    /**
     * 荣誉称号
     */
    public void addHrHonor(){
        //1.增加common_form
        int formId=addCommonForm("荣誉称号","hrHonor");

        //2.增加common_form_detail
        addCommonFormDetail(formId,"工号","userLogin");

        addCommonFormDetail(formId,"获得时间","acquireDate");
        addCommonFormDetail(formId,"荣誉类别","honorType");
        addCommonFormDetail(formId,"具体奖项名称","awardName");
        addCommonFormDetail(formId,"授予机构","awardDept");
        addCommonFormDetail(formId,"荣誉等级","awardRank");
        addCommonFormDetail(formId,"证书编号","certificateNo");

        basicInsert(formId, "其他说明", "remark", 50, "{}",true);
        basicInsert(formId, "创建人", "creator", 110, "{}",true);
        basicInsert(formId, "创建人", "creatorName", 111, "{\"headStyle\":{\"width\":\"80px\"}}",true);
        basicInsert(formId, "创建时间", "gmtCreate", 112, "{\"headStyle\":{\"width\":\"160px\"}}",true);
        basicInsert(formId, "修改时间", "gmtModified", 113, "{\"headStyle\":{\"width\":\"160px\"}}",true);
    }

    /**
     * 论著类别
     */
    public void addHrPaper (){
        //1.增加common_form
        int formId=addCommonForm("论著类别","hrPaper");

        //2.增加common_form_detail
        addCommonFormDetail(formId,"工号","userLogin");

        addCommonFormDetail(formId,"论著类别","paperType");
        addCommonFormDetail(formId,"论著发表时间","paperDate");
        addCommonFormDetail(formId,"论著名称","paperName");
        addCommonFormDetail(formId,"主办机构和刊物名称","deptName");
        addCommonFormDetail(formId,"隶属层次","level");
        addCommonFormDetail(formId,"编著角色","write");
        addCommonFormDetail(formId,"作者排序","authorSeq");
        addCommonFormDetail(formId,"主要合作者","collaborate");
        addCommonFormDetail(formId,"本人作用和主要贡献","personalContribution");
        addCommonFormDetail(formId,"是否被SCI、EI、SSCI、CSSCI收录","magazine");

        basicInsert(formId, "其他说明", "remark", 50, "{}",true);
        basicInsert(formId, "创建人", "creator", 110, "{}",true);
        basicInsert(formId, "创建人", "creatorName", 111, "{\"headStyle\":{\"width\":\"80px\"}}",true);
        basicInsert(formId, "创建时间", "gmtCreate", 112, "{\"headStyle\":{\"width\":\"160px\"}}",true);
        basicInsert(formId, "修改时间", "gmtModified", 113, "{\"headStyle\":{\"width\":\"160px\"}}",true);
    }

    /**
     * 专利情况
     */
    public void addHrPatent(){
        //1.增加common_form
        int formId=addCommonForm("专利情况","hrPatent");

        //2.增加common_form_detail
        addCommonFormDetail(formId,"工号","userLogin");
        addCommonFormDetail(formId,"专利号","patentNo");
        addCommonFormDetail(formId,"获得专利日期","acquireDate");
        addCommonFormDetail(formId,"有效截至日期","validDate");
        addCommonFormDetail(formId,"专利名称","patentName");
        addCommonFormDetail(formId,"专利类别","patentType");
        addCommonFormDetail(formId,"个人排名","rankNo");
        addCommonFormDetail(formId,"主要合作者","mainTeamwork");
        addCommonFormDetail(formId,"本人作用及主要贡献","mainContribution");


        basicInsert(formId, "备注", "remark", 50, "{}",true);
        basicInsert(formId, "创建人", "creator", 110, "{}",true);
        basicInsert(formId, "创建人", "creatorName", 111, "{\"headStyle\":{\"width\":\"80px\"}}",true);
        basicInsert(formId, "创建时间", "gmtCreate", 112, "{\"headStyle\":{\"width\":\"160px\"}}",true);
        basicInsert(formId, "修改时间", "gmtModified", 113, "{\"headStyle\":{\"width\":\"160px\"}}",true);
    }

    /**
     * 惩处记录
     */
    public void addHrPunish(){
        //1.增加common_form
        int formId=addCommonForm("惩处记录","hrPunish");

        //2.增加common_form_detail
        addCommonFormDetail(formId,"工号","userLogin");
        addCommonFormDetail(formId,"处分名称","punishName");
        addCommonFormDetail(formId,"处分时间","punishDate");
        addCommonFormDetail(formId,"处分文号","punishNo");
        addCommonFormDetail(formId,"处分级别","punishRank");
        addCommonFormDetail(formId,"处分原因","punishReason");
        addCommonFormDetail(formId,"批准单位","approveDept");

        basicInsert(formId, "备注", "remark", 50, "{}",true);
        basicInsert(formId, "创建人", "creator", 110, "{}",true);
        basicInsert(formId, "创建人", "creatorName", 111, "{\"headStyle\":{\"width\":\"80px\"}}",true);
        basicInsert(formId, "创建时间", "gmtCreate", 112, "{\"headStyle\":{\"width\":\"160px\"}}",true);
        basicInsert(formId, "修改时间", "gmtModified", 113, "{\"headStyle\":{\"width\":\"160px\"}}",true);
    }

    /**
     * 出国情况
     */
    public void addHrGoAbroad(){
        //1.增加common_form
        int formId=addCommonForm("出国情况","hrGoAbroad");

        //2.增加common_form_detail
        addCommonFormDetail(formId,"工号","userLogin");
        addCommonFormDetail(formId,"出国（出境）性质（选择）","character");
        addCommonFormDetail(formId,"出国原因","reason");
        addCommonFormDetail(formId,"出国（出境）时间","date");
        addCommonFormDetail(formId,"出国（出境）结束时间","endDate");
        addCommonFormDetail(formId,"出国培训学习方式","studyWay");
        addCommonFormDetail(formId,"学习专业","studyMajor");
        addCommonFormDetail(formId,"出国路线","Path");
        addCommonFormDetail(formId,"所去单位","inDept");
        addCommonFormDetail(formId,"派出单位","outDept");

        basicInsert(formId, "备注", "remark", 50, "{}",true);
        basicInsert(formId, "创建人", "creator", 110, "{}",true);
        basicInsert(formId, "创建人", "creatorName", 111, "{\"headStyle\":{\"width\":\"80px\"}}",true);
        basicInsert(formId, "创建时间", "gmtCreate", 112, "{\"headStyle\":{\"width\":\"160px\"}}",true);
        basicInsert(formId, "修改时间", "gmtModified", 113, "{\"headStyle\":{\"width\":\"160px\"}}",true);
    }

    /**
     * 标准规范
     */
    public void addHrStandard(){
        //1.增加common_form
        int formId=addCommonForm("标准规范","hrStandard");

        //2.增加common_form_detail
        addCommonFormDetail(formId,"工号","userLogin");
        addCommonFormDetail(formId,"发表时间","publishDate");
        addCommonFormDetail(formId,"标准名称","standardName");
        addCommonFormDetail(formId,"标准级别","standardRank");
        addCommonFormDetail(formId,"排名","seq");
        addCommonFormDetail(formId,"主要创作者","originator");
        addCommonFormDetail(formId,"本人主要贡献和作用","mainContribution");

        basicInsert(formId, "备注", "remark", 50, "{}",true);
        basicInsert(formId, "创建人", "creator", 110, "{}",true);
        basicInsert(formId, "创建人", "creatorName", 111, "{\"headStyle\":{\"width\":\"80px\"}}",true);
        basicInsert(formId, "创建时间", "gmtCreate", 112, "{\"headStyle\":{\"width\":\"160px\"}}",true);
        basicInsert(formId, "修改时间", "gmtModified", 113, "{\"headStyle\":{\"width\":\"160px\"}}",true);
    }

    /**
     * 培训计划
     */
    public void addHrTrain(){
        //1.增加common_form
        int formId=addCommonForm("培训计划","hrTrain");

        //2.增加common_form_detail
        addCommonFormDetail(formId,"主键","primaryKey");
        addCommonFormDetail(formId,"工号","userLogin");
        addCommonFormDetail(formId,"培训内容","trainContent");
        addCommonFormDetail(formId,"培训编号","trainNo");
        addCommonFormDetail(formId,"培训名称","trainName");
        addCommonFormDetail(formId,"培训类型","trainType");
        addCommonFormDetail(formId,"培训渠道","trainChannel");
        addCommonFormDetail(formId,"培训处室","trainOffice");
        addCommonFormDetail(formId,"主办单位","sponsor");
        addCommonFormDetail(formId,"开始时间","startDate");
        addCommonFormDetail(formId,"结束时间","endDate");
        addCommonFormDetail(formId,"培训地点","trainPlace");
        addCommonFormDetail(formId,"学习形式","trainForm");
        addCommonFormDetail(formId,"培训机构","trainInstitution");
        addCommonFormDetail(formId,"培训人数","trainPeople");
        addCommonFormDetail(formId,"培训学时","trainTime");
        addCommonFormDetail(formId,"培训学分","trainPoint");

        basicInsert(formId, "备注", "remark", 50, "{}",true);
        basicInsert(formId, "创建人", "creator", 110, "{}",true);
        basicInsert(formId, "创建人", "creatorName", 111, "{\"headStyle\":{\"width\":\"80px\"}}",true);
        basicInsert(formId, "创建时间", "gmtCreate", 112, "{\"headStyle\":{\"width\":\"160px\"}}",true);
        basicInsert(formId, "修改时间", "gmtModified", 113, "{\"headStyle\":{\"width\":\"160px\"}}",true);
    }

    /**
     * 培训参与人员
     */
    public void addHrTrainMember(){
        //1.增加common_form
        int formId=addCommonForm("培训参与人员","hrTrainMember");

        //2.增加common_form_detail
        addCommonFormDetail(formId,"培训主键","trainKey");
        addCommonFormDetail(formId,"工号","userLogin");
        addCommonFormDetail(formId,"培训结果","trainResult");
        addCommonFormDetail(formId,"学时","trainTime");
        addCommonFormDetail(formId,"取得学分","trainPoint");

        basicInsert(formId, "备注", "remark", 50, "{}",true);
        basicInsert(formId, "创建人", "creator", 110, "{}",true);
        basicInsert(formId, "创建人", "creatorName", 111, "{\"headStyle\":{\"width\":\"80px\"}}",true);
        basicInsert(formId, "创建时间", "gmtCreate", 112, "{\"headStyle\":{\"width\":\"160px\"}}",true);
        basicInsert(formId, "修改时间", "gmtModified", 113, "{\"headStyle\":{\"width\":\"160px\"}}",true);
    }

    /**
     * 授课情况
     */
    public void addHrCourse(){
        //1.增加common_form
        int formId=addCommonForm("授课情况","hrCourse");

        //2.增加common_form_detail
        addCommonFormDetail(formId,"工号","userLogin");
        addCommonFormDetail(formId,"授课时间","courseDate");
        addCommonFormDetail(formId,"授课所在培训班或会议名称","trainName");
        addCommonFormDetail(formId,"授课地点","coursePlace");
        addCommonFormDetail(formId,"授课名称","courseName");
        addCommonFormDetail(formId,"学习人员","studyPerson");
        addCommonFormDetail(formId,"授课课时","courseHour");
        addCommonFormDetail(formId,"授课内容","courseContent");

        basicInsert(formId, "备注", "remark", 50, "{}",true);
        basicInsert(formId, "创建人", "creator", 110, "{}",true);
        basicInsert(formId, "创建人", "creatorName", 111, "{\"headStyle\":{\"width\":\"80px\"}}",true);
        basicInsert(formId, "创建时间", "gmtCreate", 112, "{\"headStyle\":{\"width\":\"160px\"}}",true);
        basicInsert(formId, "修改时间", "gmtModified", 113, "{\"headStyle\":{\"width\":\"160px\"}}",true);
    }

    /**
     * 重点项目
     */
    public void addHrProject(){
        //1.增加common_form
        int formId=addCommonForm("重点项目","hrProject");

        //2.增加common_form_detail
        addCommonFormDetail(formId,"工号","userLogin");
        addCommonFormDetail(formId,"项目名称","projectName");
        addCommonFormDetail(formId,"承担项目时间","projectTime");
        addCommonFormDetail(formId,"担任职务","undertakePosition");
        addCommonFormDetail(formId,"具体担任职务","definiteUndertakePosition");
        addCommonFormDetail(formId,"项目层级","projectRank");
        addCommonFormDetail(formId,"项目来源","projectSource");
        addCommonFormDetail(formId,"项目经费","projectCost");
        addCommonFormDetail(formId,"发挥作用","playRole");

        basicInsert(formId, "备注", "remark", 50, "{}",true);
        basicInsert(formId, "创建人", "creator", 110, "{}",true);
        basicInsert(formId, "创建人", "creatorName", 111, "{\"headStyle\":{\"width\":\"80px\"}}",true);
        basicInsert(formId, "创建时间", "gmtCreate", 112, "{\"headStyle\":{\"width\":\"160px\"}}",true);
        basicInsert(formId, "修改时间", "gmtModified", 113, "{\"headStyle\":{\"width\":\"160px\"}}",true);
    }

    private int addCommonForm(String formDesc,String formKey){

        Map params= Maps.newHashMap();
        params.put("formKey",formKey);
        if(PageHelper.count(()->commonFormMapper.selectAll(params))==0) {
            CommonForm commonForm = new CommonForm();
            commonForm.setTenetId("wuzhou");
            commonForm.setFormKey(formKey);
            commonForm.setFormIcon("icon-note");
            commonForm.setFormCategory("人力资源");
            commonForm.setFormDesc(formDesc);
            commonForm.setFormVersion(1);
            commonForm.setOtherTplConfig("{}");
            commonForm.setPublished(true);
            commonForm.setDeleted(false);
            commonForm.setCreator("mqh");
            commonForm.setCreatorName("马千惠");
            commonForm.setGmtModified(new Date());
            commonForm.setGmtCreate(new Date());
            ModelUtil.setNotNullFields(commonForm);
            commonFormMapper.insert(commonForm);
            return commonForm.getId();
        }else {
            return commonFormMapper.selectAll(params).get(0).getId();
        }

    }

    private void addCommonFormDetail(int formId,String inputText,String inputCode) {
        basicInsert(formId, inputText, inputCode, 0, "{}",false);
    }

    private void basicInsert(int formId, String inputText, String inputCode, int seq, String inputConfig,boolean listHidden) {
        Map params= Maps.newHashMap();
        params.put("formId",formId);
        if(seq==0) {
            seq = (int) PageHelper.count(() -> commonFormDetailMapper.selectAll(params)) + 1;
        }
        params.put("inputCode",inputCode);
        if(PageHelper.count(()->commonFormDetailMapper.selectAll(params))==0) {
            CommonFormDetail detail = new CommonFormDetail();
            detail.setFormId(formId);
            detail.setGroupName("基础信息");
            detail.setInputType("text");
            detail.setInputCode(inputCode);
            detail.setInputText(inputText);
            detail.setInputSize(6);
            detail.setInputConfig(inputConfig);
            detail.setDetailSeq(seq);
            detail.setListSeq(seq);
            detail.setEditable(true);
            detail.setDisabled(false);
            detail.setMaxLength(100);
            detail.setRequired(false);
            detail.setDetailHidden(inputCode.equalsIgnoreCase("creator"));
            detail.setVariable(false);
            detail.setDeleted(false);
            detail.setGmtCreate(new Date());
            detail.setGmtModified(new Date());
            detail.setListHidden(listHidden);
            detail.setDetailHidden(false);
            detail.setEditableTag("creator");
            detail.setMultiple(false);
            ModelUtil.setNotNullFields(detail);
            commonFormDetailMapper.insert(detail);
        }
    }

}
