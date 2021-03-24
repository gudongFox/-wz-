package com.cmcu.common.util;

import java.text.NumberFormat;
import java.util.Locale;

public class NumberChangeUtils {
    /**
     * 把阿拉伯数字转换为罗马数字
       * @param number
     * @return
     */
    public static String a2r(int number) {
        String rNumber = "";
        int[] aArray = { 1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1 };
        String[] rArray = { "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X",
                "IX", "V", "IV", "I" };
        if (number < 1 || number > 3999) {
            rNumber = "-1";
        } else {
            for (int i = 0; i < aArray.length; i++) {
                while (number >= aArray[i]) {
                    rNumber += rArray[i];
                    number -= aArray[i];
                }
            }
        }
        return rNumber;
    }

    /**
     * 把罗马数字转换为阿拉伯数字
     *
     * @param m
     * @return
     */
    public static int r2a(String m) {
        int graph[] = new int[400];
        graph['I'] = 1;
        graph['V'] = 5;
        graph['X'] = 10;
        graph['L'] = 50;
        graph['C'] = 100;
        graph['D'] = 500;
        graph['M'] = 1000;
        char[] num = m.toCharArray();
        int sum = graph[num[0]];
        for (int i = 0; i < num.length - 1; i++) {
            if (graph[num[i]] >= graph[num[i + 1]]) {
                sum += graph[num[i + 1]];
            } else {
                sum = sum + graph[num[i + 1]] - 2 * graph[num[i]];
            }
        }
        return sum;
    }

    public static String moneyToChinese(double amount) {
        try {

            String[] yuan = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖",
                    "拾", "佰", "仟", "万", "亿", "圆", "角", "分", "负"};

            NumberFormat format = NumberFormat.getInstance(Locale.CHINA);
            format.setMaximumFractionDigits(2);
            format.setMinimumFractionDigits(2);
            format.setGroupingUsed(false);

            String money = format.format(amount);

            // 记录符号
            String flag = null;
            if (money.indexOf('-') == 0) {
                flag = yuan[18];
                money = money.substring(1);
            }

            // 最大小数点前24位
            if (money.length() > 27) {
                return "超出有效范围";
            }

            StringBuilder strb = new StringBuilder();

            char[] chars = money.toCharArray();
            int len = chars.length;
            for (int i = len - 1; i >= 0; i--) {
                String temp = null;
                switch (chars[i]) {
                    case '0':
                        temp = yuan[0];
                        break;
                    case '1':
                        temp = yuan[1];
                        break;
                    case '2':
                        temp = yuan[2];
                        break;
                    case '3':
                        temp = yuan[3];
                        break;
                    case '4':
                        temp = yuan[4];
                        break;
                    case '5':
                        temp = yuan[5];
                        break;
                    case '6':
                        temp = yuan[6];
                        break;
                    case '7':
                        temp = yuan[7];
                        break;
                    case '8':
                        temp = yuan[8];
                        break;
                    case '9':
                        temp = yuan[9];
                        break;
                    case '.':
                        temp = yuan[15];
                        break;
                }

                switch (len - 1 - i) {
                    case 0:
                        temp += yuan[17];
                        break;
                    case 1:
                        temp += yuan[16];
                        break;
                    case 4:
                    case 8:
                    case 12:
                    case 16:
                    case 20:
                    case 24:
                        temp += yuan[10];
                        break;
                    case 5:
                    case 9:
                    case 13:
                    case 17:
                    case 21:
                    case 25:
                        temp += yuan[11];
                        break;
                    case 6:
                    case 10:
                    case 14:
                    case 18:
                    case 22:
                    case 26:
                        temp += yuan[12];
                        break;
                    case 7:
                    case 15:
                    case 23:
                        temp += yuan[13];
                        break;
                    case 11:
                    case 19:
                        temp += yuan[14];
                        break;
                }

                strb.insert(0, temp);
            }

            // 插入符号位
            if (null != flag) {
                strb.insert(0, flag);
            }


            String result = strb.toString();
            result = result.replaceAll("零拾", "零");
            result = result.replaceAll("零佰", "零");
            result = result.replaceAll("零仟", "零");
            result = result.replaceAll("零零零", "零");
            result = result.replaceAll("零零", "零");
            result = result.replaceAll("零角零分", "整");
            result = result.replaceAll("零分", "整");
            result = result.replaceAll("零角", "零");
            result = result.replaceAll("零亿零万零圆", "亿圆");
            result = result.replaceAll("亿零万零圆", "亿圆");
            result = result.replaceAll("零亿零万", "亿");
            result = result.replaceAll("零万零圆", "万圆");
            result = result.replaceAll("零亿", "亿");
            result = result.replaceAll("零万", "万");
            result = result.replaceAll("零圆", "圆");
            result = result.replaceAll("零零", "零");

        return result;
        } catch (Exception e){
            return "";
        }
    }

    public static String ToCH(int intInput) {
        String si = String.valueOf(intInput);
        String sd = "";
        if (si.length() == 1) // 個
        {
            sd += GetCH(intInput);
            return sd;
        } else if (si.length() == 2)// 十
        {
            if (si.substring(0, 1).equals("1"))
                sd += "十";
            else
                sd += (GetCH(intInput / 10) + "十");
            sd += ToCH(intInput % 10);
        } else if (si.length() == 3)// 百
        {
            sd += (GetCH(intInput / 100) + "百");
            if (String.valueOf(intInput % 100).length() < 2)
                sd += "零";
            sd += ToCH(intInput % 100);
        } else if (si.length() == 4)// 千
        {
            sd += (GetCH(intInput / 1000) + "千");
            if (String.valueOf(intInput % 1000).length() < 3)
                sd += "零";
            sd += ToCH(intInput % 1000);
        } else if (si.length() == 5)// 萬
        {
            sd += (GetCH(intInput / 10000) + "萬");
            if (String.valueOf(intInput % 10000).length() < 4)
                sd += "零";
            sd += ToCH(intInput % 10000);
        }

        return sd;
    }

    private static String GetCH(int input) {
        String sd = "";
        switch (input) {
            case 1:
                sd = "一";
                break;
            case 2:
                sd = "二";
                break;
            case 3:
                sd = "三";
                break;
            case 4:
                sd = "四";
                break;
            case 5:
                sd = "五";
                break;
            case 6:
                sd = "六";
                break;
            case 7:
                sd = "七";
                break;
            case 8:
                sd = "八";
                break;
            case 9:
                sd = "九";
                break;
            default:
                break;
        }
        return sd;
    }

}
