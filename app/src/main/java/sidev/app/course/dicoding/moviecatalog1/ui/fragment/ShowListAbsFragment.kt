package sidev.app.course.dicoding.moviecatalog1.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sidev.app.course.dicoding.moviecatalog1.R
import sidev.app.course.dicoding.moviecatalog1.data.repository.ShowRepo
import sidev.app.course.dicoding.moviecatalog1.databinding.PageShowListBinding
import sidev.app.course.dicoding.moviecatalog1.ui.activity.DetailActivity
import sidev.app.course.dicoding.moviecatalog1.ui.adapter.ShowAdp
import sidev.app.course.dicoding.moviecatalog1.util.AppConfig
import sidev.app.course.dicoding.moviecatalog1.util.Const
import sidev.app.course.dicoding.moviecatalog1.viewmodel.AsyncVm
import sidev.app.course.dicoding.moviecatalog1.viewmodel.ShowListViewModel
import sidev.lib.android.std.tool.util.`fun`.startAct

abstract class ShowListAbsFragment: Fragment() {
    private lateinit var binding: PageShowListBinding
/*
    private lateinit var adp: ShowAdp
    private lateinit var vm: ShowListViewModel
    private lateinit var showRepo: ShowRepo
 */
    protected abstract val adp: RecyclerView.Adapter<*>
    protected abstract val vm: AsyncVm
    //private lateinit var showRepo: ShowRepo
    protected var type: Const.ShowType = Const.ShowType.TV
        private set

    protected abstract fun onAfterVmConfigured()
    protected open fun onGetArgs(args: Bundle) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            type = getSerializable(Const.KEY_TYPE) as Const.ShowType
            onGetArgs(this)
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
/*
        adp = ShowAdp().apply {
            setOnItemClick { _, data ->
                startAct<DetailActivity>(
                    Const.KEY_SHOW to data,
                    Const.KEY_TYPE to type,
                )
            }
        }
 */
        binding.apply {
            rv.apply {
                adapter = adp
                layoutManager = GridLayoutManager(requireContext(), 2)
            }
        }

        //vm = ShowListViewModel.getInstance(this, requireActivity().application, showRepo, type).apply {
        vm.apply {
            onPreAsyncTask {
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

    protected  fun showDataAnomaly(show: Boolean = true) = binding.apply {
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