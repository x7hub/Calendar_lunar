/*
 * Get from:
 * https://code.google.com/p/android-project-sse-ustc/
 * 
 * Licensed under the Apache License, Version 2.0
 * 
 * modified by zzz
 * 
 */

package edu.bupt.calendar;

import java.util.Date;

public final class Lunar {
    private static int monCyl;
    private static int dayCyl;
    private static int yearCyl;
    private static int year;
    private static int month;
    private static int day;
    /** zzz */
    private static int syear;
    private static int smonth;
    private static int sday;
    private static boolean isSetShowLunar = true;

    private static boolean isLeap;
    private static int[] lunarInfo = { 19416, 19168, 42352, 21717, 53856,
            55632, 91476, 22176, 39632, 21970, 19168, 42422, 42192, 53840,
            119381, 46400, 54944, 44450, 38320, 84343, 18800, 42160, 46261,
            27216, 27968, 109396, 11104, 38256, 21234, 18800, 25958, 54432,
            59984, 28309, 23248, 11104, 100067, 37600, 116951, 51536, 54432,
            120998, 46416, 22176, 107956, 9680, 37584, 53938, 43344, 46423,
            27808, 46416, 86869, 19872, 42448, 83315, 21200, 43432, 59728,
            27296, 44710, 43856, 19296, 43748, 42352, 21088, 62051, 55632,
            23383, 22176, 38608, 19925, 19152, 42192, 54484, 53840, 54616,
            46400, 46496, 103846, 38320, 18864, 43380, 42160, 45690, 27216,
            27968, 44870, 43872, 38256, 19189, 18800, 25776, 29859, 59984,
            27480, 21952, 43872, 38613, 37600, 51552, 55636, 54432, 55888,
            30034, 22176, 43959, 9680, 37584, 51893, 43344, 46240, 47780,
            44368, 21977, 19360, 42416, 86390, 21168, 43312, 31060, 27296,
            44368, 23378, 19296, 42726, 42208, 53856, 60005, 54576, 23200,
            30371, 38608, 19415, 19152, 42192, 118966, 53840, 54560, 56645,
            46496, 22224, 21938, 18864, 42359, 42160, 43600, 111189, 27936,
            44448 };

    private static String[] Gan = { "甲", "乙", "丙", "丁", "戊", "己", "庚", "辛",
            "壬", "癸" };

    private static String[] Zhi = { "子", "丑", "寅", "卯", "辰", "巳", "午", "未",
            "申", "酉", "戌", "亥" };

    private static String[] Animals = { "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊",
            "猴", "鸡", "狗", "猪" };

    private static String[] nStr1 = { "日", "一", "二", "三", "四", "五", "六", "七",
            "八", "九", "十" };

    private static String[] nStr2 = { "初", "十", "廿", "卅", "　" };

    private static String[] monthNong = { "正", "正", "二", "三", "四", "五", "六",
            "七", "八", "九", "十", "十一", "十二" };

    private static String[] yearName = { "零", "壹", "贰", "叁", "肆", "伍", "陆",
            "柒", "捌", "玖" };

    private static String cDay(int d) {
        String s;

        switch (d) {
        case 10:
            s = "初十";

            break;
        case 20:
            s = "二十";

            break;
        case 30:
            s = "三十";

            break;
        default:
            s = nStr2[(d / 10)];

            s = s + nStr1[(d % 10)];
        }

        return s;
    }

    private static String cyclical(int num) {
        return Gan[(num % 10)] + Zhi[(num % 12)];
    }

    private static String cYear(int y) {
        String s = "";

        while (y > 0) {
            int d = y % 10;

            y = (y - d) / 10;

            s = yearName[d] + s;
        }

        return s;
    }

    public static int getDay() {
        return day;
    }

    private static int getDayCyl() {
        return dayCyl;
    }

    public static boolean getIsBig() {
        return monthDays(getYear(), getMonth()) != 29;
    }

    public static boolean getIsLeap() {
        return isLeap;
    }

    public static void setLunar(int year, int month, int day) {
        if(!isSetShowLunar) {
            return;
        }
        syear = year;
        smonth = month;
        sday = day;

        Date sDObj = new Date(syear - 1900, smonth, sday);
        Lunar1(sDObj);
    }

