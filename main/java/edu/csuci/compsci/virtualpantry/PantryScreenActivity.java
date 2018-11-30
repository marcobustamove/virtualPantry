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


    }
    public void enterItemScreen(String pantryUUID, String category)
    {
        Intent intent = new Intent(this, ItemScreenActivity.class);
        intent.putExtra("EXTRA_PANTRY_UUID", pantryUUID);
        intent.putExtra("EXTRA_PANTRY_CATEGORY", category);
        startActivity(intent);
    }
}