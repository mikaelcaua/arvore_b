package model;

import java.util.Arrays;

public class NO_Btree {
    private int t;
    private int n;
    private boolean folha;
    private CHAVE_Btree[] chaves;
    private NO_Btree[] filhos;

    public NO_Btree(int t) {
        this.filhos = new NO_Btree[2 * t];
        this.t = t;
        this.n = 0;
        this.folha = true;
        this.chaves = new CHAVE_Btree[(2 * t) - 1];
    }

    public int getT() {
        return t;
    }

    public int getN() {
        return n;
    }

    public boolean isFolha() {
        return folha;
    }

    public CHAVE_Btree[] getChaves() {
        return chaves;
    }

    public NO_Btree[] getFilhos() {
        return filhos;
    }

    public void setT(int t) {
        this.t = t;
    }

    public void setN(int n) {
        this.n = n;
    }

    public void setFolha(boolean folha) {
        this.folha = folha;
    }

    public void setChaves(CHAVE_Btree[] chaves) {
        this.chaves = chaves;
    }

    public void setFilhos(NO_Btree[] filhos) {
        this.filhos = filhos;
    }

    @Override
    public String toString() {
        return "chaves=" + Arrays.toString(chaves);
    }
}
