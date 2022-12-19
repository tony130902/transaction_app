package com.example.android.transactioncalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    TextView rupeeSign;
    EditText amountField , messageField;
    ImageView sendImg;
    ArrayList<transactionData> list = new ArrayList<>();
    transactionAdapter adapter = new transactionAdapter(this , list);
    boolean positive = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        loadData();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new transactionAdapter(this , list));

        rupeeSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeSign();
            }
        });

        sendImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (amountField.getText().toString().isEmpty()){
                    amountField.setError("Enter Valid Amount! ");
                    return;
                }
                if (messageField.getText().toString().isEmpty()){
                    messageField.setError("Enter Message ");
                    return;
                }

                int amount = Integer.parseInt(amountField.getText().toString().trim());
                String message = messageField.getText().toString();
                sendTransaction(amount , message , positive);
                amountField.setText("");
                messageField.setText("");

            }
        });
    }

    public void changeSign(){
        if(positive){
            rupeeSign.setText("-₹");
            rupeeSign.setTextColor(Color.parseColor("#F30909"));
            positive = false;
        }
        else{
            rupeeSign.setText("+₹");
            rupeeSign.setTextColor(Color.parseColor("#29D631"));
            positive = true;
        }
    }

    private void sendTransaction(int amount , String message , boolean positive){
        list.add(new transactionData(amount , message , positive));
        adapter.notifyDataSetChanged();
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
    }

    public void initViews(){
        recyclerView = findViewById(R.id.ex_recycleview);
        rupeeSign = findViewById(R.id.ex_sign);
        amountField = findViewById(R.id.ex_amount);
        messageField = findViewById(R.id.ex_message);
        sendImg = findViewById(R.id.ex_send);
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = getSharedPreferences("com.cs.ec",MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString("transactions",json);
        editor.apply();
    }
}