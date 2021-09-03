package com.ldv.money_tracker.ui.fragments;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ldv.money_tracker.R;
import com.ldv.money_tracker.storage.entities.AccountsEntity;
import com.ldv.money_tracker.storage.entities.ExpenseEntity;
import com.ldv.money_tracker.storage.entities.Planing;
import com.ldv.money_tracker.ui.fragments.adapters.AccountsSpinnerAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@TargetApi(Build.VERSION_CODES.M)
@EActivity(R.layout.add_planing_activity)//вывод активити
public class AddPlaning extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    @ViewById(R.id.sum)
    EditText edit_text_sum;

    @ViewById(R.id.month)
    Spinner spinner_month;

    @ViewById(R.id.account)
    Spinner account_1;

    @ViewById(R.id.add)
    Button button_add;

    @ViewById(R.id.cancel)
    Button button_cancel;

    public  List<Planing> plan;
    public  String month;
    public  String date;
    public String date1;
    public float h = 0;
    public boolean m = false;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @AfterViews
    void ready() {


        //обработали спинер
        showSpinnerMonth(spinner_month);
        showSpinnerAccount(account_1);

        //тут передаем текущую дату в едит текст


        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m = false;
                AccountsEntity account = (AccountsEntity)account_1.getSelectedItem();
             //   Toast toast1 = Toast.makeText(getApplicationContext(), " " + account.getAccountName(), Toast.LENGTH_LONG);
               // toast1.show();

                month = spinner_month.getSelectedItem().toString();
                plan = Planing.selectByAccount(account);

                //проверилина пустоту два поля
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_LONG;

                String sum = edit_text_sum.getText().toString();


                if ( sum.length() == 0) {
                    CharSequence text = getString(R.string.field);
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else {
                  //  if (Planing.selectAll().size()!=0){
                        if (plan.size()!=0) {
                            for (int i = 0; i < plan.size(); i++) {

                                if (plan.get(i).getMonth().equals(month)) {

                                    Toast.makeText(getApplicationContext(), "Уже есть плану на такой месяц для этого счета", Toast.LENGTH_LONG).show();
                                    m = true;
                                    break;

                                }
                            }

                          if (m==false){
                                    generatePlan(sum);
                                    ;
                                    finish();
                                }

                        }
                    else {
                        generatePlan(sum);

                        finish();}

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



    public void showSpinnerMonth(Spinner spinner) {//подключения списка к спинеру

       // Настраиваем адаптер
        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(this, R.array.months, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
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

    private void generatePlan(String sum) { //тут вставляем в базу данных даные по тратам

        Planing planing = new Planing();
        AccountsEntity accountsEntity = (AccountsEntity) account_1.getSelectedItem();
        planing.setAccount_name(accountsEntity);
     String   month1 = spinner_month.getSelectedItem().toString();
        planing.setMonth(month1);
        float count = Float.parseFloat(sum); //считаем сколько было на счету
        planing.setPlan(count);

        if (ExpenseEntity.selectAll("").size()!=0) {
            List<ExpenseEntity> expenseEntity = ExpenseEntity.selectByAccount(accountsEntity);
            if (expenseEntity.size()!= 0) {
                for (int i = 0; i < expenseEntity.size(); i++) {
                    String date = expenseEntity.get(i).getDate().substring(3, 5);
                    switch (date) {
                        case "01":
                            date1 = "Январь";
                            break;
                        case "02":
                            date1 = "Февраль";
                            break;
                        case "03":
                            date1 = "Март";
                            break;
                        case "04":
                            date1 = "Апрель";
                            break;
                        case "05":
                            date1 = "Май";
                            break;
                        case "06":
                            date1 = "Июнь";
                            break;
                        case "07":
                            date1 = "Июль";
                            break;
                        case "08":
                            date1 = "Август";
                            break;
                        case "09":
                            date1 = "Сентябрь";
                            break;
                        case "10":
                            date1 = "Октябрь";
                            break;
                        case "11":
                            date1 = "Ноябрь";
                            break;
                        case "12":
                            date1 = "Декабрь";
                            break;
                    }
                    if (date1.equals(month)) {
                        h = h - Float.parseFloat(expenseEntity.get(i).getPrice());
                    }
                }
            }
        }
        planing.setSpent(h);
        planing.save();//сохранили

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



}
