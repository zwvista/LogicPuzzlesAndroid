package com.zwstudio.logicpuzzlesandroid.puzzles.robotcrosswords.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.GameProgress;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.robotcrosswords.domain.RobotCrosswordsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.robotcrosswords.domain.RobotCrosswordsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.robotcrosswords.domain.RobotCrosswordsObject;

import org.androidannotations.annotations.EBean;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class RobotCrosswordsDocument extends GameDocument<RobotCrosswordsGame, RobotCrosswordsGameMove> {
    protected void saveMove(RobotCrosswordsGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.intValue1 = move.obj.ordinal();
    }
    public RobotCrosswordsGameMove loadMove(MoveProgress rec) {
        return new RobotCrosswordsGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = RobotCrosswordsObject.values()[rec.intValue1];
        }};
    }
}
