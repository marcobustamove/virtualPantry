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

import java.util.ArrayList;
import java.util.Calendar;

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

        boolean inEditMode = getArguments().getBoolean("EDIT_ITEM");
        final ArrayList<String> itemDetails = getArguments().getStringArrayList("NAME_UUID_DATE");

        newItemName = v.findViewById(R.id.AddItemName);
        expirationDate = v.findViewById(R.id.AddItemExpirationPicker);
        datePickerLayout = v.findViewById(R.id.ItemExpirationDate_Layout);
        datePickerLayout.setVisibility(View.GONE);
        Calendar date = Calendar.getInstance();
        // reset hour, minutes, seconds and millis
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        expirationDate.setMinDate(date.getTimeInMillis());

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

        if(inEditMode)
        {
            dialog.setTitle(R.string.Edit_Items);

            String[] expDate = itemDetails.get(2).split("/");
            newItemName.setText(itemDetails.get(0));
            if(expDate.equals("0/0/0"))
            {
                expirationSwitch.setChecked(false);
            }
            else
            {
                expirationSwitch.setChecked(true);
                String expYear = expDate[0];
                String expMonth = expDate[1];
                String expDay = expDate[2];

                expirationDate.updateDate(Integer.parseInt(expYear), Integer.parseInt(expMonth)-1, Integer.parseInt(expDay));
            }



            dialog.setPositiveButton(R.string.edit_item_details, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    if(expirationSwitch.isChecked())
                    {
                        listener.editItem(newItemName.getText().toString(), expirationSwitch.isChecked(), expirationDate.getMonth(), expirationDate.getDayOfMonth(), expirationDate.getYear(), itemDetails.get(1));
                    }
                    else
                    {
                        listener.editItem(newItemName.getText().toString(), expirationSwitch.isChecked(), -1, 0, 0, itemDetails.get(1));
                    }
                }
            });
        }
        else
        {
            dialog.setTitle(R.string.addItemTitle)
                    .setPositiveButton(R.string.addItemDone, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if(expirationSwitch.isChecked())
                            {
                                listener.AddItem(newItemName.getText().toString(), expirationSwitch.isChecked(), expirationDate.getMonth(), expirationDate.getDayOfMonth(), expirationDate.getYear());
                            }
                            else
                            {
                                listener.AddItem(newItemName.getText().toString(), expirationSwitch.isChecked(), -1, 0, 0);
                            }

                        }});
        }


        dialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
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
        void editItem(String itemName, Boolean expirable, int expirationMonth, int expirationDay, int expirationYear, String itemUUID);
    }
}
