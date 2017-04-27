package com.udacity.stockhawk.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.udacity.stockhawk.R;

/**
 * Created by Neha on 26-04-2017.
 */

public class StockDetailFragment extends Fragment
{

    WebView webView;
    String stock;


    public StockDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.detail_fragment, container, false);

        Bundle symbol = getArguments();
        stock = symbol.getString("Symbol");
        webView = (WebView) rootView.findViewById(R.id.webview_chart);
        webView.getSettings().setJavaScriptEnabled(true);
        String s = "http://empyrean-aurora-455.appspot.com/charts.php?symbol="+stock;
        webView.loadUrl(s);
        return rootView;
    }
}
