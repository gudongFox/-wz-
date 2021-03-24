package com.cmcu.mcc.finance.dto;

import com.cmcu.mcc.finance.entity.FiveFinanceDeptBudgetDetail;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveFinanceDeptBudgetDetailDto extends FiveFinanceDeptBudgetDetail {
    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String finishTime;
    //父节点类型
    private String parentName;
    //创建时间
    private String gmtModifiedDto;



    public static FiveFinanceDeptBudgetDetailDto adapt(FiveFinanceDeptBudgetDetail item) {
        FiveFinanceDeptBudgetDetailDto dto=new FiveFinanceDeptBudgetDetailDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }


}
