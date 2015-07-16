package edu.usc.imsc.activities;

import android.os.Bundle;
import android.widget.GridView;

import edu.usc.imsc.R;

import edu.usc.imsc.facebook.SocialButtonsAdapter;

public class SocialActivity extends AbstractActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.social_title);
		setContentView(R.layout.social);
		GridView gridview = (GridView) findViewById(R.id.infogridview);
		gridview.setAdapter(new SocialButtonsAdapter(this));
	}
	
	@Override
	public void refresh(boolean fromCache) {
		// TODO Auto-generated method stub
		
	}
}
