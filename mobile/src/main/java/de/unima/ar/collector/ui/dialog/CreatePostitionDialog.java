package de.unima.ar.collector.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import de.unima.ar.collector.R;
import de.unima.ar.collector.controller.SQLDBController;
import de.unima.ar.collector.database.DatabaseHelper;
import de.unima.ar.collector.shared.database.SQLTableName;
import de.unima.ar.collector.util.SensorDataUtil;


/**
 * @author Fabian Kramm
 */
public class CreatePostitionDialog extends DialogFragment
{
    /*
     * (non-Javadoc)
     * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setTitle("Create Position");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.createactivitydialog, null);

        builder.setView(view);

        TextView tv = (TextView) view.findViewById(R.id.createactivitydialogtv1);

        tv.setVisibility(View.GONE);

        Spinner sp = (Spinner) view.findViewById(R.id.createactivitydialogspinner);

        sp.setVisibility(View.GONE);

        tv = (TextView) view.findViewById(R.id.createactivitydialogtv2);

        tv.setText("Position name:");

        builder.setPositiveButton(R.string.dialog_ok, null);

        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {

            }
        });

        final AlertDialog d = builder.create();

        d.setOnShowListener(new DialogInterface.OnShowListener()
        {

            @Override
            public void onShow(DialogInterface dialog)
            {

                Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener()
                {

                    @Override
                    public void onClick(View v)
                    {
                        EditText et = (EditText) view.findViewById(R.id.createactivitydialogedittext);

                        if(SensorDataUtil.checkName(et.getText().toString()) == null) {
                            String positionName = et.getText().toString().trim();

                            // Check if already exists
                            boolean exists = false;
                            ArrayList<String> positions = DatabaseHelper.getStringResultSet("SELECT name FROM " + SQLTableName.POSITIONS, new String[]{ });

                            for(int i = 0; i < positions.size(); i++) {
                                if(positions.get(i).toLowerCase(Locale.ENGLISH).equals(positionName.toLowerCase(Locale.ENGLISH))) {
                                    exists = true;
                                    break;
                                }
                            }

                            if(exists) {
                                Toast.makeText(d.getContext(), "Position already exists!", Toast.LENGTH_LONG).show();
                                return;
                            }

                            ContentValues newValues = new ContentValues();
                            newValues.put("name", positionName);

                            SQLDBController.getInstance().insert(SQLTableName.POSITIONS, null, newValues);
                            d.dismiss();
                        } else {
                            Toast.makeText(d.getContext(), SensorDataUtil.checkName(et.getText().toString()), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        return d;
    }
}