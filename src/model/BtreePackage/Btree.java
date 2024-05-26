package model.BtreePackage;

import model.BtreePackage.CHAVE_Btree;
import model.BtreePackage.NO_Btree;
import model.Usuario.Usuario;

import java.io.*;

public class Btree implements Serializable {
    private NO_Btree raiz;
    private Long nNos;
    private Long t = 2L;
    private String nomeArquivo;

    public Btree(String nomeArquivo) {
        this.raiz = null;
        this.nNos = 0L;
        this.nomeArquivo = nomeArquivo+".txt";
    }

    public Object buscarBtree(NO_Btree NO, int key) {
        if(NO == null){
            return null;
        }
        int i = 0;
        while (i < NO.getN() && key > NO.getChaves()[i].getVal()) {
            i++;
        }

        if (i < NO.getN() && key == NO.getChaves()[i].getVal()) {
            return NO.getChaves()[i].getUser();
        }

        if (NO.isFolha()) {
            return null;
        } else {
            return buscarBtree(NO.getFilhos()[i], key);
        }
    }

    public void insert(int key, Usuario user) {
//        GARANTIR Q A ID SEJA UNICA, JA QUE ESTAMOS TRABALHANDO COM UM "Banco"
        if(buscarBtree(getRaiz(),key) != null){
            System.out.println("Essa ID existe no banco");
            return;
        }
        NO_Btree r = raiz;
        if (r == null) {
            raiz = new NO_Btree(t.intValue());
            r = raiz;
        }
        if (r.getN() == 2 * t - 1) { // Se o nó raiz está cheio
            NO_Btree s = new NO_Btree(t.intValue());
            raiz = s;
            s.setFolha(false);
            s.getFilhos()[0] = r;
            splitChild(s, 0); // Divide o nó raiz
            insertNonFull(s, key, user); // Insere a chave no nó não cheio
        } else {
            insertNonFull(r, key, user);
        }

        nNos++;
        salvarEmArquivo(this,this.nomeArquivo);
    }

    private void insertNonFull(NO_Btree x, int key, Usuario user) {
        int i = x.getN() - 1;
        if (x.isFolha()) {
            // Desloca as chaves para dar espaço para a nova chave
            while (i >= 0 && key < x.getChaves()[i].getVal()) {
                x.getChaves()[i + 1] = x.getChaves()[i];
                i--;
            }
            x.getChaves()[i + 1] = new CHAVE_Btree(key, user);
            x.setN(x.getN() + 1);
        } else {
            // Encontra o filho adequado para a inserção
            while (i >= 0 && key < x.getChaves()[i].getVal()) {
                i--;
            }
            i++;
            if (x.getFilhos()[i].getN() == 2 * t - 1) {
                splitChild(x, i); // Divide o filho cheio
                if (key > x.getChaves()[i].getVal()) {
                    i++;
                }
            }
            insertNonFull(x.getFilhos()[i], key,user);
        }
    }

    private void splitChild(NO_Btree x, int i) {
        int t = this.t.intValue();
        NO_Btree z = new NO_Btree(t);
        NO_Btree y = x.getFilhos()[i];
        z.setFolha(y.isFolha());
        z.setN(t - 1);

        for (int j = 0; j < t - 1; j++) {
            z.getChaves()[j] = y.getChaves()[j + t];
            y.getChaves()[j + t] = null;
        }

        if (!y.isFolha()) {
            for (int j = 0; j < t; j++) {
                z.getFilhos()[j] = y.getFilhos()[j + t];
                y.getFilhos()[j + t] = null;
            }
        }

        y.setN(t - 1);

        for (int j = x.getN(); j >= i + 1; j--) {
            x.getFilhos()[j + 1] = x.getFilhos()[j];
        }
        x.getFilhos()[i + 1] = z;

        for (int j = x.getN() - 1; j >= i; j--) {
            x.getChaves()[j + 1] = x.getChaves()[j];
        }
        x.getChaves()[i] = y.getChaves()[t - 1];
        y.getChaves()[t - 1] = null;
        x.setN(x.getN() + 1);
    }

    public Object delete(int key) {
        if (raiz == null) {
            System.out.println("A árvore está vazia.");
            return null;
        }

        Object deletedKey = delete(raiz, key);

        if (raiz.getN() == 0) {
            if (raiz.isFolha()) {
                raiz = null;
            } else {
                raiz = raiz.getFilhos()[0];
            }
        }
        nNos--;
        salvarEmArquivo(this,nomeArquivo);
        return deletedKey;
    }

    private Object delete(NO_Btree no, int key) {
        int idx = findKey(no, key);

        if (idx < no.getN() && no.getChaves()[idx].getVal() == key) {
            if (no.isFolha()) {
                return deleteFolha(no, idx);
            } else {
                return deleteNaoFolha(no, idx);
            }
        } else {
            if (no.isFolha()) {
                return null;
            }

            boolean flag = (idx == no.getN());

            if (no.getFilhos()[idx].getN() < t) {
                fill(no, idx);
            }

            if (flag && idx > no.getN()) {
                return delete(no.getFilhos()[idx - 1], key);
            } else {
                return delete(no.getFilhos()[idx], key);
            }
        }
    }

    private Object deleteFolha(NO_Btree no, int idx) {
        Object deletedKey = no.getChaves()[idx].getUser();
        for (int i = idx + 1; i < no.getN(); i++) {
            no.getChaves()[i - 1] = no.getChaves()[i];
        }
        no.setN(no.getN() - 1);
        return deletedKey;
    }

