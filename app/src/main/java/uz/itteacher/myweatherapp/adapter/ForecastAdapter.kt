package uz.itteacher.myweatherapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import uz.itteacher.myweatherapp.R
import uz.itteacher.myweatherapp.model.Forecast

class ForecastAdapter(var forecasts:MutableList<Forecast>, var dayInterface: DayInterface) : RecyclerView.Adapter<ForecastAdapter.Holder>() {

    class Holder(view:View):RecyclerView.ViewHolder(view){
        var day = view.findViewById<TextView>(R.id.date)
        var maxtemp = view.findViewById<TextView>(R.id.day_temp)
        var con_text = view.findViewById<TextView>(R.id.day_condition)
        var day_icon = view.findViewById<ImageView>(R.id.day_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_day,parent,false))
    }

    override fun getItemCount(): Int {
        return forecasts.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val day = forecasts[position]
        holder.day.text = day.day
        holder.maxtemp.text = day.maxtemp_c
        holder.con_text.text = day.con_text
        holder.day_icon.load(day.day_icon)

        holder.itemView.setOnClickListener {
            dayInterface.dayOnClick(day.day)
        }
    }
    interface DayInterface{
        fun dayOnClick(day:String)
    }
}