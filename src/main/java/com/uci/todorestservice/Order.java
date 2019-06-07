/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uci.todorestservice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.uci.todorestservice.db.DatabaseConnector;

/**
 *
 * @author davie
 */

@Path("order")
public class Order {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of Order
     */
    public Order() {
    }
    
    public Boolean checkExists(String itemid){
        try{
            Connection connection;
            connection = (new DatabaseConnector()).getConnection();
            String query = "SELECT EXISTS(SELECT * FROM orders WHERE oid = ? ) as checked";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setInt(1, Integer.parseInt(itemid));
                    //here sonoo is database name, root is username and password  
            ResultSet rs = preparedStmt.executeQuery(); 
            Boolean exists = false;
            while(rs.next()) {
                exists = rs.getBoolean("checked");
            }
            return exists;
        }catch(Exception e){
            return false;
        } 
    }

    @GET
    public Response getXml() {
        //TODO return proper representation object
        try{  
                JSONObject mainobj = new JSONObject();
                
                Connection connection;
                connection = (new DatabaseConnector()).getConnection();
                Statement stmt=connection.createStatement();    
                //here sonoo is database name, root is username and password  
                ResultSet rs=stmt.executeQuery("select * from orders");  
                while(rs.next()) { 
                    JSONObject obj = new JSONObject();
                    obj.put("pid", rs.getString("pid"));
                    obj.put("cid", rs.getString("cid"));
                    obj.put("quantity", rs.getString("quantity"));
                    obj.put("firstname", rs.getString("firstname"));
                    obj.put("lastname", rs.getString("lastname"));
                    obj.put("suffix", rs.getString("suffix"));
                    obj.put("phone_number", rs.getString("phone_number"));
                    obj.put("shipping_address", rs.getString("shipping_address"));
                    obj.put("credit_card_number", rs.getString("credit_card_number"));
                    obj.put("card_verification_value", rs.getString("card_verification_value"));
                    obj.put("billing_address", rs.getString("billing_address"));
                    obj.put("time_ordered", rs.getString("time_ordered"));
                    obj.put("cost_in_cents", rs.getString("cost_in_cents"));
                    obj.put("delivery_method", rs.getString("delivery_method"));
                    obj.put("price_in_cents", rs.getString("price_in_cents"));
                    mainobj.put(rs.getString("oid"), obj);
                }
                return Response.ok(mainobj.toString()).build();
            }catch(Exception e){ 
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
    }
    
    @POST
    public Response postreq(String data) {
        try{
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(data);
            
            String pid = (String) jsonObject.get("pid");
            String cid = (String) jsonObject.get("cid");
            String quantity = (String) jsonObject.get("quantity");
            String firstname = (String) jsonObject.get("firstname");
            String lastname = (String) jsonObject.get("lastname");
            String suffix = (String) jsonObject.get("suffix");
            String phone = (String) jsonObject.get("phone_number");
            String shipping = (String) jsonObject.get("shipping_address");
            String card = (String) jsonObject.get("credit_card_number");
            String cvv = (String) jsonObject.get("card_verification_value");
            String billing = (String) jsonObject.get("billing_address");
            String cost = (String) jsonObject.get("cost_in_cents");
            String delivery = (String) jsonObject.get("delivery_method");
            String price = (String) jsonObject.get("price_in_cents");

            try{
                Connection connection;
                connection = (new DatabaseConnector()).getConnection();
                String query = "INSERT Into orders(pid,cid, quantity, firstname, lastname, suffix, phone_number, shipping_address, credit_card_number, card_verification_value, billing_address, time_ordered, cost_in_cents, delivery_method, price_in_cents) "
                        + "VALUES (?,?,?,?,?,?,?,?,?,?,?, CURRENT_TIMESTAMP, ?, ?, ?)";
                PreparedStatement preparedStmt = connection.prepareStatement(query);
                preparedStmt.setInt(1, Integer.parseInt(pid));
                preparedStmt.setInt(2, Integer.parseInt(cid));
                preparedStmt.setInt(3, Integer.parseInt(quantity));
                preparedStmt.setString(4, firstname);
                preparedStmt.setString(5, lastname);
                preparedStmt.setString(6, suffix);
                preparedStmt.setString(7, phone);
                preparedStmt.setString(8, shipping);
                preparedStmt.setString(9, card);
                preparedStmt.setString(10, cvv);
                preparedStmt.setString(11, billing);
                preparedStmt.setInt(12, Integer.parseInt(cost));
                preparedStmt.setString(13, delivery);
                preparedStmt.setInt(14, Integer.parseInt(price));

                preparedStmt.executeUpdate();
            return Response.status(200).build();
            } catch(Exception e) {
            return Response.status(500).build();
            }
        } catch(Exception e) {
            return Response.status(500).build();
        }
    } 
    
    @DELETE
    @Path("{itemid}")
    public Response deleteContainer(@PathParam("itemid") String itemid) {
        if(!checkExists(itemid)){
            return Response.status(404).build();
        }
        try{
            Connection connection;
            connection = (new DatabaseConnector()).getConnection();
            String query = "DELETE FROM orders WHERE oid = ?";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString(1, itemid);
            preparedStmt.executeUpdate();
            return Response.status(200).build();
            
        } catch(Exception e) {
            return Response.status(500).build();
        }
    } 
    
    @GET
    @Path("{itemid}")
    public Response getItemResource(@PathParam("itemid") String itemid) {
        if(!checkExists(itemid)){
            return Response.status(404).build();
        }
       try{  
       
                Connection connection;
                connection = (new DatabaseConnector()).getConnection();
                Statement stmt=connection.createStatement();    
                //here sonoo is database name, root is username and password  
                ResultSet rs=stmt.executeQuery("select * from orders where oid = " + itemid);  
                JSONObject obj = new JSONObject();
                while(rs.next()) { 
                    obj.put("pid", rs.getString("pid"));
                    obj.put("cid", rs.getString("cid"));
                    obj.put("quantity", rs.getString("quantity"));
                    obj.put("firstname", rs.getString("firstname"));
                    obj.put("lastname", rs.getString("lastname"));
                    obj.put("suffix", rs.getString("suffix"));
                    obj.put("phone_number", rs.getString("phone_number"));
                    obj.put("shipping_address", rs.getString("shipping_address"));
                    obj.put("credit_card_number", rs.getString("credit_card_number"));
                    obj.put("card_verification_value", rs.getString("card_verification_value"));
                    obj.put("billing_address", rs.getString("billing_address"));
                    obj.put("time_ordered", rs.getString("time_ordered"));
                    obj.put("cost_in_cents", rs.getString("cost_in_cents"));
                    obj.put("delivery_method", rs.getString("delivery_method"));
                    obj.put("price_in_cents", rs.getString("price_in_cents"));
                }
                connection.close();  
                
                return Response.ok(obj.toString()).build();
            }catch(Exception e){ 
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
    }
    
    @PUT
    @Path("{itemid}")
    public Response putXml(@PathParam("itemid") String itemid, String data) {
        if(!checkExists(itemid)){
            return Response.status(404).build();
        }
        try{
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(data);
            
            String pid = (String) jsonObject.get("pid");
            String cid = (String) jsonObject.get("cid");
            String quantity = (String) jsonObject.get("quantity");
            String firstname = (String) jsonObject.get("firstname");
            String lastname = (String) jsonObject.get("lastname");
            String suffix = (String) jsonObject.get("suffix");
            String phone = (String) jsonObject.get("phone_number");
            String shipping = (String) jsonObject.get("shipping_address");
            String card = (String) jsonObject.get("credit_card_number");
            String cvv = (String) jsonObject.get("card_verification_value");
            String billing = (String) jsonObject.get("billing_address");
            String cost = (String) jsonObject.get("cost_in_cents");
            String delivery = (String) jsonObject.get("delivery_method");
            String price = (String) jsonObject.get("price_in_cents");

            try{
                Connection connection;
                connection = (new DatabaseConnector()).getConnection();
                String query = "Update orders set pid = ?, cid = ?, quantity = ?, firstname = ?, lastname = ?, suffix = ?, phone_number = ?, shipping_address = ?, credit_card_number = ?, card_verification_value = ?, billing_address = ?, time_ordered = CURRENT_TIMESTAMP, cost_in_cents = ?, delivery_method = ?, price_in_cents = ? WHERE oid = ?";
                    
                PreparedStatement preparedStmt = connection.prepareStatement(query);
                preparedStmt.setInt(1, Integer.parseInt(pid));
                preparedStmt.setInt(2, Integer.parseInt(cid));
                preparedStmt.setInt(3, Integer.parseInt(quantity));
                preparedStmt.setString(4, firstname);
                preparedStmt.setString(5, lastname);
                preparedStmt.setString(6, suffix);
                preparedStmt.setString(7, phone);
                preparedStmt.setString(8, shipping);
                preparedStmt.setString(9, card);
                preparedStmt.setString(10, cvv);
                preparedStmt.setString(11, billing);
                preparedStmt.setInt(12, Integer.parseInt(cost));
                preparedStmt.setString(13, delivery);
                preparedStmt.setInt(14, Integer.parseInt(price));
                preparedStmt.setInt(15, Integer.parseInt(itemid));
                
                preparedStmt.executeUpdate();
                return Response.status(200).build();
            } catch(Exception e) {
                return Response.status(500).build();
            }
        } catch(Exception e) {
            return Response.status(500).build();
        }
    }
}

