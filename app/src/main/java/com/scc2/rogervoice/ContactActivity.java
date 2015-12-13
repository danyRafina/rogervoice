package com.scc2.rogervoice;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ContactActivity extends AppCompatActivity implements
        AdapterView.OnItemClickListener {

    private ListView listView;
    private List<ContactMethods> list = new ArrayList<ContactMethods>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(this);
        Cursor phones = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
                null, null);
        while (phones.moveToNext()) {

            String name = phones
                    .getString(phones
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            String phoneNumber = phones
                    .getString(phones
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            ContactMethods objContact = new ContactMethods();
            objContact.setName(name);
            objContact.setPhoneNo(phoneNumber);
            list.add(objContact);

        }
        phones.close();

        ContactAdapter objAdapter = new ContactAdapter(
                ContactActivity.this, R.layout.row_contact, list);
        listView.setAdapter(objAdapter);

        if (null != list && list.size() != 0) {
            Collections.sort(list, new Comparator<ContactMethods>() {

                @Override
                public int compare(ContactMethods lhs, ContactMethods rhs) {
                    return lhs.getName().compareTo(rhs.getName());
                }
            });

        } else {
            showToast("Aucun contacts trouvés !");
        }
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
        if (id == R.id.action_menu) {
            startActivity(new Intent(ContactActivity.this,
                    ContactActivity.class));
        } else if (id == android.R.id.home) {
            // app icon in action bar clicked; go home
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> listview, View v, int position,
                            long id) {
        ContactMethods bean = (ContactMethods) listview.getItemAtPosition(position);
        showCallDialog(bean.getName(), bean.getPhoneNo());
    }

    private void showCallDialog(String name, final String phoneNo) {
        AlertDialog alert = new AlertDialog.Builder(ContactActivity.this)
                .create();
        alert.setTitle("Appeler ?");

        alert.setMessage("Voulez-vous appeler " + name + " ?");

        alert.setButton("Non", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.setButton2("Oui", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String phoneNumber = "tel:" + phoneNo;
                Intent intent = new Intent(Intent.ACTION_CALL, Uri
                        .parse(phoneNumber));
                startActivity(intent);
            }
        });
        alert.show();
    }

}