package com.udacity.stockhawk.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class StockDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private String symbol;
    private ArrayList<BarEntry> _stockList=new ArrayList<>();
    private ArrayList<String>_dateDataList = new ArrayList<>();
    private LineChart _stockChart;
    List<Entry> entries = new ArrayList<>();
    List<Float> timeData = new ArrayList<>();
    List<Float> stockPrice = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);
        _stockChart = (LineChart) findViewById(R.id.chart);


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
        if (cursor != null && cursor.moveToFirst())
            do {

                String history = cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quote.COLUMN_HISTORY));
                String[] dataPairs = history.split("\n");


                for(String data: dataPairs)
                {
                    String splitData[] = data.split(",");
                    timeData.add(Float.valueOf(splitData[0]));
                    stockPrice.add(Float.valueOf(splitData[1]));

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(Long.parseLong(splitData[0]));

                        int mYear = calendar.get(Calendar.YEAR);
                        int mMonth = calendar.get(Calendar.MONTH);
                        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
                        String stringDate = String.valueOf(mYear)
                                +"/"+ String.valueOf(mMonth)
                                +"/"+String.valueOf(mDay);

                        _dateDataList.add(stringDate);

                    Entry entry = new Entry(Float.valueOf(splitData[1]),Float.valueOf(splitData[0]));
                    entries.add(entry);
                }



            }
            while (cursor.moveToNext());
       // LineDataSet dataSet = new LineDataSet(entries,"Lable...");
       // _stockChart.sets
//        ArrayList<ILineDataSet> sets = (ArrayList<ILineDataSet>) _stockChart.getData()
//                .getDataSets();
//        for (ILineDataSet set : sets) {
//            set.setDrawFilled(true);
//        }

       // LineData data = new LineData(sets);
       // _stockChart.setData(data);
        _stockChart.animateXY(2000, 2000);
       // LineData data = new LineData(getXAxisValues(),getDataSet());

       // setUpChart(_stockChart, data);

        _stockChart.invalidate();

    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }







}
