package com.zwstudio.logicpuzzlesandroid.puzzles.loopy.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.loopy.domain.LoopyGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.loopy.domain.LoopyGameMove;

import org.androidannotations.annotations.EBean;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class LoopyDocument extends GameDocument<LoopyGame, LoopyGameMove> {
    protected void saveMove(LoopyGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.intValue1 = move.dir;
    }
    public LoopyGameMove loadMove(MoveProgress rec) {
        return new LoopyGameMove() {{
            p = new Position(rec.row, rec.col);
            dir = rec.intValue1;
        }};
    }
}
