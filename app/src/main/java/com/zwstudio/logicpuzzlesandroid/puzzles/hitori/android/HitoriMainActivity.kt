package com.zwstudio.logicpuzzlesandroid.puzzles.hitori.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.hitori.data.HitoriDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.hitori.domain.HitoriGame
import com.zwstudio.logicpuzzlesandroid.puzzles.hitori.domain.HitoriGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.hitori.domain.HitoriGameState

import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class HitoriMainActivity : GameMainActivity<HitoriGame, HitoriDocument, HitoriGameMove, HitoriGameState>() {
    @Bean
    protected lateinit var document: HitoriDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        HitoriOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc.resumeGame()
        HitoriGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class HitoriOptionsActivity : GameOptionsActivity<HitoriGame, HitoriDocument, HitoriGameMove, HitoriGameState>() {
    @Bean
    protected lateinit var document: HitoriDocument
    override val doc get() = document
}

@EActivity(R.layout.activity_game_help)
class HitoriHelpActivity : GameHelpActivity<HitoriGame, HitoriDocument, HitoriGameMove, HitoriGameState>() {
    @Bean
    protected lateinit var document: HitoriDocument
    override val doc get() = document
}
