package com.zwstudio.logicpuzzlesandroid.puzzles.holidayisland

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class HolidayIslandGameActivity : GameGameActivity<HolidayIslandGame, HolidayIslandDocument, HolidayIslandGameMove, HolidayIslandGameState>() {
    private val document: HolidayIslandDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = HolidayIslandGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, HolidayIslandHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        HolidayIslandGame(level.layout, this, doc)
}