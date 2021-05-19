package sidev.app.course.dicoding.moviecatalog1.viewmodel

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sidev.app.course.dicoding.moviecatalog1.data.model.Show
import sidev.app.course.dicoding.moviecatalog1.data.repository.ShowFavRepo
import sidev.app.course.dicoding.moviecatalog1.util.Const
import sidev.lib.`val`.SuppressLiteral

class ShowFavViewModel(
    private val repo: ShowFavRepo,
    private val type: Const.ShowType,
): AsyncVm(null) {

    companion object {
        fun getInstance(
            owner: ViewModelStoreOwner,
            repo: ShowFavRepo,
            type: Const.ShowType,
        ): ShowFavViewModel = ViewModelProvider(
            owner,
            object: ViewModelProvider.Factory {
                @Suppress(SuppressLiteral.UNCHECKED_CAST)
                override fun <T : ViewModel?> create(modelClass: Class<T>): T = ShowFavViewModel(repo, type) as T
            }
        ).get(ShowFavViewModel::class.java)
    }

    val showSrc : LiveData<PagingData<Show>>
        get()= mShowSrc
    private val mShowSrc = MediatorLiveData<PagingData<Show>>()

    fun queryFavList(forceLoad: Boolean = false) {
        if(!forceLoad && mShowSrc.value != null) return
        cancelJob()
        doOnPreAsyncTask()
        job = GlobalScope.launch(Dispatchers.IO) {
            val pager = Pager(PagingConfig(8)) {
                when(type){
                    Const.ShowType.TV -> repo.getPopularTvList()
                    Const.ShowType.MOVIE -> repo.getPopularMovieList()
                }
            }
            withContext(Dispatchers.Main) {
                mShowSrc.addSource(pager.liveData) { mShowSrc.postValue(it) }
            }
        }
    }
}