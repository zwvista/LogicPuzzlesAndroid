package com.zwstudio.logicpuzzlesandroid.puzzles.rooms.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.rooms.domain.RoomsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.rooms.domain.RoomsGameMove;

import org.androidannotations.annotations.EBean;

@EBean
public class RoomsDocument extends GameDocument<RoomsGame, RoomsGameMove> {
    protected void saveMove(RoomsGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.intValue1 = move.dir;
        rec.intValue2 = move.obj.ordinal();
    }
    public RoomsGameMove loadMove(MoveProgress rec) {
        return new RoomsGameMove() {{
            p = new Position(rec.row, rec.col);
            dir = rec.intValue1;
            obj = GridLineObject.values()[rec.intValue2];
        }};
    }
}
