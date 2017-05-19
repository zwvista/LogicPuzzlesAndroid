package com.zwstudio.logicpuzzlesandroid.puzzles.abstractpainting.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.abstractpainting.data.AbstractPaintingDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.abstractpainting.domain.AbstractPaintingGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.abstractpainting.domain.AbstractPaintingGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.abstractpainting.domain.AbstractPaintingGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_help)
public class AbstractPaintingHelpActivity extends GameHelpActivity<AbstractPaintingGame, AbstractPaintingDocument, AbstractPaintingGameMove, AbstractPaintingGameState> {
    @Bean
    protected AbstractPaintingDocument document;
    public AbstractPaintingDocument doc() {return document;}
}
