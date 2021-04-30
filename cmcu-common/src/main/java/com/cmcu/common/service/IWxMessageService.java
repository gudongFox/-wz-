package com.cmcu.common.service;


import com.cmcu.common.dto.FastUserDto;
import com.cmcu.common.dto.UserDto;

import java.util.List;

public interface IWxMessageService {

    void sendCommonFormDataMessage(String businessKey, List<FastUserDto> receivers);
}
