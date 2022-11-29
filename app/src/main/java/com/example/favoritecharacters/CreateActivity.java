package com.example.favoritecharacters;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateActivity extends AppCompatActivity {

    EditText nameEditText;
    EditText seriesEditText;
    Button createButton;
    Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        nameEditText = findViewById(R.id.nameCreateEditText);
        seriesEditText = findViewById(R.id.seriesCreateEditText);
        createButton = findViewById(R.id.createButton);
        cancelButton = findViewById(R.id.cancelCreateButton);

        try {

            SQLiteDatabase characterDatabase = this.openOrCreateDatabase("Characters", MODE_PRIVATE, null);

            createButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = nameEditText.getText().toString();
                    String series = seriesEditText.getText().toString();

                    if (!name.matches("") && !series.matches("")) {

                        characterDatabase.execSQL("INSERT INTO characters (name, series) VALUES ('" + name + "', '" + series + "')");

                        makeMainIntent();

                    } else {
                        Toast.makeText(CreateActivity.this, "You need a name and series", Toast.LENGTH_SHORT).show();
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