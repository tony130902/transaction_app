package com.example.android.transactioncalculator;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView ;
    TextView rupeeSign , app_name;
    public static TextView empty_textview ,balance_value_textview;
    EditText amountField , messageField;
    ImageView sendImg;
    ArrayList<transactionData> list = new ArrayList<>();
    transactionAdapter adapter = new transactionAdapter(this , list);
    boolean positive = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();  // for initiate the views.
        custom_actionbar_layout();  //for customise action bar.
        loadData();  // for saving data in sharedpref and loading data.
        checkIfEmpty(list.size());  // for empty list message.

        // setting recycleview.
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new transactionAdapter(this , list));

        // setting listner on money sign.
        rupeeSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeSign();
            }
        });

        // setting listner on send image.
        sendImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // setting error message if amount and message editText is empty.
                if (amountField.getText().toString().isEmpty() && messageField.getText().toString().isEmpty() ){
                    amountField.setError("Enter Valid Amount! ");
                    messageField.setError("Enter Message ");
                    return;
                }

                try {
                    int amount = Integer.parseInt(amountField.getText().toString().trim());
                    String message = messageField.getText().toString();
                    // passing the entered amount , message and money sign as arguments.
                    sendTransaction(amount , message , positive);
                    // calling setbalance method for setting total balance left in wallet balance.
                    setBalance(list);
                    // for empty list message
                    checkIfEmpty(list.size());

                    // for clearing feilds for further expence.
                    amountField.getText().clear();
                    messageField.getText().clear();

                } catch (NumberFormatException exception) {
                    exception.printStackTrace();
                }
            }
        });

        // setting listner in appname for refreshing the list.
        app_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.notifyDataSetChanged();
            }
        });

    }

    // Function for empty list message.
    public static void checkIfEmpty(int size) {
        if (size == 0){
            MainActivity.empty_textview.setVisibility(View.VISIBLE);
        }else{
            MainActivity.empty_textview.setVisibility(View.GONE);
        }
    }

    // Function for changing the money sign.
    public void changeSign(){
        if(positive){
            rupeeSign.setText("-₹");
            rupeeSign.setTextColor(Color.parseColor("#F30909"));
            positive = false;
        }
        else{
            rupeeSign.setText("₹");
            rupeeSign.setTextColor(Color.parseColor("#29D631"));
            positive = true;
        }
    }

    // Function for sending expence information and adding in the list.
    @SuppressLint("NotifyDataSetChanged")
    private void sendTransaction(int amount , String message , boolean positive){
        // adding data into list and calling constructor of transactionData(data as arguments).
        list.add(new transactionData(amount , message , positive));
        // notify the adapter for change in list.
        adapter.notifyDataSetChanged();
    }


    // Function To set Balance along with sign (spent(-) or received(+))
    public static void setBalance(ArrayList<transactionData> transactionList){
        int bal = calculateBalance(transactionList);
        if(bal<0)
        {
            balance_value_textview.setText("- ₹ " + bal * -1);
        }
        else {
            balance_value_textview.setText("₹ " + bal);
        }
    }

    // Function To Calculate Balance by iterating through all transactions.
    public static int calculateBalance(ArrayList<transactionData> transactionList)
    {
        int bal = 0;
        for(transactionData transaction : transactionList)
        {
            if(transaction.isPositive())
            {
                bal+=transaction.getAmount();
            }
            else {
                bal-=transaction.getAmount();
            }
        }
        return bal;
    }

    // Function for custom action bar.
    public void custom_actionbar_layout(){
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        getSupportActionBar().setElevation(0);
        View view = getSupportActionBar().getCustomView();
    }


    // Function for initate all the views.
    public void initViews(){
        recyclerView = findViewById(R.id.ex_recycleview);
        rupeeSign = findViewById(R.id.ex_sign);
        amountField = findViewById(R.id.ex_amount);
        messageField = findViewById(R.id.ex_message);
        sendImg = findViewById(R.id.ex_send);
        empty_textview = findViewById(R.id.Empty_textview);
        app_name = findViewById(R.id.expence_appname);
        balance_value_textview = findViewById(R.id.balance_value);
    }

    // To load data from shared preference
    private void loadData() {
        SharedPreferences pref = getSharedPreferences("com.cs.ec",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = pref.getString("transactions",null);
        Type type = new TypeToken<ArrayList<transactionData>>(){}.getType();
        if(json!=null)
        {
            list = gson.fromJson(json,type);
        }

        String new_balance = pref.getString("balance" , null);
        balance_value_textview.setText(new_balance);
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = getSharedPreferences("com.cs.ec",MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        String prev_balance = balance_value_textview.getText().toString();
        editor.putString("balance" , prev_balance);
        editor.putString("transactions",json);
        editor.apply();
    }
}