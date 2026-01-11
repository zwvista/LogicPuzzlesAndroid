package com.zwstudio.logicpuzzlesandroid.puzzles.floorplan

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class FloorPlanMainActivity : GameMainActivity<FloorPlanGame, FloorPlanDocument, FloorPlanGameMove, FloorPlanGameState>() {
    private val document: FloorPlanDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, FloorPlanOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, FloorPlanGameActivity::class.java))
    }
}

class FloorPlanOptionsActivity : GameOptionsActivity<FloorPlanGame, FloorPlanDocument, FloorPlanGameMove, FloorPlanGameState>() {
    private val document: FloorPlanDocument by inject()
    override val doc get() = document
}

class FloorPlanHelpActivity : GameHelpActivity<FloorPlanGame, FloorPlanDocument, FloorPlanGameMove, FloorPlanGameState>() {
    private val document: FloorPlanDocument by inject()
    override val doc get() = document
}