package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<String> items;
    Button addBtn;
    EditText editItem;
    RecyclerView listItems;
    ItemsAdapter itemsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addBtn = findViewById(R.id.addButton);
        editItem = findViewById(R.id.editItem);
        listItems = findViewById(R.id.itemView);

        loadItems();

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener()
        {
            @Override
            public void onItemLongClicked(int position) {
                items.remove(position);
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };
        itemsAdapter = new ItemsAdapter(items, onLongClickListener);
        listItems.setAdapter(itemsAdapter);
        listItems.setLayoutManager(new LinearLayoutManager(this));

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toAddItem = editItem.getText().toString();
                items.add(toAddItem);
                itemsAdapter.notifyItemInserted(items.size()-1);
                editItem.setText("");
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });
    }
    private File getDataFile()
    {
        return new File(getFilesDir(), "data.txt");
    }

    private void loadItems()
    {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }

    private void saveItems()
    {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }
    }

}