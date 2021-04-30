package com.cmcu.mybatis;

import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyGeneratorPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    //该方法在生成每一个属性的getter方法时候调用，如果我们不想生成getter，直接返回false即可；
    @Override
    public boolean modelGetterMethodGenerated(Method method,
                                              TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
                                              IntrospectedTable introspectedTable,
                                              ModelClassType modelClassType) {
        return false;
    }

    //该方法在生成每一个属性的setter方法时候调用，如果我们不想生成setter，直接返回false即可；
    @Override
    public boolean modelSetterMethodGenerated(Method method,
                                              TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
                                              IntrospectedTable introspectedTable,
                                              ModelClassType modelClassType) {
        return false;
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        String name = field.getName();
        if(name.startsWith("is") && field.getType().getShortName().equalsIgnoreCase("Boolean"))
        {
            name = name.replaceFirst("is","");
            name = (new StringBuilder()).append(Character.toLowerCase(name.charAt(0))).append(name.substring(1)).toString();
            field.setName(name);
        }
        return true;
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document,
                                           IntrospectedTable introspectedTable) {

        XmlElement sql = new XmlElement("sql");
        sql.addAttribute(new Attribute("id","Base_Where_List"));

        XmlElement where = new XmlElement("where");
        List<IntrospectedColumn> columns = introspectedTable.getAllColumns();
        for(IntrospectedColumn column : columns)
        {
            if(!column.isBLOBColumn()) {
                String colname = lineToHump(column.getActualColumnName());
                if(column.getActualColumnName().startsWith("is_"))
                {
                    colname = colname.replaceFirst("is","");
                    colname = (new StringBuilder()).append(Character.toLowerCase(colname.charAt(0))).append(colname.substring(1)).toString();
                }
                String cond = colname + " != null and isLikeSelect==null";
                if(column.isStringColumn()) {
                    cond += " and " + colname + " != ''";
                }
                XmlElement iff = new XmlElement("if");
                iff.addAttribute(new Attribute("test", cond));
                iff.addElement(new TextElement(" AND " + column.getActualColumnName() + " = #{"+colname+"}"));
                where.addElement(iff);
            }
        }
        sql.addElement(where);
        document.getRootElement().addElement(sql);

        //模糊查询
        XmlElement sql1 = new XmlElement("sql");
        sql1.addAttribute(new Attribute("id","Base_Where_List1"));
        XmlElement where1 = new XmlElement("where");
        List<IntrospectedColumn> columns1 = introspectedTable.getAllColumns();
        for(IntrospectedColumn column : columns1)
        {
            if(!column.isBLOBColumn()&&column.isStringColumn()) {
                String colname = lineToHump(column.getActualColumnName());
                if(column.getActualColumnName().startsWith("is_"))
                {
                    colname = colname.replaceFirst("is","");
                    colname = (new StringBuilder()).append(Character.toLowerCase(colname.charAt(0))).append(colname.substring(1)).toString();
                }
                String cond = colname + " != null and isLikeSelect!= null";
                cond += " and " + colname + " != '' ";

                XmlElement iff = new XmlElement("if");
                iff.addAttribute(new Attribute("test", cond));
                iff.addElement(new TextElement(" AND " + column.getActualColumnName() + " like  CONCAT('%',#{"+colname+"},'%')"));
                where1.addElement(iff);
            }
        }
        //添加deleted查询
        XmlElement iff = new XmlElement("if");
        iff.addAttribute(new Attribute("test", "deleted != null and isLikeSelect!=null"));
        iff.addElement(new TextElement(" AND is_deleted = #{deleted}"));
        where1.addElement(iff);
        //添加deptId查询
        XmlElement iff1 = new XmlElement("if");
        iff1.addAttribute(new Attribute("test", "deptId != null and isLikeSelect!=null"));
        iff1.addElement(new TextElement(" AND dept_id = #{deptId}"));
        where1.addElement(iff1);

        sql1.addElement(where1);
        document.getRootElement().addElement(sql1);



        return true;
    }

    @Override
    public boolean sqlMapSelectAllElementGenerated(XmlElement element,
                                                   IntrospectedTable introspectedTable) {

        XmlElement include = new XmlElement("include");
        include.addAttribute(new Attribute("refid", "Base_Where_List"));
        element.addElement(include);

        XmlElement include1 = new XmlElement("include");
        include1.addAttribute(new Attribute("refid", "Base_Where_List1"));
        element.addElement(include1);
        element.addElement(new TextElement(" order by id desc"));
        return true;
    }




    @Override
    public boolean sqlMapResultMapWithoutBLOBsElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable) {
        List<VisitableElement> list = element.getElements();
        for(VisitableElement element1 : list) {
            if(element1 instanceof XmlElement) {
                XmlElement xe = (XmlElement) element1;
                List<Attribute> attrs = xe.getAttributes();
                boolean isis = false;
                for(int i = attrs.size()-1;i>=0;i--) {
                    Attribute attr = attrs.get(attrs.size()-i-1);
                    if(attr.getName().equals("column") && attr.getValue().startsWith("is_")) {
                        isis = true;
                    }
                    if(isis && attr.getName().equals("property")) {
                        String name = attr.getValue();
                        name = name.replaceFirst("is","");
                        name = (new StringBuilder()).append(Character.toLowerCase(name.charAt(0))).append(name.substring(1)).toString();
                        attrs.remove(attr);
                        xe.addAttribute(new Attribute("property", name));
                        isis = false;
                    }
                }
            }
        }
        return true;
    }


    @Override
    public boolean sqlMapInsertElementGenerated(XmlElement element,
                                                IntrospectedTable introspectedTable) {
        dealIs(element);
        return true;
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable) {
        dealIs(element);
        return true;
    }

    private void dealIs(XmlElement element) {
        List<VisitableElement> els = element.getElements();
        for(int i=els.size()-1;i>=0;i--) {
            VisitableElement el = els.get(i);
            if(el instanceof TextElement) {
                TextElement te = (TextElement) el;
                String content = te.getContent();

                boolean r = false;
                while(content.indexOf("#{is")>-1) {
                    r = true;
                    int index = content.indexOf("#{is");
                    String c = String.valueOf(content.charAt(index+4)).toLowerCase();
                    char[] cs = content.toCharArray();
                    cs[index+4] = c.charAt(0);

                    content = String.valueOf(cs);
                    content = content.replaceFirst("\\#\\{is","#{");
                }

                if(r) {
                    els.remove(el);
                    els.add(i,new TextElement(content));
                }
            }
        }
    }

    @Override
    public boolean sqlMapGenerated(GeneratedXmlFile sqlMap, IntrospectedTable introspectedTable) {
        //如果想覆盖xml则设为false
        sqlMap.setMergeable(false);
        return super.sqlMapGenerated(sqlMap, introspectedTable);
    }

    @Override
    public boolean clientSelectAllMethodGenerated(Method method,
                                                  Interface interfaze, IntrospectedTable introspectedTable) {
        interfaze.addImportedType(new FullyQualifiedJavaType("java.util.Map"));
        method.addParameter(new Parameter(new FullyQualifiedJavaType("Map"), "params"));
        return true;
    }
    //转驼峰
    private String lineToHump(String str) {
        Pattern linePattern = Pattern.compile("_(\\w)");
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }


}
