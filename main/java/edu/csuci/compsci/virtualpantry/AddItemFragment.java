package edu.csuci.compsci.virtualpantry;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

public class AddItemFragment extends DialogFragment
{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_additem, null);

        return new AlertDialog.Builder(getActivity(), R.style.AddItemDialogTheme)
                .setView(v)
                .setTitle(R.string.addItemTitle)
                .setPositiveButton(R.string.addItemDone, null)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddItemFragment.this.getDialog().cancel();
                    }})
                .create();
    }

}
