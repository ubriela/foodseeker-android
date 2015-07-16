/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.usc.imsc.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import edu.usc.imsc.R;

public class GoogleMapLayersActivity extends Activity {
	private static final int DIALOG_YES_NO_MESSAGE = 1;
	private static final int DIALOG_YES_NO_LONG_MESSAGE = 2;
	private static final int DIALOG_LIST = 3;
	private static final int DIALOG_PROGRESS = 4;
	private static final int DIALOG_SINGLE_CHOICE = 5;
	private static final int DIALOG_MULTIPLE_CHOICE = 6;
	private static final int DIALOG_TEXT_ENTRY = 7;
	private static final int DIALOG_MULTIPLE_CHOICE_CURSOR = 8;

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_MULTIPLE_CHOICE:
			return new AlertDialog.Builder(GoogleMapLayersActivity.this)
					.setIcon(R.drawable.ic_popup_reminder)
					.setTitle(R.string.dialog_multi_choice)
					.setMultiChoiceItems(
							R.array.mapLayerTypes,
							new boolean[] { false, true, false, true, false,
									false, false },
							new DialogInterface.OnMultiChoiceClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton, boolean isChecked) {

									/* User clicked on a check box do some stuff */
								}
							})
					.setPositiveButton(R.string.dialog_multi_choice_ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									/* User clicked Yes so do some stuff */
								}
							})
					.setNegativeButton(R.string.dialog_multi_choice_cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									/* User clicked No so do some stuff */
								}
							}).create();
		}
		return null;
	}

	/**
	 * Initialization of the Activity after it is first created. Must at least
	 * call {@link android.app.Activity#setContentView(int)} to describe what is
	 * to be displayed in the screen.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.google_map_layers);

		/* Display a list of checkboxes */
		Button checkBox = (Button) findViewById(R.id.checkbox_button);
		checkBox.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(DIALOG_MULTIPLE_CHOICE);
			}
		});

	}
}
