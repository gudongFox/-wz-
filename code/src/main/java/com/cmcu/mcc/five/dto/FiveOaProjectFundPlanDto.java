package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaProjectfundplan;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Setter
@Getter
public class FiveOaProjectFundPlanDto extends FiveOaProjectfundplan {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;
    //总包 总合同额
    private String totalContractPrice;
    //总包 总累计收款
    private String totalAccumulativePrice;
    //总包 总累计应收
    private String totalReceivablePrice;
    //总包 总尾款
    private String totalFinalPrice;

    //分包 总合同额
    private String totalContractPrice2;
    //分包 总累计收款
    private String totalAccumulativePrice2;
    //分包 总累计应收
    private String totalReceivablePrice2;
    //分包 总尾款
    private String totalFinalPrice2;

    //总计
    private String allProjectMoney;

    public static FiveOaProjectFundPlanDto adapt(FiveOaProjectfundplan item) {
        FiveOaProjectFundPlanDto dto = new FiveOaProjectFundPlanDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
