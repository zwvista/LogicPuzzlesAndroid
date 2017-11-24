package com.zwstudio.logicpuzzlesandroid.puzzles.numberlink.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.numberlink.domain.NumberLinkGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.numberlink.domain.NumberLinkGameMove;

import org.androidannotations.annotations.EBean;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class NumberLinkDocument extends GameDocument<NumberLinkGame, NumberLinkGameMove> {
    protected void saveMove(NumberLinkGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.intValue1 = move.dir;
    }
    public NumberLinkGameMove loadMove(MoveProgress rec) {
        return new NumberLinkGameMove() {{
            p = new Position(rec.row, rec.col);
            dir = rec.intValue1;
        }};
    }
}
