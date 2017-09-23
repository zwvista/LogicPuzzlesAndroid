package com.zwstudio.logicpuzzlesandroid.puzzles.snail.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.snail.data.SnailDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.snail.domain.SnailGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.snail.domain.SnailGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.snail.domain.SnailGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class SnailMainActivity extends GameMainActivity<SnailGame, SnailDocument, SnailGameMove, SnailGameState> {
    @Bean
    protected SnailDocument document;
    public SnailDocument doc() {return document;}

    @Click
    void btnOptions() {
        SnailOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        SnailGameActivity_.intent(this).start();
    }
}
