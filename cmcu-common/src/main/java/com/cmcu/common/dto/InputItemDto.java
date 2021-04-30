package com.cmcu.common.dto;

import com.cmcu.common.entity.CommonFormDetail;
import lombok.Data;

import java.util.List;

@Data
public class InputItemDto {

   private CommonFormDetail commonFormDetail;

   private Object inputValue;

   private InputConfigDto inputConfigDto;

   /**
    * List<CommonCodeDto>
    */
   private Object dataSource;


}
