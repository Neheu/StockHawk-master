package com.udacity.stockhawk.stockwidget;

/**
 * Created by Neha on 27-04-2017.
 */

import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

public class StockWidgetService extends RemoteViewsService {
//This will be used to get remote views for the stocks..
    @Override
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StockWidgetFactory(getApplicationContext(),intent);

    }
}
