package com.cmcu.common.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class CommonFormDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="表单不能为空!")
    @Max(value=999999999, message="表单必须为数字")
    private Integer formId;

    @NotNull(message="组名不能为空!")
    @Size(max=45, message="组名长度不能超过45")
    private String groupName;

    @NotNull(message="input,select,checkbox不能为空!")
    @Size(max=45, message="input,select,checkbox长度不能超过45")
    private String inputType;

    @NotNull(message="inputCode不能为空!")
    @Size(max=45, message="inputCode长度不能超过45")
    private String inputCode;

    @NotNull(message="字段名称不能为空!")
    @Size(max=45, message="字段名称长度不能超过45")
    private String inputText;

    @NotNull(message="字段提示不能为空!")
    @Size(max=250, message="字段提示长度不能超过250")
    private String inputTip;

    @NotNull(message="所占尺寸,12为一行不能为空!")
    @Max(value=999999999, message="所占尺寸,12为一行必须为数字")
    private Integer inputSize;

    @NotNull(message="其他配置{multiple:false,data:}不能为空!")
    @Size(max=450, message="其他配置{multiple:false,data:}长度不能超过450")
    private String inputConfig;

    @NotNull(message="dataSource不能为空!")
    @Size(max=145, message="dataSource长度不能超过145")
    private String dataSource;

    @NotNull(message="可编辑阶段,默认为creator不能为空!")
    @Size(max=145, message="可编辑阶段,默认为creator长度不能超过145")
    private String editableTag;


    @NotNull(message="可显示阶段!")
    @Size(max=145, message="可显示阶段,默认为creator长度不能超过145")
    private String showTag;

    @NotNull(message="数据长度不能为空!")
    @Max(value=999999999, message="数据长度必须为数字")
    private Integer maxLength;

    @NotNull(message="列表隐藏不能为空!")
    private Boolean listHidden;

    @NotNull(message="可编辑不能为空!")
    private Boolean editable;

    @NotNull(message="屏蔽用户直接修改!")
    private Boolean disabled;


    @NotNull(message="是否多选(when input_type=select)不能为空!")
    private Boolean multiple;

    @NotNull(message="是否必填不能为空!")
    private Boolean required;

    @NotNull(message="是否显示字段不能为空!")
    private Boolean detailHidden;

    @NotNull(message="是否流程变量不能为空!")
    private Boolean variable;

    @NotNull(message="listSeq不能为空!")
    @Max(value=999999999, message="listSeq必须为数字")
    private Integer listSeq;

    @NotNull(message="排序不能为空!")
    @Max(value=999999999, message="排序必须为数字")
    private Integer detailSeq;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;

    @NotNull(message="remark不能为空!")
    @Size(max=145, message="remark长度不能超过145")
    private String remark;
}