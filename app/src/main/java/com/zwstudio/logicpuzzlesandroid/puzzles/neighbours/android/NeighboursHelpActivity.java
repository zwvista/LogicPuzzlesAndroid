package com.zwstudio.logicpuzzlesandroid.puzzles.neighbours.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.neighbours.data.NeighboursDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.neighbours.domain.NeighboursGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.neighbours.domain.NeighboursGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.neighbours.domain.NeighboursGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_help)
public class NeighboursHelpActivity extends GameHelpActivity<NeighboursGame, NeighboursDocument, NeighboursGameMove, NeighboursGameState> {
    @Bean
    protected NeighboursDocument document;
    public NeighboursDocument doc() {return document;}
}
