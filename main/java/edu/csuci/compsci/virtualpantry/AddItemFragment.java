package edu.csuci.compsci.virtualpantry;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;

public class AddItemFragment extends DialogFragment
{

    private AddItemListener listener;
    private EditText newItemName;
    private Switch expirationSwitch;
    private DatePicker expirationDate;
    private LinearLayout datePickerLayout;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_additem, null);

        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity(), R.style.AddItemDialogTheme);
        dialog.setView(v);

        newItemName = v.findViewById(R.id.AddItemName);
        expirationDate = v.findViewById(R.id.AddItemExpirationPicker);
        datePickerLayout = v.findViewById(R.id.ItemExpirationDate_Layout);
        datePickerLayout.setVisibility(View.GONE);


        expirationSwitch = v.findViewById(R.id.itemExpirationQuerySwitch);
        expirationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    datePickerLayout.setVisibility(View.VISIBLE);
                }
                else
                {
                    datePickerLayout.setVisibility(View.GONE);
                }
            }
        });


        dialog.setTitle(R.string.addItemTitle)
        .setPositiveButton(R.string.addItemDone, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if(expirationSwitch.isChecked())
                {
                    listener.AddItem(newItemName.getText().toString(), expirationSwitch.isChecked(), expirationDate.getMonth(), expirationDate.getDayOfMonth(), expirationDate.getYear());
                }
                else
                {
                    listener.AddItem(newItemName.getText().toString(), expirationSwitch.isChecked(), 0, 0, 0);
                }

            }})
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddItemFragment.this.getDialog().cancel();
                    }});

        return dialog.create();
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        listener = (AddItemListener) context;
    }

    public interface AddItemListener
    {
        void AddItem(String newItemName, Boolean expirable, int expirationMonth, int expirationDay, int expirationYear);
    }
}
