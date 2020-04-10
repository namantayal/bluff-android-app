package com.example.bluff;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import static com.example.bluff.MainActivity.cards_detail;

class Card {
    private static ArrayList<Integer> cards_deck;
    private int resId;
    int cardNum;
    String cardDeck;
    boolean selected=false;

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

    static void initialize(Context ctx) {
        cards_deck = new ArrayList<>();

        String[] deck = {"club", "heart", "spade", "diamond"};
        for (int i = 0; i < 4; i++) {
            for (int j = 1; j <= 13; j++) {
                String card_name = deck[i] + "_" + j;
                int drawableResourceId = ctx.getResources().getIdentifier(card_name, "drawable", ctx.getPackageName());
                Card card = new Card();
                card.setResId(drawableResourceId);
                card.cardNum = j;
                card.cardDeck = deck[i];
                cards_detail.add(card);
            }
        }
    }

}
