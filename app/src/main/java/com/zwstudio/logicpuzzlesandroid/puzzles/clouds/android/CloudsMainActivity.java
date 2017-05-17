package com.zwstudio.logicpuzzlesandroid.puzzles.clouds.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.puzzles.clouds.data.CloudsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.clouds.domain.CloudsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.clouds.domain.CloudsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.clouds.domain.CloudsGameState;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class CloudsMainActivity extends GameMainActivity<CloudsGame, CloudsDocument, CloudsGameMove, CloudsGameState> {
    public CloudsDocument doc() {return app.cloudsDocument;}

    @Click
    void btnOptions() {
        CloudsOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        CloudsGameActivity_.intent(this).start();
    }
}
