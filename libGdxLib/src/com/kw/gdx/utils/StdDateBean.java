package com.kw.gdx.utils;

import java.util.Objects;

public class StdDateBean {
    private int year ;
    private int month ;
    private int day ;
    private int weekDay;

    public void setWeekDay(int weekDay) {
        this.weekDay = weekDay;
    }

    public int getWeekDay() {
        return weekDay;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public String toString() {
        return "DateBean{" +
                "year=" + year +
                ", month=" + month +
                ", day=" + day +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StdDateBean bean = (StdDateBean) o;
        return year == bean.year && month == bean.month && day == bean.day;
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, month, day);
    }
}

