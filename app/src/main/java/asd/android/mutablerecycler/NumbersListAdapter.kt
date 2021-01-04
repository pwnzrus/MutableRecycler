package asd.android.mutablerecycler

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class NumbersListAdapter(var listener: OnRecyclerItemClickListener) :
    ListAdapter<Int, NumbersListAdapter.CustomViewHolder>(DiffCallbackNumber()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recyler_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.numberTextView.text = currentList[position].toString()
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var numberTextView: TextView = itemView.findViewById(R.id.recycler_item_number)
        private var deleteButton: Button = itemView.findViewById(R.id.delete_button)

        init {
            deleteButton.setOnClickListener {
                //Костыль для обхода краша при множественном нажатии на элемент,находящимся на этапе анимации.Лучшего решения не нашел.
                try {
                    listener.onClick(currentList[adapterPosition])
                } catch (e: ArrayIndexOutOfBoundsException) {
                    Log.e("MutableRecycler", e.toString())
                }

            }
        }
    }

    class DiffCallbackNumber : DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }
    }
}