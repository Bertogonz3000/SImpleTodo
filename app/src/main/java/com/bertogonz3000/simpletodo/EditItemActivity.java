package com.bertogonz3000.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import static com.bertogonz3000.simpletodo.MainActivity.ITEM_POSITION;
import static com.bertogonz3000.simpletodo.MainActivity.ITEM_TEXT;

public class  EditItemActivity extends AppCompatActivity {

    //Track edit Text
    EditText etItemText;
    //Position of edited item in list
    int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        //resolve the edit text from the layout

        etItemText = (EditText) findViewById(R.id.etItemText);

        //Set edit text value from intent extra

        etItemText.setText(getIntent().getStringExtra(ITEM_TEXT));

        //Update position from intent extra

        position = getIntent().getIntExtra(ITEM_POSITION, 0);

        //Update the title bar of the activity
        getSupportActionBar().setTitle("Edit Item");

    }

    //handler for the save button
    public void onSaveItem(View v){
        //prepare new intent for result

        Intent i = new Intent();

        //pass updated item text as extra
        i.putExtra(ITEM_TEXT, etItemText.getText().toString());

        //pass original position
        i.putExtra(ITEM_POSITION, position);

        //set the intent as the result of the activity
        setResult(RESULT_OK, i);

        //close the edit activity and return to main
        finish();
    }

}
