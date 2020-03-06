package net.lcadsl.qintalker.factory.presenter.search;

import net.lcadsl.qintalker.factory.presenter.BasePresenter;


/**
 * 搜索人的实现
 */
public class SearchUserPresenter extends BasePresenter<SearchContract.UserView>
        implements SearchContract.Presenter {

    public SearchUserPresenter(SearchContract.UserView view) {
        super(view);
    }

    @Override
    public void search(String content) {

    }
}
