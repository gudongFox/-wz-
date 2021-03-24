package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.dao.FiveOaFileTransferMapper;
import com.cmcu.mcc.five.entity.FiveOaFileTransfer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;


import javax.annotation.Resource;
@Getter
@Setter
public class FiveOaFileTransferDto extends FiveOaFileTransfer {
    @Resource
    FiveOaFileTransferMapper fiveOaFileTransferMapper;

    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaFileTransferDto adapt(FiveOaFileTransfer item) {
        FiveOaFileTransferDto dto = new FiveOaFileTransferDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }

}
