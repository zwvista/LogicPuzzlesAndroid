package com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.data.CarpentersWallDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.domain.CarpentersWallGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.domain.CarpentersWallGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.domain.CarpentersWallGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class CarpentersWallMainActivity extends GameMainActivity<CarpentersWallGame, CarpentersWallDocument, CarpentersWallGameMove, CarpentersWallGameState> {
    @Bean
    protected CarpentersWallDocument document;
    public CarpentersWallDocument doc() {return document;}

    @Click
    void btnOptions() {
        CarpentersWallOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        CarpentersWallGameActivity_.intent(this).start();
    }
}
