package br.edu.ifsul.control;

import br.edu.ifsul.dao.ItemDAO;
import br.edu.ifsul.dao.PedidoDAO;
import br.edu.ifsul.model.Item;
import br.edu.ifsul.model.Pedido;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class PedidoController {
    private static Scanner s = new Scanner(System.in);
    private PedidoDAO  pedidoDAO = new PedidoDAO();
    private ItemDAO itemDAO = new ItemDAO();

    public static void main(String[] args) {
        PedidoController telaPedido = new PedidoController();
        int opcao = 0;
        do{
            System.out.println("\n\n******** Pedidos ********");
            System.out.print(
                    "1. Check-out do pedido" +
                            "\n2. Enviar Pedido" +
                            "\n3. Excluir Pedido" +
                            "\n4. Lista todos os pedidos (inativos)" +
                            "\n5. Lista todos os pedidos (ativos)" +
                            "\n6. Lista todos os pedidos por período" +
                            "\n7. Listar todos os pedidos por cliente" +
                            "\n8. Listar pedido"+
                            "\n9. Retirar item do pedido"+
                            "\nDigite a opção (0 para sair): "
            );
            Scanner s = new Scanner(System.in);
            opcao = s.nextInt();
            switch (opcao){
                case 1:
                    System.out.println("Check-out do pedido");
                    telaPedido.checkoutPedido();
                    break;
                case 2:
                    System.out.println("Enviar pedido " + opcao);

                    break;
                case 3:
                    System.out.println("Excluir pedido" + opcao);
                    break;
                case 4:
                    System.out.println("Listar todos os pedidos inativos " + opcao);
                    break;
                case 5:
                    System.out.println("Listar todos os pedidos ativos " + opcao);
                    break;
                case 6:
                    System.out.println("Lista todos os pedidos por período" + opcao);
                    break;
                case 7:
                    System.out.println("Listar todos os pedidos por cliente " + opcao);
                    break;
                case 8:
                    System.out.println("Listar itens do pedido" );
                    telaPedido.listarPedido();
                case 9:
                    System.out.println("Retirar item do pedido");
                    telaPedido.softDeleteItem();
                    break;
                default:
                    if(opcao != 0) System.out.println("Opção inválida.");
            }
        }while (opcao != 0);
    }

    //case 1. check-out de pedido
    private void checkoutPedido(){
        System.out.print("Digite o código do pedido: ");
        int codigo = s.nextInt();
        Pedido pedido = pedidoDAO.getPedidoById(codigo);
        if(pedido != null){
            System.out.println(pedido);
            System.out.println("Confirmar a operação? (0-sim/1-não)");
            if(s.nextInt() == 0){
                if(pedidoDAO.checkout(pedido)) {
                    pedido = pedidoDAO.getPedidoById(codigo);
                    System.out.println("Check-out realizado.");
                    //emite a nota fiscal
                    System.out.println(pedido);
                }else{
                    System.out.println("Erro ao fazer check-out");
                }
            }else{
                System.out.println("Operação cancelada.");
            }
        }else{
            System.out.println("Código não localizado.");
        }
    }


    //case 8
    private void listarPedido() {
        System.out.print("Digite o código do pedido: ");
        Pedido pedido = pedidoDAO.getPedidoById(s.nextInt());
        if(pedido != null){
            System.out.print(pedido);
        }else{
            System.out.println("Código não localizado.");
        }
    }

    //case 9 ----- O Matheus me ajudou nesse, pois estava com dificuldade!
    private void softDeleteItem() {
        PedidoDAO produtoDAO = new PedidoDAO();
        ItemDAO itemDAO = new ItemDAO();
        Pedido pedido = new Pedido();
        int idPedido = 0;
        int idItem = 0;
        int flag = 0;
        do {
            System.out.println("Digite o id do pedido: ");
            idPedido = s.nextInt();
            pedido = pedidoDAO.getPedidoById(idPedido);
            if (pedido.getId() == 0){
                System.out.println("Codigo invalido");
                idPedido = 0;
            }else{
                System.out.println(pedido);
            }
        }while (idPedido == 0);
        do {
            System.out.println("Digite o id do item");
            idItem = s.nextInt();
            for (Item i : pedido.getItens()){
                if (i.getId() == idItem){
                    flag = 2;
                }
            }
            if (flag == 0){
                System.out.println("Id do item nao encontrado\nQuer continuar [0-sim/1-nao]");
                flag = s.nextInt();
            }
        }while (flag == 0);
        if (flag == 2) {
            if(itemDAO.softDeleteItemPedido(idItem, false) == false){
                System.out.println("Nao foi possivel excluir o item");
            }else {
                System.out.println("Item exlcuido");
            }
        }
    }

}

