package com.cmcu.mcc.finance.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/finance")
public class FinanceController {
    @RequestMapping("/travelExpense")
    public ModelAndView travelExpense(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/travelExpense");
        return modelAndView;
    }
    @RequestMapping("/travelExpenseDetail")
    public ModelAndView travelExpenseDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/travelExpenseDetail");
        return modelAndView;
    }

    @RequestMapping("/reimburse")
    public ModelAndView reimburse(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/reimburse");
        return modelAndView;
    }

    @RequestMapping("/reimburseDetail")
    public ModelAndView reimburseDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/reimburseDetail");
        return modelAndView;
    }
    @RequestMapping("/income")
    public ModelAndView income(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/income");
        return modelAndView;
    }
    @RequestMapping("/incomeDetail")
    public ModelAndView incomeDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/incomeDetail");
        return modelAndView;
    }
    @RequestMapping("/incomeLibrary")
    public ModelAndView incomeLibrary(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/incomeLibrary");
        return modelAndView;
    }

    @RequestMapping("/invoice")
    public ModelAndView invoice(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/invoice");
        return modelAndView;
    }
    @RequestMapping("/invoiceDetail")
    public ModelAndView invoiceDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/invoiceDetail");
        return modelAndView;
    }
    @RequestMapping("/projectBudget")
    public ModelAndView projectBudget(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/projectBudget");
        return modelAndView;
    }
    @RequestMapping("/projectBudgetDetail")
    public ModelAndView projectBudgetDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/projectBudgetDetail");
        return modelAndView;
    }

    @RequestMapping("/deptFund")
    public ModelAndView deptFund(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/deptFund");
        return modelAndView;
    }
    @RequestMapping("/deptFundDetail")
    public ModelAndView deptFundDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/deptFundDetail");
        return modelAndView;
    }

    @RequestMapping("/deptBudget")
    public ModelAndView deptBudget(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/deptBudget");
        return modelAndView;
    }
    @RequestMapping("/deptBudgetDetail")
    public ModelAndView deptBudgetDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/deptBudgetDetail");
        return modelAndView;
    }
    @RequestMapping("/stampTax")
    public ModelAndView stampTax(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/stampTax");
        return modelAndView;
    }
    @RequestMapping("/stampTaxDetail")
    public ModelAndView stampTaxDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/stampTaxDetail");
        return modelAndView;
    }

    @RequestMapping("/invoiceCancel")
    public ModelAndView invoiceCancel(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/invoiceCancel");
        return modelAndView;
    }
    @RequestMapping("/invoiceCancelDetail")
    public ModelAndView invoiceCancelDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/invoiceCancelDetail");
        return modelAndView;
    }
    @RequestMapping("/invoiceCollection")
    public ModelAndView invoiceCollection(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/invoiceCollection");
        return modelAndView;
    }
    @RequestMapping("/invoiceCollectionDetail")
    public ModelAndView invoiceCollectionDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/invoiceCollectionDetail");
        return modelAndView;
    }

    @RequestMapping("/incomeConfirm")
    public ModelAndView incomeConfirm(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/incomeConfirm");
        return modelAndView;
    }
    @RequestMapping("/incomeConfirmDetail")
    public ModelAndView incomeConfirmDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/incomeConfirmDetail");
        return modelAndView;
    }

    @RequestMapping("/node")
    public ModelAndView node(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/node");
        return modelAndView;
    }
    @RequestMapping("/nodeDetail")
    public ModelAndView nodeDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/nodeDetail");
        return modelAndView;
    }
    @RequestMapping("/balance")
    public ModelAndView balance(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/balance");
        return modelAndView;
    }
    @RequestMapping("/balanceDetail")
    public ModelAndView balanceDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/balanceDetail");
        return modelAndView;
    }

    @RequestMapping("/outSupply")
    public ModelAndView outSupply(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/outSupply");
        return modelAndView;
    }
    @RequestMapping("/outSupplyDetail")
    public ModelAndView outSupplyDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/outSupplyDetail");
        return modelAndView;
    }

    @RequestMapping("/selfBank")
    public ModelAndView selfBank(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/selfBank");
        return modelAndView;
    }
    @RequestMapping("/selfBankDetail")
    public ModelAndView selfBankDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/selfBankDetail");
        return modelAndView;
    }
    @RequestMapping("/companyBank")
    public ModelAndView companyBank(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/companyBank");
        return modelAndView;
    }
    @RequestMapping("/companyBankDetail")
    public ModelAndView companyBankDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/companyBankDetail");
        return modelAndView;
    }

    @RequestMapping("/loan")
    public ModelAndView loan(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/loan");
        return modelAndView;
    }
    @RequestMapping("/loanDetail")
    public ModelAndView loanDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/loanDetail");
        return modelAndView;
    }
    @RequestMapping("/loanFunction")
    public ModelAndView loanFunction(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/loanFunction");
        return modelAndView;
    }
    @RequestMapping("/loanFunctionDetail")
    public ModelAndView loanFunctionDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/loanFunctionDetail");
        return modelAndView;
    }
    @RequestMapping("/loanRed")
    public ModelAndView loanRed(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/loanRed");
        return modelAndView;
    }
    @RequestMapping("/loanRedDetail")
    public ModelAndView loanRedDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/loanRedDetail");
        return modelAndView;
    }
    @RequestMapping("/loanBuild")
    public ModelAndView loanBuild(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/loanBuild");
        return modelAndView;
    }
    @RequestMapping("/loanBuildDetail")
    public ModelAndView loanBuildDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/loanBuildDetail");
        return modelAndView;
    }
    @RequestMapping("/refund")
    public ModelAndView refund(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/refund");
        return modelAndView;
    }
    @RequestMapping("/refundDetail")
    public ModelAndView refundDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/refundDetail");
        return modelAndView;
    }
    @RequestMapping("/transferAccounts")
    public ModelAndView transferAccounts(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/transferAccounts");
        return modelAndView;
    }
    @RequestMapping("/transferAccountsDetail")
    public ModelAndView transferAccountsDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/transferAccountsDetail");
        return modelAndView;
    }
    @RequestMapping("/receipt")
    public ModelAndView receipt(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/receipt");
        return modelAndView;
    }
    @RequestMapping("/receiptDetail")
    public ModelAndView receiptDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/receiptDetail");
        return modelAndView;
    }
    @RequestMapping("/backLetter")
    public ModelAndView backLetter(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/backLetter");
        return modelAndView;
    }
    @RequestMapping("/backLetterDetail")
    public ModelAndView backLetterDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/backLetterDetail");
        return modelAndView;
    }
    @RequestMapping("/entrance")
    public ModelAndView entrance(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/entrance");
        return modelAndView;
    }
   /* @RequestMapping("/transferAccountsRed")
    public ModelAndView transferAccountsRedRiver(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/transferAccounts");
        return modelAndView;
    }
    @RequestMapping("/transferAccountsRedDetail")
    public ModelAndView transferAccountsRedDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/transferAccountsDetail");
        return modelAndView;
    }
    @RequestMapping("/transferAccountsBuild")
    public ModelAndView transferAccountsBuild(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/transferAccounts");
        return modelAndView;
    }
    @RequestMapping("/transferAccountsBuildDetail")
    public ModelAndView transferAccountsBuildDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/transferAccountsBuild");
        return modelAndView;
    }*/
    @RequestMapping("/transferFee")
    public ModelAndView transferFee(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/transferFee");
        return modelAndView;
    }
    @RequestMapping("/transferFeeDetail")
    public ModelAndView transferFeeDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/finance/transferFeeDetail");
        return modelAndView;
    }
}
