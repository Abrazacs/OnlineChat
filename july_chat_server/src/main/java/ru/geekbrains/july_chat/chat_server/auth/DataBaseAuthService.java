package ru.geekbrains.july_chat.chat_server.auth;

import ru.geekbrains.july_chat.chat_server.error.BadRequestException;
import ru.geekbrains.july_chat.chat_server.error.UserNotFoundException;

import java.sql.*;

public class DataBaseAuthService implements AuthService{

    private static Connection connection;
    private static Statement statement;

    @Override
    public void start() {
        try {
            this.connection = DriverManager.getConnection("jdbc:sqlite:db/users.db");
            this.statement = connection.createStatement();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        try {
            this.statement.close();
            this.connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public String getNicknameByLoginAndPassword(String login, String password){
        start();
        try {
            ResultSet result = this.statement.executeQuery("SELECT nickname FROM users WHERE login = \'"+login+"\' AND password = \'"+password+"\'");
            String nickName = result.getString("nickname");
            stop();
            return nickName;
        }catch (SQLException RSClosed){
            throw new UserNotFoundException("User not found");
        }finally {
            stop();
        }
    }

    @Override
    public String changeNickname(String oldNick, String newNick){
        start();
        try{
            this.statement.executeUpdate("UPDATE users SET nickname = \'" + newNick + "\' WHERE nickname = \'" + oldNick+"\'");
             stop();
            return newNick;
        }catch (SQLException e){
            throw new BadRequestException("This nick busy");
        } finally {
            stop();
        }
    }
    

    @Override
    public void changePassword(String nickname, String oldPassword, String newPassword) {
        start();
        try {
        this.statement.executeUpdate("UPDATE users SET password = \'"+ newPassword+"\' WHERE nickname = \'" + nickname+"\' AND password = \'"+ oldPassword+"\'");
            stop();
            return;
        } catch (SQLException e){
            throw new UserNotFoundException("User not found");
        }finally {
            stop();
        }
    }


    @Override
    public void createNewUser(String login, String password, String nickname){
        start();
        try{
            this.statement.executeUpdate("INSERT INTO users (login, password, nickname) VALUES (\'"+login+"\', \'"+password+"\', \'"+nickname+"\'");
            stop();
            return;
        }catch (SQLException e){
            throw new BadRequestException("This nick or login busy");
        }finally {
            stop();
        }
    }

    @Override
    public void deleteUser(String nickname) {
        start();
        try{
            this.statement.executeUpdate("DELETE FROM users WHERE nickname = \'"+nickname+"\'");
            stop();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            stop();
        }

    }
}

