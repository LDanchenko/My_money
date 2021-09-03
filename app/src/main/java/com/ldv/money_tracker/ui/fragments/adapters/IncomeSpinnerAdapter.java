package com.ldv.money_tracker.ui.fragments.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.ldv.money_tracker.R;
import com.ldv.money_tracker.storage.entities.IncomeEntity;

import java.util.List;

public class IncomeSpinnerAdapter extends ArrayAdapter<IncomeEntity> implements SpinnerAdapter {

    private List<IncomeEntity> incomeList;
    public String o = "";
    public String month = "";
    public IncomeSpinnerAdapter(Context context, List<IncomeEntity> incomeList) {
        super(context, 0, incomeList);
        this.incomeList = incomeList;//конструктор
    }


    public int getCount() {
        return incomeList.size();
    }


    public long getItemId(int position) {
        return incomeList.get(position).getId();
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        IncomeEntity incomeEntity = (IncomeEntity) getItem(position);//создали экзепляр
// используем созданные, но не используемые view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.income_item_for_spinner, parent, false);//Куда будем отображать данные - список категорий
            //ищет лейаут - лейаут фрагмента
        }

        TextView income_date = (TextView) convertView.findViewById(R.id.income_item_type_name); //в какое поле в верхнем лейауте
        String concat_date = incomeEntity.date;
        income_date.setText(o);
        //находит техтвью - туда добавляет категорию

        return convertView;
    }

    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {

        IncomeEntity incomeEntity = (IncomeEntity) getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.income_item_for_spinner, parent, false);

        }

        TextView income_date = (TextView) convertView.findViewById(R.id.income_item_type_name);
        String concat_date = incomeEntity.date;

        char[] t = concat_date.toCharArray();

            o = "" + t[3] + t[4];
        switch (o) {
            case "01":  month = "Январь";
                break;
            case "02":  month = "Февраль";
                break;
            case "03":  month = "Март";
                break;
            case "04":  month = "Апрель";
                break;
            case "05":  month = "Май";
                break;
            case "06":  month = "Июнь";
                break;
            case "07":  month = "Июль";
                break;
            case "08":  month = "Август";
                break;
            case "09":  month = "Сентябрь";
                break;
            case "10":  month = "Октябрь";
                break;
            case "11": month = "Ноябрь";
                break;
            case "12": month = "Декабрь";
                break;
        }


        income_date.setText(month);

        return convertView;
    }


}
