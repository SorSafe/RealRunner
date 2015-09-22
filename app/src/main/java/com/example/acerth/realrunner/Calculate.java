package com.example.acerth.realrunner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Admin PC on 8/9/2558.
 */
public class Calculate extends Activity {

    private Spinner gender;
    private ArrayList<String> genderData = new ArrayList<String>();
    private Spinner daysExerciseSpinner;
    private ArrayList<String> daysExerciseData = new ArrayList<String>();

    private ImageView calButton;
    private ImageView resetButton;
    private String bmiStr;
    private String bmrStr;
    private String tdeeStr;
    private double mutiply;
    private String genderChosen;

    EditText wieghtValue;
    EditText hieghtValue;
    EditText ageValue;

    TextView bmiValue;
    TextView statusValue;
    TextView bmrValue;
    TextView tdeeValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_calculate);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear);
//        linearLayout.setBackgroundColor(Color.parseColor("#00b06b"));

        wieghtValue = (EditText) findViewById(R.id.weight);
        hieghtValue = (EditText) findViewById(R.id.hieght);
        ageValue = (EditText) findViewById(R.id.Age);

        bmiValue = (TextView) findViewById(R.id.bmi);
        statusValue = (TextView) findViewById(R.id.status);
        bmrValue = (TextView) findViewById(R.id.bmr);
        tdeeValue  = (TextView) findViewById(R.id.TDEE);

        // Add gender Data to spinner
        gender = (Spinner) findViewById(R.id.gender);
        createGenderData();

        ArrayAdapter<String> adapterGender = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, genderData);
        gender.setAdapter(adapterGender);

        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String gender_select = genderData.get(position);
                if (gender_select.equalsIgnoreCase("Male")) {
                    genderChosen = "Male";
                } else if (gender_select.equalsIgnoreCase("Female")) {
                    genderChosen = "Female";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(), "Please select day of Gender !!!!!", Toast.LENGTH_SHORT);
            }
        });

        // Add days Exercise to spinner
        daysExerciseSpinner = (Spinner) findViewById(R.id.day_of_exercise_per_week);
        createdaysExerciseData();

        ArrayAdapter<String> adapterExercise = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, daysExerciseData);
        daysExerciseSpinner.setAdapter(adapterExercise);

        daysExerciseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected_day = daysExerciseData.get(position);
                if (selected_day.equalsIgnoreCase("No Exercise")) {
                    mutiply = 1.2;
                } else if (selected_day.equalsIgnoreCase("1-3 days/week")) {
                    mutiply = 1.375;
                } else if (selected_day.equalsIgnoreCase("4-5 days/week")) {
                    mutiply = 1.55;
                } else if (selected_day.equalsIgnoreCase("6-7 days/week")) {
                    mutiply = 1.725;
                } else if (selected_day.equalsIgnoreCase("everyday (for altelete)")) {
                    mutiply = 1.9;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(), "Please select day of Exercise !!!!!", Toast.LENGTH_SHORT);
            }
        });

        calButton = (ImageView) findViewById(R.id.cal);
        calButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateBMI();
                calculateBMR();
                calculateTDEE();
            }
        });

        resetButton = (ImageView) findViewById(R.id.reset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wieghtValue.setText("");
                hieghtValue.setText("");
                ageValue.setText("");
                bmiValue.setText("");
                statusValue.setText("");
                bmrValue.setText("");
                tdeeValue.setText("");
            }
        });
    }

    public void createdaysExerciseData(){
        daysExerciseData.add("No Exercise");
        daysExerciseData.add("1-3 days/week");
        daysExerciseData.add("4-5 days/week");
        daysExerciseData.add("6-7 days/week");
        daysExerciseData.add("everyday (for altelete)");
    }

    public void createGenderData(){
        genderData.add("Male");
        genderData.add("Female");
    }


    public  void calculateBMI(){
        double wieght = Double.parseDouble(wieghtValue.getText().toString());
        double hieght = Double.parseDouble(hieghtValue.getText().toString());

        double bmi = wieght / Math.pow(hieght / 100, 2);
        bmiStr = bmi+"";

        bmiValue.setText(bmiStr);

        if (bmi >= 40) {
            statusValue.setText("The Most Obeysity");
        } else if (bmi >= 35) {
            statusValue.setText("Obeysity Level 2");
        } else if (bmi >= 28.5) {
            statusValue.setText("Obeysity Level 1");
        } else if (bmi >= 23.5) {
            statusValue.setText("Too Much Wieght");
        } else if (bmi >= 18.5) {
            statusValue.setText("Common Wieght");
        } else {
            statusValue.setText("Too Less Wieght");
        }
    }

    public void calculateBMR (){
        double wieght = Double.parseDouble(wieghtValue.getText().toString());
        double hieght = Double.parseDouble(hieghtValue.getText().toString());
        double age = Double.parseDouble(ageValue.getText().toString());

        if(genderChosen != null && genderChosen.equalsIgnoreCase("Male")){
            double malebmr = 66+(13.7*wieght)+(5*hieght)-(6.8*age);
            bmrStr = malebmr+"";
        }else if(genderChosen != null && genderChosen.equalsIgnoreCase("Female")){
            double femalebmr = 665+(9.6*wieght)+(1.8*hieght)-(4.7*age);
            bmrStr = femalebmr+"";
        }
        bmrValue.setText(bmrStr);
    }

    public void calculateTDEE(){
        double bmr = Double.parseDouble(bmrValue.getText().toString());

        double tdee = bmr*mutiply;
        tdeeStr = tdee+"";
        tdeeValue.setText(tdeeStr);
    }
}
