package com.projetos.megabit.trampoweb4;

/**
 * Created by jeremy on 02/05/2017.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.util.EntityUtils;

public class Listar extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout containerAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);

        containerAddress = LinearLayout.class.cast(findViewById(R.id.containerAddress));

        ChamadaWeb chamada = new ChamadaWeb("http://"+getText(R.string.ip)+"/ThirdWorkDWFour/servicos/pegartodos");

        chamada.execute();

    }

    public void atualizaMensagem(String resultado)
    {
        JSONArray rst = null;
        JSONObject jsonO = null;
        try {

            rst = new JSONArray(resultado);

            for(int i=0; i<rst.length(); i++){
                jsonO = rst.getJSONObject(i);

                final LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                final TextView txtView = new TextView(linearLayout.getContext());
                final Button btn = new Button(linearLayout.getContext());
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                txtView.setText(jsonO.getString("descricao"));
                txtView.setTextSize(20);
                txtView.setTextColor(Color.parseColor("#000000"));
                btn.setId(Integer.parseInt(jsonO.getString("id")));
                btn.setText("Editar");
                btn.setOnClickListener(this);
                btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                //Seta os parametros
                txtView.setLayoutParams(new LinearLayout.LayoutParams(350, LinearLayout.LayoutParams.WRAP_CONTENT));
                //adiciona no containerAddress
                linearLayout.addView(txtView);
                linearLayout.addView(btn);
                containerAddress.addView(linearLayout);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("produto", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("idProduto", v.getId());
        editor.commit();


        Intent intent = new Intent(this, VisualizaProduto.class);
        finish();
        startActivity(intent);

    }

    private class ChamadaWeb extends AsyncTask<String, Void, String> {
        private String enderecoWeb;


        public ChamadaWeb(String endereco) {
            enderecoWeb = endereco;
        }

        @Override
        protected String doInBackground(String... params) {
            HttpClient cliente = HttpClientBuilder.create().build();

            try {
                HttpPost chamada = new HttpPost(enderecoWeb);
                System.out.println(enderecoWeb);
                HttpResponse resposta = cliente.execute(chamada);

                String responseBody = EntityUtils.toString(resposta.getEntity());
                return responseBody;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        public void onPostExecute(String resultado) {
            if (resultado != null) {
                atualizaMensagem(resultado);
            }
        }
    }
}