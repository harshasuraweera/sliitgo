package com.teamevox.freshfred;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class TodayIncomeForShop extends AppCompatActivity {

    TextView totalIncomeHistory_itemCount, totalIncomeHistory_totalEarnings, totalIncomeHistory_forRiders, totalIncomeHistory_forYou;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_income_for_shop);

        totalIncomeHistory_itemCount = findViewById(R.id.totalIncomeHistory_itemCount);
        totalIncomeHistory_totalEarnings = findViewById(R.id.totalIncomeHistory_totalEarnings);
        totalIncomeHistory_forRiders = findViewById(R.id.totalIncomeHistory_forRiders);
        totalIncomeHistory_forYou = findViewById(R.id.totalIncomeHistory_forYou);



        FirebaseDatabase database99 = FirebaseDatabase.getInstance();
        DatabaseReference getCommissionRateNow = database99.getReference("totalEarningHistory");

        getCommissionRateNow.addValueEventListener(new ValueEventListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {

                totalIncomeHistory_itemCount.setText("Total Item Delivered : " + Objects.requireNonNull(snapshot.child("totalItemDelivered").getValue()).toString());
                totalIncomeHistory_totalEarnings.setText("Your Total Earnings : " + Objects.requireNonNull(snapshot.child("totalCostOfDeliveredItems").getValue()).toString() + " LKR");
                totalIncomeHistory_forRiders.setText(Objects.requireNonNull(snapshot.child("totalRiderCommissio