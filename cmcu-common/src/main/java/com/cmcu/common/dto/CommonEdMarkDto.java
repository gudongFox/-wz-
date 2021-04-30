package com.cmcu.common.dto;

import com.cmcu.common.entity.CommonEdMark;
import com.cmcu.common.entity.CommonEdMarkMore;
import lombok.Data;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Data
public class CommonEdMarkDto extends CommonEdMark {

    private String majorName;

    private String enLogin;

    private boolean editAble;

    private boolean answerAble;

    private boolean processAble;

    private List<CommonEdMarkMore> moreList;

    public static CommonEdMarkDto adapt(CommonEdMark item) {
        CommonEdMarkDto dto = new CommonEdMarkDto();
        BeanUtils.copyProperties(item, dto);
        dto.setEditAble(false);
        dto.setAnswerAble(false);
        dto.setMoreList(Lists.newArrayList());
        return dto;
    }

}
