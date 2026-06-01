package com.example.electricitybillestimator;


import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    EditText editMonth,
            editUnit,
            editRebate,
            editTotal,
            editFinal;

    Button buttonUpdate,
            buttonDelete;

    DatabaseHelper myDB;

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        editMonth = findViewById(R.id.editMonth);
        editUnit = findViewById(R.id.editUnit);
        editRebate = findViewById(R.id.editRebate);
        editTotal = findViewById(R.id.editTotal);
        editFinal = findViewById(R.id.editFinal);

        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonDelete = findViewById(R.id.buttonDelete);

        myDB = new DatabaseHelper(this);

        // GET ID

        id = getIntent().getStringExtra("ID");

        // LOAD DATA

        Cursor res = myDB.getDataById(id);

        if (res.moveToFirst()) {

            editMonth.setText(res.getString(1));
            editUnit.setText(res.getString(2));
            editRebate.setText(res.getString(3));
            editTotal.setText(res.getString(4));
            editFinal.setText(res.getString(5));
        }

        // UPDATE

        buttonUpdate.setOnClickListener(v -> {

            int unit =
                    Integer.parseInt(
                            editUnit.getText().toString()
                    );

            double rebate =
                    Double.parseDouble(
                            editRebate.getText().toString()
                    );

            double totalCharges = 0;

            if (unit <= 200) {

                totalCharges = unit * 0.218;
            }

            else if (unit <= 300) {

                totalCharges =
                        (200 * 0.218)
                                +
                                ((unit - 200) * 0.334);
            }

            else if (unit <= 600) {

                totalCharges =
                        (200 * 0.218)
                                +
                                (100 * 0.334)
                                +
                                ((unit - 300) * 0.516);
            }

            else {

                totalCharges =
                        (200 * 0.218)
                                +
                                (100 * 0.334)
                                +
                                (300 * 0.516)
                                +
                                ((unit - 600) * 0.546);
            }

            double finalCost =
                    totalCharges -
                            (totalCharges * rebate / 100);

            editTotal.setText(
                    String.format(
                            "%.2f",
                            totalCharges
                    )
            );

            editFinal.setText(
                    String.format(
                            "%.2f",
                            finalCost
                    )
            );

            boolean result =
                    myDB.updateData(
                            id,
                            editMonth.getText().toString(),
                            editUnit.getText().toString(),
                            editRebate.getText().toString(),
                            editTotal.getText().toString(),
                            editFinal.getText().toString()
                    );

            if(result)
            {
                Toast.makeText(
                        DetailActivity.this,
                        "Record Updated Successfully",
                        Toast.LENGTH_SHORT
                ).show();

                finish();
            }
            else
            {
                Toast.makeText(
                        DetailActivity.this,
                        "Update Failed",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        // DELETE

        buttonDelete.setOnClickListener(v -> {

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(
                            DetailActivity.this
                    );

            builder.setTitle("Delete Record");

            builder.setMessage(
                    "Are you sure you want to delete this record?"
            );

            builder.setPositiveButton(
                    "YES",
                    (dialog, which) -> {

                        Integer deletedRows =
                                myDB.deleteData(id);

                        if(deletedRows > 0)
                        {
                            Toast.makeText(
                                    DetailActivity.this,
                                    "Record Deleted Successfully",
                                    Toast.LENGTH_SHORT
                            ).show();

                            finish();
                        }
                    });

            builder.setNegativeButton(
                    "NO",
                    (dialog, which) -> dialog.dismiss()
            );

            builder.show();
        });
    }
}