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

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     *
     *
     * Derived classes should call through to the base class for it to
     * perform the default menu handling.
     *
     * @param item The menu item that was selected.
     *
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     *
     * @see .onCreateOptionsMenu
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        R.id.menu_fav -> {

            true
        }
        else -> false
    }
}