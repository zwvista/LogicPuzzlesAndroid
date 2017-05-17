package com.zwstudio.logicpuzzlesandroid.puzzles.productsentinels.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.productsentinels.data.ProductSentinelsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.productsentinels.domain.ProductSentinelsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.productsentinels.domain.ProductSentinelsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.productsentinels.domain.ProductSentinelsGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class ProductSentinelsMainActivity extends GameMainActivity<ProductSentinelsGame, ProductSentinelsDocument, ProductSentinelsGameMove, ProductSentinelsGameState> {
    public ProductSentinelsDocument doc() {return app.productsentinelsDocument;}

    @Click
    void btnOptions() {
        ProductSentinelsOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        ProductSentinelsGameActivity_.intent(this).start();
    }
}
