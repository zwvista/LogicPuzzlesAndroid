package com.zwstudio.logicpuzzlesandroid.puzzles.mosaik.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.mosaik.data.MosaikDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.mosaik.domain.MosaikGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.mosaik.domain.MosaikGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.mosaik.domain.MosaikGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_help)
public class MosaikHelpActivity extends GameHelpActivity<MosaikGame, MosaikDocument, MosaikGameMove, MosaikGameState> {
    public MosaikDocument doc() {return app.mosaikDocument;}

    @AfterViews
    protected void init() {
        super.init();
    }
}
