package com.zwstudio.logicpuzzlesandroid.common.data

import com.zwstudio.logicpuzzlesandroid.common.domain.Game
import com.zwstudio.logicpuzzlesandroid.home.android.HomeChooseGameActivity
import com.zwstudio.logicpuzzlesandroid.home.android.LogicPuzzlesApplication
import com.zwstudio.logicpuzzlesandroid.home.android.realm
import org.androidannotations.annotations.AfterInject
import org.androidannotations.annotations.App
import org.androidannotations.annotations.EBean
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream

interface GameDocumentInterface {
    val markerOption: Int
    fun setMarkerOption(rec: GameProgress, o: Int)
    val isAllowedObjectsOnly: Boolean
    fun setAllowedObjectsOnly(rec: GameProgress, o: Boolean)
}

@EBean(scope = EBean.Scope.Singleton)
abstract class GameDocument<GM> : GameDocumentInterface {
    val gameID: String
        get() {
            val name = javaClass.simpleName
            return name.substring(0, name.indexOf("Document"))
        }

    val gameTitle: String
        get() {
            val name = gameID
            return HomeChooseGameActivity.name2title[name] ?: name
        }

    var levels = mutableListOf<GameLevel>()
    lateinit var selectedLevelID: String
    val selectedLevelIDSolution get() = "$selectedLevelID Solution"

    var help = listOf<String>()

    @App
    lateinit var app: LogicPuzzlesApplication

    @AfterInject
    fun init() {
        val filename = gameID + ".xml"
        val in_s = app.applicationContext.assets.open("xml/$filename")
        loadXml(in_s)
        in_s.close()
        selectedLevelID = gameProgress().levelID
    }

    private fun loadXml(in_s: InputStream) {
        val pullParserFactory = XmlPullParserFactory.newInstance()
        val parser = pullParserFactory.newPullParser()
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
        parser.setInput(in_s, null)
        parseXML(parser)
    }

