package com.ldv.money_tracker.storage.entities;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by lubov on 24.05.17.
 */

@Table(name = "currency")
public class Currency extends Model {

    @Column(name = "currency")
    public String currency;


    public Currency() {
        super();
    }


    public String getCurrency() {
        return currency;
    }


    public void setCurrency(String currency) {
        this.currency = currency;
    }


    public static List<Currency> selectAll(String query) {
        return new Select().from(Currency.class)
                .where("currency LIKE?", new String[]{'%' + query + '%'})//выбери нам все что похоже  и выведи в массив стринг
                //условие пишется тут, потому что если строка не задаа выведет все
                .execute();//execute возвращает список
    }
}