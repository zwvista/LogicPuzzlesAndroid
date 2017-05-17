package com.zwstudio.logicpuzzlesandroid.puzzles.battleships.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.battleships.data.BattleShipsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.battleships.domain.BattleShipsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.battleships.domain.BattleShipsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.battleships.domain.BattleShipsGameState;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class BattleShipsMainActivity extends GameMainActivity<BattleShipsGame, BattleShipsDocument, BattleShipsGameMove, BattleShipsGameState> {
    public BattleShipsDocument doc() {return app.battleshipsDocument;}

    @Click
    void btnOptions() {
        BattleShipsOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        BattleShipsGameActivity_.intent(this).start();
    }
}
