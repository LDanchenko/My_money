package com.ldv.money_tracker.storage.entities;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

@Table(name = "income_type")
public class IncomeType extends Model {

    @Column(name = "type_name")
    public String type_name;

    @Column(name = "type_image")
    public int type_image;

    public IncomeType() {
        super();
    }

    public String getTypeName() {
        return type_name;
    }

    public int  getTypeImage() {return type_image;} //Думай почему так?

    public void setTypeName(String type_name) {
        this.type_name = type_name;
    }

    public void setTypeImage(int  type_image) {
        this.type_image = type_image;
    }

    public List<IncomeEntity> incomes() { //метод типа лист(список обьекта експенс_е), для создания связи по полю категория
        return getMany(IncomeEntity.class, "type_name");
    }

    public static List<IncomeType> selectAll(String query) {//запрос на выборку - выбрали все из табл. Категории
        return new Select().from(IncomeType.class)
                .where("type_name LIKE?", new String[]{'%' + query + '%'})
                .execute();//execute возвращает список
    }

/*
    public static CategoryEntity selectById(long query) {//запрос на выборку - выбрали все из табл. Категории
        return new Select().from(CategoryEntity.class)
                .where("id LIKE?", query)
                .executeSingle();//execute возвращает список
    }*/


}