package com.cmcu.mcc.hr.dto;

import com.cmcu.mcc.hr.entity.HrEmployee;
import com.cmcu.mcc.hr.entity.HrQualify;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class HrEmployeeDto extends HrEmployee {

    private String operateUserLogin;

    private String wxId;

    private Integer deptId;

    private String deptName;

    private Boolean deleted;

    private String positionNames;

    private String positionIds;

    private String roleIds;

    private String roleNames;

    private String graduateCollege;

    private Boolean aclConfig;

    public HrQualify hrQualify;
    //选人控件使用
    private int parentId;

    public String icon;

    public static HrEmployeeDto adapt(HrEmployee item){
        HrEmployeeDto dto=new HrEmployeeDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }

}
