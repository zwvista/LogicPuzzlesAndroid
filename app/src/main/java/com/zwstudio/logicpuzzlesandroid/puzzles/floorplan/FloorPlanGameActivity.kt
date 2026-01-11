package com.zwstudio.logicpuzzlesandroid.puzzles.floorplan

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class FloorPlanGameActivity : GameGameActivity<FloorPlanGame, FloorPlanDocument, FloorPlanGameMove, FloorPlanGameState>() {
    private val document: FloorPlanDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = FloorPlanGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, FloorPlanHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        FloorPlanGame(level.layout, this, doc)
}