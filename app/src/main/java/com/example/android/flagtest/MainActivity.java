package com.example.android.flagtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    Test test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        test = new Test(getApplicationContext(),10,getString(R.string.file_name));
        displayTest();
    }

    /**
     * Displays the 10 random questions from test object
     */
    private void displayTest(){
        List<Question> testQuestions = test.getTest();
        for (int i = 0; i < test.getN(); i++) {
            String idString = "image" + (i + 1) ;
            int id = getId(idString,R.id.class);
            ImageView imageView = (ImageView) findViewById(id);
            id = getResources().getIdentifier(testQuestions.get(i).getImage(),"drawable",getPackageName());
            imageView.setImageResource(id);
            for(int j = 0; j < 4; j++) {
                idString = "group" + (i + 1) + "_option" + (j + 1);
                id = getId(idString,R.id.class);
                TextView textView = (TextView) findViewById(id);
                textView.setText(testQuestions.get(i).getOptions().get(j));
            }
        };
    }

    /**
     * Get the value of an id if I know the name of it
     */
    private int getId(String resourceName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resourceName);
            return idField.getInt(idField);
        } catch (Exception e) {
            throw new RuntimeException("No resource ID found for: "
                    + resourceName + " / " + c, e);
        }
    }

    /**
     * Check the answers when the user press the Ready button
     * @param view
     */

    public void checkTest(View view){
        //Collect all the answers to an ArrayList and count the checked RadioButtons
        List<String> answers = new ArrayList<String>();
        int count = 0;
        for(int i = 0; i < test.getN(); i++){
            for(int j = 1; j < 5; j++) {
                String idString = "group" + (i + 1) + "_option" + j;
                int id = getId(idString,R.id.class);
                RadioButton radioButton = (RadioButton) findViewById(id);
                if (radioButton.isChecked()){
                    answers.add(radioButton.getText().toString());
                    count++;
                }
            }
        }

        String message;
        //If the number of checked RadioButtons are smaller than 10, display error message to the user, otherwise check the answers and display the number of correct ones
        if(count < test.getN()){
            message = getResources().getString(R.string.not_all_answered);
            Toast.makeText(this.getApplicationContext(),message,Toast.LENGTH_SHORT).show();
        } else {
            int rightAnswerCount = test.checkAnswers(answers);
            message = getResources().getString(R.string.test_result,rightAnswerCount);
            Toast.makeText(this.getApplicationContext(),message,Toast.LENGTH_SHORT).show();
        }
    }
}
