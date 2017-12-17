package com.zwstudio.logicpuzzlesandroid.puzzles.abcpath.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.abcpath.data.ABCPathDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.abcpath.domain.ABCPathGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.abcpath.domain.ABCPathGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.abcpath.domain.ABCPathGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class ABCPathMainActivity extends GameMainActivity<ABCPathGame, ABCPathDocument, ABCPathGameMove, ABCPathGameState> {
    @Bean
    protected ABCPathDocument document;
    public ABCPathDocument doc() {return document;}

    @Click
    void btnOptions() {
        ABCPathOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        ABCPathGameActivity_.intent(this).start();
    }
}
