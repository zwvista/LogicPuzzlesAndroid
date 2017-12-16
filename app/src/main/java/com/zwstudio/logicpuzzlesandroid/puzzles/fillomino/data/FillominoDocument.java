package com.zwstudio.logicpuzzlesandroid.puzzles.fillomino.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.fillomino.domain.FillominoGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.fillomino.domain.FillominoGameMove;

import org.androidannotations.annotations.EBean;

@EBean
public class FillominoDocument extends GameDocument<FillominoGame, FillominoGameMove> {
    protected void saveMove(FillominoGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.strValue1 = String.valueOf(move.obj);
    }
    public FillominoGameMove loadMove(MoveProgress rec) {
        return new FillominoGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = rec.strValue1.charAt(0);
        }};
    }
}
