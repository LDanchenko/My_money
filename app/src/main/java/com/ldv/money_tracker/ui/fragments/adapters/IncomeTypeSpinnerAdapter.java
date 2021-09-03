package com.ldv.money_tracker.ui.fragments.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.ldv.money_tracker.R;
import com.ldv.money_tracker.storage.entities.IncomeType;

import java.util.List;

public class IncomeTypeSpinnerAdapter extends ArrayAdapter<IncomeType> implements SpinnerAdapter {

    private List<IncomeType> incomeTypes;

    public IncomeTypeSpinnerAdapter(Context context, List<IncomeType> incomeTypes) {
        super(context, 0, incomeTypes);
        this.incomeTypes = incomeTypes;//конструктор
    }


    public int getCount() {
        return incomeTypes.size();
    }


    public long getItemId(int position) {
        return incomeTypes.get(position).getId();
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        IncomeType incomeType = (IncomeType) getItem(position);//создали экзепляр
// используем созданные, но не используемые view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.income_item_for_spinner, parent, false);//Куда будем отображать данные - список категорий
            //ищет лейаут - лейаут фрагмента
        }

        TextView type_name = (TextView) convertView.findViewById(R.id.income_item_type_name); //в какое поле в верхнем лейауте
        type_name.setText(incomeType.type_name);
        //находит техтвью - туда добавляет категорию

        return convertView;
    }

    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {

       IncomeType incomeType = (IncomeType) getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.income_item_for_spinner, parent, false);

        }

        TextView category_name = (TextView) convertView.findViewById(R.id.income_item_type_name);
        category_name.setText(incomeType.type_name);
        return convertView;

    }


}
