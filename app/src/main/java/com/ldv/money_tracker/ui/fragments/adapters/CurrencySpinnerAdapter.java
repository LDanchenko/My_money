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
import com.ldv.money_tracker.storage.entities.Currency;

import java.util.List;

/**
 * Created by lubov on 24.05.17.
 */



public class CurrencySpinnerAdapter extends ArrayAdapter<Currency> implements SpinnerAdapter {

    private List<Currency> currencies;

    public CurrencySpinnerAdapter(Context context, List<Currency> currencies) {
        super(context, 0, currencies);
        this.currencies = currencies;//конструктор
    }


    public int getCount() {
        return currencies.size();
    }


    public long getItemId(int position) {
        return currencies.get(position).getId();
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        Currency currencies = (Currency) getItem(position);//создали экзепляр
// используем созданные, но не используемые view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.curency_item_for_spiner, parent, false);//Куда будем отображать данные - список категорий
            //ищет лейаут - лейаут фрагмента
        }

        TextView currency = (TextView) convertView.findViewById(R.id.currency_item); //в какое поле в верхнем лейауте
        currency.setText(currencies.getCurrency());
        //находит техтвью - туда добавляет категорию

        return convertView;
    }

    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {

        Currency currency = (Currency) getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.curency_item_for_spiner, parent, false);

        }

        TextView currenc = (TextView) convertView.findViewById(R.id.currency_item);
        currenc.setText(currency.currency);

        return convertView;
    }


}
