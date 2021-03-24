package com.cmcu.mcc.five.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaCar {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @NotNull(message="所属部门不能为空!")
    @Size(max=145, message="所属部门长度不能超过145")
    private String deptName;

    @NotNull(message="车牌号不能为空!")
    @Size(max=45, message="车牌号长度不能超过45")
    private String carNo;

    @NotNull(message="轿车、SUV不能为空!")
    @Size(max=45, message="轿车、SUV长度不能超过45")
    private String carType;

    @NotNull(message="汽车排量不能为空!")
    @Size(max=45, message="汽车排量长度不能超过45")
    private String carCc;

    @NotNull(message="汽车价格不能为空!")
    @Max(value=999999999, message="汽车价格必须为数字")
    private BigDecimal carPrice;

    @NotNull(message="汽车品牌不能为空!")
    @Size(max=45, message="汽车品牌长度不能超过45")
    private String carBrand;

    @NotNull(message="车辆颜色不能为空!")
    @Size(max=45, message="车辆颜色长度不能超过45")
    private String carColor;

    @NotNull(message="状态：正常、维修、使用中不能为空!")
    @Size(max=45, message="状态：正常、维修、使用中长度不能超过45")
    private String carStatus;

    @NotNull(message="buyDate不能为空!")
    @Size(max=45, message="buyDate长度不能超过45")
    private String buyDate;

    @NotNull(message="是否删除不能为空!")
    private Boolean deleted;

    @NotNull(message="creator不能为空!")
    @Size(max=45, message="creator长度不能超过45")
    private String creator;

    @NotNull(message="creatorName不能为空!")
    @Size(max=45, message="creatorName长度不能超过45")
    private String creatorName;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
    private String remark;

    @NotNull(message="chargeMan不能为空!")
    @Size(max=45, message="chargeMan长度不能超过45")
    private String chargeMan;

    @NotNull(message="chargeManName不能为空!")
    @Size(max=45, message="chargeManName长度不能超过45")
    private String chargeManName;

    @NotNull(message="座位数不能为空!")
    @Size(max=45, message="座位数长度不能超过45")
    private String seating;

    @NotNull(message="使用性质不能为空!")
    @Size(max=45, message="使用性质长度不能超过45")
    private String useNature;

    @NotNull(message="油卡卡号（一车一号)不能为空!")
    @Size(max=45, message="油卡卡号（一车一号)长度不能超过45")
    private String oilCardNo;

    @NotNull(message="注册日期不能为空!")
    @Size(max=45, message="注册日期长度不能超过45")
    private String registerTime;

    @NotNull(message="发证类型不能为空!")
    @Size(max=45, message="发证类型长度不能超过45")
    private String certificateType;

    @NotNull(message="车辆识别码不能为空!")
    @Size(max=45, message="车辆识别码长度不能超过45")
    private String vehicleIdentificationCode;

    @NotNull(message="发动机号不能为空!")
    @Size(max=45, message="发动机号长度不能超过45")
    private String engineNumber;
}