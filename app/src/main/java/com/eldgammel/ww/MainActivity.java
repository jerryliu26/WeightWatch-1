package com.eldgammel.ww;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    DBHelper helper;
    public double weight, height, age;
    public double bmr;
    String gender = " " ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onResume() {
        super.onResume();
        // This method is called when this activity is put foreground.

        helper = new DBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(amount*cal) calA FROM ww;", null);
        cursor.moveToFirst();

        Double calA = cursor.getDouble(0);

        Double cLeft = bmr-calA;

        TextView tvGP = (TextView) findViewById(R.id.tvBMR1);
        tvGP.setText(Double.toString(bmr));
        TextView tvCR = (TextView) findViewById(R.id.tvCal1);
        tvCR.setText(Double.toString(calA));
        TextView tvGPA = (TextView) findViewById(R.id.tvLeft1);
        tvGPA.setText(Double.toString(cLeft));



    }

    public void buttonClicked(View v) {
        int id = v.getId();
        Intent i;

        switch(id) {
            case R.id.btAdd:
                i = new Intent(this, AddFoodActivity.class);
                startActivityForResult(i, 88);
                break;

            case R.id.btShow:
                i = new Intent(this, ListFoodActivity.class);
                startActivity(i);
                break;

            case R.id.btBMR:
                i = new Intent(this, SettingMActivity.class);
                startActivityForResult(i, 55);
                break;

            case R.id.btReset:

                SQLiteDatabase db  = helper.getWritableDatabase();
                int n_rows = db.delete("ww", "", null);
                TextView tvGP = (TextView) findViewById(R.id.tvBMR1);
                tvGP.setText("0.0");
                TextView tvCR = (TextView) findViewById(R.id.tvCal1);
                tvCR.setText("0.0");
                TextView tvGPA = (TextView) findViewById(R.id.tvLeft1);
                tvGPA.setText("0.0");
                bmr = 0.0;
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 88) {
            if (resultCode == RESULT_OK) {
                String code = data.getStringExtra("fName");
                int credit = data.getIntExtra("cal", 0);
                int amount = data.getIntExtra("amount", 0);


                helper = new DBHelper(this.getApplicationContext());
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues r = new ContentValues();
                r.put("fName", code);
                r.put("cal", credit);
                r.put("amount", amount);


                long new_id = db.insert("ww", null, r);
            }
        }
        if (requestCode == 55) {
            if (resultCode == RESULT_OK) {
                weight = data.getIntExtra("Weight", 0);
                height = data.getIntExtra("Height", 0);
                age = data.getIntExtra("Age", 0);
                gender = data.getStringExtra("Gender");

                if (gender.equals("Female")) {
                    bmr = 655+(9.6*weight)+(1.8*height)-(4.7*age);
                } else if (gender.equals("Male")){
                    bmr = 66+(13.7*weight)+(5*height)-(6.8*age);
                }
            }
        }

        Log.d("ww", "onActivityResult");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
