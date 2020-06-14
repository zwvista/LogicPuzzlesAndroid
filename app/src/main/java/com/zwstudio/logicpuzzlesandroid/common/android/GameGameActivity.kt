package com.zwstudio.logicpuzzlesandroid.common.android

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.common.domain.Game
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.GameState
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.ViewById

@EActivity
abstract class GameGameActivity<G : Game<G, GM, GS>, GD : GameDocument<G, GM>, GM, GS : GameState<GM>> : BaseActivity(), GameInterface<G, GM, GS> {
    abstract val doc: GD

    @ViewById
    protected lateinit var activity_game_game: ViewGroup
    protected lateinit var gameView: View
    @ViewById
    protected lateinit var tvGame: TextView
    @ViewById
    protected lateinit var tvLevel: TextView
    @ViewById
    protected lateinit var tvSolved: TextView
    @ViewById
    protected lateinit var tvMoves: TextView
    @ViewById
    protected lateinit var tvSolution: TextView
    @ViewById
    protected lateinit var btnSaveSolution: Button
    @ViewById
    protected lateinit var btnLoadSolution: Button
    @ViewById
    protected lateinit var btnDeleteSolution: Button
    lateinit var game: G
    protected var levelInitilizing = false

    protected fun init() {
        /*
            <view
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                class="com.zwstudio.logicpuzzlesandroid.puzzles.abc.android.AbcGameView"
                android:id="@+id/gameView"
                android:background="@color/colorBackground"
                android:layout_centerInParent="true"/>
        */
        val params = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.addRule(RelativeLayout.CENTER_IN_PARENT)
        activity_game_game.addView(gameView, params)
        tvGame.text = doc.gameTitle
        startGame()
    }

    @Click
    protected fun btnUndo() {
        game.undo()
    }

    @Click
    protected fun btnRedo() {
        game.redo()
    }

    @Click
    protected fun btnClear() {
        yesNoDialog("Do you really want to reset the level?") {
            doc.clearGame()
            startGame()
        }
    }

    protected fun startGame() {
        val selectedLevelID = doc.selectedLevelID
        val level = doc.levels[doc.levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()

        levelInitilizing = true
        game = newGame(level)
        try {
            // restore game state
            for (rec in doc.moveProgress()) {
                val move = doc.loadMove(rec)
                game.setObject(move)
            }
            val moveIndex = doc.levelProgress().moveIndex
            if (moveIndex in 0 until game.moveCount)
                while (moveIndex != game.moveIndex)
                    game.undo()
        } finally {
            levelInitilizing = false
        }
    }
    protected abstract fun newGame(level: GameLevel): G

    override fun moveAdded(game: G, move: GM) {
        if (levelInitilizing) return
        doc.moveAdded(game, move)
    }

    private fun updateMovesUI(game: G) {
        tvMoves.text = String.format("Moves: %d(%d)", game.moveIndex, game.moveCount)
        tvSolved.setTextColor(if (game.isSolved) Color.WHITE else Color.BLACK)
        btnSaveSolution.isEnabled = game.isSolved
    }

    override fun levelInitilized(game: G, state: GS) {
        gameView.invalidate()
        updateMovesUI(game)
    }

    override fun levelUpdated(game: G, stateFrom: GS, stateTo: GS) {
        gameView.invalidate()
        updateMovesUI(game)
        if (!levelInitilizing) doc.levelUpdated(game)
    }

    override fun gameSolved(game: G) {
        if (levelInitilizing) return
        app.soundManager.playSoundSolved()
        doc.gameSolved(game)
        updateSolutionUI()
    }

    protected fun updateSolutionUI() {
        val rec = doc.levelProgressSolution()
        val hasSolution = rec.moveIndex != 0
        tvSolution.text = "Solution: " + if (!hasSolution) "None" else rec.moveIndex.toString()
        btnLoadSolution.isEnabled = hasSolution
        btnDeleteSolution.isEnabled = hasSolution
    }

    @Click
    protected fun btnSaveSolution() {
        doc.saveSolution(game)
        updateSolutionUI()
    }

    @Click
    protected fun btnLoadSolution() {
        doc.loadSolution()
        startGame()
    }

    @Click
    protected fun btnDeleteSolution() {
        yesNoDialog("Do you really want to delete the solution?") {
            doc.deleteSolution()
            updateSolutionUI()
        }
    }
}