    private Object deleteNaoFolha(NO_Btree no, int idx) {
        CHAVE_Btree key = no.getChaves()[idx];

        if (no.getFilhos()[idx].getN() >= t) {
            CHAVE_Btree pred = getPredecessor(no, idx);
            no.getChaves()[idx] = pred;
            return delete(no.getFilhos()[idx], pred.getVal());
        } else if (no.getFilhos()[idx + 1].getN() >= t) {
            CHAVE_Btree succ = getSuccessor(no, idx);
            no.getChaves()[idx] = succ;
            return delete(no.getFilhos()[idx + 1], succ.getVal());
        } else {
            merge(no, idx);
            return delete(no.getFilhos()[idx], key.getVal());
        }
    }

    private CHAVE_Btree getPredecessor(NO_Btree no, int idx) {
        NO_Btree cur = no.getFilhos()[idx];
        while (!cur.isFolha()) {
            cur = cur.getFilhos()[cur.getN()];
        }
        return cur.getChaves()[cur.getN() - 1];
    }

    private CHAVE_Btree getSuccessor(NO_Btree no, int idx) {
        NO_Btree cur = no.getFilhos()[idx + 1];
        while (!cur.isFolha()) {
            cur = cur.getFilhos()[0];
        }
        return cur.getChaves()[0];
    }

    private void fill(NO_Btree no, int idx) {
        if (idx != 0 && no.getFilhos()[idx - 1].getN() >= t) {
            borrowFromPrev(no, idx);
        } else if (idx != no.getN() && no.getFilhos()[idx + 1].getN() >= t) {
            borrowFromNext(no, idx);
        } else {
            if (idx != no.getN()) {
                merge(no, idx);
            } else {
                merge(no, idx - 1);
            }
        }
    }

    private void borrowFromPrev(NO_Btree no, int idx) {
        NO_Btree child = no.getFilhos()[idx];
        NO_Btree sibling = no.getFilhos()[idx - 1];

        for (int i = child.getN() - 1; i >= 0; i--) {
            child.getChaves()[i + 1] = child.getChaves()[i];
        }

        if (!child.isFolha()) {
            for (int i = child.getN(); i >= 0; i--) {
                child.getFilhos()[i + 1] = child.getFilhos()[i];
            }
        }

        child.getChaves()[0] = no.getChaves()[idx - 1];

        if (!no.isFolha()) {
            child.getFilhos()[0] = sibling.getFilhos()[sibling.getN()];
        }

        no.getChaves()[idx - 1] = sibling.getChaves()[sibling.getN() - 1];
        child.setN(child.getN() + 1);
        sibling.setN(sibling.getN() - 1);
    }

    private void borrowFromNext(NO_Btree no, int idx) {
        NO_Btree child = no.getFilhos()[idx];
        NO_Btree sibling = no.getFilhos()[idx + 1];

        child.getChaves()[child.getN()] = no.getChaves()[idx];

        if (!child.isFolha()) {
            child.getFilhos()[child.getN() + 1] = sibling.getFilhos()[0];
        }

        no.getChaves()[idx] = sibling.getChaves()[0];

        for (int i = 1; i < sibling.getN(); i++) {
            sibling.getChaves()[i - 1] = sibling.getChaves()[i];
        }

        if (!sibling.isFolha()) {
            for (int i = 1; i <= sibling.getN(); i++) {
                sibling.getFilhos()[i - 1] = sibling.getFilhos()[i];
            }
        }

        child.setN(child.getN() + 1);
        sibling.setN(sibling.getN() - 1);
    }

    private void merge(NO_Btree no, int indice) {
        NO_Btree filho = no.getFilhos()[indice];
        NO_Btree irmao = no.getFilhos()[indice + 1];

        filho.getChaves()[t.intValue() - 1] = no.getChaves()[indice];

        for (int i = 0; i < irmao.getN(); i++) {
            filho.getChaves()[i + t.intValue()] = irmao.getChaves()[i];
        }

        if (!filho.isFolha()) {
            for (int i = 0; i <= irmao.getN(); i++) {
                filho.getFilhos()[i + t.intValue()] = irmao.getFilhos()[i];
            }
        }

        for (int i = indice + 1; i < no.getN(); i++) {
            no.getChaves()[i - 1] = no.getChaves()[i];
        }

        for (int i = indice + 1; i <= no.getN(); i++) {
            no.getFilhos()[i - 1] = no.getFilhos()[i];
        }

        filho.setN(filho.getN() + irmao.getN() + 1);
        no.setN(no.getN() - 1);
    }

    private int findKey(NO_Btree no, int key) {
        int idx = 0;
        while (idx < no.getN() && no.getChaves()[idx].getVal() < key) {
            idx++;
        }
        return idx;
    }

    public void salvarEmArquivo(Btree tree, String nomeArquivo) {
        try (FileOutputStream fileOut = new FileOutputStream(nomeArquivo);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(tree);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> T arquivoGenericoBtree(String nomeArquivo) {
        T objeto = null;
        try (FileInputStream fileIn = new FileInputStream(nomeArquivo+".txt");
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            objeto = (T) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return objeto;
    }

    public void emOrdem(NO_Btree no) {
        if (no != null) {
            for (int i = 0; i < no.getN(); i++) {
                if (!no.isFolha()) {
                    emOrdem(no.getFilhos()[i]);
                }
                System.out.println(no.getChaves()[i].getUser());
            }
            if (!no.isFolha()) {
                emOrdem(no.getFilhos()[no.getN()]);
            }
        }
    }

    public void verNos(NO_Btree no) {
        if (no != null) {
            System.out.println(no);
            for (int i = 0; i < no.getN(); i++) {
                if (!no.isFolha()) {
                    verNos(no.getFilhos()[i]);
                }
            }
            if (!no.isFolha()) {
                verNos(no.getFilhos()[no.getN()]);
            }
        }
    }

    public Long getNumElms(){
        return nNos;
    }

    public NO_Btree getRaiz() {
        return raiz;
    }


}


