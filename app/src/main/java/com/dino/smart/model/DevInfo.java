package com.dino.smart.model;

import java.io.Serializable;

public class DevInfo implements Serializable {
    String bindback;
    String bindgo;
    String bindleft;
    String bindmid;
    String bindright;
    boolean changeL;
    boolean changeR;
    String codeA1;
    String codeA2;
    String codeB1;
    String codeB2;
    String codeC1;
    String codeC2;
    String codeD1;
    String codeD2;
    String codeLo1;
    String codeLo2;
    String codeLt1;
    String codeLt2;
    String codeR1;
    String codeR2;
    String devId;
    String devName;
    String devType;
    int index;
    Boolean pyChange;

    public String getDevName() {
        return this.devName;
    }

    public void setDevName(String str) {
        this.devName = str;
    }

    public String getDevId() {
        return this.devId;
    }

    public void setDevId(String str) {
        this.devId = str;
    }

    public boolean isChangeL() {
        return this.changeL;
    }

    public void setChangeL(boolean z) {
        this.changeL = z;
    }

    public boolean isChangeR() {
        return this.changeR;
    }

    public void setChangeR(boolean z) {
        this.changeR = z;
    }

    public String getCodeLo1() {
        return this.codeLo1;
    }

    public void setCodeLo1(String str) {
        this.codeLo1 = str;
    }

    public String getCodeLo2() {
        return this.codeLo2;
    }

    public void setCodeLo2(String str) {
        this.codeLo2 = str;
    }

    public String getCodeLt1() {
        return this.codeLt1;
    }

    public void setCodeLt1(String str) {
        this.codeLt1 = str;
    }

    public String getCodeLt2() {
        return this.codeLt2;
    }

    public void setCodeLt2(String str) {
        this.codeLt2 = str;
    }

    public String getCodeR1() {
        return this.codeR1;
    }

    public void setCodeR1(String str) {
        this.codeR1 = str;
    }

    public String getCodeR2() {
        return this.codeR2;
    }

    public void setCodeR2(String str) {
        this.codeR2 = str;
    }

    public String getDevType() {
        return this.devType;
    }

    public void setDevType(String str) {
        this.devType = str;
    }

    public Boolean getPyChange() {
        return this.pyChange;
    }

    public void setPyChange(Boolean bool) {
        this.pyChange = bool;
    }

    public String getBindgo() {
        return this.bindgo;
    }

    public void setBindgo(String str) {
        this.bindgo = str;
    }

    public String getBindback() {
        return this.bindback;
    }

    public void setBindback(String str) {
        this.bindback = str;
    }

    public String getBindleft() {
        return this.bindleft;
    }

    public void setBindleft(String str) {
        this.bindleft = str;
    }

    public String getBindright() {
        return this.bindright;
    }

    public void setBindright(String str) {
        this.bindright = str;
    }

    public String getBindmid() {
        return this.bindmid;
    }

    public String getCodeA1() {
        return this.codeA1;
    }

    public void setCodeA1(String str) {
        this.codeA1 = str;
    }

    public String getCodeB1() {
        return this.codeB1;
    }

    public void setCodeB1(String str) {
        this.codeB1 = str;
    }

    public String getCodeC1() {
        return this.codeC1;
    }

    public void setCodeC1(String str) {
        this.codeC1 = str;
    }

    public String getCodeD1() {
        return this.codeD1;
    }

    public void setCodeD1(String str) {
        this.codeD1 = str;
    }

    public String getCodeA2() {
        return this.codeA2;
    }

    public void setCodeA2(String str) {
        this.codeA2 = str;
    }

    public String getCodeB2() {
        return this.codeB2;
    }

    public void setCodeB2(String str) {
        this.codeB2 = str;
    }

    public String getCodeC2() {
        return this.codeC2;
    }

    public void setCodeC2(String str) {
        this.codeC2 = str;
    }

    public String getCodeD2() {
        return this.codeD2;
    }

