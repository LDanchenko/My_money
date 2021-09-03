package com.ldv.money_tracker.ui.fragments.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ldv.money_tracker.R;

import com.ldv.money_tracker.storage.entities.IncomeEntity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class IncomeAdapter extends SelectableAdapter<IncomeAdapter.IncomeHolder> {

    private List<IncomeEntity> incomeList;//подключились к таблицe

    private ClickListener clickListener;

    public IncomeAdapter(List<IncomeEntity> incomeList, ClickListener clickListener) {//конструктор

        this.incomeList = incomeList;
        this.clickListener = clickListener;

    }



    @Override
    public IncomeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.income_item, parent, false); //нашли куда передавать данные
        return new IncomeHolder(itemView, clickListener);
    }


    @Override
    public void onBindViewHolder(IncomeHolder holder, int position) {

        IncomeEntity income = incomeList.get(position); //с позиции начинаем передавать данные
        holder.name.setText(income.getType().getTypeName()); //присвоили имя
        holder.income.setText(income.cost); //цену
        holder.date.setText(income.date);//дату
        if (income.getAccount() != null) {
            holder.account.setText(income.getAccount().getAccountName());
        }


        holder.selectedOverlay.setVisibility(isSelected(position) ? View.VISIBLE : View.INVISIBLE);
        //если выделено показываем, и наоборот
    }

    @Override
    public int getItemCount() {
        return incomeList.size();
    }

    private void removeItem(int position) {
        removeIncomes(position);
        notifyItemRemoved(position);
    }

    private void removeIncomes(int position) {
        if (incomeList.get(position) != null) {
            incomeList.get(position).delete();
            incomeList.remove(position);
        }

    }


    public void removeItems(List<Integer> positions) {
        Collections.sort(positions, new Comparator<Integer>() {
            @Override
            public int compare(Integer lhs, Integer rhs) {
                return rhs - lhs;
            }
        } );
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


    class IncomeHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView name;//задали все вьюхи для вывода полей
        TextView income;
        TextView date;

        TextView account;
        View selectedOverlay;
        private ClickListener clickListener;


        public IncomeHolder(View itemView, ClickListener clickListener) {//конструктор
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.income_item_income_name);//имя передали во вьюху в фрагменте
            income = (TextView) itemView.findViewById(R.id.income_item_income_price);//цену
            date = (TextView) itemView.findViewById(R.id.income_item_date);//дату

            account = (TextView) itemView.findViewById(R.id.income_item_account) ;
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
