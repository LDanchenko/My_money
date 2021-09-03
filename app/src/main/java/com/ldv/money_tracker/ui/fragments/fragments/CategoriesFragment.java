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
import android.widget.TextView;
import android.widget.Toast;

import com.ldv.money_tracker.R;
import com.ldv.money_tracker.storage.entities.CategoryEntity;
import com.ldv.money_tracker.ui.fragments.adapters.CategoriesAdapter;
import com.ldv.money_tracker.ui.fragments.adapters.ClickListener;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.api.BackgroundExecutor;

import java.util.List;

@EFragment
@OptionsMenu(R.menu.menu_search)//нашли лейаут меню и заменили setHasOptionsMenu(true)
public class CategoriesFragment extends Fragment implements View.OnClickListener {


    private ActionModeCallback actionModeCallback = new ActionModeCallback();
    private ActionMode actionMode;
    private CategoriesAdapter adapter;

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
        View rootView = inflater.inflate(R.layout.categories_fragment, container, false);//Выводим фрагмент
        //LayoutInflater отвечает за сериализацию XML-файла в Java-объект типа View.

        context = getActivity();
        lLayout = new GridLayoutManager(getActivity(), 1);
       // coordinatorLayout = (LinearLayout) rootView.findViewById(R.id.categories_fragment_root_layout);// нашли Coordinator layout
        floatingActionButton = ((FloatingActionButton) rootView.findViewById(R.id.floating_button_categories));//нашли кнопку на фрагменте(рутвью выше)
        floatingActionButton.setOnClickListener(this);//повесили слушатель клика

        recyclerView = (RecyclerView) rootView.findViewById(R.id.list_of_categories);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(lLayout);

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(700);
        itemAnimator.setRemoveDuration(700);
        recyclerView.setItemAnimator(itemAnimator);


        refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.category_refresh_layout);
        refreshLayout.setColorSchemeColors(new int[] {getResources().getColor(R.color.colorAccent)});
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadCategories("");
            }
        });

        return rootView;
    }

    @Override
    public void onClick(View v) {
        final Dialog dialog = new Dialog(getActivity(),R.style.PauseDialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.background_light);
        dialog.getWindow().setGravity(Gravity.CENTER_VERTICAL);
        dialog.setContentView(R.layout.dialog_window_account);
        TextView textView = (TextView) dialog.findViewById(R.id.title);
        final EditText editText = (EditText) dialog.findViewById(R.id.edittext);
        Button okButton = (Button) dialog.findViewById(R.id.okButton);
        Button cancelButton = (Button) dialog.findViewById(R.id.cancelButton);
        textView.setText("Введите название категории:");

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable text = editText.getText();
                if (!TextUtils.isEmpty(text)) {
                   CategoryEntity categoryEntity = new CategoryEntity();
              String name = editText.getText().toString();
                    categoryEntity.setName(name);
                    categoryEntity.setImage(R.drawable.food); //добалвять из галереи?
                    categoryEntity.save();
                    loadCategories("");
                    dialog.getWindow().getAttributes().windowAnimations = R.anim.dialog_slide_down;
                    dialog.dismiss();

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
                loadCategories(query);//делаем запрос
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) { //то что человек набирает
                //класс сбрасывает запрос, после 600 мс запускает поток и делает запрос
                //сбрасывает запрос с таким айди(запрос пометилианнотацией с айди) тру - можно ли сбросить
                BackgroundExecutor.cancelAll(SEARCH_QUERY_ID, true);
                queryCategories(newText);
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadCategories("");
    }

    @Background(delay = 600, id = SEARCH_QUERY_ID)
    //запускаем запрос в бд  в другом потоке с определенной задержкой (600 мс)
    public void queryCategories(String query) {
        loadCategories(query);//делаем запрос
    }

    public void loadCategories(final String query) {
        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<List<CategoryEntity>>() {
            @Override
            public Loader<List<CategoryEntity>> onCreateLoader(int id, Bundle args) {
                final AsyncTaskLoader<List<CategoryEntity>> loader = new AsyncTaskLoader<List<CategoryEntity>>(getActivity()) {
                    @Override
                    public List<CategoryEntity> loadInBackground() {
                        return CategoryEntity.selectAll(query);
                    }
                };
                loader.forceLoad();
                return loader;
            }

            @Override
            public void onLoadFinished(Loader<List<CategoryEntity>> loader, List<CategoryEntity> data) {
                adapter = new CategoriesAdapter(context, data, new ClickListener() {
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


            /*@Override
             public void onLoadFinished(Loader<List<CategoryEntity>> loader, List<CategoryEntity> data) {
                 adapter = new CategoriesAdapter(context, data, new ClickListener(), {
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
 */

            @Override
            public void onLoaderReset(Loader<List<CategoryEntity>> loader) {

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
