package com.fictivestudios.docsvisor.model

class SpinnerModel(var text: String) {

    var isSelected = false
    var positionInList = 0
    var description = ""

    /**
     * Set image path URL, if imagePathResId is not -1, then it will consider it first
     */
    private var imagePathURL = ""


    /**
     * Set image path ResId, if imagePathResId is not -1, then it will consider it first
     */
    private var imagePathResId = -1

    /**
     *  Set Image type as {ImageType.IMAGE_CIRCLE or ImageType.IMAGE_SQUARE}
     */
    // var imageType : ImageType = ImageType.IMAGE_CIRCLE


    /**
     * Provide image path URL to download, or provide File path to load from memory
     */
    fun imagePath(path: String) {
        this.imagePathURL = path
    }

    /**
     * Provide image ResID [R.drawable.icon]
     */
    fun imagePath(path: Int) {
        this.imagePathResId = path
    }

    fun getImagePathURL(): String {
        return this.imagePathURL
    }

    fun getImagePathResId(): Int {
        return this.imagePathResId
    }

    override fun toString(): String {
        return text
    }

    override fun equals(obj: Any?): Boolean {
        return if (obj is SpinnerModel) {
            obj.text == text
        } else super.equals(obj)
    }

}