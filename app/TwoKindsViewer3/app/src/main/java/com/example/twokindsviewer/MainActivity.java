package com.example.twokindsviewer;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    int current_page = 1;

    int length_of_comic = 1000;

    String[] Index_of_pages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Index_of_pages = new String[1000];

        // TODO Switch from Webview to image view or load only the image and not the webpage for the comic

        WebView page = findViewById(R.id.page);

        page.getSettings().setBuiltInZoomControls(true);
        page.getSettings().setDisplayZoomControls(false);

        page.loadUrl("http://cdn.twokinds.keenspot.com/comics/20031022.jpg");
        create_index_of_pages();

        // Listeners for buttons
        final Button next_button = findViewById(R.id.next_button);
        next_button.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                try {
                    WebView page = findViewById(R.id.page);
                    current_page = current_page + 1;
                    page.loadUrl(Index_of_pages[current_page]);
                } catch (Exception e){
                    Toast.makeText(MainActivity.this, "Next Page has not been Indexed",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        final Button prev_button = findViewById(R.id.prev_button);
        prev_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                WebView page = findViewById(R.id.page);
                current_page = current_page - 1;
                page.loadUrl(Index_of_pages[current_page]);
            }
        });
        final Button goto_button = findViewById(R.id.goto_button);
        goto_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                try {
                    WebView page = findViewById(R.id.page);
                    TextView page_selected = findViewById(R.id.page_number);
                    CharSequence num = page_selected.getText();
                    current_page = Integer.parseInt(num.toString());

                    page.loadUrl(Index_of_pages[current_page]);
                } catch (Exception e){
                    Toast.makeText(MainActivity.this, "This page has not been indexed yet",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    protected void create_index_of_pages(){

        new Thread(){

            @Override
            public void run() {
                // TODO Improve Speed of the index
                // TODO Every Comic after 20181005 is .png .jpg so fix it -_-
                // TODO GENERAL CLEAN UP OF CODE
                // TODO Make Index of the pages go into a file so the index does not have to run everytime the app is ran.
                String customURL = "http://cdn.twokinds.keenspot.com/comics/";
                String string_url;
                int total = 0;
                Log.i("Twokinds","Index is running");
                super.run();
                for (int year = 2003; year <= 2019; year ++){
                    Log.i("Twokinds","Year:" + year);
                    for (int month = 1; month <= 12; month ++) {
                        Log.i("Twokinds","Month:" + month);
                        for(int day = 1; day <= 31; day ++) {
                            Log.i("Twokinds","Day:" + day);
                            try {

                                if (Integer.toString(month).length() < 2 && Integer.toString(day).length() < 2 ){
                                    string_url = customURL + year + "0" + month + "0" + day + ".jpg";
                                } else if (Integer.toString(month).length() < 2 ){
                                    string_url = customURL + year + "0" + month + day + ".jpg";
                                } else if (Integer.toString(day).length() < 2){
                                    string_url = customURL + year + month + "0" + day + ".jpg";
                                } else {
                                    string_url = customURL + year + month + day + ".jpg";
                                }

                                URL url = new URL(string_url);

                                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                                con.setRequestMethod("HEAD");
                                con.connect();

                                Log.i("Twokinds", string_url + " Responce Code:" + con.getResponseCode());

                                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                    total = total + 1;
                                    Index_of_pages[total] = string_url;
                                    Log.i( "Twokinds",string_url + " Found");
                                }
                            } catch (Exception e) {
                                Log.i("Twokinds", "Not Found");
                                e.printStackTrace();
                            }
                        }
                        string_url = "";
                    }
                }
            }
        }.start();
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
