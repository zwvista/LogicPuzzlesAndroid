package com.zwstudio.logicpuzzlesandroid.puzzles.rippleeffect.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.GameProgress;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.rippleeffect.domain.RippleEffectGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.rippleeffect.domain.RippleEffectGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.rippleeffect.domain.RippleEffectObject;

import org.androidannotations.annotations.EBean;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class RippleEffectDocument extends GameDocument<RippleEffectGame, RippleEffectGameMove> {
    protected void saveMove(RippleEffectGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.intValue1 = move.obj.ordinal();
    }
    public RippleEffectGameMove loadMove(MoveProgress rec) {
        return new RippleEffectGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = RippleEffectObject.values()[rec.intValue1];
        }};
    }
}
