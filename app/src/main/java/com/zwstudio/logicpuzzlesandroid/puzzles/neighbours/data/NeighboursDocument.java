package com.zwstudio.logicpuzzlesandroid.puzzles.neighbours.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.neighbours.domain.NeighboursGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.neighbours.domain.NeighboursGameMove;

import org.androidannotations.annotations.EBean;

@EBean
public class NeighboursDocument extends GameDocument<NeighboursGame, NeighboursGameMove> {
    protected void saveMove(NeighboursGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.intValue1 = move.dir;
        rec.intValue2 = move.obj.ordinal();
    }
    public NeighboursGameMove loadMove(MoveProgress rec) {
        return new NeighboursGameMove() {{
            p = new Position(rec.row, rec.col);
            dir = rec.intValue1;
            obj = GridLineObject.values()[rec.intValue2];
        }};
    }
}
