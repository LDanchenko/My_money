package com.ldv.money_tracker.ui.fragments.fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ldv.money_tracker.R;
import com.ldv.money_tracker.storage.entities.AccountsEntity;
import com.ldv.money_tracker.storage.entities.CategoryEntity;
import com.ldv.money_tracker.storage.entities.ExpenseEntity;
import com.ldv.money_tracker.ui.fragments.AddExpenseActivity_;
import com.ldv.money_tracker.ui.fragments.adapters.AccountsSpinnerAdapter;
import com.ldv.money_tracker.ui.fragments.adapters.CategorySpinnerAdapter;
import com.ldv.money_tracker.ui.fragments.adapters.ClickListener;
import com.ldv.money_tracker.ui.fragments.adapters.ExpensesAdapter;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.api.BackgroundExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

//ТУТ НЕ ПАШЕТ ПОИСК
@EFragment
//нашли лейаут меню и заменили setHasOptionsMenu(true)
public class ExpensesFragment extends Fragment implements DatePickerDialog.OnDateSetListener {


    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private ExpensesAdapter adapter;
    private ActionModeCallback actionModeCallback = new ActionModeCallback();
    private ActionMode actionMode;
    private SwipeRefreshLayout refreshLayout;
    SearchView searchView;
    public String selectesMonth;
    public int id;

    public Context context;

    final String SEARCH_QUERY_ID = "search_query_id";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.expenses_fragment, container, false); //нашли лайаут через инфлейт
        setHasOptionsMenu(true);
        context = getActivity();
        floatingActionButton = ((FloatingActionButton) rootView.findViewById(R.id.floating_button_expenses));//нашли кнопку на фрагменте(рутвью выше)
        recyclerView = (RecyclerView) rootView.findViewById(R.id.list_of_expenses);//обработка recycle view
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // ПОИСК ПО ДАТЕ КАТЕГОРИИ
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(700);
        itemAnimator.setRemoveDuration(700);
        recyclerView.setItemAnimator(itemAnimator);

        refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.expense_refresh_layout);
        refreshLayout.setColorSchemeColors(new int[]{getResources().getColor(R.color.colorAccent)});
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadExpenses("", 0);
            }
        });

        return rootView;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        id = item.getItemId();
        if (id == R.id.action_month) {

            final Dialog dialog = new Dialog(getActivity(), R.style.PauseDialog);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.background_light);
            dialog.getWindow().setGravity(Gravity.CENTER_VERTICAL);
            dialog.setContentView(R.layout.dialog_window_month_expenses);
            TextView textView = (TextView) dialog.findViewById(R.id.title);
            Button okButton = (Button) dialog.findViewById(R.id.okButton);
            Button cancelButton = (Button) dialog.findViewById(R.id.cancelButton);
            textView.setText("Выбери месяц");

            // Получаем экземпляр элемента Spinner
            final Spinner spinner = (Spinner) dialog.findViewById(R.id.spinner_income_filtr_expense);

            List<ExpenseEntity> expenseEntities = new ArrayList <ExpenseEntity>();//создали список
            expenseEntities.addAll(ExpenseEntity.selectAll(""));//добавили в него все элементы из таблицы
           final String[] dates = new String[expenseEntities.size()];

            for (int i = 0; i < expenseEntities.size(); i++) {

                dates[i] = (expenseEntities.get(i).getDate());
            }


            String month = "";

//выводить словами. и реализовать поиск



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


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.simple_spinner_custom_item, array);

            spinner.setAdapter(adapter);//повесили адаптер


