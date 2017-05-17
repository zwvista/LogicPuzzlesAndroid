package com.zwstudio.logicpuzzlesandroid.puzzles.domino.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.domino.data.DominoDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.domino.domain.DominoGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.domino.domain.DominoGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.domino.domain.DominoGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_help)
public class DominoHelpActivity extends GameHelpActivity<DominoGame, DominoDocument, DominoGameMove, DominoGameState> {
    @Bean
    protected DominoDocument document;
    public DominoDocument doc() {return document;}
}
