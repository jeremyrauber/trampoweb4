package com.projetos.megabit.trampoweb4;

/**
 * Created by jeremy on 02/05/2017.
 */

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

public class Cadastrar extends AppCompatActivity {
    EditText descricao, valor, ncm, desconto, tributacao, estoque;
    String resultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);


        descricao = (EditText) findViewById(R.id.descricao);
        valor = (EditText) findViewById(R.id.valor);
        ncm = (EditText) findViewById(R.id.ncm);
        desconto = (EditText) findViewById(R.id.desconto);
        estoque = (EditText) findViewById(R.id.estoque);
        tributacao = (EditText) findViewById(R.id.trib);

        Button btnGravar = (Button) findViewById(R.id.salvar);
        btnGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println(valor.getText().toString() + " | " + descricao.getText().toString());

                ChamadaWeb chamada = new ChamadaWeb("http://"+getText(R.string.ip)+"/ThirdWorkDWFour/servicos/inserirum", descricao.getText().toString(), valor.getText().toString(),
                        ncm.getText().toString(), tributacao.getText().toString(), estoque.getText().toString(), desconto.getText().toString());
                chamada.execute();
            }
        });
    }

    public void atualizaMensagem(String resultado)
    {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public class ChamadaWeb extends AsyncTask<String, Void, String> {
        private String enderecoWeb;
        private Produto produto;
        private int tipoChamada; //1 - GET 2 - POST


        public  ChamadaWeb(String endereco, String descricao, String valor, String ncm, String tributacao,
                           String estoque, String desconto){

            this.produto = new Produto();

            produto.setDescricao(descricao);
            produto.setValor(Double.parseDouble(valor));
            produto.setNcm(ncm);
            produto.setTributacao(Double.parseDouble(tributacao));
            produto.setEstoque(Integer.parseInt(estoque));
            produto.setDescontoMaximo(Double.parseDouble(desconto));
            enderecoWeb = endereco;
        }

        @Override
        protected String doInBackground(String... params) {
            HttpClient cliente = HttpClientBuilder.create().build();
            JSONObject json = null;

            try {
                HttpPost chamada = new HttpPost(enderecoWeb);
                List<NameValuePair> parametros = new ArrayList<NameValuePair>(1);
                json = new JSONObject();

                json.put("descricao", produto.getDescricao());
                json.put("ncm", produto.getNcm());
                json.put("valor", produto.getValor());
                json.put("estoque", produto.getEstoque());
                json.put("descontoMaximo", produto.getDescontoMaximo());
                json.put("tributacao", produto.getTributacao());

                parametros.add(new BasicNameValuePair("stringJson", json.toString()));
                System.out.println(json.toString());

                chamada.setEntity(new UrlEncodedFormEntity(parametros));
                HttpResponse resposta = cliente.execute(chamada);

                String responseBody = EntityUtils.toString(resposta.getEntity());
                return responseBody;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        public void onPostExecute(String resultado)
        {
            if(resultado != null){
                System.out.println(resultado);
                atualizaMensagem(resultado);
            }
        }
    }
}