package com.zwstudio.logicpuzzlesandroid.puzzles.rippleeffect

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class RippleEffectGameActivity : GameGameActivity<RippleEffectGame, RippleEffectDocument, RippleEffectGameMove, RippleEffectGameState>() {
    private val document: RippleEffectDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = RippleEffectGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, RippleEffectHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        RippleEffectGame(level.layout, this, doc)
}