package com.zwstudio.logicpuzzlesandroid.puzzles.busyseas.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.busyseas.data.BusySeasDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.busyseas.domain.BusySeasGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.busyseas.domain.BusySeasGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.busyseas.domain.BusySeasGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class BusySeasMainActivity extends GameMainActivity<BusySeasGame, BusySeasDocument, BusySeasGameMove, BusySeasGameState> {
    @Bean
    protected BusySeasDocument document;
    public BusySeasDocument doc() {return document;}

    @Click
    void btnOptions() {
        BusySeasOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        BusySeasGameActivity_.intent(this).start();
    }
}
