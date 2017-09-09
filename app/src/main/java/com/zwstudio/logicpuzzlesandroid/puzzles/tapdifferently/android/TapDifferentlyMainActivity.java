package com.zwstudio.logicpuzzlesandroid.puzzles.tapdifferently.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapdifferently.data.TapDifferentlyDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapdifferently.domain.TapDifferentlyGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapdifferently.domain.TapDifferentlyGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapdifferently.domain.TapDifferentlyGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class TapDifferentlyMainActivity extends GameMainActivity<TapDifferentlyGame, TapDifferentlyDocument, TapDifferentlyGameMove, TapDifferentlyGameState> {
    @Bean
    protected TapDifferentlyDocument document;
    public TapDifferentlyDocument doc() {return document;}

    @Click
    void btnOptions() {
        TapDifferentlyOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        TapDifferentlyGameActivity_.intent(this).start();
    }
}
