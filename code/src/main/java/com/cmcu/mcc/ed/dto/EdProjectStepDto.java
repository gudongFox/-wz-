package com.cmcu.mcc.ed.dto;

import com.cmcu.mcc.ed.entity.EdProjectStep;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Getter
@Setter
public class EdProjectStepDto extends EdProjectStep {

    public static EdProjectStepDto adapt(EdProjectStep item){
        EdProjectStepDto dto=new EdProjectStepDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }

    //待删
    private String edType;

    private String projectType;

    private String deptName;

    private String signTime;

    private String businessKey;

    private int buildCount;

    //当前用户
    private String userLogin;

    //是否CAD显示
    private boolean cadHide;

    private BigDecimal contractMoney;

    private String operateUserLogin;


}
