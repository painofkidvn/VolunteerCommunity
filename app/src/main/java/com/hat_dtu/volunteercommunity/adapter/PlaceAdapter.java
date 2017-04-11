package com.hat_dtu.volunteercommunity.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.transition.TransitionManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hat_dtu.volunteercommunity.R;
import com.hat_dtu.volunteercommunity.app.AppConfig;
import com.hat_dtu.volunteercommunity.app.AppController;
import com.hat_dtu.volunteercommunity.fragment.HomeFragment;
import com.hat_dtu.volunteercommunity.fragment.PlaceFragment;
import com.hat_dtu.volunteercommunity.model.Place;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by paino on 4/2/2017.
 */

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.MyViewHolder> {
    private static final String TAG = PlaceAdapter.class.getSimpleName();
    private ArrayList<Place> places;
    private int mExpandedPosition = -1;
    private Context context;
    public PlaceAdapter(ArrayList<Place> places, Context context){
        this.places = places;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Place place = places.get(position);
        holder.title.setText(place.getTitle());
        holder.address.setText(place.getAddress());
        holder.phone.setText(place.getPhone());
        holder.activity.setText(place.getActivity());

        final boolean isExpanded = position== mExpandedPosition;
        holder.activity.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.itemView.setActivated(isExpanded);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandedPosition = isExpanded ? -1:position;
                TransitionManager.beginDelayedTransition(holder.cardView);
                notifyDataSetChanged();
            }
        });
        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, holder.options);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.edit:
                                break;
                            case R.id.delete:

                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                                alertDialog.setCancelable(true);
                                alertDialog.setTitle("Delete Place!").setMessage("Are you sure you want to delete this Place?");
                                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        onDelete(holder.getAdapterPosition(), place.getId());

                                    }
                                });
                                alertDialog.show();
                                break;

                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });


    }
    public void removeAt(int position) {
        places.remove(position);
        notifyItemChanged(position);
    }
    private void onDelete(final int pos, final int id) {
        String tag_string_req = "req_delete_place";
        AppConfig.URL_PLACE_L = AppConfig.URL_PLACE_L+id;
        StringRequest strReq = new StringRequest(Request.Method.DELETE,
                AppConfig.URL_PLACE_L, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Create Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        removeAt(pos);
                        String message = jObj.getString("message");
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                        AppConfig.URL_PLACE_L = "http://slimapp.esy.es/slimapp/places/";


                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Creating Error: " + error.getMessage());
                Toast.makeText(context,
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                headers.put("Authorization", AppConfig.API_KEY);
                return headers;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    @Override
    public int getItemCount() {
        return places.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView title, address, phone, activity, options;
        public CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.card_view);
            title = (TextView)itemView.findViewById(R.id.tv_title);
            address = (TextView)itemView.findViewById(R.id.tv_address);
            phone = (TextView)itemView.findViewById(R.id.tv_phone);
            activity = (TextView)itemView.findViewById(R.id.tv_activity);
            options = (TextView)itemView.findViewById(R.id.tv_options);

        }

    }
}
