package br.edu.ifsul.control;

import br.edu.ifsul.dao.ItemDAO;
import br.edu.ifsul.dao.PedidoDAO;
import br.edu.ifsul.model.Pedido;

import java.sql.Date;
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
                    telaPedido.insert();
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
                    System.out.println("Listar itens do pedido" + opcao);
                    telaPedido.listarPedido();
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
                if(pedidoDAO.updateEstado(pedido,"faturado")) System.out.println(pedidoDAO.getPedidoById(codigo));
                //gerar a nota fiscal

            }else{
                System.out.println("Operação cancelada.");
            }
        }else{
            System.out.println("Código não localizado.");
        }
    }

    //case 2. Enviar pedido
    private void insert(){
        Pedido pedido = new Pedido();
        System.out.println("Digite os dados do pedido:\nPagamento:");
        pedido.setFormaPagamento(s.next());
        System.out.println("Estado:");
        pedido.setEstado(s.next());
        System.out.println("Data Criação:");
        pedido.setDataCriacao( new Date(new java.util.Date().getTime()));
        System.out.println("Data Modificação:");
        pedido.setDataModificacao( new Date(new java.util.Date().getTime()));
        System.out.println("Situação:");
        pedido.setSituacao(true);
        System.out.println("Cliente:");
        //pedido.setCliente();
        System.out.println("Total pedido:");
        pedido.setTotalPedido(s.nextDouble());
        System.out.println("Nota Fiscal:");
        pedido.setNotaFiscal(s.nextInt());
        if(pedidoDAO.insert(pedido)){
            System.out.println("Pedido Salvo");
        }else{
            System.out.println("Erro ao tentar salvar o pedido. Por favor, contate o adminstrador.");
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

}
