import model.Btree;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Btree arvore = new Btree(2L);
        arvore.insert(10);
        arvore.insert(20);
        arvore.insert(30);
        arvore.insert(15);
        arvore.insert(40);
        arvore.insert(50);
        arvore.insert(60);
        arvore.insert(70);
        arvore.insert(80);
        arvore.delete(50);

        System.out.println((arvore.buscarBtree(arvore.getRaiz(),80)));
    }
}