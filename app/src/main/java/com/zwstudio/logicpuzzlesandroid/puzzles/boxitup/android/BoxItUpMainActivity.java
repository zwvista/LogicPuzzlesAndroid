package com.zwstudio.logicpuzzlesandroid.puzzles.boxitup.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitup.data.BoxItUpDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitup.domain.BoxItUpGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitup.domain.BoxItUpGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitup.domain.BoxItUpGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class BoxItUpMainActivity extends GameMainActivity<BoxItUpGame, BoxItUpDocument, BoxItUpGameMove, BoxItUpGameState> {
    @Bean
    protected BoxItUpDocument document;
    public BoxItUpDocument doc() {return document;}

    @Click
    void btnOptions() {
        BoxItUpOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        BoxItUpGameActivity_.intent(this).start();
    }
}
