package edu.usc.imsc.statistic;

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
import android.widget.TextView;


public class StatisticDialog extends Dialog {

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
	Statistic statistic;

	public StatisticDialog(Context context, Statistic statistic) {
		super(context);
		this.statistic = statistic;
		eventHTML = generateEventHTML(statistic);
	}

	private String generateEventHTML(Statistic statistic) {
		StringBuilder sb = new StringBuilder();
		String content = new String();

		content = content.concat("<b>Sessions:</b> " + statistic.getNumberOfSessions() + "</br>");
		content = content.concat("<b>Time:</b> " + statistic.getTimes() + "</br>");
		content = content.concat("<b>Speed:</b> " + statistic.getSpeed() + "</br>");

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
		mTitle.setText("Statistic");
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
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.i("EventDialog", "onTouchEvent is happening.");
		this.dismiss();
		return super.onTouchEvent(event);
	}
}
