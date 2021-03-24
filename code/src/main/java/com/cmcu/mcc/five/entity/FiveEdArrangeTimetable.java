package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveEdArrangeTimetable {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="arrangeId不能为空!")
    @Max(value=999999999, message="arrangeId必须为数字")
    private Integer arrangeId;

    @NotNull(message="发起专业不能为空!")
    @Size(max=145,message="发起专业长度不能大于145!")
    private String sendMajor;

    @NotNull(message="接收专业不能为空!")
    @Size(max=45,message="接收专业长度不能大于45!")
    private String receiptMajor;

    @NotNull(message="发起时间1不能为空!")
    @Size(max=45,message="发起时间1长度不能大于45!")
    private String receiptTime1;

    @NotNull(message="发起时间2不能为空!")
    @Size(max=45,message="发起时间2长度不能大于45!")
    private String receiptTime2;

    @NotNull(message="发起时间3不能为空!")
    @Size(max=45,message="发起时间3长度不能大于45!")
    private String receiptTime3;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;

    @NotNull(message="seq不能为空!")
    @Max(value=999999999, message="seq必须为数字")
    private Integer seq;
}