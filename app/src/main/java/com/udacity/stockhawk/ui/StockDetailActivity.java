package com.udacity.stockhawk.ui;

import android.database.Cursor;
import android.graphics.Color;
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
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.udacity.stockhawk.R;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;

import com.udacity.stockhawk.data.Contract;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class StockDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private String symbol;
    private ArrayList<BarEntry> _stockList = new ArrayList<>();
    private ArrayList<String> _xVals = new ArrayList<>();
    private LineChart _stockChart;
    List<Entry> _yVals = new ArrayList<>();
    List<Float> timeData = new ArrayList<>();
    List<Float> stockPrice = new ArrayList<>();
    Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);
        _stockChart = (LineChart) findViewById(R.id.chart);


        symbol = getIntent().getStringExtra("symbol");

        getSupportLoaderManager().initLoader(1, bundle, this);


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
                        bundle.putInt("choice", 0);
                        getSupportLoaderManager().initLoader(1, bundle, StockDetailActivity.this);


                        break;
                    case 1:
                        bundle.putInt("choice", 1);
                        getSupportLoaderManager().initLoader(1, bundle, StockDetailActivity.this);

                        break;
                    case 2:
                        bundle.putInt("choice", 2);
                        getSupportLoaderManager().initLoader(1, bundle, StockDetailActivity.this);


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

        return new CursorLoader(
                this,
                Contract.Quote.makeUriForStock(symbol), projection,
                null,
                null,
                null        );
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        float minimumPrice = Float.MAX_VALUE;
        float maximumPrice = Float.MIN_VALUE;
        float price = 0f;

        if (cursor != null && cursor.moveToFirst())
            do {
                String history = cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quote.COLUMN_HISTORY));
                price = Float.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quote.COLUMN_PRICE)));
                String[] dataPairs = history.split("\n");
                int i = 0;
                for (String data : dataPairs) {
                    String splitData[] = data.split(",");
                    timeData.add(Float.valueOf(splitData[0]));
                    stockPrice.add(Float.valueOf(splitData[1]));

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(Long.parseLong(splitData[0]));

                    int mYear = calendar.get(Calendar.YEAR);
                    int mMonth = calendar.get(Calendar.MONTH);
                    int mDay = calendar.get(Calendar.DAY_OF_MONTH);
                    String stringDate = String.valueOf(mYear)
                            + "/" + String.valueOf(mMonth)
                            + "/" + String.valueOf(mDay);
                    _xVals.add(stringDate);


                    Entry entry = new Entry(i, Float.valueOf(splitData[1]));
                    ++i;
                    _yVals.add(entry);
                }


            }
            while (cursor.moveToNext());

        setData(price);


    }

    private void setData(float price) {


        LineDataSet dataSet;

        // create a dataset and give it a type
        dataSet = new LineDataSet(_yVals, symbol);
        dataSet.setFillAlpha(110);
        dataSet.setFillColor((Color.parseColor("#2d374c")));

        // set the line to be drawn like this "- - - - - -"
        dataSet.enableDashedLine(10f, 5f, 0f);
        dataSet.enableDashedHighlightLine(10f, 5f, 0f);
        dataSet.setColor((Color.parseColor("#758cbb")));
        dataSet.setCircleColor((Color.parseColor("#6a84c3")));
        dataSet.setLineWidth(1f);
        dataSet.setCircleRadius(3f);
        dataSet.setDrawCircleHole(false);
        dataSet.setValueTextSize(9f);
        dataSet.setDrawFilled(true);
        _stockChart.setDescription(null);



// - X Axis
        XAxis xAxis = _stockChart.getXAxis();
        //xAxis.setTypeface(tf);
        xAxis.setTextSize(12f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(ColorTemplate.getHoloBlue());
        xAxis.setEnabled(true);
        xAxis.disableGridDashedLine();
        xAxis.setSpaceMin(5);
        xAxis.setDrawGridLines(false);
        xAxis.setAvoidFirstLastClipping(true);


// - Y Axis
        YAxis leftAxis = _stockChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        //leftAxis.setTypeface(tf);//
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setStartAtZero(false);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawLimitLinesBehindData(true);
        leftAxis.setDrawGridLines(true);
        leftAxis.setAxisMaximum(price);
        _stockChart.getAxisRight().setEnabled(false);

        _stockChart.setVisibleXRangeMaximum(10);


//-----------------
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

        // create a data object with the datasets
        LineData data = new LineData(dataSet);

        // set data
        //XAxis xAxis = _stockChart.getXAxis();

        xAxis.setValueFormatter(new StringValueFormatter(_xVals));
        _stockChart.setData(data);
        Legend l = _stockChart.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.LINE);

    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public class StringValueFormatter implements IAxisValueFormatter {

        private ArrayList<String> mValues;

        public StringValueFormatter(ArrayList<String> values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // "value" represents the position of the label on the axis (x or y)
            return mValues.get((int) value);
        }
    }


}
