package com.zwstudio.logicpuzzlesandroid.puzzles.neighbours.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.GameProgress;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.neighbours.domain.NeighboursGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.neighbours.domain.NeighboursGameMove;

import org.androidannotations.annotations.EBean;

/**
 * Created by zwvista on 2016/09/29.
 */

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
    public int getMarkerOption() {
        String o = gameProgress().option1;
        return o == null ? 0 : Integer.parseInt(o);
    }
    public void setMarkerOption(GameProgress rec, int o) {
        rec.option1 = String.valueOf(o);
    }
}
