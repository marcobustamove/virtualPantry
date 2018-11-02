package edu.csuci.compsci.virtualpantry;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.Random;

import database.ItemBaseHelper;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private String[] mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Random random = new Random();

    private Context mContext;
    private SQLiteDatabase mDatabase;

    // data is passed into the constructor
    MyRecyclerViewAdapter(Context context, String[] data){
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_item, parent, false);

        TextView innertext = view.findViewById(R.id.info_text);
        innertext.setBackgroundResource(0); //This line is possibly not necessary, experiment later
        innertext.setBackgroundResource(R.drawable.categorybutton);


        return new ViewHolder(view);

    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.myTextView.setText(mData[position]);
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.length;
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.info_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());

            TextView innertext = view.findViewById(R.id.info_text);

            int select = random.nextInt(4);
            switch(select)
            {
                case 0:
                    innertext.setBackgroundResource(0);
                    innertext.setBackgroundResource(R.drawable.mockitemfull);
                    break;
                case 1:
                    innertext.setBackgroundResource(0);
                    innertext.setBackgroundResource(R.drawable.mockitemlow);
                    break;
                case 2:
                    innertext.setBackgroundResource(0);
                    innertext.setBackgroundResource(R.drawable.mockitemempty);
                    break;
                case 3:
                    innertext.setBackgroundResource(0);
                    innertext.setBackgroundResource(R.drawable.mockitemexpired);
                    break;
            }

        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData[id];
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}