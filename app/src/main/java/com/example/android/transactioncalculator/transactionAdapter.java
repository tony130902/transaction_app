package com.example.android.transactioncalculator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class transactionAdapter extends RecyclerView.Adapter<transactionAdapter.viewHolder> {

    private ArrayList<transactionData> transactionDataList ;
    private Context context;

    public transactionAdapter(Context context , ArrayList<transactionData> transactionDataList){
        this.transactionDataList = transactionDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.transaction_item , parent , false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        int adapterPosition = holder.getAdapterPosition();

        String message = transactionDataList.get(adapterPosition).getMessage();
        holder.msg.setText(message);

        String amount = Integer.toString(transactionDataList.get(adapterPosition).getAmount());
        holder.amt.setText(amount);
    }

    @Override
    public int getItemCount() {
        if (transactionDataList == null){
            return 0;
        }
        return transactionDataList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        TextView amt , msg;
        ImageView dlt;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            amt = itemView.findViewById(R.id.tv_amount);
            msg = itemView.findViewById(R.id.tv_message);
            dlt = itemView.findViewById(R.id.tv_delete);
        }
    }
}
