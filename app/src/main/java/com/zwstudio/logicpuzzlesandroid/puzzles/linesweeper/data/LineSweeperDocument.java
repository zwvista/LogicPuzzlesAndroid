package com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.domain.LineSweeperGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.domain.LineSweeperGameMove;

import org.androidannotations.annotations.EBean;

@EBean
public class LineSweeperDocument extends GameDocument<LineSweeperGame, LineSweeperGameMove> {
    protected void saveMove(LineSweeperGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.intValue1 = move.dir;
    }
    public LineSweeperGameMove loadMove(MoveProgress rec) {
        return new LineSweeperGameMove() {{
            p = new Position(rec.row, rec.col);
            dir = rec.intValue1;
        }};
    }
}
