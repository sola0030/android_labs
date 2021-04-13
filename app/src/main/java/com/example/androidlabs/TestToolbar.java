package com.example.androidlabs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

import static com.example.androidlabs.R.id.my_toolbar;

public class TestToolbar extends AppCompatActivity {

    Toolbar myToolbar;
    String overflowToast = "This is the initial message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);
        //Toolbar
        myToolbar = findViewById(my_toolbar);
        setSupportActionBar(myToolbar);

//        Button alertDialogButton = (Button)findViewById(R.id.btnToolbar);
//        alertDialogButton.setOnClickListener( clik ->   alertExample()  );


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuitem, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //what to do when the menu item is selected:
            case R.id.MenuItemsOverflow:
                Toast.makeText(this, "You clicked on the overflow menu", Toast.LENGTH_LONG).show();
                break;

            case R.id.choice1:
                Toast.makeText(this, overflowToast, Toast.LENGTH_LONG).show();
                break;
            case R.id.choice2:
                alertExample();
                break;
            case R.id.choice3:
                //Snackbar code:
                Snackbar sb = Snackbar.make(myToolbar, "This is the Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Go Back?", e -> finish());
                sb.show();
                break;

        }
        return true;
    }

    public void alertExample() {
        View middle = getLayoutInflater().inflate(R.layout.lab8activity, null);
        EditText et = (EditText) middle.findViewById(R.id.view_edit_text);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("The Message")
                .setPositiveButton("Positive", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        overflowToast = et.getText().toString();
                    }
                })
                .setNegativeButton("Negative", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // What to do on Cancel
                    }
                }).setView(middle);

        builder.create().show();
    }
}