package com.zwstudio.logicpuzzlesandroid.puzzles.fencesentinels.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.fencesentinels.domain.FenceSentinelsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.fencesentinels.domain.FenceSentinelsGameMove;

import org.androidannotations.annotations.EBean;

@EBean
public class FenceSentinelsDocument extends GameDocument<FenceSentinelsGame, FenceSentinelsGameMove> {
    protected void saveMove(FenceSentinelsGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.intValue1 = move.dir;
        rec.intValue2 = move.obj.ordinal();
    }
    public FenceSentinelsGameMove loadMove(MoveProgress rec) {
        return new FenceSentinelsGameMove() {{
            p = new Position(rec.row, rec.col);
            dir = rec.intValue1;
            obj = GridLineObject.values()[rec.intValue2];
        }};
    }
}
