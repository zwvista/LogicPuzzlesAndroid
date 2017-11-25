package com.zwstudio.logicpuzzlesandroid.puzzles.walls.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.walls.data.WallsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.walls.domain.WallsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.walls.domain.WallsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.walls.domain.WallsGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class WallsMainActivity extends GameMainActivity<WallsGame, WallsDocument, WallsGameMove, WallsGameState> {
    @Bean
    protected WallsDocument document;
    public WallsDocument doc() {return document;}

    @Click
    void btnOptions() {
        WallsOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        WallsGameActivity_.intent(this).start();
    }
}
