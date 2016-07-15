package com.gorobaby.jh.phonebook.View;

import android.Manifest;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gorobaby.jh.phonebook.Model.Person;
import com.gorobaby.jh.phonebook.Model.PersonAdapter;
import com.gorobaby.jh.phonebook.R;
import com.gorobaby.jh.phonebook.Util.EndlessScrollListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DETAILS_QUERY_ID = 0;

    private ListView mListView;
    private ArrayList<Person> mContactList;
    private PersonAdapter mAdapter;
    private TextView mContactCount;
    private Cursor mContactData;

    private int mListOffset = 0;

    final String[] PROJECTION =
            {
                    ContactsContract.CommonDataKinds.Phone._ID,
                    ContactsContract.CommonDataKinds.Phone.PHOTO_URI,
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY:
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER
            };
    private final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.contact_listview);
        mContactList = new ArrayList<Person>();
        mContactCount = (TextView)findViewById(R.id.contact_count_textview);
        mAdapter = new PersonAdapter(getApplicationContext(),MainActivity.this, mContactList);
        mListView.setAdapter(mAdapter);

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_CONTACTS)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }else{
            getLoaderManager().initLoader(DETAILS_QUERY_ID, null, this);
        }

        mListView.setOnScrollListener(new EndlessScrollListener(10, getApplicationContext()) {
            @Override
            public void loadMore(int page, int totalItemsCount) {
                mContactCount.setText(String.valueOf(totalItemsCount));
                getContactInfo(mContactData);
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case DETAILS_QUERY_ID:{
                // Assigns the selection parameter
                // Starts the query
                CursorLoader cursorLoader =
                        new CursorLoader(
                                MainActivity.this,
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                PROJECTION,
                                null,
                                null,
                                "display_name ASC"
                        );
                return cursorLoader;
            }
            default: {
                return null;
            }
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case DETAILS_QUERY_ID:
                mContactData=data;
                getContactInfo(data);
        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case DETAILS_QUERY_ID:
                /*
                 * If you have current references to the Cursor,
                 * remove them here.
                 */
        }
    }

    private void getContactInfo(Cursor contactData) {
        Toast.makeText(getApplicationContext(),"getContactInfo",Toast.LENGTH_SHORT).show();
        int index = 0;
        if (contactData.moveToPosition(mListOffset)){
            do{
                int id = Integer.parseInt(contactData.getString(0));
                String photo = contactData.getString(1);
                String name = contactData.getString(2);
                String pNumber = contactData.getString(3);

                // do what ever you want here
                mContactList.add(new Person(id, photo, name, pNumber, false));
                index++;
            }while(contactData.moveToNext() && index < 10);
            mListOffset += 10;
        }
        mAdapter.notifyDataSetChanged();;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLoaderManager().initLoader(DETAILS_QUERY_ID, null, this);
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
