package com.cmcu.mcc.ed.entity;

import com.cmcu.common.annotation.FieldName;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class EdProjectTree {
    @FieldName("id")
    @NotNull
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @FieldName("projectId")
    @NotNull
    @Max(value=999999999, message="projectId必须为数字")
    private Integer projectId;

    @FieldName("foreignKey")
    @NotNull
    @Max(value=999999999, message="foreignKey必须为数字")
    private Integer foreignKey;

    @FieldName("parentId")
    @NotNull
    @Max(value=999999999, message="parentId必须为数字")
    private Integer parentId;

    @FieldName("nodeName")
    @NotEmpty
    @Size(max=45)
    private String nodeName;

    @FieldName("nodeUrl")
    @Size(max=45)
    private String nodeUrl;

    @FieldName("referType")
    @Size(max=450)
    private String referType;

    @FieldName("seq")
    @NotNull
    @Max(value=999999999, message="seq必须为数字")
    private Integer seq;

    @FieldName("icon")
    @Size(max=45)
    private String icon;

    @FieldName("isOpened")
    @NotNull
    private Boolean opened;

    @FieldName("isSelected")
    @NotNull
    private Boolean selected;

    @FieldName("是否可用")
    @NotNull
    private Boolean disabled;

    @FieldName("是否删除")
    @NotNull
    private Boolean deleted;

    @FieldName("gmtCreate")
    @NotNull
    private Date gmtCreate;

    @FieldName("gmtModified")
    @NotNull
    private Date gmtModified;

    @FieldName("remark")
    @Size(max=450)
    private String remark;
}