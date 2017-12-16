package com.zwstudio.logicpuzzlesandroid.puzzles.theoddbrick.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.theoddbrick.domain.TheOddBrickGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.theoddbrick.domain.TheOddBrickGameMove;

import org.androidannotations.annotations.EBean;

@EBean
public class TheOddBrickDocument extends GameDocument<TheOddBrickGame, TheOddBrickGameMove> {
    protected void saveMove(TheOddBrickGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.intValue1 = move.obj;
    }
    public TheOddBrickGameMove loadMove(MoveProgress rec) {
        return new TheOddBrickGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = rec.intValue1;
        }};
    }
}
