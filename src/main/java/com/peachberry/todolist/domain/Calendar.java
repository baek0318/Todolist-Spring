package com.peachberry.todolist.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Calendar {

    private int year;

    private int month;

    private int day;

    protected Calendar() {}

    public Calendar(Integer year, Integer month, Integer day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

}
