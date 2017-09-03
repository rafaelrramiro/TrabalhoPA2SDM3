package br.edu.ifspsaocarlos.sdm.trabalhopa2sdm3.model;

import java.io.Serializable;

/**
 * Created by Note on 03/09/2017.
 */

@Entity
public class Contato implements Serializable {

    static final long serialVersionUID = 1L;

    @Id
    private Long id;

    private String nome_completo;

    private String apelido;

    @Generated(hash = 1448271076)
    public Contato() {
    }

    @Generated(hash = 1634916936)
    public Contato(Long id, String nome_completo, String apelido) {
        this.id = id;
        this.nome_completo = nome_completo;
        this.apelido = apelido;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome_completo() {
        return nome_completo;
    }

    public void setNome_completo(String nome_completo) {
        this.nome_completo = nome_completo;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }
}
