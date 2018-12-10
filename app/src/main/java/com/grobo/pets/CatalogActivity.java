package com.grobo.pets;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.grobo.pets.data.PetContract;
import com.grobo.pets.data.PetDbHelper;

public class CatalogActivity extends AppCompatActivity {

    private PetDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, com.grobo.pets.EditorActivity.class);
                startActivity(intent);
            }
        });

        mDbHelper = new PetDbHelper(this);
        displayDatabaseInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertPet();
                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo() {

        String[] projection = {
                BaseColumns._ID, PetContract.PetEntry.COLUMN_PET_NAME,
                PetContract.PetEntry.COLUMN_PET_BREED,
                PetContract.PetEntry.COLUMN_PET_GENDER,
                PetContract.PetEntry.COLUMN_PET_WEIGHT};

        Cursor cursor = getContentResolver().query(
                PetContract.PetEntry.CONTENT_URI, projection,
                null,
                null,
                null);

        TextView displayView = (TextView) findViewById(R.id.text_view_pet);

        try {
            displayView.setText("The pets table contains " + cursor.getCount() + " pets.\n\n");

            displayView.append(PetContract.PetEntry._ID + " - "
                    + PetContract.PetEntry.COLUMN_PET_NAME + " - "
                    + PetContract.PetEntry.COLUMN_PET_BREED + " - "
                    + PetContract.PetEntry.COLUMN_PET_GENDER + " - "
                    + PetContract.PetEntry.COLUMN_PET_WEIGHT + "\n");

            while (cursor.moveToNext()) {
                int currentId = cursor.getInt(cursor.getColumnIndex(PetContract.PetEntry._ID));
                String currentName = cursor.getString(cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_NAME));
                String currentBreed = cursor.getString(cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_BREED));
                int currentGender = cursor.getInt(cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_GENDER));
                int currentWeight = cursor.getInt(cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_WEIGHT));

                displayView.append("\n" + currentId + " - "
                        + currentName + " - "
                        + currentBreed + " - "
                        + currentGender + " - "
                        + currentWeight);
            }

        } finally {
            cursor.close();
        }
    }

    private void insertPet(){

        ContentValues contentValues = new ContentValues();
        contentValues.put(PetContract.PetEntry.COLUMN_PET_NAME, "Toto");
        contentValues.put(PetContract.PetEntry.COLUMN_PET_BREED, "Terrier");
        contentValues.put(PetContract.PetEntry.COLUMN_PET_GENDER, PetContract.PetEntry.GENDER_MALE);
        contentValues.put(PetContract.PetEntry.COLUMN_PET_WEIGHT, 7);

        getContentResolver().insert(PetContract.PetEntry.CONTENT_URI, contentValues);
        Toast.makeText(this, "Dummy pet inserted", Toast.LENGTH_SHORT).show();
    }
}
