package com.zwstudio.logicpuzzlesandroid.puzzles.tapa.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapa.domain.TapaGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapa.domain.TapaGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapa.domain.TapaObject;

import org.androidannotations.annotations.EBean;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class TapaDocument extends GameDocument<TapaGame, TapaGameMove> {
    protected void saveMove(TapaGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.strValue1 = move.obj.objTypeAsString();
    }
    public TapaGameMove loadMove(MoveProgress rec) {
        return new TapaGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = TapaObject.objTypeFromString(rec.strValue1);
        }};
    }
}
