package com.zwstudio.logicpuzzlesandroid.puzzles.magnets.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.GameProgress;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.magnets.domain.MagnetsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.magnets.domain.MagnetsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.magnets.domain.MagnetsObject;

import org.androidannotations.annotations.EBean;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class MagnetsDocument extends GameDocument<MagnetsGame, MagnetsGameMove> {
    protected void saveMove(MagnetsGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.intValue1 = move.obj.ordinal();
    }
    public MagnetsGameMove loadMove(MoveProgress rec) {
        return new MagnetsGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = MagnetsObject.values()[rec.intValue1];
        }};
    }
}
