package edu.usc.imsc.facebook;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import edu.usc.imsc.R;

public class FacebookButtonsAdapter extends BaseAdapter {
	private Context mContext;

	public FacebookButtonsAdapter(Context c) {
		mContext = c;
	}

	public int getCount() {
		return mThumbIds.length;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		Button button;
		if (convertView == null) {
			// if it's not recycled, initialize some attributes
			button = new Button(mContext);
			button.setLayoutParams(new GridView.LayoutParams(100, 130));
			button.setPadding(8, 8, 8, 8);
		} else {
			button = (Button) convertView;
		}

		button.setText(buttonNames[position]);
		button.setTextColor(Color.GRAY);
		button.setCompoundDrawablesWithIntrinsicBounds(0, mThumbIds[position],
				0, 0);
		button.setBackgroundColor(0xffffff);

		button.setId(position);

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				switch (position) {
				case 0:
					mContext.startActivity(new Intent(mContext, FacebookStreamActivity.class));
					break;
				case 1:
					new AlertDialog.Builder(mContext)
							.setMessage(
									"The button is: " + ((Button) v).getText())
							.setTitle("Button Clicked!").setCancelable(true)
							.show();
					break;

				case 2:
					new AlertDialog.Builder(mContext)
							.setMessage(
									"The button is: " + ((Button) v).getText())
							.setTitle("Button Clicked!").setCancelable(true)
							.show();
					break;

				default:

					break;
				}

			}

		});

		return button;
	}

	public String[] buttonNames = { "New Feed", "Profile", "Friends" };

	// references to our images
	private Integer[] mThumbIds = { R.drawable.fb_feeds_64,
			R.drawable.fb_profile_64, R.drawable.fb_friends_64 };

}
