package com.zwstudio.logicpuzzlesandroid.puzzles.slitherlink.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.slitherlink.domain.SlitherLinkGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.slitherlink.domain.SlitherLinkGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.slitherlink.domain.SlitherLinkObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.slitherlink.domain.SlitherLinkObjectOrientation;

import org.androidannotations.annotations.EBean;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class SlitherLinkDocument extends GameDocument<SlitherLinkGame, SlitherLinkGameMove> {
    protected void saveMove(SlitherLinkGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.intValue1 = move.objOrientation.ordinal();
        rec.intValue2 = move.obj.ordinal();
    }
    public SlitherLinkGameMove loadMove(MoveProgress rec) {
        return new SlitherLinkGameMove() {{
            p = new Position(rec.row, rec.col);
            objOrientation = SlitherLinkObjectOrientation.values()[rec.intValue1];
            obj = SlitherLinkObject.values()[rec.intValue2];
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
