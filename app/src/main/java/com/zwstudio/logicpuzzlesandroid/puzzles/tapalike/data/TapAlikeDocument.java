package com.zwstudio.logicpuzzlesandroid.puzzles.tapalike.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapalike.domain.TapAlikeGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapalike.domain.TapAlikeGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapalike.domain.TapAlikeObject;

import org.androidannotations.annotations.EBean;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class TapAlikeDocument extends GameDocument<TapAlikeGame, TapAlikeGameMove> {
    protected void saveMove(TapAlikeGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.strValue1 = move.obj.objTypeAsString();
    }
    public TapAlikeGameMove loadMove(MoveProgress rec) {
        return new TapAlikeGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = TapAlikeObject.objTypeFromString(rec.strValue1);
        }};
    }
}
