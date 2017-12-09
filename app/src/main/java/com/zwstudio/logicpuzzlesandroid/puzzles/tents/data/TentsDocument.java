package com.zwstudio.logicpuzzlesandroid.puzzles.tents.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.tents.domain.TentsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.tents.domain.TentsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.tents.domain.TentsObject;

import org.androidannotations.annotations.EBean;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class TentsDocument extends GameDocument<TentsGame, TentsGameMove> {
    protected void saveMove(TentsGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.strValue1 = move.obj.objAsString();
    }
    public TentsGameMove loadMove(MoveProgress rec) {
        return new TentsGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = TentsObject.objFromString(rec.strValue1);
        }};
    }
}
