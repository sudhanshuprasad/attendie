package com.regain.attendie;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public class API_COLLEGE {
//    payload = {"styno": str(sem)}
    static String DOMAIN = "http://115.240.101.71:8282";
    static String LOGIN_URL = DOMAIN+"/CampusPortalSOA/login";
   static String STUDENTRESULT_URL = DOMAIN+"/CampusPortalSOA/stdrst";
   static String STUDENTPHOTO_URL = DOMAIN+"/CampusPortalSOA/image/studentPhoto";
//            # styno = int(1-8) semester number
    static String STUDENTINFO_URL = DOMAIN+"/CampusPortalSOA/studentinfo";
   static String RESULTDETAIL_URL = DOMAIN+"/CampusPortalSOA/rstdtl";
   static String RESULTDOWNLOAD_URL = DOMAIN+"/CampusPortalSOA/downresultpdf";
   static String ATTENDANCE_URL = DOMAIN+"/CampusPortalSOA/attendanceinfo";
    public static PostService postService=null;
    public static  PostService getPostService(){
        HttpLoggingInterceptor loggingInterceptor=new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .cookieJar(new CookieJar()).addInterceptor(loggingInterceptor)
                .build();
        if(postService==null){

            Retrofit retrofit=new Retrofit.Builder()
                    .baseUrl(DOMAIN)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            postService=retrofit.create(PostService.class);
        }
        return postService;
    }
    public interface PostService{
        @Headers("Content-Type:application/json")
        @POST("CampusPortalSOA/login")
        Call<Object> User(@Body UserAuthentication userAuthentication);

        @POST("CampusPortalSOA/stdrst")
        Call<Object> result();

        @Headers("Content-Type:application/json")
        @GET("CampusPortalSOA/image/studentPhoto")
        Call<ResponseBody> getPhoto();

        @Headers("Content-Type:application/json")
        @POST("CampusPortalSOA/rstdtl")
        Call<Object> getDetailedResult(@Body Semester sem);

        @Headers("Content-Type:application/json")
        @POST("CampusPortalSOA/studentinfo")
        Call<Object> getInfo();

        @Headers("Content-Type:application/json")
        @POST("CampusPortalSOA/login")
        Call<Object> Update_password(@Body Update_Change_Password update_change_password);

        @Headers("Content-Type:application/json")
        @POST("CampusPortalSOA/downresultpdf")
        Call<ResponseBody> downloadResult(@Body DownloadResult downloadResult);

        @Headers("Content-Type:application/json")
        @POST("CampusPortalSOA/attendanceinfo")
        Call<Object> attendanceInfo(@Body RegistrationAttendance registrationAttendance);

        @Headers("Content-Type:application/json")
        @POST("CampusPortalSOA/attendanceinfo")
        Call<Object> attendanceInfos();
        @Headers("Content-Type:application/json")
        @POST("CampusPortalSOA/studentSemester/lov")
        Call<Object> lovInfos();
        
    }
}
