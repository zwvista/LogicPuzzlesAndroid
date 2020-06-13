package com.zwstudio.logicpuzzlesandroid.puzzles.clouds

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class CloudsMainActivity : GameMainActivity<CloudsGame, CloudsDocument, CloudsGameMove, CloudsGameState>() {
    @Bean
    protected lateinit var document: CloudsDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        CloudsOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc.resumeGame()
        CloudsGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class CloudsOptionsActivity : GameOptionsActivity<CloudsGame, CloudsDocument, CloudsGameMove, CloudsGameState>() {
    @Bean
    protected lateinit var document: CloudsDocument
    override val doc get() = document
}

@EActivity(R.layout.activity_game_help)
class CloudsHelpActivity : GameHelpActivity<CloudsGame, CloudsDocument, CloudsGameMove, CloudsGameState>() {
    @Bean
    protected lateinit var document: CloudsDocument
    override val doc get() = document
}