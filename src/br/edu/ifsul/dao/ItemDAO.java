package br.edu.ifsul.dao;

import br.edu.ifsul.model.Item;
import br.edu.ifsul.model.Pedido;
import br.edu.ifsul.model.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO extends BaseDAO {
    public List<Item> getItensByPedido(Pedido p){
        List<Item> itens = new ArrayList<>();
        try {
            Connection conn = getConnection();
            String sql = "SELECT * FROM itens WHERE id_pedido=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, p.getId());
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                itens.add(resultsetToItens(rs));
            }
            rs.close();
            stmt.close();
            conn.close();
            return itens;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    //softDelete de item de um pedido
    public boolean softDelete(int id, boolean situacao){
        try {
            Connection conn = getConnection();
            String sql = "UPDATE itens SET situacao=? WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            stmt.setBoolean(2, situacao);
            int count = stmt.executeUpdate();
            stmt.close();
            conn.close();
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    //listar itens de um pedido

    private Item resultsetToItens(ResultSet rs) throws SQLException{
        ProdutoDAO p = new ProdutoDAO();

        Item item = new Item();
        item.setId(rs.getLong("id"));
        item.setSituacao(rs.getBoolean("situacao"));
        item.setQuantidade(rs.getInt("quantidade"));
        item.setTotalItem(rs.getDouble("total_item"));
        item.setProduto(p.getProdutoById(rs.getLong("id_produto")));
        return item;
    }
    public static void main(String[] args) {
        ItemDAO itemDAO= new ItemDAO();
        System.out.println(itemDAO.softDelete((int) 3L, false));
    }
}
