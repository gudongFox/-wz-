package com.cmcu.mcc.budget.dto;

import com.cmcu.mcc.budget.entity.FiveBudgetFee;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class SelectBudgetDto {
    //预算类型
    private String budgetType;

    //查询的预算
    private int budgetId;

    //当前查询部门
    private String budgetDeptName;

    //当前查询的预算年份
    private String budgetYear;

}
