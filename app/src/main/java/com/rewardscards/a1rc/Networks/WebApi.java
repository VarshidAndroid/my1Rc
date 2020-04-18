package com.rewardscards.a1rc.Networks;

import com.rewardscards.a1rc.Model.MainMenuModel;
import com.rewardscards.a1rc.Model.NewProuctAdResponse;
import com.rewardscards.a1rc.Model.RegistrationModel;
import com.rewardscards.a1rc.Model.StoreProductDetilsModel;
import com.rewardscards.a1rc.Model.SubMenumodell;
import com.rewardscards.a1rc.Response.AboutResponse;
import com.rewardscards.a1rc.Response.AddCAardToUSerModel;
import com.rewardscards.a1rc.Response.ApplyRcResponse;
import com.rewardscards.a1rc.Response.CommonModel;
import com.rewardscards.a1rc.Response.CommonModelSmallResponse;
import com.rewardscards.a1rc.Response.DeleteAccountResponse;
import com.rewardscards.a1rc.Response.FAQANSModel;
import com.rewardscards.a1rc.Response.FAQLISTREsponseModel;
import com.rewardscards.a1rc.Response.HomeBanner;
import com.rewardscards.a1rc.Response.LoginModel;
import com.rewardscards.a1rc.Response.ModelResponseCommonSmallCase;
import com.rewardscards.a1rc.Response.OtpVerificationResponse;
import com.rewardscards.a1rc.Response.PRofileUpdateSuccesResponse;
import com.rewardscards.a1rc.Response.PrivacyPolicyResponse;
import com.rewardscards.a1rc.Response.ProfileModel;
import com.rewardscards.a1rc.Response.SavedCardResponseModel;
import com.rewardscards.a1rc.Response.StoreCardListResponseModel;
import com.rewardscards.a1rc.Response.StoreLatLan;
import com.rewardscards.a1rc.Response.StoreListModel;
import com.rewardscards.a1rc.Response.StoreLocal;
import com.rewardscards.a1rc.Response.StoreLocationResponseModel;
import com.rewardscards.a1rc.Response.StoreProductModel;
import com.rewardscards.a1rc.Response.TermsAndConditionResponse;
import com.rewardscards.a1rc.params.AddCAardToUSerPAram;
import com.rewardscards.a1rc.params.ApplyRcParam;
import com.rewardscards.a1rc.params.DeleteAccountParam;
import com.rewardscards.a1rc.params.FAQQUEPARAM;
import com.rewardscards.a1rc.params.Login;
import com.rewardscards.a1rc.params.OtpParam;
import com.rewardscards.a1rc.params.ParamStore;
import com.rewardscards.a1rc.params.ProductDetails;
import com.rewardscards.a1rc.params.ProfilePicParam;
import com.rewardscards.a1rc.params.ProfileupdatePRama;
import com.rewardscards.a1rc.params.RegistrationMoel;
import com.rewardscards.a1rc.params.ResponseModelCode;
import com.rewardscards.a1rc.params.SAIDParam;
import com.rewardscards.a1rc.params.StoreProductParam;
import com.rewardscards.a1rc.params.SubMenuParam;
import com.rewardscards.a1rc.params.UerIdParam;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface WebApi {

    @POST("/API/login.php")
    Call<LoginModel> login(@Body Login login);

    @POST("/API/registration.php")
    Call<RegistrationModel> registration(@Body RegistrationMoel login);

    @POST("/API/fetch_store_group_menu.php")
    Call<MainMenuModel> fetch_store_group_menu();

    @POST("/API/store_group_cat.php")
    Call<SubMenumodell> store_group_cat(@Body SubMenuParam subMenuParam);

    @POST("/API/fetch_store_from_group.php")
    Call<StoreListModel> fetch_store_from_group(@Body com.rewardscards.a1rc.params.StoreListModel subMenuParam);

    @POST("/API/fetch_adverts_store_product.php")
    Call<StoreProductModel> fetch_adverts_store_product(@Body StoreProductParam storeProductParam);

    @POST("/API/fetch_product_details.php")
    Call<StoreProductDetilsModel> fetch_product_details(@Body ProductDetails productDetails);

    @POST("/API/apply_onerc_card.php")
    Call<ApplyRcResponse> apply_onerc_card(@Body ApplyRcParam applyRcParam);

    @GET("/API/about_app.php")
    Call<AboutResponse> about_app();

    @GET("/API/terms_condition.php")
    Call<TermsAndConditionResponse> terms_condition();

    @GET("/API/privacy_policy.php")
    Call<PrivacyPolicyResponse> privacy_policy();

    @GET("/API/faq.php")
    Call<FAQLISTREsponseModel> faq();

    @POST("/API/update_profile.php")
    Call<PRofileUpdateSuccesResponse> update_profile(@Body ProfileupdatePRama applyRcParam);

    @POST("/API/delete_account.php")
    Call<DeleteAccountResponse> delete_account(@Body DeleteAccountParam deleteAccountParam);

    @GET("/API/store_cards.php")
    Call<StoreCardListResponseModel> store_cards();

    @POST("/API/add_store_card.php")
    Call<AddCAardToUSerModel> add_store_card(@Body AddCAardToUSerPAram addCAardToUSerPAram);

    @POST("/API/users_card.php")
    Call<SavedCardResponseModel> users_card(@Body UerIdParam uerIdParam);

    @POST("/API/fetch_mobile_number.php")
    Call<CommonModelSmallResponse> fetch_mobile_number(@Body SAIDParam saidParam);

    @POST("/API/active_onerc_card.php")
    Call<ModelResponseCommonSmallCase> active_onerc_card(@Body SAIDParam saidParam);

    @GET("/API/fetch_advertise.php")
    Call<HomeBanner> fetch_advertise();

    @POST("/API/update_profile_pic.php")
    Call<ProfileModel> update_profile_pic(@Body ProfilePicParam profilePicParam);

    @GET("/API/store_locator.php")
    Call<StoreLocationResponseModel> store_locator();

    @POST("/API/otp_verify.php")
    Call<OtpVerificationResponse> otp_verify(@Body OtpParam otpParam);

    @POST("/API/faq_answer.php")
    Call<FAQANSModel> faq_answer(@Body FAQQUEPARAM faqqueparam);

    @POST("/API/location_store.php")
    Call<StoreLatLan> location_store();

    @POST("/API/push_notification.php")
    Call<StoreLatLan> push_notification(@Body ParamStore faqqueparam);

    @POST("/API/fetch_adverts_store_product.php")
    Call<NewProuctAdResponse> fetch_adverts_store_product_new(@Body ResponseModelCode responseModelCode);

}
