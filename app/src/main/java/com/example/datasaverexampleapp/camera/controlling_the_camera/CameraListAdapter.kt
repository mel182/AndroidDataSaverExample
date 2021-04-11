package com.example.datasaverexampleapp.camera.controlling_the_camera

import android.hardware.Camera
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.datasaverexampleapp.type_alias.ViewByID

class CameraListAdapter : RecyclerView.Adapter<CameraListAdapter.CameraListItemViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CameraListItemViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: CameraListItemViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    inner class CameraListItemViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        private val cameraFacingTextView = view.findViewById<TextView>(ViewByID.camera_facing_textview)
        private val cameraOrientationTextView = view.findViewById<TextView>(ViewByID.camera_orientation_textview)

        fun bind(item:Any)
        {
            if (item is Camera.CameraInfo)
            {
                cameraFacingTextView?.text = if (item.facing == Camera.CameraInfo.CAMERA_FACING_BACK) "Back" else "front"
                cameraOrientationTextView?.text = "${item.orientation} °"
            } else if (item is String)
            {

            }
        }
    }
}