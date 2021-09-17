package br.com.deliverit.financeiro.util;

import java.util.Map;

public class RequestUtil {

    public static String extrairParametro(Map<String, String[]> params, String nameParam) {
        return params.get(nameParam) != null ? params.get(nameParam)[0] : null;
    }
}
