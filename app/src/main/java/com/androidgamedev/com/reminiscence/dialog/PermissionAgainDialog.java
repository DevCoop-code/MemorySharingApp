package com.androidgamedev.com.reminiscence.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.androidgamedev.com.reminiscence.ImageGridActivity;
import com.androidgamedev.com.reminiscence.MyPhotoFragment;
import com.androidgamedev.com.reminiscence.R;
import com.androidgamedev.com.reminiscence.contact.DBManager;
import com.androidgamedev.com.reminiscence.contact.DBSqlData;

public class PermissionAgainDialog extends DialogFragment
{
    Activity activity;

    int mRowsDeleted = 0;

    public static PermissionAgainDialog newInstance(int title, String imagePath, int year, int month)
    {
        PermissionAgainDialog permissionDialog = new PermissionAgainDialog();
        Bundle args = new Bundle();
        args.putInt("TITLE", title);
        args.putString("IMAGEPATH", imagePath);
        args.putInt("YEAR", year);
        args.putInt("MONTH", month);

        permissionDialog.setArguments(args);

        return permissionDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        int title = getArguments().getInt("TITLE");
        final String imagePath = getArguments().getString("IMAGEPATH");
        final int year = getArguments().getInt("YEAR");
        final int month = getArguments().getInt("MONTH");

        final String mSelectionClause = MediaStore.Images.Media.DATA + " = ?";
        final String[] mSelectionArgs = {imagePath};

        return new AlertDialog.Builder(getActivity())
                .setIcon(R.mipmap.reminiscence)
                .setTitle(title)
                .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        mRowsDeleted = activity.getContentResolver().delete(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                mSelectionClause,
                                mSelectionArgs
                        );

                        String[] imagepaths = {imagePath};

                        DBManager dbMgr = new DBManager(activity);
                        dbMgr.dbOpen();
                        dbMgr.delete(DBSqlData.SQL_DB_DELETE_DATA, imagepaths);
                        dbMgr.dbClose();

                        Toast.makeText(getContext(),"삭제되었습니다.",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getContext(), ImageGridActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.putExtra("year", year);
                        i.putExtra("month", month);
                        startActivity(i);
                    }
                })
                .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Toast.makeText(getContext(),"취소되었습니다.",Toast.LENGTH_SHORT).show();
                    }
                })
                .create();
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        //createDateListView(activity);
        this.activity = activity;
    }
}
