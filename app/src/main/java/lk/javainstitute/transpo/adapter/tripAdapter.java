package lk.javainstitute.transpo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import lk.javainstitute.transpo.ChooseYourSeatActivity;
import lk.javainstitute.transpo.R;
import lk.javainstitute.transpo.model.trip;

public class tripAdapter extends RecyclerView.Adapter<tripAdapter.MyViewHolder> {

    Context context;
    TextView tvBookingTPass, tvBookingDate;
    ArrayList<trip> tripArrayList;
    String strGetEmail, strGetName;
    Intent intent;

    public tripAdapter(Context context, ArrayList<trip> tripArrayList, TextView tvBookingTPass, TextView tvBookingDate, String strGetEmail, String strGetName) {
        this.context = context;
        this.tripArrayList = tripArrayList;
        this.tvBookingTPass = tvBookingTPass;
        this.tvBookingDate = tvBookingDate;
        this.strGetEmail = strGetEmail;
        this.strGetName = strGetName;
    }


    @NonNull
    @Override
    public tripAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.activity_ticket_book, parent, false);
        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull tripAdapter.MyViewHolder holder, int position) {

        trip trip = tripArrayList.get(position);
        holder.tv_rTime.setText(trip.getrTime());
        holder.tv_tTime.setText(trip.gettTime());
        holder.tv_Pick.setText(trip.getPick());
        holder.tv_Drop.setText(trip.getDrop());
        holder.tv_Price.setText(trip.getPrice());
        intent = ((Activity) context).getIntent();


        holder.btnBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            String strTPass = tvBookingTPass.getText().toString();
            String strDate = tvBookingDate.getText().toString();
            strGetEmail = intent.getStringExtra("email");
            strGetName = intent.getStringExtra("name");
            intent = new Intent(v.getContext(), ChooseYourSeatActivity.class);
            intent.putExtra("pick", trip.getPick().toString());
            intent.putExtra("drop", trip.getDrop().toString());
            intent.putExtra("time", trip.getrTime().toString());
            intent.putExtra("price", trip.getPrice().toString());
            intent.putExtra("pass", strTPass);
            intent.putExtra("date", strDate);
            intent.putExtra("email", strGetEmail);
            intent.putExtra("name", strGetName);
            context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {

        return tripArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_rTime, tv_tTime, tv_Pick, tv_Drop, tv_Price;
        Button btnBooking;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            tv_rTime = itemView.findViewById(R.id.rvTrip_rTimeID);
            tv_tTime = itemView.findViewById(R.id.rvTrip_tTimeID);
            tv_Pick = itemView.findViewById(R.id.rvTrip_PickID);
            tv_Drop = itemView.findViewById(R.id.rvTrip_tDropID);
            tv_Price = itemView.findViewById(R.id.rvTrip_PriceID);
            btnBooking = itemView.findViewById(R.id.btn_recyBookID);
        }
    }
}
