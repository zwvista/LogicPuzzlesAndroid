package com.zwstudio.logicpuzzlesandroid.puzzles.pathonthemountains

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class PathOnTheMountainsGameActivity : GameGameActivity<PathOnTheMountainsGame, PathOnTheMountainsDocument, PathOnTheMountainsGameMove, PathOnTheMountainsGameState>() {
    private val document: PathOnTheMountainsDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = PathOnTheMountainsGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, PathOnTheMountainsHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        PathOnTheMountainsGame(level.layout, this, doc)
}