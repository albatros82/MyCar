package com.example.mycar.Service.Admin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycar.Enum.CarType;
import com.example.mycar.Model.Order;
import com.example.mycar.Prevalent.Orders;
import com.example.mycar.Service.User.UserCapabilities;
import com.example.mycar.ui.Admin.AdminAddNewProductActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminCapabilities extends UserCapabilities{
    private Activity activity;
    @Override
    public void onClckCategory(Activity activity, CarType category, RecyclerView.Adapter adapter) {
        Intent intent = new Intent(activity, AdminAddNewProductActivity.class);
        intent.putExtra("category", CarType.valueOfStr(category));
        activity.startActivity(intent);
    }

    public AdminCapabilities(Activity activity){
        this.activity = activity;


        Intent myIntent = new Intent(activity, NotificationService.class);
        activity.startService(myIntent);
    }

    @Override
    public void incrementNewOrders() {
        incrementinfo.setValue(0);
    }

    @Override
    public void changeYourselfData(Activity activity, String name, String phone, String address) {
        super.changeYourselfData(activity, name, phone, address);
    }

    @Override
    public void updateStatus(String date, String phone, String answer) {
        orderRef.child(phone).child(date).child("status").setValue(answer);
    }

    @Override
    public void getOrdersUser(Activity activity, ProgressDialog loadingBar, String phone, RecyclerView.Adapter adapter) {

        this.activity = activity;
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Orders.allOrder.clear();
                for(DataSnapshot peopleItem: snapshot.getChildren()) {
                    for (DataSnapshot orderItem : peopleItem.getChildren()) {
                        Order order = orderItem.getValue(Order.class);
                        order.setTime(orderItem.getKey());
                        order.setPhone(peopleItem.getKey());
                        Orders.allOrder.add(order);
                    }
                }

                loadingBar.dismiss();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
