package com.ldv.money_tracker.storage.entities;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;
@Table(name = "account_entity")
    public class AccountsEntity extends Model {

        @Column(name = "account_name")
        public String account_name;

        @Column(name = "account_count")
        public String account_count;


    @Column(name = "currency", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public Currency currency;//содержит обьект типа категори

        public AccountsEntity() {
            super();
        }


    public String getAccountName() {
        return account_name;
    }

    public String  getAccountCount() {return account_count;} //Думай почему так?

    public void setAccountName(String account_name) {
        this.account_name = account_name;
    }

    public void setAccountCount(String  account_count) {
        this.account_count = account_count;
    }

    public Currency  getAccountCurency() {return currency;}


    public void setAccountCurrency(Currency  account_currency) {
        this.currency = account_currency;
    }

    public static List<AccountsEntity> selectAll(String query) {
        return new Select().from(AccountsEntity.class)
                .where("account_name LIKE?", new String[]{'%' + query + '%'})//выбери нам все что похоже  и выведи в массив стринг
                //условие пишется тут, потому что если строка не задаа выведет все
                .execute();//execute возвращает список
    }


    }

