package com.fictivestudios.docsvisor.constants

import com.fictivestudios.docsvisor.model.DummyAdapterModel
import com.fictivestudios.docsvisor.model.SpinnerModel
import java.util.*

object Constants {


    /*    val sampleList: ArrayList<DummyAdapterModel>
            get() {
                val arrayList =
                    ArrayList<DummyAdapterModel>()
                arrayList.add(DummyAdapterModel())
                arrayList.add(DummyAdapterModel())
                arrayList.add(DummyAdapterModel())
                return arrayList
            }*/
    val sampleList: ArrayList<DummyAdapterModel>
        get() {
            val arrayList =
                ArrayList<DummyAdapterModel>()
            arrayList.add(DummyAdapterModel())
            arrayList.add(DummyAdapterModel())
            arrayList.add(DummyAdapterModel())
            arrayList.add(DummyAdapterModel())
            arrayList.add(DummyAdapterModel())
            arrayList.add(DummyAdapterModel())
            arrayList.add(DummyAdapterModel())
            arrayList.add(DummyAdapterModel())
            arrayList.add(DummyAdapterModel())

            return arrayList
        }

    val callList: ArrayList<DummyAdapterModel>
        get() {
            val arrayList =
                ArrayList<DummyAdapterModel>()
            arrayList.add(DummyAdapterModel())
            arrayList.add(DummyAdapterModel())
            return arrayList
        }

    /* val selectorSampleList: ArrayList<SelectorModel>
         get() {
             val arrayList =
                 ArrayList<SelectorModel>()
             arrayList.add(SelectorModel("text", true, 0))
             arrayList.add(SelectorModel("text", false, 0))
             arrayList.add(SelectorModel("text", false, 0))
             arrayList.add(SelectorModel("text", false, 0))
             return arrayList
         }
 */
    val arrChartVertical: ArrayList<String>
        get() {
            val arrayList =
                ArrayList<String>()
            arrayList.add("0")
            arrayList.add("10")
            arrayList.add("20")
            arrayList.add("33")
            arrayList.add("40")
            arrayList.add("50")
            arrayList.add("60")
            arrayList.add("70")
            arrayList.add("80")
            return arrayList
        }

    val arrChartHorizontal: ArrayList<String>
        get() {
            val arrayList =
                ArrayList<String>()
            arrayList.add("Day 1")
            arrayList.add("Day 2")
            arrayList.add("Day 3")
            arrayList.add("Day 4")
            arrayList.add("Day 5")
            arrayList.add("Day 6")
            return arrayList
        }


    val arrTest: ArrayList<SpinnerModel>
        get() {
            val arrayList =
                ArrayList<SpinnerModel>()
            arrayList.add(SpinnerModel("Blood Glucose"))
            arrayList.add(SpinnerModel("Systolic Blood Pressure"))
            arrayList.add(SpinnerModel("Diastolic Blood Pressure"))
            arrayList.add(SpinnerModel("Heart Rate"))
            arrayList.add(SpinnerModel("Temperature"))
            return arrayList
        }


    val arrPart: ArrayList<SpinnerModel>
        get() {
            val arrayList =
                ArrayList<SpinnerModel>()
            arrayList.add(SpinnerModel("Neck"))
            arrayList.add(SpinnerModel("Shoulder"))
            arrayList.add(SpinnerModel("Thigh"))
            return arrayList
        }


    val arrSym: ArrayList<SpinnerModel>
        get() {
            val arrayList =
                ArrayList<SpinnerModel>()
            arrayList.add(SpinnerModel("Audiologist"))
            arrayList.add(SpinnerModel("Epidemiologist"))
            arrayList.add(SpinnerModel("Dentist"))
            return arrayList
        }

    val arrGender: ArrayList<SpinnerModel>
        get() {
            val arrayList =
                ArrayList<SpinnerModel>()
            arrayList.add(SpinnerModel("Male"))
            arrayList.add(SpinnerModel("Female"))
            return arrayList
        }


    val arrDaysss: ArrayList<String>
        get() {
            val arrayList =
                ArrayList<String>()
            arrayList.add("Monday")
            arrayList.add("Tuesday")
            arrayList.add("Wednesday")
            arrayList.add("Thursday")
            arrayList.add("Friday")
            arrayList.add("Saturday")
            arrayList.add("Sunday")
            return arrayList
        }


    val arrHeading: ArrayList<String>
        get() {
            val arrayList =
                ArrayList<String>()
            arrayList.add("Getting Started")
            arrayList.add("Introduction")
            arrayList.add("Basics")
            return arrayList
        }

    val arrSubCourse: ArrayList<String>
        get() {
            val arrayList =
                ArrayList<String>()
            arrayList.add("Develop Annual Goals")
            arrayList.add("Develop Annual Plans")
            arrayList.add("Invest Students")
            arrayList.add("Adjust Plans")
            return arrayList
        }


/*
    fun sampleDataSubCoursesSpinner(): ArrayList<SpinnerModel> {
        val arraySpinnerModel = ArrayList<SpinnerModel>()

        for (i in 1..12) {
            val spinnerModel = SpinnerModel(
                "Sub Course No $i",i
            )
            spinnerModel.id = i
            arraySpinnerModel.add(
                spinnerModel
            )
        }

        return arraySpinnerModel
    }*/


}