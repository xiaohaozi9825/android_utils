package pw.xiaohaozi.android_utils.utils;

import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import androidx.databinding.BindingAdapter;

public class BindingUtils {
    @BindingAdapter({"src"})
    public static void setSrc(ImageView view, String url) {
        Log.i("tag", "setSrc: url = " + url);
        Glide.with(view).load(url).into(view);
    }

    @BindingAdapter({"src"})
    public static void setSrc(ImageView view, int res_id) {
        Log.i("tag", "setSrc: res_id = " + res_id);
        Glide.with(view).load(res_id).into(view);
    }

    @BindingAdapter({"circle_src"})
    public static void setCircleSrc(ImageView view, int res_id) {
        Glide.with(view).load(res_id).apply(new RequestOptions().circleCropTransform()).into(view);
    }

    @BindingAdapter({"circle_src"})
    public static void setCircleSrc(ImageView view, String url) {
        Glide.with(view).load(url).apply(new RequestOptions().circleCropTransform()).into(view);
    }

}
