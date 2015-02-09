package com.restfultest;

import android.util.Log;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collection;

/**
 * Created by Novalogiq on 2/9/2015.
 */
public class FeriadoApi extends JSONHttpClient<Feriado> {

    /**
     * Consulta el webservice de los dias feriados en republica dominicana.
     * @return Collection de objetos tipo Feriado
     */
    public Collection<Feriado> getDiasFeriados(){
        try {
            Type type = new TypeToken<Collection<Feriado>>(){}.getType();

            String url = "http://data.developers.do/api/v1/feriados/2014.json";
            Collection<Feriado> feriados = Get(url, type);

            return feriados;
        }catch (Exception e){
            Log.e("Feriado Api", e.getMessage(), e);
        }
        return null;
    }
}
