package com.zwstudio.logicpuzzlesandroid.puzzles.mathrax.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.mathrax.data.MathraxDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.mathrax.domain.MathraxGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.mathrax.domain.MathraxGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.mathrax.domain.MathraxGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class MathraxMainActivity extends GameMainActivity<MathraxGame, MathraxDocument, MathraxGameMove, MathraxGameState> {
    @Bean
    protected MathraxDocument document;
    public MathraxDocument doc() {return document;}

    @Click
    void btnOptions() {
        MathraxOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        MathraxGameActivity_.intent(this).start();
    }
}
