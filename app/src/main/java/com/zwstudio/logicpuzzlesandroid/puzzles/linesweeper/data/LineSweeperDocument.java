package com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.domain.LineSweeperGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.domain.LineSweeperGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.domain.LineSweeperObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.domain.LineSweeperObjectOrientation;

import org.androidannotations.annotations.EBean;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class LineSweeperDocument extends GameDocument<LineSweeperGame, LineSweeperGameMove> {
    protected void saveMove(LineSweeperGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.intValue1 = move.objOrientation.ordinal();
        rec.intValue2 = move.obj.ordinal();
    }
    public LineSweeperGameMove loadMove(MoveProgress rec) {
        return new LineSweeperGameMove() {{
            p = new Position(rec.row, rec.col);
            objOrientation = LineSweeperObjectOrientation.values()[rec.intValue1];
            obj = LineSweeperObject.values()[rec.intValue2];
        }};
    }
    public int getMarkerOption() {
        String o = gameProgress().option1;
        return o == null ? 0 : Integer.parseInt(o);
    }
    public void setMarkerOption(int o) {
        gameProgress().option1 = String.valueOf(o);
    }
}
