package com.tianxie.demo.retrofit.repository;

import android.accounts.NetworkErrorException;

import com.aries.library.fast.retrofit.FastRetryWhen;
import com.aries.library.fast.retrofit.FastTransformer;
import com.tianxie.demo.base.BaseEntity;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * @Author: AriesHoo on 2018/10/10 17:24
 * @E-Mail: AriesHoo@126.com
 * @Function: retrofit使用基类封装
 * @Description:
 */
public abstract class BaseRepository {

    /**
     * @param observable 用于解析 统一返回实体统一做相应的错误码--如登录失效
     * @param <T>
     * @return
     */
//    protected <T> Observable<T> transform(Observable<BaseEntity<T>> observable) {
//        return FastTransformer.switchSchedulers(
//                observable.retryWhen(new FastRetryWhen())
//                        .flatMap((Function<BaseEntity<T>, ObservableSource<T>>) result -> {
//                            if (result == null) {
//                                return Observable.error(new NetworkErrorException());
//                            } else {
//                                return Observable.just(result.data);
//                            }
//                        }));
//    }
}
