package sidev.app.course.dicoding.moviecatalog1.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sidev.app.course.dicoding.moviecatalog1.data.model.Show
import sidev.app.course.dicoding.moviecatalog1.databinding.ItemMainListBinding


class ShowAdp: RecyclerView.Adapter<ShowViewHolder>(), ShowViewHolder.OnItemClick {
    //RecyclerView.Adapter<ShowAdp.ViewHolder>() {

    var dataList: List<Show>?= null
        set(v) {
            field = v
            notifyDataSetChanged()
        }

    private var onItemClick: ShowViewHolder.OnItemClick?= null //((pos: Int, data: Show) -> Unit)? = null
    fun setOnItemClick(l: ShowViewHolder.OnItemClick?) {
        onItemClick = l
    }

    override fun getItemCount(): Int = dataList?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        val binding = ItemMainListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShowViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        //when the `dataList` is null, then this method won't be invoked.
        val data = dataList!![position]
        holder.bind(data)
    }

    override fun onItemClick(pos: Int, data: Show) {
        onItemClick?.onItemClick(pos, data)
    }
}