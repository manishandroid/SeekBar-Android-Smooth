
package com.example.customseekbarsample;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.android.customseekbar.CustomSeekBar;

public class MainActivity extends Activity
{
    private CustomSeekBar seekBar;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.textView1);
        seekBar = (CustomSeekBar) findViewById(R.id.custom_seekbar);
        seekBar.setBarColor(0xff27839a);
        seekBar.setBarWeight(3.0f);
        seekBar.setConnectingLineColor(0xffffffff);
        seekBar.setConnectingLineWeight(3.0f);
        seekBar.setThumbColorPressed(0xffffffff);
        seekBar.setThumbColorNormal(0xffffffff);
        seekBar.setThumbRadius(-1);
        seekBar.setThumbImageNormal(R.drawable.seek_thumb_normal_new);
        seekBar.setThumbImagePressed(R.drawable.seek_thumb_pressed_new);

        seekBar.setOnSeekBarChangeListener(new CustomSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onIndexChangeListener(CustomSeekBar rangeBar, int leftThumbIndex){
            	Toast.makeText(MainActivity.this, ""+leftThumbIndex, Toast.LENGTH_SHORT).show();
            	textView.setText("Progress :: "+leftThumbIndex); 
            }
        });
    }
}
