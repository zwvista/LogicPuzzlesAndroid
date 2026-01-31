package com.zwstudio.logicpuzzlesandroid.puzzles.assemblyinstructions

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class AssemblyInstructionsGameActivity : GameGameActivity<AssemblyInstructionsGame, AssemblyInstructionsDocument, AssemblyInstructionsGameMove, AssemblyInstructionsGameState>() {
    private val document: AssemblyInstructionsDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = AssemblyInstructionsGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, AssemblyInstructionsHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        AssemblyInstructionsGame(level.layout, this, doc)
}