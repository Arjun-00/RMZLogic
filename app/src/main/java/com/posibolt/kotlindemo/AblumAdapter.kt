import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.posibolt.kotlindemo.R
import com.posibolt.kotlindemo.model.AlbumItem

class AlbumAdapter(var albumItems: List<AlbumItem>) : RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        // Add other views you want to display
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_album, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val albumItem = albumItems[position]
        holder.titleTextView.text = albumItem.title
        // Bind other data to views
    }

    override fun getItemCount(): Int {
        return albumItems.size
    }
}
