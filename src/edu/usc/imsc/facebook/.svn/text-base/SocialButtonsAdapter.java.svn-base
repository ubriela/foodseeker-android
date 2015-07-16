package edu.usc.imsc.facebook;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import edu.usc.imsc.R;
import edu.usc.imsc.statistic.Statistic;
import edu.usc.imsc.statistic.StatisticDialog;
import edu.usc.imsc.statistic.StatisticJsonParser;
import edu.usc.imsc.util.Tools;

public class SocialButtonsAdapter extends BaseAdapter {
	private Context mContext;

	public SocialButtonsAdapter(Context c) {
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
				case 0: // visitor
					if (DataStorage.isLoggedIn()) {
						StatisticJsonParser jsonParser = new StatisticJsonParser(
								mContext);
						List<Statistic> statistics = jsonParser
								.parseUSCEvent(jsonParser.retrieveStream(Tools
										.getStatisticURL(String
												.valueOf(DataStorage.getMe()
														.getId()))));
						if (statistics.size() > 0) {
							StatisticDialog dialog = new StatisticDialog(
									mContext, statistics.get(0));
							dialog.show();
						}
					} else {
						new AlertDialog.Builder(mContext)
								.setMessage("You need login to view status!")
								.setTitle("Status").setCancelable(true).show();
					}
					break;
				// case 1: //vendor
				// new AlertDialog.Builder(mContext)
				// .setMessage("The button is: " + ((Button) v).getText())
				// .setTitle("Button Clicked!")
				// .setCancelable(true)
				// .show();
				// break;

				case 1: // facebook
					mContext.startActivity(new Intent(mContext,
							FacebookActivity.class));
					break;
				// case 3: //twitter
				// new AlertDialog.Builder(mContext)
				// .setMessage("The button is: " + ((Button) v).getText())
				// .setTitle("Button Clicked!")
				// .setCancelable(true)
				// .show();
				// break;

				default:

					break;
				}

			}

		});

		return button;
	}

	public String[] buttonNames = { "Visitor Information",
			// "Vendors List",
			"Facebook Signup"
	// "Twitter Feeds"
	};

	// references to our images
	private Integer[] mThumbIds = { R.drawable.info,
			// R.drawable.vendor_list,
			R.drawable.fb
	// R.drawable.twitter
	};

}
