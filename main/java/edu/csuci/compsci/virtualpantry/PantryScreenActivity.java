package edu.csuci.compsci.virtualpantry;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PantryScreenActivity extends AppCompatActivity {

    private Button closePantryButton;

    private Button dairy;
    private Button grains;
    private Button meats;
    private Button fish;
    private Button frozen;
    private Button beverages;
    private Button vegetables;
    private Button fruits;
    private Button snacks;
    private Button oils;
    private Button condiments;
    private Button hygiene;
    private Button allItems;


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
                finish();

            }
        });

        dairy = (Button) findViewById(R.id.dairy);
        dairy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterItemScreen(pantryUUID, "DAIRY");
            }
        });

        grains = (Button) findViewById(R.id.grains);
        grains.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterItemScreen(pantryUUID, "GRAINS");
            }
        });

        meats = (Button) findViewById(R.id.meat);
        meats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterItemScreen(pantryUUID, "MEATS");
            }
        });

        fish = (Button) findViewById(R.id.fish);
        fish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterItemScreen(pantryUUID, "FISH");
            }
        });

        frozen = (Button) findViewById(R.id.frozen);
        frozen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterItemScreen(pantryUUID, "FROZEN");
            }
        });

        beverages = (Button) findViewById(R.id.beverages);
        beverages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterItemScreen(pantryUUID, "BEVERAGES");
            }
        });

        vegetables = (Button) findViewById(R.id.vegies);
        vegetables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterItemScreen(pantryUUID, "VEGETABLES");
            }
        });

        fruits = (Button) findViewById(R.id.fruits);
        fruits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterItemScreen(pantryUUID, "FRUITS");
            }
        });

        snacks = (Button) findViewById(R.id.snacks);
        snacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterItemScreen(pantryUUID, "SNACKS");
            }
        });

        oils = (Button) findViewById(R.id.oils);
        oils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterItemScreen(pantryUUID, "OILS");
            }
        });

        condiments = (Button) findViewById(R.id.condiments);
        condiments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterItemScreen(pantryUUID, "CONDIMENTS");
            }
        });

        hygiene = (Button) findViewById(R.id.hygiene);
        hygiene.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterItemScreen(pantryUUID, "HYGIENE");
            }
        });

        allItems = (Button) findViewById(R.id.allitems);
        allItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterItemScreen(pantryUUID, "ALL ITEMS");
            }
        });


        
    }
    public void enterItemScreen(String pantryUUID, String category)
    {
        Intent intent = new Intent(this, ItemScreenActivity.class);
        intent.putExtra("EXTRA_PANTRY_UUID", pantryUUID);
        intent.putExtra("EXTRA_PANTRY_CATEGORY", category);
        startActivity(intent);
    }
}