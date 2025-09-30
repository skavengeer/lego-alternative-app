package com.dino.smart.handler;


public class HexHandler {
    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static byte[] intToByteArray(int i) {
        return new byte[]{(byte) ((i >> 24) & 255), (byte) ((i >> 16) & 255), (byte) ((i >> 8) & 255), (byte) (i & 255)};
    }

    public static String bytesToHexFun1(byte[] bArr) {
        char[] cArr = new char[bArr.length * 2];
        int i = 0;
        for (int i2 : bArr) {
            if (i2 < 0) {
                i2 += 256;
            }
            int i3 = i + 1;
            char[] cArr2 = HEX_CHAR;
            cArr[i] = cArr2[i2 / 16];
            i = i3 + 1;
            cArr[i3] = cArr2[i2 % 16];
        }
        return new String(cArr);
    }

    public static String bytesToHexFun2(byte[] bArr) {
        char[] cArr = new char[bArr.length * 2];
        int i = 0;
        for (byte b : bArr) {
            int i2 = i + 1;
            char[] cArr2 = HEX_CHAR;
            cArr[i] = cArr2[(b >>> 4) & 15];
            i = i2 + 1;
            cArr[i2] = cArr2[b & 15];
        }
        return new String(cArr);
    }

    public static String bytesToHexFun3(byte[] bArr) {
        if (bArr == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder(bArr.length * 2);
        for (byte b : bArr) {
            sb.append(String.format("%02x ", new Integer(b & 255)));
        }
        String string = sb.toString();
        return string.length() > 6 ? string.substring(0, string.length() - 1) : string;
    }

    public static byte[] toBytes(String str) {
        if (str == null || str.trim().equals("")) {
            return new byte[0];
        }
        String strReplace = str.replace(" ", "");
        byte[] bArr = new byte[strReplace.length() / 2];
        for (int i = 0; i < strReplace.length() / 2; i++) {
            int i2 = i * 2;
            bArr[i] = (byte) Integer.parseInt(strReplace.substring(i2, i2 + 2), 16);
        }
        return bArr;
    }

    public static int byteArrayToInt(byte[] bArr) {
        int i = 0;
        for (int i2 = 0; i2 < 4; i2++) {
            i += (bArr[i2] & 255) << ((3 - i2) * 8);
        }
        return i;
    }

    public static int bytesToInt(byte[] bArr) {
        int iPow = 0;
        for (int length = bArr.length - 1; length >= 0; length--) {
            iPow = (int) (iPow + (bArr[length] * Math.pow(255.0d, (bArr.length - length) - 1)));
        }
        return iPow;
    }

    public static int Bytes2Dec(byte[] bArr) {
        int i = 0;
        for (int i2 = 0; i2 < bArr.length; i2++) {
            i += (bArr[i2] & 255) << ((3 - i2) * 8);
        }
        return i;
    }
}
