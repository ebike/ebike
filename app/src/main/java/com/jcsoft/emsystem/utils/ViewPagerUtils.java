package com.jcsoft.emsystem.utils;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcsoft.emsystem.R;
import com.jcsoft.emsystem.base.TabIndicator;

import java.util.ArrayList;
import java.util.List;


/**
 * ViewPager工具类, 用于提供ViewPager相关的公用方法
 */
public class ViewPagerUtils {

    // 获取ViewPager的TabIndicator列表
    public static List<TabIndicator> getTabIndicator(Integer number) {
        List<TabIndicator> list = new ArrayList<TabIndicator>();
        for (int i = 0; i < number; i++) {
            TabIndicator indicator = new TabIndicator();
            indicator.type = i;
            list.add(indicator);
        }
        return list;
    }

    /**
     * 变换底部选项卡图标和字体颜色
     *
     * @param context
     * @param index
     * @param textViews
     */
    public static void setBottomBar(Context context, int index, List<TextView> textViews, List<ImageView> imageViews) {
        for (int i = 0; i < textViews.size(); i++) {
            TextView textView = textViews.get(i);
            ImageView imageView = imageViews.get(i);
            if (index == i) {
                textView.setTextColor(context.getResources().getColor(R.color.bottombar_text_selected));
                switch (i) {
                    case 0:
                        imageView.setImageResource(R.mipmap.location_ico_hover);
                        break;
                    case 1:
                        imageView.setImageResource(R.mipmap.alarm_ico_hover);
                        break;
                    case 2:
                        imageView.setImageResource(R.mipmap.chart_ico_hover);
                        break;
                    case 3:
                        imageView.setImageResource(R.mipmap.my_ico_hover);
                        break;
                }
            } else {
                textView.setTextColor(context.getResources().getColor(R.color.bottombar_text_default));
                switch (i) {
                    case 0:
                        imageView.setImageResource(R.mipmap.location_ico);
                        break;
                    case 1:
                        imageView.setImageResource(R.mipmap.alarm_ico);
                        break;
                    case 2:
                        imageView.setImageResource(R.mipmap.char_ico);
                        break;
                    case 3:
                        imageView.setImageResource(R.mipmap.my_ico);
                        break;
                }
            }
        }
    }

}
