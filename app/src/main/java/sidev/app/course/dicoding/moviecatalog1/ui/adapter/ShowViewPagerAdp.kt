package sidev.app.course.dicoding.moviecatalog1.ui.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import sidev.app.course.dicoding.moviecatalog1.data.repository.ShowRepo
import sidev.app.course.dicoding.moviecatalog1.ui.fragment.ShowListFragment
import sidev.app.course.dicoding.moviecatalog1.util.AppConfig
import sidev.app.course.dicoding.moviecatalog1.util.Const

//TODO 18 Mei 2021: Add fragment instantiator to `createFragment()`
class ShowViewPagerAdp(
    act: FragmentActivity,
    private val onCreateFragment: (pos: Int) -> Fragment,
): FragmentStateAdapter(act) {
    private val showTypes = Const.ShowType.values()
    override fun getItemCount(): Int = showTypes.size

    override fun createFragment(position: Int): Fragment = onCreateFragment(position).apply {
        val args = arguments ?: Bundle()
        args.putSerializable(Const.KEY_TYPE, showTypes[position])
        arguments = args
    }
}