package com.fastaccess.ui.modules.repo.pull_request;

import com.fastaccess.ui.base.mvp.BaseMvp;

/**
 * Created by Kosh on 31 Dec 2016, 1:35 AM
 */

public interface RepoPullRequestPagerMvp {

    interface View extends BaseMvp.FAView {}

    interface Presenter extends BaseMvp.FAPresenter<View> {}
}
