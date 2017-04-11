package com.udacity.stockhawk.ui;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;


public class StockDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private String symbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);
        LineChart chart = (LineChart) findViewById(R.id.chart);

        /*BarData data = new BarData(getXAxisValues(), getDataSet());
        chart.setData(data);
//        chart.setDescription("My Chart");
        chart.animateXY(2000, 2000);
        chart.invalidate();*/
        symbol = getIntent().getStringExtra("symbol");
        getSupportLoaderManager().initLoader(1, null, this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.stock_activity_chart, menu);
        MenuItem item = menu.findItem(R.id.action_sort);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_time_intervals, R.layout.spinner_text_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:

                        break;
                    case 1:


                        break;
                    case 2:

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner.setAdapter(adapter);

        return true;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = Contract.Quote.QUOTE_COLUMNS.toArray(new String[]{});

        String selection = Contract.Quote.COLUMN_SYMBOL + " = ?";

        String[] selectionArgs = new String[]{symbol};


        return new CursorLoader(
                this,
                Contract.Quote.makeUriForStock(symbol), projection,
                null,
                null,
                null
        );


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        //String label = data.getString(data.getColumnIndexOrThrow(Contract.Quote.COLUMN_PRICE));
        if (cursor != null && cursor.moveToFirst())

            do {

                String a = cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quote.COLUMN_PRICE));

                Toast.makeText(this, a + " nnnn", Toast.LENGTH_SHORT).show();


            }
            while (cursor.moveToNext());
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


}