//     //       spinner.setSelection(2);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent,
                                           View itemSelected, int selectedItemPosition, long selectedId) {

                    String[] choose = dates;


                    char[] t = choose[selectedItemPosition].toCharArray();

                    selectesMonth = "" + t[3] + t[4];


                }

                public void onNothingSelected(AdapterView<?> parent) {
                    selectesMonth = "";

                }
            });

            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    loadExpenses(selectesMonth, 1);

                    dialog.getWindow().getAttributes().windowAnimations = R.anim.dialog_slide_down;
                    dialog.dismiss();

                }
                // }
            });
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // dialog.getWindow().getAttributes().windowAnimations = R.anim.dialog_slide_down;
                    dialog.dismiss();
                }
            });
            dialog.show();
            return true;

        }
        if (id == R.id.category) {


            final Dialog dialog = new Dialog(getActivity(), R.style.PauseDialog);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.background_light);
            dialog.getWindow().setGravity(Gravity.CENTER_VERTICAL);
            dialog.setContentView(R.layout.dialog_window_cat);

            Button okButton = (Button) dialog.findViewById(R.id.okButton);
            final Button cancelButton = (Button) dialog.findViewById(R.id.cancelButton);


            final Spinner spinner_category = (Spinner) dialog.findViewById(R.id.spinner_filtr_category);


            final List<CategoryEntity> categoryEntities = new ArrayList<CategoryEntity>();//создали список
            categoryEntities.addAll(CategoryEntity.selectAll(""));//добавили в него все элементы из таблицы
            CategorySpinnerAdapter categorySpinnerAdapter = new CategorySpinnerAdapter(getContext(), categoryEntities);//создали экзмплярсвоего адаптера, в него занесли свой лист
            categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);//тут передаем данные в эти спинер айтемы
            categorySpinnerAdapter.notifyDataSetChanged();//если обновляются данные в таблице, то и в спинере
            spinner_category.setAdapter(categorySpinnerAdapter);//повесили адаптер
            spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                    CategoryEntity categoryEntity = (CategoryEntity) spinner_category.getSelectedItem();

                    selectesMonth = categoryEntity.getName();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    selectesMonth = "";

                }
            });
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int u = 4;
                    loadExpenses(selectesMonth, u);


                    dialog.getWindow().getAttributes().windowAnimations = R.anim.dialog_slide_down;
                    dialog.dismiss();

                }
                // }
            });
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // dialog.getWindow().getAttributes().windowAnimations = R.anim.dialog_slide_down;
                    dialog.dismiss();
                }
            });
            dialog.show();
            return true;
        }
        if (id == R.id.type) {


            final Dialog dialog = new Dialog(getActivity(), R.style.PauseDialog);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.background_light);
            dialog.getWindow().setGravity(Gravity.CENTER_VERTICAL);
            dialog.setContentView(R.layout.dialog_window_type);
            TextView textView = (TextView) dialog.findViewById(R.id.title);
            // final EditText editText = (EditText) dialog.findViewById(R.id.edittext);
            Button okButton = (Button) dialog.findViewById(R.id.okButton);
            Button cancelButton = (Button) dialog.findViewById(R.id.cancelButton);


            final Spinner spinner_category = (Spinner) dialog.findViewById(R.id.spinner_income_filtr_category);


            final List<AccountsEntity>  accountsEntities = new ArrayList<AccountsEntity>();//создали список
            accountsEntities.addAll(AccountsEntity.selectAll(""));//добавили в него все элементы из таблицы
            AccountsSpinnerAdapter accountsSpinnerAdapter = new AccountsSpinnerAdapter(getContext(), accountsEntities);//создали экзмплярсвоего адаптера, в него занесли свой лист
            accountsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);//тут передаем данные в эти спинер айтемы
            accountsSpinnerAdapter.notifyDataSetChanged();//если обновляются данные в таблице, то и в спинере
            spinner_category.setAdapter(accountsSpinnerAdapter);//повесили адаптер
            spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                    AccountsEntity accountsEntity = (AccountsEntity) spinner_category.getSelectedItem();

                    selectesMonth = accountsEntity.getAccountName();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    selectesMonth = "";

                }
            });
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int u = 2;
                    loadExpenses(selectesMonth, u);


                    dialog.getWindow().getAttributes().windowAnimations = R.anim.dialog_slide_down;
                    dialog.dismiss();

                }
                // }
            });
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // dialog.getWindow().getAttributes().windowAnimations = R.anim.dialog_slide_down;
                    dialog.dismiss();
                }
            });
            dialog.show();
            return true;
        }
        if (id == R.id.action_day) {
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

            DatePickerDialog dialog = new DatePickerDialog(context, (DatePickerDialog.OnDateSetListener) this,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));




            dialog.show();
            // отображаем диалоговое окно для выбора даты
            return true;


        }

        return super.

                onOptionsItemSelected(item);

        //спинер категории
    }


    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_category, menu);
        inflater.inflate(R.menu.menu_search, menu);
        searchView = (SearchView) menu.findItem(R.id.search_action).getActionView();//по айди нашли item меню
        searchView.setQueryHint(getString(R.string.search_title));//подсказка
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() { //повесили listener
            @Override
            public boolean onQueryTextSubmit(String query) {//когданажимаем кнопку поиск
                loadExpenses(query, 0);//делаем запрос
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) { //то что человек набирает
                //класс сбрасывает запрос, после 600 мс запускает поток и делает запрос
                //сбрасывает запрос с таким айди(запрос пометилианнотацией с айди) тру - можно ли сбросить
                BackgroundExecutor.cancelAll(SEARCH_QUERY_ID, true);
                queryExpense(newText);
                return false;
            }
        });
    }


    @Override
    public void onStart () {
        super.onStart();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AccountsEntity.selectAll("").size() != 0) {
                    AddExpenseActivity_.intent(getActivity()).start()
                            .withAnimation(R.anim.enter_pull_in, R.anim.exit_fade_out);
                    //   startActivity(new Intent(getActivity(), AddIncomeActivity_.class));//тут мы запускаем AddExpenseActivity
                } else
                    Toast.makeText(getContext(), "Сначала добавьте счет", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume () {
        super.onResume();
        loadExpenses ("", 0); //тут отрабатывает лоадер, полностью все методы
    }

    @Background(delay = 600, id = SEARCH_QUERY_ID)
    //запускаем запрос в бд  в другом потоке с определенной задержкой (600 мс)
    public void queryExpense (String query){

        loadExpenses (query, 0);//делаем запрос
    }

    private void loadExpenses ( final String query, final int j){
        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<List<ExpenseEntity>>() { //инициализировали лоадер
            @Override
            public Loader<List<ExpenseEntity>> onCreateLoader(int id, Bundle args) {
                final AsyncTaskLoader<List<ExpenseEntity>> loader = new AsyncTaskLoader<List<ExpenseEntity>>(getActivity()) {
                    @Override
                    public List<ExpenseEntity> loadInBackground() {
                        List<ExpenseEntity> expenseEntities = new ArrayList<ExpenseEntity>();


                        if (query != "" && j == 1) {

                            List<ExpenseEntity> expense = ExpenseEntity.selectDate(query);


                            for (int i = 0; i < expense.size(); i++) {
                                String t = expense.get(i).getDate();
                                String o = t.substring(3, 5);
                                if (o.equals(query)) {
                                    //года
                                    expenseEntities.add(expense.get(i));
                                }
                            }

                        }
                        else if (query!="" && j == 4) {

                            List<CategoryEntity> categoryEnt = CategoryEntity.selectAll("");
                            for (int i = 0; i < categoryEnt.size(); i++) {
                                String t = categoryEnt.get(i).getName();

                                if (query.equals(t)) {
                                    CategoryEntity categoryEntity = categoryEnt.get(i);
                                    expenseEntities = ExpenseEntity.selectByCategory(categoryEntity);

                                }
                            }
                        }
                        else if (query!="" && j == 2) {

                            List<AccountsEntity> accountsEntities = AccountsEntity.selectAll("");
                            for (int i = 0; i < accountsEntities.size(); i++) {
                                String t = accountsEntities.get(i).getAccountName();

                                if (query.equals(t)) {
                                    AccountsEntity accountsEntity = accountsEntities.get(i);
                                    expenseEntities = ExpenseEntity.selectByAccount(accountsEntity);

                                }
                            }
                        }
                        else if ( j == 0) {
                           expenseEntities = ExpenseEntity.selectAll(query);
                        }

                        else  if (j == 5) {
                            expenseEntities = ExpenseEntity.selectDate(query);
                        }
                        return expenseEntities;
                    }

                };
                loader.forceLoad();
                return loader;
            }

            @Override
            public void onLoadFinished(Loader<List<ExpenseEntity>> loader, List<ExpenseEntity> data) {
                adapter = new ExpensesAdapter(data, new ClickListener() {
                    @Override
                    public void onItemClicked(int position) {
                        if (actionMode != null) {
                            toggleSelection(position);
                        }
                    }

                    @Override
                    public boolean onItemLongClicked(int position) {
                        if (actionMode == null) {
                            AppCompatActivity activity = (AppCompatActivity) getActivity();
                            actionMode = activity.startSupportActionMode(actionModeCallback);
                        }
                        toggleSelection(position);
                        return true;
                    }
                });
                recyclerView.setAdapter(adapter);
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onLoaderReset(Loader<List<ExpenseEntity>> loader) {

            }
        });
    }


    private void toggleSelection ( int position){
        adapter.toggleSelection(position);
        int count = adapter.getSelectedItemCount();
        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String  _years = Integer.toString(year);
        String  _months = Integer.toString(month+1);
        if (_months.length()==1) {_months = "0" + _months;}
        String  _days = Integer.toString(dayOfMonth);
        String query = _days + "-" + _months + "-" + _years;
        loadExpenses(query , 5);
    }


    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) { //подключили менюшку
            mode.getMenuInflater().inflate(R.menu.contextual_action_bar, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_remove:
                    adapter.removeItems(adapter.getSelectedItems());

                    mode.finish();
                    return true;
                default:
                    return false;
            }

        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            adapter.clearSelection();
            actionMode = null;
        }
    }



}

