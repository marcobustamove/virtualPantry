package edu.csuci.compsci.virtualpantry;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class homeactivity extends AppCompatActivity {

    private Button open_pantry;
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

    }

    public void openPantry()
    {
        Intent intent = new Intent(this, pantryScreenActivity.class);
        startActivity(intent);
    }
}
