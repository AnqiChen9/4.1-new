package com.example.timer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvLastTime,tvCurTime;
    ImageView ivStart,ivStop,ivPause;
    EditText input;
    private int timeNum=0;
    boolean isStart=false;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input=findViewById(R.id.etInput);
        tvLastTime=findViewById(R.id.lasttime);
        tvCurTime=findViewById(R.id.curtime);
        ivStart=findViewById(R.id.ivStart);
        ivPause=findViewById(R.id.ivPause);
        ivStop=findViewById(R.id.ivStop);
        ivStart.setOnClickListener(this);
        ivPause.setOnClickListener(this);
        ivStop.setOnClickListener(this);

        sp = getSharedPreferences("setting", Context.MODE_PRIVATE);
        String t = sp.getString("t",null);
        String tt= sp.getString("tt",null);
        if (t!=null)
         tvLastTime.setText("you spent "+t+" on "+tt+" last time");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivStart:
                isStart=true;
                mHandler.removeMessages(1000);
                mHandler.sendEmptyMessage(1000);
                break;
            case R.id.ivPause:
                isStart=false;
                mHandler.removeMessages(1000);
                break;
            case R.id.ivStop:
                String task=input.getText().toString();
                if (task.isEmpty()){
                    Toast.makeText(this, "input task type error!", Toast.LENGTH_SHORT).show();
                    return;
                }
                isStart=false;
                mHandler.removeMessages(1000);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("t",tvCurTime.getText().toString()).commit();
                editor.putString("tt",task).commit();
                Toast.makeText(this, "save successfully!", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1000:
                    timeNum = timeNum + 1;
                    String time = getTime();

                    tvCurTime.setText(time);
                    if (isStart)
                        mHandler.sendEmptyMessageDelayed(1000, 1000);

                    break;

            }

        }
    };
    private String getTime() {
        int h = timeNum / 3600;
        int m = (timeNum - h * 3600) / 60;
        int s = (timeNum - h * 3600 - m * 60);
        return changeNumToStr(h)+":"+changeNumToStr(m)+":"+changeNumToStr(s);

    }
    private String changeNumToStr(int num){
        if (num<10)
            return "0"+num;
        else
            return num+"";
    }
}