
package model.BtreePackage;

import model.Usuario.Usuario;

import java.io.Serializable;

public class CHAVE_Btree<T> implements Serializable {
    private int val;//ID
    private T user;

    public CHAVE_Btree(int val, T user) {
        this.val = val;
        this.user = user;
        ((Usuario)user).setId(this.val);
    }

    public int getVal() {
        return val;
    }

    @Override
    public String toString() {
        return String.valueOf(val);
    }

    public T getUser() {
        return user;
    }
}

