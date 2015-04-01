/*
 * Copyright © 2015 Intel Corporation
 * All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice (including the next
 * paragraph) shall be included in all copies or substantial portions of the
 * Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 *
 * Authors:
 *    Dennis Dominic <dennis.dominic@intel.com>
 *
 */

package com.intel.dynamicaudiooutputsettings;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemProperties;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.util.Log;

public class MainActivity extends Activity {

	private static final String TAG = "dynamicaudiooutput";
	private static final String CMD_AUDIO_1 = "persist.sys.daudioout.mode";
	private static final String CMD_AUDIO_2 = "persist.sys.daudioout.forceuse";
	private static final String CMD_AUDIO_3 = "persist.sys.daudioout.alt";
	private static final String CMD_AUDIO_4 = "persist.sys.daudioout.altapp";
	private static final String DEFAULT_VAL = "0";
	private static final String SPEAKER_VAL = "1";
	private static final String HDMI_VAL = "2";
	private static final String BOTH_ON = "1";
	private static final String BOTH_OFF = "0";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radioGroupOutput);
		final CheckBox checkBox = (CheckBox) findViewById(R.id.Bothout);
		final EditText editText = (EditText) findViewById(R.id.secappname);
		final Button buttonSave = (Button)findViewById(R.id.secappnamesave);
		final RadioGroup radioGroupSec = (RadioGroup)findViewById(R.id.radioGroupOutputSecondary);

		int retCmd = 0;
		try {
			retCmd = SystemProperties.getInt(CMD_AUDIO_2, 0);
			Log.d(TAG, "getprop: "+ CMD_AUDIO_2 + " = " + retCmd);
		} catch (Exception e) {
			Log.e(TAG, "Exception on getprop: "+ CMD_AUDIO_2);
			retCmd = 0;
		}

		switch(retCmd) {
			case 1:
				radioGroup.check(R.id.speaker);
				break;
			case 2:
				radioGroup.check(R.id.hdmiaudio);
				break;
			default:
				radioGroup.check(R.id.sysdefault);
				break;
		}
		retCmd = 0;

		try {
			retCmd = SystemProperties.getInt(CMD_AUDIO_1, 0);
			Log.d(TAG, "getprop: "+ CMD_AUDIO_1 + " = " + retCmd);
		} catch (Exception e) {
			Log.e(TAG, "Exception on getprop: "+ CMD_AUDIO_1);
			retCmd = 0;
		}

		switch(retCmd) {
			case 0:
			default:
				checkBox.setChecked(false);
				break;
			case 1:
				checkBox.setChecked(true);
				break;
		}

		String mAppName = SystemProperties.get(CMD_AUDIO_4);
                editText.setText(mAppName);

		try {
			retCmd = SystemProperties.getInt(CMD_AUDIO_3, 0);
			Log.d(TAG, "getprop: "+ CMD_AUDIO_3 + " = " + retCmd);
		} catch (Exception e) {
			Log.e(TAG, "Exception on getprop: "+ CMD_AUDIO_3);
			retCmd = 0;
		}

		switch(retCmd) {
			case 1:
				radioGroupSec.check(R.id.secspeaker);
				break;
			case 2:
				radioGroupSec.check(R.id.sechdmiaudio);
				break;
			default:
				radioGroupSec.check(R.id.secsysdefault);
				break;
		}
		retCmd = 0;
	
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup radioGroup, int i) {
			int selectedRadioId = radioGroup.getCheckedRadioButtonId();
			switch(selectedRadioId) {
			    case R.id.speaker:
					try {
						SystemProperties.set(CMD_AUDIO_2, SPEAKER_VAL);
					} catch (Exception e) {
						Log.e(TAG, "Exception on setprop: "+ CMD_AUDIO_2 +" "+SPEAKER_VAL);
					}
					break;
			    case R.id.hdmiaudio:
					try {
						SystemProperties.set(CMD_AUDIO_2, HDMI_VAL);
					} catch (Exception e) {
						Log.e(TAG, "Exception on setprop: "+ CMD_AUDIO_2 +" "+HDMI_VAL);
					}
					break;
			    default:
			    case R.id.sysdefault:
					try {
						SystemProperties.set(CMD_AUDIO_2, DEFAULT_VAL);
					} catch (Exception e) {
						Log.e(TAG, "Exception on setprop: "+ CMD_AUDIO_2 +" "+DEFAULT_VAL);
					}
					break;
			}
		}
		});

		checkBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if (checkBox.isChecked()) {
				checkBox.setChecked(true);
				SystemProperties.set(CMD_AUDIO_1, BOTH_ON);
			} else {
				SystemProperties.set(CMD_AUDIO_1, BOTH_OFF);
			}
		}
		});

		buttonSave.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				SystemProperties.set(CMD_AUDIO_4, editText.getText().toString());
			}
		});
		
		radioGroupSec.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup radioGroupSec, int i) {
			int selectedRadioId = radioGroupSec.getCheckedRadioButtonId();
			switch(selectedRadioId) {
			    case R.id.secspeaker:
					try {
						SystemProperties.set(CMD_AUDIO_3, SPEAKER_VAL);
					} catch (Exception e) {
						Log.e(TAG, "Exception on setprop: "+ CMD_AUDIO_3 +" "+SPEAKER_VAL);
					}
					break;
			    case R.id.sechdmiaudio:
					try {
						SystemProperties.set(CMD_AUDIO_3, HDMI_VAL);
					} catch (Exception e) {
						Log.e(TAG, "Exception on setprop: "+ CMD_AUDIO_3 +" "+HDMI_VAL);
					}
					break;
			    default:
			    case R.id.secsysdefault:
					try {
						SystemProperties.set(CMD_AUDIO_3, DEFAULT_VAL);
					} catch (Exception e) {
						Log.e(TAG, "Exception on setprop: "+ CMD_AUDIO_3 +" "+DEFAULT_VAL);
					}
					break;
			}
		}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

