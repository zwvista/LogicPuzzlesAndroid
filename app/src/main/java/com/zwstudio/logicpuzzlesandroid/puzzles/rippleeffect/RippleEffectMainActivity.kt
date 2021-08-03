package com.zwstudio.logicpuzzlesandroid.puzzles.rippleeffect

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class RippleEffectMainActivity : GameMainActivity<RippleEffectGame, RippleEffectDocument, RippleEffectGameMove, RippleEffectGameState>() {
    private val document: RippleEffectDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, RippleEffectOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, RippleEffectGameActivity::class.java))
    }
}

class RippleEffectOptionsActivity : GameOptionsActivity<RippleEffectGame, RippleEffectDocument, RippleEffectGameMove, RippleEffectGameState>() {
    private val document: RippleEffectDocument by inject()
    override val doc get() = document
}

class RippleEffectHelpActivity : GameHelpActivity<RippleEffectGame, RippleEffectDocument, RippleEffectGameMove, RippleEffectGameState>() {
    private val document: RippleEffectDocument by inject()
    override val doc get() = document
}