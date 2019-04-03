package com.tianxie.demo.retrofit.repository;

import com.aries.library.fast.retrofit.FastRetrofit;
import com.aries.library.fast.retrofit.FastRetryWhen;
import com.aries.library.fast.retrofit.FastTransformer;
import com.tianxie.demo.entity.BaiduAccessToken;
import com.tianxie.demo.entity.BaseInfo;
import com.tianxie.demo.entity.EntryInfo;
import com.tianxie.demo.entity.FaceSearchResult;
import com.tianxie.demo.entity.LoginEntity;
import com.tianxie.demo.entity.MyanmarIdentityInfo;
import com.tianxie.demo.entity.RegistFaceEntity;
import com.tianxie.demo.entity.RegistFaceResult;
import com.tianxie.demo.entity.ResideInfo;
import com.tianxie.demo.entity.TrafficInfo;
import com.tianxie.demo.entity.WorkInfo;
import com.tianxie.demo.entity.scan.ScanEntity;
import com.tianxie.demo.entity.VerificationEntitiy;
import com.tianxie.demo.retrofit.service.ApiService;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * @Author: AriesHoo on 2018/11/19 14:25
 * @E-Mail: AriesHoo@126.com
 * @Function: Retrofit api调用示例
 * @Description:
 */
public class ApiRepository extends BaseRepository
{

    private static volatile ApiRepository instance;
    private ApiService mApiService;

    private ApiRepository()
    {
        mApiService = getApiService();
    }

    public static ApiRepository getInstance()
    {
        if (instance == null)
        {
            synchronized (ApiRepository.class)
            {
                if (instance == null)
                {
                    instance = new ApiRepository();
                }
            }
        }
        return instance;
    }

    private ApiService getApiService()
    {
        mApiService = FastRetrofit.getInstance().createService(ApiService.class);
        return mApiService;
    }

    public Observable<LoginEntity> doLogin(Map<String, Object> map)
    {
        return FastTransformer.switchSchedulers(getApiService().login(map).retryWhen(new FastRetryWhen()));
    }


    public Observable<ScanEntity> doScanReq(String url, String token, Map<String, String> map)
    {
        return FastTransformer.switchSchedulers(getApiService().scanReq(url, token, map)).retryWhen(new FastRetryWhen());
    }

    public Observable<VerificationEntitiy> doGetVerification(Map<String, String> map)
    {
        return FastTransformer.switchSchedulers(getApiService().getVerifivcationCode(map)).retryWhen(new FastRetryWhen());
    }

    public Observable<List<BaseInfo>> doGetBaseInfo(String id)
    {
        return FastTransformer.switchSchedulers(getApiService().getBaseInfo(id)).retryWhen(new FastRetryWhen());
    }

    public Observable<List<MyanmarIdentityInfo>> doGetIdCardInfo(String id)
    {
        return FastTransformer.switchSchedulers(getApiService().getIdCardInfo(id)).retryWhen(new FastRetryWhen());
    }

    public Observable<List<EntryInfo>> doGetImmigrationInfo(String id)
    {
        return FastTransformer.switchSchedulers(getApiService().getImmigrationInfo(id)).retryWhen(new FastRetryWhen());
    }

    public Observable<List<WorkInfo>> doGetLaborInfo(String id)
    {
        return FastTransformer.switchSchedulers(getApiService().getLaborInfo(id)).retryWhen(new FastRetryWhen());
    }

    public Observable<List<ResideInfo>> doGetResideInfo(String id)
    {
        return FastTransformer.switchSchedulers(getApiService().getResideInfo(id)).retryWhen(new FastRetryWhen());
    }

    public Observable<List<TrafficInfo>> doGetTrafficInfo(String id)
    {
        return FastTransformer.switchSchedulers(getApiService().getTrafficInfo(id)).retryWhen(new FastRetryWhen());
    }

    public Observable<BaiduAccessToken> doGetBaiduToken(Map map)
    {
        return FastTransformer.switchSchedulers(getApiService().getBaiduToken(map)).retryWhen(new FastRetryWhen());
    }

    public Observable<RegistFaceResult> doRegistBaiduFace(String str, Map map)
    {
        return FastTransformer.switchSchedulers(getApiService().registBaiduFace(str, map)).retryWhen(new FastRetryWhen());
    }

    public Observable<FaceSearchResult> doFaceSearch(String str, Map map)
    {
        return FastTransformer.switchSchedulers(getApiService().faceSearch(str, map)).retryWhen(new FastRetryWhen());
    }
}
