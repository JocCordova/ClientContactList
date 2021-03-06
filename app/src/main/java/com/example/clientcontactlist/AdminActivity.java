package com.example.clientcontactlist;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AdminActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    EditText editDate, editNachname, editId;
    Button btnShowLog;
    DatePickerDialog picker;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        myDb = new DatabaseHelper(this);

        editDate = (EditText) findViewById(R.id.editText_date);
        editNachname = findViewById(R.id.editText_nachname);
        editId = findViewById(R.id.editText_id);
        btnShowLog = findViewById(R.id.button_log);

        viewLog();
        datePicker();

    }

    public void viewLog() {
        btnShowLog.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Cursor res = myDb.getLog(editId.getText().toString(), editDate.getText().toString(), editNachname.getText().toString());

                        if (res.getCount() == 0) {
                            showMsg("Error", "No Logs Found");
                            return;
                        }
                        StringBuffer buffer = new StringBuffer();
                        while (res.moveToNext()) {
                            buffer.append("Id : " + res.getString(0) + "\n");
                            buffer.append("Nachname : " + res.getString(1) + "\n");
                            buffer.append("Vorname : " + res.getString(2) + "\n");
                            buffer.append("Adresse : " + res.getString(3) + "\n");
                            buffer.append("Telefonnummer : " + res.getString(4) + "\n");
                            buffer.append("Datum : " + res.getString(5) + "\n");
                            buffer.append("Tischnummer : " + res.getString(6) + "\n\n");
                        }
                        showMsg("Logs", buffer.toString());
                        return;

                    }
                }
        );
    }

    public void showMsg(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.show();
    }

    public void datePicker(){
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
            // date picker dialog
            picker = new DatePickerDialog(AdminActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String month = String.valueOf(monthOfYear + 1);
                    String day = String.valueOf(dayOfMonth);
                    if ((monthOfYear + 1)<10){month = "0" + month;}
                    if ((dayOfMonth)<10){day = "0" + day;}


                    editDate.setText(year+ "-" +month + "-" + day);
                }
            }, year, month, day);
                picker.show();
        }
    }

        );

    }

}




