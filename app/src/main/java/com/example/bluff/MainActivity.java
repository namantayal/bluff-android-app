package com.example.bluff;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    private boolean first=true;
    Player[] players=new Player[4];
    public static ArrayList<Card> cards_list;
    public int dipToPx(int dip){
        Resources r=getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dip,r.getDisplayMetrics());
    }

    public void deleteCard(int cardNum){
            players[0].player_cards.remove(Integer.valueOf(cardNum));
            play();
    }

    public void test(View view){
        play();
    }

    public void addCard(int cardNum, int top_margin){
        LinearLayout cardLayout=findViewById(R.id.linearLayout);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ImageView card=new ImageView(this);
        card.setMaxHeight(dipToPx(105));
        card.setAdjustViewBounds(true);
        int resId=cards_list.get(cardNum).getResId();
        card.setImageResource(resId);
        card.setTag(cardNum);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.animate().translationY(-100f).setDuration(600);
                v.animate().alpha(0f).setDuration(600);
                new CountDownTimer(600,1){

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        deleteCard(Integer.parseInt(v.getTag().toString()));
                    }
                }.start();
            }
        });
        if(first) {
            params.setMargins(0, 0, 0, dipToPx(-30));
            first=false;
        }
        else
            params.setMargins(dipToPx(-38), 0, 0, dipToPx(top_margin));


        cardLayout.addView(card,params);
        int count=cardLayout.getChildCount();
        float tilt= (float) ((count/2)*(-1.1));
        for(int i=0;i<count;i++){
            ImageView temp=(ImageView) cardLayout.getChildAt(i);
            temp.setRotation(tilt);
            tilt+=1.1;
        }

    }

    public void play(){
        LinearLayout cardLayout=findViewById(R.id.linearLayout);
        cardLayout.removeAllViews();
        first=true;
        int top_margin=-29;
        int num_cards=players[0].player_cards.size();
        for(int i=0;i<num_cards;i++){
            int card_num=players[0].player_cards.get(i);
            if(i<=num_cards/2)
                top_margin+=1;
            else
                top_margin-=1;
            addCard(card_num,top_margin);
        }
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Player.numOfPlayer=4;
        for(int i=0;i<Player.numOfPlayer;i++){
            players[i]=new Player();
        }
        Card.initialize(this);
        Card.shuffle_distribute(players);
    }
}
