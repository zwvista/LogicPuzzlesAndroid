package com.zwstudio.logicpuzzlesandroid.puzzles.kakurasu.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.kakurasu.domain.KakurasuGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.kakurasu.domain.KakurasuGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.kakurasu.domain.KakurasuObject;

import org.androidannotations.annotations.EBean;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class KakurasuDocument extends GameDocument<KakurasuGame, KakurasuGameMove> {
    protected void saveMove(KakurasuGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.intValue1 = move.obj.ordinal();
    }
    public KakurasuGameMove loadMove(MoveProgress rec) {
        return new KakurasuGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = KakurasuObject.values()[rec.intValue1];
        }};
    }
}
