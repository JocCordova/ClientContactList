package com.example.clientcontactlist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    EditText editNachname, editId,editTisch;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editNachname = findViewById(R.id.editText_nachname);
        editId = findViewById(R.id.editText_id);
        editTisch= findViewById(R.id.editText_tisch);
        btnLogin= findViewById(R.id.button_log);

        Intent intent=getIntent();
        Bundle extras = intent.getExtras();
        String tisch;
        tisch = intent.getStringExtra("tisch");
        editTisch.setText(tisch);


        myDb = new DatabaseHelper(this);



        login();
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
                return true;
            default: return super.onOptionsItemSelected(item);
        }

    }
    public void askForPassword(){
        Dialog dialog = new Dialog();
        dialog.show(getSupportFragmentManager(),"dialog");
    }

    public void showDialog(){
        AlertDialog.Builder alertadd = new AlertDialog.Builder(LoginActivity.this);
        LayoutInflater inflater = LoginActivity.this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.layout_code, null);
        alertadd.setView(view);
        alertadd.setNegativeButton("Fertig", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        alertadd.show();
    }

    public void login() {
        btnLogin.setOnClickListener(
                new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View v) {
                        if(editId.getText().toString().equals("") || editId.getText().toString().equals(null) || editNachname.getText().toString().equals("") || editNachname.getText().toString().equals(null) ){
                            Toast.makeText(LoginActivity.this, "Bitte füllen Sie alle Felder aus", Toast.LENGTH_LONG).show();
                            return;
                        }

                        Boolean isInserted = myDb.visitLogin(editId.getText().toString(),editNachname.getText().toString(),editTisch.getText().toString());

                        if(!isInserted) {
                            Toast.makeText(LoginActivity.this, "Id oder Nachname falsch", Toast.LENGTH_LONG).show();
                        }
                        if(isInserted){
                            Toast.makeText(LoginActivity.this, "Erfolgreich Eingeloggt", Toast.LENGTH_SHORT).show();
                            editNachname.setText(null);
                            editId.setText(null);
                            finish();
                        }
                    }
                }
        );
    }
}
