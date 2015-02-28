package cn.jeesoft.qa.simple.adapter;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;
import android.widget.TextView;
import cn.jeesoft.qa.QACore;
import cn.jeesoft.qa.ui.adapter.QAAdapter;
import cn.jeesoft.qa.ui.adapter.QAAdapterHelper;

/**
 * 极简Adapter开发
 */
public class AdapterActivity extends ActionBarActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QACore.getManager().addActivity(this);
        
        // 创建列表布局
        ListView lvContent = new ListView(this);
        setContentView(lvContent);
        
        // 设置数据适配器
        lvContent.setAdapter(new QAAdapter<Entrys>(this,
                android.R.layout.simple_list_item_1,
                Entrys.SIMPLE_DATA) {
            @Override
            protected void convert(QAAdapterHelper helper, Entrys item) {
                // 这里初始化列表项Item
                helper.setText(android.R.id.text1, item.getName());
                helper.setTextColor(android.R.id.text1, Color.BLACK);
                
                // 根据索引特殊处理
                TextView text1 = helper.getView(android.R.id.text1);
                int position = helper.getPosition();
                
                if (position == 2) {
                    text1.setTextColor(Color.BLUE);
                } else if (position == 4) {
                    text1.setTextColor(Color.RED);
                    text1.setText("pos:"+helper.getPosition());
                }
            }
        });
        
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        QACore.getManager().finishActivity(this);
    }
    
}
