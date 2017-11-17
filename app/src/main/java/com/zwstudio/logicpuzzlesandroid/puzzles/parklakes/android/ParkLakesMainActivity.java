package com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.data.ParkLakesDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.domain.ParkLakesGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.domain.ParkLakesGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.domain.ParkLakesGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class ParkLakesMainActivity extends GameMainActivity<ParkLakesGame, ParkLakesDocument, ParkLakesGameMove, ParkLakesGameState> {
    @Bean
    protected ParkLakesDocument document;
    public ParkLakesDocument doc() {return document;}

    @Click
    void btnOptions() {
        ParkLakesOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        ParkLakesGameActivity_.intent(this).start();
    }
}
