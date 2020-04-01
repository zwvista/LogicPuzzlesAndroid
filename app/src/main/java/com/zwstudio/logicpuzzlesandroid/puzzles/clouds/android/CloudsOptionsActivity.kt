package com.zwstudio.logicpuzzlesandroid.puzzles.clouds.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.clouds.data.CloudsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.clouds.domain.CloudsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.clouds.domain.CloudsGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.clouds.domain.CloudsGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_options)
class CloudsOptionsActivity : GameOptionsActivity<CloudsGame?, CloudsDocument?, CloudsGameMove?, CloudsGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: CloudsDocument? = null
    override fun doc(): CloudsDocument {
        return document!!
    }
}