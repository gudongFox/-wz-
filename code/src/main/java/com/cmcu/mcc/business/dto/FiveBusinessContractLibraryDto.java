package com.cmcu.mcc.business.dto;

import com.cmcu.mcc.business.entity.FiveBusinessContractLibrary;
import com.cmcu.mcc.business.entity.FiveBusinessContractReview;
import com.cmcu.mcc.finance.entity.FiveFinanceStampTax;
import com.cmcu.mcc.finance.entity.FiveFinanceStampTaxDetail;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Setter
@Getter
public class FiveBusinessContractLibraryDto extends FiveBusinessContractLibrary {
    private String processName;

    private String operateUserLogin;

    private String finishTime;
    //印花税
    private FiveFinanceStampTaxDetail fiveFinanceStampTaxDetail;
    //合同评审
    private FiveBusinessContractReview fiveBusinessContractReview;

    public static FiveBusinessContractLibraryDto adapt(FiveBusinessContractLibrary item) {
        FiveBusinessContractLibraryDto dto = new FiveBusinessContractLibraryDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
