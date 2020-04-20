package com.example.bluff;

import android.app.Activity;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.bluff.Card.cardsDeck;
import static com.example.bluff.Play4.bots;


class CenterTable {
    private Activity mainActivity;
    int nextPlayerId;
    int lastPlayerId;
    ArrayList<Integer>cardsOnTable=new ArrayList<>();
    ArrayList<Integer>lastPlayedCards=new ArrayList<>();
    int claimNumber;
    private int cardRotation=0;
    private Player player;
    int passCount;
    boolean newChance;

    CenterTable(Activity activity,Player player){
        mainActivity=activity;
        this.player=player;
        claimNumber=-1;
        passCount=0;
        newChance=true;
    }

    private int dipToPx(int dip){
        Resources r=mainActivity.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dip,r.getDisplayMetrics());
    }

    private int getCardRotation(){
        cardRotation+=50;
        if(cardRotation>360)
            cardRotation=50;
        return cardRotation;
    }

    void displayCards(ArrayList<Integer> cards){
        TextView playDetail=mainActivity.findViewById(R.id.playDetail);
        playDetail.setText("Play - "+cards.size());
        playDetail.setAlpha(0f);
        playDetail.animate().alpha(1f).setDuration(100).setStartDelay(50);
        lastPlayedCards.clear();
        lastPlayedCards.addAll(cards);
        cardsOnTable.addAll(cards);
        FrameLayout centerTableLayout=mainActivity.findViewById(R.id.centerTable);
        FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity= Gravity.CENTER;
        for(int i=0;i<lastPlayedCards.size();i++) {
            int delay =(i+1)*300;
            ImageView cardBack = new ImageView(mainActivity.getApplicationContext());
            cardBack.setImageResource(R.drawable.green_back);
            cardBack.setMaxHeight(dipToPx(100));
            cardBack.setAdjustViewBounds(true);
            cardBack.setRotation(getCardRotation());
            centerTableLayout.addView(cardBack,0,params);
            centerTableLayout.bringChildToFront(cardBack);
            cardBack.setAlpha(0f);
            cardBack.animate().setStartDelay(delay).alpha(1f).setDuration(300).start();
        }
    }

    void setClaimNumber(int cardNum){
        claimNumber=cardNum;
        TextView claimTextView=mainActivity.findViewById(R.id.currentClaimText);
        if(cardNum==-1)
            claimTextView.setText("");
        else if(cardNum==0)
            claimTextView.setText("A");
        else if(cardNum==10)
            claimTextView.setText("J");
        else if(cardNum==11)
            claimTextView.setText("Q");
        else if(cardNum==12)
            claimTextView.setText("K");
        else
            claimTextView.setText(String.valueOf(cardNum+1));
    }

    void check(){
        boolean checkFail=true;
        for(int i=0;i<lastPlayedCards.size();i++){
            if((lastPlayedCards.get(i)%13)!=claimNumber){
                checkFail=false;
                if(lastPlayerId==Bot.noOfBots){
                    player.playerCards.addAll(cardsOnTable);
                    player.displayCards(cardsOnTable);
                }
                else{
                    bots[lastPlayerId].botCards.addAll(cardsOnTable);
                }
                break;
            }
        }
        if(checkFail){
            if(nextPlayerId==Bot.noOfBots){
                player.playerCards.addAll(cardsOnTable);
                player.displayCards(cardsOnTable);
            }
            else{
                bots[nextPlayerId].botCards.addAll(cardsOnTable);
            }
            nextPlayerId=lastPlayerId-1;
        }
        reset();
    }

    boolean isWin(){
        boolean flag=false;
        String winner;
        if(player.playerCards.size()==0){
            winner=player.name+" Wins!!";
            flag=true;
            Toast.makeText(mainActivity,winner,Toast.LENGTH_SHORT).show();
        }
        for(int i=0;i<Bot.noOfBots;i++){
            if(bots[i].botCards.size()==0){
                winner=bots[i].name+" Wins!!";
                flag=true;
                Toast.makeText(mainActivity,winner,Toast.LENGTH_SHORT).show();
            }
        }
        return flag;
    }

    boolean checkPassAll(){
        if(passCount==Bot.noOfBots+1){
            nextPlayerId=lastPlayerId;
            reset();
            return true;
        }
        else
            return false;
    }

    void reset(){
        FrameLayout centerTableLayout=mainActivity.findViewById(R.id.centerTable);
        centerTableLayout.removeAllViews();
        passCount=0;
        setClaimNumber(-1);
        cardsOnTable.clear();
        lastPlayedCards.clear();
        newChance=true;
        for(int i=0;i<Bot.noOfBots;i++){
            bots[i].pass=false;
        }
        player.pass=false;
    }

    void showTurn(){
        TextView turnView=mainActivity.findViewById(R.id.turn);
        if(nextPlayerId==Bot.noOfBots)
            turnView.setText("Player Turn");
        else
            turnView.setText("Bot " +(nextPlayerId+1)+" Turn");
        turnView.setAlpha(0f);
        turnView.animate().alpha(1f).setDuration(100).setStartDelay(50);
    }

    static void shuffle_distribute(Player player,Bot[] bots) {
        cardsDeck.clear();
        for (int i = 0; i < 52; i++)
            cardsDeck.add(i);
        Collections.shuffle(cardsDeck);

        boolean flag = true;
        while (flag) {
            if (cardsDeck.isEmpty())
                flag = false;
            else {
                player.playerCards.add(cardsDeck.get(0));
                cardsDeck.remove(0);
                for (int i = 0; i < Bot.noOfBots; i++) {
                    if (cardsDeck.isEmpty()) {
                        flag = false;
                        break;
                    } else {
                        bots[i].botCards.add(cardsDeck.get(0));
                        cardsDeck.remove(0);
                    }
                }
            }
        }
    }

}
