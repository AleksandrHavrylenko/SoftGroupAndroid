package ua.sg.academy.havrulenko.android;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

@SuppressWarnings("unused")
public class HashUtils {

    public static String sha512(String data) {
        return new String(Hex.encodeHex(DigestUtils.sha512(data)));
    }

    public static String md5(String data) {
        return new String(Hex.encodeHex(DigestUtils.md5(data)));
    }
}
