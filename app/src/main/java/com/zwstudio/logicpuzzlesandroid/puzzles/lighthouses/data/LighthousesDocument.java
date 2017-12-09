package com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses.domain.LighthousesGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses.domain.LighthousesGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses.domain.LighthousesObject;

import org.androidannotations.annotations.EBean;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class LighthousesDocument extends GameDocument<LighthousesGame, LighthousesGameMove> {
    protected void saveMove(LighthousesGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.strValue1 = move.obj.objAsString();
    }
    public LighthousesGameMove loadMove(MoveProgress rec) {
        return new LighthousesGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = LighthousesObject.objFromString(rec.strValue1);
        }};
    }
}
