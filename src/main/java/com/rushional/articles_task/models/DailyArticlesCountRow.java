package com.rushional.articles_task.models;

import lombok.Getter;

import java.util.Date;

@Getter
public class DailyArticlesCountRow {

    public DailyArticlesCountRow(Date date, Long articlesAmount) {
        this.date = date;
        this.articlesAmount = articlesAmount;
    }


    private Date date;
    private Long articlesAmount;
}
