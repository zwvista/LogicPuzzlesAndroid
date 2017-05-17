package com.zwstudio.logicpuzzlesandroid.puzzles.mosaik.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.mosaik.data.MosaikDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.mosaik.domain.MosaikGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.mosaik.domain.MosaikGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.mosaik.domain.MosaikGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_options)
public class MosaikOptionsActivity extends GameOptionsActivity<MosaikGame, MosaikDocument, MosaikGameMove, MosaikGameState> {
    @Bean
    protected MosaikDocument document;
    public MosaikDocument doc() {return document;}
}
