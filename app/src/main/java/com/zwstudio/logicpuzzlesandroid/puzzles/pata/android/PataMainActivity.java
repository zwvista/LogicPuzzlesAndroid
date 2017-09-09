package com.zwstudio.logicpuzzlesandroid.puzzles.pata.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.pata.data.PataDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.pata.domain.PataGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.pata.domain.PataGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.pata.domain.PataGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class PataMainActivity extends GameMainActivity<PataGame, PataDocument, PataGameMove, PataGameState> {
    @Bean
    protected PataDocument document;
    public PataDocument doc() {return document;}

    @Click
    void btnOptions() {
        PataOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        PataGameActivity_.intent(this).start();
    }
}
