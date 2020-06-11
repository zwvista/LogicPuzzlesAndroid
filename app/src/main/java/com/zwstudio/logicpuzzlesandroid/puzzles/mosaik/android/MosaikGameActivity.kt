package com.zwstudio.logicpuzzlesandroid.puzzles.mosaik.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.mosaik.data.MosaikDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.mosaik.domain.MosaikGame
import com.zwstudio.logicpuzzlesandroid.puzzles.mosaik.domain.MosaikGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.mosaik.domain.MosaikGameState
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class MosaikGameActivity : GameGameActivity<MosaikGame, MosaikDocument, MosaikGameMove, MosaikGameState>() {
    @Bean
    protected lateinit var document: MosaikDocument
    override fun doc() = document

    protected lateinit var gameView2: MosaikGameView
    override fun getGameView() = gameView2

    @AfterViews
    protected override fun init() {
        gameView2 = MosaikGameView(this)
        super.init()
    }

    protected override fun startGame() {
        val selectedLevelID = doc().selectedLevelID
        val level = doc().levels[doc().levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()
        levelInitilizing = true
        game = MosaikGame(level.layout, this, doc())
        try {
            // restore game state
            for (rec in doc().moveProgress()) {
                val move = doc().loadMove(rec)
                game.setObject(move)
            }
            val moveIndex = doc().levelProgress().moveIndex
            if (moveIndex >= 0 && moveIndex < game.moveCount())
                while (moveIndex != game.moveIndex())
                    game.undo()
        } finally {
            levelInitilizing = false
        }
    }

    @Click
    protected fun btnHelp() {
        MosaikHelpActivity_.intent(this).start()
    }
}