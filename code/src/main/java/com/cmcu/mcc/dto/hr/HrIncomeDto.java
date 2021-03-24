package com.cmcu.mcc.dto.hr;

import com.cmcu.mcc.entity.hr.HrIncome;
import org.springframework.beans.BeanUtils;

/**
 * Created with IntelliJ IDEA.
 * User: Roy
 * Time: 2019/3/6 11:40
 * To change this template use File | Settings | File Templates.
 */
public class HrIncomeDto  extends HrIncome {

    public static HrIncomeDto adapt(HrIncome item){
        HrIncomeDto dto=new HrIncomeDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }

    private String processInstanceName;


    public String getProcessInstanceName() {
        return processInstanceName;
    }

    public void setProcessInstanceName(String processInstanceName) {
        this.processInstanceName = processInstanceName;
    }
}
