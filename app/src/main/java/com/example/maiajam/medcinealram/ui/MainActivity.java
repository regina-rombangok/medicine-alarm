package com.example.maiajam.medcinealram.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.maiajam.medcinealram.data.model.Medcine;
import com.example.maiajam.medcinealram.adapter.MedicneAdapter;
import com.example.maiajam.medcinealram.data.sql.Mysql;
import com.example.maiajam.medcinealram.R;
import com.example.maiajam.medcinealram.util.prefrenceSetting;

import java.util.ArrayList;
import java.util.List;

import static com.example.maiajam.medcinealram.helper.HelperMethodes.rateTheApp;
import static com.example.maiajam.medcinealram.helper.HelperMethodes.shareTheApp;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Mysql db;
    List<Medcine> listMed;
    RecyclerView recyclerView;
    TextView tV_emptyList, tv_emptyList_startAdding;
    RecyclerView.LayoutManager layoutManager;
    SharedPreferences pref;

    MedicneAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.title_activity_add__medcine));

        db = new Mysql(getBaseContext());
        listMed = new ArrayList<>();
        listMed = db.allMedcine();


        recyclerView = (RecyclerView) findViewById(R.id.RcView);
        tV_emptyList = (TextView) findViewById(R.id.Main_TV_WelcomeEmptyList);
        tv_emptyList_startAdding = (TextView) findViewById(R.id.Main_TV_WelcomeEmptyList_startAdding);


        if (listMed.isEmpty()) {
            tV_emptyList.setVisibility(View.VISIBLE);
            tv_emptyList_startAdding.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tV_emptyList.setVisibility(View.GONE);
            tv_emptyList_startAdding.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        PreferenceManager.setDefaultValues(this, R.xml.setting, false);
        pref = PreferenceManager.getDefaultSharedPreferences(this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        adapter = new MedicneAdapter(listMed, getBaseContext());
        recyclerView.setAdapter(adapter);
        LinearLayoutManager llayoutManager = new LinearLayoutManager(this);
        llayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llayoutManager);

        adapter.notifyDataSetChanged();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), AddMedcine.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();


        db = new Mysql(getBaseContext());
        listMed = new ArrayList<>();
        listMed = db.allMedcine();


        recyclerView = (RecyclerView) findViewById(R.id.RcView);

        adapter = new MedicneAdapter(listMed, getBaseContext());
        recyclerView.setAdapter(adapter);
        LinearLayoutManager llayoutManager = new LinearLayoutManager(this);
        llayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llayoutManager);


        if (listMed.isEmpty()) {
            tV_emptyList.setVisibility(View.VISIBLE);
            tv_emptyList_startAdding.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tV_emptyList.setVisibility(View.GONE);
            tv_emptyList_startAdding.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }


        adapter.notifyDataSetChanged();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Mymedcine) {

        } else if (id == R.id.nav_setting) {
            openSetting();
        } else if (id == R.id.nav_share) {
            shareTheApp(getBaseContext());
        } else if (id == R.id.nav_rate) {
            rateTheApp(getBaseContext());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openSetting() {

        startActivity(new Intent(getBaseContext(), prefrenceSetting.class));
        pref.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

            }
        });
    }
}
