package com.bertogonz3000.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //A numeric code to id the edit activity
    public final static int EDIT_REQUEST_CODE = 20;

    //Keys to use for passing data between activity
    public final static String ITEM_TEXT = "itemText";
    public final static String ITEM_POSITION = "itemPosition";


    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readItems();

        itemsAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, items);
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(itemsAdapter);

        //Mock adata
      //  items.add("First item");
      //  items.add("second item");

        setupListViewListener();
    }

    public void onAddItem(View view) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);

        String itemText = etNewItem.getText().toString();

        itemsAdapter.add(itemText);

        etNewItem.setText("");

        writeItems();

        Toast.makeText(getApplicationContext(), "Item added to the list", Toast.LENGTH_SHORT)
                .show();

    }

    //private cuz we're calling this directly,not called by the framework
    private void setupListViewListener(){

        Log.i("MainActivity", "Setting up listener on list view");

        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {

                Log.i("MainAcivity", "Item removed from list: " + position);

                items.remove(position);

                itemsAdapter.notifyDataSetChanged();

                writeItems();

                //Method consumed the long click, so return true!  If ignored, false
                return true;
            }
        });

        //Set up item listener for edit (regular click)
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //Create new activity
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                //pass the data being edited to the activity
                i.putExtra(ITEM_TEXT, items.get(position));
                i.putExtra(ITEM_POSITION, position);
                //Display the activity to the user
                startActivityForResult(i, EDIT_REQUEST_CODE);
            }
        });
    }

    // handle results from edit activity

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if the edit activity completed ok
        if (resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE){
            //extract the updated item text from result intent extras
            String updatedItem = data.getExtras().getString(ITEM_TEXT);
            //extract original position of edited item
            int position = data.getExtras().getInt(ITEM_POSITION);
            //update the model with the new item text at the edited position
            items.set(position, updatedItem);
            // notify the adapter that the model changed
            itemsAdapter.notifyDataSetChanged();

            //persist the changed model
            writeItems();
            //notify the user that the operation completed okay
            Toast.makeText(this, "Item updated successfully", Toast.LENGTH_LONG).show();
        }
    }


    //These next few methods are private because they will only be called by MainActivity

    private File getDataFile() {
        return new File(getFilesDir(), "todo.txt");
    }

    private void readItems(){

        //FileUtils comes with Commons.io

        try {
        items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error Reading File", e);
            items = new ArrayList<>();
        }
    }

    private void writeItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e){
            Log.e("MainActivity", "Error Writing File", e);
        }
    }

}