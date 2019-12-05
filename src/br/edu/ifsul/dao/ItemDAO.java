package br.edu.ifsul.dao;

import br.edu.ifsul.model.Item;
import br.edu.ifsul.model.Pedido;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO extends BaseDAO {
//remover nomedalista.remove.indice do dado
    //remover item do pedido
    public boolean deleteItem(int id_pedido, int id_produto){
        try {
            Connection conn = getConnection();
            String sql = "DELETE FROM pedidos WHERE id_produto=? and id_pedido=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id_pedido);
            stmt.setLong(2,id_produto);
            int count = stmt.executeUpdate();
            stmt.close();
            conn.close();
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    //softDelete de item de um pedido
    public boolean softDelete(int id_pedido, int id_produto, boolean situacao){
        try {
            Connection conn = getConnection();
            String sql = "UPDATE itens SET situacao=? WHERE id_produto=? and id_pedido=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setBoolean(1, situacao);
            stmt.setLong(2, id_pedido);
            stmt.setLong(3,id_produto);
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
       Item item = new Item();
        item.setId(rs.getLong("id"));
        item.setSituacao(rs.getBoolean("situacao"));

        return item;
    }
    public static void main(String[] args) {
        ItemDAO itemDAO= new ItemDAO();
        System.out.println(itemDAO.softDelete((int) 3L, (int) 3L,false));
    }
}
