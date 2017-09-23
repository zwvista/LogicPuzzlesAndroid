package com.zwstudio.logicpuzzlesandroid.puzzles.snail.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.GameProgress;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.snail.domain.SnailGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.snail.domain.SnailGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.snail.domain.SnailObject;

import org.androidannotations.annotations.EBean;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class SnailDocument extends GameDocument<SnailGame, SnailGameMove> {
    protected void saveMove(SnailGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.intValue1 = move.obj.ordinal();
    }
    public SnailGameMove loadMove(MoveProgress rec) {
        return new SnailGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = SnailObject.values()[rec.intValue1];
        }};
    }
}