    public void setCodeD2(String str) {
        this.codeD2 = str;
    }

    public void setBindmid(String str) {
        this.bindmid = str;
    }

    public String stopmove() {
        return changepx(getBindmid()).replace(",", "");
    }

    public String control_forward() {
        return changepx(getBindgo()).replace(",", "");
    }

    public String control_backward() {
        return changepx(getBindback()).replace(",", "");
    }

    public String control_left() {
        String[] strArrSplit = changepx(getBindgo()).split(",");
        String[] strArrSplit2 = changepx(getBindleft()).split(",");
        String str = "";
        for (int i = 0; i < strArrSplit.length; i++) {
            String str2 = strArrSplit[i];
            String str3 = strArrSplit2[i];
            if (str2.equals(str3)) {
                str = str + str3;
            } else if (!str2.equals(str3)) {
                if (!str2.equals("00") && !str3.equals("00")) {
                    str = str + str3;
                } else if (str3.equals("00")) {
                    str = str + str2;
                } else {
                    str = str + str3;
                }
            }
        }
        return str;
    }

    public String control_right() {
        String[] strArrSplit = changepx(getBindgo()).split(",");
        String[] strArrSplit2 = changepx(getBindright()).split(",");
        String str = "";
        for (int i = 0; i < strArrSplit.length; i++) {
            String str2 = strArrSplit[i];
            String str3 = strArrSplit2[i];
            if (str2.equals(str3)) {
                str = str + str3;
            } else if (!str2.equals(str3)) {
                if (!str2.equals("00") && !str3.equals("00")) {
                    str = str + str3;
                } else if (str3.equals("00")) {
                    str = str + str2;
                } else {
                    str = str + str3;
                }
            }
        }
        return str;
    }

    public String control_back_right() {
        String[] strArrSplit = changepx(getBindback()).split(",");
        String[] strArrSplit2 = changepx(getBindright()).split(",");
        String str = "";
        for (int i = 0; i < strArrSplit.length; i++) {
            String str2 = strArrSplit[i];
            String str3 = strArrSplit2[i];
            if (str2.equals(str3)) {
                str = str + str3;
            } else if (!str2.equals(str3)) {
                if (!str2.equals("00") && !str3.equals("00")) {
                    str = str + str3;
                } else if (str3.equals("00")) {
                    str = str + str2;
                } else {
                    str = str + str3;
                }
            }
        }
        return str;
    }

    public String control_back_left() {
        String[] strArrSplit = changepx(getBindback()).split(",");
        String[] strArrSplit2 = changepx(getBindleft()).split(",");
        String str = "";
        for (int i = 0; i < strArrSplit.length; i++) {
            String str2 = strArrSplit[i];
            String str3 = strArrSplit2[i];
            if (str2.equals(str3)) {
                str = str + str3;
            } else if (!str2.equals(str3)) {
                if (!str2.equals("00") && !str3.equals("00")) {
                    str = str + str3;
                } else if (str3.equals("00")) {
                    str = str + str2;
                } else {
                    str = str + str3;
                }
            }
        }
        return str;
    }

    public String changepx(String str) {
        String[] strArrSplit = str.split(",");
        return strArrSplit[4] + "," + strArrSplit[3] + "," + strArrSplit[2] + "," + strArrSplit[1] + "," + strArrSplit[0];
    }

    public String changecx(String str) {
        return changepx(str);
    }

    public String control_A1() {
        return changecx(getCodeA1());
    }

    public String control_A2() {
        return changecx(getCodeA2());
    }

    public String control_B1() {
        return changecx(getCodeB1());
    }

    public String control_B2() {
        return changecx(getCodeB2());
    }

    public String control_C1() {
        return changecx(getCodeC1());
    }

    public String control_C2() {
        return changecx(getCodeC2());
    }

    public String control_D1() {
        return changecx(getCodeD1());
    }

    public String control_D2() {
        return changecx(getCodeD2());
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int i) {
        this.index = i;
    }
}
