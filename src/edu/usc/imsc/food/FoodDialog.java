package edu.usc.imsc.food;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import edu.usc.imsc.spatial.Place;
import edu.usc.imsc.util.Tools;

public class FoodDialog extends Dialog {

	static final int FB_BLUE = 0xFF6D84B4;
	static final LayoutParams DEFAULT_LANDSCAPE = new LayoutParams(460, 260);
	static final LayoutParams DEFAULT_PORTRAIT = new LayoutParams(280, 420);
	static final LayoutParams FILL = new LayoutParams(
			ViewGroup.LayoutParams.FILL_PARENT,
			ViewGroup.LayoutParams.FILL_PARENT);
	static final int MARGIN = 6;
	static final int PADDING = 2;
	static final String FB_ICON = "icon.png";

	private String eventHTML;
	private LinearLayout mContent;
	private TextView mTitle;
	private WebView mWebView;

	private Food food;
//	RatingBar ratingBar;

	public FoodDialog(Context context, Food food) {
		super(context);
		this.food = food;
		eventHTML = generateEventHTML(food);
//		ratingBar = new RatingBar(context);
	}

	private String generateEventHTML(Food food) {
		StringBuilder sb = new StringBuilder();
		sb.append("<h3>" + food.getTitle() + "</h3>");
		String content = new String();
		if (food.getPlace() != null && food.getPlace().trim() != "") {
			content = "<p><b>Place:</b> " + food.getPlace().trim() + "</br>";
		}
		if (food.getTime() != null && food.getTime().trim() != "") {
			content += "<p><b>Time:</b> " + food.getTime().trim() + "</br>";
		}
		if (food.getPhoto() != null && food.getPhoto().trim() != "") {
			content += "<p><img src='" + food.getPhoto() + "' /> </br>";
		}
		content += "<p><b>Rating:</b> " + food.getRating().trim() + "/5</br>";

//		ratingBar.setNumStars(Integer.valueOf(food.getRating()));
		sb.append(content);
		return sb.toString();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mContent = new LinearLayout(getContext());
		mContent.setOrientation(LinearLayout.VERTICAL);
		setUpTitle(FB_BLUE);
		setUpWebView();
		Display display = getWindow().getWindowManager().getDefaultDisplay();
		addContentView(mContent,
				display.getWidth() < display.getHeight() ? DEFAULT_PORTRAIT
						: DEFAULT_LANDSCAPE);
	}

	private void setUpTitle(int color) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Drawable icon = Drawable.createFromStream(getClass().getClassLoader()
				.getResourceAsStream(FB_ICON), FB_ICON);
		mTitle = new TextView(getContext());
		mTitle.setText("Food");
		mTitle.setTextColor(Color.BLACK);
		mTitle.setTypeface(Typeface.DEFAULT_BOLD);

		mTitle.setBackgroundColor(color);// FB_BLUE);
		mTitle.setPadding(MARGIN + PADDING, MARGIN, MARGIN, MARGIN);
		mTitle.setCompoundDrawablePadding(MARGIN + PADDING);
		mTitle.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
		mContent.addView(mTitle);
	}

	private void setUpWebView() {
		mWebView = new WebView(getContext());
		mWebView.setVerticalScrollBarEnabled(true);
		mWebView.setHorizontalScrollBarEnabled(true);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.loadData(eventHTML, "text/html", null);
		mWebView.setLayoutParams(FILL);
		mContent.addView(mWebView);
//		mContent.addView(ratingBar);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.i(Tools.TAG, "PlaceDialog" + "onTouchEvent is happening.");
		this.dismiss();
		return super.onTouchEvent(event);
	}
}
