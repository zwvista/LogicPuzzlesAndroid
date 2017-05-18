package com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.GameProgress;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.domain.MinesweeperGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.domain.MinesweeperGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.domain.MinesweeperObject;

import org.androidannotations.annotations.EBean;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class MinesweeperDocument extends GameDocument<MinesweeperGame, MinesweeperGameMove> {
    protected void saveMove(MinesweeperGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.intValue1 = move.obj.ordinal();
    }
    public MinesweeperGameMove loadMove(MoveProgress rec) {
        return new MinesweeperGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = MinesweeperObject.values()[rec.intValue1];
        }};
    }
}
