package com.hat_dtu.volunteercommunity.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.transition.TransitionManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hat_dtu.volunteercommunity.R;
import com.hat_dtu.volunteercommunity.activity.MainActivity;
import com.hat_dtu.volunteercommunity.app.AppConfig;
import com.hat_dtu.volunteercommunity.model.Place;

import java.util.ArrayList;

/**
 * Created by paino on 4/18/2017.
 */

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.MyViewHolder> {

    private static final String TAG = PlacesAdapter.class.getSimpleName();
    private ArrayList<Place> places;
    private ArrayList<Place> filteredPlaceList;
    private int mExpandedPosition = -1;
    private Context context;
    private LayoutInflater mInflater;

    public PlacesAdapter(ArrayList<Place> places, Context context) {
        this.places = places;
        this.context = context;
        this.filteredPlaceList = places;

        mInflater = LayoutInflater.from(context);
    }

    @Override
    public PlacesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item_all, parent, false);
        PlacesAdapter.MyViewHolder viewHolder = new PlacesAdapter.MyViewHolder(itemView);
        return viewHolder;
    }
    public void setFilter(ArrayList<Place> filterList){
        places = new ArrayList<>();
        places.addAll(filterList);
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(final PlacesAdapter.MyViewHolder holder, final int position) {
        final Place place = places.get(position);
        holder.title.setText(place.getTitle());
        holder.address.setText(place.getAddress());
        holder.phone.setText(place.getPhone());
        holder.activity.setText(place.getActivity());

        final boolean isExpanded = position == mExpandedPosition;
        holder.activity.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.itemView.setActivated(isExpanded);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandedPosition = isExpanded ? -1 : position;
                TransitionManager.beginDelayedTransition(holder.cardView);
                notifyDataSetChanged();
            }
        });
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("LAT", place.getLat());
                bundle.putString("LNG", place.getLng());
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("MOVING", bundle);
                context.startActivity(intent);
                AppConfig.isMove = true;
            }
        });


    }


    @Override
    public int getItemCount() {
        return places.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title, address, phone, activity;
        private ImageButton btn;
        public CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.my_card_view);
            title = (TextView) itemView.findViewById(R.id.tv_a_title);
            address = (TextView) itemView.findViewById(R.id.tv_a_address);
            phone = (TextView) itemView.findViewById(R.id.tv_a_phone);
            activity = (TextView) itemView.findViewById(R.id.tv_a_activity);
            btn = (ImageButton) itemView.findViewById(R.id.btn_location);

        }

    }

}
