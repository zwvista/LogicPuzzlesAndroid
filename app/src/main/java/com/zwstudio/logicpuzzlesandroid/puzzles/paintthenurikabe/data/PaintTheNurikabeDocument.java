package com.zwstudio.logicpuzzlesandroid.puzzles.paintthenurikabe.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.paintthenurikabe.domain.PaintTheNurikabeGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.paintthenurikabe.domain.PaintTheNurikabeGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.paintthenurikabe.domain.PaintTheNurikabeObject;

import org.androidannotations.annotations.EBean;

@EBean
public class PaintTheNurikabeDocument extends GameDocument<PaintTheNurikabeGame, PaintTheNurikabeGameMove> {
    protected void saveMove(PaintTheNurikabeGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.intValue1 = move.obj.ordinal();
    }
    public PaintTheNurikabeGameMove loadMove(MoveProgress rec) {
        return new PaintTheNurikabeGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = PaintTheNurikabeObject.values()[rec.intValue1];
        }};
    }
}
