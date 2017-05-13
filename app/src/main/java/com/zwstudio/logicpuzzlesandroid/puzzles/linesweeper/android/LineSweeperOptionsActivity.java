package com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.data.LineSweeperDocument;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_options)
public class LineSweeperOptionsActivity extends GameOptionsActivity {
    public LineSweeperDocument doc() {return app.linesweeperDocument;}

    protected void onDefault() {}
}
