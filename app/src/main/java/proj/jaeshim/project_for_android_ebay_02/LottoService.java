package proj.jaeshim.project_for_android_ebay_02;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by jaeshim on 2018-04-14.
 */

public class LottoService {
    private static final String LOTTO_SERVER = "http://www.nlotto.co.kr";

    static class NLottoResponse {
        String returnValue;
        String drwNo;
        String drwNoDate;
        String drwtNo1;
        String drwtNo2;
        String drwtNo3;
        String drwtNo4;
        String drwtNo5;
        String drwtNo6;
        String bnusNo;

        String firstPrzwnerCo;
        String firstAccumamnt;
        String firstWinamnt;
        String totSellamnt;

        public String toString() {
            return "returnValue: " + returnValue +
                    "\ndrwNo: " + drwNo +
                    "\ndrwNoDate: " + drwNoDate +
                    "\ndrwtNo1: " + drwtNo1 +
                    "\ndrwtNo2: " + drwtNo2 +
                    "\ndrwtNo3: " + drwtNo3 +
                    "\ndrwtNo4: " + drwtNo4 +
                    "\ndrwtNo5: " + drwtNo5 +
                    "\ndrwtNo6: " + drwtNo6 +
                    "\nfirstPrzwnerCo: " + firstPrzwnerCo +
                    "\nfirstAccumamnt: " + firstAccumamnt +
                    "\nfirstWinamnt: " + firstWinamnt +
                    "\ntotSellamnt: " + totSellamnt;
        }
    }

    interface LottoNumberRetriever {

        @GET("common.do")
        Call<NLottoResponse> getLottoNumbers(@Query("method") String method,@Query("drwNo") String no);
    }

    public static Call<NLottoResponse> getLottoNumbers(String drwNo){
        LottoNumberRetriever nLottoCon = new Retrofit.Builder().baseUrl(LOTTO_SERVER).addConverterFactory(GsonConverterFactory.create()).build().create(LottoNumberRetriever.class);

        return nLottoCon.getLottoNumbers("getLottoNumber",drwNo);
    }
}