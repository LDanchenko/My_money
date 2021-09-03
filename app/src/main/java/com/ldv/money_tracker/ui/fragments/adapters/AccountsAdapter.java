package com.ldv.money_tracker.ui.fragments.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ldv.money_tracker.R;
import com.ldv.money_tracker.storage.entities.AccountsEntity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//Адаптер передает данные из таблицы "Категории" в RecycleView ВЕЗДЕ ГДЕ СПИСОК - НУЖЕН АДАПТЕР
public class AccountsAdapter extends SelectableAdapter<AccountsAdapter.AccountsHolder> { //передали класс что нижечерез адаптер

    private List<AccountsEntity> accountsList; //создали экземпляр списка
    private Context context;
    private ClickListener clickListener;

    public AccountsAdapter(Context context, List<AccountsEntity> accountsList, ClickListener clickListener ) {//конструктор
        this.context = context;
        this.accountsList = accountsList;
        this.clickListener = clickListener;
    }

    @Override
    public AccountsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.account_item, parent, false);//данные передаем в categories_item(карточка).xml
        return new AccountsHolder(itemView, clickListener);
    }

    @Override
    public void onBindViewHolder(AccountsHolder holder, int position) {
        AccountsEntity accounts = accountsList.get(position); //применили метод гет, получили данные из таблицы с такой то позиции
        holder.accounts_name.setText(accounts.account_name);//из гета в сет передали, присвоили имя тексвью
        if (accounts.getAccountCurency() != null) {
            String name = accounts.account_count + " " + accounts.currency.getCurrency();

            holder.accounts_count.setText(name);
        }
        // int url = categories.getImage(); //что быстрее работает - глайд или просто?

  //      Log.d("Entity", "" + categories.getImage());
//
     /*   Glide
                .with(context)
                .load(url)
                .asBitmap()
              //   .crossFade()//от прозрачного к видному -анимация
               // .diskCacheStrategy(DiskCacheStrategy.SOURCE) //сохранить оригинал изображеня в кєш
                .into(holder.categories_image);*/
        holder.selectedOverlay.setVisibility(isSelected(position) ? View.VISIBLE : View.INVISIBLE);
    }


    @Override
    public int getItemCount() {
        return accountsList.size();
    }

    private void removeItem(int position) {
        removeAccounts(position);
        notifyItemRemoved(position);
    }

    private void removeAccounts(int position) {
        if (accountsList.get(position) != null) {
            accountsList.get(position).delete();
            accountsList.remove(position);

        }

    }


    public void removeItems(List<Integer> positions) {
        Collections.sort(positions, new Comparator<Integer>() {
            @Override
            public int compare(Integer lhs, Integer rhs) {
                return rhs - lhs;
            }
        });
        while (!positions.isEmpty()) {
            if (positions.size() == 1) {
                removeItem(positions.get(0));
                positions.remove(0);
            } else {
                for (int i = 0; i <= positions.size(); i++) {
                    removeItem(positions.get(0));
                    positions.remove(0);
                }
            }
        }
    }


    class AccountsHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {//тут ищем  текст вью для вывода категори

        TextView accounts_name;
        TextView accounts_count;
        View selectedOverlay;
        private ClickListener clickListener;


        public AccountsHolder(View itemView, ClickListener clickListener) {//konstruktor
            super(itemView);
            accounts_name = (TextView) itemView.findViewById(R.id.account_item_account_name);//нашли поле в текст вью
           accounts_count = (TextView) itemView.findViewById(R.id.account_item_account_price);
            selectedOverlay = itemView.findViewById(R.id.selected_overlay);
            this.clickListener = clickListener;
            itemView.setOnClickListener(this);//повесили на всю карточку слушатель клика
            itemView.setOnLongClickListener(this);//повесили слушатель долгого клика
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null)
                clickListener.onItemClicked(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            return clickListener != null && clickListener.onItemLongClicked(getAdapterPosition());
        }
    }


}
