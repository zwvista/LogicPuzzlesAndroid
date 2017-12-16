package com.zwstudio.logicpuzzlesandroid.puzzles.busyseas.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.busyseas.domain.BusySeasGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.busyseas.domain.BusySeasGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.busyseas.domain.BusySeasObject;

import org.androidannotations.annotations.EBean;

@EBean
public class BusySeasDocument extends GameDocument<BusySeasGame, BusySeasGameMove> {
    protected void saveMove(BusySeasGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.strValue1 = move.obj.objAsString();
    }
    public BusySeasGameMove loadMove(MoveProgress rec) {
        return new BusySeasGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = BusySeasObject.objFromString(rec.strValue1);
        }};
    }
}
