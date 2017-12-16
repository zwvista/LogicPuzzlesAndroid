package com.zwstudio.logicpuzzlesandroid.puzzles.numberpath.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.numberpath.domain.NumberPathGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.numberpath.domain.NumberPathGameMove;

import org.androidannotations.annotations.EBean;

@EBean
public class NumberPathDocument extends GameDocument<NumberPathGame, NumberPathGameMove> {
    protected void saveMove(NumberPathGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.intValue1 = move.dir;
    }
    public NumberPathGameMove loadMove(MoveProgress rec) {
        return new NumberPathGameMove() {{
            p = new Position(rec.row, rec.col);
            dir = rec.intValue1;
        }};
    }
}
