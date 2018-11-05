package edu.csuci.compsci.virtualpantry;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

public class homeactivity extends AppCompatActivity {

    private Button open_pantry;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;


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

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);



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
