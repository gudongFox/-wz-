package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaPrivilegeManagement;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaPrivilegeManagementDto extends FiveOaPrivilegeManagement {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaPrivilegeManagementDto adapt(FiveOaPrivilegeManagement item) {
        FiveOaPrivilegeManagementDto dto = new FiveOaPrivilegeManagementDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
