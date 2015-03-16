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
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.os.SystemProperties;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class MainActivity extends Activity {

	private static final String TAG = "dynamicaudiooutput";
	private static final String CMD_AUDIO_1 = "sys.dynoutput.mode";
	private static final String CMD_AUDIO_2 = "sys.dynoutput.forceuse";
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

		int retCmd = 0;
		try {
			retCmd = SystemProperties.getInt(CMD_AUDIO_2, 0);
		} catch (Exception e) {
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
		} catch (Exception e) {
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

		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup radioGroup, int i) {
			int selectedRadioId = radioGroup.getCheckedRadioButtonId();
			switch(selectedRadioId) {
			    case R.id.speaker:
				Toast.makeText(getApplicationContext(), "Speaker", Toast.LENGTH_SHORT).show();
				try {
				    SystemProperties.set(CMD_AUDIO_2, SPEAKER_VAL);
				    Toast.makeText(getApplicationContext(),"setprop: "+ CMD_AUDIO_2 +" "+SPEAKER_VAL,Toast.LENGTH_LONG).show();
				} catch (Exception e) {
				    Toast.makeText(getApplicationContext(), "Exception", Toast.LENGTH_SHORT).show();
				}
				break;
			    case R.id.hdmiaudio:
				Toast.makeText(getApplicationContext(), "HDMI Audio", Toast.LENGTH_SHORT).show();
				try {
				    SystemProperties.set(CMD_AUDIO_2, HDMI_VAL);
				    Toast.makeText(getApplicationContext(),"setprop: "+ CMD_AUDIO_2 +" "+HDMI_VAL,Toast.LENGTH_LONG).show();
				} catch (Exception e) {
				    Toast.makeText(getApplicationContext(), "Exception", Toast.LENGTH_SHORT).show();
				}
				break;
			    default:
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
				Toast.makeText(getApplicationContext(), "Enable both channel Output", Toast.LENGTH_SHORT).show();
			} else {
				SystemProperties.set(CMD_AUDIO_1, BOTH_OFF);
				Toast.makeText(getApplicationContext(), "Disable both channel Output", Toast.LENGTH_SHORT).show();
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

