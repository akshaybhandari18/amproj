package com.example.amproj;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class DatePickerActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_picker_activity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.date_picker_activity, menu);
        return true;
    }
}
