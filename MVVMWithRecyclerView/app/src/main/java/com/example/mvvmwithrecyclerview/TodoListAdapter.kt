package com.example.mvvmwithrecyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmwithrecyclerview.databinding.TodoListItemBinding


class TodoListAdapter(private var todoItems: List<TodoItem>,
private val onClick : (item: TodoItem) -> Unit
) : RecyclerView.Adapter<TodoListAdapter.ViewHolder>() {

    fun updateList(newList: List<TodoItem>){
        todoItems = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: TodoListItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //parent, false are important parameters for sizing reasons!!
        return ViewHolder(TodoListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = todoItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = todoItems[position]
        holder.binding.textView.text = item.text
        holder.binding.textView.setBackgroundColor(item.color.toArgb())
        holder.binding.button2.setOnClickListener{
            onClick(item)
        }
    }
}
