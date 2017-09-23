package com.zwstudio.logicpuzzlesandroid.puzzles.numberpath.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.numberpath.data.NumberPathDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.numberpath.domain.NumberPathGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.numberpath.domain.NumberPathGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.numberpath.domain.NumberPathGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class NumberPathMainActivity extends GameMainActivity<NumberPathGame, NumberPathDocument, NumberPathGameMove, NumberPathGameState> {
    @Bean
    protected NumberPathDocument document;
    public NumberPathDocument doc() {return document;}

    @Click
    void btnOptions() {
        NumberPathOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        NumberPathGameActivity_.intent(this).start();
    }
}
