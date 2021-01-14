package com.peachberry.todolist.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Calendar {

    private Integer year;

    private Integer month;

    private Integer day;

    protected Calendar() {}

    public Calendar(Integer year, Integer month, Integer day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

}
