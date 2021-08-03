package com.zwstudio.logicpuzzlesandroid.common.android

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.common.domain.Game
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.GameState
import com.zwstudio.logicpuzzlesandroid.databinding.ActivityGameGameBinding
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

abstract class GameGameActivity<G : Game<G, GM, GS>, GD : GameDocument<GM>, GM, GS : GameState<GM>> : AppCompatActivity(), GameInterface<G, GM, GS> {
    abstract val doc: GD
    private val soundManager: SoundManager by inject()
    protected lateinit var gameView: View
    lateinit var game: G
    protected var levelInitilizing = false

    protected lateinit var binding: ActivityGameGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        binding.activityGameGame.addView(gameView, params)
        binding.tvGame.text = doc.gameTitle
        binding.btnUndo.setOnClickListener {
            game.undo()
        }
        binding.btnRedo.setOnClickListener {
            game.redo()
        }
        binding.btnClear.setOnClickListener {
            yesNoDialog("Do you really want to reset the level?") {
                doc.clearGame()
                startGame()
            }
        }
        binding.btnSaveSolution.setOnClickListener {
            doc.saveSolution(game)
            updateSolutionUI()
        }
        binding.btnLoadSolution.setOnClickListener {
            doc.loadSolution()
            startGame()
        }
        binding.btnDeleteSolution.setOnClickListener {
            yesNoDialog("Do you really want to delete the solution?") {
                doc.deleteSolution()
                updateSolutionUI()
            }
        }
        startGame()
    }

    protected fun startGame() {
        val selectedLevelID = doc.selectedLevelID
        val level = doc.levels.firstOrNull { it.id == selectedLevelID } ?: doc.levels.first()
        binding.tvLevel.text = selectedLevelID
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
        binding.tvMoves.text = String.format("Moves: %d(%d)", game.moveIndex, game.moveCount)
        binding.tvSolved.setTextColor(if (game.isSolved) Color.WHITE else Color.BLACK)
        binding.btnSaveSolution.isEnabled = game.isSolved
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
        soundManager.playSoundSolved()
        doc.gameSolved(game)
        updateSolutionUI()
    }

    protected fun updateSolutionUI() {
        val rec = doc.levelProgressSolution()
        val hasSolution = rec.moveIndex != 0
        binding.tvSolution.text = "Solution: " + if (!hasSolution) "None" else rec.moveIndex.toString()
        binding.btnLoadSolution.isEnabled = hasSolution
        binding.btnDeleteSolution.isEnabled = hasSolution
    }
}