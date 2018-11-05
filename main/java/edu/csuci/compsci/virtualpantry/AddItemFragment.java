package edu.csuci.compsci.virtualpantry;

import android.app.Dialog;
import android.app.FragmentManager;
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
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }

}
