package com.cmcu.mcc.business.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveBusinessContractReviewDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="contractReviewId不能为空!")
    @Max(value=999999999, message="contractReviewId必须为数字")
    private Integer contractReviewId;

    @NotNull(message="评审内容不能为空!")
    @Size(max=145, message="评审内容长度不能超过145")
    private String reviewContent;

    @NotNull(message="评审结论不能为空!")
    @Size(max=45, message="评审结论长度不能超过45")
    private String reviewResult;

    @NotNull(message="seq不能为空!")
    @Size(max=45, message="seq长度不能超过45")
    private String seq;

    @NotNull(message="remark不能为空!")
    @Size(max=45, message="remark长度不能超过45")
    private String remark;

    @NotNull(message="creator不能为空!")
    @Size(max=45, message="creator长度不能超过45")
    private String creator;

    @NotNull(message="creatorName不能为空!")
    @Size(max=45, message="creatorName长度不能超过45")
    private String creatorName;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;
}