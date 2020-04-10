package com.example.bluff;

import java.util.ArrayList;

public class CenterTable {
    ArrayList<Integer>cardsOnTable=new ArrayList<>();
    int cardNumByUser;
    int actualCardNum;
    int cardRotation=0;

    int getCardRotation(){
        cardRotation+=50;
        if(cardRotation>360)
            cardRotation=0;
        return cardRotation;
    }
}
