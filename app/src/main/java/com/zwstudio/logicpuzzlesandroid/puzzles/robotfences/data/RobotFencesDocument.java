package com.zwstudio.logicpuzzlesandroid.puzzles.robotfences.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.robotfences.domain.RobotFencesGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.robotfences.domain.RobotFencesGameMove;

import org.androidannotations.annotations.EBean;

@EBean
public class RobotFencesDocument extends GameDocument<RobotFencesGame, RobotFencesGameMove> {
    protected void saveMove(RobotFencesGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.intValue1 = move.obj;
    }
    public RobotFencesGameMove loadMove(MoveProgress rec) {
        return new RobotFencesGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = rec.intValue1;
        }};
    }
}
