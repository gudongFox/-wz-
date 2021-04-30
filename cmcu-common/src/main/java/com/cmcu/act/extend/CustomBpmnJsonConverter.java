package com.cmcu.act.extend;

import org.activiti.editor.language.json.converter.BaseBpmnJsonConverter;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;

import java.util.Map;

public class CustomBpmnJsonConverter extends BpmnJsonConverter {

    public static Map<String,Class<? extends BaseBpmnJsonConverter>> getConvertersToBpmnMap(){
        return convertersToBpmnMap;
    }
}
