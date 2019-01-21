package tech.alvarez.contacts.listedit;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tech.alvarez.contacts.R;
import tech.alvarez.contacts.data.db.entity.Person;
import tech.alvarez.contacts.utils.Util;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder> {

    private List<Person> mValues;
    private ListContract.OnItemClickListener mOnItemClickListener;

    // конструктор который принимает онАйтемклилЛисенер
    public PeopleAdapter(ListContract.OnItemClickListener onItemClickListener) {

        // новый список
        mValues = new ArrayList<>();
        mOnItemClickListener = onItemClickListener;
        //полученный онклин принимает в конструктор
    }

    //создаем вью холдер для ресайклера
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
        return new ViewHolder(view);
    }

    // создает связки между разметкой и данными
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.nameTextView.setText(mValues.get(position).name);
        holder.phoneTextView.setText(mValues.get(position).phone);

        holder.addressTextView.setText(holder.mItem.address);
        holder.emailTextView.setText(holder.mItem.email);
        holder.birthdayTextView.setText(Util.formatMin(holder.mItem.birthday));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.clickItem(holder.mItem);
            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mOnItemClickListener.clickLongItem(holder.mItem);
                return false;
            }
        });
    }

    // проверяем длину списка
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    // установи значения - получает список значений - получить новые значения персон и передать на обновление
    public void setValues(List<Person> values) {
        mValues = values;
        notifyDataSetChanged();
    }

    // внутренние поля сопоставляем с полями разметки
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView nameTextView;
        public final TextView phoneTextView;
        public final TextView emailTextView;
        public final TextView birthdayTextView;
        public final TextView addressTextView;
        public Person mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            nameTextView = (TextView) view.findViewById(R.id.nameTextView);
            phoneTextView = (TextView) view.findViewById(R.id.phoneTextView);
            emailTextView = (TextView) view.findViewById(R.id.emailTextView);
            birthdayTextView = (TextView) view.findViewById(R.id.birthdayTextView);
            addressTextView = (TextView) view.findViewById(R.id.addressTextView);
        }
    }


}
