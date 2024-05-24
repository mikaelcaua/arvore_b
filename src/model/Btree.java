package model;

public class Btree {
    private NO_Btree raiz;
    private Long nNos;
    private Long t;

    public Btree(Long t) {
        this.t = t;
        this.raiz = null;
        this.nNos = 0L;
    }

    public void insert(int key) {
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
            insertNonFull(s, key); // Insere a chave no nó não cheio
        } else {
            insertNonFull(r, key);
        }
        nNos++;
    }

    private void insertNonFull(NO_Btree x, int key) {
        int i = x.getN() - 1;
        if (x.isFolha()) {
            // Desloca as chaves para dar espaço para a nova chave
            while (i >= 0 && key < x.getChaves()[i].getVal()) {
                x.getChaves()[i + 1] = x.getChaves()[i];
                i--;
            }
            x.getChaves()[i + 1] = new CHAVE_Btree(key);
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
            insertNonFull(x.getFilhos()[i], key);
        }
    }

    private void splitChild(NO_Btree x, int i) {
        int t = this.t.intValue();
        NO_Btree z = new NO_Btree(t);
        NO_Btree y = x.getFilhos()[i];
        z.setFolha(y.isFolha());
        z.setN(t - 1);

        // Copia as chaves do meio para o novo nó z
        for (int j = 0; j < t - 1; j++) {
            z.getChaves()[j] = y.getChaves()[j + t];
            y.getChaves()[j + t] = null; // Limpa a referência para evitar vazamento de memória
        }

        // Copia os filhos do meio para o novo nó z, se y não for uma folha
        if (!y.isFolha()) {
            for (int j = 0; j < t; j++) {
                z.getFilhos()[j] = y.getFilhos()[j + t];
                y.getFilhos()[j + t] = null; // Limpa a referência para evitar vazamento de memória
            }
        }

        y.setN(t - 1);

        // Move os filhos de x para a direita para abrir espaço para z
        for (int j = x.getN(); j >= i + 1; j--) {
            x.getFilhos()[j + 1] = x.getFilhos()[j];
        }
        x.getFilhos()[i + 1] = z;

        // Move as chaves de x para a direita para abrir espaço para a chave do meio de y
        for (int j = x.getN() - 1; j >= i; j--) {
            x.getChaves()[j + 1] = x.getChaves()[j];
        }
        x.getChaves()[i] = y.getChaves()[t - 1];
        y.getChaves()[t - 1] = null; // Limpa a referência para evitar vazamento de memória
        x.setN(x.getN() + 1);
    }

    public void delete(int key) {
        if (raiz == null) {
            System.out.println("A árvore está vazia.");
            return;
        }

        delete(raiz, key);

        // Se a raiz tiver 0 chaves, a raiz é deletada
        if (raiz.getN() == 0) {
            if (raiz.isFolha()) {
                raiz = null;
            } else {
                raiz = raiz.getFilhos()[0];
            }
        }
    }

    private void delete(NO_Btree no, int key) {
        int idx = findKey(no, key);

        if (idx < no.getN() && no.getChaves()[idx].getVal() == key) {
            if (no.isFolha()) {
                deleteFromLeaf(no, idx);
            } else {
                deleteFromNonLeaf(no, idx);
            }
        } else {
            if (no.isFolha()) {
                System.out.println("A chave " + key + " não existe na árvore.");
                return;
            }

            boolean flag = (idx == no.getN());

            if (no.getFilhos()[idx].getN() < t) {
                fill(no, idx);
            }

            if (flag && idx > no.getN()) {
                delete(no.getFilhos()[idx - 1], key);
            } else {
                delete(no.getFilhos()[idx], key);
            }
        }
    }

    private void deleteFromLeaf(NO_Btree no, int idx) {
        for (int i = idx + 1; i < no.getN(); i++) {
            no.getChaves()[i - 1] = no.getChaves()[i];
        }
        no.setN(no.getN() - 1);
    }

    private void deleteFromNonLeaf(NO_Btree no, int idx) {
        int key = no.getChaves()[idx].getVal();

        if (no.getFilhos()[idx].getN() >= t) {
            int pred = getPredecessor(no, idx);
            no.getChaves()[idx] = new CHAVE_Btree(pred);
            delete(no.getFilhos()[idx], pred);
        } else if (no.getFilhos()[idx + 1].getN() >= t) {
            int succ = getSuccessor(no, idx);
            no.getChaves()[idx] = new CHAVE_Btree(succ);
            delete(no.getFilhos()[idx + 1], succ);
        } else {
            merge(no, idx);
            delete(no.getFilhos()[idx], key);
        }
    }

    private int getPredecessor(NO_Btree no, int idx) {
        NO_Btree cur = no.getFilhos()[idx];
        while (!cur.isFolha()) {
            cur = cur.getFilhos()[cur.getN()];
        }
        return cur.getChaves()[cur.getN() - 1].getVal();
    }

    private int getSuccessor(NO_Btree no, int idx) {
        NO_Btree cur = no.getFilhos()[idx + 1];
        while (!cur.isFolha()) {
            cur = cur.getFilhos()[0];
        }
        return cur.getChaves()[0].getVal();
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

    private void merge(NO_Btree no, int idx) {
        NO_Btree child = no.getFilhos()[idx];
        NO_Btree sibling = no.getFilhos()[idx + 1];

        child.getChaves()[t.intValue() - 1] = no.getChaves()[idx];

        for (int i = 0; i < sibling.getN(); i++) {
            child.getChaves()[i + t.intValue()] = sibling.getChaves()[i];
        }

        if (!child.isFolha()) {
            for (int i = 0; i <= sibling.getN(); i++) {
                child.getFilhos()[i + t.intValue()] = sibling.getFilhos()[i];
            }
        }

        for (int i = idx + 1; i < no.getN(); i++) {
            no.getChaves()[i - 1] = no.getChaves()[i];
        }

        for (int i = idx + 1; i <= no.getN(); i++) {
            no.getFilhos()[i - 1] = no.getFilhos()[i];
        }

        child.setN(child.getN() + sibling.getN() + 1);
        no.setN(no.getN() - 1);
    }

    private int findKey(NO_Btree no, int key) {
        int idx = 0;
        while (idx < no.getN() && no.getChaves()[idx].getVal() < key) {
            idx++;
        }
        return idx;
    }

    public NO_Btree buscarBtree(NO_Btree NO, int key) {
        int i = 0;
        while (i < NO.getN() && key > NO.getChaves()[i].getVal()) {
            i++;
        }

        if (i < NO.getN() && key == NO.getChaves()[i].getVal()) {
            return NO;
        }

        if (NO.isFolha()) {
            return null;
        } else {
            return buscarBtree(NO.getFilhos()[i], key);
        }
    }

    public NO_Btree getRaiz() {
        return raiz;
    }
}


