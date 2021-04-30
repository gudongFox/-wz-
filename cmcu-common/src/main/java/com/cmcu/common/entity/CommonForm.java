package com.cmcu.common.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class CommonForm {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="客户编码不能为空!")
    @Size(max=45, message="客户编码长度不能超过45")
    private String tenetId;

    @NotNull(message="表单编码不能为空!")
    @Size(max=145, message="表单编码长度不能超过145")
    private String formKey;

    @NotNull(message="表单的图标不能为空!")
    @Size(max=45, message="表单的图标")
    private String formIcon;

    @NotNull(message="表单类别不能为空!")
    @Size(max=45, message="表单类别长度不能超过45")
    private String formCategory;

    @NotNull(message="表单说明不能为空!")
    @Size(max=450, message="表单说明长度不能超过450")
    private String formDesc;

    @NotNull(message="表单版本不能为空!")
    @Max(value=999999999, message="表单版本必须为数字")
    private Integer formVersion;

    @NotNull(message="其他内容配置不能为空!")
    @Size(max=850, message="其他内容配置长度不能超过850")
    private String otherTplConfig;

    @NotNull(message="是否发布不能为空!")
    private Boolean published;

    @NotNull(message="isDeleted不能为空!")
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
}