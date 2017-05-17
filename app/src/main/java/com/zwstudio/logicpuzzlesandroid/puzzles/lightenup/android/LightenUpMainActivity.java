package com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.data.LightenUpDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain.LightenUpGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain.LightenUpGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain.LightenUpGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class LightenUpMainActivity extends GameMainActivity<LightenUpGame, LightenUpDocument, LightenUpGameMove, LightenUpGameState> {
    public LightenUpDocument doc() {return app.lightenupDocument;}

    @Click
    void btnOptions() {
        LightenUpOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        LightenUpGameActivity_.intent(this).start();
    }
}
