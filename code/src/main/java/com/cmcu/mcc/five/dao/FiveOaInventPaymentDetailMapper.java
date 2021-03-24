package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaInventPaymentDetail;
import java.util.List;
import java.util.Map;

public interface FiveOaInventPaymentDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaInventPaymentDetail record);

    FiveOaInventPaymentDetail selectByPrimaryKey(Integer id);

    List<FiveOaInventPaymentDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveOaInventPaymentDetail record);
}