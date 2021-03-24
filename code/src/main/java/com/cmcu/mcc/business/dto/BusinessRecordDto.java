package com.cmcu.mcc.business.dto;

import com.cmcu.mcc.business.entity.BusinessRecord;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
@Getter
@Setter
public class BusinessRecordDto extends BusinessRecord {
    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String finishTime;

    private Boolean isMain;
    //是否被合同评审使用
    private Boolean reviewUse;
    //是否被超前任务单使用
    private Boolean preUse;
    //纳税人识别号
    private String taxNo;




    public static BusinessRecordDto adapt(BusinessRecord item) {
        BusinessRecordDto dto=new BusinessRecordDto();
        BeanUtils.copyProperties(item,dto);
        dto.setPreUse(false);
        dto.setReviewUse(false);
        return dto;
    }


}
