package net.lcadsl.qintalker.factory.presenter;

import android.support.annotation.StringRes;

import net.lcadsl.qintalker.common.widget.recycler.RecyclerAdapter;


/**
 * MVP模式中公共的基本契约
 *
 *
 * @version 1.0.0
 */
public interface BaseContract {
    //基本的界面职责
    interface View<T extends Presenter> {
        // 公共的：显示一个字符串错误
        void showError(@StringRes int str);

        // 公共的：显示进度条
        void showLoading();

        // 支持设置一个Presenter
        void setPresenter(T presenter);
    }

    //基本的Presenter职责
    interface Presenter {
        // 共用的开始触发
        void start();

        // 共用的销毁触发
        void destroy();
    }

    //基本的列表的View的职责
    interface RecyclerView<T extends Presenter,ViewMode> extends View<T>{
        //拿到适配器，然后自主进行刷新
        RecyclerAdapter<ViewMode> getRecyclerAdapter();

        //当数据更改了的时候触发
        void onAdapterDataChanged();
    }
}