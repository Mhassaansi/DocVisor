package com.fictivestudios.docsvisor.libraries;

import android.widget.ImageView;

import com.fictivestudios.docsvisor.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ImageLoaderHelper {


    /**
     * WITHOUT ANIMATION, WITHOUT HEADER
     *
     * @param imageView
     * @param url
     */

    public static void loadImageWithoutAnimation(ImageView imageView, String url, boolean isUser) {
        ImageLoader.getInstance().displayImage("http://server.appsstaging.com:3003" + url,
                imageView,
                ImageLoaderHelper.getOptionsSimple(isUser));
    }


    /*
     * WITH ANIMATION, WITHOUT HEADER
     *
     * @param imageView
     * @param url
     * @param isUser
     *//*

    public static void loadImageWithAnimations(ImageView imageView, String url, boolean isUser) {
        ImageLoader.getInstance().displayImage(url,
                imageView,
                ImageLoaderHelper.getOptionsWithAnimation(isUser));
    }


     * WITHOUT ANIMATION, WITHOUT HEADER, will create URL from path
     *
     * @param imageView
     * @param path
     *//*

    public static void loadImageWithouAnimationByPath(ImageView imageView, String path, boolean isUser) {
        if (!StringHelper.isNullOrEmpty(path) && path.startsWith("https")) {
            loadImageWithAnimations(imageView, path, isUser);
            return;
        }
        ImageLoader.getInstance().displayImage(getImageURLFromPath(path),
                imageView,
                ImageLoaderHelper.getOptionsSimple(isUser));
    }*/

    /*

     * WITH ANIMATION, WITHOUT HEADER,  will create URL from path
     *
     * @param imageView
     * @param path
     * @param isUser
     *//*

    public static void loadImageWithAnimationsByPath(ImageView imageView, String path, boolean isUser) {
        if (!StringHelper.isNullOrEmpty(path) && path.startsWith("https")) {
            loadImageWithAnimations(imageView, path, isUser);
            return;
        }

        ImageLoader.getInstance().displayImage(getImageURLFromPath(path),
                imageView,
                ImageLoaderHelper.getOptionsWithAnimation(isUser));
    }


    public static void loadBase64Image(ImageView imageView, String base64) {
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        if (decodedByte != null) {
            imageView.setImageBitmap(decodedByte);
        } else {
            imageView.setImageResource(R.drawable.placeholderimg);
        }
    }*/

/*

    public static String getImageURLFromPath(String path) {
        return WebServiceConstants.IMAGE_BASE_URL + path;
    }
*/


   /* public static String getImageURLFromPath(String path, String width, String height) {
        return WebServiceConstants.IMAGE_BASE_URL + path + "?w=" + width + "&h=" + height;
    }*/

    public static DisplayImageOptions getOptionsSimple(boolean isUser) {

        if (isUser) {
            return new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                    .showImageForEmptyUri(R.color.heading)
                    .showImageOnFail(R.drawable.placeholderimg)
                    .showImageOnLoading(R.drawable.placeholderimg)
                    .build();
        } else {
            return new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                    .showImageForEmptyUri(R.color.heading)
                    .showImageOnFail(R.color.heading)
                    .showImageOnLoading(R.color.heading)
                    .build();
        }


    }
/*

    public static DisplayImageOptions getOptionsSimple(Map<String, String> headers, boolean isUser) {

        if (isUser) {
            return new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                    .showImageForEmptyUri(R.color.base_dark_gray)
                    .showImageOnFail(R.drawable.placeholderimg)
                    .showImageOnLoading(R.drawable.placeholderimg)
                    .extraForDownloader(headers)
                    .build();
        } else {
            return new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                    .showImageForEmptyUri(R.color.base_dark_gray)
                    .showImageOnFail(R.color.background_gray)
                    .showImageOnLoading(R.color.background_gray)
                    .extraForDownloader(headers)
                    .build();
        }


    }

    public static DisplayImageOptions getOptionsWithAnimation(boolean isUser) {

        if (isUser) {
            return new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                    .showImageForEmptyUri(R.color.base_dark_gray)
                    .showImageOnFail(R.drawable.placeholderimg)
                    .showImageOnLoading(R.drawable.placeholderimg)
                    .imageScaleType(ImageScaleType.EXACTLY).displayer(new FadeInBitmapDisplayer(200)).build();
        } else {
            return new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                    .showImageForEmptyUri(R.color.base_dark_gray)
                    .showImageOnFail(R.color.background_gray)
                    .showImageOnLoading(R.color.background_gray)
                    .imageScaleType(ImageScaleType.EXACTLY).displayer(new FadeInBitmapDisplayer(200)).build();
        }
    }


    public static DisplayImageOptions getOptionsWithAnimation(Map<String, String> headers, boolean isUser) {

        if (isUser) {
            return new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                    .showImageForEmptyUri(R.color.base_dark_gray)
                    .showImageOnFail(R.drawable.placeholderimg)
                    .showImageOnLoading(R.drawable.placeholderimg)
                    .extraForDownloader(headers)
                    .imageScaleType(ImageScaleType.EXACTLY).displayer(new FadeInBitmapDisplayer(200)).build();
        } else {
            return new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                    .showImageForEmptyUri(R.color.base_dark_gray)
                    .showImageOnFail(R.color.background_gray)
                    .showImageOnLoading(R.color.background_gray)
                    .extraForDownloader(headers)
                    .imageScaleType(ImageScaleType.EXACTLY).displayer(new FadeInBitmapDisplayer(200)).build();
        }
    }*/
}
