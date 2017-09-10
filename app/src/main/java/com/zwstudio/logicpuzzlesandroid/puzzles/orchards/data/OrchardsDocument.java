package com.zwstudio.logicpuzzlesandroid.puzzles.orchards.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.GameProgress;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.orchards.domain.OrchardsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.orchards.domain.OrchardsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.orchards.domain.OrchardsObject;

import org.androidannotations.annotations.EBean;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class OrchardsDocument extends GameDocument<OrchardsGame, OrchardsGameMove> {
    protected void saveMove(OrchardsGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.strValue1 = move.obj.objAsString();
    }
    public OrchardsGameMove loadMove(MoveProgress rec) {
        return new OrchardsGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = OrchardsObject.objFromString(rec.strValue1);
        }};
    }
}
