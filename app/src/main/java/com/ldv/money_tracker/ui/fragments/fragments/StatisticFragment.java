package com.ldv.money_tracker.ui.fragments.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.ldv.money_tracker.R;
import com.ldv.money_tracker.storage.entities.AccountsEntity;
import com.ldv.money_tracker.storage.entities.CategoryEntity;
import com.ldv.money_tracker.storage.entities.ExpenseEntity;
import com.ldv.money_tracker.ui.fragments.adapters.AccountsSpinnerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class StatisticFragment extends Fragment {

    public  Spinner spinner_account;
    public  Spinner spinner_month;
    public Button button_ok;
    int y;
    float count;
    public  String date1;
    public String month;
    public AccountsEntity accounts;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.statistic_fragment, container, false);//Выводим фрагмент

        spinner_account = (Spinner) rootView.findViewById(R.id.account_spiner);
        spinner_month = (Spinner) rootView.findViewById(R.id.account_month);
        button_ok = (Button) rootView.findViewById(R.id.okButton);

        final PieChart chart = (PieChart) rootView.findViewById(R.id.chart);


        showSpinnerAccount(spinner_account);
        showSpinnerMonth(spinner_month);

        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List <ExpenseEntity> fin = new ArrayList<ExpenseEntity>();
                List<PieEntry> entries = new ArrayList<>();

                if (AccountsEntity.selectAll("").size() == 0) { Toast.makeText(getContext(), "Сначала добавьте счет", Toast.LENGTH_SHORT).show();}
                else {
                    List<ExpenseEntity> expenseEntity = ExpenseEntity.selectByAccount(accounts);// нашли траты по выбранному аккаунту
                if (expenseEntity.size()!=0){
                    for (int i=0; i< expenseEntity.size(); i++) {

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

                            fin.add(expenseEntity.get(i));

                        }}
                    }


                    CategoryEntity.selectAll("");
                    for (int i = 1; i <= CategoryEntity.selectAll("").size(); i++) {
                        y = 0;
                        count = 0;
                        CategoryEntity category = CategoryEntity.selectById(i);
                        if (category != null) {
                            for (int j = 0; j < fin.size(); j++) {
                                ExpenseEntity expense = fin.get(j);
                                if (expense != null && expense.getCategory() == category) {
                                    int tableSize = ExpenseEntity.selectAll("").size();
                                    float table = (float) tableSize;
                                    Log.d("table_size", "" + tableSize);
                                    y = y + 1;
                                    float u = (float) y;
                                    Log.d("Num", "num" + y);
                                    count = ((u * 100) / table);

                                    Log.d("COUNT", "" + count);
                                }
                            }
                            if (count != 0) {

                                entries.add(new PieEntry(count, category.getName()));
                            }

                        }
                    }


                    PieDataSet dataSet = new PieDataSet(entries, getString(R.string.statistic));
                    dataSet.setSliceSpace(3f);
                    dataSet.setSelectionShift(5f);



                    ArrayList<Integer> colors = new ArrayList<Integer>();

                    for (int c : ColorTemplate.VORDIPLOM_COLORS) {
                        colors.add(c);
                    }
                    for (int c : ColorTemplate.JOYFUL_COLORS) {
                        colors.add(c);
                    }
                    for (int c : ColorTemplate.COLORFUL_COLORS) {
                        colors.add(c);
                    }

                    colors.add(ColorTemplate.getHoloBlue());

                   // dataSet.setColors(colors);
                    dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                    PieData data = new PieData(dataSet);
                    data.setValueFormatter(new PercentFormatter());
                    data.setValueTextSize(20f);

                    data.setValueTextColor(R.color.colorBlack);
                    chart.setData(data);
                    chart.getDescription().setEnabled(false);
                    chart.highlightValues(null);
                    chart.invalidate();


                }
            }
        });

        return rootView;
    }

    public void showSpinnerMonth(Spinner spinner) {//подключения списка к спинеру


        List<ExpenseEntity> expenseEntities = new ArrayList <ExpenseEntity>();//создали список
        expenseEntities.addAll(ExpenseEntity.selectAll(""));//добавили в него все элементы из таблицы
        final String[] dates = new String[expenseEntities.size()];

        for (int i = 0; i < expenseEntities.size(); i++) {

            dates[i] = (expenseEntities.get(i).getDate());
        }





        Set<String> set = new HashSet<String>(Arrays.asList(dates));
        String[] result = set.toArray(new String[set.size()]);
        String[] result1 = new String[set.size()];
        for (int j = 0; j < set.size(); j++) {
            char[] t = result[j].toCharArray();

            String o = "" + t[3] + t[4];
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


            result1[j] = month;


        }


        //убираем дубликаты
        String []  array = new HashSet<String>(Arrays.asList(result1)).toArray(new String[0]);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);//повесили адаптер


//     //       spinner.setSelection(2);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

               month = spinner_month.getSelectedItem().toString();


            }

            public void onNothingSelected(AdapterView<?> parent) {
                month = spinner_month.getSelectedItem().toString();

            }
        });
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
