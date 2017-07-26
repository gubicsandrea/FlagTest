package com.example.android.flagtest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Hadiné on 2017.07.24..
 */

/**
 * Adapter for listing test results
 */

public class TestResultAdapter extends ArrayAdapter<TestResult> {
    public TestResultAdapter(@NonNull Context context, @NonNull List<TestResult> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        //For the first time we should inflate a new list item
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.toplist_item, parent, false);
        }

        //Get the current element to display
        TestResult currentResult = getItem(position);

        //Get the views to populate
        TextView rankTextView = (TextView) listItemView.findViewById(R.id.rank);
        TextView nameTextView = (TextView) listItemView.findViewById(R.id.player_name);
        TextView pointsTextView = (TextView) listItemView.findViewById(R.id.player_points);

        //Populate the view with the current result
        rankTextView.setText(Integer.toString(position + 1));
        nameTextView.setText(currentResult.getName());
        pointsTextView.setText(Integer.toString(currentResult.getPoints()));

        return listItemView;
    }
}
