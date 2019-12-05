package br.edu.ifsul.dao;

import br.edu.ifsul.model.Pedido;
import br.edu.ifsul.model.Item;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO extends BaseDAO {

    public List<Pedido> getPedidos(){
        List<Pedido> pedidos = new ArrayList<>();
        try {
            Connection conn = getConnection();
            String sql = "SELECT * FROM pedidos";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                pedidos.add(resultsetToPedido(rs));
            }
            rs.close();
            stmt.close();
            conn.close();
            return pedidos;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Pedido> getPedidosBySituacao(boolean situacao){
        List<Pedido> pedidos = new ArrayList<>();
        try {
            Connection conn = getConnection();
            String sql = "SELECT * FROM pedidos WHERE situacao=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setBoolean(1, situacao);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                pedidos.add(resultsetToPedido(rs));
            }
            rs.close();
            stmt.close();
            conn.close();
            return pedidos;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Pedido> getPedidosByEstado(String estado){
        List<Pedido> pedidos = new ArrayList<>();
        try {
            Connection conn = getConnection();
            String sql = "SELECT * FROM pedidos WHERE estado=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, estado);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                pedidos.add(resultsetToPedido(rs));
            }
            rs.close();
            stmt.close();
            conn.close();
            return pedidos;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Pedido getPedidoById(int id){
        try {
            Pedido pedido = null;
            Connection conn = getConnection();
            String sql = "SELECT * FROM pedidos WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                pedido = resultsetToPedido(rs);
            }
            rs.close();
            stmt.close();
            conn.close();
            return pedido;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean insert (Pedido pedido){
        Connection conn = null;
        PreparedStatement stmt;
        ResultSet rs = null;
        String sql;
        try{
            conn = getConnection();
            /*
                Inicia a transação, desligando o autocommit.
             */
            conn.setAutoCommit(false);
            sql = "INSERT INTO pedidos (pagamento, estado, data_criacao, data_modificacao, id_cliente, total_pedido, situacao) VALUES (?, ?, ?, ?, ?, ?, ?);";
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, pedido.getFormaPagamento());
            stmt.setString(2, pedido.getEstado());
            stmt.setDate(3, new Date(new java.util.Date().getTime()));
            stmt.setDate(4, new Date(new java.util.Date().getTime()));
            stmt.setLong(5, pedido.getCliente().getId());
            stmt.setDouble(6, pedido.getTotalPedido());
            stmt.setBoolean(7, true);
            int count = stmt.executeUpdate();
            long id = 0L;
            if (count > 0) { //se inseriu o pedido na tabela pedidos, pega o id e salva os itens
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    id = rs.getLong(1);
                }
                rs.close(); //libera o objeto
                /*
                    Realiza a operação de inserção na tabela itens.
                 */
                for(Item i : pedido.getItens()){
                    sql = "INSERT INTO itens (id_produto, id_pedido, quantidade, total_item, situacao) VALUES (?, ?, ?, ?, ?)";
                    stmt = conn.prepareStatement(sql);
                    stmt.setLong(1, i.getProduto().getId());
                    stmt.setLong(2, id); //id foi gerado no insert anterior, da tabela pedidos
                    stmt.setInt(3, i.getQuantidade());
                    stmt.setDouble(4, i.getTotalItem());
                    stmt.setBoolean(5, true);
                    count = stmt.executeUpdate();
                }
            }
            conn.commit();
            conn.setAutoCommit(true);
            /*
                Fim da transação ao comitar. Religa o autocomite, assim outros comportamentos da classe
                ficam liberados para realizar as operações com o autocommit.
             */
            rs.close();
            stmt.close();
            conn.close();
            return count > 0;
        }catch (SQLException e){
            e.printStackTrace();
            try {
                /*
                    Se lançou uma exceção, seja ela ao atualizar a tabela pedidos ou itens um rollback
                    é realizado no banco, cancelando todas as operações (usa o conceito de transações).
                 */
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
                return false;
            }
            return false;
        }
    }

    public boolean update(Pedido pedido){
        try {
            Connection conn = getConnection();
            String sql = "UPDATE pedidos SET pagamento=?, estado=?, data_criacao=?, data_modificacao =?, situacao=?, id_cliente =?, total_pedido =?, notaFiscal=? WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, pedido.getFormaPagamento());
            stmt.setString(2, pedido.getEstado());
            stmt.setDate(3, pedido.getDataCriacao());
            stmt.setDate(4,pedido.getDataModificacao());
            stmt.setBoolean(5,pedido.getSituacao());
            stmt.setLong(6,pedido.getCliente().getId());
            stmt.setDouble(7, pedido.getTotalPedido());
            stmt.setInt(8, pedido.getNotaFiscal());
            int count = stmt.executeUpdate();
            stmt.close();
            conn.close();
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkout (Pedido pedido){
        try {
            Connection conn = getConnection();
            String sql = "UPDATE pedidos SET estado=?, notaFiscal=? WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "faturado");
            stmt.setInt(2, pedido.getId());
            stmt.setInt(3, pedido.getId());
            int count = stmt.executeUpdate();
            stmt.close();
            conn.close();
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    //Exclusão de um item do pedido ainda não persistido


    //softdelete de um item de um pedido já persistido
    public boolean softDelete(int id_pedido, int id_produto, boolean situacao){
        try {
            Connection conn = getConnection();
            String sql = "UPDATE itens SET situacao=? WHERE id_produto=? and id_pedido=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id_pedido);
            stmt.setInt(2, id_produto);
            stmt.setBoolean(3, situacao);
            int count = stmt.executeUpdate();
            stmt.close();
            conn.close();
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    private Pedido resultsetToPedido(ResultSet rs) throws SQLException {
       Pedido p = new Pedido();
       ClienteDAO cliente = new ClienteDAO();
       ItemDAO item = new ItemDAO();
        p.setId(rs.getInt("id"));
        p.setFormaPagamento(rs.getString("pagamento"));
        p.setEstado(rs.getString("estado"));
        p.setDataCriacao(rs.getDate("data_criacao"));
        p.setDataModificacao(rs.getDate("data_modificacao"));
        p.setSituacao(rs.getBoolean("situacao"));
        p.setCliente(cliente.getClienteById(rs.getLong("id_cliente")));
        p.setTotalPedido(rs.getDouble("total_pedido"));
        p.setNotaFiscal(rs.getInt("notaFiscal"));
        p.setItens(item.getItensByPedido(p));
        return p;
    }

    public static void main(String[] args) {
        PedidoDAO pedidoDAO = new PedidoDAO();
        System.out.println(pedidoDAO.softDelete((int) 3L, (int) 3L, false));
    }

}
