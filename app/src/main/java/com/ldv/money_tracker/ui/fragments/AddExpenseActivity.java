package com.ldv.money_tracker.ui.fragments;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ldv.money_tracker.R;
import com.ldv.money_tracker.storage.entities.AccountsEntity;
import com.ldv.money_tracker.storage.entities.CategoryEntity;
import com.ldv.money_tracker.storage.entities.ExpenseEntity;
import com.ldv.money_tracker.storage.entities.Planing;
import com.ldv.money_tracker.ui.fragments.adapters.AccountsSpinnerAdapter;
import com.ldv.money_tracker.ui.fragments.adapters.CategorySpinnerAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

@TargetApi(Build.VERSION_CODES.M)
@EActivity(R.layout.add_expense_activity)//вывод активити
public class AddExpenseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    @ViewById(R.id.expense_sum)
    EditText edit_text_sum;

    @ViewById(R.id.spinner)
    Spinner spinner_category;

    @ViewById(R.id.spinner_account)
    Spinner spinner_account;

    @ViewById(R.id.add)
    Button button_add;

    @ViewById(R.id.cancel)
    Button button_cancel;

    @ViewById(R.id.expense_description)
    EditText edit_text_description;

    @ViewById(R.id.date)
    TextView edit_text_date;

    long accountId;
    long categoryId;
    public  String month;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @AfterViews
    void ready() {


        //обработали спинер
        showSpinner(spinner_category);
        showSpinnerAccount(spinner_account);
        showDate(edit_text_date);
        edit_text_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

                DatePickerDialog dialog = new DatePickerDialog(AddExpenseActivity.this, myCallBack,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));




                dialog.show();

            }
        });
        //тут передаем текущую дату в едит текст


        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    //проверилина пустоту два поля
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_LONG;
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
                    }

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

    @RequiresApi(api = Build.VERSION_CODES.N)//вывод даты в едит
    public void showDate(TextView editText) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = sdf.format(c.getTime());
        editText.setText(strDate, TextView.BufferType.EDITABLE);

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

    public void showSpinnerAccount(Spinner spinner) {//подключения списка к спинеру

        List<AccountsEntity> accountsEntities = new ArrayList<AccountsEntity>();//создали список
        accountsEntities.addAll(AccountsEntity.selectAll(""));//добавили в него все элементы из таблицы
       AccountsSpinnerAdapter accountsSpinnerAdapter = new AccountsSpinnerAdapter(this, accountsEntities);//создали экзмплярсвоего адаптера, в него занесли свой лист
        accountsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);//тут передаем данные в эти спинер айтемы
        accountsSpinnerAdapter.notifyDataSetChanged();//если обновляются данные в таблице, то и в спинере
        spinner.setAdapter(accountsSpinnerAdapter);//повесили адаптер
        spinner.setOnItemSelectedListener(this);//повесили слушатель

    }

    private void generateExpenses(String sum, String description, String date) { //тут вставляем в базу данных даные по тратам

        ExpenseEntity expenseEntity = new ExpenseEntity();
        expenseEntity.setName(description);//передали имя
        expenseEntity.setPrice(sum);//передали трату
        expenseEntity.setDate(date);//передать дату
        CategoryEntity category = (CategoryEntity) spinner_category.getSelectedItem();//экземпляр с id выбраного элемента спинера
        expenseEntity.setCategory(category);
        AccountsEntity account = (AccountsEntity) spinner_account.getSelectedItem();
        expenseEntity.setAccount(account);

        float count = Float.parseFloat(account.getAccountCount()); //считаем сколько было на счету
        float plus = count - Float.parseFloat(sum);
        account.setAccountCount(Float.toString(plus));
        account.save();

//тут траты добавляем для планирования - условие где


       String o = date.substring(3, 5);

        switch (o) {
            case "01":
                month = "Январь";
                break;
            case "02":
                month = "Февраль";
                break;
            case "03":
                month = "Март";
                break;
            case "04":
                month = "Апрель";
                break;
            case "05":
                month = "Май";
                break;
            case "06":
                month = "Июнь";
                break;
            case "07":
                month = "Июль";
                break;
            case "08":
                month = "Август";
                break;
            case "09":
                month = "Сентябрь";
                break;
            case "10":
                month = "Октябрь";
                break;
            case "11":
                month = "Ноябрь";
                break;
            case "12":
                month = "Декабрь";
                break;
        }
        List<Planing> plan = Planing.selectByAccount(account);
        if (plan.size()!=0) {
            for (int i = 0; i < plan.size(); i++) {

                if (plan.get(i).getMonth().equals(month)) {

                    float ss = plan.get(i).getSpent();
                    float sp = ss - Float.parseFloat(sum);
                    plan.get(i).setSpent(sp);
                    plan.get(i).save();
                }
            }
        }

        expenseEntity.setPrice(sum);//передали трату
        //записали категорию, такой метод сет категори, принимает обьект типа категории, а в адаптере сетит имя
        expenseEntity.save();//сохранили

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    DatePickerDialog.OnDateSetListener myCallBack = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            String _years = Integer.toString(year);
            String _months = Integer.toString(month + 1);
            if (_months.length() == 1) {
                _months = "0" + _months;
            }


            String _days = Integer.toString(dayOfMonth);
            if (_days.length() == 1) {
                _days = "0" + _days;
            }
            String query = _days + "-" + _months + "-" + _years;
            edit_text_date.setText(query);
        }
    };

}
