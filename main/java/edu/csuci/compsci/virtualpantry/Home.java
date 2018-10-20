package edu.csuci.compsci.virtualpantry;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

public class Home extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {

    MyRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        // data to populate the RecyclerView with
        String[] data = {"Apples", "Bananas", "Oranges", "Watermelon", "Peaches", "Kiwi", "Strawberries", "Grapes", "Avocado", "Pineapple", "Kiwano Melon", "Dragonfruit", "Tomato"};
        /* test array for displaying 150 grid items
        String data[] = new String[150];
        for(int i = 0; i < data.length; i++)
        {
            data[i] = Integer.toString(i+1);
        }
        */
        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvNumbers);
        int numberOfColumns = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new MyRecyclerViewAdapter(this, data);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.i("TAG", "You clicked number " + adapter.getItem(position) + ", which is at cell position " + position);
    }
}

