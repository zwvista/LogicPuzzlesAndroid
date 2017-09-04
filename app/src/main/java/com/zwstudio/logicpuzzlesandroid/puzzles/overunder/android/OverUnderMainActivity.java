package com.zwstudio.logicpuzzlesandroid.puzzles.overunder.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.overunder.data.OverUnderDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.overunder.domain.OverUnderGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.overunder.domain.OverUnderGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.overunder.domain.OverUnderGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class OverUnderMainActivity extends GameMainActivity<OverUnderGame, OverUnderDocument, OverUnderGameMove, OverUnderGameState> {
    @Bean
    protected OverUnderDocument document;
    public OverUnderDocument doc() {return document;}

    @Click
    void btnOptions() {
        OverUnderOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        OverUnderGameActivity_.intent(this).start();
    }
}
