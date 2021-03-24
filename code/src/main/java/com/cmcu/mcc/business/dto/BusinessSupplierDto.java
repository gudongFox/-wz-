package com.cmcu.mcc.business.dto;

import com.cmcu.mcc.business.entity.BusinessContract;
import com.cmcu.mcc.business.entity.BusinessContractDetail;
import com.cmcu.mcc.business.entity.BusinessSupplier;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class BusinessSupplierDto extends BusinessSupplier {

    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String attendUser;

    private String finishTime;
    //合作项目数量
    private int cooperProjectNum;

    public static BusinessSupplierDto adapt(BusinessSupplier item) {
        BusinessSupplierDto dto=new BusinessSupplierDto();
        if(item!=null) {
            BeanUtils.copyProperties(item, dto);
        }
        return dto;
    }

}
