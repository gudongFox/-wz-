package com.cmcu.mybatis;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.DefaultCommentGenerator;



public class MyCommentGenerator extends DefaultCommentGenerator {
    public MyCommentGenerator() {
        super();
    }


    public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        topLevelClass.addImportedType("lombok.Data");
        topLevelClass.addImportedType("javax.validation.constraints.*");
        topLevelClass.addAnnotation("@Data");
    }

    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        //字段名
        String remarks = introspectedColumn.getRemarks();
        String filedType = field.getType().getShortName();
        if (remarks.isEmpty()) {
            remarks = field.getName();
        }

        //是否可以为空
        if (!introspectedColumn.isNullable()) {
            field.addAnnotation("@NotNull(message=\""+remarks+"不能为空!\")");
        }

        //字符串长度
        if (filedType.equals("String")) {
            field.addAnnotation("@Size(max=" + introspectedColumn.getLength() + ", message=\"" + remarks + "长度不能超过"+introspectedColumn.getLength()+"\")");
        }

        //数字
        if (filedType.equals("Integer") || filedType.equals("Long") || filedType.equals("Double") || filedType.equals("Float") || filedType.equals("BigDecimal")) {
            field.addAnnotation("@Max(value=999999999, message=\"" + remarks + "必须为数字\")");
        }


    }
}


