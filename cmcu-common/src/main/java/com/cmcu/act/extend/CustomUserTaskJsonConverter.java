package com.cmcu.act.extend;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import org.activiti.bpmn.model.CustomProperty;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.UserTask;
import org.activiti.editor.language.json.converter.UserTaskJsonConverter;
import org.activiti.editor.language.json.converter.util.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomUserTaskJsonConverter extends UserTaskJsonConverter implements IPropertyConstants {




    //自定义属性
    private static List<String> propertyKeys= Lists.newArrayList(CC_ENABLE,CC_USER,TRANSFER_ENABLE,DELEGATE_ENABLE,BACK_ENABLE,ACCEPT_BACK);

    @Override
    protected FlowElement convertJsonToElement(JsonNode elementNode, JsonNode modelNode, Map<String, JsonNode> shapeMap) {
        FlowElement flowElement= super.convertJsonToElement(elementNode, modelNode, shapeMap);
        if(flowElement instanceof UserTask) {
            UserTask userTask=(UserTask)flowElement;
            List<CustomProperty> customProperties = new ArrayList<>();

            propertyKeys.forEach(key->{
                String value = getPropertyValueAsString(key, elementNode);
                if (StringUtils.isNotBlank(value)) {
                    CustomProperty property = this.createProperty(key, value);
                    customProperties.add(property);
                }
            });
            if (CollectionUtils.isNotEmpty(customProperties)) {
                userTask.setCustomProperties(customProperties);
            }
        }

        return flowElement;
    }


    /**
     * 创建自定义属性
     * @param propertyName
     * @param propertyValue
     * @return
     */
    private CustomProperty createProperty(String propertyName, String propertyValue) {
        CustomProperty customProperty = new CustomProperty();
        customProperty.setId(propertyName);
        customProperty.setName(propertyName);
        customProperty.setSimpleValue(propertyValue);
        return customProperty;
    }
}
