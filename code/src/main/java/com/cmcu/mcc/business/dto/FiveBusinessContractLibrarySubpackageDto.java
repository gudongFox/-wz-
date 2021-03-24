package com.cmcu.mcc.business.dto;

import com.cmcu.mcc.business.entity.BusinessSubpackage;
import com.cmcu.mcc.business.entity.FiveBusinessContractLibrary;
import com.cmcu.mcc.business.entity.FiveBusinessContractLibrarySubpackage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Setter
@Getter
public class FiveBusinessContractLibrarySubpackageDto extends FiveBusinessContractLibrarySubpackage {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    private BusinessSubpackage businessSubpackage;
    //补充情况
    private List<BusinessSubpackage> reviews;

    public static FiveBusinessContractLibrarySubpackageDto adapt(FiveBusinessContractLibrarySubpackage item) {
        FiveBusinessContractLibrarySubpackageDto dto = new FiveBusinessContractLibrarySubpackageDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
