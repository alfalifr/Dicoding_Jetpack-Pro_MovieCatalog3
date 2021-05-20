package sidev.app.course.dicoding.moviecatalog1.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sidev.app.course.dicoding.moviecatalog1.R
import sidev.app.course.dicoding.moviecatalog1.databinding.PageShowListBinding
import sidev.app.course.dicoding.moviecatalog1.util.AppConfig
import sidev.app.course.dicoding.moviecatalog1.util.Const
import sidev.app.course.dicoding.moviecatalog1.viewmodel.AsyncVm
import sidev.lib.android.std.tool.util.`fun`.loge

abstract class ShowListAbsFragment: Fragment() {
    private lateinit var binding: PageShowListBinding

    protected abstract val adp: RecyclerView.Adapter<*>
    protected abstract val vm: AsyncVm
    protected var type: Const.ShowType = Const.ShowType.TV
        private set

    protected abstract fun onAfterVmConfigured()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            type = getSerializable(Const.KEY_TYPE) as Const.ShowType
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PageShowListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            rv.apply {
                adapter = adp
                layoutManager = GridLayoutManager(requireContext(), 2)
            }
        }

        vm.apply {
            onPreAsyncTask {
                loge("onPreAsyncTask() AppConfig.incUiAsync()")
                AppConfig.incUiAsync()
                showNoData(false)
                showLoading()
            }
            onCallNotSuccess { code, e ->
                showLoading(false)
                showDataError(true, code, e)
                AppConfig.decUiAsync()
            }
            onAfterVmConfigured()
        }
    }

    protected fun showNoData(show: Boolean = true) {
        showDataAnomaly(show)
        binding.tvNoData.text = getString(R.string.no_data)
    }

    @Suppress("SameParameterValue")
    protected  fun showDataError(show: Boolean = true, code: Int = -1, e: Throwable? = null) {
        showDataAnomaly(show)
        val eClass = if(e != null) e::class.java.simpleName else "null"
        binding.tvNoData.text = getString(R.string.error_data, "$eClass ($code)", e?.message ?: "null")
    }

    private fun showDataAnomaly(show: Boolean = true) = binding.apply {
        if(show){
            tvNoData.visibility = View.VISIBLE
            rv.visibility = View.GONE
        } else {
            tvNoData.visibility = View.GONE
            rv.visibility = View.VISIBLE
        }
    }

    protected  fun showLoading(show: Boolean = true) = binding.apply {
        if(show){
            pbLoading.visibility = View.VISIBLE
            rv.visibility = View.GONE
        } else {
            pbLoading.visibility = View.GONE
            rv.visibility = View.VISIBLE
        }
    }
}