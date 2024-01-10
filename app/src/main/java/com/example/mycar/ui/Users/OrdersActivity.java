
package com.example.mycar.ui.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.mycar.Model.Order;
import com.example.mycar.Prevalent.Orders;
import com.example.mycar.Prevalent.Prevalent;
import com.example.mycar.R;

import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    ImageView imageView;
    RecyclerView orders_mass;
    OrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(Prevalent.currentOnLineUser.getPermissions())
            Prevalent.capabilities.incrementNewOrders();
        Prevalent.currentOnLineUser.setActivity(OrdersActivity.this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        ProgressDialog loadingBar = new ProgressDialog(OrdersActivity.this);
        loadingBar.setTitle("Загрузка данных");
        loadingBar.setMessage("Пожалуйста подождите");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        adapter = new OrderAdapter();
        Prevalent.capabilities.getOrdersUser(OrdersActivity.this, loadingBar, Prevalent.currentOnLineUser.getPhone(), adapter);

        init();
        orders_mass.setAdapter(adapter);
        orders_mass.setLayoutManager(new LinearLayoutManager(OrdersActivity.this, RecyclerView.VERTICAL, false));

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void init(){
        orders_mass = findViewById(R.id.orders_mass);
        imageView = findViewById(R.id.imageView);
    }

    @Override
    public void onBackPressed() {
        if(!isFinishing()) {
            Intent intent = new Intent(OrdersActivity.this, HomeActivity.class);
            finish();

            startActivity(intent);
        }
        super.onBackPressed();
    }

    public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

        private List<Order> ordersCurr;
        public OrderAdapter() {
            ordersCurr = Prevalent.currentOnLineUser.getPermissions() ? Orders.allOrder : Orders.myOrder;
        }

        @NonNull
        @Override
        public OrderAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_user, parent, false);
            return new OrderAdapter.OrderViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull OrderAdapter.OrderViewHolder holder, int position) {
            Order order = ordersCurr.get(position);
            holder.bind(order);
        }

        @Override
        public int getItemCount() {
            return ordersCurr.size();
        }
        public class OrderViewHolder extends RecyclerView.ViewHolder{

            TextView order_status, order_time_send, order_car_price, order_car_name, order_status_info, order_phone, order_phone_name, comment;
            Button plus, minus;
            View view;

            CardView cardview;

            public OrderViewHolder(@NonNull View itemView) {
                super(itemView);
                order_status = itemView.findViewById(R.id.order_status);
                order_time_send = itemView.findViewById(R.id.order_time_send);
                order_car_price = itemView.findViewById(R.id.order_car_price);
                order_car_name = itemView.findViewById(R.id.order_car_name);
                order_status_info = itemView.findViewById(R.id.order_status_info);
                order_phone = itemView.findViewById(R.id.order_phone);
                order_phone_name = itemView.findViewById(R.id.order_phone_name);
                comment = itemView.findViewById(R.id.order_comment);
                cardview = itemView.findViewById(R.id.cardview);

                plus = itemView.findViewById(R.id.plus);
                minus = itemView.findViewById(R.id.minus);
                view = itemView.findViewById(R.id.view3);
            }

            public void bind(Order order) {
                order_time_send.setText("Создание заявки: " + order.getTime());
                order_car_price.setText("Цена: " + order.getProductPrice());
                order_car_name.setText("Тип/Название: " + order.getCategory() + "/" + order.getProductName());
                order_phone.setText("Телефон: " + order.getPhone());
                if(order.getMy_wishes() != null)
                    comment.setText("Комментарий: " + order.getMy_wishes());
                else
                    comment.setVisibility(View.GONE);
                order_phone_name.setText(order.getNameUser());

                parseStatus(order);


            }

            private void parseStatus(Order order){

                if(Prevalent.currentOnLineUser.getPermissions()){
                    plus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Prevalent.capabilities.updateStatus(order.getTime(), order.getPhone(), "accept");
                        }
                    });

                    minus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Prevalent.capabilities.updateStatus(order.getTime(), order.getPhone(), "no-accept");
                        }
                    });

                }
                else{
                    plus.setVisibility(View.GONE);
                    minus.setVisibility(View.GONE);
                    view.setVisibility(View.GONE);
                }

                if(order.getStatus().equals("wait")){
                    order_status.setText("Заявка не рассмотрена");
                    order_status_info.setText("Ожидайте звонка");
                    cardview.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));


                }
                else if(order.getStatus().equals("accept")){
                    order_status.setTextColor(Color.rgb(75, 210, 60));
                    order_status_info.setTextColor(Color.rgb(75, 210, 60));
                    order_status.setText("Одобрена аренда");
                    order_status_info.setText("Будьте аккуратны");
                    cardview.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey_my)));
                }
                else{
                    order_status.setTextColor(Color.BLACK);
                    order_status_info.setTextColor(Color.RED);
                    order_status.setText("Аренда отклонена");
                    cardview.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey_my)));
                    order_status_info.setText("Свяжитесь с тех. поддержкой\n по телефону 8-923-464-9753 ");
                }
            }
        }
    }

}