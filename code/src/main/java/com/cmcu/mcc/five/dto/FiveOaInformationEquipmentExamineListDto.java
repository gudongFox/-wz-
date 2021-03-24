package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaInformationEquipmentApply;
import com.cmcu.mcc.five.entity.FiveOaInformationEquipmentExamineList;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;


@Getter
@Setter
public class FiveOaInformationEquipmentExamineListDto extends FiveOaInformationEquipmentExamineList {

    private String processName;

    private String operateUserLogin;

    //审批信息化设备采购单
    private FiveOaInformationEquipmentApply equipmentApply;

    public static FiveOaInformationEquipmentExamineListDto adapt(FiveOaInformationEquipmentExamineList item) {
        FiveOaInformationEquipmentExamineListDto dto = new FiveOaInformationEquipmentExamineListDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
