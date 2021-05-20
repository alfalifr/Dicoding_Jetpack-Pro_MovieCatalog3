package sidev.app.course.dicoding.moviecatalog1.ui

import android.content.Context
import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.annotation.Config
import sidev.app.course.dicoding.moviecatalog1.R
import sidev.app.course.dicoding.moviecatalog1.RobolectricTestingUtil
import sidev.app.course.dicoding.moviecatalog1.data.repository.ShowDummyRepo
import sidev.app.course.dicoding.moviecatalog1.data.repository.ShowEmptyRepo
import sidev.app.course.dicoding.moviecatalog1.data.repository.ShowErrorRepo
import sidev.app.course.dicoding.moviecatalog1.ui.activity.ShowListActivity
import sidev.app.course.dicoding.moviecatalog1.util.AppConfig
import sidev.app.course.dicoding.moviecatalog1.util.Dummy

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class ShowListActivityUnitTest {

    val textMatcher = RobolectricTestingUtil.ViewMatchers::textMatchesAndDisplayed

    // The activity creation line can't be located in @Before because
    // it depends on TestingUtil.defaultShowRepo set first.
    // So the best way is to gather bolierplate code in this method.
    private fun createActivity(): ShowListActivity = Robolectric.buildActivity(ShowListActivity::class.java)
        .create()
        .start()
        .resume()
        .visible()
        .get()

    @After
    fun finish(){
        AppConfig.resetDefautlShowRepo()
    }

    @Test
    fun getShowList(){
        // Use dummy repo because Robolectric can't integrate with Espresso Idling Resource.
        AppConfig.defaultShowRepo = ShowDummyRepo
        val data = Dummy.dummyTvItem

        val act = createActivity()

        // Assert RecyclerView is displayed.
        val rv = act.findViewById<RecyclerView>(R.id.rv)
        assertNotNull(rv)
        assert(ViewMatchers.isDisplayed().matches(rv))

        // Assert RecyclerView's LayoutManger is not null.
        val lm = rv.layoutManager
        assertNotNull(lm)

        val item = lm!!.findViewByPosition(0)

        // Assert first item is displayed
        assert(ViewMatchers.isDisplayed().matches(item))

        // Assert title is displayed and not template
        val tvTitle = item!!.findViewById<TextView>(R.id.tv_title)
        val strTitle = ApplicationProvider.getApplicationContext<Context>().getString(R.string.title)
        assert(textMatcher { it != strTitle && it == data.title }.matches(tvTitle))

        // Assert release date is displayed and not template
        val tvRelease = item.findViewById<TextView>(R.id.tv_release)
        val strRelease = ApplicationProvider.getApplicationContext<Context>().getString(R.string.release_date)
        assert(textMatcher { it != strRelease && it.isNotBlank() }.matches(tvRelease))

        // Assert loading progress bar should be gone.
        val pb = act.findViewById<View>(R.id.pb_loading)
        assert(!ViewMatchers.isDisplayed().matches(pb))

        // Assert no data TextView should be gone.
        val tvNoData = act.findViewById<View>(R.id.tv_no_data)
        assert(!ViewMatchers.isDisplayed().matches(tvNoData))
    }

    @Test
    fun getShowListOnError(){
        AppConfig.defaultShowRepo = ShowErrorRepo
        val act = createActivity()

        // Assert RecyclerView should be gone but not null.
        val rv = act.findViewById<RecyclerView>(R.id.rv)
        assertNotNull(rv)
        assert(!ViewMatchers.isDisplayed().matches(rv))

        // Assert loading progress bar should be gone.
        val pb = act.findViewById<View>(R.id.pb_loading)
        assert(!ViewMatchers.isDisplayed().matches(pb))

        // Assert error TextView is displayed and shows error message
        val tvError = act.findViewById<TextView>(R.id.tv_no_data)
        assert(ViewMatchers.isDisplayed().matches(tvError))
        assert(
            textMatcher { it.startsWith("Error:") && it.contains("cause:") }.matches(tvError)
        )
    }

    @Test
    fun getShowListOnNoData(){
        AppConfig.defaultShowRepo = ShowEmptyRepo
        val act = createActivity()

        // Assert RecyclerView should be gone but not null.
        val rv = act.findViewById<RecyclerView>(R.id.rv)
        assertNotNull(rv)
        assert(!ViewMatchers.isDisplayed().matches(rv))

        // Assert loading progress bar should be gone.
        val pb = act.findViewById<View>(R.id.pb_loading)
        assert(!ViewMatchers.isDisplayed().matches(pb))

        // Assert error TextView is displayed and shows no data message.
        val tvNoData = act.findViewById<TextView>(R.id.tv_no_data)
        val strNoData = ApplicationProvider.getApplicationContext<Context>().getString(R.string.no_data)
        assert(ViewMatchers.isDisplayed().matches(tvNoData))
        assert(ViewMatchers.withText(strNoData).matches(tvNoData))
    }
}