package com.zwstudio.logicpuzzlesandroid.puzzles.abcpath.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.abcpath.domain.ABCPathGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.abcpath.domain.ABCPathGameMove;

import org.androidannotations.annotations.EBean;

@EBean
public class ABCPathDocument extends GameDocument<ABCPathGame, ABCPathGameMove> {
    protected void saveMove(ABCPathGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.strValue1 = String.valueOf(move.obj);
    }
    public ABCPathGameMove loadMove(MoveProgress rec) {
        return new ABCPathGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = rec.strValue1.charAt(0);
        }};
    }
}
