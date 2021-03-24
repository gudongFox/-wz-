package com.cmcu.mcc.five.entity;

import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveEdPlotApplyDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="plotApplyId不能为空!")
    @Max(value=999999999, message="plotApplyId必须为数字")
    private Integer plotApplyId;

    @NotNull(message="图纸大小不能为空!")
    @Size(max=45,message="图纸大小长度不能大于45!")
    private String frameSize;

    @NotNull(message="图纸张数不能为空!")
    @Size(max=45,message="图纸张数长度不能大于45!")
    private String frameNumber;

    @NotNull(message="图纸加长不能为空!")
    @Size(max=45,message="图纸加长长度不能大于45!")
    private String frameLengthen;

    @NotNull(message="图号不能为空!")
    @Size(max=45,message="图号长度不能大于45!")
    private String frameNo;

    @NotNull(message="remark不能为空!")
    @Size(max=45,message="remark长度不能大于45!")
    private String remark;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;
}