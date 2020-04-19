package com.example.bluff;

import android.content.Context;
import java.util.ArrayList;
import static com.example.bluff.Play4.cardsDetail;


class Card {
    static ArrayList<Integer> cardsDeck;
    int resId;
    int viewId;
    boolean selected=false;


    static void initialize(Context ctx) {
        cardsDeck = new ArrayList<>();

        String[] deck = {"club", "heart", "spade", "diamond"};
        for (int i = 0; i < 4; i++) {
            for (int j = 1; j <= 13; j++) {
                String card_name = deck[i] + "_" + j;
                int drawableResourceId = ctx.getResources().getIdentifier(card_name, "drawable", ctx.getPackageName());
                Card card = new Card();
                card.resId=drawableResourceId;
                cardsDetail.add(card);
            }
        }
    }

}
