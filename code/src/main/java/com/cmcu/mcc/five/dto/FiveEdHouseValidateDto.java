package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveEdHouseValidate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Setter
@Getter
public class FiveEdHouseValidateDto extends FiveEdHouseValidate {


    private String processName;

    private String myTaskName;

    private String operateUserLogin;
    //文件数
    private int fileCount;
    //已处理意见数
    private  int markFinishCount;
    //意见总数
    private  int markCount;

    public static FiveEdHouseValidateDto adapt(FiveEdHouseValidate item){
        FiveEdHouseValidateDto dto=new FiveEdHouseValidateDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }
}
