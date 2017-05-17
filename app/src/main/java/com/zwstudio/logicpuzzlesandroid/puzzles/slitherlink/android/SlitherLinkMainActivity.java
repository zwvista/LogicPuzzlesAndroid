package com.zwstudio.logicpuzzlesandroid.puzzles.slitherlink.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.slitherlink.data.SlitherLinkDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.slitherlink.domain.SlitherLinkGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.slitherlink.domain.SlitherLinkGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.slitherlink.domain.SlitherLinkGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class SlitherLinkMainActivity extends GameMainActivity<SlitherLinkGame, SlitherLinkDocument, SlitherLinkGameMove, SlitherLinkGameState> {
    public SlitherLinkDocument doc() {return app.slitherlinkDocument;}

    @Click
    void btnOptions() {
        SlitherLinkOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        SlitherLinkGameActivity_.intent(this).start();
    }
}
