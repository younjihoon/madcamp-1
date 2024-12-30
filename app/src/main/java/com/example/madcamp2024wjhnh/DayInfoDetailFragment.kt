package com.example.madcamp2024wjhnh

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
            displayDayInfoDetails(dayInfo)
        }


    }

    private fun displayDayInfoDetails(dayInfo: DayInfo) {
        binding.detailDayNumberEditText.setText("${dayInfo.number}")
        binding.detailAddressEditText.setText(dayInfo.address.joinToString(", "))
        binding.detailDescriptionEditText.setText(dayInfo.description)
        Log.e("[DIDFragment]", "Photos:${dayInfo.photoList}")
        val imageAdapter = DayInfoDetailImagePagerAdapter(dayInfo.photoList)
        Log.e("[DIDFragment]", "imageAdapter:${imageAdapter}")
        binding.detailViewPager.adapter = imageAdapter
    }

    private fun editButtonOnClick(editing: Boolean, i: Int) {
        Log.e("[DIDFragment]", "editing: $editing, i : $i")
        binding.detailDayNumberEditText.isEnabled = editing
        binding.detailAddressEditText.isEnabled = editing
        binding.detailDescriptionEditText.isEnabled = editing
        if (editing) {
            binding.editButton.text = "완료"
            binding.linearlayout3.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.rounded_background_outline)
            binding.detailAddressEditText.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.rounded_background_outline)
            binding.detailDescriptionEditText.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.rounded_background_outline)
            this.editing = false
        } else {
            binding.editButton.text = "수정"
            binding.linearlayout3.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.textedit_disabled)
            binding.detailAddressEditText.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.textedit_disabled)
            binding.detailDescriptionEditText.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.textedit_disabled)
            this.editing = true
            travel.DayInfos[i] = DayInfo(
                binding.detailDayNumberEditText.text.toString().toInt(),
                binding.detailAddressEditText.text.split(",").toMutableList(),
                binding.detailDescriptionEditText.text.toString(),
                mutableListOf()
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
}
