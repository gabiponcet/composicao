package br.edu.ifsul.dao;

import br.edu.ifsul.model.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO extends BaseDAO {
    //insert
    public boolean insert(Produto produto){
        try {
            Connection conn = getConnection();
            String sql = "INSERT INTO produtos (nome, sobrenome, situacao, codigo) VALUES (?, ?, ?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setDouble(3,produto.getValor());
            stmt.setInt(4,produto.getQuantidade());
            stmt.setBoolean(5, produto.getSituacao());
            stmt.setLong(6, produto.getCodigo());
            int count = stmt.executeUpdate();
            stmt.close();
            conn.close();
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //read
    //busca tds produtos
    public List<Produto> getProdutos(){
        List<Produto> produtos = new ArrayList<>();
        try {
            Connection conn = getConnection();
            String sql = "SELECT * FROM produtos";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                produtos.add(resultsetToProduto(rs));
            }
            rs.close();
            stmt.close();
            conn.close();
            return produtos;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    // read por situacao
    public List<Produto> getProdutosBySituacao(boolean situacao){
        List<Produto> produtos = new ArrayList<>();
        try {
            Connection conn = getConnection();
            String sql = "SELECT * FROM produtos WHERE situacao=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setBoolean(1, situacao);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
               produtos.add(resultsetToProduto(rs));
            }
            rs.close();
            stmt.close();
            conn.close();
            return produtos;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    //read por codigo
    public List<Produto> getProdutosByCodigo(long codigo){
        List<Produto> produtos = new ArrayList<>();
        try {
            Connection conn = getConnection();
            String sql = "SELECT * FROM produtos WHERE codigo=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setLong(1, codigo);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                produtos.add(resultsetToProduto(rs));
            }
            rs.close();
            stmt.close();
            conn.close();
            return produtos;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // read por nome
    public List<Produto> getProdutosByName(String nome){
        List<Produto> produtos = new ArrayList<>();
        try {
            Connection conn = getConnection();
            String sql = "SELECT * FROM produtos WHERE nome LIKE ? ORDER BY nome";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome.toLowerCase() + "%");
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                produtos.add(resultsetToProduto(rs));
            }
            rs.close();
            stmt.close();
            conn.close();
            return produtos;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    //read por ID

    public Produto getProdutoById(Long id){
        try {
            Produto produto = null;
            Connection conn = getConnection();
            String sql = "SELECT * FROM produtos WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                produto = resultsetToProduto(rs);
            }
            rs.close();
            stmt.close();
            conn.close();
            return produto;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    //update
    public boolean update(Produto produto){
        try {
            Connection conn = getConnection();
            String sql = "UPDATE produtos SET nome=?, descricao=?, valor=?, quantidade= ?,situacao= ?, codigo=? WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setDouble(3,produto.getValor());
            stmt.setInt(4,produto.getQuantidade());
            stmt.setBoolean(5,produto.getSituacao());
            stmt.setInt(6,produto.getCodigo());
            stmt.setLong(7,produto.getId());
            int count = stmt.executeUpdate();
            stmt.close();
            conn.close();
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //delete
    public boolean softDelete(long id, boolean situacao){
        try {
            Connection conn = getConnection();
            String sql = "UPDATE produtos SET situacao=? WHERE id=?";
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

    //método utilitário
    //método utilitário, converte ResultSet na classe de modelo (nesse caso, Produto)
    private Produto resultsetToProduto(ResultSet rs) throws SQLException {
        Produto p = new Produto();
        p.setId(rs.getLong("id"));
        p.setNome(rs.getString("nome"));
        p.setDescricao(rs.getString("descricao"));
        p.setValor(rs.getDouble("valor"));
        p.setQuantidade(rs.getInt("quantidade"));
        p.setSituacao(rs.getBoolean("situacao"));

        return p;
    }

    //método utilizado para testar esta camada do software (isto é, as operações dessa classe)
    public static void main(String[] args) {
        ProdutoDAO produtoDAO = new ProdutoDAO();

        System.out.println(produtoDAO.softDelete(3L, false));
    }

}
