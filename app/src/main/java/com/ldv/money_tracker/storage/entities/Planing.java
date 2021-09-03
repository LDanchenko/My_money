package com.ldv.money_tracker.storage.entities;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;


@Table(name = "planing")
public class Planing extends Model{

    @Column(name = "plan")
    public float plan;

    @Column (name= "month")
    public String month;

    @Column (name = "spent")
    public float spent;


    @Column(name = "account", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public AccountsEntity account;

    public Planing() {
        super();
    }

    public AccountsEntity getAccount_name() {
        return account;
    }

    public void setAccount_name(AccountsEntity account_name) {
        this.account = account_name;
    }

    public float getPlan() {
        return plan;
    }

    public void setPlan(float plan) {
        this.plan = plan;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public float getSpent() {
        return spent;
    }

    public void setSpent(float spent) {
        this.spent = spent;
    }

    public static List<Planing> selectByAccount(AccountsEntity accountsEntity) {
        return new Select().from(Planing.class)
                .where("account = ?", accountsEntity.getId())
                .execute();//execute возвращает список
    }

    public static List<Planing> selectAll() {
        return new Select().from(Planing.class)
                //выбери нам все что похоже  и выведи в массив стринг
                //условие пишется тут, потому что если строка не задаа выведет все
                .execute();//execute возвращает список
    }
}
