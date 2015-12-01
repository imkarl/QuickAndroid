package cn.jeesoft.qa.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Adapter操作辅助类
 * @version v0.1.0 king 2014-12-26 Adapter操作辅助类拓展
 */
public class QAAdapterHelper extends BaseAdapterHelper<QAAdapterHelper> {

    private QAAdapterHelper(Context context, ViewGroup parent, int layoutId, int position) {
        super(context, parent, layoutId, position);
    }

    /**
     * This method is the only entry point to get a BaseAdapterHelper.
     * @param context     The current context.
     * @param convertView The convertView arg passed to the getView() method.
     * @param parent      The parent arg passed to the getView() method.
     * @return A BaseAdapterHelper instance.
     */
    public static QAAdapterHelper get(Context context, View convertView, ViewGroup parent, int layoutId) {
        return get(context, convertView, parent, layoutId, -1);
    }

    /** This method is package private and should only be used by QuickAdapter. */
    static QAAdapterHelper get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new QAAdapterHelper(context, parent, layoutId, position);
        }

        // Retrieve the existing helper and update its position
        QAAdapterHelper existingHelper = (QAAdapterHelper) convertView.getTag();
        existingHelper.position = position;
        return existingHelper;
    }
    
}
