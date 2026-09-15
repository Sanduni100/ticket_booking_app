package lk.javainstitute.transpo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.Arrays;
import java.util.List;

import lk.javainstitute.transpo.R;

public class SeatAdapter extends BaseAdapter {

    Context context;
    int[] seatBooking;
    String strSeatNo;
    LayoutInflater inflater;


    public SeatAdapter(Context context, int[] seatBooking, String[] strSeatNo) {
        this.context = context;
        this.seatBooking = seatBooking;
        this.strSeatNo = String.valueOf(strSeatNo);
        this.inflater = inflater;

    }



    @Override
    public int getCount() {
        return seatBooking.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.seat_view, parent, false);
        ImageView btnSeat = convertView.findViewById(R.id.imgButton_seatID);
        btnSeat.setImageResource(seatBooking[position]);
    }
        return convertView;
    }

    public void setSelectedSeats(List<String> selectedSeats) {
    }
}
