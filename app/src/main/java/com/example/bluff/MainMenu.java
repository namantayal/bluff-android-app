package com.example.bluff;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainMenu extends AppCompatActivity {

    public void playOffline(View view) {
        LinearLayout dialogueBox=findViewById(R.id.dialogueBox);
        ImageView bgImage=findViewById(R.id.bgImage);
        LinearLayout buttonLayout=findViewById(R.id.buttonLayout);
        bgImage.setAlpha(0.3f);
        buttonLayout.setAlpha(0.3f);
        dialogueBox.setVisibility(View.VISIBLE);
    }
    public void finalPlay(View v){
        LinearLayout dialogueBox=findViewById(R.id.dialogueBox);
        ImageView bgImage=findViewById(R.id.bgImage);
        LinearLayout buttonLayout=findViewById(R.id.buttonLayout);
        bgImage.setAlpha(1f);
        buttonLayout.setAlpha(1f);
        dialogueBox.setVisibility(View.INVISIBLE);
        RadioGroup radioGroup=findViewById(R.id.noOfPlayers);
        int radioButtonId=radioGroup.getCheckedRadioButtonId();
        RadioButton selectedButton=findViewById(radioButtonId);
        int noOfPlayers=Integer.parseInt(selectedButton.getText().toString());
        Intent playActivity=new Intent(this, Play4.class);
        playActivity.putExtra("numOfPlayers",noOfPlayers);
        startActivity(playActivity);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_layout);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


}
