package com.ldv.money_tracker.ui.fragments.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.ldv.money_tracker.R;
import com.ldv.money_tracker.storage.entities.CategoryEntity;

import java.util.List;

public class CategorySpinnerAdapter extends ArrayAdapter<CategoryEntity> implements SpinnerAdapter {

    private List<CategoryEntity> categoriesList;

    public CategorySpinnerAdapter(Context context, List<CategoryEntity> categoriesList) {
        super(context, 0, categoriesList);
        this.categoriesList = categoriesList;//конструктор
    }


    public int getCount() {
        return categoriesList.size();
    }


    public long getItemId(int position) {
        return categoriesList.get(position).getId();
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        CategoryEntity categoryEntity = (CategoryEntity) getItem(position);//создали экзепляр
// используем созданные, но не используемые view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.categories_item_for_spinner, parent, false);//Куда будем отображать данные - список категорий
            //ищет лейаут - лейаут фрагмента
        }

        TextView category_name = (TextView) convertView.findViewById(R.id.categories_item_categories_name); //в какое поле в верхнем лейауте
        category_name.setText(categoryEntity.name);
        //находит техтвью - туда добавляет категорию

        return convertView;
    }

    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {

        CategoryEntity categoryEntity = (CategoryEntity) getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.categories_item_for_spinner, parent, false);

        }

        TextView category_name = (TextView) convertView.findViewById(R.id.categories_item_categories_name);
        category_name.setText(categoryEntity.name);

        return convertView;
    }


}
