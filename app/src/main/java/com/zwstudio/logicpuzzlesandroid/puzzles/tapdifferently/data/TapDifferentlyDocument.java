package com.zwstudio.logicpuzzlesandroid.puzzles.tapdifferently.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapdifferently.domain.TapDifferentlyGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapdifferently.domain.TapDifferentlyGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapdifferently.domain.TapDifferentlyObject;

import org.androidannotations.annotations.EBean;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class TapDifferentlyDocument extends GameDocument<TapDifferentlyGame, TapDifferentlyGameMove> {
    protected void saveMove(TapDifferentlyGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.strValue1 = move.obj.objTypeAsString();
    }
    public TapDifferentlyGameMove loadMove(MoveProgress rec) {
        return new TapDifferentlyGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = TapDifferentlyObject.objTypeFromString(rec.strValue1);
        }};
    }
}
