package com.cmcu.mcc.five.dto;

import com.cmcu.mcc.five.entity.FiveOaInventPayment;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FiveOaInventPaymentDto extends FiveOaInventPayment {
    private String processName;

   private String operateUserLogin;

    private String attendUser;

    private String finishTime;

    public static FiveOaInventPaymentDto adapt(FiveOaInventPayment item) {
        FiveOaInventPaymentDto dto = new FiveOaInventPaymentDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}
