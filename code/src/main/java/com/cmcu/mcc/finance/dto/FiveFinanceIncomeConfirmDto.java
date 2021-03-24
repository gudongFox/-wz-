package com.cmcu.mcc.finance.dto;

import com.cmcu.mcc.finance.entity.FiveFinanceIncome;
import com.cmcu.mcc.finance.entity.FiveFinanceIncomeConfirm;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveFinanceIncomeConfirmDto extends FiveFinanceIncomeConfirm {
    private String processName;

    private String operateUserLogin;

    private String businessKey;

    private String finishTime;

    //收入已认领金额
    private String incomeConfirmMoney;
    //收入正在认领金额
    private String incomeConfirmMoneyIng;
    //收入总额
    private String incomeMoney;
    //收入剩余
    private String incomeMoneyRemain;

    //是否选择发票 或者 合同
    private boolean isChooseInvoiceOrContract;


    //合同号
    private String contractNo;
    //发票号
    private String invoiceNo;
    //合同名称
    private String contractName;

    private FiveFinanceIncome fiveFinanceIncome;



    public static FiveFinanceIncomeConfirmDto adapt(FiveFinanceIncomeConfirm item) {
        FiveFinanceIncomeConfirmDto dto=new FiveFinanceIncomeConfirmDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }


}
