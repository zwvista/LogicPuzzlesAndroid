package com.zwstudio.logicpuzzlesandroid.puzzles.paintthenurikabe.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.paintthenurikabe.data.PaintTheNurikabeDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.paintthenurikabe.domain.PaintTheNurikabeGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.paintthenurikabe.domain.PaintTheNurikabeGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.paintthenurikabe.domain.PaintTheNurikabeGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_options)
public class PaintTheNurikabeOptionsActivity extends GameOptionsActivity<PaintTheNurikabeGame, PaintTheNurikabeDocument, PaintTheNurikabeGameMove, PaintTheNurikabeGameState> {
    @Bean
    protected PaintTheNurikabeDocument document;
    public PaintTheNurikabeDocument doc() {return document;}
}
