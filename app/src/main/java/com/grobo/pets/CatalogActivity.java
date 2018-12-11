package com.grobo.pets;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.grobo.pets.data.PetContract;

public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    ListView petsListView;
    private static final int PET_LOADER = 0;
    PetCursorAdapter cursorAdapter;

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

        petsListView = (ListView) findViewById(R.id.pets_list_view);
        View emptyView = findViewById(R.id.empty_view);
        petsListView.setEmptyView(emptyView);

        petsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getBaseContext(), EditorActivity.class);
                i.setData(Uri.withAppendedPath(PetContract.PetEntry.CONTENT_URI, String.valueOf(id)));
                startActivity(i);
            }
        });

        cursorAdapter = new PetCursorAdapter(this, null);
        petsListView.setAdapter(cursorAdapter);

        LoaderManager loaderManager = LoaderManager.getInstance(this);
        loaderManager.initLoader(PET_LOADER, null, this);
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
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void insertPet(){

        ContentValues contentValues = new ContentValues();
        contentValues.put(PetContract.PetEntry.COLUMN_PET_NAME, "Toto");
        contentValues.put(PetContract.PetEntry.COLUMN_PET_BREED, "Terrier");
        contentValues.put(PetContract.PetEntry.COLUMN_PET_GENDER, PetContract.PetEntry.GENDER_MALE);
        contentValues.put(PetContract.PetEntry.COLUMN_PET_WEIGHT, 7);

        Uri returnedUri = getContentResolver().insert(PetContract.PetEntry.CONTENT_URI, contentValues);
        Toast.makeText(this, "Dummy pet inserted", Toast.LENGTH_SHORT).show();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {

        String[] projection = {
                BaseColumns._ID, PetContract.PetEntry.COLUMN_PET_NAME,
                PetContract.PetEntry.COLUMN_PET_BREED};

        return new CursorLoader(this, PetContract.PetEntry.CONTENT_URI, projection,
                null, null, null);

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        cursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }
}
