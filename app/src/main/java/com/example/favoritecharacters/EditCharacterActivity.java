package com.example.favoritecharacters;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditCharacterActivity extends AppCompatActivity {

    EditText nameEditText;
    EditText seriesEditText;
    Button submitButton;
    Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_character);

        nameEditText = findViewById(R.id.nameEditText);
        seriesEditText = findViewById(R.id.seriesEditText);
        submitButton = findViewById(R.id.submitButton);
        cancelButton = findViewById(R.id.cancelButton);

        Intent intent = getIntent();

        try {

            SQLiteDatabase characterDatabase = this.openOrCreateDatabase("Characters", MODE_PRIVATE, null);

            Cursor characterCursor = characterDatabase.rawQuery("SELECT * FROM characters WHERE id = " + intent.getIntExtra("characterNumber", 0), null);

            int characterNameIndex = characterCursor.getColumnIndex("name");
            int characterSeriesIndex = characterCursor.getColumnIndex("series");
            int characterIdIndex = characterCursor.getColumnIndex("id");

            characterCursor.moveToNext();

            String name = characterCursor.getString(characterNameIndex);
            String series = characterCursor.getString(characterSeriesIndex);
            int id = characterCursor.getInt(characterIdIndex);

            nameEditText.setText(name);
            seriesEditText.setText(series);

            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String updateName = nameEditText.getText().toString();
                    String updateSeries = seriesEditText.getText().toString();

                    if (!updateName.matches("") && !updateSeries.matches("")) {
                        characterDatabase.execSQL("UPDATE characters SET name = '" + updateName + "', series = '" + updateSeries + "' WHERE id = " + id);
                        makeMainIntent();
                    } else {
                        Toast.makeText(EditCharacterActivity.this, "You need a name and series", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeMainIntent();
            }
        });
    }

    public void makeMainIntent() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}