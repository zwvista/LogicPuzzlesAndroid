package com.zwstudio.logicpuzzlesandroid.puzzles.numberlink.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.numberlink.data.NumberLinkDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.numberlink.domain.NumberLinkGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.numberlink.domain.NumberLinkGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.numberlink.domain.NumberLinkGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class NumberLinkMainActivity extends GameMainActivity<NumberLinkGame, NumberLinkDocument, NumberLinkGameMove, NumberLinkGameState> {
    @Bean
    protected NumberLinkDocument document;
    public NumberLinkDocument doc() {return document;}

    @Click
    void btnOptions() {
        NumberLinkOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        NumberLinkGameActivity_.intent(this).start();
    }
}
