package com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels.data.WallSentinelsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels.domain.WallSentinelsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels.domain.WallSentinelsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels.domain.WallSentinelsGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class WallSentinelsMainActivity extends GameMainActivity<WallSentinelsGame, WallSentinelsDocument, WallSentinelsGameMove, WallSentinelsGameState> {
    @Bean
    protected WallSentinelsDocument document;
    public WallSentinelsDocument doc() {return document;}

    @Click
    void btnOptions() {
        WallSentinelsOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        WallSentinelsGameActivity_.intent(this).start();
    }
}
