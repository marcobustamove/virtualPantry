package edu.csuci.compsci.virtualpantry;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class CreatePantryFragment extends AppCompatDialogFragment
{
    private EditText editTextPantryName;
    private CreatePantryListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_createpantry, null);

        editTextPantryName = view.findViewById(R.id.userpantryname);

        builder.setView(view)
                .setTitle(R.string.create_pantry)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        String pantryName = editTextPantryName.getText().toString();
                        listener.applyTexts(pantryName);
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        listener = (CreatePantryListener) context;
    }


    public interface CreatePantryListener
    {
        void applyTexts(String inputPantryName);
    }

}
