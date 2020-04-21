package com.example.bluff;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import android.os.Handler;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.view.View;
import android.widget.LinearLayout;

import android.widget.TextView;

import java.util.ArrayList;


public class Play4 extends AppCompatActivity {
    CenterTable centerTable;
    public static ArrayList<Card> cardsDetail;
    static Bot[] bots=new Bot[5];
    Player player;
    int noOfplayers;
    final Handler handler=new Handler();

    private Runnable botPlay=new Runnable() {
        @Override
        public void run() {
            int chance=centerTable.nextPlayerId;
            if(chance!=Bot.noOfBots && !centerTable.isWin()) {
                centerTable.nextPlayerId += 1;
                centerTable.showTurn();
                bots[chance].play();
                handler.postDelayed(this,3000);
            }
            else {
                handler.removeCallbacks(this);
                play();
            }
        }
    };

    public void claimCall(View view){
        LinearLayout claimLayout=findViewById(R.id.numbers);
        TextView claimByUser=(TextView) view;
        int cardNum=Integer.parseInt(claimByUser.getTag().toString());
        centerTable.setClaimNumber(cardNum);
        centerTable.displayCards(player.selectedCards);
        player.selectedCards.clear();
        claimLayout.setVisibility(View.GONE);
        play();

    }

    public  void playCards(View view) {
        centerTable.lastPlayerId=Bot.noOfBots;
        LinearLayout cardLayout=findViewById(R.id.linearLayout);
        for(int i=0;i<player.selectedCards.size();i++){
            int cardNum=player.selectedCards.get(i);
            ImageView imageView=findViewById(cardsDetail.get(cardNum).viewId);
            cardLayout.removeView(imageView);
        }
        cardLayout.invalidate();
        player.arrangeCards();
        player.playerCards.removeAll(player.selectedCards);
        if(centerTable.newChance){
            LinearLayout claimLayout=findViewById(R.id.numbers);
            centerTable.newChance=false;
            claimLayout.bringToFront();
            claimLayout.setVisibility(View.VISIBLE);
        }
        else{
            centerTable.displayCards(player.selectedCards);
            player.selectedCards.clear();
            play();
        }
    }

    public void pass(View view){
        if(!player.pass){
            centerTable.passCount++;
            player.pass=true;
        }
        centerTable.checkPassAll();
        play();
    }

    public void check(View view){
        centerTable.check();
        play();
    }

    public void play(){
        if(!centerTable.isWin()) {
            int turn = centerTable.nextPlayerId;
            if (turn == Bot.noOfBots) {
                centerTable.nextPlayerId=0;
                player.setClickCards(true);
            }
            else{
                centerTable.showTurn();
                player.setClickCards(false);
                handler.postDelayed(botPlay, 3000);
            }
        }
    }

    public void test(View view){
        LinearLayout claimShowLayout=findViewById(R.id.claimSetLayout);
        view.setVisibility(View.GONE);
        player.displayCards(player.playerCards);
        player.animateCards();
        claimShowLayout.setVisibility(View.VISIBLE);
        centerTable.nextPlayerId=Bot.noOfBots;
        centerTable.showTurn();
        play();
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play4_layout);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        player=new Player(this,0,"player");
        centerTable = new CenterTable(this,player);
        cardsDetail = new ArrayList<>();
        noOfplayers=getIntent().getIntExtra("numOfPlayers",4);
        Player.noOfplayer=1;
        Bot.noOfBots=noOfplayers-1;
        String name="Bot ";
        for(int i=0;i<Bot.noOfBots;i++){
            name=name+(Player.noOfplayer+i);
            bots[i]=new Bot(name,i,centerTable);
        }
        Card.initialize(this);
        CenterTable.shuffle_distribute(player,bots);
    }
}
