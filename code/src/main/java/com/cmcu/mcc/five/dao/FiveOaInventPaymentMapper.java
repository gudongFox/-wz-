package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaInventPayment;
import java.util.List;
import java.util.Map;

public interface FiveOaInventPaymentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaInventPayment record);

    FiveOaInventPayment selectByPrimaryKey(Integer id);

    List<FiveOaInventPayment> selectAll(Map params);

    int updateByPrimaryKey(FiveOaInventPayment record);
}