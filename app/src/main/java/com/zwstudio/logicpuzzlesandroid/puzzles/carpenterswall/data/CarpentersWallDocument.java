package com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.domain.CarpentersWallGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.domain.CarpentersWallGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.domain.CarpentersWallObject;

import org.androidannotations.annotations.EBean;

@EBean
public class CarpentersWallDocument extends GameDocument<CarpentersWallGame, CarpentersWallGameMove> {
    protected void saveMove(CarpentersWallGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.strValue1 = move.obj.objAsString();
    }
    public CarpentersWallGameMove loadMove(MoveProgress rec) {
        return new CarpentersWallGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = CarpentersWallObject.objTypeFromString(rec.strValue1);
        }};
    }
}
