package com.cmcu.mcc.hr.dto;

import com.cmcu.mcc.hr.entity.HrEmployee;
import com.cmcu.mcc.hr.entity.HrEmployeeSys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class HrEmployeeSysDto extends HrEmployeeSys {

    private String deptName;

    private String deptCode;

    private HrEmployee hrEmployee;

    public static HrEmployeeSysDto adapt(HrEmployeeSys item){
        HrEmployeeSysDto dto=new HrEmployeeSysDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }

}
