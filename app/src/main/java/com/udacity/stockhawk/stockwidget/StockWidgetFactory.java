package com.udacity.stockhawk.stockwidget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

/**
 * Created by Neha on 27-04-2017.
 */
public class StockWidgetFactory implements RemoteViewsService.RemoteViewsFactory {
    private Cursor _cursor;
    private Context _context;
    int _widgetId;
    public StockWidgetFactory(Context applicationContext, Intent intent) {
        this._context = applicationContext;
        _widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        if (_cursor != null) {
            _cursor.close();
        }
        _cursor = _context.getContentResolver().query(Contract.Quote.CONTENT_URI,
                Contract.Quote.QUOTE_COLUMNS.toArray(new String[]{}),
                null,
                null,
                null);
    }

    @Override
    public void onDestroy() {
        if (_cursor != null) {
            _cursor.close();
        }
    }

    @Override
    public int getCount() {
        return _cursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(_context.getPackageName(), R.layout.list_item_quote);
        if (_cursor.moveToPosition(position)) {
            rv.setTextViewText(R.id.symbol,
                    _cursor.getString(_cursor.getColumnIndex(Contract.Quote.COLUMN_SYMBOL)));
            rv.setTextViewText(R.id.price,
                    _cursor.getString(_cursor.getColumnIndex(Contract.Quote.COLUMN_PRICE)));
            rv.setTextViewText(R.id.change,
                    _cursor.getString(_cursor.getColumnIndex(Contract.Quote.COLUMN_PERCENTAGE_CHANGE)));
        }
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
