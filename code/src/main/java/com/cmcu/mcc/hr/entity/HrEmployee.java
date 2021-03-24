package com.cmcu.mcc.hr.entity;

import com.cmcu.common.annotation.FieldName;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class HrEmployee {
    @FieldName("id")
    @NotNull
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @FieldName("userName")
    @NotNull
    @Size(max=45)
    private String userName;

    @FieldName("登录名")
    @NotNull
    @Size(max=45)
    private String userLogin;

    @FieldName("登录名")
    @NotNull
    @Size(max=45)
    private String logoGram;

    @FieldName("员工编号")
    @NotNull
    @Size(max=45)
    private String userNo;

    @FieldName("证件类型")
    @NotNull
    @Size(max=45)
    private String idCardType;

    @FieldName("证件编号")
    @NotNull
    @Size(max=45)
    private String idCardNo;

    @FieldName("gender")
    @NotNull
    private Boolean gender;

    @FieldName("age")
    @NotNull
    @Max(value=999999999, message="age必须为数字")
    private Integer age;

    @FieldName("birthDay")
    @NotNull
    @Size(max=45)
    private String birthDay;

    @FieldName("country")
    @NotNull
    @Size(max=45)
    private String country;

    @FieldName("nation")
    @NotNull
    @Size(max=45)
    private String nation;

    @FieldName("birthPlace")
    @NotNull
    @Size(max=45)
    private String birthPlace;

    @FieldName("政治面貌")
    @NotNull
    @Size(max=45)
    private String politics;

    @FieldName("加入时间")
    @NotNull
    @Size(max=45)
    private String politicsTime;

    @FieldName("参加工作时间")
    @NotNull
    @Size(max=45)
    private String joinWorkTime;

    @FieldName("入职时间")
    @NotNull
    @Size(max=45)
    private String joinCompanyTime;

    @FieldName("离职时间")
    @NotNull
    @Size(max=45)
    private String leaveCompanyTime;

    @FieldName("户籍地址")
    @NotNull
    @Size(max=145)
    private String idAddress;

    @FieldName("现住地址")
    @NotNull
    @Size(max=145)
    private String liveAddress;

    @FieldName("mobile")
    @NotNull
    @Size(max=45)
    private String mobile;

    @FieldName("officeTel")
    @NotNull
    @Size(max=45)
    private String officeTel;

    @FieldName("homeTel")
    @NotNull
    @Size(max=45)
    private String homeTel;

    @FieldName("enEmail")
    @NotNull
    @Size(max=45)
    private String enEmail;

    @FieldName("城镇户口")
    @NotNull
    private Boolean cityId;

    @FieldName("avatar")
    @NotNull
    @Size(max=145)
    private String avatar;

    @FieldName("signUrl")
    @NotNull
    @Size(max=145)
    private String signUrl;

    @FieldName("最高学历")
    @NotNull
    @Size(max=45)
    private String educationBackground;

    @FieldName("车牌号")
    @NotNull
    @Size(max=45)
    private String carNo;

    @FieldName("正式员工、外包员工")
    @NotNull
    @Size(max=45)
    private String userType;

    @FieldName("外单位退休人员、外单位内退人员、劳务派遣人员、其他")
    @NotNull
    @Size(max=45)
    private String userTypeDetail;

    @FieldName("在职、离职、退休、已逝")
    @NotNull
    @Size(max=45)
    private String userStatus;

    @FieldName("专业")
    @NotNull
    @Size(max=45)
    private String specialty;

    @FieldName("其他专业")
    @NotNull
    @Size(max=45)
    private String otherSpecialty;

    @FieldName("seq")
    @NotNull
    @Max(value=999999999, message="seq必须为数字")
    private Integer seq;

    @FieldName("gmtCreate")
    @NotNull
    private Date gmtCreate;

    @FieldName("gmtModified")
    @NotNull
    private Date gmtModified;

    @FieldName("remark")
    @NotNull
    @Size(max=450)
    private String remark;

    @FieldName("签名图片")
    @NotNull
    @Size(max=145)
    private String signPicUrl;

    @FieldName("signPicAttachId")
    @NotNull
    @Size(max=45)
    private String signPicAttachId;

    @FieldName("headAttachId")
    @NotNull
    @Size(max=45)
    private String headAttachId;

    @FieldName("signAttachId")
    @NotNull
    @Size(max=45)
    private String signAttachId;

    @FieldName("职称")
    @NotNull
    @Size(max=45)
    private String ranks;

    @FieldName("职称认定时间")
    @NotNull
    @Size(max=45)
    private String rankTime;
}