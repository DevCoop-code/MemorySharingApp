package com.androidgamedev.com.reminiscence.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.androidgamedev.com.reminiscence.R;
import com.androidgamedev.com.reminiscence.contact.ContactData;
import com.androidgamedev.com.reminiscence.contact.DBManager;
import com.androidgamedev.com.reminiscence.contact.DBSqlData;

public class WritingMemoryDialog extends DialogFragment
{
    Activity activity;

    public static WritingMemoryDialog newInstance(String imagepath, int orientation)
    {
        WritingMemoryDialog dialog = new WritingMemoryDialog();
        Bundle args = new Bundle();
        args.putString("imagepath", imagepath);
        args.putInt("orientation", orientation);

        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final String imagepath = getArguments().getString("imagepath");
        final int orientation = getArguments().getInt("orientation");

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();

        View view = inflater.inflate(R.layout.write_dialog_activity, null);
        final EditText edit = view.findViewById(R.id.write_txt);

        builder.setView(view)
                .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String memory = edit.getText().toString();
                        //Log.v("WritingMemoryDialog", edit.getText().toString());

                        ContactData cData = new ContactData(imagepath, orientation+"", memory);

                        DBManager dbMgr = new DBManager(activity);
                        dbMgr.dbOpen();
                        dbMgr.insertData(DBSqlData.SQL_DB_INSERT_DATA, cData);
                        dbMgr.dbClose();
                    }
                })
                .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        //createDateListView(activity);
        this.activity = activity;
    }
}
