package com.zwstudio.logicpuzzlesandroid.puzzles.magnets.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.magnets.data.MagnetsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.magnets.domain.MagnetsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.magnets.domain.MagnetsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.magnets.domain.MagnetsGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class MagnetsMainActivity extends GameMainActivity<MagnetsGame, MagnetsDocument, MagnetsGameMove, MagnetsGameState> {
    @Bean
    protected MagnetsDocument document;
    public MagnetsDocument doc() {return document;}

    @Click
    void btnOptions() {
        MagnetsOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        MagnetsGameActivity_.intent(this).start();
    }
}
