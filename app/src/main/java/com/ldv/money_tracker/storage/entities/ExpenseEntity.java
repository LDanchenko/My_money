package com.ldv.money_tracker.storage.entities;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

@Table(name = "expenses") //создали таблицу траты
public class ExpenseEntity extends Model {


    @Column(name = "price")
    public String price;

    @Column(name = "name")
    public String name;

    @Column(name = "date")
    public String date;

    @Column(name = "category", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public CategoryEntity category;//содержит обьект типа категори

    @Column(name = "account", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public AccountsEntity account;//содержит обьект типа категори


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public AccountsEntity getAccount() {
        return account;
    }

    public void setAccount(AccountsEntity account) {
        this.account = account;
    }

    //запрос - выбрать все данные из табл Траты

    public static List<ExpenseEntity> selectAll(String query) {
        return new Select().from(ExpenseEntity.class)
                .where("name LIKE?", new String[]{'%' + query + '%'})//выбери нам все что похоже  и выведи в массив стринг
                //условие пишется тут, потому что если строка не задаа выведет все
                .execute();//execute возвращает список
    }

    public static ExpenseEntity selectById(int query) {//запрос на выборку - выбрали все из табл. Категории
        return new Select().from(ExpenseEntity.class)
                .where("id LIKE?", query)
                .executeSingle();//execute возвращает список
    }

    public static List<ExpenseEntity> selectByCategory(CategoryEntity category) {//запрос на выборку - выбрали все из табл. Категории
        return new Select().from(ExpenseEntity.class)
                .where("category = ?", category.getId())//выбери нам все что похоже  и выведи в массив стринг
                //условие пишется тут, потому что если строка не задаа выведет все
                .execute();//execute возвращает список
    }

    public static List<ExpenseEntity> selectByAccount(AccountsEntity accountsEntity) {//запрос на выборку - выбрали все из табл. Категории
        return new Select().from(ExpenseEntity.class)
                .where("account = ?", accountsEntity.getId())//выбери нам все что похоже  и выведи в массив стринг
                //условие пишется тут, потому что если строка не задаа выведет все
                .execute();//execute возвращает список
    }
    public static List<ExpenseEntity> selectDate(String query) {


        return new Select().from(ExpenseEntity.class)
                .where("date LIKE?", new String[]{'%'+ query + '%' })//выбери нам все что похоже  и выведи в массив стринг
                //условие пишется тут, потому что если строка не задаа выведет все
                .execute();//execute возвращает список
    }

}