    public static String getLunar() {
        if(!isSetShowLunar) {
            return "";
        }
        int sy = (year - 4) % 12;
        String s = "农历 【" + Animals[sy] + "】" + cYear(getYear()) + "年" + " ";

        s = s + ((getIsLeap()) ? "闰" : "") + monthNong[getMonth()] + "月"
                + ((!getIsBig()) ? "小" : "大");

        s = s + cDay(getDay()) + " ";

        s = s + cyclical(getYearCyl()) + "年" + cyclical(getMonCyl()) + "月"
                + cyclical(getDayCyl()) + "日";

        return s;
    }

    private static int getMonCyl() {
        return monCyl;
    }

    public static int getMonth() {
        return month;
    }

    public static String getLunarDay() {
        if(!isSetShowLunar) {
            return "";
        }
        return cDay(getDay());
    }

    public static String getLunarDayForDisplay() {
        if(!isSetShowLunar) {
            return "";
        }
        String dayName = "";
        dayName = Festival.showSFtv(smonth, sday);
        dayName = Festival.showLFtv(month, day, monthDays(year, month));

        // 初一
        if (getDay() == 1 && dayName == "") {
            return getLunarMonth();
        }
        // 节假日
        if (dayName != "")
            return dayName;
        // 平时
        return cDay(getDay());
    }

    public static String getLunarWithComma() {
        if(!isSetShowLunar) {
            return "";
        }
        return " , " + getLunarMonth() + getLunarDay();
    }
    
    public static String getLunarMonth() {
        if(!isSetShowLunar) {
            return "";
        }
        return monthNong[getMonth()] + "月";
    }

    public static String getLunarYear() {
        if(!isSetShowLunar) {
            return "";
        }
        int animalYear = (year - 4) % 12;
        String lunarYear = cyclical(yearCyl) + "年【" + Animals[animalYear]
                + "年】";
        return lunarYear;
    }

    public static int getYear() {
        return year;
    }

    private static int getYearCyl() {
        return yearCyl;
    }

    private static int leapDays(int y) {
        if (leapMonth(y) != 0) {
            return ((lunarInfo[(y - 1900)] & 0x10000) == 0) ? 29 : 30;
        }

        return 0;
    }

    private static int leapMonth(int y) {
        return lunarInfo[(y - 1900)] & 0xF;
    }

    private static void Lunar1(Date objDate) {
        int i;
        int leap = 0;
        int temp = 0;

        Date baseDate = new Date(0, 0, 31);

        int offset = (int) ((objDate.getTime() - baseDate.getTime()) / 86400000L);

        dayCyl = offset + 40;

        monCyl = 14;

        for (i = 1900; (i < 2050) && (offset > 0); ++i) {
            temp = lYearDays(i);

            offset -= temp;

            monCyl += 12;
        }

        if (offset < 0) {
            offset += temp;

            --i;

            monCyl -= 12;
        }

        year = i;

        yearCyl = i - 1864;

        leap = leapMonth(i);

        isLeap = false;

        for (i = 1; (i < 13) && (offset > 0); ++i) {
            if ((leap > 0) && (i == leap + 1) && (!isLeap)) {
                --i;

                isLeap = true;

                temp = leapDays(year);
            } else {
                temp = monthDays(year, i);
            }

            if ((isLeap) && (i == leap + 1)) {
                isLeap = false;
            }
            offset -= temp;

            if (isLeap)
                continue;
            monCyl += 1;
        }

        if ((offset == 0) && (leap > 0) && (i == leap + 1)) {
            if (isLeap) {
                isLeap = false;
            } else {
                isLeap = true;

                --i;

                monCyl -= 1;
            }
        }

        if (offset < 0) {
            offset += temp;

            --i;

            monCyl -= 1;
        }

        month = i;

        day = offset + 1;
    }

    private static int lYearDays(int y) {
        int sum = 348;

        for (int i = 32768; i > 8; i >>= 1) {
            sum += (((lunarInfo[(y - 1900)] & i) == 0) ? 0 : 1);
        }

        return sum + leapDays(y);
    }

    private static int monthDays(int y, int m) {
        return ((lunarInfo[(y - 1900)] & 65536 >> m) == 0) ? 29 : 30;
    }

    /** zzz */
    public static void setShowLunch(boolean b) {
        // TODO
        isSetShowLunar = b;
    }
}