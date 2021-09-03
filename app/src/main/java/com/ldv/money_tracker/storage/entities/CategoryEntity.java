package com.ldv.money_tracker.storage.entities;



import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;


import java.util.List;


@Table(name = "categories")
public class CategoryEntity extends Model {

    @Column(name = "name")
    public String name;

    @Column(name = "image")
    public int image;

    public CategoryEntity() {
        super();
    }

    public String getName() {
        return name;
    }

   public int  getImage() {return image;} //Думай почему так?

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(int  image) {
      this.image = image;
    }

    public List<ExpenseEntity> expenses() { //метод типа лист(список обьекта експенс_е), для создания связи по полю категория
        return getMany(ExpenseEntity.class, "category");
    }

    public static List<CategoryEntity> selectAll(String query) {//запрос на выборку - выбрали все из табл. Категории
        return new Select().from(CategoryEntity.class)
                .where("name LIKE?", new String[]{'%' + query + '%'})
                .execute();//execute возвращает список
    }


    public static CategoryEntity selectById(long query) {//запрос на выборку - выбрали все из табл. Категории
        return new Select().from(CategoryEntity.class)
                .where("id LIKE?", query)
                .executeSingle();//execute возвращает список
    }


}
