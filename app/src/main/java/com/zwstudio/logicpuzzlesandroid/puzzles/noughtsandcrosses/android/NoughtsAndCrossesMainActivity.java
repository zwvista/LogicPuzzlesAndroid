package com.zwstudio.logicpuzzlesandroid.puzzles.noughtsandcrosses.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.noughtsandcrosses.data.NoughtsAndCrossesDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.noughtsandcrosses.domain.NoughtsAndCrossesGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.noughtsandcrosses.domain.NoughtsAndCrossesGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.noughtsandcrosses.domain.NoughtsAndCrossesGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class NoughtsAndCrossesMainActivity extends GameMainActivity<NoughtsAndCrossesGame, NoughtsAndCrossesDocument, NoughtsAndCrossesGameMove, NoughtsAndCrossesGameState> {
    @Bean
    protected NoughtsAndCrossesDocument document;
    public NoughtsAndCrossesDocument doc() {return document;}

    @Click
    void btnOptions() {
        NoughtsAndCrossesOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        NoughtsAndCrossesGameActivity_.intent(this).start();
    }
}
