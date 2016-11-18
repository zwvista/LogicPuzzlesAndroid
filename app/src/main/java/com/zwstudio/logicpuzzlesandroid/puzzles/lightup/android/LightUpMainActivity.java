package com.zwstudio.logicpuzzlesandroid.puzzles.lightup.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.MainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightup.data.LightUpDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightup.domain.LightUpGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightup.domain.LightUpGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightup.domain.LightUpGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_lightup_main)
public class LightUpMainActivity extends MainActivity<LightUpGame, LightUpDocument, LightUpGameMove, LightUpGameState> {
    public LightUpDocument doc() {return app.lightupDocument;}

    @AfterViews
    void init() {
        int[] levels = {1, 2, 3, 4, 5, 6, 7, 8, 16, 24, 34, 81};
        super.init(levels);
    }

    @Click
    void btnOptions() {
        LightUpOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        LightUpGameActivity_.intent(this).start();
    }
}
