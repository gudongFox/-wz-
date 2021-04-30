package com.cmcu.common.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class ModelUtil {

    public static void setNotNullFields(Object userBean) {

        List<Field> fieldList = Lists.newArrayList();
        fieldList.addAll(Arrays.stream(userBean.getClass().getDeclaredFields()).collect(Collectors.toList()));

        if (userBean.getClass().getSuperclass() != Object.class) {
            fieldList.addAll(Arrays.stream(userBean.getClass().getSuperclass().getDeclaredFields()).collect(Collectors.toList()));
        }

        userBean.getClass().getSuperclass().getDeclaredFields();
        for (Field field : fieldList) {
            // 取得注解的设置的属性值
            //NotNull notNull = field.getAnnotation(NotNull.class);
            if (!field.getName().equalsIgnoreCase("id")) {
                field.setAccessible(true);
                String setMethod = "set" + field.getName().substring(0, 1).toUpperCase()
                        + field.getName().substring(1);
                String getMethod = "get" + field.getName().substring(0, 1).toUpperCase()
                        + field.getName().substring(1);
                Class[] methodParam = null;
                Object[] params = null;
                try {
                    Object value = userBean.getClass().getMethod(getMethod, methodParam).invoke(userBean, params);
                    if (value != null) {
                        continue;
                    }
                } catch (Exception e) {

                }

                if (field.getType() == String.class) {
                    methodParam = new Class[]{String.class};
                    params = new Object[]{""};
                } else if (field.getType() == Integer.class) {
                    methodParam = new Class[]{Integer.class};
                    params = new Object[]{0};
                } else if (field.getType() == Boolean.class) {
                    methodParam = new Class[]{Boolean.class};
                    params = new Object[]{false};
                } else if (field.getType() == Double.class) {
                    methodParam = new Class[]{Double.class};
                    params = new Object[]{0};
                } else if (field.getType() == Long.class) {
                    methodParam = new Class[]{Long.class};
                    params = new Object[]{0l};
                }
                else if (field.getType() == BigDecimal.class) {
                    methodParam = new Class[]{BigDecimal.class};
                    params = new Object[]{BigDecimal.ZERO};
                }
                else if (field.getType() == Date.class) {
//                    methodParam = new Class[]{Date.class};
//                    params = new Object[]{new Date()};
                }
                try {
                    userBean.getClass().getMethod(setMethod, methodParam).invoke(userBean, params);
                } catch (Exception e) {

                }
            }
        }
    }


    public static List<Map> list2map(Object objectList) {

        List<Map> list = Lists.newArrayList();
        try {
            List<Object> oList = (List<Object>) objectList;
            if (oList.size() > 0) {
                Object source = oList.get(0);
                List<Field> fieldList = Lists.newArrayList();
                fieldList.addAll(Arrays.stream(source.getClass().getDeclaredFields()).collect(Collectors.toList()));

                Class parent = source.getClass().getSuperclass();
                while (parent != Object.class) {
                    fieldList.addAll(Arrays.stream(parent.getDeclaredFields()).collect(Collectors.toList()));
                    parent = parent.getSuperclass();
                }
                for (Object item : oList) {
                    Map map = Maps.newHashMap();
                    for (Field field : fieldList) {
                        PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(
                                source.getClass(), field.getName());
                        if (sourcePd != null && sourcePd.getReadMethod() != null) {
                            try {
                                Method readMethod = sourcePd.getReadMethod();
                                if (!Modifier.isPublic(readMethod.getDeclaringClass()
                                        .getModifiers())) {
                                    readMethod.setAccessible(true);
                                }
                                Object value = readMethod.invoke(item, new Object[0]);
                                if (value != null) {
                                    map.put(field.getName(), value);
                                }
                            } catch (Exception ex) {
                                //ex.printStackTrace();
                            }
                        }
                    }
                    list.add(map);
                }


                System.out.println(fieldList.size());

            }
        } catch (Exception ex) {
            ex.printStackTrace();

        }
        return list;
    }


}
