package com.example.hangmanfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;


public class MainActivity extends AppCompatActivity {

    TextView txtWordstobeGuessed;
    String WordstobeGuessed;
    String wordDisplayedString;
    char[] wordDisplayedChararray;
    ArrayList<String> myListofWords;
    EditText editInput;
    TextView txtLetterstried;
    String lettersTried;
    final String MESSAGE_WITH_LETTERS_TRIED = "Letters Tried: ";
    TextView txtTriesleft;
    String triesLeft;
    final String WINNING_MESSAGE = "You Won";
    final String LOSING_MESSAGE = "You Lost";
    Animation rotateAnimation;
    Animation scaleAnimation;
    Animation scaleandRotateAnimation;

    void revealLetterInWord(char letter){
        int indexOfLetter = WordstobeGuessed.indexOf(letter);

        while (indexOfLetter>=0){

            wordDisplayedChararray[indexOfLetter] = WordstobeGuessed.charAt(indexOfLetter);

            indexOfLetter = WordstobeGuessed.indexOf(letter, indexOfLetter+1);
        }

        wordDisplayedString = String.valueOf(wordDisplayedChararray);

    }

    void displayWordOnScreen(){

        String formattedString = "";
        for(char character : wordDisplayedChararray){
            formattedString += character + " ";
        }
        txtWordstobeGuessed.setText(formattedString);
    }

    void initializeGame()
    {
        Collections.shuffle(myListofWords);
        WordstobeGuessed = myListofWords.get(0);
        myListofWords.remove(0);

        wordDisplayedChararray = WordstobeGuessed.toCharArray();

        for(int i=1; i<wordDisplayedChararray.length-1; i++) {
            wordDisplayedChararray[i] = '_';

        }


        revealLetterInWord(wordDisplayedChararray[0]);

        revealLetterInWord(wordDisplayedChararray[wordDisplayedChararray.length-1]);

        wordDisplayedString = String.valueOf(wordDisplayedChararray);

        displayWordOnScreen();

        editInput.setText("");

        lettersTried = " ";

        txtLetterstried.setText(MESSAGE_WITH_LETTERS_TRIED);

        triesLeft = "X X X X X";
        txtTriesleft.setText(triesLeft);




    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myListofWords = new ArrayList<String>();
        txtWordstobeGuessed = findViewById(R.id.txtWordtobeGuessed);
        editInput = findViewById(R.id.editInput);
        txtLetterstried = findViewById(R.id.txtLettersTried);
        txtTriesleft = findViewById(R.id.txtTriesleft);

        InputStream myInputStream = null;
        Scanner in = null;
        String aWord = "";

        try {
            myInputStream = getAssets().open("database_file.txt");
            in = new Scanner(myInputStream);
            while (in.hasNext()) {
                aWord = in.next();
                myListofWords.add(aWord);
                Toast.makeText(MainActivity.this, aWord, Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, e.getClass().getSimpleName() + ":" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally {
            if(in != null) {
                in.close();
            }
            try {
                if(myInputStream != null) {
                    myInputStream.close();
                }
            } catch (IOException e) {
                Toast.makeText(MainActivity.this, e.getClass().getSimpleName() + ":" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        initializeGame();

        editInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length() != 0){

                    checkIfLetterisInWord(s.charAt(0));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




    }

    void checkIfLetterisInWord(char letter){

        if(WordstobeGuessed.indexOf(letter) >= 0){

            if(wordDisplayedString.indexOf(letter) < 0){

                revealLetterInWord(letter);

                displayWordOnScreen();

                if(!wordDisplayedString.contains("_")){
                    txtTriesleft.setText(WINNING_MESSAGE);
                }
            }

        }
        else{
            descreaseAndDisplayTriesLeft();

            if(triesLeft.isEmpty()){
                txtTriesleft.setText(LOSING_MESSAGE);
                txtWordstobeGuessed.setText(WordstobeGuessed);
            }
        }

        if(lettersTried.indexOf(letter) < 0){

            lettersTried += letter + " ";
            String messagetobeDisplayed = MESSAGE_WITH_LETTERS_TRIED + lettersTried;
            txtLetterstried.setText(messagetobeDisplayed);
        }
    }

    void descreaseAndDisplayTriesLeft(){

        if(!triesLeft.isEmpty()){
            triesLeft = triesLeft.substring(0, triesLeft.length()-2);
            txtTriesleft.setText(triesLeft);
        }
    }


    public void resetGame(View v) {
        initializeGame();
    }
}
