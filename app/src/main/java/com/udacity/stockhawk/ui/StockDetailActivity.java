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

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;


public class StockDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private String symbol;
    private ArrayList<BarEntry> _stockList=new ArrayList<>();
    private ArrayList<String> _xVals = new ArrayList<>();
    private LineChart _stockChart;
    List<Entry> _yVals = new ArrayList<>();
    List<Float> timeData = new ArrayList<>();
    List<Float> stockPrice = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);
        _stockChart = (LineChart) findViewById(R.id.chart);


        symbol = getIntent().getStringExtra("symbol");
        Bundle bundle = new Bundle();
        bundle.putString("Symbol",symbol);
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
                        getTimeBasedStocksList(0);
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
        String SelectedSymbol= args.getString("Symbol");

        String[] selectionArgs = new String[]{SelectedSymbol};


        return new CursorLoader(
                this,
                Contract.Quote.makeUriForStock(SelectedSymbol), projection,
                null,
                null,
                Contract.Quote._ID + " ASC LIMIT 2"
        );
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
//        Calendar to = Calendar.getInstance();
//        Calendar from = Calendar.getInstance();
//        Stock google = null;
//        from.add(Calendar.YEAR, -1);
//
//        try {
//            google = YahooFinance.get(symbol);
//            List<HistoricalQuote> googleHistQuotes = google.getHistory(from, to, Interval.DAILY);
//            Toast.makeText(this,"",Toast.LENGTH_SHORT).show();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        if (cursor != null && cursor.moveToFirst())
            do {


                String history = cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quote.COLUMN_HISTORY));
                String[] dataPairs = history.split("\n");
                int i=0;
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
                    _xVals.add(stringDate);


                    Entry entry = new Entry(i,Float.valueOf(splitData[1]));
                    ++i;
                    _yVals.add(entry);
                }



            }
            while (cursor.moveToNext());
     setData();


    }
    private void setData() {


        LineDataSet dataSet;

        // create a dataset and give it a type
        dataSet = new LineDataSet(_yVals, symbol);
        dataSet.setFillAlpha(110);
         dataSet.setFillColor(Color.WHITE);

        // set the line to be drawn like this "- - - - - -"
         dataSet.enableDashedLine(10f, 5f, 0f);
         dataSet.enableDashedHighlightLine(10f, 5f, 0f);
        dataSet.setColor(Color.BLUE);
        dataSet.setCircleColor(Color.WHITE);
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
        leftAxis.setAxisMaxValue(1000f);
        leftAxis.setAxisMinValue(0f); // to set minimum yAxis
        leftAxis.setStartAtZero(false);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawLimitLinesBehindData(true);
        leftAxis.setDrawGridLines(true);
        _stockChart.getAxisRight().setEnabled(false);


//-----------------
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

        // create a data object with the datasets
        LineData data = new LineData( dataSet);

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

    private void getTimeBasedStocksList(int choice)
    {
        Calendar to = Calendar.getInstance();
        Calendar from = Calendar.getInstance();

        switch (choice)
        {
            case 0: // For One year stock data.

                break;
            case 1: //For 6 Months
                break;
            case 2: // For One month
                break;
            case 3: //For one week
                break;
            default:

        }
    }




}
