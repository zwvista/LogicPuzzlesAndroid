package com.zwstudio.logicgamesandroid.games.slitherlink.android;

import com.zwstudio.logicgamesandroid.R;
import com.zwstudio.logicgamesandroid.common.android.GameActivity;
import com.zwstudio.logicgamesandroid.common.domain.Position;
import com.zwstudio.logicgamesandroid.games.slitherlink.data.SlitherLinkDocument;
import com.zwstudio.logicgamesandroid.games.slitherlink.data.SlitherLinkMoveProgress;
import com.zwstudio.logicgamesandroid.games.slitherlink.domain.SlitherLinkGame;
import com.zwstudio.logicgamesandroid.games.slitherlink.domain.SlitherLinkGameMove;
import com.zwstudio.logicgamesandroid.games.slitherlink.domain.SlitherLinkGameState;
import com.zwstudio.logicgamesandroid.games.slitherlink.domain.SlitherLinkObject;
import com.zwstudio.logicgamesandroid.games.slitherlink.domain.SlitherLinkObjectOrientation;

import org.androidannotations.annotations.EActivity;

import java.util.List;

@EActivity(R.layout.activity_slitherlink_game)
public class SlitherLinkGameActivity extends GameActivity<SlitherLinkGame, SlitherLinkDocument, SlitherLinkGameMove, SlitherLinkGameState> {
    public SlitherLinkDocument doc() {return app.slitherlinkDocument;}

    protected void startGame() {
        String selectedLevelID = doc().selectedLevelID;
        List<String> layout = doc().levels.get(selectedLevelID);
        tvLevel.setText(selectedLevelID);

        levelInitilizing = true;
        game = new SlitherLinkGame(layout, this);
        try {
            // restore game state
            for (SlitherLinkMoveProgress rec : doc().moveProgress()) {
                SlitherLinkGameMove move = new SlitherLinkGameMove();
                move.p = new Position(rec.row, rec.col);
                move.objOrientation = SlitherLinkObjectOrientation.values()[rec.objOrientation];
                move.obj = SlitherLinkObject.values()[rec.obj];
                game.setObject(move);
            }
            int moveIndex = doc().levelProgress().moveIndex;
            if (!(moveIndex >= 0 && moveIndex < game.moveCount())) return;
            while (moveIndex != game.moveIndex())
                game.undo();
        } finally {
            levelInitilizing = false;
        }
    }
}
