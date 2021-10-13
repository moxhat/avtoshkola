package com.madcrew.pravamobil.view.fragment.practiceoptions.lessonhistory.openlesson

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar.OnRatingBarChangeListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.madcrew.pravamobil.R
import com.madcrew.pravamobil.databinding.FragmentOpenLessonBinding
import com.madcrew.pravamobil.utils.alphaDown
import com.madcrew.pravamobil.utils.alphaUp
import com.madcrew.pravamobil.utils.setGone
import com.madcrew.pravamobil.utils.setVisible
import com.madcrew.pravamobil.view.dialog.InstructorCancelDialogFragment


class OpenLessonFragment(var status: Int = 0) : Fragment() {

    private var _binding: FragmentOpenLessonBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOpenLessonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rateSheet = binding.openLessonRatingSheet
        val btRateClose = binding.btOpenLessonCloseRating
        val ratingSheetBar = binding.openLessonRatingSheetRatingBar
        btRateClose.setGone()
        val mBottomRateSheet = BottomSheetBehavior.from(rateSheet)


        mBottomRateSheet.state = BottomSheetBehavior.STATE_COLLAPSED
        mBottomRateSheet.isHideable = true

        hideAll()

        Glide.with(requireContext()).load(R.drawable.ic_man).circleCrop()
            .into(binding.openLessonInstructorAvatar)

        if (status > 1) {
            binding.openLessonRatingBar.rating = status.toFloat()
            binding.openLessonRateComment.text = "Вот такой вот Питт водитель!"
        }

        when (status) {
            0 -> setCancel()
            1 -> setUnrated()
            else -> setRated()
        }

        binding.btOpenLessonBack.setOnClickListener {
            val mainManager = parentFragmentManager
            val transaction: FragmentTransaction = mainManager.beginTransaction()
            transaction.remove(this)
            transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
            transaction.commit()
        }

        binding.openLessonMainConstraint.setOnClickListener {

        }

        binding.btOpenLessonRateLesson.setOnClickListener {
            btRateClose.alphaUp(100)
            btRateClose.setVisible()
            mBottomRateSheet.state = BottomSheetBehavior.STATE_EXPANDED
        }

        btRateClose.setOnClickListener {
            btRateClose.alphaDown(100)
            btRateClose.setGone()
            mBottomRateSheet.state = BottomSheetBehavior.STATE_COLLAPSED
        }



        binding.openLessonRatingContentConstraint.setOnClickListener {

        }

        ratingSheetBar.onRatingBarChangeListener =
            OnRatingBarChangeListener { ratingBar, rating, fromUser ->
                when (rating) {
                    0f -> hideAll()
                    5f -> showGood()
                    else -> showBad()
                }
            }

        binding.btRateSheetDone.setOnClickListener {
            btRateClose.alphaDown(100)
            btRateClose.setGone()
            setRated()
            binding.openLessonRatingBar.rating = binding.openLessonRatingSheetRatingBar.rating
            mBottomRateSheet.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.btRateSheetAddCallback.setOnClickListener {
            val callBackDialog = InstructorCancelDialogFragment("callback")
            callBackDialog.show(childFragmentManager, "InstructorCancelDialogFragment")
        }
    }

    private fun setCancel() {
        binding.openLessonLessonCancelTitle.setVisible()
        binding.btOpenLessonLessonCancel.setVisible()
        binding.openLessonRateTitle.setGone()
        binding.btOpenLessonRateLesson.setGone()
        binding.openLessonInstructorRatingTitle.setGone()
        binding.openLessonInstructorRatingText.setGone()
        binding.openLessonRatingBar.setGone()
        binding.openLessonRateCommentTitle.setGone()
        binding.openLessonRateComment.setGone()
    }

    private fun setUnrated() {
        binding.openLessonLessonCancelTitle.setGone()
        binding.btOpenLessonLessonCancel.setGone()
        binding.openLessonRateTitle.setVisible()
        binding.btOpenLessonRateLesson.setVisible()
        binding.btOpenLessonRateLesson.alpha = 1f
        binding.btOpenLessonRateLesson.isEnabled = true
        binding.openLessonInstructorRatingTitle.setVisible()
        binding.openLessonInstructorRatingText.setVisible()
        binding.openLessonRatingBar.setGone()
        binding.openLessonRateCommentTitle.setVisible()
        binding.openLessonRateComment.setVisible()
    }

    private fun setRated() {
        binding.openLessonLessonCancelTitle.setGone()
        binding.btOpenLessonLessonCancel.setGone()
        binding.openLessonRateTitle.setVisible()
        binding.btOpenLessonRateLesson.setVisible()
        binding.btOpenLessonRateLesson.alpha = 0.5f
        binding.btOpenLessonRateLesson.isEnabled = false
        binding.openLessonInstructorRatingTitle.setVisible()
        binding.openLessonInstructorRatingText.setGone()
        binding.openLessonRatingBar.setVisible()
        binding.openLessonRateCommentTitle.setVisible()
        binding.openLessonRateComment.setVisible()
    }

    private fun showGood() {
        binding.rateSheetRatingGood.setVisible()
        binding.rateSheetRatingBad.setGone()
    }

    private fun showBad() {
        binding.rateSheetRatingGood.setGone()
        binding.rateSheetRatingBad.setVisible()
    }

    private fun hideAll() {
        binding.rateSheetRatingGood.setGone()
        binding.rateSheetRatingBad.setGone()
    }

    fun setCommentText(text: String){
        binding.openLessonRateComment.text = text
    }

}