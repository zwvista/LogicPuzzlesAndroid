package com.zwstudio.logicpuzzlesandroid.puzzles.mosaik.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.MainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.mosaik.data.MosaikDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.mosaik.domain.MosaikGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.mosaik.domain.MosaikGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.mosaik.domain.MosaikGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_mosaik_main)
public class MosaikMainActivity extends MainActivity<MosaikGame, MosaikDocument, MosaikGameMove, MosaikGameState> {
    public MosaikDocument doc() {return app.mosaikDocument;}

    @AfterViews
    void init() {
        int[] levels = {1, 2, 3, 4, 5, 6, 7, 8, 16, 24, 34, 81};
        super.init(levels);
    }

    @Click
    void btnOptions() {
        MosaikOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        MosaikGameActivity_.intent(this).start();
    }
}
