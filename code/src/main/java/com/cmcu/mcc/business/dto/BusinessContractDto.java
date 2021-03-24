package com.cmcu.mcc.business.dto;

import com.cmcu.mcc.business.entity.BusinessContract;
import com.cmcu.mcc.business.entity.BusinessContractDetail;
import com.cmcu.mcc.business.entity.BusinessCustomer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class BusinessContractDto extends BusinessContract {

    //取出项目名称（无特殊字符的）
    private String goodProjectName;

    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String finishTime;

    private BusinessContractDetail businessContractDetail;

    private String  customerCode;

    public static BusinessContractDto adapt(BusinessContract item) {
        BusinessContractDto dto=new BusinessContractDto();
        if(item!=null) {
            BeanUtils.copyProperties(item, dto);
        }
        return dto;
    }

}
