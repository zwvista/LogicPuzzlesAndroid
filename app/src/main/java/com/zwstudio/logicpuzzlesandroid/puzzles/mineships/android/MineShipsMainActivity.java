package com.zwstudio.logicpuzzlesandroid.puzzles.mineships.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.mineships.data.MineShipsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.mineships.domain.MineShipsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.mineships.domain.MineShipsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.mineships.domain.MineShipsGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class MineShipsMainActivity extends GameMainActivity<MineShipsGame, MineShipsDocument, MineShipsGameMove, MineShipsGameState> {
    @Bean
    protected MineShipsDocument document;
    public MineShipsDocument doc() {return document;}

    @Click
    void btnOptions() {
        MineShipsOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        MineShipsGameActivity_.intent(this).start();
    }
}
