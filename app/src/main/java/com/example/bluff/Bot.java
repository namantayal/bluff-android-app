package com.example.bluff;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

class Bot {
    int id;
    boolean pass;
    static int noOfBots;
    String name;
    ArrayList<Integer> botCards=new ArrayList<>();
    private int[] cardsQty=new int[13];
    private CenterTable centerTable;

    Bot(String name,int id,CenterTable centerTable){
        this.name=name;
        this.centerTable=centerTable;
        for(int i=0;i<13;i++){
            cardsQty[i]=0;
        }
        this.id=id;
        pass=false;
    }

    private int maxCard(){
        int max=0;
        int index=0;
        for(int i=0;i<13;i++){
            if(cardsQty[i]==4){
                index=i;
                break;
            }
            else if(cardsQty[i]>max) {
                max = cardsQty[i];
                index=i;
            }
        }
        return index;
    }

    private int minCard(){
        int min=5;
        int index=0;
        for(int i=0;i<13;i++) {
            if (cardsQty[i] != 0) {
                if (cardsQty[i] == 1) {
                    index = i;
                    break;
                } else if (cardsQty[i] < min) {
                    min = cardsQty[i];
                    index = i;
                }
            }
        }
        return index;
    }

    private void arrange(){
        for(int i=0;i<13;i++){
            cardsQty[i]=0;
        }
        for(int i=0;i<botCards.size();i++){
            int card=botCards.get(i)%13;
            cardsQty[card]++;
        }
    }

    private int predictCardNumQty(int cardNum){
        int cardNumQty=cardsQty[cardNum];
        float[] probCardQty=new float[cardNumQty];
        float initialProb=0.1f;
        float totalProb=0;
        for(int i=cardNumQty-1;i>0;i--){
            probCardQty[i]=initialProb;
            totalProb+=initialProb;
            initialProb+=initialProb;
        }
        probCardQty[0]=1-totalProb;

        float[] cumProbCardQty=new float[cardNumQty];
        cumProbCardQty[0]=probCardQty[0];
        for(int i=1;i<cardNumQty;i++){
            cumProbCardQty[i]=cumProbCardQty[i-1]+probCardQty[i];
        }

        Random random=new Random();
        float probability=random.nextFloat();
        int quantity=0;
        for(int i=0;i<cardNumQty;i++){
            if(probability<cumProbCardQty[i]){
                quantity=i;
                break;
            }
        }
        return quantity+1;
    }

    private void addCardsToTable(ArrayList<Integer> cards){
        centerTable.lastPlayedCards.clear();
        centerTable.displayCards(cards);
    }

    private void truth(int cardNum){
        centerTable.lastPlayerId=id;
        ArrayList<Integer> cards=new ArrayList<>();
        int qtyToPlay=predictCardNumQty(cardNum);
        for (int i=0;i<qtyToPlay;i++){
            for(int j=0;j<botCards.size();j++){
                if((botCards.get(j)%13)==cardNum){
                    cards.add(botCards.get(j));
                    botCards.remove(botCards.get(j));
                }
            }
        }
        addCardsToTable(cards);
    }

    private void bluff(){
        centerTable.lastPlayerId=id;
        ArrayList<Integer> cards=new ArrayList<>();
        int qtyToPlay;
        Random random=new Random();
        float prob =random.nextFloat();
        if(prob<0.65)
            qtyToPlay=1;
        else
            qtyToPlay=2;
        for(int i=0;i<qtyToPlay;i++){
            int minCardNum=minCard();
            for(int j=0;j<botCards.size();j++){
                if(botCards.get(j)%13==minCardNum){
                    cards.add(botCards.get(j));
                    botCards.remove(botCards.get(j));
                }
            }
        }
        addCardsToTable(cards);
    }

    void play(){
        arrange();
        Random random=new Random();
        float probability=random.nextFloat();
        if(centerTable.newChance){
            centerTable.newChance=false;
            int maxCardNum=maxCard();
            centerTable.setClaimNumber(maxCardNum);
            if(probability>0.5)
                truth(maxCardNum);
            else
                bluff();
        }
        else{
            if(cardsQty[centerTable.claimNumber]>(4-centerTable.lastPlayedCards.size())){
                centerTable.check();
            }
            else if(cardsQty[centerTable.claimNumber]==0){
                float [] cumProbability={0.3f,0.8f};    //B,P,C
                if(probability<cumProbability[0] && !pass){
                    bluff();
                }
                else if(probability<cumProbability[1]){
                    if(!pass) {
                        pass=true;
                        centerTable.passCount++;
                    }
                    centerTable.checkPassAll();
                }
                else{
                    centerTable.check();
                }
            }
            else{
                float [] cumProbability={0.5f,0.9f};
                if(probability<cumProbability[0]){
                    truth(centerTable.claimNumber);
                }
                else if(probability<cumProbability[1]){
                    bluff();
                }
                else{
                    centerTable.check();
                }
            }
        }
    }
}