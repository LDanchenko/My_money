package com.ldv.money_tracker.storage.entities;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;


@Table (name = "income")
public class IncomeEntity extends Model {

    @Column(name = "cost")
    public String cost;

    @Column(name = "name")
    public String name;

    @Column(name = "date")
    public String date;

    @Column(name = "type", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public IncomeType type;//содержит обьект типа категори

    @Column(name = "account", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public AccountsEntity account;//содержит обьект типа категори

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public IncomeType getType() {
        return type;
    }

    public void setAccount(AccountsEntity account) {
        this.account = account;
    }

    public AccountsEntity getAccount() {
        return account;
    }

    public void setType(IncomeType type) {
        this.type = type;
    }

    //запрос - выбрать все данные из табл Траты

    public static List<IncomeEntity> selectAll(String query) {

        return new Select().from(IncomeEntity.class)
                .where("type LIKE?", new String[]{'%' + query + '%'})//выбери нам все что похоже  и выведи в массив стринг
                //условие пишется тут, потому что если строка не задаа выведет все
                .execute();//execute возвращает список
    }

    public static List<IncomeEntity> selectAllType(String query) {

    List <IncomeEntity> inc = IncomeEntity.selectAll("");

       for (int i=0; i<inc.size();i++){
            IncomeType incomeType = inc.get(i).getType();
            inc.get(i).setName(incomeType.getTypeName().toString());
            inc.get(i).save();
    }

        return new Select().from(IncomeEntity.class)
                .where("name LIKE?", new String[]{'%' + query + '%'})//выбери нам все что похоже  и выведи в массив стринг
                //условие пишется тут, потому что если строка не задаа выведет все
                .execute();//execute возвращает список
    }

    public static IncomeEntity selectById(int query) {//запрос на выборку - выбрали все из табл. Категории
        return new Select().from(IncomeEntity.class)
                .where("id LIKE?", query)
                .executeSingle();//execute возвращает список
    }

    public static List<IncomeEntity> selectDate(String query) {


        return new Select().from(IncomeEntity.class)
                .where("date LIKE?", new String[]{'%'+ query + '%' })//выбери нам все что похоже  и выведи в массив стринг
                //условие пишется тут, потому что если строка не задаа выведет все
                .execute();//execute возвращает список
    }

    public static List<IncomeEntity> selectByType(IncomeType type) {


        return new Select().from(IncomeEntity.class)
                .where("type = ?", type.getId())//выбери нам все что похоже  и выведи в массив стринг
                //условие пишется тут, потому что если строка не задаа выведет все
                .execute();//execute возвращает список
    }


    public static List<IncomeEntity> selectByAcount(AccountsEntity account) {


        return new Select().from(IncomeEntity.class)
                .where("account = ?", account.getId())//выбери нам все что похоже  и выведи в массив стринг
                //условие пишется тут, потому что если строка не задаа выведет все
                .execute();//execute возвращает список
    }
}
