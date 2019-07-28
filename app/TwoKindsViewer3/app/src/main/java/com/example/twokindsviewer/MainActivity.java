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


public class MainActivity extends AppCompatActivity {

    private int current_page = 1;
    private String[] Index_of_pages;
    private WebView page;
    private TextView number_holder;
    private int total_number_of_pages;
    private String base_url;

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

        Index_of_pages = new String[1000];

        page = findViewById(R.id.page);
        page.getSettings().setBuiltInZoomControls(true);
        page.getSettings().setDisplayZoomControls(false);
        page.setInitialScale(130);

        base_url = "http://cdn.twokinds.keenspot.com/comics/";

        refresh();

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
                Log.i("Twokinds","Index is running");
                super.run();
                for (int year = 2003; year <= 20019; year ++){
                    Log.i("Twokinds","Year:" + year);
                    for (int month = 1; month <= 12; month ++) {
                        Log.i("Twokinds","Month:" + month);
                        for(int day = 1; day <= 31; day ++) {
                            Log.i("Twokinds","Day:" + day);
                            try {
                                if (Integer.toString(month).length() < 2 && Integer.toString(day).length() < 2 ){
                                    string_url = base_url + year + "0" + month + "0" + day + ".jpg";
                                } else if (Integer.toString(month).length() < 2 ){
                                    string_url = base_url + year + "0" + month + day + ".jpg";
                                } else if (Integer.toString(day).length() < 2){
                                    string_url = base_url + year + month + "0" + day + ".jpg";
                                } else {
                                    string_url = base_url + year + month + day + ".jpg";
                                }

                                URL url = new URL(string_url);

                                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                                con.setRequestMethod("HEAD");
                                con.connect();

                                Log.i("Twokinds", string_url + " Responce Code:" + con.getResponseCode());

                                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                    total_number_of_pages = total_number_of_pages + 1;
                                    Index_of_pages[total_number_of_pages] = string_url;
                                    Log.i( "Twokinds",string_url + " Found");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }.start();
        // TODO Add multicore indexing to speed up the app. The code below works but it will produce the index out of order
//        new Thread(){
//
//            @Override
//            public void run() {
//
//                String customURL = "http://cdn.twokinds.keenspot.com/comics/";
//                String string_url;
//                Log.i("Twokinds", "Index is running");
//                super.run();
//                for (int year = 2006; year <= 2009; year++) {
//                    Log.i("Twokinds", "Year:" + year);
//                    for (int month = 1; month <= 12; month++) {
//                        Log.i("Twokinds", "Month:" + month);
//                        for (int day = 1; day <= 31; day++) {
//                            Log.i("Twokinds", "Day:" + day);
//                            try {
//                                if (Integer.toString(month).length() < 2 && Integer.toString(day).length() < 2) {
//                                    string_url = customURL + year + "0" + month + "0" + day + ".jpg";
//                                } else if (Integer.toString(month).length() < 2) {
//                                    string_url = customURL + year + "0" + month + day + ".jpg";
//                                } else if (Integer.toString(day).length() < 2) {
//                                    string_url = customURL + year + month + "0" + day + ".jpg";
//                                } else {
//                                    string_url = customURL + year + month + day + ".jpg";
//                                }
//
//                                URL url = new URL(string_url);
//
//                                HttpURLConnection con = (HttpURLConnection) url.openConnection();
//                                con.setRequestMethod("HEAD");
//                                con.connect();
//
//                                Log.i("Twokinds", string_url + " Responce Code:" + con.getResponseCode());
//
//                                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                                    total_number_of_pages = total_number_of_pages + 1;
//                                    Index_of_pages[total_number_of_pages] = string_url;
//                                    Log.i("Twokinds", string_url + " Found");
//                                }
//                            } catch (Exception e) {
//                                Log.i("Twokinds", "Not Found");
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }
//            }
//        }.start();
    }

    public void setup_listeners(){

        page.loadUrl(Index_of_pages[current_page]);

        // Listeners for buttons
        final Button next_button = findViewById(R.id.next_button);
        next_button.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                try {
                    current_page = current_page + 1;
                    page.loadUrl(Index_of_pages[current_page]);
                    number_holder.setText(String.valueOf(current_page));

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
                    current_page = current_page - 1;
                    page.loadUrl(Index_of_pages[current_page]);
                    number_holder.setText(String.valueOf(current_page));
                } catch (Exception e){
                    Log.i("twokinds","Prev_button Fail");
                }
            }
        });
        final Button goto_button = findViewById(R.id.goto_button);
        goto_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                try {
                    CharSequence num = number_holder.getText();
                    current_page = Integer.parseInt(num.toString());

                    page.loadUrl(Index_of_pages[current_page]);
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
//    public void SaveString(String key, String value){
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(key, value);
//        editor.commit();
//    }
//    public String LoadString(String value){
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        String savedValue = sharedPreferences.getString(value, null);
//        return savedValue;
//    }

    public void refresh(){
        number_holder.setText(String.valueOf(current_page));
        page.loadUrl(Index_of_pages[current_page]);
    }
//    public static List<String> readLines() {
//        File f = new File("Index_of_pages.txt");
//        BufferedReader r;
//        List<String> lines = new ArrayList<String>();
//        try {
//            r = new BufferedReader(new FileReader(f));
//            String line;
//            while (true) {
//                if ((line = r.readLine()) == null)
//                    break;
//                lines.add(line);
//            }
//        } catch (Exception e) {
//            e.printStackTrace(); // file not found
//        }
//        return lines;
//    }
//    public static void writeLines(List<String> lines) {
//        File f = new File("Index_of_pages.txt");
//        try {
//            PrintWriter pw = new PrintWriter(f);
//            for (String line : lines)
//                pw.println(line);
//            pw.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace(); // file not found
//        }
//    }

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
            page.loadUrl(Index_of_pages[current_page]);
            return true;
        }
        if (id == R.id.latest_action){
            page.loadUrl(Index_of_pages[total_number_of_pages]);
            current_page = total_number_of_pages;
            number_holder.setText(String.valueOf(current_page));
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
