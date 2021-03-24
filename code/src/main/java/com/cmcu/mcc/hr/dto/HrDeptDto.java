package com.cmcu.mcc.hr.dto;

import com.cmcu.mcc.hr.entity.HrContract;
import com.cmcu.mcc.hr.entity.HrDept;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class HrDeptDto extends HrDept {
    private String icon;

    private String userLogin;

    private String parentDeptName;

    private String userDeptName;

    public static HrDeptDto adapt(HrDept item){
        HrDeptDto dto=new HrDeptDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }
}
