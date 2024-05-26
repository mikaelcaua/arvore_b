package model.BtreePackage;

import java.io.Serializable;
import java.util.Arrays;

public class NO_Btree implements Serializable {
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

    public void setN(int n) {
        this.n = n;
    }

    public void setFolha(boolean folha) {
        this.folha = folha;
    }

    @Override
    public String toString() {
        return "chaves=" + Arrays.toString(chaves);
    }
}
