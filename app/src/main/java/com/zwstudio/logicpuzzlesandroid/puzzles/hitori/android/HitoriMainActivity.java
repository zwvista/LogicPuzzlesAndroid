package com.zwstudio.logicpuzzlesandroid.puzzles.hitori.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.hitori.data.HitoriDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.hitori.domain.HitoriGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.hitori.domain.HitoriGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.hitori.domain.HitoriGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class HitoriMainActivity extends GameMainActivity<HitoriGame, HitoriDocument, HitoriGameMove, HitoriGameState> {
    public HitoriDocument doc() {return app.hitoriDocument;}

    @AfterViews
    void init() {
        int[] levels = {1, 2, 3, 4, 5, 6, 7, 8, 16, 24, 34, 81};
        super.init(levels);
    }

    @Click
    void btnOptions() {
        HitoriOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        HitoriGameActivity_.intent(this).start();
    }
}
