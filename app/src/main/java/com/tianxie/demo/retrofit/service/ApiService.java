package com.tianxie.demo.retrofit.service;


import com.aries.library.fast.retrofit.FastRetrofit;
import com.tianxie.demo.base.Common;
import com.tianxie.demo.entity.BaiduAccessToken;
import com.tianxie.demo.entity.BaseInfo;
import com.tianxie.demo.entity.EntryInfo;
import com.tianxie.demo.entity.FaceSearchEntity;
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

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * @Author: AriesHoo on 2018/7/30 14:01
 * @E-Mail: AriesHoo@126.com
 * Function: 接口定义
 * Description:
 */
public interface ApiService
{
    @FormUrlEncoded
    @POST("login")
    Observable<LoginEntity> login(@FieldMap Map<String, Object> map);

    @GET()
    Observable<ScanEntity> scanReq(@Url String url, @Header("Authorization") String token, @QueryMap Map<String, String> map);

    @GET("sms")
    Observable<VerificationEntitiy> getVerifivcationCode(@QueryMap Map<String, String> map);

    @GET("apiAction/v1/getBaseInfo/{idNumber}")
    Observable<List<BaseInfo>> getBaseInfo(@Path("idNumber") String idNumber);

    @GET("apiAction/v1/getIdCardInfo/{idNumber}")
    Observable<List<MyanmarIdentityInfo>> getIdCardInfo(@Path("idNumber") String idNumber);

    @GET("apiAction/v1/getImmigrationInfo/{idNumber}")
    Observable<List<EntryInfo>> getImmigrationInfo(@Path("idNumber") String idNumber);

    @GET("apiAction/v1/getLaborInfo/{idNumber}")
    Observable<List<WorkInfo>> getLaborInfo(@Path("idNumber") String idNumber);

    @GET("apiAction/v1/getResideInfo/{idNumber}")
    Observable<List<ResideInfo>> getResideInfo(@Path("idNumber") String idNumber);

    @GET("apiAction/v1/getTrafficInfo/{idNumber}")
    Observable<List<TrafficInfo>> getTrafficInfo(@Path("idNumber") String idNumber);

    @POST("oauth/2.0/token")
    @Headers({FastRetrofit.BASE_URL_NAME_HEADER + Common.BASE_BAIDU_KEY})
    Observable<BaiduAccessToken> getBaiduToken(@QueryMap Map<String, Object> map);

    @POST("rest/2.0/face/v3/faceset/user/add")
    @Headers({FastRetrofit.BASE_URL_NAME_HEADER + Common.BASE_BAIDU_KEY})
    Observable<RegistFaceResult> registBaiduFace(@Query("access_token") String token, @Body Map<String, Object> map);


    @POST("rest/2.0/face/v3/search")
    @Headers({FastRetrofit.BASE_URL_NAME_HEADER + Common.BASE_BAIDU_KEY})
    Observable<FaceSearchResult> faceSearch(@Query("access_token") String token, @Body Map<String, Object> map);
}
