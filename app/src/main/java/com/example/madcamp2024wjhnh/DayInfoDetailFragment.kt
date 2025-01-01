package com.example.madcamp2024wjhnh

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp2024wjhnh.data.DayInfo
import com.example.madcamp2024wjhnh.data.TravelR
import com.example.madcamp2024wjhnh.databinding.FragmentDayInfoDetailBinding

class DayInfoDetailFragment : Fragment() {

    private var _binding: FragmentDayInfoDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var travelViewModel: TravelViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private var editing = true
    private var travelID = 0
    private var index = 0
    private lateinit var travel: TravelR
    private lateinit var dayInfo: DayInfo
    private lateinit var imageAdapter : DayInfoImageAddAdapter
    private lateinit var imageDisplayAdapter : DayInfoDetailImagePagerAdapter
    private lateinit var imageList : MutableList<Uri>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.e("[DIDFragment]","CREATE")
        _binding = FragmentDayInfoDetailBinding.inflate(inflater, container, false)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        travelViewModel = ViewModelProvider(requireActivity())[TravelViewModel::class.java]


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Arguments for DayInfo and travelID
        val dayInfo = arguments?.getParcelable<DayInfo>("dayInfo")
        if (dayInfo==null) Log.e("[DIDFragment]","Dayinfo is null: $dayInfo")
        else this.dayInfo = dayInfo
        sharedViewModel.travels.observe(viewLifecycleOwner) { travels ->
            Log.e("[DIDFragment]","res: $travels")
            if (!travels.isNullOrEmpty()) {
                Log.e("[DIDFragment]","travels: $travels")
                travel = travels[0]
                if (travel.DayInfos.indexOf(dayInfo)>-1) index = travel.DayInfos.indexOf(dayInfo)
                val editButton: Button = binding.editButton
                editButton.setOnClickListener {
                    Log.e("[DIDFragment]","Button Clicked")
                    editButtonOnClick(editing, index)
                }
            }
        }
        travelID = arguments?.getInt("travelID", 0) ?: 0
        if (dayInfo != null) {
            imageList = dayInfo.photoList
            displayDayInfoDetails(dayInfo)
        }


    }

    private fun displayDayInfoDetails(dayInfo: DayInfo) {
        binding.detailDayNumberEditText.setText("${dayInfo.number}")
        binding.detailAddressEditText.setText(dayInfo.address.joinToString(", "))
        binding.detailDescriptionEditText.setText(dayInfo.description)
        Log.e("[DIDFragment]", "Photos:${imageList}")
        imageDisplayAdapter = DayInfoDetailImagePagerAdapter(imageList)
        Log.e("[DIDFragment]", "imageAdapter:${imageDisplayAdapter}")
        binding.detailViewPager.adapter = imageDisplayAdapter
    }

    private fun editButtonOnClick(editing: Boolean, i: Int) {
        Log.e("[DIDFragment]", "editing: $editing, i : $i")
        binding.detailDayNumberEditText.isEnabled = editing
        binding.detailAddressEditText.isEnabled = editing
        binding.detailDescriptionEditText.isEnabled = editing


        imageAdapter = DayInfoImageAddAdapter(imageList) {
            openImagePicker()
        }
        val dialogView = LayoutInflater.from(context).inflate(R.layout.item_day_info_image_selector, null)
        val imageRecyclerView = dialogView.findViewById<RecyclerView>(R.id.addImagesRecyclerView)
        imageRecyclerView.layoutManager = GridLayoutManager(requireActivity(), 3) // 3열 Grid
        imageRecyclerView.adapter = imageAdapter
        binding.btnViewimageedit.setOnClickListener {
            AlertDialog.Builder(requireActivity())
                .setView(dialogView)
                .setPositiveButton("확인") { dialog, _ ->
                    dialog.dismiss()
                }
                .setNegativeButton("취소") { dialog, _ ->
                    imageList = dayInfo.photoList
                    dialog.dismiss()
                }
                .create()
                .show()
        }
        if (editing) {
            binding.editButton.text = "완료"
            binding.btnViewimageedit.visibility = View.VISIBLE
            binding.linearlayout3.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.rounded_background_outline)
            binding.detailAddressEditText.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.rounded_background_outline)
            binding.detailDescriptionEditText.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.rounded_background_outline)
            this.editing = false

        } else {

            binding.editButton.text = "수정"
            binding.btnViewimageedit.visibility = View.INVISIBLE
            binding.linearlayout3.background = null
            binding.detailAddressEditText.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.textedit_disabled)
            binding.detailDescriptionEditText.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.textedit_disabled)
            this.editing = true
            travel.DayInfos[i] = DayInfo(
                binding.detailDayNumberEditText.text.toString().toInt(),
                binding.detailAddressEditText.text.split(",").toMutableList(),
                binding.detailDescriptionEditText.text.toString(),
                imageList
            )
            travelViewModel.updateById(travelID, travel.DayInfos) {}
            sharedViewModel.updateTravel(0,travel)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(dayInfo: DayInfo, travelID: Int): DayInfoDetailFragment {
            val fragment = DayInfoDetailFragment()
            val args = Bundle()
            args.putParcelable("dayInfo", dayInfo)
            args.putInt("travelID", travelID)
            fragment.arguments = args
            return fragment
        }
    }
    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true) // 다중 선택 가능
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*")
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val clipData = data?.clipData
            Log.e("[DayInfoActivity]Error","No image : $clipData")
            if (clipData != null) {
                // 다중 선택
                for (i in 0 until clipData.itemCount) {
                    val imageUri = clipData.getItemAt(i).uri
                    imageList.add(imageUri)
                }
                imageAdapter.notifyItemRangeInserted(imageList.size - clipData.itemCount,clipData.itemCount)
                imageDisplayAdapter.notifyItemRangeInserted(imageList.size - clipData.itemCount,clipData.itemCount)

            } else {
                // 단일 선택
                val imageUri = data?.data
                imageUri?.let {
                    imageList.add(it)
                    imageAdapter.notifyItemInserted(imageList.size - 1)
                    imageDisplayAdapter.notifyItemInserted(imageList.size - 1)

                }
            }

        }
    }
}
