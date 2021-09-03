package com.ldv.money_tracker.ui.fragments.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ldv.money_tracker.R;
import com.ldv.money_tracker.storage.entities.AccountsEntity;
import com.ldv.money_tracker.storage.entities.Currency;
import com.ldv.money_tracker.storage.entities.ExpenseEntity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ExpensesAdapter extends SelectableAdapter<ExpensesAdapter.ExpenseHolder> {

    private List<ExpenseEntity> expensesList;//подключились к таблицe

    private ClickListener clickListener;

    public ExpensesAdapter(List<ExpenseEntity> expensesList, ClickListener clickListener) {//конструктор

        this.expensesList = expensesList;
        this.clickListener = clickListener;

    }

    @Override
    public ExpenseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expense_item, parent, false); //нашли куда передавать данные
        return new ExpenseHolder(itemView, clickListener);
    }

    @Override
    public void onBindViewHolder(ExpenseHolder holder, int position) {
        String price = "";
        ExpenseEntity expense = expensesList.get(position); //с позиции начинаем передавать данные
        holder.name.setText(expense.name); //присвоили имя
        holder.date.setText(expense.date);//дату
        if (expense.getAccount() != null) {
            holder.account.setText(expense.getAccount().getAccountName());

            AccountsEntity accountsEntity = expense.getAccount();
            Currency currency = accountsEntity.getAccountCurency();

           price = currency.getCurrency().toString();
        }
        holder.price.setText(expense.price + " " + price); //цену

        if (expense.getCategory() != null) {
            holder.category.setText(expense.getCategory().getName());//вернули категорию, доставли имя
            //Потому что объект expense уже содержит в себе объект типа CategoryEntity
            //гет сатегори - метод трат, гед нейм метод категорий
            //показывать когда на трату нажали
        }
        holder.selectedOverlay.setVisibility(isSelected(position) ? View.VISIBLE : View.INVISIBLE);
        //если выделено показываем, и наоборот
    }

    @Override
    public int getItemCount() {
        return expensesList.size();
    }

    private void removeItem(int position) {
        removeExpenses(position);
        notifyItemRemoved(position);
    }

    private void removeExpenses(int position) {
        if (expensesList.get(position) != null) {
            expensesList.get(position).delete();
            expensesList.remove(position);
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


    class ExpenseHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView name;//задали все вьюхи для вывода полей
        TextView price;
        TextView date;
        TextView category;
        TextView account;
        View selectedOverlay;
        private ClickListener clickListener;


        public ExpenseHolder(View itemView, ClickListener clickListener) {//конструктор
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.expense_item_expense_name);//имя передали во вьюху в фрагменте
            price = (TextView) itemView.findViewById(R.id.expense_item_expense_price);//цену
            date = (TextView) itemView.findViewById(R.id.expense_item_date);//дату
            category = (TextView) itemView.findViewById(R.id.expense_item_category);//категорию
            account = (TextView) itemView.findViewById(R.id.expense_item_account) ;
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
