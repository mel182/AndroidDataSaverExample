package com.example.datasaverexampleapp.camera.controlling_the_camera

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.datasaverexampleapp.type_alias.Layout
import com.example.datasaverexampleapp.type_alias.ViewByID

class CameraListAdapter(private val dataList:ArrayList<CameraInfoListItem>, private val callback:(CameraInfoListItem) -> Unit) : RecyclerView.Adapter<CameraListAdapter.CameraListItemViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CameraListItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(Layout.item_camera_list_item,parent,false)
        return CameraListItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: CameraListItemViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    inner class CameraListItemViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        private val mainView = view.findViewById<ConstraintLayout>(ViewByID.main_view)

        private val canDisableShutterSoundTitleTextView = view.findViewById<TextView>(ViewByID.can_disable_shutter_title)

        private val cameraFacingTextView = view.findViewById<TextView>(ViewByID.camera_facing_textview)
        private val cameraOrientationTextView = view.findViewById<TextView>(ViewByID.camera_orientation_textview)
        private val canDisableShutterSoundTextView = view.findViewById<TextView>(ViewByID.disable_shutter_sound_textview)
        private val scalerStreamConfigMapTextView = view.findViewById<TextView>(ViewByID.scaler_stream_config_map_textview)
        private val scalerStreamMapTextView = view.findViewById<TextView>(ViewByID.scaler_stream_map_textview)

        fun bind(item:CameraInfoListItem)
        {
            cameraFacingTextView?.text = when(item.facing){
                Facing.FRONT -> "Front"
                Facing.BACK -> "Back"
                Facing.EXTERNAL -> "External"
            }

            cameraOrientationTextView?.text = if (item.orientation != -1) "${item.orientation} °" else "-"

            item.canDisableShutterSound?.let {
                canDisableShutterSoundTextView?.apply {
                    visibility = View.VISIBLE
                    text = if (it) "yes" else "no"
                }
                canDisableShutterSoundTitleTextView?.visibility = View.VISIBLE
            }?: kotlin.run {
                canDisableShutterSoundTextView?.visibility = View.GONE
                canDisableShutterSoundTitleTextView?.visibility = View.GONE
            }

            scalerStreamConfigMapTextView?.apply {
                text = if (item.controlAutoFocusModes.isNotEmpty()) "available" else "not available"
            }

            item.scalerStreamMap?.let {
                scalerStreamMapTextView?.text = "available"
            }?: kotlin.run {
                scalerStreamMapTextView?.text = "not available"
            }

            mainView.setOnClickListener {
                callback(item)
            }
        }
    }
}