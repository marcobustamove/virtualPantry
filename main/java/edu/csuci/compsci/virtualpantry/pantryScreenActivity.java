package edu.csuci.compsci.virtualpantry;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class pantryScreenActivity extends AppCompatActivity {

    private Button closePantryButton;
    private Button fruitsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry_screen);

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
                enterFruitItemScreen();
            }
        });
    }
    public void goHomeScreen()
    {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }
    public void enterFruitItemScreen()
    {
        Intent intent = new Intent(this, fruitScreenActivity.class);
        startActivity(intent);
    }
}
