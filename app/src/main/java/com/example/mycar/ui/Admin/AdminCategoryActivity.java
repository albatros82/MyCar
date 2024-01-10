package com.example.mycar.ui.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.mycar.Prevalent.Prevalent;
import com.example.mycar.R;

public class AdminCategoryActivity extends AppCompatActivity {

    private LinearLayout dump_truck, bulldozer, excavator, truck_crane, truck_loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Prevalent.currentOnLineUser.setActivity(AdminCategoryActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);
        init();


        dump_truck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "dump_track");
                startActivity(intent);

            }
        });

        bulldozer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "bulldozer");
                startActivity(intent);

            }
        });

        excavator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "excavator");
                startActivity(intent);

            }
        });

        truck_crane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "truck_crane");
                startActivity(intent);

            }
        });

        truck_loader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "truck_loader");
                startActivity(intent);

            }
        });

    }
    private void init(){
        dump_truck = findViewById(R.id.dump_truck);
        bulldozer = findViewById(R.id.bulldozer);
        excavator = findViewById(R.id.excavator);
        truck_crane = findViewById(R.id.truck_crane);
        truck_loader = findViewById(R.id.truck_loader);
    }
}