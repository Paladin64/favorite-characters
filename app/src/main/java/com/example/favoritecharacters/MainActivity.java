package com.example.favoritecharacters;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> characters = new ArrayList<String>();
    static ArrayList<Integer> characterIds = new ArrayList<Integer>();
    static ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        characters.clear();
        characterIds.clear();

        characters.add("Add new character");
        characterIds.add(0);

        try {

            SQLiteDatabase characterDatabase = this.openOrCreateDatabase("Characters", MODE_PRIVATE, null);

            characterDatabase.execSQL("CREATE TABLE IF NOT EXISTS characters (name VARCHAR, series VARCHAR, id INTEGER PRIMARY KEY)");

            Cursor characterCursor = characterDatabase.rawQuery("SELECT * FROM characters", null);

            int characterNameIndex = characterCursor.getColumnIndex("name");
            int characterSeriesIndex = characterCursor.getColumnIndex("series");
            int characterIdIndex = characterCursor.getColumnIndex("id");

            characterCursor.moveToFirst();

            while (!characterCursor.isAfterLast()) {
                String characterName = characterCursor.getString(characterNameIndex);
                String characterSeries = characterCursor.getString(characterSeriesIndex);
                int characterId = characterCursor.getInt(characterIdIndex);

                String characterEntry = characterName + ": " + characterSeries;

                characters.add(characterEntry);
                characterIds.add(characterId);

                characterCursor.moveToNext();
            }

            ListView listView = findViewById(R.id.listView);

            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, characters);

            listView.setAdapter(arrayAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent;

                    if (i != 0) {
                        intent = new Intent(getApplicationContext(), EditCharacterActivity.class);
                        intent.putExtra("characterNumber", i);
                    } else {
                        intent = new Intent(getApplicationContext(), CreateActivity.class);
                    }

                    startActivity(intent);

                }
            });

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                    int itemToDelete = i;

                    new AlertDialog.Builder(MainActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Are you sure?")
                            .setMessage("Do you want to delete this character?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    int characterDatabaseDelete = characterIds.get(itemToDelete);

                                    characterDatabase.execSQL("DELETE FROM characters WHERE id = " + characterDatabaseDelete);

                                    characters.remove(itemToDelete);
                                    characterIds.remove(itemToDelete);
                                    arrayAdapter.notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();

                    return true;
                }
            });
            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}