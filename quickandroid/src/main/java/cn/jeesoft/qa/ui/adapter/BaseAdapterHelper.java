/**
 * Copyright 2013 Joan Zapata
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.jeesoft.qa.ui.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import cn.jeesoft.qa.QACore;
import cn.jeesoft.qa.error.QAAdapterException;
import cn.jeesoft.qa.error.QAException;

/**
 * Allows an abstraction of the ViewHolder pattern.<br>
 * <br>
 * <p/>
 * <b>Usage</b>
 * <p/>
 * <pre>
 * return BaseAdapterHelper.get(context, convertView, parent, R.layout.item)
 *         .setText(R.id.tvName, contact.getName())
 *         .setText(R.id.tvEmails, contact.getEmails().toString())
 *         .setText(R.id.tvNumbers, contact.getNumbers().toString())
 *         .getView();
 * </pre>
 */
@SuppressWarnings("rawtypes")
class BaseAdapterHelper<H extends BaseAdapterHelper> {

    /** Views indexed with their IDs */
    private final SparseArray<View> views;

    private final Context context;

    int position;

    private View convertView;

    /** Package private field to retain the associated user object and detect a change */
    Object associatedObject;

    BaseAdapterHelper(Context context, ViewGroup parent, int layoutId, int position) {
        this.context = context;
        this.position = position;
        this.views = new SparseArray<View>();
        convertView = LayoutInflater.from(context) //
                .inflate(layoutId, parent, false);
        convertView.setTag(this);
    }

    /**
     * This method allows you to retrieve a view and perform custom
     * operations on it, not covered by the BaseAdapterHelper.<br/>
     * If you think it's a common use case, please consider creating
     * a new issue at https://github.com/JoanZapata/base-adapter-helper/issues.
     * @param viewId The id of the view you want to retrieve.
     */
    public <T extends View> T getView(int viewId) {
        return retrieveView(viewId);
    }

    /**
     * Will set the text of a TextView.
     * @param viewId The view id.
     * @param value  The text to put in the text view.
     * @return The BaseAdapterHelper for chaining.
     */
    @SuppressWarnings("unchecked")
    public H setText(int viewId, CharSequence value) {
        TextView view = retrieveView(viewId);
        view.setText(value);
        return (H) this;
    }

    public H appendText(int viewId, CharSequence value) {
        TextView view = retrieveView(viewId);
        view.append(value);
        return (H) this;
    }

    /**
     * Will set the image of an ImageView from a resource id.
     * @param viewId     The view id.
     * @param imageResId The image resource id.
     * @return The BaseAdapterHelper for chaining.
     */
    @SuppressWarnings("unchecked")
    public H setImageResource(int viewId, int imageResId) {
        ImageView view = retrieveView(viewId);
        view.setImageResource(imageResId);
        return (H) this;
    }

    /**
     * Will set background color of a view.
     * @param viewId The view id.
     * @param color  A color, not a resource id.
     * @return The BaseAdapterHelper for chaining.
     */
    @SuppressWarnings("unchecked")
    public H setBackgroundColor(int viewId, int color) {
        View view = retrieveView(viewId);
        view.setBackgroundColor(color);
        return (H) this;
    }

    /**
     * Will set background of a view.
     * @param viewId        The view id.
     * @param backgroundRes A resource to use as a background.
     * @return The BaseAdapterHelper for chaining.
     */
    @SuppressWarnings("unchecked")
    public H setBackgroundRes(int viewId, int backgroundRes) {
        View view = retrieveView(viewId);
        view.setBackgroundResource(backgroundRes);
        return (H) this;
    }

    /**
     * Will set text color of a TextView.
     * @param viewId    The view id.
     * @param textColor The text color (not a resource id).
     * @return The BaseAdapterHelper for chaining.
     */
    @SuppressWarnings("unchecked")
    public H setTextColor(int viewId, int textColor) {
        TextView view = retrieveView(viewId);
        view.setTextColor(textColor);
        return (H) this;
    }

    /**
     * Will set text color of a TextView.
     * @param viewId       The view id.
     * @param textColorRes The text color resource id.
     * @return The BaseAdapterHelper for chaining.
     */
    @SuppressWarnings("unchecked")
    public H setTextColorRes(int viewId, int textColorRes) {
        TextView view = retrieveView(viewId);
        view.setTextColor(context.getResources().getColor(textColorRes));
        return (H) this;
    }

