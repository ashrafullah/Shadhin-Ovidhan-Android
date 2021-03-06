/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.imaginativeworld.shadhinovidhan;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class favorite_list_activity extends AppCompatActivity implements View.OnClickListener {

    // for DB favorite
    final String[] from = new String[]{
            DatabaseHelper.SO_FAVORITE
    };
    final int[] to = new int[]{
            R.id.textView
    };
    Button btnClose;
    private DBManager dbManager;
    private ListView listView;
    private SimpleCursorAdapter adapter;
    private String tEXT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.favorite_view_layout); // select default layout

        //Make window fill full width
//        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //============================================================

        dbManager = new DBManager(this);
        dbManager.open();

        //============================================================

        final Cursor cursor = null;

        listView = (ListView) findViewById(R.id.favorite_list);

        listView.setEmptyView(findViewById(R.id.empty)); // for empty view

        adapter = new SimpleCursorAdapter(this, R.layout.simple_list_item, cursor, from, to, 0);
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);

        adapter.changeCursor(dbManager.getFavorite());

        //============================================================

        btnClose = (Button) findViewById(R.id.btn_close);
        btnClose.setOnClickListener(this);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                TextView text = (TextView) view.findViewById(R.id.textView);

                tEXT = text.getText().toString();

                AlertDialog.Builder adb = new AlertDialog.Builder(favorite_list_activity.this);
                adb.setTitle(getString(R.string.fav_list_what_do_you_want));
                //adb.setMessage("?");

                adb.setNeutralButton(getString(R.string.fav_list_btn_delete), new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dbManager.deleteInfoFavorite(tEXT);
                        adapter.changeCursor(dbManager.getFavorite());
                        adapter.notifyDataSetChanged();

                    }
                });
                adb.setNegativeButton(getString(R.string.fav_list_btn_copy_to_clipboard),
                        new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                //Copy to Clip Board (Only support API >=11)
                                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("SO_Meaning", tEXT);
                                clipboard.setPrimaryClip(clip);

                                Toast t = Toast.makeText(favorite_list_activity.this,
                                        getString(R.string.text_copied_to_clipboard),
                                        Toast.LENGTH_LONG);
                                t.show();

                            }
                        });
                adb.setPositiveButton(getString(R.string.fav_list_btn_view_meaning), new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishWithResult();
                    }
                });
                adb.show();

            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_close:
                finish();
                break;
        }
    }

    private void finishWithResult() {
        Bundle conData = new Bundle();
        conData.putString("results", tEXT);
        Intent intent = new Intent();
        intent.putExtras(conData);
        setResult(RESULT_OK, intent);
        finish();
    }
}























