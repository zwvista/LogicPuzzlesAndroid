package com.zwstudio.logicpuzzlesandroid.puzzles.square100.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.square100.domain.Square100Game;
import com.zwstudio.logicpuzzlesandroid.puzzles.square100.domain.Square100GameMove;

import org.androidannotations.annotations.EBean;

@EBean
public class Square100Document extends GameDocument<Square100Game, Square100GameMove> {
    protected void saveMove(Square100GameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.strValue1 = move.obj;
    }
    public Square100GameMove loadMove(MoveProgress rec) {
        return new Square100GameMove() {{
            p = new Position(rec.row, rec.col);
            obj = rec.strValue1;
        }};
    }
}
