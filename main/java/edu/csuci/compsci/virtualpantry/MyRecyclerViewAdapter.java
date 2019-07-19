package edu.csuci.compsci.virtualpantry;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private ArrayList<String> mData;
    private ArrayList<String> mItemStatusList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    private Dialog myDialog;


    // data is passed into the constructor
    MyRecyclerViewAdapter(Context context, ArrayList<String> data, ArrayList<String> itemStatusList){
        this.mInflater = LayoutInflater.from(context);

        //pull item names from pertinent category in db into data
        this.mData = data;
        this.mItemStatusList = itemStatusList;
        myDialog = new Dialog(context);
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_item, parent, false);

        TextView innertext = view.findViewById(R.id.info_text);
        innertext.setBackgroundResource(R.drawable.itemname);

        view.setLongClickable(true);

        return new ViewHolder(view);

    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.myTextView.setText(mData.get(position));

        String status = mItemStatusList.get(position);

        switch(status)
        {
            case "1":
                holder.itemStatusImage.setBackgroundResource(R.drawable.fullstatus);
                break;
            case "2":
                holder.itemStatusImage.setBackgroundResource(R.drawable.lowstatus);
                break;
            case "3":
                holder.itemStatusImage.setBackgroundResource(R.drawable.emptystatus);
                break;
            case "4":
                holder.itemStatusImage.setBackgroundResource(R.drawable.expstatus);
                break;
        }
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView myTextView;
        LinearLayout itemStatusImage;

        ViewHolder(View itemView) {
            super(itemView);
            itemStatusImage = itemView.findViewById(R.id.item_button);
            myTextView = itemView.findViewById(R.id.info_text);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());

            TextView innertext = view.findViewById(R.id.info_text);
            LinearLayout itemStatus = view.findViewById(R.id.item_button);

            System.out.println("OnClick received");



            //functionality will be added here to change item status, or select multiple items

        }

        @SuppressLint("ClickableViewAccessibility")
        public boolean onLongClick(final View view){
            Button btnSetEmpty;
            Button btnSetLow;
            Button btnSetFull;
            Button btnDeleteItem;


            myDialog.setContentView(R.layout.fragment_change_item_status);
            btnSetEmpty = (Button) myDialog.findViewById(R.id.set_empty_button);
            btnSetLow = (Button) myDialog.findViewById(R.id.set_low_button);
            btnSetFull = (Button) myDialog.findViewById(R.id.set_full_button);
            btnDeleteItem = (Button) myDialog.findViewById(R.id.item_delete_button);

            btnSetEmpty.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN) {
                        return true;
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        mClickListener.itemModifyStatus(view, getAdapterPosition(), ItemScreenActivity.EMPTY );
                        myDialog.cancel();
                        return true;
                    }
                    itemStatusImage.setBackgroundResource(R.drawable.emptystatus);
                    return false;
                }
            });

            btnSetLow.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN) {
                        return true;
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        mClickListener.itemModifyStatus(view, getAdapterPosition(), ItemScreenActivity.LOW);
                        myDialog.cancel();
                        return true;
                    }
                    itemStatusImage.setBackgroundResource(R.drawable.lowstatus);
                    return false;
                }
            });

            btnSetFull.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN) {
                        return true;
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        mClickListener.itemModifyStatus(view, getAdapterPosition(), ItemScreenActivity.FULL);
                        myDialog.cancel();
                        return true;
                    }
                    itemStatusImage.setBackgroundResource(R.drawable.fullstatus);
                    return false;
                }
            });

            btnDeleteItem.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN) {
                        return true;
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        mClickListener.deleteItem(view, getAdapterPosition());
                        myDialog.cancel();
                        return true;
                    }
                    return false;
                }
            });

            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            myDialog.show();

            return false;
        }

    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
        void deleteItem(View view, int position);
        void itemModifyStatus(View view, int position, int newStatus);
    }
}