package com.ldv.money_tracker.ui.fragments.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.ldv.money_tracker.R;
import com.ldv.money_tracker.storage.entities.Planing;

import java.util.List;

public class PlaningSpinnerAdapterAccount extends ArrayAdapter<Planing> implements SpinnerAdapter {

    private List<Planing> planings;

    public PlaningSpinnerAdapterAccount(Context context, List<Planing> planings) {
        super(context, 0, planings);
        this.planings = planings;//конструктор
    }


    public int getCount() {
        return planings.size();
    }


    public long getItemId(int position) {
        return planings.get(position).getId();
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        Planing plan_account = (Planing) getItem(position);//создали экзепляр
// используем созданные, но не используемые view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.planing_item_for_spinner, parent, false);//Куда будем отображать данные - список категорий
            //ищет лейаут - лейаут фрагмента
        }

        TextView planing = (TextView) convertView.findViewById(R.id.planing_item_account_name); //в какое поле в верхнем лейауте
        if (plan_account != null) {


            planing.setText(plan_account.getAccount_name().getAccountName());
        }
        //находит техтвью - туда добавляет категорию

        return convertView;
    }

    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {

        Planing planing1 = (Planing) getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.planing_item_for_spinner, parent, false);

        }

        TextView planing = (TextView) convertView.findViewById(R.id.planing_item_account_name);
        planing.setText(planing1.getAccount_name().getAccountName());

        return convertView;
    }


}

