package com.example.electricitybillestimator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Spinner spinnerMonth, spinnerRebate;
    EditText editTextUnit;
    TextView textViewTotal, textViewFinal;

    Button buttonCalculate,
            buttonSave,
            buttonView,
            buttonAbout;

    DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerMonth = findViewById(R.id.spinnerMonth);
        spinnerRebate = findViewById(R.id.spinnerRebate);

        editTextUnit = findViewById(R.id.editTextUnit);

        textViewTotal = findViewById(R.id.textViewTotal);
        textViewFinal = findViewById(R.id.textViewFinal);

        buttonCalculate = findViewById(R.id.buttonCalculate);
        buttonSave = findViewById(R.id.buttonSave);
        buttonView = findViewById(R.id.buttonView);
        buttonAbout = findViewById(R.id.buttonAbout);

        myDB = new DatabaseHelper(this);

        String[] months = {
                "January",
                "February",
                "March",
                "April",
                "May",
                "June",
                "July",
                "August",
                "September",
                "October",
                "November",
                "December"
        };

        ArrayAdapter<String> monthAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        months
                );

        monthAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerMonth.setAdapter(monthAdapter);

        String[] rebates = {
                "0",
                "1",
                "2",
                "3",
                "4",
                "5"
        };

        ArrayAdapter<String> rebateAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        rebates
                );

        rebateAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerRebate.setAdapter(rebateAdapter);

        // CALCULATE

        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String unitText =
                        editTextUnit.getText().toString();

                if (unitText.isEmpty()) {

                    editTextUnit.setError(
                            "Please enter electricity unit"
                    );

                    return;
                }

                int unit =
                        Integer.parseInt(unitText);

                if (unit < 1 || unit > 1000) {

                    Toast.makeText(
                            MainActivity.this,
                            "Unit must be between 1 and 1000",
                            Toast.LENGTH_SHORT
                    ).show();

                    return;
                }

                double totalCharges =
                        calculateBill(unit);

                double rebate =
                        Double.parseDouble(
                                spinnerRebate
                                        .getSelectedItem()
                                        .toString()
                        );

                double finalCost =
                        totalCharges -
                                (totalCharges * rebate / 100);

                textViewTotal.setText(
                        "Total Charges : RM "
                                + String.format("%.2f",
                                totalCharges)
                );

                textViewFinal.setText(
                        "Final Cost : RM "
                                + String.format("%.2f",
                                finalCost)
                );
            }
        });

        // SAVE

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String month =
                        spinnerMonth.getSelectedItem().toString();

                String unit =
                        editTextUnit.getText().toString();

                String rebate =
                        spinnerRebate.getSelectedItem().toString();

                String total =
                        textViewTotal.getText()
                                .toString()
                                .replace("Total Charges : RM ", "");

                String finalCost =
                        textViewFinal.getText()
                                .toString()
                                .replace("Final Cost : RM ", "");

                if(unit.isEmpty())
                {
                    Toast.makeText(
                            MainActivity.this,
                            "Please enter unit first",
                            Toast.LENGTH_SHORT
                    ).show();

                    return;
                }

                boolean result =
                        myDB.insertData(
                                month,
                                unit,
                                rebate,
                                total,
                                finalCost
                        );

                if(result)
                {
                    Toast.makeText(
                            MainActivity.this,
                            "Record Saved Successfully",
                            Toast.LENGTH_LONG
                    ).show();
                }
                else
                {
                    Toast.makeText(
                            MainActivity.this,
                            "Failed To Save",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }
        });

        // VIEW RECORDS

        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =
                        new Intent(
                                MainActivity.this,
                                RecordActivity.class
                        );

                startActivity(intent);
            }
        });

        // ABOUT
        buttonAbout.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            AboutActivity.class
                    );

            startActivity(intent);

        });
    }

    private double calculateBill(int unit) {

        double total = 0;

        if (unit <= 200) {

            total = unit * 0.218;
        }

        else if (unit <= 300) {

            total =
                    (200 * 0.218)
                            +
                            ((unit - 200) * 0.334);
        }

        else if (unit <= 600) {

            total =
                    (200 * 0.218)
                            +
                            (100 * 0.334)
                            +
                            ((unit - 300) * 0.516);
        }

        else {

            total =
                    (200 * 0.218)
                            +
                            (100 * 0.334)
                            +
                            (300 * 0.516)
                            +
                            ((unit - 600) * 0.546);
        }

        return total;
    }
}