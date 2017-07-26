package com.example.android.flagtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class PersonalInfo extends AppCompatActivity {

    private EditText mNameEditText;
    private CheckBox mSaveCheckBox;
    private Button mOkButton;
    private int mPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        //Get the input fields
        mNameEditText = (EditText) findViewById(R.id.name_edittext);
        mSaveCheckBox = (CheckBox) findViewById(R.id.save_checkbox);
        mOkButton = (Button) findViewById(R.id.ok_button);

        mPoints = 0;

        //Extract points from intent
        if (getIntent().hasExtra("points")) {
            mPoints = getIntent().getIntExtra("points", 0);
        }

        //set the functionality to step to hall of fame screen with or without saving the current result
        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mNameEditText.getText().toString().trim();
                if (TextUtils.isEmpty(name) && mSaveCheckBox.isChecked()) {
                    //Display error, if the user wants to store the result, but didn't filled the name field
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.name_error), Toast.LENGTH_SHORT).show();
                } else {
                    //Make an intent with or without the results depending on the user's choice
                    Intent intent = new Intent(getApplicationContext(), HallOfFame.class);
                    if (!TextUtils.isEmpty(name) && mSaveCheckBox.isChecked()) {
                        intent.putExtra("name", name);
                        intent.putExtra("points", mPoints);
                    }
                    startActivity(intent);
                }
            }
        });
    }
}