    private fun parseXML(parser: XmlPullParser) {
        fun getCdata(strs: List<String>) = strs.filter { !it.isBlank() }
        var eventType = parser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG)
                when (parser.name) {
                    "level" -> {
                        val id = parser.getAttributeValue(null, "id")
                        val settings = mutableMapOf<String, String>()
                        for (i in 0 until parser.attributeCount)
                            settings[parser.getAttributeName(i)] = parser.getAttributeValue(i)
                        var layout = parser.nextText().split("\n").map { it.replace("\r", "") }
                        layout = getCdata(layout).map { it.replace("`", "") }
                        val level = GameLevel()
                        level.id = id
                        level.layout = layout
                        level.settings = settings
                        levels.add(level)
                    }
                    "help" -> {
                        help = parser.nextText().split("\n").map { it.replace("\r", "") }
                        help = getCdata(help)
                    }
                    else -> {}
                }
            eventType = parser.next()
        }
    }

    fun gameProgress(): GameProgress {
        var rec = realm.where(GameProgress::class.java)
            .equalTo("gameID", gameID)
            .findFirst()
        if (rec == null) {
            realm.beginTransaction()
            rec = GameProgress()
            rec.gameID = gameID
            rec.levelID = levels[0].id
            realm.insertOrUpdate(rec)
            realm.commitTransaction()
        }
        return rec
    }

    private fun levelProgress(levelID: String): LevelProgress {
        var rec = realm.where(LevelProgress::class.java)
            .equalTo("gameID", gameID)
            .equalTo("levelID", levelID).findFirst()
        if (rec == null) {
            realm.beginTransaction()
            rec = LevelProgress()
            rec.gameID = gameID
            rec.levelID = levelID
            realm.insertOrUpdate(rec)
            realm.commitTransaction()
        }
        return rec
    }
    fun levelProgress() = levelProgress(selectedLevelID)
    fun levelProgressSolution() = levelProgress(selectedLevelIDSolution)

    private fun moveProgress(levelID: String): List<MoveProgress> =
        realm.where(MoveProgress::class.java)
            .equalTo("gameID", gameID)
            .equalTo("levelID", levelID)
            .sort("moveIndex")
            .findAll()
    fun moveProgress() = moveProgress(selectedLevelID)
    fun moveProgressSolution() = moveProgress(selectedLevelIDSolution)

    fun levelUpdated(game: Game<*, *, *>) {
        realm.beginTransaction()
        val rec = levelProgress()
        rec.moveIndex = game.moveIndex
        realm.insertOrUpdate(rec)
        realm.commitTransaction()
    }

    fun gameSolved(game: Game<*, *, *>) {
        val recLP = levelProgress()
        val recLPS = levelProgressSolution()
        if (recLPS.moveIndex == 0 || recLPS.moveIndex > recLP.moveIndex) saveSolution(game)
    }

    fun moveAdded(game: Game<*, *, *>, move: GM) {
        realm.beginTransaction()
        realm.where(MoveProgress::class.java)
            .equalTo("gameID", gameID)
            .equalTo("levelID", selectedLevelID)
            .greaterThanOrEqualTo("moveIndex", game.moveIndex)
            .findAll().deleteAllFromRealm()
        val rec = MoveProgress()
        rec.gameID = gameID
        rec.levelID = selectedLevelID
        rec.moveIndex = game.moveIndex
        saveMove(move, rec)
        realm.insertOrUpdate(rec)
        realm.commitTransaction()
    }

    protected abstract fun saveMove(move: GM, rec: MoveProgress)
    abstract fun loadMove(rec: MoveProgress): GM
    fun resumeGame() {
        realm.beginTransaction()
        val rec = gameProgress()
        rec.levelID = selectedLevelID
        realm.insertOrUpdate(rec)
        realm.commitTransaction()
    }

    fun clearGame() {
        realm.beginTransaction()
        realm.where(MoveProgress::class.java)
            .equalTo("gameID", gameID)
            .equalTo("levelID", selectedLevelID)
            .findAll().deleteAllFromRealm()
        val rec = levelProgress()
        rec.moveIndex = 0
        realm.insertOrUpdate(rec)
        realm.commitTransaction()
    }

    private fun copyMoves(moveProgressFrom: List<MoveProgress>, levelIDTo: String) {
        realm.where(MoveProgress::class.java)
            .equalTo("gameID", gameID)
            .equalTo("levelID", levelIDTo)
            .findAll().deleteAllFromRealm()
        for (recMP in moveProgressFrom) {
            val move = loadMove(recMP)
            val recMPS = MoveProgress()
            recMPS.gameID = gameID
            recMPS.levelID = levelIDTo
            recMPS.moveIndex = recMP.moveIndex
            saveMove(move, recMPS)
            realm.insertOrUpdate(recMPS)
        }
    }

    fun saveSolution(game: Game<*, *, *>) {
        realm.beginTransaction()
        copyMoves(moveProgress(), selectedLevelIDSolution)
        val rec = levelProgressSolution()
        rec.moveIndex = game.moveIndex
        realm.insertOrUpdate(rec)
        realm.commitTransaction()
    }

    fun loadSolution() {
        realm.beginTransaction()
        val mps = moveProgressSolution()
        copyMoves(mps, selectedLevelID)
        val rec = levelProgress()
        rec.moveIndex = mps.size
        realm.insertOrUpdate(rec)
        realm.commitTransaction()
    }

    fun deleteSolution() {
        realm.beginTransaction()
        realm.where(MoveProgress::class.java)
            .equalTo("gameID", gameID)
            .equalTo("levelID", selectedLevelIDSolution)
            .findAll().deleteAllFromRealm()
        val rec = levelProgressSolution()
        rec.moveIndex = 0
        realm.insertOrUpdate(rec)
        realm.commitTransaction()
    }

    fun resetAllLevels() {
        realm.beginTransaction()
        realm.where(MoveProgress::class.java)
            .equalTo("gameID", gameID)
            .findAll().deleteAllFromRealm()
        realm.where(LevelProgress::class.java)
            .equalTo("gameID", gameID)
            .findAll().deleteAllFromRealm()
        realm.commitTransaction()
    }

    override val markerOption get() = gameProgress().option1?.toInt() ?: 0

    override fun setMarkerOption(rec: GameProgress, o: Int) {
        rec.option1 = o.toString()
    }

    override val isAllowedObjectsOnly get() = gameProgress().option2?.toBoolean() ?: false

    override fun setAllowedObjectsOnly(rec: GameProgress, o: Boolean) {
        rec.option2 = o.toString()
    }
}