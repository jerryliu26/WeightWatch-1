package com.eldgammel.ww;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class  MainActivity extends ActionBarActivity {

    DBHelper helper;
    public double weight, height, age;
    public double bmr;
    String gender = " " ;
    public double calA;
    public double cLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoadMessageTask task = new LoadMessageTask();
        task.execute();

        helper = new DBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(amount*cal) calA FROM ww;", null);
        cursor.moveToFirst();
        calA = cursor.getDouble(0);



    }

    @Override
    protected void onResume() {
        super.onResume();
        // This method is called when this activity is put foreground.


        helper = new DBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(amount*cal) calA FROM ww;", null);
        cursor.moveToFirst();
        calA = cursor.getDouble(0);

        cLeft = bmr-calA;

        LoadMessageTask task = new LoadMessageTask();
        task.execute();


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

                PostMessageTask p = new PostMessageTask();
                p.execute(0+"",0+"",0+"",gender);
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

    class LoadMessageTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            BufferedReader reader;
            StringBuilder buffer = new StringBuilder();
            String line;


            try {

                URL u = new URL("http://ict.siit.tu.ac.th/~u5522800069/get.php");
                HttpURLConnection h = (HttpURLConnection)u.openConnection();
                h.setRequestMethod("GET");
                h.setDoInput(true);
                h.connect();


                int response = h.getResponseCode();
                if (response == 200) {
                    reader = new BufferedReader(new InputStreamReader(h.getInputStream()));
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }
                    JSONObject json = new JSONObject(buffer.toString());
                    Log.d("Test", json.toString());
                    JSONArray user = json.getJSONArray("user");
                    for (int i = 0; i < user.length(); i++) {
                        weight = user.getJSONObject(i).getDouble("Weight");
                        height = user.getJSONObject(i).getDouble("Height");
                        age = user.getJSONObject(i).getDouble("Age");
                        gender = user.getJSONObject(i).getString("Gender");


                        Log.d("Test", weight + "");
                        Log.d("Test", height + "");
                        Log.d("Test", age + "");
                        Log.d("Test", gender + "");
                        if (weight != 0 && height != 0 && age!=0) {
                            if (gender.equals("Female")) {
                                bmr = 655 + (9.6 * weight) + (1.8 * height) - (4.7 * age);
                            } else if (gender.equals("Male")) {
                                bmr = 66 + (13.7 * weight) + (5 * height) - (6.8 * age);
                            }
                        } else {
                            bmr = 0;
                        }
                        Log.d("Test", bmr + "");
                    }
                }




                return  true;
            } catch (MalformedURLException e) {
                Log.e("LoadMessageTask", "Invalid URL");
            } catch (IOException e) {
                Log.e("LoadMessageTask", "I/O Exception");
            } catch (JSONException e) {
                Log.e("LoadMessageTask", "Invalid JSON");
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            cLeft = bmr-calA;
            double cLeftRound = Math.round(cLeft *100.0) /100.0;

            TextView tvGP = (TextView) findViewById(R.id.tvBMR1);
            tvGP.setText(Double.toString(bmr));
            TextView tvCR = (TextView) findViewById(R.id.tvCal1);
            tvCR.setText(Double.toString(calA));
            TextView tvGPA = (TextView) findViewById(R.id.tvLeft1);
            tvGPA.setText(Double.toString(cLeftRound));


        }
    }

    class PostMessageTask extends AsyncTask<String, Void, Boolean> {
        String line;
        StringBuilder buffer = new StringBuilder();
        @Override
        protected Boolean doInBackground(String... params) {

            String sWeight = params[0];
            String sHeight = params[1];
            String sAge = params[2];
            String gender = params[3];

            HttpClient h = new DefaultHttpClient();
            HttpPost p = new HttpPost("http://ict.siit.tu.ac.th/~u5522800069/connect.php");
            try {
                List<NameValuePair> values = new ArrayList<NameValuePair>();
                values.add(new BasicNameValuePair("weight", sWeight));
                values.add(new BasicNameValuePair("height", sHeight));
                values.add(new BasicNameValuePair("age", sAge));
                values.add(new BasicNameValuePair("gender", gender));

                p.setEntity(new UrlEncodedFormEntity(values));



                HttpResponse response = h.execute(p);


                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                Log.i("postData", response.getStatusLine().toString());
            }
            catch (UnsupportedEncodingException e) {
                Log.e("Error", "Invalid encoding");
            } catch (ClientProtocolException e) {
                Log.e("Error", "Error in posting a message");
            } catch (IOException e) {
                Log.e("Error", "I/O Exception");
            }
            return true;
        }
        @Override
        protected void onPostExecute(Boolean result) {

        }

    }
}


