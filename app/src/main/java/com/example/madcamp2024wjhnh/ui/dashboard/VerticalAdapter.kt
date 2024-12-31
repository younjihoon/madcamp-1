import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp2024wjhnh.R
import com.example.madcamp2024wjhnh.data.Photo
import com.example.madcamp2024wjhnh.data.Section
import com.example.madcamp2024wjhnh.ui.dashboard.HorizontalAdapter

class VerticalAdapter(private val context: Context, private var sections: List<Section>,private val onFavoriteStatusChanged: (Photo) -> Unit ) :
    RecyclerView.Adapter<VerticalAdapter.VerticalViewHolder>() {

    inner class VerticalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sectionTitle: TextView = itemView.findViewById(R.id.sectionTitle)
        val horizontalRecyclerView: RecyclerView = itemView.findViewById(R.id.horizontalRecyclerView)
    }
    private lateinit var horizontalAdapter : HorizontalAdapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerticalViewHolder {
        Log.e("[VertAdapter]","CREATE")
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dashboard, parent, false)

        return VerticalViewHolder(view)
    }

    override fun onBindViewHolder(holder: VerticalViewHolder, position: Int) {
        val section = sections[position]
        holder.sectionTitle.text = section.title

        // 가로 RecyclerView 설정
        val horizontalAdapter = HorizontalAdapter(context,section.items) { updatedPhoto ->
            onFavoriteStatusChanged(updatedPhoto)
        }
        Log.e("[VertAdapter]","horizontalAdapter: $horizontalAdapter")
        holder.horizontalRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = horizontalAdapter
        }
    }

    override fun getItemCount(): Int = sections.size

    fun updateSections(newSections: List<Section>) {
        sections = newSections
        notifyDataSetChanged()
    }
}
