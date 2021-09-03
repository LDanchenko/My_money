package com.ldv.money_tracker.ui.fragments;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.ldv.money_tracker.R;
import com.ldv.money_tracker.storage.entities.CategoryEntity;
import com.ldv.money_tracker.ui.fragments.adapters.CategorySpinnerAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@TargetApi(Build.VERSION_CODES.M)
@EActivity(R.layout.income_filtr_activity)

public class IncomeFiltrActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {



    @ViewById(R.id.spinner_income)
    Spinner spinner_income;

    @ViewById(R.id.add)
    Button button_add;

    @ViewById(R.id.cancel)
    Button button_cancel;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @AfterViews
    void ready() {


        showSpinner(spinner_income);


        //тут передаем текущую дату в едит текст


        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //проверилина пустоту два поля

               /* Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                String description = edit_text_description.getText().toString();
                String sum = edit_text_sum.getText().toString();
                String date = edit_text_date.getText().toString();

                if (description.length() == 0 || sum.length() == 0) {
                    CharSequence text = getString(R.string.field);
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } else {
                    generateExpenses(sum, description, date);

                    finish();
                }*/
            }
        });


        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//если нажата кнопка кансел просто закрываем активити - отменяем добавление траты

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_fade_in,R.anim.exit_push_out);
    }

    public void showSpinner(Spinner spinner) {//подключения списка к спинеру

        List<CategoryEntity> categoriesList = new ArrayList<CategoryEntity>();//создали список
        categoriesList.addAll(CategoryEntity.selectAll(""));//добавили в него все элементы из таблицы
        CategorySpinnerAdapter categoriesSpinner = new CategorySpinnerAdapter(this, categoriesList);//создали экзмплярсвоего адаптера, в него занесли свой лист
        categoriesSpinner.setDropDownViewResource(android.R.layout.simple_spinner_item);//тут передаем данные в эти спинер айтемы
        categoriesSpinner.notifyDataSetChanged();//если обновляются данные в таблице, то и в спинере
        spinner.setAdapter(categoriesSpinner);//повесили адаптер
        spinner.setOnItemSelectedListener(this);//повесили слушатель

    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
