package proj.jaeshim.project_for_android_ebay_02;

import java.util.List;

/**
 * Created by jaeshim on 2018-04-14.
 */

public class LottoItem {

    private List<String> lottoList;
    private String returnValue;
    private String drwNo;
    private String drwNoDate;
    private String drwtNo1;
    private String drwtNo2;
    private String drwtNo3;
    private String drwtNo4;
    private String drwtNo5;
    private String drwtNo6;
    private String bnusNo;

    private String firstPrzwnerCo;
    private String firstAccumamnt;
    private String firstWinamnt;
    private String totSellamnt;

    public LottoItem(){


    }
    public LottoItem(String drwNo){
        this.drwNo = drwNo;
    }


    public String getDrwNo(){
        return drwNo;
    }

    public void setDrwNo(String drwNo){
        this.drwNo=drwNo;
    }

}
