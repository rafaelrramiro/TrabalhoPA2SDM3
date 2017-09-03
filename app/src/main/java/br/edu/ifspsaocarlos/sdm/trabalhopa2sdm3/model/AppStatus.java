package br.edu.ifspsaocarlos.sdm.trabalhopa2sdm3.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Note on 03/09/2017.
 */

public class AppStatus implements Serializable {

    private Contato contatoAtual;

    private List<Contato> contatoList;

    public AppStatus(Contato contatoAtual, List<Contato> contatoList) {
        this.contatoAtual = contatoAtual;
        this.contatoList = contatoList;
    }

    public Contato getContatoAtual() {
        return contatoAtual;
    }

    public void setContatoAtual(Contato contatoAtual) {
        this.contatoAtual = contatoAtual;
    }

    public List<Contato> getContatoList() {
        return contatoList;
    }

    public void setContatoList(List<Contato> contatoList) {
        this.contatoList = contatoList;
    }
}
