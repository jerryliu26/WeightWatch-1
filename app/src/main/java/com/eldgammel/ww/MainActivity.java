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

    CourseDBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onResume() {
        super.onResume();
        // This method is called when this activity is put foreground.

        helper = new CourseDBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(credit) cr, SUM(amount*credit) gp FROM course;", null);
        cursor.moveToFirst();
        Double cr = cursor.getDouble(0);

        Double gp = cursor.getDouble(1);

        double gpa = gp-cr;

        TextView tvGP = (TextView) findViewById(R.id.tvBMR1);
        tvGP.setText(Double.toString(gp));
        TextView tvCR = (TextView) findViewById(R.id.tvCal1);
        tvCR.setText(Double.toString(cr));
        TextView tvGPA = (TextView) findViewById(R.id.tvLeft1);
        tvGPA.setText(Double.toString(gpa));


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

            case R.id.btReset:

                SQLiteDatabase db  = helper.getWritableDatabase();
                int n_rows = db.delete("course", "", null);
                TextView tvGP = (TextView) findViewById(R.id.tvBMR1);
                tvGP.setText("0.0");
                TextView tvCR = (TextView) findViewById(R.id.tvCal1);
                tvCR.setText("0.0");
                TextView tvGPA = (TextView) findViewById(R.id.tvLeft1);
                tvGPA.setText("0.0");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 88) {
            if (resultCode == RESULT_OK) {
                String code = data.getStringExtra("fName");
                int credit = data.getIntExtra("credit", 0);
                int amount = data.getIntExtra("amount", 0);


                helper = new CourseDBHelper(this.getApplicationContext());
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues r = new ContentValues();
                r.put("fName", code);
                r.put("credit", credit);
                r.put("amount", amount);


                long new_id = db.insert("course", null, r);
            }
        }

        Log.d("course", "onActivityResult");
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
