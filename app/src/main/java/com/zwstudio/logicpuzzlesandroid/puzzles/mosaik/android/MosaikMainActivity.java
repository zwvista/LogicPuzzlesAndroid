package com.zwstudio.logicpuzzlesandroid.puzzles.mosaik.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.mosaik.data.MosaikDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.mosaik.domain.MosaikGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.mosaik.domain.MosaikGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.mosaik.domain.MosaikGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class MosaikMainActivity extends GameMainActivity<MosaikGame, MosaikDocument, MosaikGameMove, MosaikGameState> {
    public MosaikDocument doc() {return app.mosaikDocument;}

    @Click
    void btnOptions() {
        MosaikOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        MosaikGameActivity_.intent(this).start();
    }
}
