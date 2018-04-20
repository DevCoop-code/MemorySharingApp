package com.androidgamedev.com.reminiscence;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidgamedev.com.reminiscence.contact.ContactData;
import com.androidgamedev.com.reminiscence.contact.DBManager;
import com.androidgamedev.com.reminiscence.contact.DBSqlData;

import static java.security.AccessController.getContext;

public class ImageDescriptActivity extends Activity implements View.OnClickListener
{
    EditText editText;
    Button save_btn;

    String imagePath;
    int orientation;

    int year, month;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.descript_image);

        Intent i = getIntent();
        imagePath = i.getStringExtra("IMAGE_PATH");
        orientation = i.getIntExtra("ORIENTATION",0);

        year = i.getIntExtra("year", 0);
        month = i.getIntExtra("month", 0);

        editText = (EditText)findViewById(R.id.descrip_txt);
        save_btn = (Button)findViewById(R.id.save_des_btn);
        save_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.save_des_btn:
                insertDB();
                Toast.makeText(this,"추억을 저장하셨습니다.",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), ImageGridActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("year", year);
                i.putExtra("month", month);
                startActivity(i);
                break;
            default:

                break;
        }
    }

    private int insertDB()
    {
        String imageDescript = editText.getEditableText().toString();

        ContactData cData = new ContactData(imagePath, orientation+"", imageDescript);

        DBManager dbMgr = new DBManager(this);
        dbMgr.dbOpen();
        dbMgr.insertData(DBSqlData.SQL_DB_INSERT_DATA, cData);
        dbMgr.dbClose();

        return 0;
    }
}
