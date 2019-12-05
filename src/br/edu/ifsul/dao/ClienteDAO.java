package br.edu.ifsul.dao;

import br.edu.ifsul.model.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO extends BaseDAO{

    public List<Cliente> getClientes(){
        List<Cliente> clientes = new ArrayList<>();
        try {
            Connection conn = getConnection();
            String sql = "SELECT * FROM clientes";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                clientes.add(resultsetToCliente(rs));
            }
            rs.close();
            stmt.close();
            conn.close();
            return clientes;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Cliente> getClientesBySituacao(boolean situacao){
        List<Cliente> clientes = new ArrayList<>();
        try {
            Connection conn = getConnection();
            String sql = "SELECT * FROM clientes WHERE situacao=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setBoolean(1, situacao);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                clientes.add(resultsetToCliente(rs));
            }
            rs.close();
            stmt.close();
            conn.close();
            return clientes;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Cliente> getClientesByName(String nome){
        List<Cliente> clientes = new ArrayList<>();
        try {
            Connection conn = getConnection();
            String sql = "SELECT * FROM clientes WHERE nome LIKE ? ORDER BY nome";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome.toLowerCase() + "%");
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                clientes.add(resultsetToCliente(rs));
            }
            rs.close();
            stmt.close();
            conn.close();
            return clientes;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Cliente getClienteById(Long id){
        try {
            Cliente cliente = null;
            Connection conn = getConnection();
            String sql = "SELECT * FROM clientes WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                cliente = resultsetToCliente(rs);
            }
            rs.close();
            stmt.close();
            conn.close();
            return cliente;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean insert(Cliente cliente){
        try {
            Connection conn = getConnection();
            String sql = "INSERT INTO clientes (nome, sobrenome, situacao) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getSobrenome());
            stmt.setBoolean(3, cliente.isSituação());
            int count = stmt.executeUpdate();
            stmt.close();
            conn.close();
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Cliente cliente){
        try {
            Connection conn = getConnection();
            String sql = "UPDATE clientes SET nome=?, sobrenome=?, situacao=? WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getSobrenome());
            stmt.setBoolean(3, cliente.isSituação());
            stmt.setLong(4, cliente.getId());
            int count = stmt.executeUpdate();
            stmt.close();
            conn.close();
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean softDelete(long id, boolean situacao){
        try {
            Connection conn = getConnection();
            String sql = "UPDATE clientes SET situacao=? WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setBoolean(1, situacao);
            stmt.setLong(2, id);
            int count = stmt.executeUpdate();
            stmt.close();
            conn.close();
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //método utilitário, converte ResultSet na classe de modelo (nesse caso, Cliente)
    private Cliente resultsetToCliente(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setId(rs.getLong("id"));
        c.setNome(rs.getString("nome"));
        c.setSobrenome(rs.getString("sobrenome"));
        c.setSituação(rs.getBoolean("situacao"));
        //c.setPedidos(pedidoDAO.getPedidosByIdCliente(c.getId())); //desenvolver
        return c;
    }

    //método utilizado para testar esta camada do software (isto é, as operações dessa classe)
    public static void main(String[] args) {
        ClienteDAO clienteDAO = new ClienteDAO();

        //Cliente cliente = new Cliente(3L, "João", "Vitório", true);
        System.out.println(clienteDAO.softDelete(3L, false));
    }

}
