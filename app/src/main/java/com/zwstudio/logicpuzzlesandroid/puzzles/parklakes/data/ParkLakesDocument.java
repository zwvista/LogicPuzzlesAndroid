package com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.domain.ParkLakesGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.domain.ParkLakesGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.domain.ParkLakesObject;

import org.androidannotations.annotations.EBean;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class ParkLakesDocument extends GameDocument<ParkLakesGame, ParkLakesGameMove> {
    protected void saveMove(ParkLakesGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.strValue1 = move.obj.objAsString();
    }
    public ParkLakesGameMove loadMove(MoveProgress rec) {
        return new ParkLakesGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = ParkLakesObject.objFromString(rec.strValue1);
        }};
    }
}
