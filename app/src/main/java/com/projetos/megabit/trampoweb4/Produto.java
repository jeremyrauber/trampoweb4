package com.projetos.megabit.trampoweb4;

/**
 * Created by jeremy on 02/05/2017.
 */


import java.io.Serializable;

public class Produto implements Serializable {

    private int id;

    private String descricao;

    private String ncm;

    private Double valor;

    private Integer estoque;

    private Double descontoMaximo;

    private Double tributacao;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNcm() {
        return ncm;
    }

    public void setNcm(String ncm) {
        this.ncm = ncm;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Integer getEstoque() {
        return estoque;
    }

    public void setEstoque(Integer estoque) {
        this.estoque = estoque;
    }

    public Double getDescontoMaximo() {
        return descontoMaximo;
    }

    public void setDescontoMaximo(Double descontoMaximo) {
        this.descontoMaximo = descontoMaximo;
    }

    public Double getTributacao() {
        return tributacao;
    }

    public void setTributacao(double tributacao) {
        this.tributacao = tributacao;
    }

}
