import model.BtreePackage.*;
import model.Usuario.Usuario;
import java.util.Date;

public class Main {

    public static void main(String[] args) {

//        ADICIONANDO/REMOVENDO NA ARVORE (TUDO SALVO EM ARQUIVO)
        Usuario user1 = new Usuario("Mikael", "a", new Date(2000, 0,12),"a" ,"a", "a");
        Usuario user2 = new Usuario("Alysson", "b", new Date(2000, 0,12),"b" ,"b", "b");
        Usuario user3 = new Usuario("teste3", "c", new Date(2000, 0,12),"c" ,"c", "c");
        Usuario user4 = new Usuario("teste4", "d", new Date(2000, 0,12),"d" ,"d", "d");
        Usuario user5 = new Usuario("teste5", "e", new Date(2000, 0,12),"e" ,"e", "e");

        System.out.println("*****CRIANDO, INSERINDO USUARIOS, VENDO NÓS*****");
        Btree btree = new Btree("Btree");
        btree.insert(5,user5);
        btree.insert(4,user4);
        btree.insert(1,user1);
        btree.insert(2,user2);
        btree.insert(3,user3);
        btree.verNos(btree.getRaiz());

        System.out.println("Numero de elementos na arvore: "+btree.getNumElms());

        System.out.println("\n\n\n*****BUSCANDO USUARIOS*****");
        System.out.println(btree.buscarBtree(btree.getRaiz(),1));
        System.out.println(btree.buscarBtree(btree.getRaiz(),2));
        System.out.println(btree.buscarBtree(btree.getRaiz(),3));
        System.out.println("Numero de elementos na arvore: "+btree.getNumElms());

        System.out.println("\n\n\n*****DELETANDO USUARIO E BUSCANDO USUARIO DELETADO*****");
        System.out.println(btree.delete(2));
        System.out.println(btree.buscarBtree(btree.getRaiz(),2));
        System.out.println("Numero de elementos na arvore: "+btree.getNumElms());




        //LENDO DO ARQUIVO
//        Btree btree = Btree.arquivoGenericoBtree("Btree");
//        try{
//        System.out.println("\n\n\n*****BUSCANDO USUARIOS  ARQUIVO*****");
//        System.out.println(btree.buscarBtree(btree.getRaiz(),1));
//        System.out.println(btree.buscarBtree(btree.getRaiz(),3));
//        System.out.println(btree.buscarBtree(btree.getRaiz(),4));
//        System.out.println(btree.buscarBtree(btree.getRaiz(),5));
//        System.out.println("Numero de elementos na arvore: "+btree.getNumElms());
//
//        System.out.println("\n\n\n*****INSERINDO ID JA EXISTENTE NO BANCO*****");
//        Usuario userTesteFalha = new Usuario("testefalha", "z", new Date(2000, 0,12),"z" ,"z", "z");
//        btree.insert(3,userTesteFalha);
//        System.out.println(btree.buscarBtree(btree.getRaiz(),3));
//
//        System.out.println("\n\n\n*****INSERINDO ID NOVO NO BANCO*****");
//        Usuario user6 = new Usuario("teste6", "f", new Date(2000, 0,12),"f" ,"f", "f");
//        btree.insert(6,user6);
//
//        System.out.println("\n\n\n*****BUSCANDO/DELETANDO USUARIO QUE JA FOI DELETADO*****");
//        System.out.println(btree.buscarBtree(btree.getRaiz(),2));
//        System.out.println(btree.delete(2));
//
//        //VISUALIZAR TODO O BANCO
//        System.out.println("\n\n\n*****VENDO TODO O BANCO*****");
//        btree.emOrdem(btree.getRaiz());
//
//
//        } catch (Exception e) {
//            throw new RuntimeException("arquivo vazio ou corrompido");
//        }

    }


}
