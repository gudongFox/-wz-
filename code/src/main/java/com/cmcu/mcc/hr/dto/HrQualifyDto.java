package com.cmcu.mcc.hr.dto;

import com.cmcu.mcc.hr.entity.HrContract;
import com.cmcu.mcc.hr.entity.HrQualify;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class HrQualifyDto extends HrQualify {

    private String processName;

    private String operateUserLogin;

    private String finishTime;

    private String businessKey;

    private String isProjectCharge;

    private String isMajorCharge;

    private String isProofread;

    private String isAudit;

    private String isApprove;

    private String isChiefDesigner;

    private String isProChief;

    private String isDesign;

    private String isCompanyApprove;

    private String deptName;


    public static HrQualifyDto adapt(HrQualify item){
        HrQualifyDto dto=new HrQualifyDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }

}
