package com.zwstudio.logicpuzzlesandroid.puzzles.futoshiki.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.futoshiki.domain.FutoshikiGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.futoshiki.domain.FutoshikiGameMove;

import org.androidannotations.annotations.EBean;

@EBean
public class FutoshikiDocument extends GameDocument<FutoshikiGame, FutoshikiGameMove> {
    protected void saveMove(FutoshikiGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.strValue1 = String.valueOf(move.obj);
    }
    public FutoshikiGameMove loadMove(MoveProgress rec) {
        return new FutoshikiGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = rec.strValue1.charAt(0);
        }};
    }
}
