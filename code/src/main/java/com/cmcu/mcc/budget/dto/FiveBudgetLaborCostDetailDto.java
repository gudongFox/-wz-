package com.cmcu.mcc.budget.dto;

import com.cmcu.mcc.budget.entity.FiveBudgetCapitalOutDetail;
import com.cmcu.mcc.budget.entity.FiveBudgetLaborCostDetail;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveBudgetLaborCostDetailDto extends FiveBudgetLaborCostDetail {

    private String processName;

    private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    //父节点类型
    private String parentName;
    //创建时间
    private String gmtModifiedDto;
    //是否有子节点
    private Boolean isParent;
    //预算剩余金额
    private String remainMoney;



    public static FiveBudgetLaborCostDetailDto adapt(FiveBudgetLaborCostDetail item) {
        FiveBudgetLaborCostDetailDto dto = new FiveBudgetLaborCostDetailDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
