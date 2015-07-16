package edu.usc.imsc.activities;

import edu.usc.imsc.R;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class FoodTabsActivity extends TabActivity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.foodtabs);

		Resources res = getResources(); // Resource object to get Drawables
		TabHost tabHost = getTabHost(); // The activity TabHost
		TabHost.TabSpec spec; // Resusable TabSpec for each tab
		Intent intent; // Reusable Intent for each tab

		// Create an Intent to launch an Activity for the tab (to be reused)
		intent = new Intent().setClass(this, NearbyFoodsActivity.class);
		
		Intent i = new Intent(this, FoodTabsActivity.class);
		
		// Initialize a TabSpec for each tab and add it to the TabHost
		//map tab as default
		spec = tabHost.newTabSpec("nearby").setIndicator("Nearby",
						res.getDrawable(R.drawable.tab_nearby)).setContent(intent);
		tabHost.addTab(spec);

		// information tab
		intent = new Intent().setClass(this, TopFoodsActivity.class);
		spec = tabHost.newTabSpec("top").setIndicator("Top",
						res.getDrawable(R.drawable.tab_top)).setContent(intent);
		tabHost.addTab(spec);
		
		//settings
		intent = new Intent().setClass(this, RecentFoodsActivity.class);
		spec = tabHost.newTabSpec("recent").setIndicator("Recent",
						res.getDrawable(R.drawable.tab_recent)).setContent(intent);
		tabHost.addTab(spec);

		tabHost.setCurrentTab(0);
	}
	
	
}
