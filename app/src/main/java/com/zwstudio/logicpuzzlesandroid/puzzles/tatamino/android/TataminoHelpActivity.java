package com.zwstudio.logicpuzzlesandroid.puzzles.tatamino.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.tatamino.data.TataminoDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.tatamino.domain.TataminoGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.tatamino.domain.TataminoGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.tatamino.domain.TataminoGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_help)
public class TataminoHelpActivity extends GameHelpActivity<TataminoGame, TataminoDocument, TataminoGameMove, TataminoGameState> {
    @Bean
    protected TataminoDocument document;
    public TataminoDocument doc() {return document;}
}
