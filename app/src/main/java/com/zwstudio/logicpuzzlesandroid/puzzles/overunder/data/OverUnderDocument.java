package com.zwstudio.logicpuzzlesandroid.puzzles.overunder.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.overunder.domain.OverUnderGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.overunder.domain.OverUnderGameMove;

import org.androidannotations.annotations.EBean;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class OverUnderDocument extends GameDocument<OverUnderGame, OverUnderGameMove> {
    protected void saveMove(OverUnderGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.intValue1 = move.dir;
        rec.intValue2 = move.obj.ordinal();
    }
    public OverUnderGameMove loadMove(MoveProgress rec) {
        return new OverUnderGameMove() {{
            p = new Position(rec.row, rec.col);
            dir = rec.intValue1;
            obj = GridLineObject.values()[rec.intValue2];
        }};
    }
}
