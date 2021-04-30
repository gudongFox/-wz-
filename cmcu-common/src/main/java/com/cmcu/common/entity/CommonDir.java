package com.cmcu.common.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class CommonDir {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="业务id以便兼容各种类型\\n\\n比如协同分期1  co_step_1不能为空!")
    @Size(max=45, message="业务id以便兼容各种类型\\n\\n比如协同分期1  co_step_1长度不能超过45")
    private String businessKey;

    @NotNull(message="父节点id,如无则为0不能为空!")
    @Max(value=999999999, message="父节点id,如无则为0必须为数字")
    private Integer parentId;

    @NotNull(message="cnName不能为空!")
    @Size(max=45, message="cnName长度不能超过45")
    private String cnName;

    @NotNull(message="协同专业名称 默认空不能为空!")
    @Size(max=45, message="协同专业名称 默认空长度不能超过45")
    private String majorName;

    @NotNull(message="协同子项id 默认0不能为空!")
    @Max(value=999999999, message="协同子项id 默认0必须为数字")
    private Integer buildId;


    @NotNull(message="子项名称!")
    @Size(max=60, message="子项名称 默认空长度不能超过45")
    private String buildName;

    @NotNull(message="size不能为空!")
    @Max(value=999999999, message="size必须为数字")
    private Long size;

    @NotNull(message="sizeName不能为空!")
    @Size(max=145, message="sizeName长度不能超过145")
    private String sizeName;

    @NotNull(message="seq不能为空!")
    @Max(value=999999999, message="seq必须为数字")
    private Integer seq;

    @NotNull(message="是否可修改不能为空!")
    @Size(max=45, message="是否可修改长度不能超过45")
    private String editableTag;

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

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
    private String remark;
}