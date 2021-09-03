package com.ldv.money_tracker.ui.fragments.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.ldv.money_tracker.R;
import com.ldv.money_tracker.storage.entities.AccountsEntity;

import java.util.List;

public class AccountsSpinnerAdapter extends ArrayAdapter<AccountsEntity> implements SpinnerAdapter {

    private List<AccountsEntity> accountsList;

    public AccountsSpinnerAdapter(Context context, List<AccountsEntity> accountsList) {
        super(context, 0, accountsList);
        this.accountsList = accountsList;//конструктор
    }


    public int getCount() {
        return accountsList.size();
    }


    public long getItemId(int position) {
        return accountsList.get(position).getId();
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        AccountsEntity accountsEntity = (AccountsEntity) getItem(position);//создали экзепляр
// используем созданные, но не используемые view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.account_item_for_spinner, parent, false);//Куда будем отображать данные - список категорий
            //ищет лейаут - лейаут фрагмента
        }

        TextView account = (TextView) convertView.findViewById(R.id.account_item_account_name); //в какое поле в верхнем лейауте
        account.setText(accountsEntity.account_name);
        //находит техтвью - туда добавляет категорию

        return convertView;
    }

    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {

       AccountsEntity accountsEntity = (AccountsEntity) getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.account_item_for_spinner, parent, false);

        }

        TextView account = (TextView) convertView.findViewById(R.id.account_item_account_name);
        account.setText(accountsEntity.account_name);

        return convertView;
    }


}
