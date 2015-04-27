package com.eldgammel.ww;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            int rID = rgGender.getCheckedRadioButtonId();
            String gender = ((RadioButton)findViewById(rID)).getText().toString();
            PostMessageTask p = new PostMessageTask();
            p.execute(sWeight,sHeight,sAge,gender);

            Intent result = new Intent();
            result.putExtra("Weight", Integer.valueOf(sWeight));
            result.putExtra("Height", Integer.valueOf(sHeight));
            result.putExtra("Age", Integer.valueOf(sAge));
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
            if (result) {
                Toast t = Toast.makeText(SettingMActivity.this.getApplicationContext(),
                        "Successfully saved your data",
                        Toast.LENGTH_SHORT);
                t.show();
            }
            else {
                Toast t = Toast.makeText(SettingMActivity.this.getApplicationContext(),
                        "Unable to save data",
                        Toast.LENGTH_SHORT);
                t.show();
            }
        }

    }

}
