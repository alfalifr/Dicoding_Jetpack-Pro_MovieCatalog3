package sidev.app.course.dicoding.moviecatalog1.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import sidev.app.course.dicoding.moviecatalog1.R
import sidev.app.course.dicoding.moviecatalog1.databinding.PageShowMainBinding
import sidev.app.course.dicoding.moviecatalog1.ui.adapter.ShowViewPagerAdp
import sidev.app.course.dicoding.moviecatalog1.util.Const
import sidev.app.course.dicoding.moviecatalog1.util.Util.setupWithViewPager

abstract class ShowListAbsActivity: AppCompatActivity() {

    private lateinit var binding: PageShowMainBinding
    private lateinit var vpAdp: ShowViewPagerAdp

    protected abstract fun createFragment(pos: Int): Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PageShowMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vpAdp = ShowViewPagerAdp(this, this::createFragment)

        binding.apply {
            vp.adapter = vpAdp
            bnv.setupWithViewPager(vp) {
                when(it){
                    R.id.menu_tv -> Const.ShowType.TV.ordinal
                    R.id.menu_movie -> Const.ShowType.MOVIE.ordinal
                    else -> -1
                }
            }
        }
    }
}