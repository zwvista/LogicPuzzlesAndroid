package com.zwstudio.logicpuzzlesandroid.puzzles.carpenterssquare.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterssquare.data.CarpentersSquareDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterssquare.domain.CarpentersSquareGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterssquare.domain.CarpentersSquareGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterssquare.domain.CarpentersSquareGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class CarpentersSquareMainActivity extends GameMainActivity<CarpentersSquareGame, CarpentersSquareDocument, CarpentersSquareGameMove, CarpentersSquareGameState> {
    @Bean
    protected CarpentersSquareDocument document;
    public CarpentersSquareDocument doc() {return document;}

    @Click
    void btnOptions() {
        CarpentersSquareOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        CarpentersSquareGameActivity_.intent(this).start();
    }
}
