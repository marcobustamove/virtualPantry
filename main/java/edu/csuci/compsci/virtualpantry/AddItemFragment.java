package edu.csuci.compsci.virtualpantry;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;

public class AddItemFragment extends DialogFragment
{

    private AddItemListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_additem, null);

        return new AlertDialog.Builder(getActivity(), R.style.AddItemDialogTheme)
                .setView(v)
                .setTitle(R.string.addItemTitle)

                .setPositiveButton(R.string.addItemDone, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditText newItemName = v.findViewById(R.id.AddItemName);
                        Switch expirationSwitch = v.findViewById(R.id.itemExpirationQuerySwitch);
                        DatePicker expirationDate = v.findViewById(R.id.AddItemExpirationPicker);

                        listener.AddItem(newItemName.toString(), expirationSwitch.isChecked(), expirationDate.getMonth(), expirationDate.getDayOfMonth(), expirationDate.getYear());

                        //System.out.println("Item name: " + newItemName.getText().toString()); //Test print functions
                        //System.out.println("Value of expiration switch: " + expirationSwitch.isChecked());
                        //System.out.println("Expiration Date: " + expirationDate.getMonth() + "/" + expirationDate.getDayOfMonth() + "/" + expirationDate.getYear());
                    }})

                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddItemFragment.this.getDialog().cancel();
                    }})

                .create();
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
