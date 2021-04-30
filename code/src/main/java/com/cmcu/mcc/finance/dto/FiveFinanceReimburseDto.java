package com.cmcu.mcc.finance.dto;

import com.cmcu.mcc.finance.entity.FiveFinanceReimburse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveFinanceReimburseDto extends FiveFinanceReimburse {

    private String processName;

    private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    //报销总金额
    private String totalApplyMoney;
    //财务确认总
    private String totalConfirmMoney;

    private String totalCount;

    //借还抵扣后金额
    private String deductionMoney;

    //借款剩余金额
    private String loanRemainMoney;

    //待还款金额
    private String shouldRefundMoney;
    //是否存在项目
    private Boolean isProject;
    //合同信息
    public String projectNo;

    public static FiveFinanceReimburseDto adapt(FiveFinanceReimburse item) {
        FiveFinanceReimburseDto dto = new FiveFinanceReimburseDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
    public FiveFinanceReimburseDto(){
        totalConfirmMoney ="0";
        totalApplyMoney ="0";
        deductionMoney = "0";
        shouldRefundMoney = "0";
        isProject = false;
    }
}
