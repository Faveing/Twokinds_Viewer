package com.example.twokindsviewer;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
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
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private int current_page = 1;
    private WebView page;
    private TextView number_holder;
    private int total_number_of_pages;
    private String base_url;
    private String[] unsorted_index_of_pages;
    private String[] sorted_index_of_pages;

    private boolean thread1;
    private boolean thread2;
    private boolean thread3;
    private boolean thread4;
    private boolean thread5;
    private boolean thread6;
    private boolean thread7;

    private boolean indexing_finished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


        current_page = LoadInt();

        number_holder = findViewById(R.id.page_number);

        //Index_of_pages = new String[1000];
        unsorted_index_of_pages = new String[1200];
        sorted_index_of_pages = new String[1200];

        page = findViewById(R.id.page);
        page.getSettings().setBuiltInZoomControls(true);
        page.getSettings().setDisplayZoomControls(false);
        page.setInitialScale(130);

        // Init for variables that keep track is thread is running false = running true = done
        thread1 = false;
        thread2 = false;
        thread3 = false;
        thread4 = false;
        thread5 = false;
        thread6 = false;
        thread7 = false;

        base_url = "http://cdn.twokinds.keenspot.com/comics/";

        // TODO Switch from Webview to image view or load only the image and not the webpage for the comic
        setup_listeners();
        create_index_of_pages();
    }

    protected void create_index_of_pages(){

        new Thread(){

            @Override
            public void run() {
                // TODO Improve Speed of the index
                // TODO GENERAL CLEAN UP OF CODE
                // TODO Make Index of the pages go into a file so the index does not have to run everytime the app is ran.
                String string_url;
                int thread = 1;
                //Log.i("Twokinds","Index is running");
                super.run();
                for (int year = 2003; year <= 2005; year ++){
                    //Log.i("Twokinds","Year:" + year);
                    for (int month = 1; month <= 12; month ++) {
                        //Log.i("Twokinds","Month:" + month);
                        for(int day = 1; day <= 31; day ++) {
                            //Log.i("Twokinds","Day:" + day);
                            try {
                                if (Integer.toString(month).length() < 2 && Integer.toString(day).length() < 2 ){
                                    string_url = year + "0" + month + "0" + day;
                                } else if (Integer.toString(month).length() < 2 ){
                                    string_url = year + "0" + month + day;
                                } else if (Integer.toString(day).length() < 2){
                                    string_url = year + month + "0" + day;
                                } else {
                                    string_url = String.valueOf(year) + String.valueOf(month) + String.valueOf(day);
                                }

                                URL url = new URL(base_url + string_url + ".jpg");

                                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                                con.setRequestMethod("HEAD");
                                con.connect();

                                //Log.i("Twokinds", string_url + " Responce Code:" + con.getResponseCode());

                                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                    total_number_of_pages = total_number_of_pages + 1;
                                    //Index_of_pages[total_number_of_pages] = string_url;
                                    unsorted_index_of_pages[total_number_of_pages] = string_url;
                                    Log.i("Twokinds", Long.valueOf(string_url) + " Found " + total_number_of_pages + " thread: " + thread);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                thread_finish_check(1);
            }
        }.start();
        // TODO Add multicore indexing to speed up the app. The code below works but it will produce the index out of order
        new Thread(){

            @Override
            public void run() {
                String string_url;
                //Log.i("Twokinds", "Index is running");
                int thread = 2;
                super.run();
                for (int year = 2006; year <= 2008; year++) {
                    //Log.i("Twokinds", "Year:" + year);
                    for (int month = 1; month <= 12; month++) {
                        //Log.i("Twokinds", "Month:" + month);
                        for (int day = 1; day <= 31; day++) {
                            //Log.i("Twokinds", "Day:" + day);
                            try {
                                if (Integer.toString(month).length() < 2 && Integer.toString(day).length() < 2 ){
                                    string_url = year + "0" + month + "0" + day;
                                } else if (Integer.toString(month).length() < 2 ){
                                    string_url = year + "0" + month + day;
                                } else if (Integer.toString(day).length() < 2){
                                    string_url = year + month + "0" + day;
                                } else {
                                    string_url = String.valueOf(year) + String.valueOf(month) + String.valueOf(day);
                                }

                                URL url = new URL(base_url + string_url + ".jpg");

                                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                                con.setRequestMethod("HEAD");
                                con.connect();

                                //Log.i("Twokinds", string_url + " Responce Code:" + con.getResponseCode());

                                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                    total_number_of_pages = total_number_of_pages + 1;
                                    //Index_of_pages[total_number_of_pages] = string_url;
                                    unsorted_index_of_pages[total_number_of_pages] = string_url;
                                    Log.i("Twokinds", Long.valueOf(string_url) + " Found " + total_number_of_pages + " thread: " + thread);
                                }
                            } catch (Exception e) {
                                //Log.i("Twokinds", "Not Found");
                                e.printStackTrace();
                            }
                        }
                    }
                }
                thread_finish_check(thread);
            }
        }.start();
        new Thread(){

            @Override
            public void run() {
                String string_url;
                //Log.i("Twokinds", "Index is running");
                int thread = 3;
                super.run();
                for (int year = 2009; year <= 2011; year++) {
                    //Log.i("Twokinds", "Year:" + year);
                    for (int month = 1; month <= 12; month++) {
                        //Log.i("Twokinds", "Month:" + month);
                        for (int day = 1; day <= 31; day++) {
                            //Log.i("Twokinds", "Day:" + day);
                            try {
                                if (Integer.toString(month).length() < 2 && Integer.toString(day).length() < 2 ){
                                    string_url = year + "0" + month + "0" + day;
                                } else if (Integer.toString(month).length() < 2 ){
                                    string_url = year + "0" + month + day;
                                } else if (Integer.toString(day).length() < 2){
                                    string_url = year + month + "0" + day;
                                } else {
                                    string_url = String.valueOf(year) + String.valueOf(month) + String.valueOf(day);
                                }

                                URL url = new URL(base_url + string_url + ".jpg");

                                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                                con.setRequestMethod("HEAD");
                                con.connect();

                                //Log.i("Twokinds", string_url + " Responce Code:" + con.getResponseCode());

                                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                    total_number_of_pages = total_number_of_pages + 1;
                                    //Index_of_pages[total_number_of_pages] = string_url;
                                    unsorted_index_of_pages[total_number_of_pages] = string_url;
                                    Log.i("Twokinds", Long.valueOf(string_url) + " Found " + total_number_of_pages + " thread: " + thread);
                                }
                            } catch (Exception e) {
                                //Log.i("Twokinds", "Not Found");
                                e.printStackTrace();
                            }
                        }
                    }
                }
                thread_finish_check(thread);
            }
        }.start();
        new Thread(){

            @Override
            public void run() {
                String string_url;
                //Log.i("Twokinds", "Index is running");
                int thread = 4;
                super.run();
                for (int year = 2012; year <= 2014; year++) {
                    //Log.i("Twokinds", "Year:" + year);
                    for (int month = 1; month <= 12; month++) {
                        //Log.i("Twokinds", "Month:" + month);
                        for (int day = 1; day <= 31; day++) {
                            //Log.i("Twokinds", "Day:" + day);
                            try {
                                if (Integer.toString(month).length() < 2 && Integer.toString(day).length() < 2 ){
                                    string_url = year + "0" + month + "0" + day;
                                } else if (Integer.toString(month).length() < 2 ){
                                    string_url = year + "0" + month + day;
                                } else if (Integer.toString(day).length() < 2){
                                    string_url = year + month + "0" + day;
                                } else {
                                    string_url = String.valueOf(year) + String.valueOf(month) + String.valueOf(day);
                                }

                                URL url = new URL(base_url + string_url + ".jpg");

                                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                                con.setRequestMethod("HEAD");
                                con.connect();

                                //Log.i("Twokinds", string_url + " Responce Code:" + con.getResponseCode());

                                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                    total_number_of_pages = total_number_of_pages + 1;
                                    //Index_of_pages[total_number_of_pages] = string_url;
                                    unsorted_index_of_pages[total_number_of_pages] = string_url;
                                    Log.i("Twokinds", Long.valueOf(string_url) + " Found " + total_number_of_pages + " thread: " + thread);
                                }
                            } catch (Exception e) {
                                //Log.i("Twokinds", "Not Found");
                                e.printStackTrace();
                            }
                        }
                    }
                }
                thread_finish_check(thread);
            }
        }.start();
        new Thread(){

            @Override
            public void run() {
                String string_url;
                Log.i("Twokinds", "Index is running");
                int thread = 5;
                super.run();
                for (int year = 2015; year <= 2017; year++) {
                    //Log.i("Twokinds", "Year:" + year);
                    for (int month = 1; month <= 12; month++) {
                        //Log.i("Twokinds", "Month:" + month);
                        for (int day = 1; day <= 31; day++) {
                            //Log.i("Twokinds", "Day:" + day);
                            try {
                                if (Integer.toString(month).length() < 2 && Integer.toString(day).length() < 2 ){
                                    string_url = year + "0" + month + "0" + day;
                                } else if (Integer.toString(month).length() < 2 ){
                                    string_url = year + "0" + month + day;
                                } else if (Integer.toString(day).length() < 2){
                                    string_url = year + month + "0" + day;
                                } else {
                                    string_url = String.valueOf(year) + String.valueOf(month) + String.valueOf(day);
                                }

                                URL url = new URL(base_url + string_url + ".jpg");

                                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                                con.setRequestMethod("HEAD");
                                con.connect();

                                //Log.i("Twokinds", string_url + " Responce Code:" + con.getResponseCode());

                                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                    total_number_of_pages = total_number_of_pages + 1;
                                    //Index_of_pages[total_number_of_pages] = string_url;
                                    unsorted_index_of_pages[total_number_of_pages] = string_url;
                                    Log.i("Twokinds", Long.valueOf(string_url) + " Found " + total_number_of_pages + " thread: " + thread);
                                }
                            } catch (Exception e) {
                                //Log.i("Twokinds", "Not Found");
                                e.printStackTrace();
                            }
                        }
                    }
                }
                thread_finish_check(thread);
            }
        }.start();
        new Thread(){

            @Override
            public void run() {
                String string_url;
                Log.i("Twokinds", "Index is running");
                int thread = 6;
                super.run();
                for (int year = 2018; year <= 2019; year++) {
                    //Log.i("Twokinds", "Year:" + year);
                    for (int month = 1; month <= 12; month++) {
                        //Log.i("Twokinds", "Month:" + month);
                        for (int day = 1; day <= 31; day++) {
                            //Log.i("Twokinds", "Day:" + day);
                            try {
                                if (Integer.toString(month).length() < 2 && Integer.toString(day).length() < 2 ){
                                    string_url = year + "0" + month + "0" + day;
                                } else if (Integer.toString(month).length() < 2 ){
                                    string_url = year + "0" + month + day;
                                } else if (Integer.toString(day).length() < 2){
                                    string_url = year + month + "0" + day;
                                } else {
                                    string_url = String.valueOf(year) + String.valueOf(month) + String.valueOf(day);
                                }

                                URL url = new URL(base_url + string_url + ".jpg");

                                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                                con.setRequestMethod("HEAD");
                                con.connect();

                                //Log.i("Twokinds", string_url + " Responce Code:" + con.getResponseCode());

                                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                    total_number_of_pages = total_number_of_pages + 1;
                                    //Index_of_pages[total_number_of_pages] = string_url;
                                    unsorted_index_of_pages[total_number_of_pages] = string_url;
                                    Log.i("Twokinds", Long.valueOf(string_url) + " Found " + total_number_of_pages + " thread: " + thread);
                                }
                            } catch (Exception e) {
                                //Log.i("Twokinds", "Not Found");
                                e.printStackTrace();
                            }
                        }
                    }
                }
                thread_finish_check(thread);
            }
        }.start();
        new Thread(){

            @Override
            public void run() {
                String string_url;
                //Log.i("Twokinds", "Index is running");
                int thread = 7;
                super.run();
                for (int year = 2019; year <= 2020; year++) {
                    //Log.i("Twokinds", "Year:" + year);
                    for (int month = 1; month <= 12; month++) {
                        //Log.i("Twokinds", "Month:" + month);
                        for (int day = 1; day <= 31; day++) {
                            //Log.i("Twokinds", "Day:" + day);
                            try {
                                if (Integer.toString(month).length() < 2 && Integer.toString(day).length() < 2 ){
                                    string_url = year + "0" + month + "0" + day;
                                } else if (Integer.toString(month).length() < 2 ){
                                    string_url = year + "0" + month + day;
                                } else if (Integer.toString(day).length() < 2){
                                    string_url = year + month + "0" + day;
                                } else {
                                    string_url = String.valueOf(year) + String.valueOf(month) + String.valueOf(day);
                                }

                                URL url = new URL(base_url + string_url + ".png");

                                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                                con.setRequestMethod("HEAD");
                                con.connect();

                                //Log.i("Twokinds", string_url + " Responce Code:" + con.getResponseCode());

                                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                    total_number_of_pages = total_number_of_pages + 1;
                                    //Index_of_pages[total_number_of_pages] = string_url;
                                    unsorted_index_of_pages[total_number_of_pages] = string_url;
                                    Log.i("Twokinds", Long.valueOf(string_url) + " Found " + total_number_of_pages + "  thread: " + thread);
                                }
                            } catch (Exception e) {
                                //Log.i("Twokinds", "Not Found");
                                e.printStackTrace();
                            }
                        }
                    }
                }
                thread_finish_check(thread);
            }
        }.start();
    }

    public void setup_listeners(){

        // Listeners for buttons
        final Button next_button = findViewById(R.id.next_button);
        next_button.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                try {
                    if (indexing_finished) {
                        current_page = current_page + 1;
                        page.loadUrl(base_url + sorted_index_of_pages[current_page] + check_file_type(current_page));
                        number_holder.setText(String.valueOf(current_page));
                    } else {
                        Toast.makeText(MainActivity.this, "Indexing has not finished yet it should take 1-2 minutes.",
                                Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e){
                    Toast.makeText(MainActivity.this, "Next Page has not been Indexed",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        final Button prev_button = findViewById(R.id.prev_button);
        prev_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                try {
                    if (indexing_finished) {
                        current_page = current_page - 1;
                        page.loadUrl(base_url + sorted_index_of_pages[current_page] + check_file_type(current_page));
                        number_holder.setText(String.valueOf(current_page));
                    } else {
                        Toast.makeText(MainActivity.this, "Indexing has not finished yet it should take 1-2 minutes.",
                                Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e){
                    Log.i("twokinds","Prev_button Fail");
                }
            }
        });
        final Button goto_button = findViewById(R.id.goto_button);
        goto_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                try {
                    if (indexing_finished) {
                        CharSequence num = number_holder.getText();
                        current_page = Integer.parseInt(num.toString());

                        page.loadUrl(base_url + sorted_index_of_pages[current_page] + check_file_type(current_page));
                    } else {
                        Toast.makeText(MainActivity.this, "Indexing has not finished yet it should take 1-2 minutes.",
                                Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e){
                    Toast.makeText(MainActivity.this, "This page has not been indexed yet",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public int LoadInt(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sharedPreferences.getInt("current_page", 1);
    }
    public void SaveInt(String key, int value){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public String check_file_type(int page){
        if (page >= 943){
            return ".png";
        } else{
            return ".jpg";
        }
    }

    public String[] sort_index_list(String[] unsorted_list){
        //Log.i("twokinds", unsorted_list[current_page]);
        unsorted_list = clean(unsorted_list);
        Arrays.sort(unsorted_list);
        return unsorted_list;
    }

    public void thread_finish_check(int thread){

        if(thread == 1){
            thread1 = true;
            Log.i("twokinds","Index thread 1 has finished");
        } else if(thread == 2){
            thread2 = true;
            Log.i("twokinds","Index thread 2 has finished");
        } else if(thread == 3){
            thread3 = true;
            Log.i("twokinds","Index thread 3 has finished");
        } else if(thread == 4){
            thread4 = true;
            Log.i("twokinds","Index thread 4 has finished");
        } else if(thread == 5){
            thread5 = true;
            Log.i("twokinds","Index thread 5 has finished");
        } else if(thread == 6){
            thread6 = true;
            Log.i("twokinds","Index thread 6 has finished");
        } else if(thread == 7){
            thread7 = true;
            Log.i("twokinds","Index thread 7 has finished");
        }

        if(thread1 && thread2 && thread3 && thread4 && thread5 && thread6 && thread7){
            sorted_index_of_pages = sort_index_list(unsorted_index_of_pages);
            Log.i("twokinds","All threads have finished and index list is sorted");
            indexing_finished = true;
        }
    }

    public void refresh(){
        number_holder.setText(String.valueOf(current_page));
        Log.i("twokinds", String.valueOf(sorted_index_of_pages[current_page]));
        page.loadUrl(base_url + sorted_index_of_pages[current_page] + ".jpg");
    }

    public static String[] clean(final String[] v) {
        for(int i = 0; i < v.length; i++){
            if (v[i] == null){
                Log.i("twokinds", "Cleaned " + i);
                v[i] = "A"; // A because it will be above the numbers when it is sorted
                            // Just a really dirty way of cleaning null values
            }
        }
        return v;
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
            if (indexing_finished) {
                refresh();
            } else {
                Toast.makeText(MainActivity.this, "Indexing has not finished yet it should take 1-2 minutes.",
                        Toast.LENGTH_LONG).show();
            }

        }
        if (id == R.id.latest_action){
            if (indexing_finished) {
                current_page = total_number_of_pages - 1;
                page.loadUrl(base_url + sorted_index_of_pages[current_page] + check_file_type(current_page));
                Log.i("twokinds", "loaded" + sorted_index_of_pages[current_page] + check_file_type(current_page));
                number_holder.setText(String.valueOf(current_page));
            } else {
                Toast.makeText(MainActivity.this, "Indexing has not finished yet it should take 1-2 minutes.",
                        Toast.LENGTH_LONG).show();
            }
        }
        if (id == R.id.save_action){
            SaveInt("current_page", current_page);
        }
        if (id == R.id.quit_action){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
