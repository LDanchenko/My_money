package com.ldv.money_tracker.ui.fragments.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.ldv.money_tracker.R;
import com.ldv.money_tracker.storage.entities.AccountsEntity;
import com.ldv.money_tracker.storage.entities.Currency;
import com.ldv.money_tracker.ui.fragments.adapters.AccountsAdapter;
import com.ldv.money_tracker.ui.fragments.adapters.ClickListener;
import com.ldv.money_tracker.ui.fragments.adapters.CurrencySpinnerAdapter;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.api.BackgroundExecutor;

import java.util.ArrayList;
import java.util.List;

@EFragment
@OptionsMenu(R.menu.menu_search)//нашли лейаут меню и заменили setHasOptionsMenu(true)
public class AccountsFragment extends Fragment implements View.OnClickListener {


    private ActionModeCallback actionModeCallback = new ActionModeCallback();
    private ActionMode actionMode;
    private AccountsAdapter adapter;

    private RecyclerView recyclerView;
    public GridLayoutManager lLayout;

    private FloatingActionButton floatingActionButton;
    SearchView searchView;
    final String SEARCH_QUERY_ID = "search_query_id";
    private SwipeRefreshLayout refreshLayout;
    public Context context;
    final int GALLERY_REQUEST = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.account_fragment, container, false);//Выводим фрагмент
        //LayoutInflater отвечает за сериализацию XML-файла в Java-объект типа View.

        context = getActivity();
       lLayout = new GridLayoutManager(getActivity(), 1);
        floatingActionButton = ((FloatingActionButton) rootView.findViewById(R.id.floating_button_account));//нашли кнопку на фрагменте(рутвью выше)
        floatingActionButton.setOnClickListener(this);//повесили слушатель клика

        recyclerView = (RecyclerView) rootView.findViewById(R.id.list_of_accounts);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(lLayout);

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(700);
        itemAnimator.setRemoveDuration(700);
        recyclerView.setItemAnimator(itemAnimator);

        //    TextView date_text = (TextView) rootView.findViewById(R.id.)

        refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.account_refresh_layout);
        refreshLayout.setColorSchemeColors(new int[] {getResources().getColor(R.color.colorAccent)});
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadAccounts("");
            }
        });

        return rootView;
    }

    @Override
    public void onClick(View v) {
        final Dialog dialog = new Dialog(getActivity(),R.style.PauseDialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.background_light);
        dialog.getWindow().setGravity(Gravity.CENTER_VERTICAL);
        dialog.setContentView(R.layout.dialog_window_add_account);

        // Получаем экземпляр элемента Spinner
        final Spinner spinner = (Spinner)dialog.findViewById(R.id.spinner_currency);

// Настраиваем адаптер


          final EditText editText = (EditText) dialog.findViewById(R.id.edittext);
        final EditText editTextCount = (EditText) dialog.findViewById(R.id.editcount);
        Button okButton = (Button) dialog.findViewById(R.id.okButton);
        Button cancelButton = (Button) dialog.findViewById(R.id.cancelButton);

        List<Currency> currencies = new ArrayList<Currency>();//создали список
        currencies.addAll(Currency.selectAll(""));//добавили в него все элементы из таблицы
        CurrencySpinnerAdapter currencySpinnerAdapter = new CurrencySpinnerAdapter(getContext(), currencies);//создали экзмплярсвоего адаптера, в него занесли свой лист
        currencySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);//тут передаем данные в эти спинер айтемы
        currencySpinnerAdapter.notifyDataSetChanged();//если обновляются данные в таблице, то и в спинере
        spinner.setAdapter(currencySpinnerAdapter);//повесили адаптер




               okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  Editable text = editText.getText();
                    Editable count = editTextCount.getText();
                   if (!TextUtils.isEmpty(text)&&!TextUtils.isEmpty(count)) {


                       String name = editText.getText().toString();
                       String money = editTextCount.getText().toString();

                       List<AccountsEntity> accountsEntities = AccountsEntity.selectAll("");
                 //      for (int i = 0; i < accountsEntities.size(); i++) {
                   //        if (accountsEntities.get(i).getAccountName().equals(name)) {
                     //          Toast.makeText(getContext(), "Вы уже добавили счет с таким названием!", Toast.LENGTH_SHORT).show();
                       //        break;
                        //   } else {
                               AccountsEntity accountsEntity = new AccountsEntity();
                               accountsEntity.setAccountCount(money);
                               accountsEntity.setAccountName(name);
                               //добалвять из галереи?
                               Currency currency = (Currency) spinner.getSelectedItem();
                               accountsEntity.setAccountCurrency(currency);
                               accountsEntity.save();
                               loadAccounts("");
                               dialog.getWindow().getAttributes().windowAnimations = R.anim.dialog_slide_down;
                               dialog.dismiss();
                            //    break;
                         //  }
                    //  }
                   }

                  else Toast.makeText(getContext(), "Заполните все поля", Toast.LENGTH_SHORT).show();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dialog.getWindow().getAttributes().windowAnimations = R.anim.dialog_slide_down;
                dialog.dismiss();
            }
        });
        dialog.show();
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        searchView = (SearchView) menu.findItem(R.id.search_action).getActionView();//по айди нашли item меню
        searchView.setQueryHint(getString(R.string.search_title));//подсказка
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() { //повесили listener
            @Override
            public boolean onQueryTextSubmit(String query) {//когданажимаем кнопку поиск
                loadAccounts(query);//делаем запрос
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) { //то что человек набирает
                //класс сбрасывает запрос, после 600 мс запускает поток и делает запрос
                //сбрасывает запрос с таким айди(запрос пометилианнотацией с айди) тру - можно ли сбросить
                BackgroundExecutor.cancelAll(SEARCH_QUERY_ID, true);
                queryAccounts(newText);
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadAccounts("");
    }

    @Background(delay = 600, id = SEARCH_QUERY_ID)
    //запускаем запрос в бд  в другом потоке с определенной задержкой (600 мс)
    public void queryAccounts(String query) {
        loadAccounts(query);//делаем запрос
    }

    public void loadAccounts(final String query) {
        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<List<AccountsEntity>>() {
            @Override
            public Loader<List<AccountsEntity>> onCreateLoader(int id, Bundle args) {
                final AsyncTaskLoader<List<AccountsEntity>> loader = new AsyncTaskLoader<List<AccountsEntity>>(getActivity()) {
                    @Override
                    public List<AccountsEntity> loadInBackground() {

                        return AccountsEntity.selectAll(query);
                    }
                };
                loader.forceLoad();
                return loader;
            }

            @Override
            public void onLoadFinished(Loader<List<AccountsEntity>> loader, List<AccountsEntity> data) {
                adapter = new AccountsAdapter(context, data, new ClickListener() {
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
                recyclerView.setAdapter(adapter);//через адаптер подгрузили данные во фрагмент
                refreshLayout.setRefreshing(false);
            }






            @Override
            public void onLoaderReset(Loader<List<AccountsEntity>> loader) {

            }
        });

    }


    private void toggleSelection(int position) {
        adapter.toggleSelection(position);
        int count = adapter.getSelectedItemCount();
        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
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
