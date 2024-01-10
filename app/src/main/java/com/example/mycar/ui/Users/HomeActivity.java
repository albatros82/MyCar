package com.example.mycar.ui.Users;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.mycar.Enum.CarType;
import com.example.mycar.Prevalent.Prevalent;
import com.example.mycar.R;

import com.example.mycar.Service.Admin.AdminCapabilities;
import com.example.mycar.Service.Admin.NotificationService;
import com.example.mycar.ui.LoginActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import io.paperdb.Paper;




public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private LinearLayout dump_truck, bulldozer, excavator, truck_crane, truck_loader;
    private TextView userNameTextView;
    private ActivityResultLauncher<String> notificationPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Prevalent.currentOnLineUser.setActivity(HomeActivity.this);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(HomeActivity.this, OrdersActivity.class);
                startActivity(loginIntent);
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }

        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (Build.VERSION.SDK_INT >= 33) {
            notifyRegister();
            notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);
        }

        if(Prevalent.currentOnLineUser.getPermissions()){
            Menu menu = navigationView.getMenu();
            MenuItem nav_workOrders = menu.findItem(R.id.nav_work_order);
            nav_workOrders.setVisible(true);

            MenuItem nav_orders = menu.findItem(R.id.nav_orders);
            nav_orders.setVisible(false);
        }

        View headerView = navigationView.getHeaderView(0);
        userNameTextView = headerView.findViewById(R.id.user_profile_name);

        Prevalent.capabilities.setHeader(userNameTextView);
        userNameTextView.setText(Prevalent.currentOnLineUser.getName());
//        CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image);

        init();


        dump_truck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {goCarType(CarType.DUMP_TRUCK);}
        });

        bulldozer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCarType(CarType.BULLDOZER);
            }
        });

        excavator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCarType(CarType.EXCAVATOR);
            }
        });

        truck_crane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCarType(CarType.TRUCK_CRANE);
            }
        });

        truck_loader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCarType(CarType.TRUCK_LOADER);
            }
        });
    }

    private void goCarType(CarType type){
        if(Prevalent.currentOnLineUser.getPermissions()){
            Prevalent.capabilities.onClckCategory(HomeActivity.this, type, null);
            return;
        }
        Intent intent = new Intent(HomeActivity.this, CarTypeActivity.class);
        intent.putExtra("category", CarType.valueOfStr(type));
        startActivity(intent);
    }

    private void init(){
        dump_truck = findViewById(R.id.dump_truck);
        bulldozer = findViewById(R.id.bulldozer);
        excavator = findViewById(R.id.excavator);
        truck_crane = findViewById(R.id.truck_crane);
        truck_loader = findViewById(R.id.truck_loader);

    }

    protected void notifyRegister(){

        notificationPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    Prevalent.capabilities.flag_notification = isGranted; // Make sure you define this variable
                    if (!isGranted) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                                    showNotificationPermissionRationale(); // Define this method
                                } else {
                                    showSettingDialog(); // Define this method
                                }
                            }
                        }
                    }
                }
        );
    }

    private void showSettingDialog() {
        new MaterialAlertDialogBuilder(HomeActivity.this, com.google.android.material.R.style.MaterialAlertDialog_Material3)
                .setTitle("Разрешение на уведомление")
                .setMessage("Notification permission is required, Please allow notification permission from setting")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showNotificationPermissionRationale() {
        new MaterialAlertDialogBuilder(HomeActivity.this, com.google.android.material.R.style.MaterialAlertDialog_Material3)
                .setTitle("Alert")
                .setMessage("Notification permission is required, to show notification")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private Long TIMEMILLIS_CLOSE = System.currentTimeMillis();
    @Override
    public void onBackPressed() {
        if(TIMEMILLIS_CLOSE + 2000 > System.currentTimeMillis()){
            super.onBackPressed();
            moveTaskToBack(true);
        }
        else{
            Toast.makeText(HomeActivity.this, "Для выхода из приложения нажмите ещё раз", Toast.LENGTH_LONG).show();
        }
        TIMEMILLIS_CLOSE = System.currentTimeMillis();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.nav_orders){
            Intent loginIntent = new Intent(HomeActivity.this, OrdersActivity.class);
            startActivity(loginIntent);

        } else if(id == R.id.nav_categories){
            Intent loginIntent = new Intent(HomeActivity.this, HomeActivity.class);
            startActivity(loginIntent);

        } else if(id == R.id.nav_work_order){
            Intent loginIntent = new Intent(HomeActivity.this, OrdersActivity.class);
            startActivity(loginIntent);

        } else if(id == R.id.nav_work_products){
            Intent loginIntent = new Intent(HomeActivity.this, HomeActivity.class);
            startActivity(loginIntent);

        }  else if(id == R.id.nav_settings){
            Intent loginIntent = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(loginIntent);

        } else if (id == R.id.nav_logout) {
            Paper.book().destroy();
            Intent loginIntent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            Intent myIntent = new Intent(this, NotificationService.class);
            stopService(myIntent);
            finish();
        }
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

}