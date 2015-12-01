package cn.jeesoft.qa.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * 辅助快速开发的Adapter
 * @version v0.1.0 king 2014-12-26 快速开发的Adapter
 * @param <T>
 */
public abstract class QAAdapter<T> extends BaseQuickAdapter<T, QAAdapterHelper> {

    public QAAdapter(Context context, int layoutResId, List<T> data) {
        super(context, layoutResId, data);
    }
    
    @Override
    protected final QAAdapterHelper getAdapterHelper(int position,
            View convertView, ViewGroup parent) {
        return QAAdapterHelper.get(context, convertView, parent, layoutResId, position);
    }

}
