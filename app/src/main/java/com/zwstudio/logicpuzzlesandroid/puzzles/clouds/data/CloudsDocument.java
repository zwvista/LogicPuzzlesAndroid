package com.zwstudio.logicpuzzlesandroid.puzzles.clouds.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.clouds.domain.CloudsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.clouds.domain.CloudsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.clouds.domain.CloudsObject;

import org.androidannotations.annotations.EBean;

@EBean
public class CloudsDocument extends GameDocument<CloudsGame, CloudsGameMove> {
    protected void saveMove(CloudsGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.intValue1 = move.obj.ordinal();
    }
    public CloudsGameMove loadMove(MoveProgress rec) {
        return new CloudsGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = CloudsObject.values()[rec.intValue1];
        }};
    }
}
