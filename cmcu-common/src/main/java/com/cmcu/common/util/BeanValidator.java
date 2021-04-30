package com.cmcu.common.util;

import com.cmcu.common.exception.ParamException;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Roy
 * Time: 2018/11/26 15:48
 * To change this template use File | Settings | File Templates.
 */
public class BeanValidator {

    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    private static <T> Map<String, String> validate(T t, Class... groups) {
        Validator validator = validatorFactory.getValidator();
        Set validateResult = validator.validate(t, groups);
        if (validateResult.isEmpty()) {
            return Collections.emptyMap();
        } else {
            LinkedHashMap errors = Maps.newLinkedHashMap();
            Iterator iterator = validateResult.iterator();
            while (iterator.hasNext()) {
                ConstraintViolation violation = (ConstraintViolation) iterator.next();
                String errMessage = violation.getMessage();

//                if (!violation.getMessageTemplate().startsWith("{javax.")) {
//                    errMessage = violation.getMessage();
//                } else {
//                    String fieldName = "";
//                    try {
//                        fieldName = violation.getRootBean().getClass().getDeclaredField(violation.getPropertyPath().toString()).getAnnotation(FieldName.class).value();
//                    } catch (Exception e) {
//
//                    }
//                    errMessage = fieldName + violation.getMessage();
//                }

                errors.put(violation.getPropertyPath().toString(), errMessage);
            }
            return errors;
        }
    }

    private static Map<String, String> validateList(Collection<?> collection) {
        Preconditions.checkNotNull(collection);
        Iterator iterator = collection.iterator();
        Map errors;

        do {
            if (!iterator.hasNext()) {
                return Collections.emptyMap();
            }
            Object object = iterator.next();
            errors = validate(object, new Class[0]);
        } while (errors.isEmpty());

        return errors;
    }

    private static Map<String, String> getValidateObjectMap(Object first, Object... objects) {
        if (objects != null && objects.length > 0) {
            return validateList(Lists.asList(first, objects));
        } else {
            return validate(first, new Class[0]);
        }
    }


    public static String getValidateObjectMsg(Object first, Object... objects) {
        Map<String, String> result = getValidateObjectMap(first, objects);
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> kv : result.entrySet()) {
            if (StringUtils.isNotEmpty(kv.getValue())) {
                stringBuilder.append(kv.getValue());
                stringBuilder.append("\n\n");
            }
        }
        return stringBuilder.toString();
    }


    public static Map<String, String> validateObject(Object first, Object... objects) {
        if (objects != null && objects.length > 0) {
            return validateList(Lists.asList(first, objects));
        } else {
            return validate(first, new Class[0]);
        }
    }


    public static void check(Object param)  {
        Map<String, String> map = BeanValidator.validateObject(param);
        StringBuilder stringBuilder = new StringBuilder();
        if (map != null&&map.size()>0) {
            for (String value : map.values()) {
                stringBuilder.append(value + "<br>");
            }
            throw new ParamException(stringBuilder.toString());
        }
    }
}
