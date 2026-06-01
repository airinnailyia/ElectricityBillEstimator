package com.example.electricitybillestimator;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RecordActivity extends AppCompatActivity {

    ListView listView;

    DatabaseHelper myDB;

    ArrayList<String> list;
    ArrayList<String> idList;

    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_record);

        listView = findViewById(R.id.listView);

        myDB = new DatabaseHelper(this);

        list = new ArrayList<>();
        idList = new ArrayList<>();

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                list
        );

        listView.setAdapter(adapter);

        loadData();

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(
                            AdapterView<?> parent,
                            View view,
                            int position,
                            long id) {

                        Intent intent =
                                new Intent(
                                        RecordActivity.this,
                                        DetailActivity.class
                                );

                        intent.putExtra(
                                "ID",
                                idList.get(position)
                        );

                        startActivity(intent);
                    }
                });
    }

    @Override
    protected void onResume() {

        super.onResume();

        loadData();
    }

    private void loadData() {

        list.clear();
        idList.clear();

        Cursor res = myDB.getAllData();

        while (res.moveToNext()) {

            String month = res.getString(1);

            if (month == null || month.trim().isEmpty()) {
                continue;
            }

            idList.add(res.getString(0));

            String data =
                    "📅 Month : " + month
                            + "\n💰 Final Cost : RM "
                            + res.getString(5);

            list.add(data);
        }

        adapter.notifyDataSetChanged();
    }
}