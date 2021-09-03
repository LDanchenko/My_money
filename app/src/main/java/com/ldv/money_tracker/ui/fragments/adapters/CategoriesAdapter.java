package com.ldv.money_tracker.ui.fragments.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ldv.money_tracker.R;
import com.ldv.money_tracker.storage.entities.CategoryEntity;
import com.ldv.money_tracker.storage.entities.ExpenseEntity;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//Адаптер передает данные из таблицы "Категории" в RecycleView ВЕЗДЕ ГДЕ СПИСОК - НУЖЕН АДАПТЕР
public class CategoriesAdapter extends SelectableAdapter<CategoriesAdapter.CategoriesHolder> { //передали класс что нижечерез адаптер

    private List<CategoryEntity> categoriesList; //создали экземпляр списка
    private Context context;
    private ClickListener clickListener;

    public CategoriesAdapter(Context context, List<CategoryEntity> categoriesList, ClickListener clickListener ) {//конструктор
        this.context = context;
        this.categoriesList = categoriesList;
        this.clickListener = clickListener;
    }

    @Override
    public CategoriesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.categories_item, parent, false);//данные передаем в categories_item(карточка).xml
        return new CategoriesHolder(itemView, clickListener);
    }

    @Override
    public void onBindViewHolder(CategoriesHolder holder, int position) {
        CategoryEntity categories = categoriesList.get(position); //применили метод гет, получили данные из таблицы с такой то позиции
        holder.categories_name.setText(categories.name);//из гета в сет передали, присвоили имя тексвью

//       holder.categories_image.setImageResource(categories.getImage());
      // int url = categories.getImage(); //что быстрее работает - глайд или просто?

   Log.d("Entity", "" + categories.getImage());

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
        return categoriesList.size();
    }

    private void removeItem(int position) {
        removeCategories(position);
        notifyItemRemoved(position);
    }

    private void removeCategories(int position) {
        if (categoriesList.get(position) != null) {
            categoriesList.get(position).delete();
            categoriesList.remove(position);

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


    class CategoriesHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {//тут ищем  текст вью для вывода категори

        TextView categories_name;
       ImageView categories_image;
        View selectedOverlay;
        private ClickListener clickListener;


        public CategoriesHolder(View itemView, ClickListener clickListener) {//konstruktor
            super(itemView);
            categories_name = (TextView) itemView.findViewById(R.id.categories_item_categories_name);//нашли поле в текст вью
         //  categories_image = (ImageView) itemView.findViewById(R.id.categories_image);
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
