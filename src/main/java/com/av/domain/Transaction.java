package com.av.domain;

/**
 * Created by vasil on 28.10.2015.
 */
public class Transaction {

private long docParentId;

    private String sD1;

    private String sD2;

    public long getDocParentId() {
        return docParentId;
    }

    public void setDocParentId(long docParentId) {
        this.docParentId = docParentId;
    }

    private String sD3;


    private String sC1;

    private String sC2;

    private String sC3;

    public String getsD1() {
        return sD1;
    }

    public void setsD1(String sD1) {
        this.sD1 = sD1;
    }

    public String getsD2() {
        return sD2;
    }

    public void setsD2(String sD2) {
        this.sD2 = sD2;
    }

    public String getsD3() {
        return sD3;
    }

    public void setsD3(String sD3) {
        this.sD3 = sD3;
    }

    public String getsC2() {
        return sC2;
    }

    public void setsC2(String sC2) {
        this.sC2 = sC2;
    }

    public String getsC3() {
        return sC3;
    }

    public void setsC3(String sC3) {
        this.sC3 = sC3;
    }

    public String getsC1() {
        return sC1;
    }

    public void setsC1(String sC1) {
        this.sC1 = sC1;
    }


    @Override
    public String toString() {
        return "Transaction{" +
                "Дебет: КБК=" + sD1 + ',' +
                "Вид деятельности=" + sD2 + ',' +
                "СБУ=" + sD3  +
                ", Кредит: КБК=" + sC1 +
                ", Вид деятельности=" + sC2 +
                ", СБУ=" + sC3  +
                '}';
    }
}
