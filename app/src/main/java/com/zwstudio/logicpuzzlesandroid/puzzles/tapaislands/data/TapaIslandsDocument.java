package com.zwstudio.logicpuzzlesandroid.puzzles.tapaislands.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapaislands.domain.TapaIslandsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapaislands.domain.TapaIslandsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapaislands.domain.TapaIslandsObject;

import org.androidannotations.annotations.EBean;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class TapaIslandsDocument extends GameDocument<TapaIslandsGame, TapaIslandsGameMove> {
    protected void saveMove(TapaIslandsGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.strValue1 = move.obj.objTypeAsString();
    }
    public TapaIslandsGameMove loadMove(MoveProgress rec) {
        return new TapaIslandsGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = TapaIslandsObject.objTypeFromString(rec.strValue1);
        }};
    }
}
