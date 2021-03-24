package com.cmcu.mcc.five.dto;

import com.cmcu.common.dto.CommonFileDto;
import com.cmcu.mcc.five.entity.FiveOaDecisionMarkingDetail;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Getter
@Setter
public class FiveOaDecisionMarkingDetailDto extends FiveOaDecisionMarkingDetail {

    private List<CommonFileDto> fileList;

    public static FiveOaDecisionMarkingDetailDto adapt(FiveOaDecisionMarkingDetail item) {
        FiveOaDecisionMarkingDetailDto dto=new FiveOaDecisionMarkingDetailDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }
}
