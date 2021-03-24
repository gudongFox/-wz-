package com.cmcu.mcc.business.dto;

import com.cmcu.mcc.business.entity.BusinessSubpackage;
import com.cmcu.mcc.business.entity.BusinessSupplier;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class BusinessSubpackageDto extends BusinessSubpackage {

    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String attendUser;

    private String finishTime;

    private int headDeptId;

    //是否补充
    private Boolean isReplenish;

    public static BusinessSubpackageDto adapt(BusinessSubpackage item) {
        BusinessSubpackageDto dto=new BusinessSubpackageDto();
        if(item!=null) {
            BeanUtils.copyProperties(item, dto);
        }
        return dto;
    }
    BusinessSubpackageDto(){
        isReplenish = false;
    }

}
