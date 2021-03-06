package sidev.app.course.dicoding.moviecatalog1.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.annotation.CallSuper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.jetbrains.anko.runOnUiThread

/**
 * Template for all ViewModel in this project.
 * This class mimics [AndroidViewModel] but with optional [app] parameter for convinience in unit testing.
 */
@SuppressLint("StaticFieldLeak")
open class AsyncVm(app: Application?): ViewModel() {
    protected var ctx: Context? = app
        private set

    private var isJobJoin = false
    protected var job: Job?= null
        set(v){
            if(!isJobJoin)
                field = v
        }

    /**
     * Executed before any async task in `this` runs.
     */
    protected var onPreAsyncTask: (() -> Unit)?= null
    private var onCallNotSuccess: ((code: Int, e: Throwable?) -> Unit)?= null

    /**
     * This method will be called when this ViewModel is no longer used and will be destroyed.
     *
     *
     * It is useful when ViewModel observes some data and you need to clear this subscription to
     * prevent a leak of this ViewModel.
     */
    @CallSuper
    override fun onCleared() {
        ctx = null //So there won't be a memory leak.
    }

    protected fun cancelJob(){
        job?.cancel()
    }
    fun onPreAsyncTask(f: (() -> Unit)?){
        onPreAsyncTask= f
    }
    fun onCallNotSuccess(f: ((code: Int, e: Throwable?) -> Unit)?){
        onCallNotSuccess= f
    }
    protected fun doOnPreAsyncTask(){
        onPreAsyncTask?.also { ctx?.runOnUiThread { it() } }
    }
    protected fun doCallNotSuccess(code: Int, e: Throwable?){
        onCallNotSuccess?.also { ctx?.runOnUiThread { it(code, e) } }
    }

    fun multipleJob(lazyBlock: () -> List<Job?>) {
        cancelJob()
        isJobJoin = false
        job = GlobalScope.launch(Dispatchers.IO) {
            isJobJoin = true
            val jobs = lazyBlock()
            if(jobs.isEmpty()) return@launch
            val firstJob = jobs.first()
            var prevJob = firstJob
            for(i in 1 until jobs.size) {
                val currJob = jobs[i]
                prevJob?.invokeOnCompletion { currJob?.start() }
                    ?: run {
                        currJob?.start()
                    }
                prevJob = currJob
            }
            firstJob?.start()
            isJobJoin = false
        }
    }
}