package br.edu.ifspsaocarlos.sdm.trabalhopa2sdm3.service;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Note on 03/09/2017.
 */

public class WebService {

    public static final String url_service = "http://www.nobile.pro.br/sdm/mensageiro";
    public static final String contato_end_point= "/contato";
    public static final String mensagem_end_point= "/mensagem";

    public static final int SUCESS = 0;
    public static final int ERROR = 1;

    public static void cadastraContato(final Contato contato, final Context context, final Handler handler){
        new Thread(){

            @Override
            public void run() {
                RequestQueue queue = Volley.newRequestQueue(context);
                String url = url_service + contato_end_point;
                Gson gson = new Gson();

                try {
                    JSONObject jsonBody = new JSONObject(gson.toJson(contato));

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                            Request.Method.POST,
                            url,
                            jsonBody,
                            new Response.Listener<JSONObject>() {
                                public void onResponse(JSONObject jsonObject) {
                                    Message mensagem = new Message();
                                    mensagem.what = SUCESS;
                                    mensagem.obj = jsonObject.toString();
                                    handler.sendMessage(mensagem);
                                }
                            },
                            new Response.ErrorListener() {
                                public void onErrorResponse(VolleyError volleyError) {
                                    Message mensagem = new Message();
                                    mensagem.what = ERROR;
                                    handler.sendMessage(mensagem);
                                }
                            });
                    queue.add(jsonObjectRequest);
                }catch (Exception e ) {
                    Message mensagem = new Message();
                    mensagem.what = ERROR;
                    handler.sendMessage(mensagem);
                }
            }
        }.start();
    }

    public static void buscaContatos(final Context context, final Handler handler) {
        new Thread(){

            @Override
            public void run() {
                RequestQueue queue = Volley.newRequestQueue(context);
                String url = url_service + contato_end_point;
                try {
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                            Request.Method.GET,
                            url,
                            null,
                            new Response.Listener<JSONObject>() {
                                public void onResponse(JSONObject jsonObject) {
                                    Message mensagem = new Message();
                                    mensagem.what = SUCESS;
                                    try {
                                        JSONArray jsonArray = jsonObject.getJSONArray("contatos");
                                        mensagem.obj = jsonArray.toString();
                                    } catch (JSONException e) {e.printStackTrace();}
                                    handler.sendMessage(mensagem);
                                }
                            },
                            new Response.ErrorListener() {
                                public void onErrorResponse(VolleyError volleyError) {
                                    Message mensagem = new Message();
                                    mensagem.what = ERROR;
                                    handler.sendMessage(mensagem);
                                }
                            });
                    queue.add(jsonObjectRequest);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static void buscaMensagens(final long lastId, final Contato contatoOrigem,
                                      final Contato contatoDestino, final Context context, final Handler handler) {
        new Thread() {

            @Override
            public void run() {

                RequestQueue queue = Volley.newRequestQueue(context);

                String url = url_service + mensagem_end_point +
                        "/"+lastId + "/"+contatoOrigem.getId().toString() + "/"+contatoDestino.getId();

                try {
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                            Request.Method.GET,
                            url,
                            null,
                            new Response.Listener<JSONObject>() {
                                public void onResponse(JSONObject jsonObject) {
                                    Message mensagem = new Message();
                                    mensagem.what = SUCESS;
                                    try {
                                        JSONArray jsonArray = jsonObject.getJSONArray("mensagens");
                                        mensagem.obj = jsonArray.toString();
                                    } catch (JSONException e) {e.printStackTrace();}
                                    handler.sendMessage(mensagem);
                                }
                            },
                            new Response.ErrorListener() {
                                public void onErrorResponse(VolleyError volleyError) {
                                    Message mensagem = new Message();
                                    mensagem.what = ERROR;
                                    handler.sendMessage(mensagem);
                                }
                            });
                    queue.add(jsonObjectRequest);
                } catch (Exception e) {

                }
            }

        }.start();
    }

    public static void cadastraMensagem(final Mensagem mensagem,
                                        final Context context, final Handler handler){
        new Thread(){

            @Override
            public void run() {
                RequestQueue queue = Volley.newRequestQueue(context);
                String url = url_service + mensagem_end_point;
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

                try {
                    JSONObject jsonBody = new JSONObject(gson.toJson(mensagem));

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                            Request.Method.POST,
                            url,
                            jsonBody,
                            new Response.Listener<JSONObject>() {
                                public void onResponse(JSONObject jsonObject) {
                                    Message mensagem = new Message();
                                    mensagem.what = SUCESS;
                                    mensagem.obj = jsonObject.toString();
                                    handler.sendMessage(mensagem);
                                }
                            },
                            new Response.ErrorListener() {
                                public void onErrorResponse(VolleyError volleyError) {
                                    Message mensagem = new Message();
                                    mensagem.what = ERROR;
                                    handler.sendMessage(mensagem);
                                }
                            });
                    queue.add(jsonObjectRequest);
                }catch (Exception e ) {
                    Message mensagem = new Message();
                    mensagem.what = ERROR;
                    handler.sendMessage(mensagem);
                }
            }
        }.start();
    }
}