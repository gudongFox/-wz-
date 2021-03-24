package com.cmcu.mcc.budget.dto;

import com.cmcu.mcc.budget.entity.FiveBudgetTurnInDetail;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveBudgetTurnInDetailDto extends FiveBudgetTurnInDetail {

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
    //是否为外部资产
    private boolean outAssert;
    //预算剩余金额
    private String remainMoney;


    public static FiveBudgetTurnInDetailDto adapt(FiveBudgetTurnInDetail item) {
        FiveBudgetTurnInDetailDto dto = new FiveBudgetTurnInDetailDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
