package com.zwstudio.logicpuzzlesandroid.puzzles.magnets.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.puzzles.magnets.data.MagnetsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.magnets.domain.MagnetsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.magnets.domain.MagnetsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.magnets.domain.MagnetsGameState;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class MagnetsMainActivity extends GameMainActivity<MagnetsGame, MagnetsDocument, MagnetsGameMove, MagnetsGameState> {
    public MagnetsDocument doc() {return app.magnetsDocument;}

    @AfterViews
    void init() {
        int[] levels = {1, 2, 3, 4, 5, 6, 7, 8, 16, 24, 34, 81};
        super.init(levels);
    }

    @Click
    void btnOptions() {
        MagnetsOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        MagnetsGameActivity_.intent(this).start();
    }
}
