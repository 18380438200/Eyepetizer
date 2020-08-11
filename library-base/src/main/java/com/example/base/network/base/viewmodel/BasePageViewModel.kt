package com.example.base.network.base.viewmodel

import android.app.Application
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import com.example.base.BR
import me.tatarka.bindingcollectionadapter2.ItemBinding

/**
 * create by libo
 * create on 2020/8/4
 * description 列表页面viewModel基类
 */
open abstract class BasePageViewModel<T>(application: Application) : BaseViewModel(application) {
    val items = ObservableArrayList<T>()

    val itemBinding by lazy {
        ItemBinding.of<T>(BR.item, getItemLayoutId()).bindExtra(BR.viewModel, this)
    }

    var curPage = MutableLiveData<Int>()

    /**
     * 获取item的布局ID
     */
    abstract fun getItemLayoutId(): Int

    abstract fun requestData(page: Int)

    fun getStartPageNum(): Int = 0

    fun refresh() {
        curPage.value = getStartPageNum()

        requestData(getStartPageNum())
    }

    fun loadMore() {
        curPage.value = curPage.value?.plus(1)
        requestData(curPage.value!!)
    }

    /**
     * 请求数据成功处理
     */
    fun handleItemData(page: Int, datas: List<T>) {
        if (page == getStartPageNum()) {
            //刷新完成
            items.clear()
            baseLiveData.refresh.postValue(1)
        } else {
            //加载更多完成
            baseLiveData.loadMore.postValue(1)
        }

        items.addAll(datas)

        if (items.size == 0) {
            // TODO: 2020/8/11   空数据，显示空布局
        }
    }

}