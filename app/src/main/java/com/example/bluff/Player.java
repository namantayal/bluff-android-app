package com.example.bluff;

import android.app.Activity;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.bluff.Play4.cardsDetail;

class Player {
    String name;
    static int noOfplayer;
    int id;
    boolean pass;
    private Activity mainActivity;
    ArrayList<Integer> playerCards=new ArrayList<>();
    ArrayList<Integer> selectedCards=new ArrayList<>();

    Player(Activity activity,int id,String name) {
        mainActivity=activity;
        this.id=id;
        pass=false;
        this.name=name;
        pass=false;
    }

    private int dipToPx(int dip){
        Resources r=mainActivity.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dip,r.getDisplayMetrics());
    }

    void animateCards(){
        LinearLayout cardLayout=mainActivity.findViewById(R.id.linearLayout);
        for(int i=0;i<cardLayout.getChildCount();i++){
            ImageView card=(ImageView) cardLayout.getChildAt(i);
            card.setAlpha(0f);
        }
        for(int i=0;i<cardLayout.getChildCount();i++) {
            int delay = i * 150;
            final ImageView card = (ImageView) cardLayout.getChildAt(i);
            card.setTranslationY(-70);
            card.animate().setStartDelay(delay).alpha(1).translationY(0).setDuration(150).start();
        }
    }

    void displayCards(ArrayList<Integer> cards){
        LinearLayout cardLayout=mainActivity.findViewById(R.id.linearLayout);
        for(int i=0;i<cards.size();i++) {
            cardsDetail.get(cards.get(i)).selected=false;
            int cardNum=cards.get(i);
            ImageView card = new ImageView(mainActivity);
            card.setMaxHeight(dipToPx(105));
            card.setAdjustViewBounds(true);
            int resId = cardsDetail.get(cardNum).resId;
            card.setImageResource(resId);
            card.setTag(cardNum);
            card.setId(View.generateViewId());
            cardsDetail.get(cardNum).viewId=card.getId();
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    selectCard(Integer.parseInt(v.getTag().toString()), (ImageView) v);
                }
            });
            cardLayout.addView(card);
        }
        arrangeCards();
    }

    void arrangeCards(){
        LinearLayout cardLayout=mainActivity.findViewById(R.id.linearLayout);
        int maxHeight;
        int bottomMargin = -33;
        float tiltAdjustment=1.1f;
        int count=cardLayout.getChildCount();
        if(playerCards.size()<5){
            maxHeight = 113;
            tiltAdjustment=1.4f;
        }
        else if(playerCards.size()<10){
            maxHeight = 110;
            tiltAdjustment=1.2f;
        }
        else if(playerCards.size()<15) {
            maxHeight = 105;
            tiltAdjustment = 1f;
        }
        else if(playerCards.size()<25){
            maxHeight = 95;
            tiltAdjustment = 0.9f;
        }
        else if(playerCards.size()<35) {
            maxHeight = 88;
            tiltAdjustment = 0.8f;
        }
        else{
            maxHeight = 80;
            tiltAdjustment = 0.7f;
        }
        float tilt= ((count/2)*(-tiltAdjustment));
        for(int i=0;i<count;i++){
            ImageView card=(ImageView) cardLayout.getChildAt(i);

            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            if(i!=0)
                params.leftMargin=dipToPx(-39);

            params.bottomMargin=dipToPx(bottomMargin);

            if (i <= playerCards.size() / 2)
                bottomMargin +=1;
            else
                bottomMargin -=1;
            card.setMaxHeight(dipToPx(maxHeight));
            card.setRotation(tilt);
            tilt+=tiltAdjustment;
            card.setLayoutParams(params);
        }
    }

    void setClickCards(boolean clickState){
        LinearLayout cardLayout=mainActivity.findViewById(R.id.linearLayout);
        LinearLayout buttonLayout=mainActivity.findViewById(R.id.playerButtons);
        for(int i=0;i<cardLayout.getChildCount();i++){
            ImageView card=(ImageView) cardLayout.getChildAt(i);
            card.setClickable(clickState);
        }
        for(int i=0;i<buttonLayout.getChildCount();i++){
            Button button=(Button) buttonLayout.getChildAt(i);
            button.setClickable(clickState);
        }
    }

    private void selectCard(int cardNum,ImageView view){
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        Button removeButton=mainActivity.findViewById(R.id.playCards);
        if(!cardsDetail.get(cardNum).selected) {
            if(selectedCards.size()<4) {
                params.bottomMargin += dipToPx(30);
                selectedCards.add(cardNum);
                cardsDetail.get(cardNum).selected = true;
                view.setLayoutParams(params);
            }
            else
                Toast.makeText(mainActivity.getApplicationContext(),"Can't Select more than 4",Toast.LENGTH_SHORT).show();
        }
        else{
            params.bottomMargin-=dipToPx(30);
            selectedCards.remove(Integer.valueOf(cardNum));
            cardsDetail.get(cardNum).selected = false;
            view.setLayoutParams(params);
        }
        if(selectedCards.isEmpty())
            removeButton.setClickable(false);
        else
            removeButton.setClickable(true);
    }
}
