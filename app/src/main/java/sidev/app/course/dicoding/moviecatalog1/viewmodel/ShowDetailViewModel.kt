package sidev.app.course.dicoding.moviecatalog1.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.*
import sidev.app.course.dicoding.moviecatalog1.data.Failure
import sidev.app.course.dicoding.moviecatalog1.data.Success
import sidev.app.course.dicoding.moviecatalog1.data.model.Show
import sidev.app.course.dicoding.moviecatalog1.data.model.ShowDetail
import sidev.app.course.dicoding.moviecatalog1.data.repository.ShowFavRepo
import sidev.app.course.dicoding.moviecatalog1.data.repository.ShowRepo
import sidev.app.course.dicoding.moviecatalog1.util.Const
import sidev.lib.`val`.SuppressLiteral

class ShowDetailViewModel(
    c: Application?,
    private val showRepo: ShowRepo,
    private val favRepo: ShowFavRepo,
    private val type: Const.ShowType,
): AsyncVm(c) {

    companion object {
        fun getInstance(
            owner: ViewModelStoreOwner,
            c: Application?,
            showRepo: ShowRepo,
            favRepo: ShowFavRepo,
            type: Const.ShowType,
        ): ShowDetailViewModel = ViewModelProvider(
            owner,
            object: ViewModelProvider.Factory {
                @Suppress(SuppressLiteral.UNCHECKED_CAST)
                override fun <T : ViewModel?> create(modelClass: Class<T>): T = ShowDetailViewModel(c, showRepo, favRepo, type) as T
            }
        ).get(ShowDetailViewModel::class.java)
    }

    private val mShowDetail: MutableLiveData<ShowDetail> = MutableLiveData()
    val showDetail: LiveData<ShowDetail>
        get()= mShowDetail

    private val mIsFav: MutableLiveData<Boolean> = MutableLiveData()
    val isFav: LiveData<Boolean>
        get()= mIsFav

    fun downloadShowDetail(id: String, forceDownload: Boolean = false): Job? {
        if(!forceDownload && mShowDetail.value != null) return null
        cancelJob()
        doOnPreAsyncTask()
        val job = GlobalScope.launch(Dispatchers.IO, CoroutineStart.LAZY) {
            val result = when(type){
                Const.ShowType.MOVIE -> showRepo.getMovieDetail(ctx, id)
                Const.ShowType.TV -> showRepo.getTvDetail(ctx, id)
            }
            when(result){
                is Success -> mShowDetail.postValue(result.data)
                is Failure -> doCallNotSuccess(result.code, result.e)
            }
        }
        this.job = job
        return job
    }

    fun isFav(showId: String, forceLoad: Boolean = false): Job? {
        if(!forceLoad && mIsFav.value != null) return null
        cancelJob()
        doOnPreAsyncTask()
        val job = GlobalScope.launch(Dispatchers.IO, CoroutineStart.LAZY) {
            val isFav = favRepo.isShowFav(type.ordinal, showId)
            mIsFav.postValue(isFav)
        }
        this.job = job
        return job
    }

    fun insertFav(show: Show) {
        cancelJob()
        doOnPreAsyncTask()
        job = GlobalScope.launch(Dispatchers.IO) {
            val isFav = try {
                favRepo.insertFav(show.copy(type = type.ordinal))
                true
            } catch (e: Throwable) { false }
            mIsFav.postValue(isFav)
        }
    }

    fun deleteFav(show: Show) {
        cancelJob()
        doOnPreAsyncTask()
        job = GlobalScope.launch(Dispatchers.IO) {
            val isNotFav = favRepo.deleteFav(show.copy(type = type.ordinal)) == 1
            mIsFav.postValue(!isNotFav)
        }
    }
}