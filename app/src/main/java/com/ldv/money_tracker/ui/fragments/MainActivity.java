package com.ldv.money_tracker.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.activeandroid.Cache;
import com.activeandroid.query.Delete;
import com.activeandroid.util.Log;
import com.activeandroid.util.SQLiteUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.ldv.money_tracker.MoneyTrackerApplication;
import com.ldv.money_tracker.R;
import com.ldv.money_tracker.notification.ServiceSample;
import com.ldv.money_tracker.storage.entities.CategoryEntity;
import com.ldv.money_tracker.storage.entities.Currency;
import com.ldv.money_tracker.storage.entities.ExpenseEntity;
import com.ldv.money_tracker.storage.entities.IncomeType;
import com.ldv.money_tracker.ui.fragments.fragments.AccountsFragment_;
import com.ldv.money_tracker.ui.fragments.fragments.CategoriesFragment_;
import com.ldv.money_tracker.ui.fragments.fragments.ExpensesFragment_;
import com.ldv.money_tracker.ui.fragments.fragments.IncomeFragment;
import com.ldv.money_tracker.ui.fragments.fragments.IncomeFragment_;
import com.ldv.money_tracker.ui.fragments.fragments.PlaningFragment;
import com.ldv.money_tracker.ui.fragments.fragments.SettingsFragment;
import com.ldv.money_tracker.ui.fragments.fragments.StatisticFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.main_activity)
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @ViewById
    Toolbar toolbar;

    private static final int NOTIFICATION_ID = 4004;

    @ViewById(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @ViewById(R.id.navigation_view)
    NavigationView navigationView;

    private static final String TAG = MainActivity.class.getSimpleName();

    private ServiceSample mServiceSample;

    private boolean isBound = false;


    @AfterViews
    void ready() {



        toolbar.setTitle(R.string.money_tracker);
        setupActionBar(toolbar);//вывели тул бар
        setupDrawerLayout(drawerLayout, toolbar, navigationView);//дровер лейаут

        replaceFragment(new ExpensesFragment_()); //первым вывелифрагмент трат

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {//повесили слушатель на бэк стэк (метод replace fragment)
            //обновляет в зависимости от фрагмента
            @Override
            public void onBackStackChanged() { //для обновления тулбара и нажатой кнопки при replace
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.main_container);
                if (f != null) {
                    updateTitleAndDrawer(f);
                }
            }
        });

        // потом подвязать это все к логину - пользователь ввел свое имя, добавили стандартные кактегории
        // потом сделать подвзяку к человеку его категорий и трат и доходов
 if (IncomeType.selectAll("").isEmpty()) {


     IncomeType incomeType = new IncomeType();
     incomeType.setTypeName("Депозиты");
    // incomeType.setTypeImage(R.drawable.android_logo);
     incomeType.save();
     IncomeType incomeType1 = new IncomeType();
     incomeType1.setTypeName("Кредиты");
     // incomeType.setTypeImage(R.drawable.android_logo);
     incomeType1.save();
     IncomeType incomeType2 = new IncomeType();
     incomeType2.setTypeName("Зарплата");
     // incomeType.setTypeImage(R.drawable.android_logo);
     incomeType2.save();
     Log.d("entity", incomeType.getTypeName());

 }
        if (CategoryEntity.selectAll("").isEmpty()) {
            CategoryEntity categoryEntity3 = new CategoryEntity();
            categoryEntity3.setName("Гигиена");
            categoryEntity3.save();
            CategoryEntity categoryEntity4 = new CategoryEntity();
            categoryEntity4.setName("Жилье");
            //categoryEntity.setImage(R.mipmap.food);
            categoryEntity4.save();
            CategoryEntity categoryEntity5 = new CategoryEntity();
            categoryEntity5.setName("Здоровье");
            categoryEntity5.save();
            CategoryEntity categoryEntity6 = new CategoryEntity();
            categoryEntity6.setName("Машина");
            categoryEntity6.save();
            CategoryEntity categoryEntity7 = new CategoryEntity();
            categoryEntity7.setName("Одежда");
            categoryEntity7.save();
        }

        if (Currency.selectAll("").isEmpty()) {
            Currency currency = new Currency();
            currency.setCurrency("грн.");
            currency.save();
            Currency currency1 = new Currency();
            currency1.setCurrency("руб.");
            currency1.save();
            Currency currency2 = new Currency();
            currency2.setCurrency("$");
            currency2.save();
            Currency currency3 = new Currency();
            currency3.setCurrency("€");
            currency3.save();
        }

    }



    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {

        super.onStop();
    }


    public void updateTitleAndDrawer(Fragment fragment) {//метод для обновления тулбара и меню по нажатию кнопки назад
        String fragmentClassName = fragment.getClass().getName();//получили имя фрагмента

        if (fragmentClassName.equals(AccountsFragment_.class.getName())) {//если имя фрагмента,на который вернулись, равно имени фрагмента траты
            toolbar.setTitle("Счета");
            navigationView.setCheckedItem(R.id.drawer_accounts);//Выделили пункт меню
        }
           else  if (fragmentClassName.equals(IncomeFragment_.class.getName())) {//если имя фрагмента,на который вернулись, равно имени фрагмента траты
            toolbar.setTitle("Доходы");
            navigationView.setCheckedItem(R.id.drawer_income);//Выделили пункт меню
        }

        else  if (fragmentClassName.equals(PlaningFragment.class.getName())) {//если имя фрагмента,на который вернулись, равно имени фрагмента траты
            toolbar.setTitle("Планирование");
            navigationView.setCheckedItem(R.id.drawer_planing);//Выделили пункт меню
        }
              else   if (fragmentClassName.equals(ExpensesFragment_.class.getName())) {//если имя фрагмента,на который вернулись, равно имени фрагмента траты
                    toolbar.setTitle(getString(R.string.nav_drawer_expenses));
                    navigationView.setCheckedItem(R.id.drawer_expenses);//Выделили пункт меню
                } else if (fragmentClassName.equals(CategoriesFragment_.class.getName())) {//если имя фрагмента,на который вернулись, равно имени фрагмента траты
                    toolbar.setTitle(getString(R.string.nav_drawer_categories));
                    navigationView.setCheckedItem(R.id.drawer_categories);//Выделили пункт меню
                } else if (fragmentClassName.equals(SettingsFragment.class.getName())) {//если имя фрагмента,на который вернулись, равно имени фрагмента траты
                    toolbar.setTitle(getString(R.string.nav_drawer_settings));
                    navigationView.setCheckedItem(R.id.drawer_settings);//Выделили пункт меню
                } else if (fragmentClassName.equals(StatisticFragment.class.getName())) {//если имя фрагмента,на который вернулись, равно имени фрагмента траты
                    toolbar.setTitle(getString(R.string.nav_drawer_statistics));
                    navigationView.setCheckedItem(R.id.drawer_statistics);//Выделили пункт меню
                } else if (fragmentClassName.equals(IncomeFragment.class.getName())) {//если имя фрагмента,на который вернулись, равно имени фрагмента траты
                    toolbar.setTitle(getString(R.string.drawer_income));
                    navigationView.setCheckedItem(R.id.drawer_income);//Выделили пункт меню
                }
            }


    //В drawerlayout лежит navigationview, в нем меню и хедер
    public void setupDrawerLayout(DrawerLayout drawerLayout, Toolbar toolbar, NavigationView navigationView) {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);//связывает вместе дравер и actionbar для осуществления навигации
        drawerLayout.addDrawerListener(toggle);// Устанавливаете листенер (слушатель) на него (чтобы при нажатии на иконку гамбургера на тулбаре, DrawerLayout показывал NavigationView)
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);//добавляете слушатель событий на NavigationView. События будут прилетать в метод onNavigationItemSelected(MenuItem item)
        //для перехода по фрагментам

        //тут грузим картинку в хедер


        View headerview = navigationView.getHeaderView(0);//тут получаем картинку в хедере навигейшн вью с позицией ноль - перва картнка, у нас одна, считается от нуля
        final ImageView avatar = (ImageView) headerview.findViewById(R.id.imageView);//нашли по айди
        String url = MoneyTrackerApplication.getPicture();
      //  String url = "mipmap-hdpi/ic_account_circle_black_24dp.png";
        Glide.with(this).load(url).asBitmap().into(new BitmapImageViewTarget(avatar) {
            @Override
            protected void setResource(Bitmap resource) {
                Context context = getApplicationContext();
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                avatar.setImageDrawable(circularBitmapDrawable);
            }
        });


        TextView email = (TextView) headerview.findViewById(R.id.email);
        email.setText(MoneyTrackerApplication.getEmail());
        TextView name = (TextView) headerview.findViewById(R.id.name_surname);
        name.setText(MoneyTrackerApplication.getName());

    }


    public void setupActionBar(Toolbar toolbar) {

        setSupportActionBar(toolbar);//Устанавливает тул бар в качестве action bar
        ActionBar actionBar = getSupportActionBar();//получает ссылку на actionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//если не пустой при нажатии на кнопку возврат на один уровень приложения
        }
    }

    public void replaceFragment(Fragment fragment) { //для правильного вывода фрагментов
        String backStackName = fragment.getClass().getName(); //нашли фрагмент
        FragmentManager manager = getSupportFragmentManager();

        boolean fragmentPopped = manager.popBackStackImmediate(backStackName, 0);
//выталкивает фрагмент в майн контейнер, если фрагмент не в стеке, делаем транзакцию
        if (!fragmentPopped && manager.findFragmentByTag(backStackName) == null) {
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.main_container, fragment, backStackName);
            ft.addToBackStack(backStackName);
            ft.commit();
        }
    }

    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {//для навигаци, для перехода между пунктами меню

        if (drawerLayout != null) { //если что то выбрали
            drawerLayout.closeDrawer(GravityCompat.START);//свернули дровер
        }


        switch (item.getItemId()) {//нашли id из drawer_menu
            case R.id.drawer_expenses:
                replaceFragment(new ExpensesFragment_());//вівели фрагмент
                return true;

            case R.id.drawer_accounts:
                replaceFragment(new AccountsFragment_());//вівели фрагмент
                return true;

            case R.id.drawer_categories:
                replaceFragment(new CategoriesFragment_());
                return true;

            case R.id.drawer_statistics:
                replaceFragment(new StatisticFragment());
                return true;

            case R.id.drawer_settings:
                replaceFragment(new SettingsFragment());
                return true;

            case R.id.drawer_income:
                replaceFragment(new IncomeFragment());
                return true;

            case R.id.drawer_planing:
                replaceFragment(new PlaningFragment());
                return true;

            case R.id.drawer_exit:

                MoneyTrackerApplication.savePicture("");
                MoneyTrackerApplication.saveName("");
                MoneyTrackerApplication.saveName("");
                deleteAll();

                Intent intent = new Intent(this, LoginActivity_.class);
                startActivity(intent);
        }
        return true;
    }

    @Background
    public void deleteAll() {

        new Delete().from(ExpenseEntity.class).execute();
        new Delete().from(CategoryEntity.class).execute();

        SQLiteUtils.execSql("DROP TABLE IF EXISTS categories");
        SQLiteUtils.execSql(SQLiteUtils.createTableDefinition(Cache.getTableInfo(CategoryEntity.class)));
        SQLiteUtils.execSql("DROP TABLE IF EXISTS expenses");
        SQLiteUtils.execSql(SQLiteUtils.createTableDefinition(Cache.getTableInfo(ExpenseEntity.class)));

    }

}

