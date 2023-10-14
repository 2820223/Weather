package uz.itteacher.myweatherapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import uz.itteacher.myweatherapp.R
import uz.itteacher.myweatherapp.model.today

class TodayAdapter(var todayList: MutableList<today>):RecyclerView.Adapter<TodayAdapter.Holder>() {

    class Holder(view: View): RecyclerView.ViewHolder(view){
        var hour = view.findViewById<TextView>(R.id.hour)
        var hour_temp = view.findViewById<TextView>(R.id.hour_temp)
        var hour_icon = view.findViewById<ImageView>(R.id.hour_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.hour_item,parent,false))
    }

    override fun getItemCount(): Int {
        return todayList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val hour = todayList[position]
        holder.hour.text = hour.time
        holder.hour_temp.text = hour.temp_c
        holder.hour_icon.load(hour.icon)
    }
}