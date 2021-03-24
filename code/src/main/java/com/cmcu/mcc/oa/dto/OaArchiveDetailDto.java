package com.cmcu.mcc.oa.dto;

import com.cmcu.mcc.oa.entity.OaArchiveDetail;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

/**
 * @author hnz
 * @date 2019/10/8
 */
@Getter
@Setter
public class OaArchiveDetailDto extends OaArchiveDetail {

    private boolean selected;

    public static OaArchiveDetailDto adapt(OaArchiveDetail item) {
        OaArchiveDetailDto dto=new OaArchiveDetailDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }
}
