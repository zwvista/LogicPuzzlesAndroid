package com.zwstudio.logicpuzzlesandroid.puzzles.powergrid.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.powergrid.domain.PowerGridGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.powergrid.domain.PowerGridGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.powergrid.domain.PowerGridObject;

import org.androidannotations.annotations.EBean;

@EBean
public class PowerGridDocument extends GameDocument<PowerGridGame, PowerGridGameMove> {
    protected void saveMove(PowerGridGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.strValue1 = move.obj.objAsString();
    }
    public PowerGridGameMove loadMove(MoveProgress rec) {
        return new PowerGridGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = PowerGridObject.objFromString(rec.strValue1);
        }};
    }
}
