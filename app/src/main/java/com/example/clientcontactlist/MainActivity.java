package com.example.clientcontactlist;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    EditText editVorname, editNachname, editTelnummer, editAdresse, editTisch;

    Button btnAddData;
    Button btnShowLog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DatabaseHelper(this);

        editNachname = findViewById(R.id.editText_nachname);
        editVorname = findViewById(R.id.editText_vorname);
        editAdresse = findViewById(R.id.editText_adresse);
        editTelnummer = findViewById(R.id.editText_telnummer);
        editTisch = findViewById(R.id.editText_tisch);
        btnAddData = findViewById(R.id.button_add);


    editTisch.setText("100");
        AddData();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.admin:
                askForPassword();
                return true;
            case R.id.karte:
                showDialog();
                return true;
            case R.id.login:
                Intent intent = new Intent(this, LoginActivity.class);
                intent.putExtra("tisch", editTisch.getText().toString());
                startActivity(intent);
                return true;
            default: return super.onOptionsItemSelected(item);
        }

    }

    public void askForPassword(){
        Dialog dialog = new Dialog();
        dialog.show(getSupportFragmentManager(),"dialog");
    }

    public void AddData() {
        btnAddData.setOnClickListener(
                new View.OnClickListener() {

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View v) {


                        boolean isInserted = myDb.insertValues(editVorname.getText().toString(), editNachname.getText().toString(), editAdresse.getText().toString(), editTelnummer.getText().toString(), editTisch.getText().toString());
                        if (!isInserted) {
                            Toast.makeText(MainActivity.this, "Bitte füllen Sie alle Felder aus", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (isInserted) {

                            editNachname.setText(null);
                            editVorname.setText(null);
                            editAdresse.setText(null);
                            editTelnummer.setText(null);

                            showMsg(myDb.getLastId(),"Merken Sie sich diese Nummer!\nBeim nächsten Mal brauchen Sie nur das eingeben");

                        }
                    }
                }
        );
    }

    public void showMsg(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView myMsg = new TextView(this);
        myMsg.setText(title);
        myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
        myMsg.setTextSize(50);
        myMsg.setTextColor(Color.BLACK);
        builder.setCustomTitle(myMsg);
        builder.setCancelable(true);
        //builder.setTitle(title);
        builder.setMessage(msg);
        builder.setNegativeButton("Fertig", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }

    public void showDialog(){
        AlertDialog.Builder alertadd = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.layout_code, null);
        alertadd.setView(view);
        alertadd.setNegativeButton("Fertig", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        alertadd.show();
    }

}
