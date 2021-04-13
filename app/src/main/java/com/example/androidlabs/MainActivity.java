package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    SharedPreferences share;
    EditText emailField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        Button nextButton = (Button)findViewById(R.id.loginButton);
        emailField = (EditText)findViewById(R.id.typeEmail);
        share = getSharedPreferences("FileName", Context.MODE_PRIVATE);
        String saveEmail = share.getString("ReserveName", "Default value");
        emailField.setHint(saveEmail);
        nextButton.setOnClickListener( b -> {

            //Give directions to go from this page, to SecondActivity
            Intent nextPage = new Intent(MainActivity.this, ProfileActivity.class);

            //   EditText et =(EditText)findViewById(R.id.)
            nextPage.putExtra("emailType", emailField.getText().toString());
            //Now make the transition:
            startActivityForResult(nextPage, 345);
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = share.edit();
        String typeEmail  = emailField.getText().toString();
        editor.putString("ReserveName", typeEmail);
        editor.commit();
    }
}