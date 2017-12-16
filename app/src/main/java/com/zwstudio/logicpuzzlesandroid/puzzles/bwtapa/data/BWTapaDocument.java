package com.zwstudio.logicpuzzlesandroid.puzzles.bwtapa.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.bwtapa.domain.BWTapaGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.bwtapa.domain.BWTapaGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.bwtapa.domain.BWTapaObject;

import org.androidannotations.annotations.EBean;

@EBean
public class BWTapaDocument extends GameDocument<BWTapaGame, BWTapaGameMove> {
    protected void saveMove(BWTapaGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.strValue1 = move.obj.objTypeAsString();
    }
    public BWTapaGameMove loadMove(MoveProgress rec) {
        return new BWTapaGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = BWTapaObject.objTypeFromString(rec.strValue1);
        }};
    }
}
