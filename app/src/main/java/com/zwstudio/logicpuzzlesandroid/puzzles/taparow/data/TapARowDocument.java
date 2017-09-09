package com.zwstudio.logicpuzzlesandroid.puzzles.taparow.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.taparow.domain.TapARowGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.taparow.domain.TapARowGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.taparow.domain.TapARowObject;

import org.androidannotations.annotations.EBean;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class TapARowDocument extends GameDocument<TapARowGame, TapARowGameMove> {
    protected void saveMove(TapARowGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.strValue1 = move.obj.objTypeAsString();
    }
    public TapARowGameMove loadMove(MoveProgress rec) {
        return new TapARowGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = TapARowObject.objTypeFromString(rec.strValue1);
        }};
    }
}
