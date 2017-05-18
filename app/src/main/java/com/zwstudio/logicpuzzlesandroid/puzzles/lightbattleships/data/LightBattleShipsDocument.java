package com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.GameProgress;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.domain.LightBattleShipsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.domain.LightBattleShipsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.domain.LightBattleShipsObject;

import org.androidannotations.annotations.EBean;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class LightBattleShipsDocument extends GameDocument<LightBattleShipsGame, LightBattleShipsGameMove> {
    protected void saveMove(LightBattleShipsGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.intValue1 = move.obj.ordinal();
    }
    public LightBattleShipsGameMove loadMove(MoveProgress rec) {
        return new LightBattleShipsGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = LightBattleShipsObject.values()[rec.intValue1];
        }};
    }
}