    /**
     * Will set the image of an ImageView from a drawable.
     * @param viewId   The view id.
     * @param drawable The image drawable.
     * @return The BaseAdapterHelper for chaining.
     */
    @SuppressWarnings("unchecked")
    public H setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = retrieveView(viewId);
        view.setImageDrawable(drawable);
        return (H) this;
    }

    /**
     * Will download an image from a URL and put it in an ImageView.<br/>
     * It uses Square's Picasso library to download the image asynchronously and put the result into the ImageView.<br/>
     * Picasso manages recycling of views in a ListView.<br/>
     * If you need more control over the Picasso settings, use {BaseAdapterHelper#setImageBuilder}.
     * @param viewId   The view id.
     * @param imageUrl The image URL.
     * @return The BaseAdapterHelper for chaining.
     */
    @SuppressWarnings("unchecked")
    public H setImageUrl(int viewId, String imageUrl) {
        View view = retrieveView(viewId);
        QACore.getImage().load(view, imageUrl);
        return (H) this;
    }

    /** Add an action to set the image of an image view. Can be called multiple times. */
    @SuppressWarnings("unchecked")
    public H setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = retrieveView(viewId);
        view.setImageBitmap(bitmap);
        return (H) this;
    }

    /**
     * Add an action to set the alpha of a view. Can be called multiple times.
     * Alpha between 0-1.
     */
    @SuppressWarnings("unchecked")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public H setAlpha(int viewId, float value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            retrieveView(viewId).setAlpha(value);
        } else {
            // Pre-honeycomb hack to set Alpha value
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            retrieveView(viewId).startAnimation(alpha);
        }
        return (H) this;
    }

    /**
     * Set a view visibility to VISIBLE (true) or GONE (false).
     * @param viewId  The view id.
     * @param visible True for VISIBLE, false for GONE.
     * @return The BaseAdapterHelper for chaining.
     */
    @SuppressWarnings("unchecked")
    public H setVisible(int viewId, boolean visible) {
        View view = retrieveView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return (H) this;
    }

    /**
     * Add links into a TextView.
     * @param viewId The id of the TextView to linkify.
     * @return The BaseAdapterHelper for chaining.
     */
    @SuppressWarnings("unchecked")
    public H linkify(int viewId) {
        TextView view = retrieveView(viewId);
        Linkify.addLinks(view, Linkify.ALL);
        return (H) this;
    }

    /** Apply the typeface to the given viewId, and enable subpixel rendering. */
    @SuppressWarnings("unchecked")
    public H setTypeface(int viewId, Typeface typeface) {
        TextView view = retrieveView(viewId);
        view.setTypeface(typeface);
        view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        return (H) this;
    }

    /** Apply the typeface to all the given viewIds, and enable subpixel rendering. */
    @SuppressWarnings("unchecked")
    public H setTypeface(Typeface typeface, int... viewIds) {
        for (int viewId : viewIds) {
            TextView view = retrieveView(viewId);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        return (H) this;
    }

    /**
     * Sets the progress of a ProgressBar.
     * @param viewId   The view id.
     * @param progress The progress.
     * @return The BaseAdapterHelper for chaining.
     */
    @SuppressWarnings("unchecked")
    public H setProgress(int viewId, int progress) {
        ProgressBar view = retrieveView(viewId);
        view.setProgress(progress);
        return (H) this;
    }

    /**
     * Sets the progress and max of a ProgressBar.
     * @param viewId   The view id.
     * @param progress The progress.
     * @param max      The max value of a ProgressBar.
     * @return The BaseAdapterHelper for chaining.
     */
    @SuppressWarnings("unchecked")
    public H setProgress(int viewId, int progress, int max) {
        ProgressBar view = retrieveView(viewId);
        view.setMax(max);
        view.setProgress(progress);
        return (H) this;
    }

    /**
     * Sets the range of a ProgressBar to 0...max.
     * @param viewId The view id.
     * @param max    The max value of a ProgressBar.
     * @return The BaseAdapterHelper for chaining.
     */
    @SuppressWarnings("unchecked")
    public H setMax(int viewId, int max) {
        ProgressBar view = retrieveView(viewId);
        view.setMax(max);
        return (H) this;
    }

    /**
     * Sets the rating (the number of stars filled) of a RatingBar.
     * @param viewId The view id.
     * @param rating The rating.
     * @return The BaseAdapterHelper for chaining.
     */
    @SuppressWarnings("unchecked")
    public H setRating(int viewId, float rating) {
        RatingBar view = retrieveView(viewId);
        view.setRating(rating);
        return (H) this;
    }

    /**
     * Sets the rating (the number of stars filled) and max of a RatingBar.
     * @param viewId The view id.
     * @param rating The rating.
     * @param max    The range of the RatingBar to 0...max.
     * @return The BaseAdapterHelper for chaining.
     */
    @SuppressWarnings("unchecked")
    public H setRating(int viewId, float rating, int max) {
        RatingBar view = retrieveView(viewId);
        view.setMax(max);
        view.setRating(rating);
        return (H) this;
    }

    /**
     * Sets the on click listener of the view.
     * @param viewId   The view id.
     * @param listener The on click listener;
     * @return The BaseAdapterHelper for chaining.
     */
    @SuppressWarnings("unchecked")
    public H setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = retrieveView(viewId);
        view.setOnClickListener(listener);
        return (H) this;
    }

    /**
     * Sets the on touch listener of the view.
     * @param viewId   The view id.
     * @param listener The on touch listener;
     * @return The BaseAdapterHelper for chaining.
     */
    @SuppressWarnings("unchecked")
    public H setOnTouchListener(int viewId, View.OnTouchListener listener) {
        View view = retrieveView(viewId);
        view.setOnTouchListener(listener);
        return (H) this;
    }

    /**
     * Sets the on long click listener of the view.
     * @param viewId   The view id.
     * @param listener The on long click listener;
     * @return The BaseAdapterHelper for chaining.
     */
    @SuppressWarnings("unchecked")
    public H setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        View view = retrieveView(viewId);
        view.setOnLongClickListener(listener);
        return (H) this;
    }

    /**
     * Sets the tag of the view.
     * @param viewId The view id.
     * @param tag    The tag;
     * @return The BaseAdapterHelper for chaining.
     */
    @SuppressWarnings("unchecked")
    public H setTag(int viewId, Object tag) {
        View view = retrieveView(viewId);
        view.setTag(tag);
        return (H) this;
    }

    /**
     * Sets the tag of the view.
     * @param viewId The view id.
     * @param key    The key of tag;
     * @param tag    The tag;
     * @return The BaseAdapterHelper for chaining.
     */
    @SuppressWarnings("unchecked")
    public H setTag(int viewId, int key, Object tag) {
        View view = retrieveView(viewId);
        view.setTag(key, tag);
        return (H) this;
    }

    /**
     * Sets the checked status of a checkable.
     * @param viewId  The view id.
     * @param checked The checked status;
     * @return The BaseAdapterHelper for chaining.
     */
    @SuppressWarnings("unchecked")
    public H setChecked(int viewId, boolean checked) {
        Checkable view = (Checkable) retrieveView(viewId);
        view.setChecked(checked);
        return (H) this;
    }

    /**
     * Sets the adapter of a adapter view.
     * @param viewId  The view id.
     * @param adapter The adapter;
     * @return The BaseAdapterHelper for chaining.
     */
    @SuppressWarnings("unchecked")
    public H setAdapter(int viewId, Adapter adapter) {
        AdapterView<Adapter> view = retrieveView(viewId);
        view.setAdapter(adapter);
        return (H) this;
    }

    /** Retrieve the convertView */
    public View getView() {
        return convertView;
    }

    /**
     * Retrieve the overall position of the data in the list.
     * @throws IllegalArgumentException If the position hasn't been set at the construction of the this helper.
     */
    public int getPosition() {
        if (position == -1)
            throw new QAAdapterException(QAException.CODE_ILLEGAL_STATE,
                    "Use AdapterHelper constructor with position if you need to retrieve the position.");
        return position;
    }

    @SuppressWarnings("unchecked")
    private <T extends View> T retrieveView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    /** Retrieves the last converted object on this view. */
    public Object getAssociatedObject() {
        return associatedObject;
    }

    /** Should be called during convert */
    public void setAssociatedObject(Object associatedObject) {
        this.associatedObject = associatedObject;
    }
}
