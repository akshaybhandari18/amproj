package com.example.amproj;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class SelectEventActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_activity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_activity, menu);
        return true;
    }
    public void bdayOnButtonClick(View v)
    {
    	Intent i=new Intent(getBaseContext(),AddBirthdayActivity.class);
		i.putExtra("Event_Id", "create");
    	startActivity(i);
    }
    public void asaryOnButtonClick(View v)
    {
    	Intent i=new Intent(getBaseContext(),AddAnnyversaryEvent.class);
    	i.putExtra("Event_Id", "create");
    	startActivity(i);
    }
    public void cstmOnButtonClick(View v)
    {
    	Intent i=new Intent(getBaseContext(),AddCustomEvent.class);
    	i.putExtra("Event_Id", "create");
    	startActivity(i);    	
    }
}
