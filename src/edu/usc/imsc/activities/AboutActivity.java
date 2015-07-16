package edu.usc.imsc.activities;

import edu.usc.imsc.R;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class AboutActivity extends Activity {
    
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        
        setContentView(R.layout.about);
        setTitle("CSCI 587");
        
        final String mimeType = "text/html";
        final String encoding = "utf-8";
        
        WebView wv;
        
        wv = (WebView) findViewById(R.id.wv1);
        wv.loadData("Contact: Hien To, hto@usc.edu", mimeType, encoding);
        
        wv = (WebView) findViewById(R.id.wv2);
        wv.loadData("Lian Liu, lian@usc.edu", mimeType, encoding);
    }
}