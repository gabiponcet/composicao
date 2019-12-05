package br.edu.ifsul.control;

import br.edu.ifsul.dao.ClienteDAO;
import br.edu.ifsul.dao.PedidoDAO;
import br.edu.ifsul.dao.ProdutoDAO;
import br.edu.ifsul.model.Cliente;
import br.edu.ifsul.model.Item;
import br.edu.ifsul.model.Pedido;
import br.edu.ifsul.model.Produto;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VendasController {

    private static ProdutoDAO produtoDAO = new ProdutoDAO();
    private static double totalPedido;

    public static void main(String[] args) {
        int opcao;
        Scanner s = new Scanner(System.in);
        ClienteDAO clienteDAO = new ClienteDAO();
        Cliente cliente;
        Produto produto;
        List<Item> itens = new ArrayList<>();
        do {
            opcao = 0;
            System.out.println("\n\n******** Vendas ********");
            System.out.print("Digite o código do cliente: ");
            long codigoCliente = s.nextLong();
            cliente = clienteDAO.getClienteById(codigoCliente);
            if(cliente == null){
                System.out.println("Código inválido");
                opcao = 1;
            }else{
                System.out.println("Cliente selecionado: " + cliente);
                int sair = 2;
                do{
                    System.out.print("Digite o código do produto: ");
                    long codigoProduto = s.nextInt();
                    produto = produtoDAO.getProdutoById(codigoProduto);
                    if(produto == null){
                        System.out.println("Código inválido");
                        sair = 1;
                    }else{
                        System.out.println("Produto selecionado:" + produto);
                        System.out.print("Digite a quantidade: ");
                        int quantidade = s.nextInt();
                        if(quantidade > produto.getQuantidade()){
                            System.out.println("Quantidade inválida.");
                            sair = 1;
                        }else{
                            Item item = new Item(produto);
                            item.setQuantidade(quantidade);
                            item.setSituacao(true);
                            item.setTotalItem(quantidade * produto.getValor());
                            itens.add(item);
                            System.out.println("Produto adicionado ao carrinho.");
                            baixarQuantidade(item); //baixa o estoque ao adicionar no carrinho
                            System.out.print("\nDeseja vender outro produto (sim-1/não-2/cancelar item-3)? ");
                            sair = s.nextInt();

                            while(sair == 3 && !itens.isEmpty()){
                                exibirCarrinho(itens);
                                System.out.println("\nQual item você deseja excluir?");
                                int indice = s.nextInt();
                                voltarEstoque(itens.get(indice));
                                itens.remove(indice);
                                exibirCarrinho(itens);
                                System.out.println("\\nDeseja vender outro produto (sim-1/não-0/cancelar item-3)?");
                                sair = s.nextInt();
                            }

                        }
                    }
                }while(sair != 2);
                if(!itens.isEmpty()){ //se tem itens no carrinho
                    System.out.println("\n******* Seu carrinho *******");
                    totalPedido = 0;
                    itens.forEach( i -> { //firula para alinhar as colunas na impressão do carrinho
                        String nome = i.getProduto().getNome();
                        String precoUnitario = NumberFormat.getCurrencyInstance().format(i.getProduto().getValor());
                        int MAX = 20;
                        if(nome.length() <= MAX){
                            for (int j = nome.length(); j < MAX; j++) {
                                nome += " ";
                            }
                        }
                        if(precoUnitario.length() <= MAX){
                            for (int j = precoUnitario.length(); j < MAX-5; j++) {
                                precoUnitario += " ";
                            }
                        } //fim da firula
                        System.out.println(
                                "\tProduto: " + nome +
                                        "\tValor unidade = " +  precoUnitario +
                                        "\t\tQuantidade = " + i.getQuantidade() +
                                        "\t\tTotalItem = " + (NumberFormat.getCurrencyInstance().format(i.getQuantidade()*i.getProduto().getValor()))
                        );
                        totalPedido += i.getQuantidade()*i.getProduto().getValor();
                    });
                    System.out.println("*************************************\n" + "TOTAL DO PEDIDO = " + NumberFormat.getCurrencyInstance().format(totalPedido));
                    System.out.print("Fechar o pedido?(1-sim/2-não/3-excluir item ");
                    opcao = s.nextInt();
                    if(opcao == 1){
                        //salva o pedido
                        Pedido pedido = new Pedido(itens);
                        pedido.setFormaPagamento("a vista");
                        pedido.setEstado("aberto");
                        pedido.setCliente(cliente);
                        pedido.setTotalPedido(totalPedido);
                        pedido.setNotaFiscal(0);
                        if(new PedidoDAO().insert(pedido)){
                            System.out.println("Pedido salvo.");
                        }else{
                            System.out.println("Não foi possível salvar o pedido. Por favor, contate o administrador.");
                        }
                    }else if(opcao == 2){
                        System.out.print("Ops! Tem certeza? Você perderá esse pedido. (sim-1/não-2) ");
                        opcao = s.nextInt();
                        if(opcao == 1){
                            System.out.println("Pedido cancelado.");
                            //volta o estoque que foi baixado na venda
                            itens.forEach((i) -> {
                                voltarEstoque(i);
                            });
                        }
                    }
                    opcao = 0;
                }
            }
        }while (opcao != 0);

    }

    private static void exibirCarrinho(List<Item> itens){
        System.out.println("\n ******** Seu carrinho **********");
        totalPedido = 0;
        int count = 0;
        for(int i =0; i < itens.size(); i++){
            String nome = itens.get(i).getProduto().getNome();
            String precoUnitario = NumberFormat.getCurrencyInstance().format(itens.get(i).getProduto().getValor());
            int MAX = 25;
            if (nome.length() <= MAX){
                for (int j = nome.length(); j < MAX; j++) {
                    nome += " ";
                }
            }
            if(precoUnitario.length() <= MAX){
                for (int j = precoUnitario.length(); j < MAX-5; j++) {
                    precoUnitario += " ";
                }
            }
            System.out.println(
                    "item: " + i +
                            "\tProduto: " + nome +
                            "\tValor unidade = " +  precoUnitario +
                            "\t\tQuantidade = " + itens.get(i).getQuantidade() +
                            "\t\tTotalItem = " + (NumberFormat.getCurrencyInstance().format(itens.get(i).getQuantidade()*itens.get(i).getProduto().getValor()))
            );
            totalPedido += itens.get(i).getQuantidade()*itens.get(i).getProduto().getValor();
        }
        System.out.println("*************************************\n" + "TOTAL DO PEDIDO = " + NumberFormat.getCurrencyInstance().format(totalPedido));
    }


    private static void baixarQuantidade(Item item){
        Produto produto = item.getProduto();
        produto.setQuantidade(produto.getQuantidade() - item.getQuantidade());
        produtoDAO.update(produto);
    }

    private static void voltarEstoque(Item item){
        Produto produto = item.getProduto();
        produto.setQuantidade(produto.getQuantidade() + item.getQuantidade());
        produtoDAO.update(produto);
    }
}