package com.fictivestudios.docsvisor.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.adapter.SpinnerDialogSingleSelectAdapter
import com.fictivestudios.docsvisor.callbacks.OnSpinnerItemClickListener
import com.fictivestudios.docsvisor.callbacks.OnSpinnerOKPressedListener
import com.fictivestudios.docsvisor.model.SpinnerModel
import kotlinx.android.synthetic.main.fragment_spinner_popup.*

class SpinnerDialogFragment : androidx.fragment.app.DialogFragment(),
    OnSpinnerItemClickListener, View.OnClickListener {

    private var singleSelectAdapter: SpinnerDialogSingleSelectAdapter? = null
    private var arrData: ArrayList<SpinnerModel> = ArrayList()
    private var arrFilteredData: ArrayList<SpinnerModel> = ArrayList()
    private var onSpinnerOKPressedListener: OnSpinnerOKPressedListener? = null
    private var scrollToPosition: Int = 0
    private var selectedPosition = 0


    // Properties
    var title = ""
    //var buttonText: String = "OK"
    private var dialogHeight = ViewGroup.LayoutParams.MATCH_PARENT
    private var showDescription: Boolean = false
    private var showChoiceImage: Boolean = false


    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            dialogHeight
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NO_TITLE,
            R.style.DialogTheme
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_spinner_popup, container)

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()

        // Set text to OK Button
       // btnOK.text = buttonText

        // Set text of title
        txtTitle.text = title

        // init Adapter
        singleSelectAdapter =
            SpinnerDialogSingleSelectAdapter(
                requireContext(),
                arrFilteredData,
                this,
                showDescription,
                showChoiceImage
            )


        bindView()
    }

    private fun setListeners() {
        btnOK.setOnClickListener(this)
        imgCloseDialog.setOnClickListener(this)
    }

    private fun bindView() {
        val mLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            context,
            androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
            false
        )
        recyclerView.layoutManager = mLayoutManager
       /* (recyclerView.itemAnimator as androidx.recyclerview.widget.DefaultItemAnimator).supportsChangeAnimations =
            false
        val resId =
            R.anim.layout_animation_fall_bottom
        val animation = AnimationUtils.loadLayoutAnimation(context, resId)
        recyclerView.layoutAnimation = animation*/
        recyclerView.adapter = singleSelectAdapter
        scrollToPosition(scrollToPosition)
    }

    fun scrollToPosition(scrollToPosition: Int) {
        if (scrollToPosition > -1) {
            recyclerView.scrollToPosition(scrollToPosition)
        } else {
            recyclerView.scrollToPosition(0)
        }

    }

    fun setFilterData(filterText: String?) {
        if (filterText.isNullOrEmpty()) {
            arrFilteredData.clear()
            arrFilteredData.addAll(arrData)
        } else {
            arrFilteredData.clear()
            arrFilteredData.addAll(arrData.filter {
                it.text.contains(
                    filterText,
                    true
                ) || it.description.contains(filterText, true)
            })
        }
        singleSelectAdapter?.notifyDataSetChanged()

    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imgCloseDialog -> {
                this.dismiss()
            }
            /*else -> {
                if (onSpinnerOKPressedListener != null) {
                    arrData.filter { it.isSelected }.let {
                        if (it.size == 0) {
                            UIHelper.showToast(activity, "Please Select")
                            return
                        } else {
                            onSpinnerOKPressedListener!!.onSingleSelection(
                                it.first(),
                                selectedPosition
                            )
                        }

                    }
                }
                this.dismiss()
            }*/
        }
    }

    override fun onItemClick(
        position: Int,
        anyObject: Any,
        singleSelectAdapter: SpinnerDialogSingleSelectAdapter
    ) {
        selectedPosition = arrData.indexOf(anyObject as SpinnerModel)

        // Set selected from all data
        arrData.forEach { it.isSelected = false }
        arrData[selectedPosition].isSelected = true

        // Set selected in Filtered data
        arrFilteredData.forEach { it.isSelected = false }
        arrFilteredData[position].isSelected = true

        singleSelectAdapter.notifyDataSetChanged()
        arrData.filter { it.isSelected }.let {
          /*  if (it.size == 0) {
                UIHelper.showToast(activity, "Please Select")
                return
            } else {*/
                onSpinnerOKPressedListener!!.onSingleSelection(
                    it.first(),
                    selectedPosition
                )
          //  }

        }
        this.dismiss()
    }

    companion object {
        fun newInstance(
            title: String,
            arrData: ArrayList<SpinnerModel>,
            onSpinnerOKPressedListener: OnSpinnerOKPressedListener,
            scrollToPosition: Int
        ): SpinnerDialogFragment {
            val frag =
                SpinnerDialogFragment()
            val args = Bundle()
            frag.title = title
            frag.arrData.addAll(arrData)
            frag.arrFilteredData.addAll(arrData)
            frag.scrollToPosition = scrollToPosition
            frag.onSpinnerOKPressedListener = onSpinnerOKPressedListener
            frag.arguments = args

            return frag
        }
    }

    /**
     * @param height provide height in Integer
     *
     * Provide value in dp to set height of dialog
     * default value is ViewGroup.LayoutParams.MATCH_PARENT
     * use ViewGroup.LayoutParams.WRAP_CONTENT to choose height as wrap content.
     *
     * If provided height is less than 0, then it will be assigned as MATCH_PARENT.
     *
     * You can use extension #{IntegerExtension.Int.dp} to convert integer value into dp. Example 500.dp
     */
    fun setDialogHeight(height: Int) {
        dialogHeight =
            if (height == ViewGroup.LayoutParams.MATCH_PARENT || height == ViewGroup.LayoutParams.WRAP_CONTENT) {
                height
            } else if (height < 0) {
                ViewGroup.LayoutParams.MATCH_PARENT
            } else {
                height
            }
    }

    /**
     *  Set true if you want to show description, else false.
     *
     *  If true, it will show description which is provided in {@link SpinnerModel#description}
     *
     *  Default value is false
     *
     */
    fun showDescription(showDescription: Boolean) {
        this.showDescription = showDescription
    }


    /**
     *  Set true if you want to show Image for choices, else false.
     *
     *  If true, it will show image which is provided in {@link SpinnerModel#imagePath}. User can also set ImageType.
     *
     *  Default value is false
     *
     */
    fun showImage(showChoiceImage: Boolean) {
        this.showChoiceImage = showChoiceImage
    }


}
