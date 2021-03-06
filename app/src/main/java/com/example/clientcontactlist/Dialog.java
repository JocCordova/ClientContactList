package com.example.clientcontactlist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class Dialog extends AppCompatDialogFragment {
    private EditText editTextPwd;
    private String password = "Admin01";


    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
       AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
       View view = inflater.inflate(R.layout.layout_password, null);

        builder.setView(view)
                .setTitle("Admin Login")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(editTextPwd.getText().toString().equals(password)){

                            Intent intent = new Intent(getContext(), AdminActivity.class);
                            startActivity(intent);
                        }else{
                            showWrongPwd();
                        }

                    }
                });

        editTextPwd = view.findViewById(R.id.editText_pwd);
        return builder.create();
    }


    public void showWrongPwd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Wrong Password");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }

        });
        builder.setMessage("Wrong Admin Password.");
        builder.show();
    }

}
