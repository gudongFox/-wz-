package com.cmcu.mcc.budget.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/budget")
public class BudgetController {
    @RequestMapping("/fee")
    public ModelAndView feeBudget(){
        ModelAndView modelAndView=new ModelAndView("/five/budget/fee");
        return modelAndView;
    }
    @RequestMapping("/feeDetail")
    public ModelAndView feeBudgetDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/budget/feeDetail");
        return modelAndView;
    }
    @RequestMapping("/maintain")
    public ModelAndView maintain(){
        ModelAndView modelAndView=new ModelAndView("/five/budget/maintain");
        return modelAndView;
    }
    @RequestMapping("/maintainDetail")
    public ModelAndView maintainDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/budget/maintainDetail");
        return modelAndView;
    }
    @RequestMapping("/stock")
    public ModelAndView stock(){
        ModelAndView modelAndView=new ModelAndView("/five/budget/stock");
        return modelAndView;
    }
    @RequestMapping("/stockDetail")
    public ModelAndView stockDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/budget/stockDetail");
        return modelAndView;
    }
    @RequestMapping("/business")
    public ModelAndView business(){
        ModelAndView modelAndView=new ModelAndView("/five/budget/business");
        return modelAndView;
    }
    @RequestMapping("/businessDetail")
    public ModelAndView businessDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/budget/businessDetail");
        return modelAndView;
    }

    @RequestMapping("/independent")
    public ModelAndView independent(){
        ModelAndView modelAndView=new ModelAndView("/five/budget/independent");
        return modelAndView;
    }
    @RequestMapping("/independentDetail")
    public ModelAndView independentDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/budget/independentDetail");
        return modelAndView;
    }
    @RequestMapping("/production")
    public ModelAndView production(){
        ModelAndView modelAndView=new ModelAndView("/five/budget/production");
        return modelAndView;
    }
    @RequestMapping("/productionDetail")
    public ModelAndView productionDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/budget/productionDetail");
        return modelAndView;
    }

    @RequestMapping("/postExpense")
    public ModelAndView postExpense(){
        ModelAndView modelAndView=new ModelAndView("/five/budget/postExpense");
        return modelAndView;
    }
    @RequestMapping("/postExpenseDetail")
    public ModelAndView postExpenseDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/budget/postExpenseDetail");
        return modelAndView;
    }

    @RequestMapping("/scientificFundsOut")
    public ModelAndView scientificFundsOut(){
        ModelAndView modelAndView=new ModelAndView("/five/budget/scientificFundsOut");
        return modelAndView;
    }
    @RequestMapping("/scientificFundsOutDetail")
    public ModelAndView scientificFundsOutDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/budget/scientificFundsOutDetail");
        return modelAndView;
    }

    @RequestMapping("/scientificFundsIn")
    public ModelAndView scientificFundsIn(){
        ModelAndView modelAndView=new ModelAndView("/five/budget/scientificFundsIn");
        return modelAndView;
    }
    @RequestMapping("/scientificFundsInDetail")
    public ModelAndView scientificFundsInDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/budget/scientificFundsInDetail");
        return modelAndView;
    }

    @RequestMapping("/publicFunds")
    public ModelAndView publicFunds(){
        ModelAndView modelAndView=new ModelAndView("/five/budget/publicFunds");
        return modelAndView;
    }
    @RequestMapping("/publicFundsDetail")
    public ModelAndView publicFundsDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/budget/publicFundsDetail");
        return modelAndView;
    }

    @RequestMapping("/turnInRent")
    public ModelAndView turnInRent(){
        ModelAndView modelAndView=new ModelAndView("/five/budget/turnInRent");
        return modelAndView;
    }
    @RequestMapping("/turnInRentDetail")
    public ModelAndView turnInRentDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/budget/turnInRentDetail");
        return modelAndView;
    }

    @RequestMapping("/capitalOut")
    public ModelAndView capitalOut(){
        ModelAndView modelAndView=new ModelAndView("/five/budget/capitalOut");
        return modelAndView;
    }
    @RequestMapping("/capitalOutDetail")
    public ModelAndView capitalOutDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/budget/capitalOutDetail");
        return modelAndView;
    }
    @RequestMapping("/laborCost")
    public ModelAndView laborCost(){
        ModelAndView modelAndView=new ModelAndView("/five/budget/laborCost");
        return modelAndView;
    }
    @RequestMapping("/laborCostDetail")
    public ModelAndView laborCostDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/budget/laborCostDetail");
        return modelAndView;
    }
    @RequestMapping("/staffNumber")
    public ModelAndView staffNumber(){
        ModelAndView modelAndView=new ModelAndView("/five/budget/staffNumber");
        return modelAndView;
    }
    @RequestMapping("/staffNumberDetail")
    public ModelAndView staffNumberDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/budget/staffNumberDetail");
        return modelAndView;
    }


    @RequestMapping("/function")
    public ModelAndView function(){
        ModelAndView modelAndView=new ModelAndView("/five/budget/function");
        return modelAndView;
    }
    @RequestMapping("/functionDetail")
    public ModelAndView functionDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/budget/functionDetail");
        return modelAndView;
    }

    @RequestMapping("/turnIn")
    public ModelAndView turnIn(){
        ModelAndView modelAndView=new ModelAndView("/five/budget/turnIn");
        return modelAndView;
    }
    @RequestMapping("/turnInDetail")
    public ModelAndView turnInDetail(){
        ModelAndView modelAndView=new ModelAndView("/five/budget/turnInDetail");
        return modelAndView;
    }
}
