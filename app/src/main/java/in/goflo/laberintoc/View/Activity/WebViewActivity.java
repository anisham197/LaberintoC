package in.goflo.laberintoc.View.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import in.goflo.laberintoc.R;
import in.goflo.laberintoc.View.JavaScriptInterface;

public class WebViewActivity extends AppCompatActivity {

    WebView webView;
    String locationID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        locationID = getIntent().getStringExtra(getString(R.string.locationID));

        webView = findViewById(R.id.web_view);
        webView.loadUrl("file:///android_asset/map.html");
        webView.addJavascriptInterface(new JavaScriptInterface(this, locationID), "Android");

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
    }
}
