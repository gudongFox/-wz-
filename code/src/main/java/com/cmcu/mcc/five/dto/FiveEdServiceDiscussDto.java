package com.cmcu.mcc.five.dto;



import com.cmcu.mcc.five.entity.FiveEdServiceDiscuss;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveEdServiceDiscussDto extends FiveEdServiceDiscuss {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveEdServiceDiscussDto adapt(FiveEdServiceDiscuss item) {
        FiveEdServiceDiscussDto dto = new FiveEdServiceDiscussDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
