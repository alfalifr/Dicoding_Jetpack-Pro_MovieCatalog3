package sidev.app.course.dicoding.moviecatalog1.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import sidev.app.course.dicoding.moviecatalog1.R
import sidev.app.course.dicoding.moviecatalog1.data.repository.ShowRepo
import sidev.app.course.dicoding.moviecatalog1.databinding.PageShowMainBinding
import sidev.app.course.dicoding.moviecatalog1.ui.adapter.ViewPagerAdp
import sidev.app.course.dicoding.moviecatalog1.util.AppConfig
import sidev.app.course.dicoding.moviecatalog1.util.Const
import sidev.app.course.dicoding.moviecatalog1.util.Util.setupWithViewPager

class ListActivity: AppCompatActivity() {
    private lateinit var binding: PageShowMainBinding
    private lateinit var vpAdp: ViewPagerAdp
    private lateinit var showRepo: ShowRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PageShowMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showRepo = intent.getSerializableExtra(Const.KEY_REPO) as? ShowRepo ?: AppConfig.defaultShowRepo

        setTitle(R.string.show_list)

        vpAdp = ViewPagerAdp(this, showRepo)

        binding.apply {
            vp.adapter = vpAdp
            bnv.setupWithViewPager(vp) {
                when(it){
                    R.id.menu_tv -> 0
                    R.id.menu_movie -> 1
                    else -> -1
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_option, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        R.id.menu_fav -> {

            true
        }
        else -> false
    }
}