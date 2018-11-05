package edu.csuci.compsci.virtualpantry;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

public class CreatePantryFragment extends DialogFragment
{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_createpantry, null);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.create_pantry)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }
}
