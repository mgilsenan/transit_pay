package com.example.transitpay.Travel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.transitpay.R;

import java.util.List;

public class TripHistoryAdapter extends RecyclerView.Adapter<TripHistoryAdapter.TripViewHolder>{
    private Context myContext;
    private List<Trip> tripList;

    public TripHistoryAdapter(Context myContext, List<Trip> tripList) {
        this.myContext = myContext;
        this.tripList = tripList;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(myContext);
         View view = inflater.inflate(R.layout.trip_listview, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        Trip trip = tripList.get(position);
        holder.dateTextView.setText(trip.Date);
        holder.timeTextView.setText(trip.Time);
        holder.addressTextView.setText(trip.Address);

    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    class TripViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView, timeTextView, addressTextView;

        public TripViewHolder(@NonNull View itemView) {
            super(itemView);

            dateTextView = itemView.findViewById(R.id.dateTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
        }
    }
}
