package com.example.fininfosoft;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import io.realm.RealmResults;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements OnPersonClickListener{
    private RealmResults<Person> data;
    private final OnPersonClickListener personClickListener;

    public void updateData(RealmResults<Person> newData) {
        data = newData;
        notifyDataSetChanged();
    }



    public MyAdapter(RealmResults<Person> data,OnPersonClickListener personClickListener) {
        this.data = data;
        this.personClickListener = personClickListener;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        Person person = data.get(position);
        holder.nameTextView.setText(person.getName());
        holder.ageTextView.setText(String.valueOf(person.getAge()));
        holder.cityTextView.setText(person.getCity());



    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onClick(int position) {

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView,ageTextView,cityTextView;
        public LinearLayout ll;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            ageTextView = itemView.findViewById(R.id.ageTextView);
            cityTextView = itemView.findViewById(R.id.cityTextView);
            ll = itemView.findViewById(R.id.root_ll);

            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (personClickListener!=null){
                        int pos = getAdapterPosition();
                        if (pos!= RecyclerView.NO_POSITION){
                            personClickListener.onClick(pos);
                        }
                    }
                }
            });



        }
    }
}
