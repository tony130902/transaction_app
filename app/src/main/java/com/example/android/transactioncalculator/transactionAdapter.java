package com.example.android.transactioncalculator;

import static com.example.android.transactioncalculator.MainActivity.checkIfEmpty;
import static com.example.android.transactioncalculator.MainActivity.setBalance;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
        if(transactionDataList.get(holder.getAdapterPosition()).isPositive()){
            holder.amt.setTextColor(Color.parseColor("#29D631"));
            holder.amt.setText(" ₹ "+transactionDataList.get(holder.getAdapterPosition()).getAmount());
        }else{
            holder.amt.setTextColor(Color.parseColor("#F30909"));
            holder.amt.setText("-₹ "+transactionDataList.get(holder.getAdapterPosition()).getAmount());
        }

        // listner for delete image and function.
        holder.dlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // setting delete alert box.
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setCancelable(false)
                        .setTitle("Are you sure? The transaction will be deleted.")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                transactionDataList.remove(holder.getAdapterPosition());
                                dialogInterface.dismiss();
                                notifyDataSetChanged();
                                checkIfEmpty(getItemCount());
                                setBalance(transactionDataList);
                            }
                        })
                        .create();
                dialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        if (transactionDataList == null){
            return 0;
        }
        return transactionDataList.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder{
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
