package com.example.bluff;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import static com.example.bluff.MainActivity.cards_list;

class Card {
    private static ArrayList<Integer> cards_deck;
    private int resId;

    private void setResId(int resId) {
        this.resId = resId;
    }

    int getResId() {
        return resId;
    }

    static void shuffle_distribute(Player[] players){
        cards_deck.clear();
        for(int i=0;i<52;i++)
            cards_deck.add(i);
        Collections.shuffle(cards_deck);

        boolean flag=true;
        while(flag){
            for(int i=0;i<Player.numOfPlayer;i++){
                if(cards_deck.isEmpty()){
                    flag=false;
                    break;
                }
                else {
                    players[i].player_cards.add(cards_deck.get(0));
                    cards_deck.remove(0);
                }
            }
        }

    }
    static void initialize(Context ctx){
        cards_list=new ArrayList<>();
        cards_deck=new ArrayList<>();

        for(int i=1;i<=13;i++){
            String card_name="club_" + i;
            int drawableResourceId = ctx.getResources().getIdentifier(card_name, "drawable", ctx.getPackageName());
            Card card=new Card();
            card.setResId(drawableResourceId);
            cards_list.add(card);
        }
        for(int i=1;i<=13;i++){
            String card_name="heart_" + i;
            int drawableResourceId = ctx.getResources().getIdentifier(card_name, "drawable", ctx.getPackageName());
            Card card=new Card();
            card.setResId(drawableResourceId);
            cards_list.add(card);
        }
        for(int i=1;i<=13;i++){
            String card_name="spade_" + i;
            int drawableResourceId = ctx.getResources().getIdentifier(card_name, "drawable", ctx.getPackageName());
            Card card=new Card();
            card.setResId(drawableResourceId);
            cards_list.add(card);
        }
        for(int i=1;i<=13;i++){
            String card_name="diamond_" + i;
            int drawableResourceId = ctx.getResources().getIdentifier(card_name, "drawable", ctx.getPackageName());
            Card card=new Card();
            card.setResId(drawableResourceId);
            cards_list.add(card);
        }
    }
}
