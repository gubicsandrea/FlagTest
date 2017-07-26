package com.example.android.flagtest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HallOfFame extends AppCompatActivity {

    //stores the name of the user from the intent if its added
    private String mName;
    //stores the points of the user from the intent if its addad
    private int mPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall_of_fame);

        //Stores {@link TestResult} objects with all the results stored in a file
        List<TestResult> results = readFromFile();

        //If the intent has the current game result, add it to the list
        if (getIntent().hasExtra("name")) {
            mName = getIntent().getStringExtra("name");
            mPoints = getIntent().getIntExtra("points", 0);
            TestResult testResult = new TestResult(mName, mPoints);
            results.add(testResult);
            //sort the list descending using the TestResult's compareTo method
            Collections.sort(results);
            //write back the ordered list to the file
            writeToFile(results);
        }

        //put the list into an adapter
        TestResultAdapter adapter = new TestResultAdapter(this, results);

        ListView listView = (ListView) findViewById(R.id.list);
        TextView emptyView = (TextView) findViewById(R.id.empty_view);
        Button button = (Button) findViewById(R.id.new_game_button);

        //assign the adapter to the listview
        listView.setAdapter(adapter);

        //set the empty view to display when there isn't any results yet
        listView.setEmptyView(emptyView);

        //add the button functionality to start a new game
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Reads the stored results from the file
     *
     * @return the complete list of results
     */
    private List<TestResult> readFromFile() {
        FileInputStream fis = null;
        ObjectInputStream is = null;
        List<TestResult> list = new ArrayList<>();
        try {
            fis = this.openFileInput(getString(R.string.top_list_filename));
            is = new ObjectInputStream(fis);
            TestResult element = (TestResult) is.readObject();
            while (element != null) {
                list.add(element);
                element = (TestResult) is.readObject();
            }
        } catch (FileNotFoundException ex) {
            //No need to do anything, there isn't any result yet.
        } catch (IOException ex) {
            //End of file?
        } catch (Exception ex) {
            Toast.makeText(this, getString(R.string.load_error), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                is.close();
                fis.close();
            } catch (Exception ex) {
                //Toast.makeText(this, getString(R.string.load_error), Toast.LENGTH_SHORT).show();
            }
        }
        return list;
    }

    /**
     * Writes the ordered results to the file
     */
    private void writeToFile(List<TestResult> results) {
        FileOutputStream fos = null;
        ObjectOutputStream os = null;
        try {
            fos = this.openFileOutput(getString(R.string.top_list_filename), Context.MODE_PRIVATE);
            os = new ObjectOutputStream(fos);
            for (int i = 0; i < results.size(); i++) {
                os.writeObject(results.get(i));
            }
        } catch (Exception ex) {
            Toast.makeText(this, getString(R.string.save_error), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                os.close();
                fos.close();
            } catch (Exception ex) {
                Toast.makeText(this, getString(R.string.save_error), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
