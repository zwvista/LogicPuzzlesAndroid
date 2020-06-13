package com.zwstudio.logicpuzzlesandroid.puzzles.castlebailey.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.castlebailey.data.CastleBaileyDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.castlebailey.domain.CastleBaileyGame
import com.zwstudio.logicpuzzlesandroid.puzzles.castlebailey.domain.CastleBaileyGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.castlebailey.domain.CastleBaileyGameState

import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class CastleBaileyMainActivity : GameMainActivity<CastleBaileyGame, CastleBaileyDocument, CastleBaileyGameMove, CastleBaileyGameState>() {
    @Bean
    protected lateinit var document: CastleBaileyDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        CastleBaileyOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc.resumeGame()
        CastleBaileyGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class CastleBaileyOptionsActivity : GameOptionsActivity<CastleBaileyGame, CastleBaileyDocument, CastleBaileyGameMove, CastleBaileyGameState>() {
    @Bean
    protected lateinit var document: CastleBaileyDocument
    override val doc get() = document
}

@EActivity(R.layout.activity_game_help)
class CastleBaileyHelpActivity : GameHelpActivity<CastleBaileyGame, CastleBaileyDocument, CastleBaileyGameMove, CastleBaileyGameState>() {
    @Bean
    protected lateinit var document: CastleBaileyDocument
    override val doc get() = document
}
