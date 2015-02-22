package com.eldgammel.ww;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by sooksinyip on 2/22/2015 AD.
 */
public class SettingMActivity extends ActionBarActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_bmr);
    }

    public void addClicked(View v) {
        EditText etWeight = (EditText)findViewById(R.id.etWeight);
        EditText etHeight = (EditText)findViewById(R.id.etHeight);
        EditText etAge = (EditText)findViewById(R.id.etAge);
        RadioGroup rgGender = (RadioGroup)findViewById(R.id.rgGender);

        String sWeight = etWeight.getText().toString();
        String sHeight = etHeight.getText().toString();
        String sAge = etAge.getText().toString();

        if (sWeight.trim().length() == 0 || sHeight.trim().length() == 0 ||
                sAge.trim().length() == 0) {
            Toast t = Toast.makeText(this.getApplicationContext(),
                    "All fields are required to be filled.",
                    Toast.LENGTH_SHORT);
            t.show();
        }
        else {
            Intent result = new Intent();
            result.putExtra("Weight", Integer.valueOf(sWeight));
            result.putExtra("Height", Integer.valueOf(sHeight));
            result.putExtra("Age", Integer.valueOf(sAge));
            int rID = rgGender.getCheckedRadioButtonId();
            String gender = ((RadioButton)findViewById(rID)).getText().toString();
            result.putExtra("Gender", gender);


            this.setResult(RESULT_OK, result);
            this.finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_course, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
