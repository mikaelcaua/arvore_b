package model.Usuario;

import java.io.Serializable;
import java.util.Date;

public class Usuario implements Serializable {
    private Integer id;
    private String nome;
    private String email;
    private Date dataNascimento;
    private String urlFoto;
    private String login;
    private String senha;

    public Usuario(String nome, String email, Date dataNascimento, String urlFoto, String login, String senha) {
        this.nome = nome;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.urlFoto = urlFoto;
        this.login = login;
        this.senha = senha;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return  "\nid= "+id+
                "\nnome='" + nome + '\n' +
                "email='" + email + '\n' +
                "dataNascimento=" + dataNascimento +'\n'+
                "urlFoto='" + urlFoto + '\n' +
                "login='" + login + '\n'+
                "senha='" + senha + "\n";
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
