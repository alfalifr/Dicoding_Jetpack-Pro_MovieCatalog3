package sidev.app.course.dicoding.moviecatalog1
/*
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.*
import androidx.test.core.app.ApplicationProvider
import org.junit.Rule
import org.junit.Test
import sidev.app.course.dicoding.moviecatalog1.data.db.ShowFavDao
import sidev.app.course.dicoding.moviecatalog1.data.model.Show
import sidev.app.course.dicoding.moviecatalog1.util.AppConfig
import sidev.lib.check.assertNotNull

///*
class CObTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
///*
    fun afaga(){
        val dao: ShowFavDao
        //val cx = ApplicationProvider.getApplicationContext<Context>()c
        val pager = Pager(
            PagingConfig(
                10
            ),

        ){
            dao.getFavTv()
        }



    //
        pager.liveData.observeForever {
            it.
            it!!.filter {  }
        }

    }
// */

    @Test
    fun test(){
        val movie = Dummy.getMovie().value
        println(movie)
        assertNotNull(movie)
    }
}

// */



object Dummy {
    fun getMovie(): LiveData<Show> {
        val movie = AppConfig.dummyMovieItem
        val liveData = MutableLiveData<Show>()
        liveData.postValue(movie)
        return liveData
    }
}

 */
//TODO 20 May 2021: Add unit testing for showFav repo and VM