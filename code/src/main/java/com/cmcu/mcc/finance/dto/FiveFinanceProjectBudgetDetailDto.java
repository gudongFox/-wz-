package com.cmcu.mcc.finance.dto;

import com.cmcu.mcc.finance.entity.FiveFinanceProjectBudget;
import com.cmcu.mcc.finance.entity.FiveFinanceProjectBudgetDetail;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveFinanceProjectBudgetDetailDto extends FiveFinanceProjectBudgetDetail {
    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String finishTime;
    //父节点类型
    private String parentName;
    //创建时间
    private String gmtModifiedDto;
    //是否有子节点
    private Boolean isParent;



    public static FiveFinanceProjectBudgetDetailDto adapt(FiveFinanceProjectBudgetDetail item) {
        FiveFinanceProjectBudgetDetailDto dto=new FiveFinanceProjectBudgetDetailDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }


}
