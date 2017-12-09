package com.zwstudio.logicpuzzlesandroid.puzzles.battleships.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.battleships.domain.BattleShipsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.battleships.domain.BattleShipsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.battleships.domain.BattleShipsObject;

import org.androidannotations.annotations.EBean;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class BattleShipsDocument extends GameDocument<BattleShipsGame, BattleShipsGameMove> {
    protected void saveMove(BattleShipsGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.intValue1 = move.obj.ordinal();
    }
    public BattleShipsGameMove loadMove(MoveProgress rec) {
        return new BattleShipsGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = BattleShipsObject.values()[rec.intValue1];
        }};
    }
}
