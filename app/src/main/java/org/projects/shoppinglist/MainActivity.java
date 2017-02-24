package org.projects.shoppinglist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {


    ArrayAdapter<String> adapter;
    ListView listView;
    ArrayList<String> bag = new ArrayList<>();

    public ArrayAdapter getMyAdapter()
    {
        return adapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getting our listiew - you can check the ID in the xml to see that it
        //is indeed specified as "list"
        listView = (ListView) findViewById(R.id.list);
        //here we create a new adapter linking the bag and the
        if (savedInstanceState!=null)
        {
            if (savedInstanceState.containsKey("list"))
                bag = savedInstanceState.getStringArrayList("list");
        }
        //listview
        adapter =  new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_checked,bag );

        //setting the adapter on the listview
        listView.setAdapter(adapter);
        //here we set the choice mode - meaning in this case we can
        //only select one item at a time.
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        Button removeButton = (Button) findViewById(R.id.removeButton);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray array = listView.getCheckedItemPositions();
                int size = array.size();
                int removed = 0;

                for(int i = 0; i < size; i++) {
                    int key = array.keyAt(i);
                    // get the object by the key.
                    boolean selected = array.get(key);
                    if (selected)
                    {
                        //note that the bag becomes smaller
                        //when removing, so we need to save
                        //how many we have removed to get the
                        //right index to remove
                        bag.remove(key-removed);
                        listView.setItemChecked(key,false);
                        //notice the line above - we need
                        //to manually uncheck the items we
                        //deleted. This is NOT done automatically
                        //by the listview in a correct way
                        //(yes, that was a surprise for me!)
                        removed++;
                    }
                } //for loop
                adapter.notifyDataSetChanged();

            }
        });

        Button addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random rand = new Random();
                adapter.add("Milk "+rand.nextInt());
                //The next line is needed in order to say to the ListView
                //that the data has changed - we have added stuff now!
                getMyAdapter().notifyDataSetChanged();
            }
        });

        //add some stuff to the list so we have something
        // to show on app startup


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("list",bag);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
