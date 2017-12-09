package com.zwstudio.logicpuzzlesandroid.puzzles.digitalbattleships.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.digitalbattleships.domain.DigitalBattleShipsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.digitalbattleships.domain.DigitalBattleShipsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.digitalbattleships.domain.DigitalBattleShipsObject;

import org.androidannotations.annotations.EBean;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class DigitalBattleShipsDocument extends GameDocument<DigitalBattleShipsGame, DigitalBattleShipsGameMove> {
    protected void saveMove(DigitalBattleShipsGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.intValue1 = move.obj.ordinal();
    }
    public DigitalBattleShipsGameMove loadMove(MoveProgress rec) {
        return new DigitalBattleShipsGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = DigitalBattleShipsObject.values()[rec.intValue1];
        }};
    }
}
