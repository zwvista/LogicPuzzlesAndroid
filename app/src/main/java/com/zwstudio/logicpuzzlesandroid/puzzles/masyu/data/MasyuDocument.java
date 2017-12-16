package com.zwstudio.logicpuzzlesandroid.puzzles.masyu.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.domain.MasyuGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.domain.MasyuGameMove;

import org.androidannotations.annotations.EBean;

@EBean
public class MasyuDocument extends GameDocument<MasyuGame, MasyuGameMove> {
    protected void saveMove(MasyuGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.intValue1 = move.dir;
    }
    public MasyuGameMove loadMove(MoveProgress rec) {
        return new MasyuGameMove() {{
            p = new Position(rec.row, rec.col);
            dir = rec.intValue1;
        }};
    }
}
