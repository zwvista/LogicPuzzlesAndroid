package com.zwstudio.logicpuzzlesandroid.puzzles.square100.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.GameProgress;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.square100.domain.Square100Game;
import com.zwstudio.logicpuzzlesandroid.puzzles.square100.domain.Square100GameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.square100.domain.Square100Object;

import org.androidannotations.annotations.EBean;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class Square100Document extends GameDocument<Square100Game, Square100GameMove> {
    protected void saveMove(Square100GameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.intValue1 = move.obj.ordinal();
    }
    public Square100GameMove loadMove(MoveProgress rec) {
        return new Square100GameMove() {{
            p = new Position(rec.row, rec.col);
            obj = Square100Object.values()[rec.intValue1];
        }};
    }
}
