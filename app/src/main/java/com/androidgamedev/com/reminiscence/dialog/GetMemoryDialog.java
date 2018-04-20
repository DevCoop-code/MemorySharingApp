package com.androidgamedev.com.reminiscence.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidgamedev.com.reminiscence.R;
import com.androidgamedev.com.reminiscence.contact.DBManager;
import com.androidgamedev.com.reminiscence.contact.DBSqlData;

public class GetMemoryDialog extends DialogFragment
{
    int mNum;
    Activity activity;

    public static GetMemoryDialog newInstance(String imagePath)
    {
        GetMemoryDialog memoryDialog = new GetMemoryDialog();
        Bundle args = new Bundle();
        args.putString("IMAGEPATH", imagePath);

        memoryDialog.setArguments(args);

        return memoryDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        String imagePath = getArguments().getString("IMAGEPATH");
        String[] imagesPath = {imagePath};

        DBManager dbMgr = new DBManager(activity);
        dbMgr.dbOpen();
        String description = dbMgr.select(DBSqlData.SQL_DB_SELECT_DATA, imagesPath);
        dbMgr.dbClose();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(description)
                .setTitle("Memory")
                .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                })
                .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });
        return builder.create();
    }
    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        String imagePath = getArguments().getString("IMAGEPATH");
        String[] imagesPath = {imagePath};

        View v = inflater.inflate(R.layout.memory_dialog, container, false);
        TextView memory_container = v.findViewById(R.id.memory_txt);
        Button check_btn = v.findViewById(R.id.dialog_check_btn);

        //memory_container.setText("Happy Happy Happy Naru!!");
        DBManager dbMgr = new DBManager(activity);
        dbMgr.dbOpen();
        String description = dbMgr.select(DBSqlData.SQL_DB_SELECT_DATA, imagesPath);
        dbMgr.dbClose();

        memory_container.setText(description);
        check_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dismiss();
                Toast.makeText(getContext(),"당신의 아름다운 추억을 공유해주세요",Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }
    */

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        //createDateListView(activity);
        this.activity = activity;
    }
}
