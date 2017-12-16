package com.zwstudio.logicpuzzlesandroid.puzzles.productsentinels.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.productsentinels.domain.ProductSentinelsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.productsentinels.domain.ProductSentinelsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.productsentinels.domain.ProductSentinelsObject;

import org.androidannotations.annotations.EBean;

@EBean
public class ProductSentinelsDocument extends GameDocument<ProductSentinelsGame, ProductSentinelsGameMove> {
    protected void saveMove(ProductSentinelsGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.strValue1 = move.obj.objAsString();
    }
    public ProductSentinelsGameMove loadMove(MoveProgress rec) {
        return new ProductSentinelsGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = ProductSentinelsObject.objFromString(rec.strValue1);
        }};
    }
}
