package com.zwstudio.logicpuzzlesandroid.puzzles.battleships.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.battleships.data.BattleShipsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.battleships.domain.BattleShipsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.battleships.domain.BattleShipsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.battleships.domain.BattleShipsGameState;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_help)
public class BattleShipsHelpActivity extends GameHelpActivity<BattleShipsGame, BattleShipsDocument, BattleShipsGameMove, BattleShipsGameState> {
    public BattleShipsDocument doc() {return app.battleshipsDocument;}
}
