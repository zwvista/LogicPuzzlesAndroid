package com.zwstudio.logicpuzzlesandroid.puzzles.abc.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.abc.data.AbcDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.abc.domain.AbcGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.abc.domain.AbcGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.abc.domain.AbcGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class AbcMainActivity extends GameMainActivity<AbcGame, AbcDocument, AbcGameMove, AbcGameState> {
    @Bean
    protected AbcDocument document;
    public AbcDocument doc() {return document;}

    @Click
    void btnOptions() {
        AbcOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        AbcGameActivity_.intent(this).start();
    }
}
