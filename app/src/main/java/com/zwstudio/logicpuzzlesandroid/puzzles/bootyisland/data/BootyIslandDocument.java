package com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.domain.BootyIslandGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.domain.BootyIslandGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.domain.BootyIslandObject;

import org.androidannotations.annotations.EBean;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class BootyIslandDocument extends GameDocument<BootyIslandGame, BootyIslandGameMove> {
    protected void saveMove(BootyIslandGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.strValue1 = move.obj.objAsString();
    }
    public BootyIslandGameMove loadMove(MoveProgress rec) {
        return new BootyIslandGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = BootyIslandObject.objFromString(rec.strValue1);
        }};
    }
}
