package com.jackchen.view_day07_2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LetterSlideBar.LetterTouchListener{

    private TextView letter_tv;
    private LetterSlideBar letter_slide_bar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        letter_tv = (TextView) findViewById(R.id.letter_tv);
        letter_slide_bar = (LetterSlideBar) findViewById(R.id.letter_slide_bar);
        letter_slide_bar.setOnLetterTouchListener(this);
    }



    @Override
    public void touch(CharSequence letter, boolean isTouch) {
        if (isTouch){
            letter_tv.setVisibility(View.VISIBLE);
            letter_tv.setText(letter);
        }else{
            letter_tv.setVisibility(View.GONE);
        }
    }
}
