package edu.csuci.compsci.virtualpantry;

import android.app.ActionBar;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.List;

public class homeactivity extends AppCompatActivity {

    private Button open_pantry;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private ArrayAdapter<String> mAdapter;
    private ListView mDrawerList;

    //CursorAdapter for binding to a sql
    ListView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        open_pantry = findViewById(R.id.openpantry);
        open_pantry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPantry();

            }
        });

        mDrawerLayout= (DrawerLayout)findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        listView = (ListView) findViewById(R.id.simpleListView);




        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        });

        addMenuItemInNavMenuDrawer();





        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*simpleList = (ListView)findViewById(R.id.simpleListView);
        ArrayAdapter<String>arrayAdapter = new ArrayAdapter<String>(this, R.layout.listview,R.id.textView,animalList);
        simpleList.setAdapter(arrayAdapter);*/


    }
    private void addMenuItemInNavMenuDrawer() {
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);

        Menu menu = navView.getMenu();
        //Menu submenu = menu.addSubMenu("New Super SubMenu");

        menu.add("Super Item1");
        menu.add("Super Item2");
        menu.add("Super Item3");

        navView.invalidate();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    public void openPantry()
    {
        Intent intent = new Intent(this, pantryScreenActivity.class);
        startActivity(intent);
    }
}
