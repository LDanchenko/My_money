package com.ldv.money_tracker.ui.fragments.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ldv.money_tracker.R;
import com.ldv.money_tracker.storage.entities.AccountsEntity;
import com.ldv.money_tracker.storage.entities.Currency;
import com.ldv.money_tracker.storage.entities.Planing;
import com.ldv.money_tracker.ui.fragments.AddPlaning_;
import com.ldv.money_tracker.ui.fragments.adapters.AccountsSpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lubov on 22.05.17.
 */

public class PlaningFragment extends Fragment{

    public  FloatingActionButton floatingActionButton;

public AccountsEntity accounts;
  public  Spinner spinner_account;
    public  Spinner spinner_month;
    public Button button_ok;
    public  Button button_save;
    public EditText spent;
    public String month;
    public EditText planing;
    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.planing_fragment, container, false); //нашли лайаут через инфлейт

        floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.floating_button_planing);
        spinner_account = (Spinner)rootView.findViewById(R.id.account_spiner);
        spinner_month = (Spinner) rootView.findViewById(R.id.account_month);
        button_ok = (Button) rootView.findViewById(R.id.okButton);
        button_save = (Button) rootView.findViewById(R.id.save);
        spent = (EditText) rootView.findViewById(R.id.spent);
        planing = (EditText) rootView.findViewById(R.id.plan);

        return rootView;




    }

    @Override
    public void onStart () {
        super.onStart();
        planing.setText("");
        spent.setText("");
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AccountsEntity.selectAll("").size() != 0) {
                    AddPlaning_.intent(getActivity()).start()
                            .withAnimation(R.anim.enter_pull_in, R.anim.exit_fade_out);
                    //   startActivity(new Intent(getActivity(), AddIncomeActivity_.class));//тут мы запускаем AddExpenseActivity
                } else
                    Toast.makeText(getContext(), "Сначала добавьте счет", Toast.LENGTH_SHORT).show();
            }
        });
        showSpinnerAccount(spinner_account);
        showSpinnerMonth(spinner_month);

        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AccountsEntity.selectAll("").size() == 0) { Toast.makeText(getContext(), "Сначала добавьте счет", Toast.LENGTH_SHORT).show();}
                else {

                    List<Planing> plan = Planing.selectByAccount(accounts);
                    if (plan.size() == 0) {
                        Toast.makeText(getActivity(), "Нет планов ни на один месяц по этому счету", Toast.LENGTH_LONG).show();
                    }
                    for (int i = 0; i < plan.size(); i++) {

                        if (plan.get(i).getMonth().equals(month)) {
                            float h = plan.get(i).getPlan();
                            Currency currency = accounts.getAccountCurency();
                            String str = Float.toString(h)+ " " + currency.getCurrency();
                            // Toast.makeText(getActivity(), " - " + plan.get(i).getPlan(), Toast.LENGTH_LONG).show();

                          if (str!="") {  planing.setText(str); }
                            else {planing.setText("");}

                            float h1 = plan.get(i).getSpent();


                            String str1 = Float.toString(h1)+ " " +  currency.getCurrency();
                          if (str1!="") {  spent.setText(str1); }
                            else {spent.setText("");}
                           //вывод если план не найден
                           /* if (plan.get(i).getPlan()==0) {
                                Toast.makeText(getActivity(), "Нет планов на этот месяц", Toast.LENGTH_LONG).show();
                            }*/
                        }
                        else {Toast.makeText(getActivity(), "Нет планов на этот месяц", Toast.LENGTH_SHORT).show();}
                    }


                }
            }
        });

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Planing> plan = Planing.selectByAccount(accounts);
                for (int i=0; i<plan.size(); i++) {

                    if (plan.get(i).getMonth().equals(month)) {

                        String str2 = planing.getText().toString() ;
                       String y = str2.substring(0,str2.indexOf(" "));
                        int u = y.length();
                        String stroka = y.substring(0,u);

                       float t = Float.parseFloat(stroka);
                       plan.get(i).setPlan(t);
                        plan.get(i).save();
                    }
                }
            }
        });
    }

    public void showSpinnerMonth(Spinner spinner) {//подключения списка к спинеру

        // Настраиваем адаптер
        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(getActivity(), R.array.months, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 month = spinner_month.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                month = spinner_month.getSelectedItem().toString();
            }
        });//повесили слушатель

    }

    public void showSpinnerAccount(Spinner spinner) {//подключения списка к спинеру

        List<AccountsEntity> planings = new ArrayList<AccountsEntity>();//создали список
        planings.addAll(AccountsEntity.selectAll(""));//добавили в него все элементы из таблицы
        AccountsSpinnerAdapter accountsSpinnerAdapter = new AccountsSpinnerAdapter(getActivity(), planings);//создали экзмплярсвоего адаптера, в него занесли свой лист
        accountsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);//тут передаем данные в эти спинер айтемы
        accountsSpinnerAdapter.notifyDataSetChanged();//если обновляются данные в таблице, то и в спинере
        spinner.setAdapter(accountsSpinnerAdapter);//повесили адаптер
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AccountsEntity accountsEntity = (AccountsEntity) spinner_account.getSelectedItem();

              accounts = accountsEntity;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                AccountsEntity accountsEntity = (AccountsEntity) spinner_account.getSelectedItem();

                accounts = accountsEntity;
            }
        });//повесили слушатель

    }

}
