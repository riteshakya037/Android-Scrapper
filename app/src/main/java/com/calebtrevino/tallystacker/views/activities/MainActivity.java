package com.calebtrevino.tallystacker.views.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.controllers.sources.League;
import com.calebtrevino.tallystacker.controllers.sources.ProBaseball;

public class MainActivity extends AppCompatActivity {

//    table.frodds-data-tbl > tbody>tr:has(td:not(.cellBorderR1))  //List ot table rows with valid data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                (new Description()).execute("http://www.vegasinsider.com/nfl/odds/las-vegas/");
            }
        });
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

    private class Description extends AsyncTask<String, Void, Void> {
        String desc;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(String... params) {
            League league = new ProBaseball();
            System.out.println("league = " + league.pullGamesFromNetwork());
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
//            System.out.println("desc = " + desc);
        }
    }

}
