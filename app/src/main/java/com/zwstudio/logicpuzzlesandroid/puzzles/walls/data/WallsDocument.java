package com.zwstudio.logicpuzzlesandroid.puzzles.walls.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.walls.domain.WallsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.walls.domain.WallsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.walls.domain.WallsObject;

import org.androidannotations.annotations.EBean;

@EBean
public class WallsDocument extends GameDocument<WallsGame, WallsGameMove> {
    protected void saveMove(WallsGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.strValue1 = move.obj.objAsString();
    }
    public WallsGameMove loadMove(MoveProgress rec) {
        return new WallsGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = WallsObject.objFromString(rec.strValue1);
        }};
    }
}
