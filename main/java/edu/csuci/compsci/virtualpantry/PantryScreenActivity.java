package edu.csuci.compsci.virtualpantry;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PantryScreenActivity extends AppCompatActivity {

    private Button closePantryButton;
    private Button fruitsButton;
    private Button dairyButton;
    private Button grainsButton;
    private Button meatsButton;
    private Button vegetablesButton;
    private Button condimentsButton;
    private Button oilsButton;
    private Button snacksButton;
    private Button fishButton;
    private Button frozenButton;
    private Button hygieneButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry_screen);

        final String pantryUUID = this.getIntent().getStringExtra("EXTRA_PANTRY_UUID");
        System.out.println(pantryUUID);

        closePantryButton = (Button) findViewById(R.id.closepantry);
        closePantryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goHomeScreen();

            }
        });

        fruitsButton = (Button) findViewById(R.id.fruits);
        fruitsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterItemScreen(pantryUUID, "FRUITS");
            }
        });

        dairyButton = (Button) findViewById(R.id.dairy);
        dairyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterItemScreen(pantryUUID, "DAIRY");
            }
        });

        grainsButton = (Button) findViewById(R.id.grains);
        grainsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterItemScreen(pantryUUID, "GRAINS");
            }
        });

        meatsButton = (Button) findViewById(R.id.meat);
        meatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterItemScreen(pantryUUID, "MEATS");
            }
        });

        vegetablesButton = (Button) findViewById(R.id.vegies);
        vegetablesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterItemScreen(pantryUUID, "VEGETABLES");
            }
        });

        condimentsButton = (Button) findViewById(R.id.condiments);
        condimentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterItemScreen(pantryUUID, "CONDIMENTS");
            }
        });

        oilsButton = (Button) findViewById(R.id.oils);
        oilsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterItemScreen(pantryUUID, "OILS");
            }
        });

        snacksButton = (Button) findViewById(R.id.snacks);
        snacksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterItemScreen(pantryUUID, "SNACKS");
            }
        });

        fishButton = (Button) findViewById(R.id.fish);
        fishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterItemScreen(pantryUUID, "FISH");
            }
        });

        frozenButton = (Button) findViewById(R.id.frozen);
        frozenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterItemScreen(pantryUUID, "FROZEN");
            }
        });

        hygieneButton = (Button) findViewById(R.id.grains);
        hygieneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterItemScreen(pantryUUID, "HYGIENE");
            }
        });

        
    }
    public void goHomeScreen()
    {
        Intent intent = new Intent(this, HomeScreenActivity.class);
        startActivity(intent);
    }
    public void enterItemScreen(String pantryUUID, String category)
    {
        Intent intent = new Intent(this, ItemScreenActivity.class);
        intent.putExtra("EXTRA_PANTRY_UUID", pantryUUID);
        intent.putExtra("EXTRA_PANTRY_CATEGORY", category);
        startActivity(intent);
    }
}