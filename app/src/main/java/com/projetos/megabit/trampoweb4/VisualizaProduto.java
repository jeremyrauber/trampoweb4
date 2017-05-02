package com.projetos.megabit.trampoweb4;

/**
 * Created by jeremy on 02/05/2017.
 */


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

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

public class VisualizaProduto extends AppCompatActivity {
    EditText descricao, valor, ncm, desconto, tributacao, estoque;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualiza_produto);

        descricao = (EditText) findViewById(R.id.descricao);
        valor = (EditText) findViewById(R.id.valor);
        ncm = (EditText) findViewById(R.id.ncm);
        desconto = (EditText) findViewById(R.id.desconto);
        estoque = (EditText) findViewById(R.id.estoque);
        tributacao = (EditText) findViewById(R.id.trib);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("produto", 0);
        final int idProduto = pref.getInt("idProduto", 0);

        ChamadaWeb chamada = new ChamadaWeb("http://"+getText(R.string.ip)+"/ThirdWorkDWFour/servicos/pegarum", "","1","","1","1",
                "1",idProduto, 1);

        chamada.execute();

        Button btnGravar = (Button) findViewById(R.id.salvar);
        btnGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println(valor.getText().toString() + " | " + descricao.getText().toString());

                ChamadaWeb chamada = new ChamadaWeb("http://"+getText(R.string.ip)+"/ThirdWorkDWFour/servicos/alterarum", descricao.getText().toString(), valor.getText().toString(),
                        ncm.getText().toString(), tributacao.getText().toString(), estoque.getText().toString(), desconto.getText().toString(), idProduto, 2);
                chamada.execute();
            }
        });

        Button btnExcluir = (Button) findViewById(R.id.excluir);
        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println(valor.getText().toString() + " | " + descricao.getText().toString());

                ChamadaWeb chamada = new ChamadaWeb("http://"+getText(R.string.ip)+"/ThirdWorkDWFour/servicos/removerum", "","1","","1","1",
                        "1",idProduto, 1);
                chamada.execute();
            }
        });
    }


    public void atualizaMensagem(String resultado)
    {
        JSONObject rst = null;
        try {
            if(resultado.contains("Produto alterado com sucesso")) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }else if(resultado.contains("Produto removido com sucesso")) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }else{
                System.out.println(resultado);
                rst = new JSONObject(resultado);
                System.out.println(rst.toString());
                descricao.setText(rst.getString("descricao"));
                valor.setText(rst.getString("valor"));
                ncm.setText(rst.getString("ncm"));
                desconto.setText(rst.getString("descontoMaximo"));
                estoque.setText(rst.getString("estoque"));
                tributacao.setText(rst.getString("tributacao"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class ChamadaWeb extends AsyncTask<String, Void, String> {
        private String enderecoWeb;
        private int idProduto, tipo;
        private Produto produto;

        public  ChamadaWeb(String endereco, String descricao, String valor, String ncm, String tributacao,
                           String estoque, String desconto, int _idProduto, int _tipo){
            idProduto = _idProduto;
            tipo = _tipo;
            enderecoWeb = endereco;

            this.produto = new Produto();

            produto.setDescricao(descricao);
            produto.setValor(Double.parseDouble(valor));
            produto.setNcm(ncm);
            produto.setTributacao(Double.parseDouble(tributacao));
            produto.setEstoque(Integer.parseInt(estoque));
            produto.setDescontoMaximo(Double.parseDouble(desconto));
        }

        @Override
        protected String doInBackground(String... params) {
            HttpClient cliente = HttpClientBuilder.create().build();
            HttpPost chamada = new HttpPost(enderecoWeb);
            JSONObject json = null;
            List<NameValuePair> parametros = new ArrayList<NameValuePair>(1);


            try {
                if(tipo == 1){
                    System.out.println(idProduto);
                    parametros.add(new BasicNameValuePair("id", String.valueOf(idProduto)));

                    chamada.setEntity(new UrlEncodedFormEntity(parametros));
                }else if(tipo == 2){
                    json = new JSONObject();

                    json.put("id", idProduto);
                    json.put("descricao", produto.getDescricao());
                    json.put("ncm", produto.getNcm());
                    json.put("valor", produto.getValor());
                    json.put("estoque", produto.getEstoque());
                    json.put("descontoMaximo", produto.getDescontoMaximo());
                    json.put("tributacao", produto.getTributacao());

                    parametros.add(new BasicNameValuePair("stringJson", json.toString()));
                    System.out.println(json.toString());

                    chamada.setEntity(new UrlEncodedFormEntity(parametros));
                }
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
