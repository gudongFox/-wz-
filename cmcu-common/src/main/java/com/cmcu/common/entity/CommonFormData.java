package com.cmcu.common.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class CommonFormData {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="tenetId不能为空!")
    @Size(max=45, message="tenetId长度不能超过45")
    private String tenetId;

    @NotNull(message="关联的类型不能为空!")
    @Size(max=45, message="关联的类型长度不能超过45")
    private String referType;

    @NotNull(message="关联id不能为空!")
    @Max(value=999999999, message="关联id必须为数字")
    private Integer referId;

    @NotNull(message="业务id不能为空!")
    @Size(max=45, message="业务id长度不能超过45")
    private String businessKey;

    @NotNull(message="formKey不能为空!")
    @Size(max=45, message="formKey长度不能超过45")
    private String formKey;

    @NotNull(message="formVersion不能为空!")
    @Max(value=999999999, message="formVersion必须为数字")
    private Integer formVersion;

    @NotNull(message="seq不能为空!")
    @Max(value=999999999, message="seq必须为数字")
    private Integer seq;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="creator不能为空!")
    @Size(max=45, message="creator长度不能超过45")
    private String creator;

    @NotNull(message="creatorName不能为空!")
    @Size(max=45, message="creatorName长度不能超过45")
    private String creatorName;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;

    @NotNull(message="流程id不能为空!")
    @Size(max=145, message="流程id长度不能超过145")
    private String processInstanceId;

    @NotNull(message="是否结束不能为空!")
    private Boolean processEnd;

    @NotNull(message="备注：不显示给客户看不能为空!")
    @Size(max=450, message="备注：不显示给客户看长度不能超过450")
    private String remark;

    @NotNull(message="formData不能为空!")
    @Size(max=65535, message="formData长度不能超过65535")
    private String formData;
}