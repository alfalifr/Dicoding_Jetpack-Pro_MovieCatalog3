package sidev.app.course.dicoding.moviecatalog1.util

import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource
import sidev.app.course.dicoding.moviecatalog1.data.datasource.ShowDataSource
import sidev.app.course.dicoding.moviecatalog1.data.datasource.ShowRemoteSource
import sidev.app.course.dicoding.moviecatalog1.data.model.Show
import sidev.app.course.dicoding.moviecatalog1.data.model.ShowDetail
import sidev.app.course.dicoding.moviecatalog1.data.repository.ShowApiRepo
import sidev.app.course.dicoding.moviecatalog1.data.repository.ShowRepo

/**
 * Value inside this class should be modified in testing process, e.g. unit / instrumented testing.
 */
object AppConfig {
    enum class UiTestType {
        ESPRESSO, ROBOLECTRIC
    }

    private val mIdlingRes by lazy { CountingIdlingResource("GLOBAL") }
    private val mLock by lazy { CountingLatch() }

    private var uiTestType = UiTestType.ESPRESSO

    private val defaultShowRemoteSource: ShowDataSource = ShowRemoteSource
    var defaultShowRepo: ShowRepo = ShowApiRepo(defaultShowRemoteSource)

    fun resetDefautlShowRepo(){
        defaultShowRepo = ShowApiRepo(defaultShowRemoteSource)
    }

    var isUiAsyncTest = false

    val idlingRes: IdlingResource?
        get()= if(isUiAsyncTest) mIdlingRes else null

    fun incUiAsync(){
        if(isUiAsyncTest) when(uiTestType){
            UiTestType.ESPRESSO -> mIdlingRes.increment()
            UiTestType.ROBOLECTRIC -> mLock.increment()
        }
    }
    fun decUiAsync(){
        if(isUiAsyncTest) when(uiTestType){
            UiTestType.ESPRESSO -> mIdlingRes.decrement()
            UiTestType.ROBOLECTRIC -> mLock.decrement()
        }
    }

    val dummyMovieItem = Show(
        id="458576",
        title="Monster Hunter",
        img="/1UCOF11QCw8kcqvce8LKOO6pimh.jpg",
        release="2020-12-03",
        rating=7.1
    )
    val dummyMovieDetail = ShowDetail(
        dummyMovieItem,
        listOf("Fantasy", "Action", "Adventure"),
        145,
        "Behind our world, there is another.",
        "A portal transports Cpt. Artemis and an elite unit of soldiers to a strange world where powerful monsters rule with deadly ferocity. Faced with relentless danger, the team encounters a mysterious hunter who may be their only hope to find a way home.",
        "/z8TvnEVRenMSTemxYZwLGqFofgF.jpg"
    )

    val dummyTvItem = Show(
        id="1429",
        title="Attack on Titan",
        img="/aiy35Evcofzl7hASZZvsFgltHTX.jpg",
        release="2013-04-07",
        rating=7.1
    )
    val dummyTvDetail = ShowDetail(
        dummyTvItem,
        listOf("Sci-Fi & Fantasy", "Animation", "Action & Adventure"),
        null,
        "He keeps moving forward until all his enemies get destroyed",
        "Several hundred years ago, humans were nearly exterminated by Titans. Titans are typically several stories tall, seem to have no intelligence, devour human beings and, worst of all, seem to do it for the pleasure rather than as a food source. A small percentage of humanity survived by walling themselves in a city protected by extremely high walls, even taller than the biggest Titans. Flash forward to the present and the city has not seen a Titan in over 100 years. Teenage boy Eren and his foster sister Mikasa witness something horrific as the city walls are destroyed by a Colossal Titan that appears out of thin air. As the smaller Titans flood the city, the two kids watch in horror as their mother is eaten alive. Eren vows that he will murder every single Titan and take revenge for all of mankind.",
        "/yvKrycViRMQcIgdnjsM5JGNWU4Q.jpg"
    )
}