package com.hat_dtu.volunteercommunity.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hat_dtu.volunteercommunity.R;
import com.hat_dtu.volunteercommunity.model.Charity;

import java.util.ArrayList;

/**
 * Created by paino on 4/2/2017.
 */

public class CharityAdapter extends RecyclerView.Adapter<CharityAdapter.MyViewHolder> {

    private ArrayList<Charity> charities;

    public CharityAdapter(ArrayList<Charity> charities){
        this.charities = charities;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Charity charity = charities.get(position);
        holder.title.setText(charity.getTitle());
        holder.address.setText(charity.getAddress());
        holder.phone.setText(charity.getPhone());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });

    }
    @Override
    public int getItemCount() {
        return charities.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView title, address, phone;
        public CardView cardView;
        public MyViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.card_view);
            title = (TextView)itemView.findViewById(R.id.tv_title);
            address = (TextView)itemView.findViewById(R.id.tv_address);
            phone = (TextView)itemView.findViewById(R.id.tv_phone);
        }
    }
}
